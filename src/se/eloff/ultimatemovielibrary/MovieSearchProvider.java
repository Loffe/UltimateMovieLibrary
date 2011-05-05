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
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending,
                false, false, false, false, false, false);
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

    static public int searchByNameWish(String name, MovieSearchClient client,
            String orderByColumn, boolean ascending, boolean wish) {
        int assignedKey = key;
        key++;
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending,
                false, false, true, wish, false, false);
        return assignedKey;
    }

    static public int searchByNameFavorite(String name,
            MovieSearchClient client, String orderByColumn, boolean ascending,
            boolean favorite) {
        int assignedKey = key;
        key++;
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending,
                false, false, false, false, true, favorite);
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
     * 
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
            final boolean useSeen, final boolean seen, final boolean useWish,
            final boolean wish, final boolean useFavorite,
            final boolean favorite) {
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
                    if (useSeen) {
                        queryBuilder.where().like("name", "%" + name + "%")
                                .and().eq("seen", seen).and()
                                .eq("discnumber", 1);

                    } else if (useFavorite) {
                        queryBuilder.where().like("name", "%" + name + "%")
                                .and().eq("favorite", favorite).and()
                                .eq("discnumber", 1);
                    } else if (useWish) {
                        queryBuilder.where().like("name", "%" + name + "%")
                                .and().eq("wish", wish).and()
                                .eq("discnumber", 1);
                    } else {
                        queryBuilder.where().like("name", "%" + name + "%")
                                .and().eq("discnumber", 1);
                    }

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
                    ArrayList<Movie> randomMovies = new ArrayList<Movie>();
                    int rating = 5;
                    // get movies until we have as many we need
                    while (randomMovies.size() < numberOfMovies && rating > -1) {
                        QueryBuilder<Movie, Integer> queryBuilder = dbMovie
                                .queryBuilder();
                        queryBuilder.where().eq("discnumber", 1).and()
                                .eq("seen", false).and().eq("rating", rating);

                        List<Movie> movies = dbMovie.query(queryBuilder
                                .prepare());

                        // there are more movies with this rating than we need,
                        // pick some random
                        if (movies.size() > numberOfMovies
                                - randomMovies.size()) {
                            Random randomGenerator = new Random();
                            Movie movie;
                            while (movies.size() > numberOfMovies
                                    - randomMovies.size()) {
                                movies.remove(randomGenerator.nextInt(movies
                                        .size()));
                            }
                        }
                        randomMovies.addAll(movies);
                        // we might need more movies, add these we have and step
                        // down one rating
                        rating--;

                    }

                    client.searchFinished(randomMovies, assignedKey);
                } catch (SQLException e) {
                    System.out.println("error searching for movies");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
