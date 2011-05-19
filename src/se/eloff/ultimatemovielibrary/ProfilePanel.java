package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.j256.ormlite.dao.Dao;

public class ProfilePanel extends JPanel implements DocumentListener {

    private final class PlaylistTransferHandler extends TransferHandler {

        private static final long serialVersionUID = -7206124317179587726L;

        public boolean canImport(TransferHandler.TransferSupport support) {
            JList tmp = (JList) support.getComponent();
            JList.DropLocation dl = (JList.DropLocation) support
                    .getDropLocation();
            int index = dl.getIndex();
            if (index < Playlist.fixedPlaylists.length
                    || tmp.getSelectedIndex() < Playlist.fixedPlaylists.length)
                return false;
            return true;
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            JList tmp = (JList) support.getComponent();
            JList.DropLocation dl = (JList.DropLocation) support
                    .getDropLocation();
            int index = dl.getIndex();

            listModel
                    .add(index, listModel.getElementAt(tmp.getSelectedIndex()));

            listModel.remove(tmp.getSelectedIndex());
            return true;

        }
        

        protected Transferable createTransferable(JComponent c) {
            return new StringSelection("Whaaat?");
        }

        public int getSourceActions(JComponent c) {
            return MOVE;
        }
    }

    private static final long serialVersionUID = 8595144249306891196L;

    private JLabel titleLabel;
    private JTextField searchTextField;
    private JCheckBox hideSeenMoviesCheckBox;

    private JList lists;
    private JToggleButton recommendedMoviesButton;
    private Box centerBox;
    private RecommendPanel recommendPanel;
    private JPanel addNewPlaylistPanel;
    private JLabel addPlaylistLabel;
    public ResultPanel resultPanel;
    protected MovieInfoPanel movieInfoPanel;
    private String title;

    private boolean showsRecommended = false;

    private ListElement selectedElement = null;

    public void setSelectedElement(ListElement element) {
        if (element != null) {
            if ((selectedElement != null)
                    || selectedElement == null)
                movieInfoPanel.refresh(element.getMovie());
        } else
            movieInfoPanel.resetInfo();
        selectedElement = element;
    }

    private ListDataListener playlistListener = new ListDataListener() {
        @Override
        public void intervalRemoved(ListDataEvent e) {
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            listModel.addElement(e.getSource());
        }
    };

    private DefaultListModel listModel;

    public ProfilePanel() {
        title = (Localization.profileTitle);
        initComponents();
        lists.setSelectedIndex(0);
        resultPanel.search();
    }

    private void initComponents() {
        resultPanel = new ResultPanel(this) {

            private static final long serialVersionUID = 8984741396097575708L;

            @Override
            public void search() {
                try {
                    Playlist selectedList = (Playlist) lists.getSelectedValue();
                    assert selectedList != null;
                    // If its the "all movies" list...
                    String name = searchTextField.getText();
                    if (selectedList != null) {
                        if (selectedList.getId() == 1) {
                            hideSeenMoviesCheckBox.setEnabled(true);
                            lastSearchId = MovieSearchProvider.searchByName(
                                    name, resultPanel, getOrderColumn(),
                                    isOrderAscending(),
                                    hideSeenMoviesCheckBox.isSelected());
                        } else if (selectedList.getId() == Playlist.SEEN_LIST_ID) {
                            hideSeenMoviesCheckBox.setSelected(false);
                            hideSeenMoviesCheckBox.setEnabled(false);
                            lastSearchId = MovieSearchProvider
                                    .searchByNameSeen(name, resultPanel,
                                            getOrderColumn(),
                                            isOrderAscending(), true);
                        } else {
                            hideSeenMoviesCheckBox.setEnabled(true);
                            lastSearchId = MovieSearchProvider.searchByList(
                                    name, resultPanel, getOrderColumn(),
                                    isOrderAscending(), selectedList,
                                    hideSeenMoviesCheckBox.isSelected());
                        }
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        };

        buildPlaylistPanel();

        Box searchBox = buildSearchPanel();

        this.setLayout(new BorderLayout());

        recommendPanel = new RecommendPanel();
        recommendedMoviesButton = new JToggleButton(
                Localization.recommendRefreshButtonText,
                Localization.recommendRefreshButtonIcon);
        recommendedMoviesButton
                .setToolTipText(Localization.recommendRefreshButtonToolTip);

        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(lists, BorderLayout.NORTH);

        addNewPlaylistPanel = new JPanel();
        addNewPlaylistPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        addPlaylistLabel = new JLabel(Localization.playlistCreateNewHeading);
        addPlaylistLabel.setFont(new Font(addPlaylistLabel.getFont().getName(),
                Font.BOLD,
                Localization.playlistCreateNewTextSize));
        addPlaylistLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showCreatePlaylist(null);
            }

        });
        addPlaylistLabel.setIcon(Localization.listsAddIcon);
        addNewPlaylistPanel.add(addPlaylistLabel);

        lists.setOpaque(false);

        // Hax to make focus leave the newPlaylistInput when this panel is
        // pressed.
        // TODO: Although, the user could click anywhere. Fix needed supporting
        // all panels...
        addNewPlaylistPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        });

        leftPanel.add(addNewPlaylistPanel, BorderLayout.CENTER);
        JPanel recommendButtonPanel = new JPanel();
        recommendButtonPanel.add(Box.createRigidArea(new Dimension(0, 7)));
        recommendButtonPanel.add(recommendedMoviesButton, BorderLayout.SOUTH);
        recommendButtonPanel.add(Box.createRigidArea(new Dimension(0, 7)));
        leftPanel.add(recommendButtonPanel, BorderLayout.SOUTH);
        recommendedMoviesButton.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                // TODO Auto-generated method stub
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!showsRecommended) {
                        ProfilePanel.this.remove(movieInfoPanel);
                        ProfilePanel.this.remove(centerBox);

                        ProfilePanel.this.add(recommendPanel,
                                BorderLayout.CENTER);
                        ProfilePanel.this.revalidate();
                        lists.clearSelection();
                    }
                    recommendPanel.refresh(ProfilePanel.this.getWidth());
                    showsRecommended = true;
                } else if (showsRecommended) {
                    recommendedMoviesButton.setSelected(true);
                }
            }
        });

        this.add(leftPanel, BorderLayout.WEST);

        centerBox = Box.createVerticalBox();
        centerBox.add(searchBox);
        centerBox.add(resultPanel);
        movieInfoPanel = new MovieInfoPanel(this, false);
        this.add(centerBox, BorderLayout.CENTER);
        this.add(movieInfoPanel, BorderLayout.EAST);
    }

    private Box buildSearchPanel() {
        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(200, 30));
        searchTextField.setMaximumSize(new Dimension(400, 30));

        titleLabel = new JLabel(Localization.searchFieldLabelText);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel.getFont().getStyle(), Localization.searchFieldLabelTextSize));

        hideSeenMoviesCheckBox = new JCheckBox(
                Localization.searchHideSeenMoviesText);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);

        hideSeenMoviesCheckBox.setToolTipText(Localization.toolTipsSearchSeen);
        hideSeenMoviesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultPanel.search();
            }
        });

        Box searchBox = Box.createHorizontalBox();
        searchBox.add(Box.createRigidArea(new Dimension(20, 20)));
        searchBox.add(titleLabel);
        searchBox.add(searchTextField);
        searchBox.add(Box.createRigidArea(new Dimension(20, 20)));
        searchBox.add(Box.createHorizontalGlue());
        searchBox.add(hideSeenMoviesCheckBox);
        searchBox.add(Box.createRigidArea(new Dimension(280, 0)));
        return searchBox;
    }

    private void buildPlaylistPanel() {
        listModel = new DefaultListModel();
        try {
            refreshPlaylists(listModel);

            DatabaseManager.getInstance().addPlaylistChangeListener(
                    playlistListener);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final JPopupMenu popupMenu = buildPlaylistPopupMenu();
        lists = new JList(listModel) {

            private static final long serialVersionUID = 6784382107114083176L;

            public Dimension getPreferredSize() {
                // Try to find the dimension of the list
                return new Dimension(100, (int)lists.getMinimumSize().getHeight());
            }
        };
        lists.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = lists.locationToIndex(e.getPoint());
                lists.setSelectedIndex(index);
                if (index < Playlist.fixedPlaylists.length
                        || index < Playlist.fixedPlaylists.length)
                    return;
                if (SwingUtilities.isRightMouseButton(e)
                        && !lists.isSelectionEmpty()) {
                    popupMenu.show(lists, e.getX(), e.getY());
                }
            }
        });
        lists.setSelectedIndex(0);
        lists.setCellRenderer(new PlaylistCellRenderer());
        lists.setPreferredSize(new Dimension(200, 10));
        lists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lists.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                resultPanel.search();
                if (showsRecommended) {
                    ProfilePanel.this.remove(recommendPanel);
                    ProfilePanel.this.add(centerBox, BorderLayout.CENTER);
                    ProfilePanel.this.add(movieInfoPanel, BorderLayout.EAST);

                    movieInfoPanel.repaint();
                    centerBox.repaint();

                    showsRecommended = false;
                    recommendedMoviesButton.setSelected(false);
                }
            }
        });

        lists.setDragEnabled(true);
        lists.setDropMode(DropMode.INSERT);
        lists.setTransferHandler(new PlaylistTransferHandler());
    }

    private JPopupMenu buildPlaylistPopupMenu() {
        final JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(new AbstractAction(Localization.playlistDelete) {
            private static final long serialVersionUID = 3776371380960369970L;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Playlist playlist = (Playlist) lists.getSelectedValue();
                    playlist.delete();
                    listModel.removeElement(playlist);
                    lists.setSelectedIndex(0);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        return popupMenu;
    }

    private void createNewPlaylist(String playlistName, LocalMovie movie) {
        Dao<MovieList, Integer> movieListDb;
        try {
            movieListDb = DatabaseManager.getInstance().getMovieListDao();
            Playlist playlist = DatabaseManager.getInstance().createPlaylist(
                    playlistName);

            if (movie != null) {
                movieListDb.create(new MovieList(movie, playlist));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Selection fix
        if (listModel.getSize() > 0)
            lists.setSelectedIndex(listModel.getSize()-1);
        else
            lists.setSelectedIndex(0);

    }

    public void showCreatePlaylist(final LocalMovie movie) {
        addNewPlaylistPanel.removeAll();
        final JTextField newPlaylistInput = new JTextField();
        newPlaylistInput.setFont(new Font(newPlaylistInput.getFont().getName(),
                newPlaylistInput.getFont().getStyle(),
                Localization.playlistDefaultTextSize));
        newPlaylistInput.setPreferredSize(new Dimension(142, 30));

        addNewPlaylistPanel.add(newPlaylistInput);
        newPlaylistInput.requestFocus();
        addNewPlaylistPanel.revalidate();
        addNewPlaylistPanel.repaint();

        newPlaylistInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!newPlaylistInput.getText().isEmpty()) {
                        createNewPlaylist(newPlaylistInput.getText(), movie);
                    }
                    addNewPlaylistPanel.removeAll();
                    addNewPlaylistPanel.add(addPlaylistLabel);
                    addNewPlaylistPanel.revalidate();
                    addNewPlaylistPanel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    addNewPlaylistPanel.removeAll();
                    addNewPlaylistPanel.add(addPlaylistLabel);
                    addNewPlaylistPanel.revalidate();
                    addNewPlaylistPanel.repaint();
                }
            }
        });

        newPlaylistInput.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                addNewPlaylistPanel.removeAll();
                addNewPlaylistPanel.add(addPlaylistLabel);
                addNewPlaylistPanel.revalidate();
                addNewPlaylistPanel.repaint();
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });
    }

    private void refreshPlaylists(final DefaultListModel listModel)
            throws SQLException {
        List<Playlist> my_lists = Playlist.getPlaylists();
        listModel.removeAllElements();
        for (Playlist list : my_lists)
            listModel.addElement(list);
    }

    // input field actions
    @Override
    public void changedUpdate(DocumentEvent arg0) {
        lists.setSelectedIndex(0);
        resultPanel.search();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        lists.setSelectedIndex(0);
        resultPanel.search();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        lists.setSelectedIndex(0);
        resultPanel.search();
    }

    /**
     * Save the order of the playlists. Should be executed when application
     * closes.
     */
    public void savePlaylistsOrder() {
        try {
            Dao<Playlist, Integer> listDao = DatabaseManager.getInstance()
                    .getListDao();

            for (int i = Playlist.fixedPlaylists.length; i < listModel
                    .getSize(); i++) {
                Playlist p = (Playlist) listModel.get(i);
                p.setPosition(i + 1);
                listDao.update(p);
            }
        } catch (SQLException e) {
            System.err.println("Couldn't save playlist order");
            e.printStackTrace();
        }

    }

    public int getSelecteListId() {
        Playlist selectedList = (Playlist) lists.getSelectedValue();
        if (selectedList != null)
            return selectedList.getId();
        return -1;
    }
}
