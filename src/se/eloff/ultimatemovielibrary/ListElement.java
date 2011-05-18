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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

import sun.awt.VerticalBagLayout;

import com.j256.ormlite.dao.Dao;

/**
 * 
 * @author david
 */
public class ListElement extends JPanel {

    private static final long serialVersionUID = -6318896437132825786L;

    private JButton playButton;
    private RatingButton rating;
    private JLabel titleLabel;
    private JLabel yearLabel;
    private JButton playlistButton;
    private JToggleButton seenButton;
    private JToggleButton wishButton;
    private JToggleButton favoriteButton;
    private JPopupMenu playlistMenu;
    private JButton moveUpButton;
    private JButton moveDownButton;

    private final ResultPanel parentPanel;
    private LocalMovie movie;

    private boolean isSelected = false;
    private int position;

    public ListElement(LocalMovie movie, ResultPanel parentPanel, int position) {
        this.parentPanel = parentPanel;
        this.movie = movie;
        initComponents();
        this.position = position;
    }

    public LocalMovie getMovie() {
        return movie;
    }

    private void initComponents() {
        setFocusable(true);
        playButton = new JButton();
       // playButton.setBorderPainted(false);
       // playButton.setOpaque(false);
       // playButton.setFocusPainted( false );
        
        playButton.setContentAreaFilled(false);

        titleLabel = new JLabel();
        yearLabel = new JLabel();
        playlistButton = new JButton(Localization.managePlaylistsButtonIcon);
        seenButton = new JToggleButton();
        wishButton = new JToggleButton();
        favoriteButton = new JToggleButton();
        JPanel ratingSpace = new JPanel() {
            int space = 14;

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, space);
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(0, space);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(0, space);
            }
        };
        JPanel ratingContainer = new JPanel();
        ratingContainer.setLayout(new BoxLayout(ratingContainer,
                BoxLayout.Y_AXIS));
        ratingContainer.add(ratingSpace);
        rating = new RatingButton();
        ratingContainer.setOpaque(false);
        ratingContainer.add(rating);

        moveUpButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // Make sure no button is painted
                Graphics2D g2 = (Graphics2D) g;
                g2.scale(1.0, 1.0);
                try {
                    g2.drawImage(ImageIO.read(new File(
                            Localization.movieMoveUpButtonIcon)), 0, 0, null);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        moveUpButton.setToolTipText(Localization.toolTipsMoveUpButton);

        moveUpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                parentPanel.moveSelectedElementUp();

            }
        });
        moveDownButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // Make sure no button is painted
                Graphics2D g2 = (Graphics2D) g;
                g2.scale(1.0, 1.0);
                try {
                    g2.drawImage(ImageIO.read(new File(
                            Localization.movieMoveDownButtonIcon)), 0, 0, null);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        moveDownButton.setToolTipText(Localization.toolTipsMoveDownButton);

        moveDownButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                parentPanel.moveSelectedElementDown();

            }
        });

        moveUpButton.setVisible(false);
        moveDownButton.setVisible(false);

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
                        parentPanel.getParenPanel().showCreatePlaylist(movie);
                    }
                });

                playlistMenu.show(playlistButton, 0, 0 + 50);
            }
        });

        rating.setRating(movie.getRating());

        playButton.setIcon(Localization.moviePlayButtonIcon);
        playButton.setToolTipText(Localization.toolTipsPlay);
        playButton.setRolloverIcon(Localization.moviePlayButtonHoverIcon);

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

        Dimension moveButtonDimension = new Dimension(30, 25);

        moveUpButton.setMaximumSize(moveButtonDimension);
        moveDownButton.setMaximumSize(moveButtonDimension);
        moveUpButton.setPreferredSize(moveButtonDimension);
        moveDownButton.setPreferredSize(moveButtonDimension);
        moveUpButton.setMinimumSize(moveButtonDimension);
        moveDownButton.setMinimumSize(moveButtonDimension);

        moveUpButton.setBorderPainted(false);

        JPanel movePanel = new JPanel(new VerticalBagLayout());
        movePanel.setAlignmentX(CENTER_ALIGNMENT);
        movePanel.add(moveUpButton);
        movePanel.add(moveDownButton);
        movePanel.setOpaque(false);

        this.setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(movePanel,
                                        GroupLayout.PREFERRED_SIZE, 32,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(playButton,
                                        GroupLayout.PREFERRED_SIZE, 52,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(
                                        LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                GroupLayout.Alignment.LEADING)
                                                .addComponent(yearLabel)
                                                .addComponent(titleLabel, 50,
                                                        50, Short.MAX_VALUE))
                                .addPreferredGap(
                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(playlistButton,
                                        GroupLayout.PREFERRED_SIZE, 52,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(seenButton,
                                        GroupLayout.PREFERRED_SIZE, 52,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(wishButton,
                                        GroupLayout.PREFERRED_SIZE, 52,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(favoriteButton,
                                        GroupLayout.PREFERRED_SIZE, 52,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(ratingContainer,
                                        GroupLayout.PREFERRED_SIZE, 245,
                                        GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()

                                .addGroup(
                                        layout.createParallelGroup(
                                                GroupLayout.Alignment.TRAILING)
                                                .addComponent(
                                                        movePanel,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        52,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addComponent(
                                                        playButton,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        52,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addGroup(
                                                        GroupLayout.Alignment.LEADING,
                                                        layout.createParallelGroup(
                                                                GroupLayout.Alignment.LEADING)
                                                                .addGroup(
                                                                        GroupLayout.Alignment.TRAILING,
                                                                        layout.createSequentialGroup()
                                                                                .addGap(10,
                                                                                        10,
                                                                                        10)
                                                                                .addComponent(
                                                                                        titleLabel)

                                                                                .addComponent(
                                                                                        yearLabel))

                                                                .addComponent(
                                                                        ratingContainer,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        70,
                                                                        GroupLayout.PREFERRED_SIZE))))
                .addGroup(
                        layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(
                                        layout.createParallelGroup(
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

        // Update the MovieInfo panel with this movie info
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                parentPanel.requestFocusInWindow();
                parentPanel.setSelectedElement(ListElement.this);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    setOpaque(false);
                    setBackground(Localization.HoverListElementColor);
                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    setOpaque(true);
                    setBackground(Localization.HoverListElementColor);
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {

            }
        });

        rating.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseManager.getInstance().getMovieDao().refresh(movie);
                    movie.setRating(rating.getRating());
                    DatabaseManager.getInstance().getMovieDao().update(movie);
                } catch (SQLException e1) {
                    System.out
                            .println("Failed to update movie with new rating");
                }
            }
        });

        moveUpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        moveDownButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

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
    }

    public void setSelected(boolean selected, boolean showMoveArrows) {
        isSelected = selected;
        this.setOpaque(selected);
        this.setBackground(selected ? Localization.selectedListElementColor
                : Color.black);
        moveUpButton.setVisible(showMoveArrows && selected);
        moveDownButton.setVisible(showMoveArrows && selected);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
