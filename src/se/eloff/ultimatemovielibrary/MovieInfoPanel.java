package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class MovieInfoPanel extends JPanel {

    private MovieInfo info;
    private LocalMovie movie;
    private JLabel title;
    private JLabel plot;
    private Cover cover;
    private JTextArea plotText;
    private JLabel genre;
    private JLabel rating;
    private JLabel cast;
    private JLabel director;

    private final int gapsize = 20;

    public void resetInfo() {
        title.setText("");
        genre.setText(Localization.movieGenreLabel);
        rating.setText(Localization.movieRatingLabel);
        director.setText(Localization.movieDirectorLabel);
        cast.setText(Localization.movieCastLabel);
        plotText.setText("");
        cover.refresh(Localization.movieInfoStandardCover);
    }

    public void refresh(LocalMovie movie) {
        resetInfo();

        try {
            movie = DatabaseManager.getInstance().getMovieDao()
                    .queryForId(movie.getId());
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        MovieInfo info = null;
        if (movie.getInfo_id() != -1) {
            try {
                info = DatabaseManager.getInstance().getMovieInfoDao()
                        .queryForId(movie.getInfo_id());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        this.movie = movie;
        title.setText(movie.getName() + ", " + movie.getYear());
        if (info != null) {

            String genreString = info.getGenres();
            if (genreString.length() > 30) {
                genreString = genreString.substring(0, 27) + "...";
            }
            genre.setText(Localization.movieGenreLabel + genreString);
            rating.setText(Localization.movieRatingLabel
                    + info.getOnlineRating());
            String directorString = info.getDirectors();
            if (directorString.length() > 30) {
                directorString = directorString.substring(0, 27) + "...";
            }
            director.setText(Localization.movieDirectorLabel + directorString);
            String castString = info.getCast();
            if (castString.length() > 30) {
                castString = castString.substring(0, 27) + "...";
            }
            cast.setText(Localization.movieCastLabel + castString);

            plotText.setText(info.getPlot());
            if (!info.getCover().isEmpty())
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
        plotText = new JTextArea();

        plot.setText(Localization.moviePlotLabel);

        plotText.setSize(Localization.moviePlotWidth,
                Localization.moviePlotHeight);
        plotText.setMaximumSize(new Dimension(Localization.moviePlotWidth,
                Localization.moviePlotHeight));
        plotText.setLineWrap(true);
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
        resetInfo();
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

    /*
     * public static void main(String[] args) { final MovieInfoPanel infoPanel =
     * new MovieInfoPanel(); LocalMovie local = new LocalMovie();
     * local.setName("The dark knight"); local.setYear(2009); MovieInfo movie =
     * new MovieInfo(); movie.setCover("img/dark.jpg");
     * movie.setCast("Christian Bale"); movie.setDirectors("Christopher Nolan");
     * movie.setGenres("Action"); movie.setOnlineRating(9);
     * movie.setPlot("Bla bla"); local.setInfo(movie); infoPanel.refresh(local);
     * JFrame frame = new JFrame();
     * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     * frame.add(infoPanel); frame.pack(); frame.setVisible(true); }
     */
}
