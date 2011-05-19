package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

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
    private Dao<MovieInfo, Integer> movieInfoManager;
    private Dao<Playlist, Integer> listManager;
    private Dao<MovieList, Integer> movieListManager;
    private Dao<WatchFolder, Integer> watchFolderManager;

    private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();
    private ArrayList<MovieListener> movieListeners = new ArrayList<MovieListener>();

    public void addPlaylistChangeListener(ListDataListener listener) {
        listeners.add(listener);
    }

    public void removePlaylistChangeListener(ListDataListener listener) {
        listeners.remove(listener);
    }

    public void addMovieListener(MovieListener listener) {
        movieListeners.add(listener);
    }

    public void removeMovieListener(MovieListener listener) {
        movieListeners.remove(listener);
    }

    protected void firePlaylistAddedEvent(ListDataEvent e) {
        for (ListDataListener l : listeners) {
            l.contentsChanged(e);
        }
    }

    public void fireMovieAddedEvent(LocalMovie movie) {
        for (MovieListener l : movieListeners) {
            l.onMovieAdded(movie);
        }
    }

    public void fireMovieUpdatedEvent(LocalMovie movie) {
        for (MovieListener l : movieListeners) {
            l.onMovieUpdated(movie);
        }
    }

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
            getMovieInfoDao();
            getMovieListDao();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Playlist createPlaylist(String name) {
        try {
            Dao<Playlist, Integer> listsDao = DatabaseManager.getInstance()
                    .getListDao();
            Playlist playlist = new Playlist(name);
            listsDao.create(playlist);
            listsDao.refresh(playlist);
            firePlaylistAddedEvent(new ListDataEvent(playlist,
                    ListDataEvent.CONTENTS_CHANGED, 0, 0));
            return playlist;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

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

    public Dao<MovieInfo, Integer> getMovieInfoDao() throws SQLException {
        if (movieInfoManager == null) {
            movieInfoManager = DaoManager.createDao(source, MovieInfo.class);
            if (!movieInfoManager.isTableExists()) {
                TableUtils.createTable(source, MovieInfo.class);
            }
        }

        return movieInfoManager;
    }

    public Dao<Playlist, Integer> getListDao() throws SQLException {
        if (listManager == null) {
            listManager = DaoManager.createDao(source, Playlist.class);
            if (!listManager.isTableExists()) {
                TableUtils.createTable(source, Playlist.class);

                // TODO DEBUG remove when there is a way to create playlists
                try {
                    for (String name : Playlist.fixedPlaylists) {
                        listManager.create(new Playlist(name));
                    }
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                // /////////////////////////////////////////////////////////
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
