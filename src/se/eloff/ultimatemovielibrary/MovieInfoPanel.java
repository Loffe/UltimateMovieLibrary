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
import java.awt.image.RescaleOp;
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
           DatabaseManager.getInstance().getMovieDao().refresh(movie);
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

    private class Cover extends JLabel implements MouseListener, Runnable {

        private static final long serialVersionUID = 4247095294118428348L;
        
        BufferedImage image;
        BufferedImage imageActive;
        
        CoverState state;
        int light;
        
        Thread animator=null;
        
        public void setState(CoverState state){
            this.state = state;
        }
        public void setLight(int light){
            this.light = light;
            repaint();
        }
        public int getLight(){
            return light;
        }
        public void startTread(){
            if(animator==null){
                animator = new Thread(this);
                animator.start();
            }
        }
        public void stopThread(){
            animator = null;
        }
        
        boolean active;

        public Cover() {
            this.setBackground(Color.green);
            this.setActive(false);
            image = null;
            imageActive = null;
            this.addMouseListener(this);
            this.setToolTipText(Localization.toolTipsPlay);
            state = new LightState(this);
            light = 0;
            animator = new Thread(this);
            animator.start();
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
            } catch (Exception e) {
                e.printStackTrace();  
            }
            this.repaint();
        }
     
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            
            // Darken the image by 70%
            float x = -Localization.minimumCoverLight*3.0f/7.0f;
            float scaleFactor = x/(x-(float)(light));
            RescaleOp op = new RescaleOp(scaleFactor, 0, null);
            imageActive = op.filter(image, null);
            g2.drawImage(imageActive, null, 0, 0);
            
            if(isActive()){
                int xPos = image.getWidth()/2-Localization.moviePlayButtonIcon.getIconWidth()/2;
                int yPos = image.getHeight()/2-Localization.moviePlayButtonIcon.getIconHeight()/2;
                g2.drawImage(Localization.moviePlayButtonIcon.getImage(), xPos, yPos, null);
            }
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
            state.mouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            state.mouseExited();
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
        @Override
        public void run() {
            // Remember the starting time
            long tm = System.currentTimeMillis();
            while (Thread.currentThread() == animator) {
                System.out.println("Running..");
                // Delay depending on how far we are behind.
                try {
                    tm += Localization.animationDelayMs;
                    Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    break;
                }
                state.update();
            }
       }
    }
    private abstract class CoverState {
        Cover stateContext;
        public CoverState(Cover context){
            this.stateContext = context;
        }
        public void mouseExited(){
        }
        public void mouseEntered(){
        }
        public void update(){
        }
    }
    private class PreDarkenState extends CoverState{
        int countDown = Localization.preDelayDarken;
        public PreDarkenState(Cover context) {
            super(context);
        }
        public void mouseExited() {
            stateContext.setActive(false);
            stateContext.setState(new LightState(stateContext));
        }
        public void update(){
            countDown--;
            if(countDown==0){
                stateContext.setState(new DarkenState(stateContext));
            }
        }
    }
    private class DarkenState extends CoverState{
        public DarkenState(Cover context) {
            super(context);
        }
        public void mouseExited() {
            stateContext.setActive(false);
            stateContext.setState(new EnlightState(stateContext));
        }
        public void update(){
            int light = stateContext.getLight()-1;
            stateContext.setLight(light);
            if(light<=Localization.minimumCoverLight){
                stateContext.setState(new DarkState(stateContext));
            }
        }
    }
    private class DarkState extends CoverState{
        public DarkState(Cover context) {
            super(context);
        }
        public void mouseExited() {
            stateContext.setActive(false);
            stateContext.setState(new EnlightState(stateContext));
        }
    }
    private class EnlightState extends CoverState{
        public EnlightState(Cover context) {
            super(context);
        }
        public void mouseEntered() {
            stateContext.setActive(true);
            stateContext.setState(new DarkenState(stateContext));
        }
        public void update(){
            int light = stateContext.getLight()+1;
            stateContext.setLight(light);
            if(light>=0){
                stateContext.stopThread();
                stateContext.setState(new LightState(stateContext));
            }
        }
    }
    private class LightState extends CoverState{
        public LightState(Cover context) {
            super(context);
        }

        public void mouseEntered() {
            stateContext.startTread();
            stateContext.setActive(true);
            stateContext.setState(new PreDarkenState(stateContext));
        }
    }
}
