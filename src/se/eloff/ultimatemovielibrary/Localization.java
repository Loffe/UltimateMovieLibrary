package se.eloff.ultimatemovielibrary;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Localization is used to make GUI-strings and Icons independent of the rest of
 * the code.
 */
public final class Localization {

    // App
    public static final String title = "Ultimate Movie Library";
    public static final int titleFontSize = 24;
    public static final Image icon = new ImageIcon("img/video_16.png")
            .getImage();
    public static final int breadCrumbFontSize = 24;

    // Watch Folder Manager
    public static final ImageIcon watchFolderIcon = new ImageIcon(
            "img/folder_32.png");
    public static final String manageWatchFolderHeading = "Hantera bevakade mappar";
    public static final int manageWatchFolderHeadingFontSize = 20;
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

    public static final String closeWatchFolderButtonText = "Stäng";

    // Search
    public static final String searchTitle = "Sök";
    public static final String searchFieldLabelText = "Sök efter filmtitel:";
    public static final String searchHideSeenMoviesText = "Dölj sedda filmer";
    public static final String searchNoMatchText = "Din sökning matchade inte någon film.";
    public static final String searchInProgressText = "Laddar filmer...";

    public static final String searchOrderButtonMovieTitle = "Filmtitel";
    public static final String searchOrderButtonMovieTitleToolTip = "Sortera filmer efter titel";
    public static final String searchOrderButtonMovieYear = "År";
    public static final String searchOrderButtonMovieYearToolTip = "Sortera filmer efter år";
    public static final String searchOrderButtonMovieRating = "Betyg";
    public static final String searchOrderButtonMovieRatingToolTip = "Sortera filmer efter betyg";

    public static final ImageIcon searchToggleSeenButtonIcon = new ImageIcon(
            "img/eye_50_crossed_disabled.png");
    public static final ImageIcon searchToggleSeenButtonIconHide = new ImageIcon(
            "img/eye_50_crossed.png");
    public static final String toolTipsSearchSeen = "Visa endast ej sedda filmer (dölj sedda filmer)";
    public static final String toolTipsSearchSeenDisable = "Visa alla filmer";

    // Recommended movies
    public static final String recommendTitle = "Rekommendationer";
    public static final String recommendRefreshButtonText = "";
    public static final ImageIcon recommendRefreshButtonIcon = new ImageIcon(
            "img/Button-Refresh-icon_20.png");
    public static final String recommendRefreshNoMatchText = "Inga filmer i databasen.";
    public static final String recommendRefreshProgressText = "Laddar filmer...";

    // MovieElement
    public static final ImageIcon moviePlayButtonIcon = new ImageIcon(
            "img/Button-Play-icon_50.png");
    public static final ImageIcon movieSeenButtonIcon = new ImageIcon(
            "img/eye_50.png");
    public static final ImageIcon movieFavoriteButtonIcon = new ImageIcon(
            "img/Favorite-icon_50.png");
    public static final ImageIcon movieStarButtonIcon = new ImageIcon(
            "img/Button-Favorite-icon_50.png");
    public static final ImageIcon movieMoveUpButtonIcon = new ImageIcon(
            "img/navigate-up-icon.png");
    public static final ImageIcon movieMoveDownButtonIcon = new ImageIcon(
            "img/navigate-down-icon.png");

    public static final ImageIcon movieSeenButtonIconDisabled = new ImageIcon(
            "img/eye_50_disabled.png");
    public static final ImageIcon movieFavoriteButtonIconDisabled = new ImageIcon(
            "img/Favorite-icon_50_disabled.png");
    public static final ImageIcon movieStarButtonIconDisabled = new ImageIcon(
            "img/Button-Favorite-icon_50_disabled.png");

    public static final String movieNoYearText = "Okänt";

    public static final String movieAddToNewPlaylistText = "Lägg till i ny spellista";

    public static final int movieTitleFontSize = 24;
    public static final int movieYearFontSize = 16;

    // Menu
    public static final int menuHeight = 100;
    public static final int menuFontSize = 20;
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

    public static final String toolTipsWishDisable = "Ta bort från önskelista";
    public static final String toolTipsWish = "Lägg till i önskelista";
    public static final String toolTipsFavoriteDisable = "Ta bort från favoriter";
    public static final String toolTipsFavorite = "Lägg till i favoriter";
    public static final String toolTipsSeenDisable = "Markera som ej sedd";
    public static final String toolTipsSeen = "Markera som sedd";
    public static final String toolTipsPlay = "Spela upp film";
    public static final String toolTipsAddWatchFolder = "Hantera bevakade kataloger";
    public static final String toolTipsExit = "Avsluta";
    public static final String toolTipsRefresh = "Nya rekommendationer";
    public static final String toolTipsRating = "Sätt betyg på film";

    // ProfilePanel
    public static final String profileTitle = "Profil";
    public static final String profileAllMoviesList = "Alla filmer";
    public static final String profileFavoriteList = "Favoriter";
    public static final String profileSeenList = "Sedda filmer";
    public static final String profileWishList = "Önskelista";

    // HomePanel
    public static final String homeTitle = "Välkommen!";
    public static final int homePanelFontSize = 40;

    // Playlists
    public static final String playlistCreateNewMessage = "Namn på ny playlist";
    public static final String playlistCreateNewHeading = "Ny playlist";
    public static final Icon listsWishIcon = new ImageIcon(
            "img/Button-Favorite-icon_16.png");
    public static final Icon listsFavoriteIcon = new ImageIcon(
            "img/Favorite-icon-16.png");
    public static final Icon listsSeenIcon = new ImageIcon("img/eye_16.png");
    public static final int playlistDefaultTextSize = 12;
    public static final int playlistAllMoviesTextSize = 22;

    public static final String translationFailedText = "Översättningen misslyckades! Engelsk version: ";

    public static final Icon ratingRemoveRating = new ImageIcon(
            "img/rating-remove-vote.png");

    //public static final Icon ratingRemoveRating = new ImageIcon("img/rating-remove-vote.png");
    public static final String ratingRemoveRating = "img/rating-remove-vote.png";


}
