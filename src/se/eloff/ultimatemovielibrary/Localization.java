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
            "img/Folder-Add-64.png");

    public static final String removeWatchFolderButtonText = null;
    public static final String removeWatchFolderButtonToolTip = "Sluta bevaka den här katalogen";
    public static final ImageIcon removeWatchFolderButtonIcon = new ImageIcon(
            "img/delete_16.png");
    public static final String removeWatchFolderConfirmationText = "Vill du sluta bevaka katalogen?\n";

    // Search
    public static final String searchTitle = "Sök";
    public static final String searchFieldLabelText = "Sök efter filmtitel:";
    public static final String searchNoMatchText = "Din sökning matchade inte någon film.";
    public static final String searchInProgressText = "Laddar filmer...";
    public static final String searchOrderButtonMovieTitle = "Filmtitel";
    public static final String searchOrderButtonMovieYear = "År";
    public static final String searchOrderButtonMovieRating = "Betyg";
    public static final ImageIcon searchToggleSeenButtonIcon = new ImageIcon(
    "img/eye_50_crossed_disabled.png");
    public static final ImageIcon searchToggleSeenButtonIconHide = new ImageIcon(
    "img/eye_50_crossed.png");
    
    // Recommended movies
    public static final String recommendTitle = "Rekommendationer";
    public static final String recommendRefreshButtonText = "";
    public static final ImageIcon recommendRefreshButtonIcon = new ImageIcon(
    "img/Button-Refresh-icon_50.png");
    public static final String recommendRefreshNoMatchText = "Inga filmer i databasen.";
    public static final String recommendRefreshProgressText = "Laddar filmer...";
    
    //MovieElement
    public static final ImageIcon moviePlayButtonIcon = new ImageIcon(
    "img/Button-Play-icon_50.png");
    public static final ImageIcon movieSeenButtonIcon = new ImageIcon(
    "img/eye_50.png");
    public static final ImageIcon movieFavoriteButtonIcon = new ImageIcon(
    "img/Favorite-icon_50.png");
    public static final ImageIcon movieStarButtonIcon = new ImageIcon(
    "img/Button-Favorite-icon_50.png");
    
    public static final ImageIcon movieSeenButtonIconDisabled = new ImageIcon(
    "img/eye_50_disabled.png");
    public static final ImageIcon movieFavoriteButtonIconDisabled = new ImageIcon(
    "img/Favorite-icon_50_disabled.png");
    public static final ImageIcon movieStarButtonIconDisabled = new ImageIcon(
    "img/Button-Favorite-icon_50_disabled.png");
    
    public static final String MovieNoYearText = "Okänt";

    // Menu
    public static final String menuSearchText = "Sök";
    public static final String menuProfileText = "Profil";
    public static final String menuRecommendText = "Rekommendationer";
    public static final String menuManageWatchFolderText = null;
    public static final ImageIcon menuManageWatchFolderIcon = new ImageIcon(
            "img/Folder-Add_16.png");
    public static final String menuExitText = null;
    public static final ImageIcon menuExitIcon = new ImageIcon(
            "img/delete_16.png");

    // ResultPanel
    public static final ImageIcon resultPanelArrowUp = new ImageIcon(
            "img/arrow-up.png");
    public static final ImageIcon resultPanelArrowDown = new ImageIcon(
            "img/arrow-down.png");

}
