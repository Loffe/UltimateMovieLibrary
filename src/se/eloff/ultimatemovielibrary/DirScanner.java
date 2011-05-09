package se.eloff.ultimatemovielibrary;

import java.io.File;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

/**
 * Not to be used from anywhere except WatchFolderManager!
 * 
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

    private static final String[] extensions = { "avi", "mpg", "mkv", "mp4" };
    private static final String[] ignoreList = { "sample", "subs", "subtitles" };
    private static final String[] splitWords = { "xvid", "720", "1080",
            "bluray", "264", "brrip", "engsub", "swesub", "cd", "dvd", "disk",
            "part" };
    private static final String[] discWords = { "cd", "disc" };

    private Boolean StopScanning = false;

    protected void stopScanning() {
        StopScanning = true;
    }

    /**
     * Scans a folder and all its subfolders, the results are saved to the db
     * 
     * @param folder
     */
    public void ScanFolder(File folder) {
        for (final File file : folder.listFiles()) {
            if (!StopScanning && file.isDirectory() && notOnIgnoreList(file)) {
                ScanFolder(file);
            } else if (!StopScanning && hasValidExtension(file)) {
                LocalMovie movie = movieFromPath(file);
                if (!StopScanning && movie != null) {
                    // movies.add(movie);
                    Dao<LocalMovie, Integer> db;
                    try {
                        db = DatabaseManager.getInstance().getMovieDao();
                        db.create(movie);
                    } catch (SQLException e) {
                        System.out
                                .println("Failed to save to db, maybe the movie is allready saved once");
                    }
                    System.out.println("Adding Movie: '" + movie.getName()
                            + "' year: " + movie.getYear() + " disc: "
                            + movie.getDiscnumber());
                }
            }
        }
    }

    /**
     * checks wither a file has a valid extension
     * 
     * @param file
     * @return
     */
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

    /**
     * returns true if the folder is not on the ignore list
     * 
     * @param folder
     * @return
     */
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

    /**
     * Extracts the disc number...
     * 
     * The discnumber must come directly after the CD,DISC, ... word It will
     * work on .....blabla...CD1.... but not on ....sd....CD.2...
     * 
     * @param name
     * @return
     */
    private int discNumberFromFileName(String name) {
        String discNumber = "0";
        for (String word : discWords) {
            int index = name.toLowerCase().indexOf(word);

            if (index != -1) {
                // we found one of the magic discwords
                // now find all numbers and break when a non-digit is found
                for (int i = index + word.length(); i < name.length(); i++) {
                    char e = name.charAt(i);
                    if (!Character.isDigit(e))
                        break;
                    discNumber += e;
                }
                return Integer.parseInt(discNumber.replaceAll("\\D", ""));
            }
        }
        return 1;
    }

    /**
     * tries to resolve a movie name and a year from a filename
     * 
     * @param file
     * @return
     */
    private LocalMovie movieFromPath(File file) {

        String path = file.toString();
        // System.out.println("Movie path: " + file.toString());

        int year = 0;

        // first remove the path
        String movieName = file.getName();

        // remove the extension
        movieName = movieName.substring(0, movieName.lastIndexOf('.'));

        // then remove all ._ and replace with space
        movieName = movieName.replaceAll("[._]", " ").toLowerCase();

        // then try to split it at a potential yearstamp, if no year split at
        // the first of the splitWords list
        String stringParts[] = movieName.split("\\s+");
        String newMovieName = "";

        partsLoop: for (String string : stringParts) {
            if (string.matches(".*\\d{4}.*")) {
                // System.out.println("Begin year search...");
                // we have a year (probably)
                // where do the year begins?
                int index = 0;
                for (; index < string.length(); index++) {
                    if (Character.isDigit(string.charAt(index))
                            && Character.isDigit(string.charAt(index + 1)))
                        break;
                }
                // now we know the location of the first digit, read 4 ahead and
                // assume that the movie is from in between year 999 and 9999
                year = Integer.parseInt(string.substring(index, index + 4));
                // System.out.println("Found a year, name: "+ movieName +
                // " year: " + year);
                break partsLoop;
            }
            // no year yet, maybe we have one of the split words, if so, break
            // here
            String lowerCaseString = string;
            for (String splitWord : splitWords) {
                if (lowerCaseString.contains(splitWord)) {
                    // System.out.println("Found a splitWord, name: "+ movieName
                    // );
                    break partsLoop;
                }
            }
            // if we made it here, just add the word back to the name,
            // capitalize the first char
            newMovieName += " " + string.substring(0, 1).toUpperCase()
                    + string.substring(1);
        }
        // nothing left to check, just return what we have
        newMovieName = newMovieName.trim();
        if (newMovieName.isEmpty())
            return null;
        return new LocalMovie(newMovieName, year, path,
                discNumberFromFileName(movieName), 0);
    }
}
