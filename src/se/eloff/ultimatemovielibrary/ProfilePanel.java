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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ProfilePanel extends ViewPanel implements DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private JLabel titleLabel;
    private JTextField searchTextField;

    private JList lists;

    public ProfilePanel() {
        setTitle(Localization.profileTitle);
        initComponents();
        resultPanel.search();
    }

    private void initComponents() {
        resultPanel = new ResultPanel() {

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
                    lastSearchId = MovieSearchProvider.searchByList(
                            searchTextField.getText(), resultPanel,
                            getOrderColumn(), isOrderAscending(), selectedList);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        };

        final DefaultListModel listModel = new DefaultListModel();
        try {
            List<Playlist> my_lists = DatabaseManager.getInstance()
                    .getListDao().queryForAll();
            for (Playlist list : my_lists)
                listModel.addElement(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        lists = new JList(listModel);
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
                return true;
            }
            public boolean importData(TransferHandler.TransferSupport support) {
                JList tmp = (JList) support.getComponent();
                JList.DropLocation dl = (JList.DropLocation) support
                        .getDropLocation();
                int index = dl.getIndex();
                if(index<3 || tmp.getSelectedIndex() <3)
                    return false;
                listModel.add(index,
                        listModel.getElementAt(tmp.getSelectedIndex()));
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

        this.add(centerBox, BorderLayout.CENTER);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);
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
}
