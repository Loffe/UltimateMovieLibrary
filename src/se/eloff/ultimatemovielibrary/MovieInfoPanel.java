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
    private JLabel title;
    private JLabel plot;
    private Cover cover;
    private TextArea plotText;
    private JLabel genre;
    private JLabel rating;
    private JLabel cast;
    private JLabel director;
    
    private final int gapsize = 20;
    
    public MovieInfoPanel(MovieInfo info, LocalMovie movie){
        this.info = info;
        title = new JLabel(movie.getName()+", "+movie.getYear());
        plot = new JLabel(Localization.moviePlotLabel);
        genre = new JLabel(Localization.movieGenreLabel + info.getGenres());
        rating = new JLabel(Localization.movieRatingLabel + info.getOnlineRating());
        director = new JLabel(Localization.movieDirectorLabel + info.getDirectors());
        cast = new JLabel(Localization.movieCastLabel + info.getCast());
        cover = new Cover();
        plotText = new TextArea(info.getPlot(), Localization.moviePlotHeight, Localization.moviePlotWidth, TextArea.SCROLLBARS_VERTICAL_ONLY);
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
    
    private class Cover extends JLabel{
        
        public Cover(){
            this.setBackground(Color.BLUE);
            this.setIcon(new ImageIcon(info.getCover()));
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Localization.movieCoverWidth, Localization.movieCoverHeight);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(Localization.movieCoverWidth, Localization.movieCoverHeight);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Localization.movieCoverWidth, Localization.movieCoverHeight);
        }
    }
    
    public static void main(String[] args) {
        LocalMovie movie = new LocalMovie();
        movie.setName("The Dark Knight");
        movie.setYear(2009);
        MovieInfo info = new MovieInfo();
        info.setCover("img/dark.jpg");
        info.setPlot("Det var en gång");
        info.setCast("Christian Bale");
        info.setDirectors("Christopher Nolan");
        info.setGenres("Action");
        info.setOnlineRating(9);
        final MovieInfoPanel infoPanel = new MovieInfoPanel(info, movie);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(infoPanel);
        frame.pack();
        frame.setVisible(true);

        /*rating.setRating(2);
        info.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked rating " + info.getRating());
            }
        });*/
    }
}
