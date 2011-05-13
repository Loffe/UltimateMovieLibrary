package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MovieInfoPanel extends JPanel {

    private MovieInfo info;
    private LocalMovie movie;
    private JLabel title;
    private JLabel plot;
    private Cover cover;
    private TextArea plotText;
    private JLabel genre;
    private JLabel rating;
    private JLabel cast;
    private JLabel director;

    private final int gapsize = 20;

    public void refresh(LocalMovie movie, MovieInfo info) {
        System.out.println("refreshing info");
        this.movie = movie;
        title.setText(movie.getName() + ", " + movie.getYear());
        plot.setText(Localization.moviePlotLabel);

        this.info = info;
        if (info != null) {

            genre.setText(Localization.movieGenreLabel + info.getGenres());
            rating.setText(Localization.movieRatingLabel
                    + info.getOnlineRating());
            director.setText(Localization.movieDirectorLabel
                    + info.getDirectors());
            cast.setText(Localization.movieCastLabel + info.getCast());
            plotText.setText(info.getPlot());
            cover.refresh(info.getCover());
        }
    }

    public MovieInfoPanel() {
        cover = new Cover();
        title = new JLabel("Filminfo");
        plot = new JLabel();
        genre = new JLabel();
        rating = new JLabel();
        director = new JLabel();
        cast = new JLabel();
        plotText = new TextArea("", Localization.moviePlotHeight,
                Localization.moviePlotWidth, TextArea.SCROLLBARS_VERTICAL_ONLY);
        plotText.setEditable(false);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        cover.setAlignmentX(Component.CENTER_ALIGNMENT);
        plot.setAlignmentX(Component.CENTER_ALIGNMENT);
        genre.setAlignmentX(Component.LEFT_ALIGNMENT);
        rating.setAlignmentX(Component.LEFT_ALIGNMENT);
        director.setAlignmentX(Component.LEFT_ALIGNMENT);
        cast.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        add(cover);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        add(plot);
        this.add(plotText);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        this.add(genre);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        this.add(rating);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        this.add(director);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        this.add(cast);
    }

    private class Cover extends JLabel {
        public Cover() {
            this.setBackground(Color.green);
        }

        public void refresh(String src) {
            this.setIcon(new ImageIcon(src));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Localization.movieCoverWidth,
                    Localization.movieCoverHeight);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(Localization.movieCoverWidth,
                    Localization.movieCoverHeight);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Localization.movieCoverWidth,
                    Localization.movieCoverHeight);
        }
    }

  /*  public static void main(String[] args) {
        final MovieInfoPanel infoPanel = new MovieInfoPanel();
        LocalMovie local = new LocalMovie();
        local.setName("The dark knight");
        local.setYear(2009);
        MovieInfo movie = new MovieInfo();
        movie.setCover("img/dark.jpg");
        movie.setCast("Christian Bale");
        movie.setDirectors("Christopher Nolan");
        movie.setGenres("Action");
        movie.setOnlineRating(9);
        movie.setPlot("Bla bla");
        local.setInfo(movie);
        infoPanel.refresh(local);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(infoPanel);
        frame.pack();
        frame.setVisible(true);
    }*/
}
