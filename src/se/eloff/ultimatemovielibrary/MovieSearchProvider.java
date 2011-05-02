package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

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
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending, false, false, false, false, false, false);
        return assignedKey;
    }

    static public int searchByNameSeen(String name, MovieSearchClient client,
            String orderByColumn, boolean ascending, boolean seen) {
        int assignedKey = key;
        key++;
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending,
                true, seen, false, false, false, false);
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
            final String orderByColumn, final boolean ascending,
            boolean useSeen, boolean seen, boolean useWish, boolean wish,
            boolean useFavorite, boolean favorite) {
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
                    queryBuilder.where().like("name", "%" + name + "%").and()
                            .eq("discnumber", 1);
                    queryBuilder.orderBy(orderByColumn, ascending);

                    List<Movie> movies = dbMovie.query(queryBuilder.prepare());
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
                    QueryBuilder<Movie, Integer> queryBuilder = dbMovie
                            .queryBuilder();

                    // select all movies that has discnumber 1
                    queryBuilder.where().eq("discnumber", 1);
                    queryBuilder.orderBy(orderByColumn, ascending);
                    List<Movie> movies = dbMovie.query(queryBuilder.prepare());

                    // Don't crash due to 0 movies
                    if (movies.size() > numberOfMovies) {
                        List<Movie> randomMovies = new ArrayList<Movie>();
                        Random randomGenerator = new Random();
                        for (int i = 0; i < numberOfMovies;) {
                            Movie movie = movies.get(randomGenerator
                                    .nextInt(movies.size() - 1));
                            // Only add unique movies, if its already in there,
                            // try again
                            if (!randomMovies.contains(movie)) {
                                randomMovies.add(movie);
                                i++;
                            }
                        }
                        // TODO sort the list based on the orderByColumn value
                        client.searchFinished(randomMovies, assignedKey);
                    } else
                        // if the database contains less then the requested
                        // number of movies, return all we got
                        client.searchFinished(movies, assignedKey);

                } catch (SQLException e) {
                    System.out.println("error searching for movies");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
