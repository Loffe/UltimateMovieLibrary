package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RecommendPanel extends JPanel implements MovieSearchClient {
    private int lastSearchId = 0;
    private JPanel outerPanel;

    public RecommendPanel() {
        this.setLayout(new GridLayout(1, 1));
        outerPanel = new JPanel();
    }

    public  void refresh(final int width) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public synchronized void run() {
                lastSearchId = MovieSearchProvider
                        .getFeaturedMovies(
                                (int) ((float) width / (float) Localization.movieInfoWidth),
                                RecommendPanel.this, "rating", false);
            }
        });
    }

    @Override
    public synchronized void searchFinished(List<LocalMovie> movies, int searchKey) {
        this.removeAll();
        outerPanel.removeAll();
        if (lastSearchId == searchKey) {
            
            outerPanel.setLayout(new GridLayout(1, movies.size()));
            for (LocalMovie localMovie : movies) {
                MovieInfoPanel panel = new MovieInfoPanel();
                panel.refresh(localMovie);
                outerPanel.add(panel);
            }
            add(outerPanel);
            revalidate();
        }
    }
}
