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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AppFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerDialog watchFolderManagerPanel;
    private ViewPanel searchPanel;
    private ProfilePanel profilePanel;
    private ViewPanel recomendPanel;
    private DefaultMenuBar botMenu;
    private DefaultMenuBar topMenu;
    private JPanel centerPanel;
    private JPanel homePanel;
    private JPanel topPanel = new JPanel();
    private JPanel infoPanel;
    private JLabel titleLabel;

    public AppFrame() throws HeadlessException {
        // scan the currently watch folders and look for new content
        WatchFolderManager.updateLibrary();
        setVisible(false); // Hide until ready, to avoid window "flashing"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Localization.title);
        setIconImage(Localization.icon);

        // TODO: decide on type of fullscreen
        initializeFullScreen();
        this.setMinimumSize(new Dimension(640, 480));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        watchFolderManagerPanel = new WatchFolderManagerDialog(this);
        watchFolderManagerPanel.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosing(WindowEvent e) {
                updateCurrentPanel();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowActivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }
        });

        centerPanel = new JPanel();
        centerPanel.setLayout(new CardLayout());

        searchPanel = new SearchPanel();
        recomendPanel = new RecommendPanel();
        profilePanel = new ProfilePanel();
        homePanel = new HomePanel(this);

        centerPanel.add(searchPanel, GuiPanel.Search.name());
        centerPanel.add(recomendPanel, GuiPanel.Recommend.name());
        centerPanel.add(profilePanel, GuiPanel.Profile.name());
        centerPanel.add(homePanel, GuiPanel.Home.name());
        
        infoPanel = new JPanel();
        titleLabel = new JLabel();
        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        infoPanel.add(new JLabel(new ImageIcon( Localization.icon )));

        infoPanel.add(titleLabel);

        // Assemble menus
        initializeTopMenu();
        initializeBotMenu();
        
        topPanel.setLayout(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(topMenu, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.PAGE_START);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(botMenu, BorderLayout.PAGE_END);

        setCurrentPanel(GuiPanel.Home);
        setVisible(true);
    }

    public void setCurrentPanel(GuiPanel panel) {
        
        CardLayout layout = (CardLayout) centerPanel.getLayout();
        switch(panel) {
        case Search:
            titleLabel.setText(Localization.title + " > " + Localization.searchTitle);
            searchPanel.update();
            break;
        case Recommend:
            titleLabel.setText(Localization.title + " > " + Localization.recommendTitle);
            recomendPanel.update();
            break;
        case Profile:
            titleLabel.setText(Localization.title + " > " + Localization.profileTitle);
            break;
        case Home:
            getContentPane().remove(botMenu);
        default:
            
            break;
        }
        layout.show(centerPanel, panel.name());
        
    }

    public void updateCurrentPanel() {
        searchPanel.update();
        recomendPanel.update();
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

    enum GuiPanel {
        Search, Recommend, Profile, Home
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiPanel panel = GuiPanel.valueOf(e.getActionCommand());
        setCurrentPanel(panel);
    }
}
