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
    public static boolean updateInProgress = false;
    private static boolean doNewScan = false;

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
        if (!updateInProgress) {
            updateInProgress = true;
            updateLibraryInfoAsync();
        } else
            doNewScan = true;
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
                            .println("starting new thread for updateLibraryInfoAsync");
                    Localization.loadingTextLabel.setText("Updating movie info...");
                    Localization.loadingLabel.setVisible(true);

                    
                    Dao<LocalMovie, Integer> dbMovie = DatabaseManager
                            .getInstance().getMovieDao();
                    QueryBuilder<LocalMovie, Integer> queryBuilder = dbMovie
                            .queryBuilder();

                    // fetch all movies without info
                    Where<LocalMovie, Integer> where = queryBuilder.where();
                    where.eq("info_id", -1).and().eq("discnumber", 1);

                    List<LocalMovie> movies = dbMovie.query(queryBuilder
                            .prepare());

                    for (LocalMovie localMovie : movies) {
                        fetchMovieInfo(localMovie);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (doNewScan) {
                    doNewScan = false;
                    updateLibraryInfoAsync();
                }
                updateInProgress = false;

                System.out.println("Done updateLibraryInfoAsync");
                Localization.loadingTextLabel.setText("");
                Localization.loadingLabel.setVisible(false);

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
        String yearString = "";
        if (localMovie.getYear() != 0)
            yearString = Integer.toString(localMovie.getYear());

        System.out.println("Trying to fetch info for " + localMovie.getName()
                + " " + yearString);
        List<Movie> reducedMovies = null;
        try {
            // Search for the Movie
            reducedMovies = Movie.search(localMovie.getName());
            if (reducedMovies != null && reducedMovies.size() > 0) {
                // Use the first movie from the search hits
                Movie movie = reducedMovies.get(0);
                System.out.println("Got reduced info for movie with ID="
                        + movie.getID());
                // Get the complete info of the movie (currently only has
                // reduced info like ID, Name etc)
                System.out.println("Fetching all info for " + movie.getName());
                movie = Movie.getInfo(movie.getID());

                String thumbPath = "thumbs/" + movie.getID() + ".jpg";

                // get the thumb
                MovieImages images = Movie.getImages(movie.getID());
                if (!images.posters.isEmpty()) {
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
                String translatedPlot = Localization.translationFailedText
                        + " " + plot;
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
                Dao<MovieInfo, Integer> dbInfo = DatabaseManager.getInstance()
                        .getMovieInfoDao();
                MovieInfo info = new MovieInfo(cast, directors, thumbPath,
                        translatedPlot, genres, (int) (movie.getRating() * 10));

                dbInfo.create(info);

                // update the movie
                Dao<LocalMovie, Integer> dbMovie = DatabaseManager
                        .getInstance().getMovieDao();

                localMovie.setName(movie.getName());
                localMovie.setYear(movie.getReleasedDate().getYear() + 1900);
                localMovie.setInfo_id(info.getId());
                dbMovie.update(localMovie);
            } else {
                System.out.println("No movie info found for "
                        + localMovie.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean downloadImage(URL url, String imagePath) {
        try {
            FileUtils.copyURLToFile(url, new File(imagePath));
        } catch (IOException e) {
            System.out.println("krashing when trying to download image");
            e.printStackTrace();
        }
        return true;
    }
}
