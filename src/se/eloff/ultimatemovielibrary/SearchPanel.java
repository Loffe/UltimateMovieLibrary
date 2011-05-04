package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchPanel extends ViewPanel implements DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private JLabel titleLabel;
    private JTextField searchTextField;
    private JToggleButton seenToggleButton;

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
                if (seenToggleButton.isSelected())
                lastSearchId = MovieSearchProvider.searchByNameSeen(
                        searchTextField.getText(), resultPanel,
                        getOrderColumn(), isOrderAscending(),
                        false);
                else
                    lastSearchId = MovieSearchProvider.searchByName(
                            searchTextField.getText(), resultPanel,
                            getOrderColumn(), isOrderAscending());
            }
        };
        searchTextField = new JTextField();
        titleLabel = new JLabel(Localization.searchFieldLabelText);
        seenToggleButton = new JToggleButton();
        seenToggleButton.setIcon(Localization.searchToggleSeenButtonIcon);
        seenToggleButton.setSize(52, 52);
        seenToggleButton.setPreferredSize(new Dimension(52,52));
        seenToggleButton.setMinimumSize(new Dimension(52,52));
        seenToggleButton.setMaximumSize(new Dimension(52,52));
        resultPanel.setName("searchResults");

        searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(200, 30));
        searchTextField.setMaximumSize(new Dimension(400, 30));

        titleLabel = new JLabel(Localization.searchFieldLabelText);

        this.setLayout(new BorderLayout());

        Box searchBox = Box.createHorizontalBox();
        searchBox.add(titleLabel);
        searchBox.add(searchTextField);
        searchBox.add(seenToggleButton);
        searchBox.add(Box.createHorizontalGlue());

        Box centerBox = Box.createVerticalBox();
        centerBox.add(searchBox);
        centerBox.add(resultPanel);

        this.add(centerBox, BorderLayout.CENTER);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);
        
        seenToggleButton.setToolTipText(Localization.toolTipsSearchSeen);
        seenToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seenToggleButton.isSelected()) {
                    seenToggleButton.setIcon(Localization.searchToggleSeenButtonIconHide);
                    seenToggleButton.setToolTipText(Localization.toolTipsSearchSeen);
                } else
                    seenToggleButton.setIcon(Localization.searchToggleSeenButtonIcon);
                    seenToggleButton.setToolTipText(Localization.toolTipsSearchSeenDisable);
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
