package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "lists")
public class Playlist {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @ForeignCollectionField(eager = true)
    ForeignCollection<MovieList> movie_list;

    public Playlist() {
    }

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Movie> getMovies() throws SQLException {
        ArrayList<Movie> list = new ArrayList<Movie>();
        Dao<Movie, Integer> movieDao = DatabaseManager.getInstance()
                .getMovieDao();

        for (MovieList ml : movie_list) {
            Movie m = ml.getMovie();
            movieDao.refresh(m);
            list.add(m);
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        Dao<Playlist, Integer> listDao = DatabaseManager.getInstance().getListDao();
        Dao<MovieList, Integer> movieListDao = DatabaseManager.getInstance()
                .getMovieListDao();
        Dao<Movie, Integer> movieDao = DatabaseManager.getInstance()
                .getMovieDao();

        Playlist myFavs = listDao.queryForId(2);
        for (Movie m : myFavs.getMovies()) {
            System.out.println(m.getName());
        }

        /*
         * Movie movie = movieDao.queryForId(15);
         *
         * MovieList ml = new MovieList(movie, myFavs); movieListDao.create(ml);
         */
    }

}
