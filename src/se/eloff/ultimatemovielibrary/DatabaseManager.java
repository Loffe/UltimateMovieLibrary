package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.table.TableUtils;

public class DatabaseManager {
    private static String url = "jdbc:sqlite:database.db";
    private static DatabaseManager instance = null;

    private JdbcConnectionSource source;
    private Dao<LocalMovie, Integer> movieManager;
    private Dao<Playlist, Integer> listManager;
    private Dao<MovieList, Integer> movieListManager;
    private Dao<WatchFolder, Integer> watchFolderManager;

    private DatabaseManager() {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            source = new JdbcConnectionSource(url);
            getMovieDao();
            getWatchFolderDao();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<LocalMovie, Integer> getMovieDao() throws SQLException {
        if (movieManager == null) {
            movieManager = DaoManager.createDao(source, LocalMovie.class);
            if (!movieManager.isTableExists()) {
                TableUtils.createTable(source, LocalMovie.class);
            }
        }

        return movieManager;
    }

    public Dao<Playlist, Integer> getListDao() throws SQLException {
        if (listManager == null) {
            listManager = DaoManager.createDao(source, Playlist.class);
            if (!listManager.isTableExists()) {
                TableUtils.createTable(source, Playlist.class);
            }
        }

        return listManager;
    }

    public Dao<MovieList, Integer> getMovieListDao() throws SQLException {
        if (movieListManager == null) {
            movieListManager = DaoManager.createDao(source, MovieList.class);
            if (!movieListManager.isTableExists()) {
                TableUtils.createTable(source, MovieList.class);
            }
        }

        return movieListManager;
    }

    public Dao<WatchFolder, Integer> getWatchFolderDao() throws SQLException {
        if (watchFolderManager == null) {
            watchFolderManager = DaoManager
                    .createDao(source, WatchFolder.class);
            if (!watchFolderManager.isTableExists()) {
                TableUtils.createTable(source, WatchFolder.class);
            }
        }

        return watchFolderManager;
    }

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();

        return instance;
    }
}
