package se.eloff.ultimatemovielibrary;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
 * Launches VLC(or the default associated application), if it fails the user can
 * choose where the player is located
 * 
 * Singleton.
 * 
 * The chosen player is saved to the user settings ( not implemented)
 */
public class ExternalPlayerLauncher {

    // The singleton instance.
    private static ExternalPlayerLauncher instance = null;

    private File moviePlayer = null;

    /**
     * Constructor. Create a new ExternalPlayerLauncher. Private constructor
     * prevents instantiation from other classes.
     */
    private ExternalPlayerLauncher() {
        // TODO: load the moviePlayer settings from db
    }

    /**
     * Launches VLC(or the default associated application), if it fails the user
     * can choose where the player is located
     * 
     * @param movie
     */
    public void playMovie(LocalMovie movie) {
        // TODO: if it is a multidisc movie, load up all additional discs
        Desktop dt = Desktop.getDesktop();
        try {
            if (moviePlayer == null)
                // tries to open the default media player, it works if it is
                // VLC, fails on windows media player for some reason
                dt.open(new File(movie.getFilepath()));

            else {
                Runtime.getRuntime().exec(new String[]{moviePlayer.getCanonicalPath(), movie.getFilepath()});
            }

        } catch (IOException e) {
            // Crash
            System.out.println("Failed to play the movie: " + movie.getName());
            // maybe its because of error in file association with vlc, let
            // the user select witch player to use
            if (selectPlayer())
                playMovie(movie);
        }
        // TODO; hold the thread and let the rest of the application know when
        // we finished so we can let teh user rate the movie etc.
    }

    private boolean selectPlayer() {
        JFileChooser fileChooser = new JFileChooser();

        // Only allow to select directories.
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Show the file chooser dialog and get the result.
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Add the selected folder path.
            moviePlayer = fileChooser.getSelectedFile();
            // TODO: save the setting somewhere in the database
            return true;
        }
        return false;
    }

    /**
     * Get the current instance of the ExternalPlayerLauncher.
     * 
     * @return the current instance of the ExternalPlayerLauncher.
     */
    public static ExternalPlayerLauncher getInstance() {
        if (instance == null)
            instance = new ExternalPlayerLauncher();
        return instance;
    }
}
