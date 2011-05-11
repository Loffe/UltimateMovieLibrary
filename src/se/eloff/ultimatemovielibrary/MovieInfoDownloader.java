package se.eloff.ultimatemovielibrary;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import net.sf.jtmdb.GeneralSettings;
import net.sf.jtmdb.Movie;

import org.json.JSONException;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class MovieInfoDownloader {
    private static MovieInfoDownloader instance;
    private static boolean updateInProgress = false;
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

    private void updateLibraryInfoAsync() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out
                            .println("starting new thread for updateLibraryInfoAsync");
                    Dao<LocalMovie, Integer> dbMovie = DatabaseManager
                            .getInstance().getMovieDao();
                    QueryBuilder<LocalMovie, Integer> queryBuilder = dbMovie
                            .queryBuilder();

                    // fetch all movies without info
                    Where<LocalMovie, Integer> where = queryBuilder.where();
                    where.isNull("info_id").and().eq("discnumber", 1);

                    List<LocalMovie> movies = dbMovie.query(queryBuilder
                            .prepare());

                    for (LocalMovie localMovie : movies) {
                        fetchMovieInfo(localMovie);
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (doNewScan) {
                    doNewScan = false;
                    updateLibraryInfoAsync();
                }
                updateInProgress = false;

                System.out.println("Done updateLibraryInfoAsync");

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
        // TODO: Download the info, but first set the API Key in constructor.
        System.out.println("Fetching info for " + localMovie.getName());
        List<Movie> reducedMovies = null;
        try {
            // Search for the Movie
            reducedMovies = Movie.search(localMovie.getName());
            if (reducedMovies != null) {
                // Use the first movie from the search hits
                Movie movie = reducedMovies.get(0);
                System.out.println("Got reduced info for movie with ID="
                        + movie.getID());
                // Get the complete info of the movie (currently only has
                // reduced info like ID, Name etc)
                System.out.println("Fetching all info for " + movie.getName());
                movie = Movie.getInfo(movie.getID());

                // Translate the overview (plot?!)
                String plot = movie.getOverview();
                String translatedPlot = Localization.translationFailedText
                        + " " + plot;
                try {
                    translatedPlot = Translate.execute(plot, Language.ENGLISH,
                            Language.SWEDISH);
                } catch (Exception e) {
                    // Translation failed.
                    System.out.println("Plot translation failed!");
                }

                // Save the info
                Dao<MovieInfo, Integer> dbInfo = DatabaseManager.getInstance()
                        .getMovieInfoDao();

                MovieInfo info = new MovieInfo("bk", "director", "cover",
                        translatedPlot, "genres", 5);

                dbInfo.create(info);

                Dao<LocalMovie, Integer> dbMovie = DatabaseManager
                        .getInstance().getMovieDao();

                localMovie.setInfo_id(info);
                dbMovie.update(localMovie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
