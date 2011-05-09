package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
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
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending,
                useSeen, seen, useWish, wish, useFavorite, favorite, null);
    }

    static private void searchByNameAsync(final String name,
            final int assignedKey, final MovieSearchClient client,
            final String orderByColumn, final boolean ascending,
            final boolean useSeen, final boolean seen, final boolean useWish,
            final boolean wish, final boolean useFavorite,
            final boolean favorite,
            final se.eloff.ultimatemovielibrary.Playlist list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dao<LocalMovie, Integer> movieDao;
                try {
                    movieDao = DatabaseManager.getInstance().getMovieDao();

                    // Force creation of movies_lists table
                    DatabaseManager.getInstance().getMovieListDao();

                    String order_clause = "ml.position asc";

                    if (orderByColumn != null) {
                        order_clause = orderByColumn;
                        order_clause += ascending ? " ASC" : " DESC";
                    }

                    String list_id;
                    if (list != null) {
                        list_id = String.valueOf(list.getId());
                    } else {
                        list_id = "list_id";
                    }

                    String sql = "select m.id, name, year, filepath, discnumber, rating, seen"
                            + " from movies_lists ml"
                            + " left join movies m on ml.movie_id = m.id"
                            + " where discnumber = 1"
                            + " and list_id = " + list_id
                            + " and name like '%" + name + "%'"
                            + " order by " + order_clause;
                    RawRowMapper<LocalMovie> rowMapper = new RawRowMapper<LocalMovie>() {
                        @Override
                        public LocalMovie mapRow(String[] columnNames,
                                String[] resultColumns) throws SQLException {
                            LocalMovie m = new LocalMovie(resultColumns[1],
                                    Integer.parseInt(resultColumns[2]),
                                    resultColumns[3], Integer
                                            .parseInt(resultColumns[4]),
                                    Integer.parseInt(resultColumns[5]));
                            boolean seen = resultColumns[6].equals("1");
                            m.setId(Integer.parseInt(resultColumns[0]));
                            m.setSeen(seen);
                            return m;
                        }
                    };
                    GenericRawResults<LocalMovie> res = movieDao.queryRaw(sql,
                            rowMapper);

                    List<LocalMovie> movies = res.getResults();
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
                Dao<LocalMovie, Integer> dbMovie;
                try {
                    // TODO nicer random function, maybe get random objects from
                    // the database in the first place... how?
                    dbMovie = DatabaseManager.getInstance().getMovieDao();
                    ArrayList<LocalMovie> randomMovies = new ArrayList<LocalMovie>();
                    int rating = 5;
                    // get movies until we have as many we need
                    while (randomMovies.size() < numberOfMovies && rating > -1) {
                        QueryBuilder<LocalMovie, Integer> queryBuilder = dbMovie
                                .queryBuilder();
                        queryBuilder.where().eq("discnumber", 1).and().eq(
                                "seen", false).and().eq("rating", rating);

                        List<LocalMovie> movies = dbMovie.query(queryBuilder
                                .prepare());

                        // there are more movies with this rating than we need,
                        // pick some random
                        if (movies.size() > numberOfMovies
                                - randomMovies.size()) {
                            Random randomGenerator = new Random();
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

    public static int searchByList(String name, MovieSearchClient client,
            String orderByColumn, boolean ascending,
            se.eloff.ultimatemovielibrary.Playlist list) {
        int assignedKey = key;
        key++;
        searchByNameAsync(name, assignedKey, client, orderByColumn, ascending,
                false, false, false, false, false, false, list);
        return assignedKey;
    }
}
