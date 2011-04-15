package se.eloff.ultimatemovielibrary;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.Dao;

/**
 * A folder scanner that scans a folder and all its subfolders for movie files.
 * It parses the filenames and tries to find the movie name and year, returns a
 * list of movies.
 * 
 * For now it need the files to be named like:
 * "name of the move 1999 720p bl blalbla.avi" it will remove all dots and _ if
 * there is any instead of whitespace. It will handle multidisc movies as
 * separate movies and if the year is not separated from the name with a space
 * or . the last word of the title will be lost.
 * 
 * Currently name lookup with foldername is not supported, only the actual
 * filename will be used.
 * 
 * @author david
 * 
 */
public class DirScanner {

    private static String[] extensions = { "avi", "mpg", "mkv", "mp4" };
    private static String[] ignoreList = { "sample", "subs", "subtitles" };
    private static String[] splitWords = { "xvid", "720", "1080", "bluray",
            "264", "brrip", "engsub", "swesub", "cd", "dvd", "disk", "part" };
    private static String[] discWords = { "cd", "dvd", "disk", "part" };

    private static HashMap<String, DirScanner> runningScans = new HashMap<String, DirScanner>();

    private Boolean StopScanning = false;

    private DirScanner() {

    }

    /**
     * Scans a folder and returns movies found
     * 
     * @param folder
     * @return a collection of all movies
     */
    public static boolean ScanFolder(final File folder) {
        Thread scanThread = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("Running new dirscanner thread");
                DirScanner scanner = new DirScanner();
                runningScans.put(folder.toString(), scanner);
                scanner.ScanFolderInt(folder);
                runningScans.remove(folder.toString());
            }
        });

        scanThread.start();
        return true;
    }

    public static void stopScan(final File folder) {
        try {
            runningScans.get(folder.toString()).stopScanning();
            runningScans.remove(folder.toString());
        } catch (Exception e) {

        }
    }

    protected void stopScanning() {
        StopScanning = true;
    }

    // the internal scanfolder call, it will call itself recursively on all
    // subfolders
    private void ScanFolderInt(File folder) {
        for (final File file : folder.listFiles()) {
            if (StopScanning)
                return;
            if (file.isDirectory() && notOnIgnoreList(file)) {
                ScanFolderInt(file);
            } else if (hasValidExtension(file)) {
                Movie movie = movieFromPath(file);
                if (movie != null) {
                    // movies.add(movie);
                    Dao<Movie, Integer> db;
                    try {
                        db = DatabaseManager.getInstance().getMovieDao();
                        db.create(movie);
                    } catch (SQLException e) {
                        System.out
                                .println("Failed to save to db, maybe the movie is allready saved once");
                    }
                    System.out.println("Adding Movie: '" + movie.getName()
                            + "' year: " + movie.getYear());
                }
            }
        }
    }

    private boolean hasValidExtension(File file) {
        String fileName = file.toString().toLowerCase();
        String fileExtension = fileName.substring(
                fileName.lastIndexOf('.') + 1, fileName.length());
        // System.out.println("Filename: " + fileName);
        // System.out.println("Extension: " + fileExtension);
        for (String extension : extensions) {
            if (fileExtension.equals(extension))
                return true;
        }
        return false;
    }

    private boolean notOnIgnoreList(File folder) {
        String folderName = folder.toString().toLowerCase();
        String subfolder = folderName.substring(
                folderName.lastIndexOf('\\') + 1, folderName.length());
        // System.out.println("Subfolder: "+subfolder);
        for (String string : ignoreList) {
            if (subfolder.equals(string)) {
                System.out.println("ignoring: " + folderName);
                return false;
            }
        }
        return true;
    }

    /*
     * private static int scanDiscNumber(String name) { String discNumber = "";
     * loop: for (String word : discWords) { int index =
     * name.toLowerCase().indexOf(word); if (index != -1){ //we found one of the
     * magic discwords //remove the disc word name = name.substring(index +
     * word.length(), name.length()); //now find all numbers and break when a
     * non digit is found
     * 
     * } } } return 1; }
     */

    private Movie movieFromPath(File file) {

        String path = file.toString();
        System.out.println("Movie path: " + file.toString());

        int year = 0;

        // first remove the path
        String movieName = file.getName();

        // remove the extension
        movieName = movieName.substring(0, movieName.lastIndexOf('.'));

        // then remove all .,_ and replace with space
        movieName = movieName.replaceAll("[.'_]", " ");

        // then try to split it at a potential yearstamp, if no year split at
        // the first of the splitWords list
        String stringParts[] = movieName.split("\\s+");
        movieName = "";

        partsLoop: for (String string : stringParts) {
            if (string.matches(".*\\d{4}.*")) {
                // we have a year (probably)
                year = Integer.parseInt(string.replaceAll("\\D", ""));
                // System.out.println("Found a year, name: "+ movieName +
                // " year: " + year);
                break partsLoop;
            }
            // no year yet, maybe we have one of the split words, if so, break
            // here
            String lowerCaseString = string.toLowerCase();
            for (String splitWord : splitWords) {
                if (lowerCaseString.contains(splitWord)) {
                    // System.out.println("Found a splitWord, name: "+ movieName
                    // );
                    break partsLoop;
                }
            }
            // if we made it here, just add the word back to the name
            movieName += " " + string;
        }
        // nothing left to check, just return what we have
        movieName = movieName.trim();
        if (movieName.isEmpty())
            return null;
        return new Movie(movieName, year, path);
    }
}
