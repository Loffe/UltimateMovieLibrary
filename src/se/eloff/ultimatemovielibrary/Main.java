package se.eloff.ultimatemovielibrary;

import java.io.File;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Main {

    /**
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException,
            ClassNotFoundException {
        AppFrame app = new AppFrame();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.setVisible(true);

//        app.add(new WatchFolderManagerPanel());
//        app.pack();

        Dao<Movie, Integer> movieDao = DatabaseManager.getInstance()
                .getMovieDao();

        Dao<WatchFolder, Integer> watchFolderDao = DatabaseManager
                .getInstance().getWatchFolderDao();

        ExternalPlayerLauncher movieLauncher = new ExternalPlayerLauncher();

        // to try the scan, just enter a valid path on your disk where you have
        // movies
        DirScanner.ScanFolder(new WatchFolder("movies", (long)0));

        // Uncomment to try the movie player
        // movieLauncher.playMovie(new Movie("Conan The Barbarian", 1,
        // "movies//Conan.The.Barbarian.1980.Disc1.avi"));
    }

}
