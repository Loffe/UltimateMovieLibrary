package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

public class RecommendPanel extends JPanel implements MovieSearchClient {

    private static final long serialVersionUID = 4596307634677007370L;
    private JScrollPane jScrollPanel;
    private JPanel resultPanel;
    private JButton refreshButton;

    private int lastSearchId;
    private static final int NUMOFMOVIES = 3;

    public RecommendPanel() {
        initComponents();
        refreshMovies();
    }

    private void initComponents() {
        jScrollPanel = new JScrollPane();
        refreshButton = new JButton(Localization.recommendRefreshButtonText,
                Localization.recommendRefreshButtonIcon);
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        jScrollPanel.setName("RecommendedMovies");

        GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout
                .setHorizontalGroup(mainPanelLayout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPanel, GroupLayout.DEFAULT_SIZE,
                                1024, Short.MAX_VALUE)
                        .addGroup(
                                GroupLayout.Alignment.TRAILING,
                                mainPanelLayout.createSequentialGroup()
                                        .addContainerGap(53, Short.MAX_VALUE)
                                        .addComponent(refreshButton)
                                        .addGap(54, 54, 54)));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING)
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        mainPanelLayout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        mainPanelLayout.createParallelGroup(
                                                GroupLayout.Alignment.BASELINE)
                                                .addComponent(refreshButton))
                                .addPreferredGap(
                                        LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPanel,
                                        GroupLayout.DEFAULT_SIZE, 217,
                                        Short.MAX_VALUE)));

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                refreshMovies();
            }
        });
        jScrollPanel.setViewportView(resultPanel);
    }

    private void refreshMovies() {
        // TODO better search in progress function, maybe some rotating thingy
        // or so
        // resultPanel.removeAll();
        // resultPanel.add(new JLabel(Localization.searchInProgressText));
        // jScrollPanel.updateUI();
        lastSearchId = MovieSearchProvider.getFeaturedMovies(NUMOFMOVIES, this,
                "rating", false);
    }

    @Override
    public void searchFinished(List<Movie> movies, int searchKey) {
        if (lastSearchId == searchKey) {
            resultPanel.removeAll();
            if (movies.isEmpty()) {
                resultPanel.add(new JLabel(Localization.searchNoMatchText));
                jScrollPanel.repaint();
            } else {
                for (Movie movie : movies) {
                    resultPanel.add(new ListElement2(movie));
                }
            }
            jScrollPanel.revalidate();
        }
    }
}
