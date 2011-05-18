package se.eloff.ultimatemovielibrary;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.j256.ormlite.dao.Dao;

public class ListElementButtons extends JPanel {
    public JButton playlistButton;
    public JToggleButton seenButton;
    public JToggleButton wishButton;
    public JToggleButton favoriteButton;
    public JPopupMenu playlistMenu;
    private LocalMovie movie;

    private final ProfilePanel parentPanel;

    public ListElementButtons(final LocalMovie movie,
            final ProfilePanel parentPanel) {
        this.movie = movie;
        this.parentPanel = parentPanel;

        playlistButton = new JButton(Localization.managePlaylistsButtonIcon);
        seenButton = new JToggleButton();
        wishButton = new JToggleButton();
        favoriteButton = new JToggleButton();

        playlistButton
                .setToolTipText(Localization.managePlaylistsButtonToolTipText);
        playlistButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Build the playlist Menu.
                playlistMenu = new JPopupMenu(
                        Localization.managePlaylistsMenuTitle);
                FlowLayout flowLayout = new FlowLayout();
                flowLayout.setHgap(8);
                flowLayout.setVgap(8);
                JPanel labelPanel = new JPanel();
                labelPanel.setLayout(flowLayout);
                JLabel label = new JLabel(
                        Localization.managePlaylistsMenuDescriptionText);
                labelPanel.add(label);
                labelPanel.setOpaque(false);
                playlistMenu.add(labelPanel);

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
                        parentPanel.showCreatePlaylist(movie);
                    }
                });

                playlistMenu.show(playlistButton, 0, 0 + 50);
            }
        });
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
        seenButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        wishButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        favoriteButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        
        wishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseManager.getInstance().getMovieDao().refresh(movie);
                    movie.setWish(wishButton.isSelected());
                    DatabaseManager.getInstance().getMovieDao().update(movie);
                    if (wishButton.isSelected()) {
                        Playlist.getWishlist().add(movie);
                    } else {
                        Playlist.getWishlist().remove(movie);
                    }
                } catch (SQLException e1) {
                    System.out.println("Failed to update movie with wish");
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

                try {
                    DatabaseManager.getInstance().getMovieDao().refresh(movie);
                    movie.setFavorite(favoriteButton.isSelected());
                    DatabaseManager.getInstance().getMovieDao().update(movie);

                    if (favoriteButton.isSelected()) {
                        Playlist.getFavoriteList().add(movie);
                    } else {
                        Playlist.getFavoriteList().remove(movie);
                    }

                } catch (SQLException e1) {
                    System.out.println("Failed to update movie with favorite");
                }
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
            }
        });

        seenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseManager.getInstance().getMovieDao().refresh(movie);
                    movie.setSeen(seenButton.isSelected());
                    if (movie.isSeen()) {
                        seenButton.setIcon(Localization.movieSeenButtonIcon);
                        seenButton
                                .setToolTipText(Localization.toolTipsSeenDisable);
                    } else {
                        seenButton
                                .setIcon(Localization.movieSeenButtonIconDisabled);
                        seenButton.setToolTipText(Localization.toolTipsSeen);
                    }

                    DatabaseManager.getInstance().getMovieDao().update(movie);
                } catch (SQLException e1) {
                    System.out
                            .println("Failed to update movie with new rating");
                }
            }
        });
        this.setOpaque(false);
        this.setLayout(new GridLayout(1, 4));
        add(playlistButton);
        add(seenButton);
        add(favoriteButton);
        add(wishButton);
    }
}
