package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

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

    public static final ImageIcon loader = new ImageIcon("img/mozilla_blu.gif");
    public static JLabel loadingTextLabel = new JLabel();
    public static JLabel loadingLabel = new JLabel(loader);
    public static String scanningFoldersText = "Söker efter filmer i bevakade kataloger...";
    public static String updatingMoviesInfoText = "Hämtar och uppdaterar information för film";

    // Watch Folder Manager
    public static final ImageIcon watchFolderIcon = new ImageIcon(
            "img/folder_32.png");
    public static final int watchFolderTextSize = 14;
    public static final String manageWatchFolderHeading = "Hantera bevakade kataloger";
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
    public static final int searchFieldLabelTextSize = 14;
    public static final String searchHideSeenMoviesText = "Dölj sedda filmer";
    public static final String searchNoMatchText = "Din sökning matchade inte någon film.";
    public static final String searchInListNoMatch = "Din lista är tom";
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
    public static final String recommendRefreshButtonText = "Rekommendationer";
    public static final String recommendRefreshButtonToolTip = "Uppdatera och visa rekommenderade filmer";
    public static final ImageIcon recommendRefreshButtonIcon = new ImageIcon(
            "img/Button-Refresh-icon_20.png");
    public static final String recommendRefreshNoMatchText = "Inga filmer i databasen.";
    public static final String recommendRefreshProgressText = "Laddar filmer...";
    public static final int numberOfRecommendedMovies = 3;

    // MovieElement
    public static final ImageIcon moviePlayButtonIcon = new ImageIcon(
            "img/Button-Play-icon_40.png");
    public static final ImageIcon moviePlayButtonHoverIcon = new ImageIcon(
    "img/Button-Play-icon-hover_40.png");
    public static final ImageIcon movieSeenButtonIcon = new ImageIcon(
            "img/eye_40.png");
    public static final ImageIcon movieFavoriteButtonIcon = new ImageIcon(
            "img/Favorite-icon_40.png");
    public static final ImageIcon movieStarButtonIcon = new ImageIcon(
            "img/Button-Favorite-icon_40.png");

    public static final ImageIcon movieSeenButtonIconDisabled = new ImageIcon(
            "img/eye_40_disabled.png");
    public static final ImageIcon movieFavoriteButtonIconDisabled = new ImageIcon(
            "img/Favorite-icon_40_disabled.png");
    public static final ImageIcon movieStarButtonIconDisabled = new ImageIcon(
            "img/Button-Favorite-icon_40_disabled.png");
    
    public static BufferedImage movieMoveDownButtonIcon;
    public static BufferedImage movieMoveDownButtonHoverIcon;
    public static BufferedImage movieMoveUpButtonIcon;
    public static BufferedImage movieMoveUpButtonHoverIcon;
    static {
        try {
            System.out.println("read image icons");
            movieMoveDownButtonIcon = ImageIO.read(new File("img/navigate-down-icon.png"));
            movieMoveDownButtonHoverIcon = ImageIO.read(new File("img/navigate-down-hover-icon.png"));
            movieMoveUpButtonIcon = ImageIO.read(new File("img/navigate-up-icon.png"));
            movieMoveUpButtonHoverIcon = ImageIO.read(new File("img/navigate-up-hover-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String movieNoYearText = "Okänt";

    public static final String movieAddToNewPlaylistText = "Lägg till i ny spellista";

    public static final int movieTitleFontSize = 24;
    public static final int movieYearFontSize = 16;

    public static final Image ratingRemoveRatingImage = new ImageIcon(
            "img/rating-remove-vote.png").getImage();
    public static final Image ratingRemoveRatingActiveImage = new ImageIcon(
            "img/rating-remove-vote-active.png").getImage();

    public static final String managePlaylistsMenuTitle = "Hantera listor";
    public static final String managePlaylistsMenuDescriptionText = "Lägg till film i befintlig lista:";
    public static final Icon managePlaylistsButtonIcon = new ImageIcon(
            "img/arrow-down.png");
    public static final String managePlaylistsButtonToolTipText = "Hantera listor";

    // Menu
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

    public static final String toolTipsMoveUpButton = "Flytta upp film";
    public static final String toolTipsMoveDownButton = "Flytta ner film";
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
    public static final String toolTipsRemoveRating = "Ta bort betyg";

    // ProfilePanel
    public static final String profileTitle = "Profil";
    public static final String profileAllMoviesList = "Alla filmer";
    public static final String profileFavoriteList = "Favoriter";
    public static final String profileSeenList = "Sedda filmer";
    public static final String profileWishList = "Önskelista";

    public static final Color selectedListElementColor = Color.DARK_GRAY;
    public static final Color HoverListElementColor = new Color(50, 50, 50);
    // new Color(0, 200, 251);

    // MovieInfo
    public static final int movieCoverWidth = 154;
    public static final int movieCoverHeight = 240;
    public static final int moviePlotWidth = 300;
    public static final int moviePlotHeight = 150;
    public static final int movieInfoWidth = 310;
    public static final int movieInfoHeight = 310;
    public static final String moviePlotLabel = "Handling: ";
    public static final String movieGenreLabel = "Genre: ";
    public static final String movieRatingLabel = "Online-betyg: ";
    public static final String movieDirectorLabel = "Regissör: ";
    public static final String movieCastLabel = "Skådespelare: ";
    public static final String movieInfoStandardCover = "img/no-poster.png";
    public static final String translationFailedText = "Översättningen misslyckades! Engelsk version: ";
    public static final String unknownPlotText = "Ingen handling hittades.";
    public static final String unknownGenreText = "Inga genrer hittades.";
    public static final String unknownRatingText = "Inget betyg hittades.";
    public static final String unknownDirectorText = "Ingen regissör hittades.";
    public static final String unknownCastText = "Inga skådespelare hittades.";
    public static final Color coverBorderColor = new Color(33, 33, 33);
    public static final int minimumCoverLight = -15;
    public static final int animationDelayMs = 16;
    public static final int preDelayDarken = 15;

    // Playlists
    public static final String playlistCreateNewMessage = "Namn på ny spellista";
    public static final String playlistCreateNewHeading = "Ny spellista";
    public static final String playlistDelete = "Ta bort";
    public static final Icon listsWishIcon = new ImageIcon(
            "img/Button-Favorite-icon_16.png");
    public static final Icon listsFavoriteIcon = new ImageIcon(
            "img/Favorite-icon-16.png");
    public static final Icon listsSeenIcon = new ImageIcon("img/eye_16.png");
    public static final Icon listsAddIcon = new ImageIcon("img/plus_16.png");
    public static final int playlistDefaultTextSize = 14;
    public static final int playlistAllMoviesTextSize = 22;
    public static final int playlistCreateNewTextSize = playlistDefaultTextSize;
    
}
