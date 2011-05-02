package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ProfilePanel extends JPanel implements MovieSearchClient,
        DocumentListener {

    private static final long serialVersionUID = 8595144249306891196L;

    private JScrollPane jScrollPanel;
    private JLabel titleLabel;
    private JTextField searchTextField;
    private JPanel resultPanel;
    private JButton sortByTitle;
    private JButton sortByYear;
    private JButton sortByRating;

    private int lastSearchId;
    private String orderColumn = "name";
    private boolean orderAscending = true;

    public ProfilePanel() {
        initComponents();
        search();
    }

    private void initComponents() {
        jScrollPanel = new JScrollPane();
        searchTextField = new JTextField();
        titleLabel = new JLabel(Localization.searchFieldLabelText);
        sortByTitle = new JButton(Localization.searchOrderButtonMovieTitle);
        sortByYear = new JButton(Localization.searchOrderButtonMovieYear);
        sortByRating = new JButton(Localization.searchOrderButtonMovieRating);
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));

        jScrollPanel.setName("searchResults");

        GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPanel, GroupLayout.DEFAULT_SIZE, 1024,
                        Short.MAX_VALUE)
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        mainPanelLayout
                                .createSequentialGroup()
                                .addContainerGap(53, Short.MAX_VALUE)
                                .addComponent(sortByTitle)
                                .addComponent(sortByYear)
                                .addComponent(sortByRating)
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
                                        .addComponent(jScrollPanel,
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

        jScrollPanel.setViewportView(resultPanel);
        jScrollPanel.getVerticalScrollBar().setUnitIncrement(20);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);
    }
    
    private void switchSort(String column){
        if (orderColumn.equals(column)) {
            orderAscending = !orderAscending;
        } else {
            orderAscending = true;
            orderColumn = column;
        }
        search();
    }

    @Override
    public void searchFinished(List<Movie> movies, int searchKey) {
        if (lastSearchId == searchKey) {
            resultPanel.removeAll();
            if (movies.isEmpty()){
                resultPanel.add(new JLabel(Localization.searchNoMatchText));
                jScrollPanel.repaint();
            }
            else {
                for (Movie movie : movies) {
                    resultPanel.add(new ListElement2(movie));
                }
            }
            jScrollPanel.revalidate();
        }
    }

    private void search() {
        //TODO better search in progress function, maybe some rotating thingy or so
        //resultPanel.removeAll();
        //resultPanel.add(new JLabel(Localization.searchInProgressText));
        //jScrollPanel.updateUI();
        lastSearchId = MovieSearchProvider.searchByName(
                searchTextField.getText(), this, orderColumn, orderAscending);
    }

    // input field actions
    @Override
    public void changedUpdate(DocumentEvent arg0) {
        search();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        search();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        search();
    }
}
