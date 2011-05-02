package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchPanel extends JPanel implements DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private ResultPanel resultPanel;
    private JLabel titleLabel;
    private JTextField searchTextField;
    private JButton sortByTitle;
    private JButton sortByYear;
    private JButton sortByRating;

    private int lastSearchId;
    private String orderColumn = "name";
    private boolean orderAscending = true;

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
                System.out.println(getOrderColumn());
                lastSearchId = MovieSearchProvider.searchByName(searchTextField
                        .getText(), resultPanel, getOrderColumn(),
                        isOrderAscending());
            }
        };
        searchTextField = new JTextField();
        titleLabel = new JLabel(Localization.searchFieldLabelText);
        sortByTitle = new JButton(Localization.searchOrderButtonMovieTitle);
        sortByYear = new JButton(Localization.searchOrderButtonMovieYear);
        sortByRating = new JButton(Localization.searchOrderButtonMovieRating);

        resultPanel.setName("searchResults");

        GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout
                .setHorizontalGroup(mainPanelLayout.createParallelGroup(
                        GroupLayout.Alignment.LEADING).addComponent(
                        resultPanel, GroupLayout.DEFAULT_SIZE, 1024,
                        Short.MAX_VALUE).addGroup(
                        GroupLayout.Alignment.TRAILING,
                        mainPanelLayout.createSequentialGroup()
                                .addContainerGap(53, Short.MAX_VALUE)
                                .addComponent(sortByTitle).addComponent(
                                        sortByYear).addComponent(sortByRating)
                                .addComponent(titleLabel).addGap(18, 18, 18)
                                .addComponent(searchTextField,
                                        GroupLayout.PREFERRED_SIZE, 255,
                                        GroupLayout.PREFERRED_SIZE).addGap(54,
                                        54, 54)));
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
                                                                sortByTitle)
                                                        .addComponent(
                                                                sortByYear)
                                                        .addComponent(
                                                                sortByRating)
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

        // Action listeners for the sort buttons
        sortByTitle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                switchSort("name");
            }
        });

        sortByYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                switchSort("year");
            }
        });

        sortByRating.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                switchSort("rating");
            }
        });

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);
    }

    private void switchSort(String column) {
        if (orderColumn.equals(column)) {
            orderAscending = !orderAscending;
        } else {
            orderAscending = true;
            orderColumn = column;
        }
        resultPanel.search();
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
