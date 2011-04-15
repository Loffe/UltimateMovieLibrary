package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class DatabaseManager {
    private static String url = "jdbc:sqlite:database.db";
    private static DatabaseManager instance = null;

    private JdbcConnectionSource source;
    private Dao<Movie, Integer> movieManager;
    private Dao<WatchFolder, Integer> watchFolderManager;

    private DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            source = new JdbcConnectionSource(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Movie, Integer> getMovieDao() throws SQLException {
        if (movieManager == null)
            movieManager = DaoManager.createDao(source, Movie.class);

        return movieManager;
    }

    public Dao<WatchFolder, Integer> getWatchFolderDao() throws SQLException {
        if (watchFolderManager == null)
            watchFolderManager = DaoManager
                    .createDao(source, WatchFolder.class);

        return watchFolderManager;
    }

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();

        return instance;
    }
}
