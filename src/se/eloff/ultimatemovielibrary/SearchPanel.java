package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchPanel extends ViewPanel implements DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private JLabel titleLabel;
    private JTextField searchTextField;
    private JCheckBox hideSeenMoviesCheckBox;

    public SearchPanel() {
        setTitle(Localization.searchTitle);
        initComponents();
        resultPanel.search();
    }

    private void initComponents() {
        resultPanel = new ResultPanel() {
            public void search() {
                // TODO better search in progress function, maybe some rotating
                // thingy
                // or so
                // resultPanel.removeAll();
                // resultPanel.add(new
                // JLabel(Localization.searchInProgressText));
                // jScrollPanel.updateUI();
                if (hideSeenMoviesCheckBox.isSelected())
                    lastSearchId = MovieSearchProvider.searchByNameSeen(
                            searchTextField.getText(), resultPanel,
                            getOrderColumn(), isOrderAscending(), false);
                else
                    lastSearchId = MovieSearchProvider.searchByName(
                            searchTextField.getText(), resultPanel,
                            getOrderColumn(), isOrderAscending());
            }
        };
        searchTextField = new JTextField();
        titleLabel = new JLabel(Localization.searchFieldLabelText);
        hideSeenMoviesCheckBox = new JCheckBox(
                Localization.searchHideSeenMoviesText);
        resultPanel.setName("searchResults");

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(200, 30));
        searchTextField.setMaximumSize(new Dimension(400, 30));

        titleLabel = new JLabel(Localization.searchFieldLabelText);

        this.setLayout(new BorderLayout());

        Box searchBox = Box.createHorizontalBox();
        searchBox.add(Box.createRigidArea(new Dimension(20, 20)));
        searchBox.add(titleLabel);
        searchBox.add(searchTextField);
        searchBox.add(Box.createRigidArea(new Dimension(20, 20)));
        searchBox.add(hideSeenMoviesCheckBox);
        searchBox.add(Box.createHorizontalGlue());

        Box centerBox = Box.createVerticalBox();
        centerBox.add(searchBox);
        centerBox.add(resultPanel);

        this.add(centerBox, BorderLayout.CENTER);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);

        hideSeenMoviesCheckBox.setToolTipText(Localization.toolTipsSearchSeen);
        hideSeenMoviesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               /* if (hideSeenMoviesCheckBox.isSelected()) {

                    hideSeenMoviesCheckBox
                            .setToolTipText(Localization.toolTipsSearchSeen);
                } else

                    hideSeenMoviesCheckBox
                            .setToolTipText(Localization.toolTipsSearchSeenDisable);
                            */
                update();
            }

        });
    }

    // input field actions
    @Override
    public void changedUpdate(DocumentEvent arg0) {
        update();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        update();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        update();
    }

}
