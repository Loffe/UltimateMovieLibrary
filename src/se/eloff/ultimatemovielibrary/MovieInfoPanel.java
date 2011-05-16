package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MovieInfoPanel extends JPanel {

    private static final long serialVersionUID = 1404834758155030092L;

    private JLabel title = new JLabel();
    private JLabel year = new JLabel();

    private Cover cover = new Cover();

    private JPanel plotPanel = new JPanel();
    private JLabel plotLabel = new JLabel();
    private JTextArea plot = new JTextArea();
    private JScrollPane plotScrollPane = new JScrollPane(plot);

    private JPanel genrePanel = new JPanel();
    private JLabel genreLabel = new JLabel();
    private JLabel genre = new JLabel();

    private JPanel ratingPanel = new JPanel();
    private JLabel ratingLabel = new JLabel();
    private JLabel rating = new JLabel();

    private JPanel directorPanel = new JPanel();
    private JLabel directorLabel = new JLabel();
    private JLabel director = new JLabel();

    private JPanel castPanel = new JPanel();
    private JLabel castLabel = new JLabel();
    private JLabel cast = new JLabel();

    private final int gapsize = 20;

    /**
     * Constructor. Creates a new MovieInfoPanel to show info about a movie.
     */
    public MovieInfoPanel() {
        // Set fix size
        setSize(Localization.movieInfoWidth, Localization.movieInfoHeight);
        setMaximumSize(new Dimension(Localization.movieInfoWidth,
                Localization.movieInfoHeight));
        setMinimumSize(new Dimension(Localization.movieInfoWidth,
                Localization.movieInfoHeight));
        setPreferredSize(new Dimension(Localization.movieInfoWidth,
                Localization.movieInfoHeight));

        // Show the title
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(new Font(title.getFont().getName(), title.getFont()
                .getStyle(), Localization.movieTitleFontSize));

        // Show the plot
        year.setAlignmentX(CENTER_ALIGNMENT);
        year.setFont(new Font(title.getFont().getName(), title.getFont()
                .getStyle(), Localization.movieYearFontSize));

        // Show the cover
        cover.setAlignmentX(CENTER_ALIGNMENT);

        // Show the plot in a scrollable textarea
        plotLabel.setText(Localization.moviePlotLabel);
        plotLabel.setFont(new Font(plotLabel.getFont().getName(), Font.BOLD,
                plotLabel.getFont().getSize()));
        //plotPanel.add(plotLabel);
        plot.setLineWrap(true);
        plot.setWrapStyleWord(true);
        plot.setEditable(false);
        plot.setOpaque(false);
        plot.setBorder(null);
       
        // Assemble panel to show genres
        genrePanel.setLayout(new BorderLayout());
        genreLabel.setFont(new Font(genreLabel.getFont().getName(), Font.BOLD,
                genreLabel.getFont().getSize()));
        genrePanel.add(genreLabel, BorderLayout.WEST);
        genrePanel.add(genre, BorderLayout.CENTER);
        genrePanel.setAlignmentX(CENTER_ALIGNMENT);

        // Show the online rating
        ratingPanel.setLayout(new BorderLayout());
        ratingLabel.setFont(new Font(ratingLabel.getFont().getName(),
                Font.BOLD, ratingLabel.getFont().getSize()));
        ratingPanel.add(ratingLabel, BorderLayout.WEST);
        ratingPanel.add(rating, BorderLayout.CENTER);
        ratingPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Show the director
        directorPanel.setLayout(new BorderLayout());
        directorLabel.setFont(new Font(directorLabel.getFont().getName(),
                Font.BOLD, directorLabel.getFont().getSize()));
        directorPanel.add(directorLabel, BorderLayout.WEST);
        directorPanel.add(director, BorderLayout.CENTER);
        directorPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Show the cast
        castPanel.setLayout(new BorderLayout());
        castLabel.setFont(new Font(castLabel.getFont().getName(), Font.BOLD,
                castLabel.getFont().getSize()));
        castPanel.add(castLabel, BorderLayout.WEST);
        castPanel.add(cast, BorderLayout.CENTER);
        castPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Layout everything
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JScrollPane centerPanel = new JScrollPane(plot);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        topPanel.add(title);
      //  add(Box.createRigidArea(new Dimension(0, gapsize)));
        topPanel.add(year);
        topPanel.add(cover);
        //add(Box.createRigidArea(new Dimension(0, gapsize)));
        JPanel plotLabelPanel = new JPanel();
        plotLabelPanel.setLayout(new BorderLayout());
        plotLabelPanel.add(plotLabel, BorderLayout.WEST);
        topPanel.add(plotLabelPanel);
        //centerPanel.add(plot);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, gapsize)));
        buttonPanel.add(genrePanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, gapsize)));
        buttonPanel.add(ratingPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, gapsize)));
        buttonPanel.add(directorPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, gapsize)));
        buttonPanel.add(castPanel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, gapsize)));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        // Initialize the info to default values.
        resetInfo();
    }

    public void resetInfo() {
        title.setText("");
        title.setToolTipText("");
        year.setText("");
        cover.refresh(Localization.movieInfoStandardCover);

        plot.setText(Localization.unknownPlotText);
        plotScrollPane.getVerticalScrollBar().setValue(0);

        genreLabel.setText(Localization.movieGenreLabel);
        genre.setText(Localization.unknownGenreText);

        ratingLabel.setText(Localization.movieRatingLabel);
        rating.setText(Localization.unknownRatingText);

        directorLabel.setText(Localization.movieDirectorLabel);
        director.setText(Localization.unknownDirectorText);

        castLabel.setText(Localization.movieCastLabel);
        cast.setText(Localization.unknownCastText);
    }

    public void refresh(LocalMovie movie) {
        try {
            movie = DatabaseManager.getInstance().getMovieDao().queryForId(
                    movie.getId());
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
        if (info == null)
            resetInfo();

        // Show the movie title
        title.setText(movie.getName());
        title.setToolTipText(title.getText());

        // Show year or "Unkown" text
        year.setText((movie.getYear() != 0) ? "" + movie.getYear()
                : Localization.movieNoYearText);
        if (info != null) {

            // Update the cover image
            if (!info.getCover().isEmpty()) {
                cover.refresh(info.getCover());
            }

            // Update plot
            plot.setText(info.getPlot());
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    plotScrollPane.getViewport().setViewPosition(
                            new Point(0, 0));
                    plotScrollPane.revalidate();
                }
            });

            // Update genres.
            String genreString = info.getGenres();
            if (genreString.isEmpty()) {
                genreString = Localization.unknownGenreText;
            }
            genre.setText(genreString);

            // Show the online rating of the movie
            ratingLabel.setText(Localization.movieRatingLabel);
            if (info.getOnlineRating() > 0) {
                // Convert to a rating between 0 to 5
                int onlineRating = info.getOnlineRating();
                rating.setText(onlineRating + " / 5");
            } else {
                rating.setText(Localization.unknownRatingText);
            }

            // Update the director info
            String directorString = info.getDirectors();
            if (directorString.isEmpty()) {
                directorString = Localization.unknownDirectorText;
            }
            director.setText(directorString);

            // Update the cast info
            String castString = info.getCast();
            if (info.getCast().isEmpty()) {
                castString = Localization.unknownCastText;
            }
            cast.setText(castString);
        }
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
