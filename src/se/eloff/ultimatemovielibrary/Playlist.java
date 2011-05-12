package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author eriel743
 * 
 */
@DatabaseTable(tableName = "lists")
public class Playlist {
    protected static final String[] fixedPlaylists = {
            Localization.profileAllMoviesList,
            Localization.profileFavoriteList, Localization.profileWishList,
            Localization.profileSeenList };

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

    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    /**
     * Add a movie to the playlist
     * 
     * @param movie
     * @return
     * @throws SQLException
     */
    public MovieList add(LocalMovie movie) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        Dao<MovieList, Integer> movieListDao = db.getMovieListDao();
        MovieList movieList = new MovieList(movie, this);

        // get the last position and add 1
        QueryBuilder<MovieList, Integer> lastPositionQuery = movieListDao
                .queryBuilder();
        lastPositionQuery.where().eq("list_id", this.id);
        lastPositionQuery.distinct();
        lastPositionQuery.orderBy("position", false);

        List<MovieList> lastPos = movieListDao.query(lastPositionQuery
                .prepare());
        int newPos = 0;
        if (!lastPos.isEmpty()) {
            newPos = lastPos.get(0).getPosition() + 1;
        }

        movieList.setPosition(newPos);
        movieListDao.create(movieList);

        return movieList;
    }

    /**
     * Remove a movie from the playlist
     * 
     * @param movie
     * @throws SQLException
     */
    public void remove(LocalMovie movie) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        Dao<MovieList, Integer> movieListDao = db.getMovieListDao();

        DeleteBuilder<MovieList, Integer> deleteBuilder = movieListDao
                .deleteBuilder();
        deleteBuilder.where().eq("movie_id", movie.getId()).and().eq("list_id",
                this.id);

        movieListDao.delete(deleteBuilder.prepare());
    }

    public ArrayList<LocalMovie> getMovies() throws SQLException {
        ArrayList<LocalMovie> list = new ArrayList<LocalMovie>();
        Dao<LocalMovie, Integer> movieDao = DatabaseManager.getInstance()
                .getMovieDao();

        for (MovieList ml : movie_list) {
            LocalMovie m = ml.getMovie();
            movieDao.refresh(m);
            list.add(m);
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        Dao<Playlist, Integer> listDao = DatabaseManager.getInstance()
                .getListDao();
        Dao<MovieList, Integer> movieListDao = DatabaseManager.getInstance()
                .getMovieListDao();
        Dao<LocalMovie, Integer> movieDao = DatabaseManager.getInstance()
                .getMovieDao();

        Playlist myFavs = listDao.queryForId(1);
        for (LocalMovie m : myFavs.getMovies()) {
            System.out.println(m.getName());
        }

        String sql = "select name, year, filepath, discnumber, rating from movies_lists ml"
                + " left join movies m on ml.movie_id = m.id"
                + " order by ml.position asc";
        RawRowMapper<LocalMovie> rowMapper = new RawRowMapper<LocalMovie>() {

            @Override
            public LocalMovie mapRow(String[] columnNames,
                    String[] resultColumns) throws SQLException {
                return new LocalMovie(resultColumns[0], Integer
                        .parseInt(resultColumns[1]), resultColumns[2], Integer
                        .parseInt(resultColumns[3]), Integer
                        .parseInt(resultColumns[4]));
            }
        };
        GenericRawResults<LocalMovie> res = movieDao.queryRaw(sql, rowMapper);

        System.out.println("RawResults");
        for (LocalMovie m : res) {
            System.out.println(m.getName());
        }

        /*
         * Movie movie = movieDao.queryForId(15);
         * 
         * MovieList ml = new MovieList(movie, myFavs); movieListDao.create(ml);
         */
    }

}
