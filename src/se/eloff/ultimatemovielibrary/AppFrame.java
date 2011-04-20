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
import javax.swing.JPanel;

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
        Component currentCenterComponent = layout
                .getLayoutComponent(BorderLayout.CENTER);

        JPanel newPanel;
        switch (panel) {
        case WatchFolder:
            newPanel = watchFolderManagerPanel;
            break;
        case Search:
            newPanel = searchPanel;
            break;

        default:
            throw new NotImplementedException();
        }
        swapCenterComponent(currentCenterComponent, newPanel);
    }

    private void swapCenterComponent(Component currentCenterComponent,
            Component newPanel) {
        Container contentPane = getContentPane();
        if (currentCenterComponent == null) {
            contentPane.add(newPanel, BorderLayout.CENTER);
        } else if (currentCenterComponent != newPanel) {
            contentPane.remove(currentCenterComponent);
            contentPane.add(newPanel, BorderLayout.CENTER);
        }
        // Really force repaint of new panel
        contentPane.invalidate();
        contentPane.validate();
        contentPane.repaint();
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
