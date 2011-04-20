package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class AppFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerPanel watchFolderManagerPanel;
    private SearchPanel searchPanel;
    private Menubar menu;

    public AppFrame() throws HeadlessException {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultimate Movie Library");
        setIconImage(new ImageIcon("img/video_16.png").getImage());

        initializeFullScreen();

        menu = new Menubar();
        menu.addActionListener(this);
        getContentPane().add(menu, BorderLayout.PAGE_END);

        watchFolderManagerPanel = new WatchFolderManagerPanel();
        searchPanel = new SearchPanel();

        setCurrentPanel(GuiPanel.Search);
    }

    public void setCurrentPanel(GuiPanel panel) {
        Container contentPane = getContentPane();
        BorderLayout layout = (BorderLayout) contentPane.getLayout();
        Component centerComponent = layout
                .getLayoutComponent(BorderLayout.CENTER);

        switch (panel) {
        case WatchFolder:
            if (centerComponent != null) {
                if (centerComponent != watchFolderManagerPanel) {
                    contentPane.remove(centerComponent);
                    contentPane.add(watchFolderManagerPanel,
                            BorderLayout.CENTER);
                }
            } else {
                contentPane.add(watchFolderManagerPanel, BorderLayout.CENTER);
            }
            break;
        case Search:
            if (centerComponent != null && centerComponent != searchPanel) {
                contentPane.remove(centerComponent);
                contentPane.add(searchPanel, BorderLayout.CENTER);
            }
            break;

        default:
            throw new NotImplementedException();
        }
        this.invalidate();
        this.repaint();

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

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiPanel panel = GuiPanel.valueOf(e.getActionCommand());
        setCurrentPanel(panel);
    }
}
