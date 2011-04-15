package se.eloff.ultimatemovielibrary;

import javax.swing.ImageIcon;

/**
 * Localization is used to make GUI-strings and Icons independent of the rest of
 * the code.
 */
public final class Localization {

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

}
