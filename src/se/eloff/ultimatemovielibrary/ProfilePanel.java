package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.j256.ormlite.dao.Dao;

public class ProfilePanel extends ViewPanel implements DocumentListener {

    private final class PlaylistTransferHandler extends TransferHandler {
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
    private JButton recommendedMovies;
    private Box centerBox;
    private RecommendPanel recommendPanel;

    private boolean showsRecommended = false;

    private ListElement selectedElement = null;

    public void setSelectedElement(ListElement element) {
        if (element != null) {
            if ((selectedElement != null && !selectedElement.getMovie().equals(
                    element.getMovie()))
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
            try {
                refreshPlaylists(listModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    };

    private DefaultListModel listModel;

    public ProfilePanel() {
        setTitle(Localization.profileTitle);
        initComponents();
        lists.setSelectedIndex(0);
        resultPanel.search();
    }

    private void initComponents() {
        resultPanel = new ResultPanel(this) {

            @Override
            public void search() {
                // TODO better search in progress function, maybe some rotating
                // thingy or so
                // resultPanel.removeAll();
                // resultPanel.add(new
                // JLabel(Localization.searchInProgressText));
                // jScrollPanel.updateUI();

                // lastSearchId = MovieSearchProvider.searchByNameSeen(
                // searchTextField.getText(), resultPanel,
                // getOrderColumn(), isOrderAscending(), true);

                try {
                    Playlist selectedList = (Playlist) lists.getSelectedValue();
                    assert selectedList != null;
                    // If its the "all movies" list...
                    String name = searchTextField.getText();
                    if (selectedList != null) {
                        if (selectedList.getId() == 1) {
                            lastSearchId = MovieSearchProvider.searchByName(
                                    name, resultPanel, getOrderColumn(),
                                    isOrderAscending(), hideSeenMoviesCheckBox
                                            .isSelected());
                        } else if (selectedList.getId() == Playlist.SEEN_LIST_ID) {
                            lastSearchId = MovieSearchProvider
                                    .searchByNameSeen(name, resultPanel,
                                            getOrderColumn(),
                                            isOrderAscending(), true);
                        } else {
                            lastSearchId = MovieSearchProvider.searchByList(
                                    name, resultPanel, getOrderColumn(),
                                    isOrderAscending(), selectedList);
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
        recommendedMovies = new JButton(
                Localization.recommendRefreshButtonText,
                Localization.recommendRefreshButtonIcon);
        recommendedMovies
                .setToolTipText(Localization.recommendRefreshButtonToolTip);

        final JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.add(lists, BorderLayout.CENTER);
        listPanel.add(recommendedMovies, BorderLayout.SOUTH);

        recommendedMovies.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ProfilePanel.this.remove(movieInfoPanel);
                ProfilePanel.this.remove(centerBox);
                recommendPanel.refresh(ProfilePanel.this.getWidth()
                        - lists.getWidth());
                ProfilePanel.this.add(recommendPanel, BorderLayout.CENTER);
                ProfilePanel.this.revalidate();
                lists.clearSelection();
                showsRecommended = true;
            }
        });

        this.add(listPanel, BorderLayout.WEST);

        centerBox = Box.createVerticalBox();
        centerBox.add(searchBox);
        centerBox.add(resultPanel);
        movieInfoPanel = new MovieInfoPanel();
        this.add(centerBox, BorderLayout.CENTER);
        this.add(movieInfoPanel, BorderLayout.EAST);
    }

    private Box buildSearchPanel() {
        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(200, 30));
        searchTextField.setMaximumSize(new Dimension(400, 30));

        titleLabel = new JLabel(Localization.searchFieldLabelText);

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
        lists = new JList(listModel);
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
        assert selectedList != null;
        return selectedList.getId();
    }
}
