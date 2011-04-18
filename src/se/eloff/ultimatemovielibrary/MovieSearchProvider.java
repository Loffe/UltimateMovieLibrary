package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.List;

import sun.misc.Cleaner;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class MovieSearchProvider {

    static private int key = 0;

    static public int searchByName(String name, MovieSearchClient client) {
        int assignedKey = key;
        key++;
        searchByNameAsync(name, assignedKey, client);
        return assignedKey;
    }

    static private void searchByNameAsync(final String name,
            final int assignedKey, final MovieSearchClient client) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Dao<Movie, Integer> dbMovie;
                try {
                    dbMovie = DatabaseManager.getInstance().getMovieDao();
                    QueryBuilder<Movie, Integer> queryBuilder = dbMovie
                            .queryBuilder();

                    //select any movie that begins with the name string and is disc 1
                    queryBuilder.where().like("name", name + "%").and()
                            .eq("discnumber", 1);
                    queryBuilder.orderBy("name", true);

                    PreparedQuery<Movie> preparedQuery = queryBuilder.prepare();
                    List<Movie> movies = dbMovie.query(preparedQuery);
                    client.searchFinished(movies, assignedKey);
                } catch (SQLException e) {
                    System.out.println("error searching for movies");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
