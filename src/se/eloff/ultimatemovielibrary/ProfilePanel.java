package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.j256.ormlite.dao.Dao;

public class ProfilePanel extends ViewPanel implements DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private JLabel titleLabel;
    private JTextField searchTextField;

    private JList lists;
    
    private ListElement selectedElement = null;
    
    public void setSelectedElement(ListElement element){
        selectedElement = element;
        LocalMovie movie = element.getMovie();
        MovieInfo info = null;
        try {
            Dao<LocalMovie, Integer> dbMovie = DatabaseManager
            .getInstance().getMovieDao();
           movie = dbMovie.queryForId(movie.getId());
           info = movie.getInfo();
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        movieInfoPanel.refresh(movie, info);
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
                    if (selectedList.getId() == 1) {
                        lastSearchId = MovieSearchProvider.searchByName(name,
                                resultPanel, getOrderColumn(),
                                isOrderAscending());
                    } else if (selectedList.getId() == Playlist.SEEN_LIST_ID) {
                        lastSearchId = MovieSearchProvider.searchByNameSeen(
                                name, resultPanel, getOrderColumn(),
                                isOrderAscending(), true);
                    } else {
                        lastSearchId = MovieSearchProvider.searchByList(name,
                                resultPanel, getOrderColumn(),
                                isOrderAscending(), selectedList);
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        };

        listModel = new DefaultListModel();
        try {
            refreshPlaylists(listModel);

            DatabaseManager.getInstance().addPlaylistChangeListener(
                    playlistListener);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lists = new JList(listModel);
        lists.setSelectedIndex(0);
        lists.setCellRenderer(new PlaylistCellRenderer());
        lists.setPreferredSize(new Dimension(200, 10));
        lists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lists.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                resultPanel.search();
            }
        });

        lists.setDragEnabled(true);
        lists.setDropMode(DropMode.INSERT);
        lists.setTransferHandler(new TransferHandler() {
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

                listModel.add(index, listModel.getElementAt(tmp
                        .getSelectedIndex()));

                listModel.remove(tmp.getSelectedIndex());
                return true;

            }

            protected Transferable createTransferable(JComponent c) {
                return new StringSelection("Whaaat?");
            }

            public int getSourceActions(JComponent c) {
                return MOVE;
            }
        });

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(200, 30));
        searchTextField.setMaximumSize(new Dimension(400, 30));

        titleLabel = new JLabel(Localization.searchFieldLabelText);

        this.setLayout(new BorderLayout());

        this.add(lists, BorderLayout.WEST);

        Box searchBox = Box.createHorizontalBox();
        searchBox.add(titleLabel);
        searchBox.add(searchTextField);
        searchBox.add(Box.createHorizontalGlue());

        Box centerBox = Box.createVerticalBox();
        centerBox.add(searchBox);
        centerBox.add(resultPanel);
        movieInfoPanel = new MovieInfoPanel();
        this.add(centerBox, BorderLayout.CENTER);
        this.add(movieInfoPanel, BorderLayout.EAST);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);
    }

    private void refreshPlaylists(final DefaultListModel listModel)
            throws SQLException {
        List<Playlist> my_lists = DatabaseManager.getInstance().getListDao()
                .queryForAll();
        listModel.removeAllElements();
        for (Playlist list : my_lists)
            listModel.addElement(list);
    }

    // input field actions
    @Override
    public void changedUpdate(DocumentEvent arg0) {
        resultPanel.search();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        resultPanel.search();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        resultPanel.search();
    }

    /**
     * Save the order of the playlists. Should be executed when application closes.
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
}
