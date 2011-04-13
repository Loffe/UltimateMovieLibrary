package se.eloff.ultimatemovielibrary;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * A panel to handle (add/remove) what folders to watch (for movies).
 * 
 * @author freddieboi
 * 
 */
public class WatchFolderPanel extends JPanel {

    private static final long serialVersionUID = 5721254657654204228L;

    // The list model of the watch folders (where the actual data is).
    private DefaultListModel watchFoldersListModel = new DefaultListModel();

    // Create a list view from the list model.
    private JList watchFoldersListView = new JList(watchFoldersListModel);

    public WatchFolderPanel() {
        setLayout(new FlowLayout());

        // Only allow one item to be selected at a time.
        watchFoldersListView
                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Make the list scrollable.
        JScrollPane scrollPane = new JScrollPane(watchFoldersListView);
        add(scrollPane);

        // Create a button to add new folders.
        JButton addFolderButton = new JButton("Add folder");
        addFolderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFolder();
            }

        });
        add(addFolderButton);

        // Create a button to remove folders
        JButton removeFolderButton = new JButton("Remove selected folder");
        removeFolderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeFolder();
            }

        });
        add(removeFolderButton);

        // Fix the layout, plz!!
        this.invalidate();
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
            // Add the selected folder path.
            String folderPath = fileChooser.getSelectedFile().toString();
            watchFoldersListModel.addElement(folderPath);
        }
    }

    /**
     * Remove a folder from watch list.
     */
    private void removeFolder() {
        // Get the currently selected folder from the list
        int index = watchFoldersListView.getSelectedIndex();
        if (index < 0) {
            // Nothing is selected! Do nothing...
            return;
        }
        // Remove the folder.
        watchFoldersListModel.remove(index);
    }

}
