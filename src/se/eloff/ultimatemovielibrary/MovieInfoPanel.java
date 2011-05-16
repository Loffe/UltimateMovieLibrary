package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

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
    
    private LocalMovie movie = null;

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
        plot.setBorder(new EmptyBorder(8, 8, 8, 8));
       
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
        topPanel.add(Box.createRigidArea(new Dimension(0, gapsize)));
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
        movie = null;
        title.setText(" ");
        title.setToolTipText("");
        year.setText(" ");
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
        this.movie = movie;
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

    private class Cover extends JLabel implements MouseListener {

        private static final long serialVersionUID = 4247095294118428348L;
        
        BufferedImage image;
        BufferedImage imageActive;
        
        boolean active;

        public Cover() {
            this.setBackground(Color.green);
            this.setActive(false);
            image = null;
            imageActive = null;
            this.addMouseListener(this);
            this.setToolTipText(Localization.toolTipsPlay);
        }
        
        public void setActive(boolean bool){
            active = bool;
        }
        
        public boolean isActive(){
            return active;
        }

        public void refresh(String src) {
            try {  
                image = ImageIO.read(new File(src)); 
                imageActive = ImageIO.read(new File(src));
            } catch (Exception e) {
                e.printStackTrace();  
            }
            
            int colorDrop = 50;
            for(int y = 0; y < imageActive.getHeight(); y++) {  
                for(int x = 0; x < imageActive.getWidth(); x++) {
                    int color = imageActive.getRGB(x, y);
                    
                    int red = (color >> 16) & 0xff;
                    int green = (color >> 8) & 0xff;
                    int blue = (color) & 0xff;
                    Color newColor = new Color(max(red-colorDrop, 0), max(green-colorDrop, 0), max(blue-colorDrop, 0));
                    
                    imageActive.setRGB(x, y, newColor.getRGB());
                }
            }
            this.repaint();
        }
        
        private int max(int a, int b){
            if(a>b)
                return a;
            return b;
        }
     
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            if(isActive()){
                g2.drawImage(imageActive, null, 0, 0);
                int xPos = image.getWidth()/2-Localization.moviePlayButtonIcon.getIconWidth()/2;
                int yPos = image.getHeight()/2-Localization.moviePlayButtonIcon.getIconHeight()/2;
                g2.drawImage(Localization.moviePlayButtonIcon.getImage(), xPos, yPos, null);
            }
            else
                g2.drawImage(image, null, 0, 0);
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

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            setActive(true);
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            setActive(false);
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
            if (movie == null)
                return;
            ExternalPlayerLauncher.getInstance().playMovie(movie);
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
        }
    }

}
