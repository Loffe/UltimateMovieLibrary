package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MovieInfoPanel extends JPanel {

    private static final long serialVersionUID = 1404834758155030092L;

    private JLabel title;
    private JLabel year;
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
        year.setText("");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MovieInfo info = null;
        if (movie.getInfo_id() != -1) {
            try {
                info = DatabaseManager.getInstance().getMovieInfoDao()
                        .queryForId(movie.getInfo_id());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        title.setText(movie.getName());
        year.setText("" + movie.getYear());
        if (info != null) {

            String genreString = info.getGenres();
            if (genreString.isEmpty()) {
                genreString = Localization.genresUnknownText;
            } else if (genreString.length() > 30) {
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
        title = new JLabel();
        year = new JLabel();
        plot = new JLabel();
        genre = new JLabel();
        rating = new JLabel();
        director = new JLabel();
        cast = new JLabel();
        plotText = new JTextArea();

        plot.setText(Localization.moviePlotLabel);

        setSize(Localization.movieInfoWidth, Localization.movieInfoHeight);
        setMaximumSize(new Dimension(Localization.movieInfoWidth,
                Localization.movieInfoHeight));
        setMinimumSize(new Dimension(Localization.movieInfoWidth,
                Localization.movieInfoHeight));
        setPreferredSize(new Dimension(Localization.movieInfoWidth,
                Localization.movieInfoHeight));

        plotText.setSize(Localization.moviePlotWidth,
                Localization.moviePlotHeight);
        plotText.setMaximumSize(new Dimension(Localization.moviePlotWidth,
                Localization.moviePlotHeight));
        plotText.setMinimumSize(new Dimension(Localization.moviePlotWidth,
                Localization.moviePlotHeight));
        plotText.setPreferredSize(new Dimension(Localization.moviePlotWidth,
                Localization.moviePlotHeight));
        plotText.setLineWrap(true);
        plotText.setWrapStyleWord(true);
        plotText.setEditable(false);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        cover.setAlignmentX(Component.CENTER_ALIGNMENT);
        plot.setAlignmentX(Component.CENTER_ALIGNMENT);
        genre.setAlignmentX(Component.CENTER_ALIGNMENT);
        rating.setAlignmentX(Component.CENTER_ALIGNMENT);
        director.setAlignmentX(Component.CENTER_ALIGNMENT);
        cast.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(title);
        this.add(Box.createRigidArea(new Dimension(0, gapsize)));
        add(year);
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

        private static final long serialVersionUID = 4247095294118428348L;

        public Cover() {
            this.setBackground(Color.green);
        }

        public void refresh(String src) {
            this.refresh(new ImageIcon(src));
        }

        public void refresh(ImageIcon icon) {
            this.setIcon(icon);
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

}
