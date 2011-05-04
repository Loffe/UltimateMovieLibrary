package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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

        resultPanel.setName("searchResults");

        GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(resultPanel, GroupLayout.DEFAULT_SIZE, 1024,
                        Short.MAX_VALUE)
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        mainPanelLayout
                                .createSequentialGroup()
                                .addContainerGap(53, Short.MAX_VALUE)
                                .addComponent(seenToggleButton,GroupLayout.PREFERRED_SIZE,
                                        52,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(titleLabel)
                                .addGap(18, 18, 18)
                                .addComponent(searchTextField,
                                        GroupLayout.PREFERRED_SIZE, 255,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)));
        mainPanelLayout
                .setVerticalGroup(mainPanelLayout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                GroupLayout.Alignment.TRAILING,
                                mainPanelLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                mainPanelLayout
                                                        .createParallelGroup(
                                                                GroupLayout.Alignment.BASELINE)
                                                        .addComponent(
                                                                seenToggleButton,GroupLayout.PREFERRED_SIZE,
                                                                52,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                titleLabel)
                                                        .addComponent(
                                                                searchTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(resultPanel,
                                                GroupLayout.DEFAULT_SIZE, 217,
                                                Short.MAX_VALUE)));

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);

        seenToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seenToggleButton.isSelected()) {
                    seenToggleButton.setIcon(Localization.searchToggleSeenButtonIconHide);
                } else
                    seenToggleButton
                            .setIcon(Localization.searchToggleSeenButtonIcon);
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
