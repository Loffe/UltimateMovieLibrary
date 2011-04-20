package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

public class AppFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerPanel watchFolderManagerPanel;
    private SearchPanel searchPanel;
    private Menubar menu;
    private JPanel centerPanel;

    public AppFrame() throws HeadlessException {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultimate Movie Library");
        setIconImage(new ImageIcon("img/video_16.png").getImage());

        // TODO: decide on type of fullscreen
        // initializeFullScreen();
        this.setMinimumSize(new Dimension(640, 480));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        menu = new Menubar();
        menu.addActionListener(this);

        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());

        watchFolderManagerPanel = new WatchFolderManagerPanel();
        searchPanel = new SearchPanel();

        centerPanel.add(watchFolderManagerPanel, GuiPanel.WatchFolder.name());
        centerPanel.add(searchPanel, GuiPanel.Search.name());

        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(menu, BorderLayout.PAGE_END);

        setCurrentPanel(GuiPanel.Search);
    }

    public void setCurrentPanel(GuiPanel panel) {
        CardLayout layout = (CardLayout) centerPanel.getLayout();
        layout.show(centerPanel, panel.name());
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
