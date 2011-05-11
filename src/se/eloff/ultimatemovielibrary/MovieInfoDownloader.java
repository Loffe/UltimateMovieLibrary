package se.eloff.ultimatemovielibrary;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import net.sf.jtmdb.Auth;
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

    private void fetchMovieInfo(LocalMovie movie) {
        // TODO: download the info
        System.out.println("Fetching info for "+movie.getName());
        List<Movie> movies = null;
        try {
            String token = Auth.getToken();
            GeneralSettings.setApiKey(TMDBAPIKey.APIKey);
            movies = Movie.search(movie.getName());
            System.out.println(movies);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (movies != null) {
            for (Movie m : movies) {
                System.out.println(m.getName());
            }
        }
        
        
        // TODO: modify translation, just testing atm...
        // This is just to help Google identify how their translation tools are used
        Translate.setHttpReferrer("https://github.com/Loffe/UltimateMovieLibrary");

        String plot = "In the end of the movie, the main actor goes rambo!";
        String translatedText = plot + " (translation failed...)";
        try {
            translatedText = Translate.execute(plot,
                    Language.ENGLISH, Language.SWEDISH);
        } catch (Exception e) {
            System.out.println("The translation failed...");
            e.printStackTrace();
        }

        System.out.println(translatedText);

        MovieInfo info = new MovieInfo("bk", "director", "cover", "plot",
                "genres", 5);

        try {
            System.out.println("saving movie with info");
            Dao<MovieInfo, Integer> dbInfo = DatabaseManager.getInstance()
                    .getMovieInfoDao();

            dbInfo.create(info);

            Dao<LocalMovie, Integer> dbMovie = DatabaseManager.getInstance()
                    .getMovieDao();

            movie.setInfo_id(info);
            dbMovie.update(movie);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
