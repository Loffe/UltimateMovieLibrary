package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;

public class Menubar extends JButton implements ActionListener {

    private static final long serialVersionUID = -8430416772154328684L;
    private JButton searchItem;
    private JButton profileItem;
    private JButton watchFolderItem;

    public Menubar() {
        this.setEnabled(false);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        searchItem = new JButton(Localization.menuSearch);
        searchItem.addActionListener(this);

        profileItem = new JButton(Localization.menuProfile);
        profileItem.addActionListener(this);

        watchFolderItem = new JButton(Localization.menuWatchFolder);
        watchFolderItem.addActionListener(this);

        this.add(searchItem);
        this.add(profileItem);
        this.add(watchFolderItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == searchItem) {
            fireActionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, AppFrame.GuiPanel.Search
                            .toString()));
        } else if (source == profileItem) {
            fireActionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, AppFrame.GuiPanel.Profile
                            .toString()));
        } else if (source == watchFolderItem) {
            fireActionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, AppFrame.GuiPanel.WatchFolder
                            .toString()));
        }
    }
}
