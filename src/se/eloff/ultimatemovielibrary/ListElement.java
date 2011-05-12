/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ListItem2.java
 *
 * Created on 2011-apr-23, 13:46:38
 */

package se.eloff.ultimatemovielibrary;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

import com.j256.ormlite.dao.Dao;

/**
 * 
 * @author david
 */
public class ListElement extends javax.swing.JPanel {

    private static final long serialVersionUID = -6318896437132825786L;

    private JButton playButton;
    private RatingButton rating;
    private JLabel titleLabel;
    private JLabel yearLabel;
    private JButton playlistButton;
    private JToggleButton seenButton;
    private JToggleButton wishButton;
    private JToggleButton favoriteButton;
    private LocalMovie movie;
    private JPopupMenu playlistMenu;

    public ListElement(LocalMovie movie) {
        this.movie = movie;
        initComponents();
    }

    private void initComponents() {

        playButton = new JButton();
        titleLabel = new JLabel();
        yearLabel = new JLabel();
        playlistButton = new JButton();
        seenButton = new JToggleButton();
        wishButton = new JToggleButton();
        favoriteButton = new JToggleButton();
        rating = new RatingButton();

        playlistButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Build the playlist Menu.
                playlistMenu = new JPopupMenu("Playlists");

                try {
                    Dao<Playlist, Integer> listsDb = DatabaseManager
                            .getInstance().getListDao();
                    // fetch all lists
                    for (final Playlist playlist : listsDb) {
                        if (playlist.getId() < Playlist.fixedPlaylists.length + 1)
                            continue;
                        final JCheckBoxMenuItem listItem = new JCheckBoxMenuItem(
                                playlist.getName(), null);

                        // TODO much better way to find out if the movie is
                        // in the
                        // playlist.movie_list.contains(movie) do not work
                        // as it should
                        // maybe override the contains function in playlist
                        for (LocalMovie compareMovie : playlist.getMovies()) {
                            if (compareMovie.compareTo(movie) == 0) {
                                listItem.setSelected(true);
                                break;
                            }
                        }
                        playlistMenu.add(listItem);
                        // for each playlist entry, add a listener
                        listItem.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    // add it to the list, put it last
                                    if (listItem.isSelected()) {
                                        playlist.add(movie);
                                    } else {
                                        playlist.remove(movie);
                                    }

                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                // a nice line
                playlistMenu.addSeparator();
                JMenuItem AddToNewListItem = new JMenuItem(
                        Localization.movieAddToNewPlaylistText, null);
                playlistMenu.add(AddToNewListItem);
                AddToNewListItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO Create a new playlist and add this movie
                        // somehow
                        try {

                            Dao<MovieList, Integer> movieListDb = DatabaseManager
                                    .getInstance().getMovieListDao();

                            String playlistname = JOptionPane.showInputDialog(
                                    null,
                                    Localization.playlistCreateNewMessage,
                                    Localization.playlistCreateNewHeading, 1);

                            Playlist playlist = DatabaseManager.getInstance()
                                    .createPlaylist(playlistname);

                            movieListDb.create(new MovieList(movie, playlist));
                            // TODO update the profilePanel listview with the
                            // new playlist!

                        } catch (SQLException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                    }
                });

                // playlistMenu.setInvoker(this);

                playlistMenu.show(playlistButton, 0, 0 + 50);

            }
        });

        rating.setRating(movie.getRating());
        rating.setToolTipText(Localization.toolTipsRating);

        playButton.setIcon(Localization.moviePlayButtonIcon);
        playButton.setToolTipText(Localization.toolTipsPlay);

        if (movie.isWish()) {
            wishButton.setIcon(Localization.movieStarButtonIcon);
            wishButton.setToolTipText(Localization.toolTipsWishDisable);
        } else {
            wishButton.setIcon(Localization.movieStarButtonIconDisabled);
            wishButton.setToolTipText(Localization.toolTipsWish);
        }

        if (movie.isFavorite()) {
            favoriteButton.setIcon(Localization.movieFavoriteButtonIcon);
            favoriteButton.setToolTipText(Localization.toolTipsFavoriteDisable);
        } else {
            favoriteButton
                    .setIcon(Localization.movieFavoriteButtonIconDisabled);
            favoriteButton.setToolTipText(Localization.toolTipsFavorite);
        }

        if (movie.isSeen()) {
            seenButton.setIcon(Localization.movieSeenButtonIcon);
            seenButton.setToolTipText(Localization.toolTipsSeenDisable);
        } else {
            seenButton.setIcon(Localization.movieSeenButtonIconDisabled);
            seenButton.setToolTipText(Localization.toolTipsSeen);
        }

        wishButton.setSelected(movie.isWish());
        favoriteButton.setSelected(movie.isFavorite());
        seenButton.setSelected(movie.isSeen());

        titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel
                .getFont().getStyle(), Localization.movieTitleFontSize));
        titleLabel.setText(movie.getName());
        titleLabel.setAlignmentY(0.0F);
        titleLabel.setText(movie.getName());

        yearLabel.setFont(new Font(yearLabel.getFont().getName(), yearLabel
                .getFont().getStyle(), Localization.movieYearFontSize));
        if (movie.getYear() != 0)
            yearLabel.setText(Integer.toString(movie.getYear()));
        else
            yearLabel.setText(Localization.movieNoYearText);
        yearLabel.setAlignmentY(0.0F);

        seenButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        wishButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        favoriteButton.setHorizontalTextPosition(SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout
                .setHorizontalGroup(layout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(playButton,
                                                GroupLayout.PREFERRED_SIZE, 52,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                                layout
                                                        .createParallelGroup(
                                                                GroupLayout.Alignment.LEADING)
                                                        .addComponent(yearLabel)
                                                        .addComponent(
                                                                titleLabel, 50,
                                                                50,
                                                                Short.MAX_VALUE))
                                        .addGap(57, 57, 57)
                                        .addComponent(playlistButton,
                                                GroupLayout.PREFERRED_SIZE, 52,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(seenButton,
                                                GroupLayout.PREFERRED_SIZE, 52,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(wishButton,
                                                GroupLayout.PREFERRED_SIZE, 52,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(favoriteButton,
                                                GroupLayout.PREFERRED_SIZE, 52,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rating,
                                                GroupLayout.PREFERRED_SIZE,
                                                300, GroupLayout.PREFERRED_SIZE)));
        layout
                .setVerticalGroup(layout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout
                                        .createSequentialGroup()

                                        .addGroup(
                                                layout
                                                        .createParallelGroup(
                                                                GroupLayout.Alignment.TRAILING)
                                                        .addComponent(
                                                                playButton,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                52,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(
                                                                GroupLayout.Alignment.LEADING,
                                                                layout
                                                                        .createParallelGroup(
                                                                                GroupLayout.Alignment.LEADING)
                                                                        .addGroup(
                                                                                GroupLayout.Alignment.TRAILING,
                                                                                layout
                                                                                        .createSequentialGroup()
                                                                                        .addGap(
                                                                                                10,
                                                                                                10,
                                                                                                10)
                                                                                        .addComponent(
                                                                                                titleLabel)

                                                                                        .addComponent(
                                                                                                yearLabel))

                                                                        .addComponent(
                                                                                rating,
                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                70,
                                                                                GroupLayout.PREFERRED_SIZE))))
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(
                                                layout
                                                        .createParallelGroup(
                                                                GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                playlistButton,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                52,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                seenButton,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                52,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                wishButton,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                52,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(
                                                                favoriteButton,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                52,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(10, Short.MAX_VALUE)));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExternalPlayerLauncher.getInstance().playMovie(movie);
            }
        });

        rating.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movie.setRating(rating.getRating());
                try {
                    DatabaseManager.getInstance().getMovieDao().update(movie);
                } catch (SQLException e1) {
                    System.out
                            .println("Failed to update movie with new rating");
                }
            }
        });

        wishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (wishButton.isSelected()) {
                        Playlist.getWishlist().add(movie);
                    } else {
                        Playlist.getWishlist().remove(movie);
                    }
                } catch (SQLException e1) {
                    System.out
                            .println("Failed to update movie with new rating");
                }

                if (movie.isWish()) {
                    wishButton.setIcon(Localization.movieStarButtonIcon);
                    wishButton.setToolTipText(Localization.toolTipsWishDisable);
                } else {
                    wishButton
                            .setIcon(Localization.movieStarButtonIconDisabled);
                    wishButton.setToolTipText(Localization.toolTipsWish);
                }

            }
        });

        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movie.setFavorite(favoriteButton.isSelected());
                if (movie.isFavorite()) {
                    favoriteButton
                            .setIcon(Localization.movieFavoriteButtonIcon);
                    favoriteButton
                            .setToolTipText(Localization.toolTipsFavoriteDisable);
                } else {
                    favoriteButton
                            .setIcon(Localization.movieFavoriteButtonIconDisabled);
                    favoriteButton
                            .setToolTipText(Localization.toolTipsFavorite);
                }

                try {
                    DatabaseManager.getInstance().getMovieDao().update(movie);
                } catch (SQLException e1) {
                    System.out
                            .println("Failed to update movie with new rating");
                }
            }
        });

        seenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movie.setSeen(seenButton.isSelected());
                if (movie.isSeen()) {
                    seenButton.setIcon(Localization.movieSeenButtonIcon);
                    seenButton.setToolTipText(Localization.toolTipsSeenDisable);
                } else {
                    seenButton
                            .setIcon(Localization.movieSeenButtonIconDisabled);
                    seenButton.setToolTipText(Localization.toolTipsSeen);
                }

                try {
                    DatabaseManager.getInstance().getMovieDao().update(movie);
                } catch (SQLException e1) {
                    System.out
                            .println("Failed to update movie with new rating");
                }
            }
        });
    }// </editor-fold>
}
