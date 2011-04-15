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
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setTitle("Ultimate Movie Library");
        app.setIconImage(new ImageIcon("img/video_16.png").getImage());
        app.add(new WatchFolderManagerPanel());
        app.pack();

        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:database.db";

        JdbcConnectionSource source = new JdbcConnectionSource(url);

        Dao<Movie, Integer> movieDao = DaoManager
                .createDao(source, Movie.class);
        if (!movieDao.isTableExists()) {
            TableUtils.createTable(source, Movie.class);
        }

        Dao<WatchFolder, Integer> watchFolderDao = DaoManager.createDao(source,
                WatchFolder.class);
        if (!watchFolderDao.isTableExists()) {
            TableUtils.createTable(source, WatchFolder.class);
        }

        ExternalPlayerLauncher movieLauncher = new ExternalPlayerLauncher();

        // to try the scan, just enter a valid path on your disk where you have
        // movies
        DirScanner.ScanFolder(new File("movies"));

        // Uncomment to try the movie player
        // movieLauncher.playMovie(new Movie("Conan The Barbarian", 1,
        // "movies//Conan.The.Barbarian.1980.Disc1.avi"));
    }

}
