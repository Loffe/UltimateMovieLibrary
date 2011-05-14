package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * AppFrame. The main window of the Ultimate Movie Library.
 */
public class AppFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerDialog watchFolderManagerPanel;
    // private ViewPanel searchPanel;
    private ProfilePanel profilePanel;
    // private ViewPanel recomendPanel;
    // private DefaultMenuBar botMenu;
    private DefaultMenuBar topMenu;
    private JPanel centerPanel;
    // private JPanel homePanel;
    private JPanel topPanel = new JPanel();
    private JPanel infoPanel;
    private JLabel titleLabel;

    // private JLabel breadCrumbLabel;

    /**
     * Constructor. Creates a new AppFrame window.
     */
    public AppFrame() throws HeadlessException {
        // scan the currently watch folders and look for new content
        WatchFolderManager.updateLibrary();

        setVisible(false); // Hide until ready, to avoid window "flashing"
        setTitle(Localization.title);
        setIconImage(Localization.icon);

        // Try to go into fullscreen mode!
        initializeFullScreen();
        this.setMinimumSize(new Dimension(640, 480));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        watchFolderManagerPanel = new WatchFolderManagerDialog(this);
        watchFolderManagerPanel.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                updateCurrentPanel();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                profilePanel.savePlaylistsOrder();
                dispose();
            }
        });

        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());

        // searchPanel = new SearchPanel();
        // recomendPanel = new RecommendPanel();
        profilePanel = new ProfilePanel();
        // homePanel = new HomePanel(this);

        // centerPanel.add(searchPanel, GuiPanel.Search.name());
        // centerPanel.add(recomendPanel, GuiPanel.Recommend.name());
        centerPanel.add(profilePanel, GuiPanel.Profile.name());
        // centerPanel.add(homePanel, GuiPanel.Home.name());

        infoPanel = new JPanel();
        titleLabel = new JLabel(Localization.title);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel
                .getFont().getStyle(), Localization.titleFontSize));

        // breadCrumbLabel = new JLabel();
        // breadCrumbLabel.setFont(new Font(breadCrumbLabel.getFont().getName(),
        // breadCrumbLabel.getFont().getStyle(),
        // Localization.breadCrumbFontSize));

        infoPanel.add(new JLabel(new ImageIcon(Localization.icon)));
        infoPanel.add(titleLabel);
        // infoPanel.add(breadCrumbLabel);

        // Assemble menus
        initializeTopMenu();
        // botMenu = new BotMenuBar();
        // botMenu.addActionListener(this);

        topPanel.setLayout(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(topMenu, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.PAGE_START);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        // getContentPane().add(botMenu, BorderLayout.PAGE_END);

        setCurrentPanel(GuiPanel.Profile);
        setVisible(true);
    }

    /**
     * Change the current panel to the specified GuiPanel.
     * 
     * @param panel
     *            the panel to switch to.
     */
    public void setCurrentPanel(GuiPanel panel) {
        // botMenu.setVisible(true);
        CardLayout layout = (CardLayout) centerPanel.getLayout();
        /*
         * switch (panel) { case Search:
         * breadCrumbLabel.setText(Localization.searchTitle);
         * searchPanel.update(); break; case Recommend:
         * breadCrumbLabel.setText(Localization.recommendTitle);
         * recomendPanel.update(); break; case Profile: //
         * breadCrumbLabel.setText(Localization.profileTitle); break; case Home:
         * breadCrumbLabel.setText(Localization.homeTitle);
         * botMenu.setVisible(false); break; }
         */
        layout.show(centerPanel, panel.name());
    }

    /**
     * Update current panel. Refresh content.
     */
    public void updateCurrentPanel() {
        // searchPanel.update();
        // recomendPanel.update();
        profilePanel.update();
    }

    /**
     * Initialize the top menu.
     */
    private void initializeTopMenu() {
        topMenu = new DefaultMenuBar();
        topMenu.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton watchFolderItem = new JButton(
                Localization.menuManageWatchFolderText,
                Localization.menuManageWatchFolderIcon);
        watchFolderItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Pop the modal dialog
                watchFolderManagerPanel.setVisible(true);
            }
        });
        topMenu.addButton(watchFolderItem);

        watchFolderItem.setToolTipText(Localization.toolTipsAddWatchFolder);

        JButton exitItem = new JButton(Localization.menuExitText,
                Localization.menuExitIcon);
        exitItem.setToolTipText(Localization.toolTipsExit);
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                profilePanel.savePlaylistsOrder();
                dispose();
                // TODO: Is a System.exit(0); needed if scans or so are running?
            }
        });
        topMenu.addButton(exitItem);
    }

    /**
     * Go to fullscreen mode if possible.
     */
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
            // if (gd.isFullScreenSupported()) {
            // gd.setFullScreenWindow(this);
            // } else {
            // Can't run fullscreen, need to bodge around it (setSize to
            // screen size, etc)
            setSize(width, height);
            // }
            this.setVisible(true);
            // Your business logic here
        } finally {
            if (gd != null)
                gd.setFullScreenWindow(null);
        }
    }

    /*
     * Enumeration of all the GuiPanels able to be shown in the center panel.
     */
    enum GuiPanel {
        Profile
        // Home, Search, Recommend
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiPanel panel = GuiPanel.valueOf(e.getActionCommand());
        setCurrentPanel(panel);
    }

}
