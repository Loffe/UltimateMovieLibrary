package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerPanel watchFolderManagerPanel;
    private SearchPanel searchPanel;
    private RecommendPanel recomendPanel;
    private DefaultMenuBar botMenu;
    private DefaultMenuBar topMenu;
    private JPanel centerPanel;

    public AppFrame() throws HeadlessException {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Localization.title);
        setIconImage(Localization.icon);

        // TODO: decide on type of fullscreen
        // initializeFullScreen();
        this.setMinimumSize(new Dimension(640, 480));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());

        watchFolderManagerPanel = new WatchFolderManagerPanel();
        searchPanel = new SearchPanel();
        recomendPanel = new RecommendPanel();

        centerPanel.add(watchFolderManagerPanel, GuiPanel.WatchFolder.name());
        centerPanel.add(searchPanel, GuiPanel.Search.name());
        centerPanel.add(recomendPanel, GuiPanel.Recommend.name());

        // Assemble menus
        initializeTopMenu();
        initializeBotMenu();

        getContentPane().add(topMenu, BorderLayout.PAGE_START);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(botMenu, BorderLayout.PAGE_END);

        setCurrentPanel(GuiPanel.Search);
    }

    public void setCurrentPanel(GuiPanel panel) {
        CardLayout layout = (CardLayout) centerPanel.getLayout();
        layout.show(centerPanel, panel.name());
    }

    private void initializeBotMenu() {
        // TODO: Break out to its own class?
        botMenu = new DefaultMenuBar();
        botMenu.addActionListener(this);
        botMenu.setLayout(new GridLayout(1, 3));

        JButton searchItem = new JButton(Localization.menuSearchText);
        searchItem.setActionCommand(GuiPanel.Search.toString());
        botMenu.addButton(searchItem);

        JButton recommendItem = new JButton(Localization.menuRecommendText);
        recommendItem.setActionCommand(GuiPanel.Recommend.toString());
        botMenu.addButton(recommendItem);

        JButton profileItem = new JButton(Localization.menuProfileText);
        profileItem.setActionCommand(GuiPanel.Profile.toString());
        botMenu.addButton(profileItem);
    }

    private void initializeTopMenu() {
        // TODO: Break out to its own class?
        topMenu = new DefaultMenuBar();
        topMenu.addActionListener(this);
        topMenu.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // TODO: Should this item pop a modal dialog instead
        // as in the prototype?
        JButton watchFolderItem = new JButton(
                Localization.menuManageWatchFolderText,
                Localization.menuManageWatchFolderIcon);
        watchFolderItem.setActionCommand(AppFrame.GuiPanel.WatchFolder
                .toString());
        topMenu.addButton(watchFolderItem);

        JButton exitItem = new JButton(Localization.menuExitText,
                Localization.menuExitIcon);
        exitItem.setActionCommand(AppFrame.GuiPanel.Profile.toString());
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // TODO: Is a System.exit(0); needed if scans or so are running?
            }
        });
        topMenu.addButton(exitItem);
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
        WatchFolder, Search, Recommend, Profile
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiPanel panel = GuiPanel.valueOf(e.getActionCommand());
        setCurrentPanel(panel);
    }
}
