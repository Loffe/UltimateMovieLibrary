package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/**
 * AppFrame. The main window of the Ultimate Movie Library.
 */
public class AppFrame extends JFrame {

    private static final long serialVersionUID = 5297734322373835993L;
    private WatchFolderManagerDialog watchFolderManagerPanel;
    private MainPanel profilePanel;
    private DefaultMenuBar topMenu;
    private JPanel topPanel = new JPanel();
    private JPanel statusPanel;
    private JLabel titleLabel;
    public static JLabel loadingLabel;

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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                profilePanel.savePlaylistsOrder();
                dispose();
            }
        });

        profilePanel = new MainPanel();

        statusPanel = new JPanel();
        titleLabel = new JLabel(Localization.title);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel
                .getFont().getStyle(), Localization.titleFontSize));

        statusPanel.add(new JLabel(new ImageIcon(Localization.icon)));
        statusPanel.add(titleLabel);
        statusPanel.add(new JLabel("   "));
        statusPanel.add(Localization.loadingLabel);
        statusPanel.add(Localization.loadingTextLabel); 
        
        
        if (!WatchFolderManager.isScanInProgress() && !MovieInfoDownloader.isUpdateInProgress()) {
        Localization.loadingLabel.setVisible(false);
        }

        // Assemble menus
        initializeTopMenu();

        topPanel.setLayout(new BorderLayout());
        topPanel.add(statusPanel, BorderLayout.WEST);
        topPanel.add(topMenu, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.PAGE_START);
        getContentPane().add(profilePanel, BorderLayout.CENTER);

        setVisible(true);
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
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
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
}
