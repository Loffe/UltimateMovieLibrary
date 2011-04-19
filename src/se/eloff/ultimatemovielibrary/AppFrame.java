package se.eloff.ultimatemovielibrary;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AppFrame extends JFrame {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerPanel watchFolderManagerPanel;
    private SearchPanel searchPanel;

    public AppFrame() throws HeadlessException {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultimate Movie Library");
        setIconImage(new ImageIcon("img/video_16.png").getImage());

        initializeFullScreen();

        watchFolderManagerPanel = new WatchFolderManagerPanel();
        searchPanel = new SearchPanel();

        setCurrentPanel(GuiPanel.Search);
    }

    public void setCurrentPanel(GuiPanel panel) {
        switch (panel) {
        case WatchFolder:
            setContentPane(watchFolderManagerPanel);
            break;
        case Search:
            setContentPane(searchPanel);
            break;

        default:
            throw new NotImplementedException();
        }

    }

    private void initializeFullScreen() {
        this.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        DisplayMode displayMode = gd.getDisplayMode();
        int width, height;
        if (displayMode != null) {
            width = displayMode.getWidth();
            height = displayMode.getHeight();
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            width = (int) screenSize.getWidth();
            height = (int) screenSize.getHeight();
        }

        try {
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(this);
            } else {
                // Can't run fullscreen, need to bodge around it (setSize to
                // screen size, etc)
                setSize(width, height);
            }
            this.setVisible(true);
            // Your business logic here
        } finally {
            if (gd != null)
                gd.setFullScreenWindow(null);
        }
    }

    enum GuiPanel {
        WatchFolder, Search, Profile
    }
}
