package se.eloff.ultimatemovielibrary;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * A class that handles all WatchFolder backend logic, retrieves and saves
 * WatchFolders to the db, initiate folder scans and so on
 * 
 * @author david
 * 
 */
public class WatchFolderManager {

    private static HashMap<String, DirScanner> runningScans = new HashMap<String, DirScanner>();

    private static boolean scanInProgress = false;

    /**
     * Get all WatchFolders that are saved in the db
     * 
     * @return a List with all WatchFolders
     */
    public static List<WatchFolder> getAllWatchFolders() {
        Dao<WatchFolder, Integer> db;
        try {
            db = DatabaseManager.getInstance().getWatchFolderDao();
            List<WatchFolder> folders = db.queryForAll();
            return folders;

        } catch (SQLException e) {
            System.out.println("Failed to scan all folders!");
            return null;
        }
    }

    /**
     * Performs a scan on all WatchFolders currently in the db
     * 
     * @return true on success
     */
    public static boolean updateLibrary() {
        Dao<WatchFolder, Integer> db;
        try {
            db = DatabaseManager.getInstance().getWatchFolderDao();
            List<WatchFolder> folders = db.queryForAll();
            for (WatchFolder watchFolder : folders) {
                scanFolder(watchFolder);
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to scan all folders!");
            return false;
        }
    }

    /**
     * Removes a WatchFolder from the db, stops dirScan if it is in progress,
     * removes all movies associated with that WatchFolder
     * 
     * @param folder
     * @return true on success
     */
    public static boolean removeWatchFolder(final WatchFolder folder) {
        // are we performing a scan of this folder?
        stopScan(folder);

        Dao<WatchFolder, Integer> dbWatchFolder;
        Dao<LocalMovie, Integer> dbMovie;
        Dao<MovieInfo, Integer> dbMovieInfo;
        Dao<MovieList, Integer> dbMovieList;
        try {
            // remove the watchFolder
            dbWatchFolder = DatabaseManager.getInstance().getWatchFolderDao();
            dbWatchFolder.delete(folder);

            // remove all movies with a path that begins with the folderpath
            // Note that if there are nested WatchFolders a removal of a higher
            // folder will also delete folders in subfolders and movies that
            // should be watched are removed. Maybe solve with a new scan on all
            // enabled watchfolders after the deletion is done.
            dbMovie = DatabaseManager.getInstance().getMovieDao();
            QueryBuilder<LocalMovie, Integer> queryBuilder = dbMovie
                    .queryBuilder();
            queryBuilder.where().like("filepath", folder.getFolderPath() + "%");
            List<LocalMovie> movies = dbMovie.query(queryBuilder.prepare());

            dbMovieInfo = DatabaseManager.getInstance().getMovieInfoDao();
            dbMovieList = DatabaseManager.getInstance().getMovieListDao();
            for (LocalMovie localMovie : movies) {
                // remove all movie info
                dbMovieInfo.delete(dbMovieInfo.queryForId(localMovie
                        .getInfo_id()));
                dbMovie.delete(localMovie);

                // remove the movie from any playlist it might be in
                DeleteBuilder<MovieList, Integer> deleteLists = dbMovieList
                        .deleteBuilder();
                deleteLists.where().eq("movie_id", localMovie.getId());
                dbMovieList.delete(deleteLists.prepare());
            }
            DatabaseManager.getInstance().fireMovieAddedEvent(null);
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to delete watchfolder from db");
            return false;
        }
    }

    /**
     * Add a WatchFolder to the db, performs an async dirscan for movies in the
     * watchFolder folder
     * 
     * @param folder
     * @return true on success, please note that the scanning is running in
     *         background even after this function returns
     */
    public static boolean addWatchFolder(final WatchFolder folder) {
        Dao<WatchFolder, Integer> db;
        try {
            db = DatabaseManager.getInstance().getWatchFolderDao();
            db.create(folder);
            scanFolder(folder);
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to save to db, maybe the watchfolder "
                    + "is allready watched");
            return false;
        }
    }

    private static boolean scanFolder(final WatchFolder folder) {

        final DirScanner scanner = new DirScanner();
        runningScans.put(folder.getFolderPath(), scanner);
        scanInProgress = true;
        Thread scanThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Running new dirscanner thread");

                // Show loadingLabels
                Localization.loadingTextLabel
                        .setText(Localization.scanningFoldersText);
                Localization.loadingTextLabel.setVisible(true);
                Localization.loadingLabel.setVisible(true);

                try {
                    scanner.scanFolder(new File(folder.getFolderPath()));
                    MovieInfoDownloader.getInstance().updateLibraryInfo();
                } catch (Exception e) {
                    System.out.println("Failed to convert path to File type");
                }
                runningScans.remove(folder.toString());

                scanInProgress = false;

                // Hide loadingLabels, if not to be hidden by the
                // MovieInfoDownloader later on
                if (!isScanInProgress()
                        && !MovieInfoDownloader.isUpdateInProgress()) {
                    Localization.loadingTextLabel.setText("");
                    Localization.loadingLabel.setVisible(false);
                }

                System.out.println("Successfully scanned: "
                        + folder.getFolderPath());
            }
        });

        scanThread.start();

        return true;
    }

    private static void stopScan(final WatchFolder folder) {
        try {
            runningScans.get(folder.getFolderPath()).stopScanning();
            runningScans.remove(folder.getFolderPath());
        } catch (Exception e) {
        }
    }

    public static boolean isScanInProgress() {
        // TODO: Do something smarter, count threads or something.
        // Is this really thread safe?
        return scanInProgress;
    }
}
