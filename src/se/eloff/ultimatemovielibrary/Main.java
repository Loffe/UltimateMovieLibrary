package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;

import javax.swing.JFrame;

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
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:database.db";

        JdbcConnectionSource source = new JdbcConnectionSource(url);

        Dao<Movie, Integer> movieDao = DaoManager
                .createDao(source, Movie.class);

        if (!movieDao.isTableExists()) {
            TableUtils.createTable(source, Movie.class);
        }

        Movie blodapelsinen = new Movie("Blodapelsinen", 1982);

        movieDao.create(blodapelsinen);
    }

}
