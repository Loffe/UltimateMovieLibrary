package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A panel to handle (add/remove) what folders to watch (for movies).
 */
public class WatchFolderManagerPanel extends JPanel {

    private static final long serialVersionUID = 5721254657654204228L;

    // A list of all the currently watched folders.
    private ArrayList<WatchFolder> watchFolders = new ArrayList<WatchFolder>();

    // A list of the view components for every watched folder.
    private JPanel watchFoldersPanel = new JPanel();

    public WatchFolderManagerPanel() {
        setLayout(new BorderLayout());

        List<WatchFolder> folders = WatchFolderManager.getAllWatchFolders();
        for (WatchFolder watchFolder2 : folders) {
            addFolderToList(watchFolder2);
        }

        // Create a panel for info and buttons at the top.
        JPanel topPanel = new JPanel(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);

        // Add a heading
        JLabel heading = new JLabel(Localization.manageWatchFolderHeading);
        topPanel.add(heading, BorderLayout.NORTH);

        // Add a description
        JLabel description = new JLabel(
                Localization.manageWatchFolderDescription);
        topPanel.add(description, BorderLayout.WEST);

        // Create a button panel with a button to add new folders.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JButton addFolderButton = new JButton(
                Localization.addWatchFolderButtonText,
                Localization.addWatchFolderButtonIcon);
        addFolderButton
                .setToolTipText(Localization.addWatchFolderButtonToolTip);
        addFolderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFolder();
            }

        });
        buttonPanel.add(addFolderButton);

        // Make folders position along Y-axis.
        watchFoldersPanel.setLayout(new BoxLayout(watchFoldersPanel,
                BoxLayout.Y_AXIS));
        watchFoldersPanel.setBackground(Color.WHITE);

        // Create inner and outer panel to achieve TOP-LEFT alignment.
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBackground(Color.WHITE);
        innerPanel.add(watchFoldersPanel, BorderLayout.WEST);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);
        outerPanel.add(innerPanel, BorderLayout.NORTH);

        // Make the list of folders scrollable
        JScrollPane scrollPane = new JScrollPane(outerPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addFolderToList(WatchFolder watchFolder) {
        // Start scanning the folder.
        // TODO: Also add folder to database.

        final WatchFolderPanel watchFolderPanel = new WatchFolderPanel(
                watchFolder);
        // Add a listener on the remove button
        watchFolderPanel.getRemoveButton().addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Confirm before removing
                        int result = JOptionPane.showConfirmDialog(
                                watchFoldersPanel,
                                Localization.removeWatchFolderConfirmationText
                                        + watchFolderPanel.getFolder()
                                                .getFolderPath());
                        if (result == JOptionPane.YES_OPTION) {
                            removeFolder(watchFolderPanel);
                        }
                    }
                });

        getWatchFolders().add(watchFolderPanel.getFolder());
        watchFoldersPanel.add(watchFolderPanel);
        validate();
    }

    /**
     * Add a folder to watch list.
     */
    private void addFolder() {
        // Create a file chooser.
        JFileChooser fileChooser = new JFileChooser();

        // Only allow to select directories.
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Show the file chooser dialog and get the result.
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected folder path.
            String folderPath = fileChooser.getSelectedFile().toString();

            // Create a view of the folder.
            WatchFolder folder = new WatchFolder(folderPath, 0L);
            if (WatchFolderManager.addWatchFolder(folder))
                addFolderToList(folder);
        }
    }

    /**
     * Remove a folder from watch list.
     */
    private void removeFolder(WatchFolderPanel watchFolder) {
        WatchFolder folder = watchFolder.getFolder();
        // Stop the scan
        // TODO: Also remove folder from database.
        WatchFolderManager.removeWatchFolder(folder);

        // Remove the folder.
        getWatchFolders().remove(folder);
        watchFoldersPanel.remove(watchFolder);
        validate();
    }

    /**
     * Get all the folders to watch.
     * 
     * @return a list of all the folders to watch.
     */
    public ArrayList<WatchFolder> getWatchFolders() {
        return watchFolders;
    }

    /**
     * Test the WatchFolderManager
     * 
     * @param args
     */
    public static void main(String[] args) {
        WatchFolderManagerPanel panel = new WatchFolderManagerPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

}
