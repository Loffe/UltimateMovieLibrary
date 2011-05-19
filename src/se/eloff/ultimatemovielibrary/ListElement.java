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
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import sun.awt.VerticalBagLayout;

/**
 * 
 * @author david
 */
public class ListElement extends JPanel implements MovieListener {

    private static final long serialVersionUID = -6318896437132825786L;

    private JButton playButton;
    private RatingButton rating;
    private JLabel titleLabel;
    private JLabel yearLabel;
    private ListElementButtons elementButtons;

    private JButton moveUpButton;
    private JButton moveDownButton;

    private final ResultPanel parentPanel;
    private LocalMovie movie;

    private boolean isSelected = false;
    private int position;

    public ListElement(LocalMovie movie, ResultPanel parentPanel, int position) {
        this.parentPanel = parentPanel;
        this.movie = movie;
        elementButtons = new ListElementButtons(movie,
                parentPanel.getParenPanel());
        initComponents();
        this.position = position;
        DatabaseManager.getInstance().addMovieListener(this);
    }

    public void destroy() {
        DatabaseManager.getInstance().removeMovieListener(this);
    }

    public LocalMovie getMovie() {
        return movie;
    }

    private void initComponents() {
        setFocusable(true);
        playButton = new JButton();

        playButton.setContentAreaFilled(false);

        titleLabel = new JLabel();
        yearLabel = new JLabel();

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
                if (getModel().isRollover())
                    g2.drawImage(Localization.movieMoveUpButtonHoverIcon, 0, 0,
                            null);
                else
                    g2.drawImage(Localization.movieMoveUpButtonIcon, 0, 0, null);
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
                if (getModel().isRollover())
                    g2.drawImage(Localization.movieMoveDownButtonHoverIcon, 0,
                            0, null);
                else
                    g2.drawImage(Localization.movieMoveDownButtonIcon, 0, 0,
                            null);
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

        rating.setRating(movie.getRating());

        playButton.setIcon(Localization.moviePlayButtonIcon);
        playButton.setToolTipText(Localization.toolTipsPlay);
        playButton.setRolloverIcon(Localization.moviePlayButtonHoverIcon);

        titleLabel.setFont(new Font(titleLabel.getFont().getName(), titleLabel
                .getFont().getStyle(), Localization.movieTitleFontSize));
        titleLabel.setText(movie.getName());
        titleLabel.setAlignmentY(0.0F);

        yearLabel.setFont(new Font(yearLabel.getFont().getName(), yearLabel
                .getFont().getStyle(), Localization.movieYearFontSize));
        if (movie.getYear() != 0)
            yearLabel.setText(Integer.toString(movie.getYear()));
        else
            yearLabel.setText(Localization.movieNoYearText);
        yearLabel.setAlignmentY(0.0F);

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
                                .addComponent(elementButtons,
                                        GroupLayout.PREFERRED_SIZE, 210,
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

                                        .addComponent(elementButtons,
                                                GroupLayout.PREFERRED_SIZE, 52,
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

    @Override
    public void onMovieAdded(LocalMovie movie) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMovieUpdated(final LocalMovie movie) {
        if (movie.getId() == this.movie.getId()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ListElement.this.movie = movie;
                    rating.setRating(movie.getRating());
                    titleLabel.setText(movie.getName());
                    if (movie.getYear() != 0)
                        yearLabel.setText(Integer.toString(movie.getYear()));
                    else
                        yearLabel.setText(Localization.movieNoYearText);
                    if (isSelected) {
                        parentPanel.setSelectedElement(ListElement.this);
                    }
                }
            });
        }
    }
}
