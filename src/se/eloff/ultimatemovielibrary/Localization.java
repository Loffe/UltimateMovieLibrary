package se.eloff.ultimatemovielibrary;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Localization is used to make GUI-strings and Icons independent of the rest of
 * the code.
 */
public final class Localization {

    // App
    public static final String title = "Ultimate Movie Library";
    public static final Image icon = new ImageIcon("img/video_16.png")
            .getImage();

    // Watch Folder Manager
    public static final ImageIcon watchFolderIcon = new ImageIcon(
            "img/folder_32.png");
    public static final String manageWatchFolderHeading = "Hantera bevakade mappar";
    public static final String manageWatchFolderDescription = "Här kan du välja att lägga till eller ta bort bevakade kataloger.";

    public static final String addWatchFolderButtonText = "Bevaka en katalog ...";
    public static final String addWatchFolderButtonToolTip = "Lägg till en katalog att bevaka";
    public static final ImageIcon addWatchFolderButtonIcon = new ImageIcon(
            "img/Folder-Add.png");

    public static final String removeWatchFolderButtonText = null;
    public static final String removeWatchFolderButtonToolTip = "Sluta bevaka den här katalogen";
    public static final ImageIcon removeWatchFolderButtonIcon = new ImageIcon(
            "img/delete_16.png");
    public static final String removeWatchFolderConfirmationText = "Vill du sluta bevaka katalogen?\n";

    // Search
    public static final String searchFieldLabelText = "Sök efter filmtitel:";
    public static final String searchNoMatchText = "Din sökning matchade inte någon film.";
    public static final String searchInProgressText = "Laddar filmer...";
    public static final String searchOrderButtonMovieTitle = "Filmtitel";
    public static final String searchOrderButtonMovieYear = "År";
    public static final String searchOrderButtonMovieRating = "Betyg";

    // Menu
    public static final String menuSearchText = "Sök";
    public static final String menuProfileText = "Profil";
    public static final String menuRecommendText = "Rekommendationer";
    public static final String menuManageWatchFolderText = null;
    public static final ImageIcon menuManageWatchFolderIcon = new ImageIcon(
            "img/Folder-Add.png");
    public static final String menuExitText = null;
    public static final ImageIcon menuExitIcon = new ImageIcon(
            "img/delete_16.png");

}
