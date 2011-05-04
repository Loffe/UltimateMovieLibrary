package se.eloff.ultimatemovielibrary;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ProfilePanel extends ViewPanel implements DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private JLabel titleLabel;
    private JTextField searchTextField;

    public ProfilePanel() {
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
                lastSearchId = MovieSearchProvider.searchByNameSeen(
                        searchTextField.getText(), resultPanel,
                        getOrderColumn(), isOrderAscending(), true);
            }
        };
        ;
        ;
        searchTextField = new JTextField();
        titleLabel = new JLabel(Localization.searchFieldLabelText);

        resultPanel.setName("searchResults");

        GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(resultPanel,
                GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE).addGroup(
                GroupLayout.Alignment.TRAILING,
                mainPanelLayout.createSequentialGroup().addContainerGap(53,
                        Short.MAX_VALUE).addComponent(titleLabel).addGap(18,
                        18, 18).addComponent(searchTextField,
                        GroupLayout.PREFERRED_SIZE, 255,
                        GroupLayout.PREFERRED_SIZE).addGap(54, 54, 54)));
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
