package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
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
            public LocalMovie mapRow(String[] columnNames, String[] resultColumns)
                    throws SQLException {
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
