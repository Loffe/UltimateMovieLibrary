package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import sun.misc.Cleaner;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class MovieSearchProvider {

    static private int key = 0;

    /**
     * Search for movies, call backs to the serachClient with the results
     * 
     * @param name
     * @param client
     * @param orderByColumn
     * @param ascending
     * @return
     */
    static public int searchByName(String name, MovieSearchClient client,
            String orderByColumn, boolean ascending) {
        int assignedKey = key;
        key++;
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending);
        return assignedKey;
    }

    /**
     * For now it just returns the number of random movies specified with
     * numberOfMovies param
     * 
     * @param numberOfMovies
     * @param client
     * @param orderByColumn
     * @param ascending
     * @return
     */
    static public int getFeaturedMovies(int numberOfMovies,
            MovieSearchClient client, String orderByColumn, boolean ascending) {
        int assignedKey = key;
        key++;
        getFeaturedMoviesAsync(numberOfMovies, assignedKey, client,
                orderByColumn, ascending);
        return assignedKey;
    }

    static private void searchByNameAsync(final String name,
            final int assignedKey, final MovieSearchClient client,
            final String orderByColumn, final boolean ascending) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dao<Movie, Integer> dbMovie;
                try {
                    dbMovie = DatabaseManager.getInstance().getMovieDao();
                    QueryBuilder<Movie, Integer> queryBuilder = dbMovie
                            .queryBuilder();

                    // select any movie that begins with the name string and is
                    // disc 1
                    queryBuilder.where().like("name", name + "%").and()
                            .eq("discnumber", 1);
                    queryBuilder.orderBy(orderByColumn, ascending);

                    PreparedQuery<Movie> preparedQuery = queryBuilder.prepare();
                    List<Movie> movies = dbMovie.query(preparedQuery);
                    client.searchFinished(movies, assignedKey);
                } catch (SQLException e) {
                    System.out.println("error searching for movies");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static private void getFeaturedMoviesAsync(final int numberOfMovies,
            final int assignedKey, final MovieSearchClient client,
            final String orderByColumn, final boolean ascending) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dao<Movie, Integer> dbMovie;
                try {
                    // TODO nicer random function, maybe get random objects from
                    // the database in the first place... how?
                    dbMovie = DatabaseManager.getInstance().getMovieDao();
                    List<Movie> movies = dbMovie.queryForAll();
                    List<Movie> randomMovies = new ArrayList<Movie>();
                    Random randomGenerator = new Random();
                    for (int i = 0; i < numberOfMovies; i++)
                        randomMovies.add(movies.get(randomGenerator
                                .nextInt(movies.size() - 1)));

                    client.searchFinished(randomMovies, assignedKey);
                } catch (SQLException e) {
                    System.out.println("error searching for movies");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
