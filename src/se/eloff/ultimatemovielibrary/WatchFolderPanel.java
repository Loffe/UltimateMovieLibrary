package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel to show and manage a watch folder.
 */
public class WatchFolderPanel extends JPanel {

    private static final long serialVersionUID = 3051972291331973602L;

    // The actual WatchFolder.
    private WatchFolder folder;

    // The remove button.
    private JButton removeButton = new JButton(new ImageIcon(
            "img/delete_16.png"));

    /**
     * Constructor. Creates a new wrapper to show and manage a WatchFolder.
     * 
     * @param folder
     *            the WatchFolder to show.
     */
    public WatchFolderPanel(WatchFolder folder) {
        this.folder = folder;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(595, 35));
        setBackground(Color.WHITE);

        // Add a folder icon.
        add(new JLabel(new ImageIcon("img/folder_32.png")), BorderLayout.WEST);

        // Add a label with the actual folder path.
        add(new JLabel(folder.getFolderPath()), BorderLayout.CENTER);

        // Add a remove button (Note: listeners rather added from parent).
        JPanel buttonPanel = new JPanel();
        removeButton.setToolTipText("Stop watching this folder");
        buttonPanel.add(removeButton);
        buttonPanel.setBackground(Color.WHITE);
        add(buttonPanel, BorderLayout.EAST);

    }

    /**
     * Get the wrapped WatchFolder object.
     * 
     * @return the watch folder shown by this panel.
     */
    public WatchFolder getFolder() {
        return folder;
    }

    /**
     * Get the remove button (to enable adding listeners from elsewhere).
     * 
     * @return the remove button.
     */
    public JButton getRemoveButton() {
        return removeButton;
    }

}
