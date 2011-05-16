package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RecommendPanel extends JPanel implements MovieSearchClient {
    int lastSearchId = 0;

    public RecommendPanel(final int width) {
        this.setLayout(new GridLayout(1, 1));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int numberOfMovies = (int) ((float) width / (float) Localization.movieInfoWidth);
                System.out.println("number of recommended movies: " + numberOfMovies + " this w: " + width);
                lastSearchId = MovieSearchProvider.getFeaturedMovies(numberOfMovies,
                        RecommendPanel.this, "rating", false);
            }
        });

    }

    @Override
    public void searchFinished(List<LocalMovie> movies, int searchKey) {
        if (lastSearchId == searchKey) {
            JPanel outerPanel = new JPanel();
            outerPanel.setLayout(new GridLayout(1, movies.size()));
            for (LocalMovie localMovie : movies) {
                MovieInfoPanel panel = new MovieInfoPanel();
                panel.refresh(localMovie);
                outerPanel.add(panel);
            }
            add(outerPanel);
            outerPanel.revalidate();
            revalidate();
        }
    }
}
