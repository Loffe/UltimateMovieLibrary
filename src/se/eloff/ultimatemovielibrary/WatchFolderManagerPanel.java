package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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

        // Add a heading
        add(new JLabel("Manage watched folders"), BorderLayout.PAGE_START);

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

        // Create a button panel with a button to add new folders.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        add(buttonPanel, BorderLayout.PAGE_END);

        JButton addFolderButton = new JButton(
                "Add another folder to watch ...", new ImageIcon(
                        "img/plus_32.png"));
        addFolderButton.setToolTipText("Add another folder to watch");
        addFolderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFolder();
            }

        });
        buttonPanel.add(addFolderButton);
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
            final WatchFolderPanel watchFolder = new WatchFolderPanel(folder);
            // Add a listener on the remove button
            watchFolder.getRemoveButton().addActionListener(
                    new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            removeFolder(watchFolder);
                        }
                    });

            getWatchFolders().add(watchFolder.getFolder());
            watchFoldersPanel.add(watchFolder);
            validate();
        }
    }

    /**
     * Remove a folder from watch list.
     */
    private void removeFolder(WatchFolderPanel watchFolder) {
        // Remove the folder.
        getWatchFolders().remove(watchFolder.getFolder());
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

}
