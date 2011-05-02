package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A panel to handle (add/remove) what folders to watch (for movies).
 */
public class WatchFolderManagerPanel extends JDialog {

    private static final long serialVersionUID = 5721254657654204228L;

    // A list of the visual components for every watched folder.
    private JPanel watchFoldersPanel = new JPanel();

    /**
     * Constructor. Create a new WatchFolderManagerPanel.
     * 
     * @param owner
     *            the parent window
     */
    public WatchFolderManagerPanel(Window owner) {
        super(owner, Localization.manageWatchFolderHeading,
                ModalityType.APPLICATION_MODAL);
        setVisible(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        // Add all the currently watched folders from database.
        for (WatchFolder watchFolder : WatchFolderManager.getAllWatchFolders()) {
            addFolderToList(watchFolder);
        }
    }

    /**
     * Add a new folder to the visual list of watched folders.
     * 
     * @param watchFolder
     *            the folder to add.
     */
    private void addFolderToList(WatchFolder watchFolder) {
        // Create a new visual panel to wrap and show the folder.
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
        // Add the folder to the visual list.
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
            // Add the folder to the database (and visual list) if needed.
            // This will also start scanning the folder.
            if (WatchFolderManager.addWatchFolder(folder))
                addFolderToList(folder);
        }
    }

    /**
     * Remove a folder from watch list.
     */
    private void removeFolder(WatchFolderPanel watchFolder) {
        // Get the real folder object.
        WatchFolder folder = watchFolder.getFolder();

        // Stop the scan and remove the folder from the database.
        WatchFolderManager.removeWatchFolder(folder);

        // Remove the folder from the visual list.
        watchFoldersPanel.remove(watchFolder);
        validate();
    }

}
