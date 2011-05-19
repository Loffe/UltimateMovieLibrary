package se.eloff.ultimatemovielibrary;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import net.sf.jtmdb.CastInfo;
import net.sf.jtmdb.GeneralSettings;
import net.sf.jtmdb.Genre;
import net.sf.jtmdb.Movie;
import net.sf.jtmdb.MovieImages;
import net.sf.jtmdb.MoviePoster;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class MovieInfoDownloader {

    private static MovieInfoDownloader instance;

    private static boolean updateInProgress = false;

    public MovieInfoDownloader() {
        // Set the API Key in order
        GeneralSettings.setApiKey(TMDBAPIKey.APIKey);

        // Prepare to translate plot. Google requires a HttpReferrer.
        // Google wants to identify how their translation tools are used
        // FRA, 1984 etc. Warning! WARNING! lol
        Translate
                .setHttpReferrer("https://github.com/Loffe/UltimateMovieLibrary");
    }

    public static MovieInfoDownloader getInstance() {
        if (instance == null)
            instance = new MovieInfoDownloader();
        return instance;
    }

    public void updateLibraryInfo() {
        System.out.println("starting updateLibraryInfo()");
        if (!isUpdateInProgress()) {
            updateInProgress = true;
            updateLibraryInfoAsync();
        }
    }

    // call when you want to display the info panel but info is not fetched yet
    // TODO make it async with a callback to stop swing from locking up
    public void updateInfo(LocalMovie localMovie) {
        fetchMovieInfo(localMovie);
    }

    private void updateLibraryInfoAsync() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    System.out
                            .println("Thread started! updateLibraryInfoAsync");

                    Dao<LocalMovie, Integer> dbMovie = DatabaseManager
                            .getInstance().getMovieDao();
                    QueryBuilder<LocalMovie, Integer> queryBuilder = dbMovie
                            .queryBuilder();

                    // fetch all movies without info
                    Where<LocalMovie, Integer> where = queryBuilder.where();
                    where.eq("info_id", -1).and().eq("discnumber", 1);

                    List<LocalMovie> movies = dbMovie.query(queryBuilder
                            .prepare());
                    int i = 1;
                    for (LocalMovie localMovie : movies) {
                        Localization.loadingTextLabel
                                .setText(Localization.updatingMoviesInfoText
                                        + " " + i++ + " av " + movies.size()
                                        + " ...");
                        fetchMovieInfo(localMovie);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                updateInProgress = false;

                // Hide the loadingLabels
                if (!WatchFolderManager.isScanInProgress()) {
                    Localization.loadingTextLabel.setText("");
                    Localization.loadingLabel.setVisible(false);
                }

                System.out.println("Thread done! updateLibraryInfoAsync");
            }

        }).start();
    }

    /**
     * Fetch TMDB Movie info for the specified LocalMovie.
     * 
     * @param localMovie
     *            the movie to get info for.
     */
    private void fetchMovieInfo(LocalMovie localMovie) {
        System.out.println("Search (name=" + localMovie.getName() + ")");
        List<Movie> reducedMovies = null;
        // Search for the Movie
        try {
            reducedMovies = Movie.search(localMovie.getName());
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (reducedMovies != null && reducedMovies.size() > 0) {
            // Use the first movie from the search hits
            Movie movie = reducedMovies.get(0);

            // Or... try to find a movie with the correct year.
            for (Movie reducedMovie : reducedMovies) {
                // Just pick the first one here as well...
                if (reducedMovie.getReleasedDate() != null
                        && (reducedMovie.getReleasedDate().getYear() + 1900 == localMovie
                                .getYear())) {
                    // Replace the movie with this one, since it's probably
                    // more correct
                    movie = reducedMovie;
                    // We're done!
                    break;
                }
            }
            System.out.println(" - Found reduced info (id=" + movie.getID()
                    + ")");
            // Get the complete info of the movie (currently only has
            // reduced info like ID, Name etc)
            System.out.println(" - Fetching all info");
            try {
                movie = Movie.getInfo(movie.getID());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            String thumbPath = "thumbs/" + movie.getID() + ".jpg";

            // get the thumb
            MovieImages images = null;
            try {
                images = Movie.getImages(movie.getID());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            if (images != null && !images.posters.isEmpty()) {
                Iterator<MoviePoster> poster = images.posters.iterator();
                if (poster.hasNext())
                    if (!downloadImage(poster.next().getLargestImage(),
                            thumbPath))
                        thumbPath = "";
            }

            // extract info
            String genres = "";
            String cast = "";
            String directors = "";
            String plot = movie.getOverview();

            // Translate the overview (plot?!)
            String translatedPlot = Localization.translationFailedText + " "
                    + plot;
            try {
                translatedPlot = Translate.execute(plot, Language.ENGLISH,
                        Language.SWEDISH);
                if (translatedPlot.toLowerCase().contains(
                        "ingen överblick hittades")) {
                    translatedPlot = Localization.unknownPlotText;
                }
            } catch (Exception e) {
                // Translation failed.
                System.out.println("Plot translation failed!");
            }

            for (Genre genre : movie.getGenres())
                genres += genre.getName() + ", ";

            for (CastInfo castInfo : movie.getCast()) {
                if (castInfo.getJob().toLowerCase().equals("director"))
                    directors += castInfo.getName() + ", ";
                else
                    cast += castInfo.getName() + ", ";
            }

            if (!genres.isEmpty())
                genres = genres.substring(0, genres.length() - 2);
            if (!cast.isEmpty())
                cast = cast.substring(0, cast.length() - 2);
            if (!directors.isEmpty())
                directors = directors.substring(0, directors.length() - 2);

            // Save the info
            Dao<MovieInfo, Integer> dbInfo;
            try {
                dbInfo = DatabaseManager.getInstance().getMovieInfoDao();

                MovieInfo info = new MovieInfo(cast, directors, thumbPath,
                        translatedPlot, genres, Math.round((float) movie
                                .getRating() / 2), localMovie.getId());

                dbInfo.create(info);

                // update the movie
                Dao<LocalMovie, Integer> dbMovie = DatabaseManager
                        .getInstance().getMovieDao();

                if (localMovie.getRating() == 0)
                    localMovie
                            .setRating(Math.round((float) movie.getRating() / 2));
                localMovie.setName(movie.getName());
                localMovie.setYear(movie.getReleasedDate().getYear() + 1900);
                localMovie.setInfo_id(info.getId());
                dbMovie.update(localMovie);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println(" - No info found!");
        }

    }

    public boolean downloadImage(URL url, String imagePath) {
        try {
            FileUtils.copyURLToFile(url, new File(imagePath));
        } catch (IOException e) {
            System.out.println("Crashed while trying to download image");
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isUpdateInProgress() {
        return updateInProgress;
    }
}
