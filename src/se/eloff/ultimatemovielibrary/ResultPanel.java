package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public abstract class ResultPanel extends JScrollPane implements
        MovieSearchClient, ActionListener {

    private static final long serialVersionUID = -7751458552907067506L;
    private JPanel resultPanel;

    protected int lastSearchId;

    private SortButton[] sortButtons = new SortButton[3];
    protected String orderColumn = "position";
    private boolean orderAscending = true;

    private ProfilePanel parentPanel;

    public ProfilePanel getParenPanel() {
        return parentPanel;
    }

    private int selectedElementPosition = -1;

    public ResultPanel(ProfilePanel parentPanel) {
        this.parentPanel = parentPanel;

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(resultPanel, BorderLayout.NORTH);

        setViewportView(outerPanel);

        setColumnHeaderView(createHeader());

        this.setInputMap(WHEN_IN_FOCUSED_WINDOW, null);

        getVerticalScrollBar().setUnitIncrement(20);

        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {

                    if (selectedElementPosition != 0
                            && resultPanel.getComponentCount() > 1) {
                        setSelectedElement((ListElement) resultPanel
                                .getComponent(selectedElementPosition - 1));
                    }
                    // selectedElementPosition = selectedElementPosition - 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (selectedElementPosition != resultPanel
                            .getComponentCount() - 1
                            && resultPanel.getComponentCount() > 1) {
                        setSelectedElement((ListElement) resultPanel
                                .getComponent(selectedElementPosition + 1));
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ListElement element = (ListElement) resultPanel
                            .getComponent(selectedElementPosition);
                    ExternalPlayerLauncher.getInstance().playMovie(
                            element.getMovie());
                }
                // dont allow the scollpane to scroll
                e.consume();
            }
        });
    }

    private JComponent createHeader() {
        Box header = Box.createHorizontalBox();

        SortButton titleSortButton = new SortButton(
                Localization.searchOrderButtonMovieTitle, "name",
                SortButton.State.Asc);
        titleSortButton.setPreferredSize(new Dimension(200, 20));
        titleSortButton.addActionListener(this);
        titleSortButton
                .setToolTipText(Localization.searchOrderButtonMovieTitleToolTip);

        header.add(titleSortButton);

        SortButton yearSortButton = new SortButton(
                Localization.searchOrderButtonMovieYear, "year",
                SortButton.State.Desc);
        yearSortButton.setPreferredSize(new Dimension(100, 20));
        yearSortButton.addActionListener(this);
        yearSortButton
                .setToolTipText(Localization.searchOrderButtonMovieYearToolTip);
        header.add(yearSortButton);

        header.add(Box.createHorizontalGlue());

        JLabel dummyLabel = new JLabel();
        dummyLabel.setPreferredSize(new Dimension(180, 20));
        header.add(dummyLabel);

        SortButton ratingSortButton = new SortButton(
                Localization.searchOrderButtonMovieRating, "rating",
                SortButton.State.Desc);
        ratingSortButton.setPreferredSize(new Dimension(250, 20));
        ratingSortButton.addActionListener(this);
        ratingSortButton
                .setToolTipText(Localization.searchOrderButtonMovieRatingToolTip);
        header.add(ratingSortButton);

        sortButtons[0] = titleSortButton;
        sortButtons[1] = yearSortButton;
        sortButtons[2] = ratingSortButton;

        return header;
    }

    @Override
    public synchronized void searchFinished(List<LocalMovie> movies,
            int searchKey) {

        if (lastSearchId == searchKey) {
            resultPanel.removeAll();
            if (movies.isEmpty()) {
                resultPanel.add(new JLabel(Localization.searchNoMatchText));
                setSelectedElement(null);
                repaint();
            } else {
                for (LocalMovie movie : movies) {
                    resultPanel.add(new ListElement(movie, this));
                }
                setSelectedElement((ListElement) resultPanel.getComponent(0));
            }
            revalidate();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (SortButton button : sortButtons) {
            if (button != e.getSource()) {
                button.setUnselected();
            }
        }
        SortButton button = (SortButton) e.getSource();
        if (button.isSelected()) {
            orderColumn = button.getColumnName();
            orderAscending = button.isOrderAscending();
        } else {
            orderColumn = "position";
            orderAscending = true;
        }
        search();
    }

    String getOrderColumn() {
        return orderColumn;
    }

    boolean isOrderAscending() {
        return orderAscending;
    }

    public abstract void search();

    public void setSelectedElement(ListElement element) {

        // Tell the selected element that it is selected
        int selectedListId = parentPanel.getSelecteListId();
        if (element != null) {
            element.select(orderColumn.equals("position")
                    && selectedListId != 1
                    && selectedListId != Playlist.SEEN_LIST_ID);

            // deselect all other
            Component components[] = resultPanel.getComponents();
            for (int i = 0; i < components.length; i++) {
                // for (Component elementL : resultPanel.getComponents()) {
                try {
                    ListElement elementCast = (ListElement) components[i];
                    if (elementCast != element)
                        elementCast.deSelect();
                    else
                        selectedElementPosition = i;
                } catch (Exception e) {
                }
            }

            // make sure the selected element is on screen
            if ((selectedElementPosition) * element.getHeight() < getVerticalScrollBar()
                    .getValue()) {
                this.getVerticalScrollBar().setValue(
                        (selectedElementPosition) * element.getHeight());
                // revalidate();
                // repaint();
            } else if ((selectedElementPosition + 1) * element.getHeight()
                    - getVerticalScrollBar().getValue() > getHeight() - 35) {
                this.getVerticalScrollBar().setValue(
                        (selectedElementPosition + 1) * element.getHeight()
                                - getHeight() + 35);
                // revalidate();
                // repaint();
            }
        }

        parentPanel.setSelectedElement(element);
    }

    private void switchPositions(ListElement moveUpElement,
            ListElement moveDownElement, int moveDownElementPos) {
        resultPanel.add(moveUpElement, moveDownElementPos);
        resultPanel.add(moveDownElement, moveDownElementPos + 1);

        // save the new positions to the db
        try {
            // update the moveupMovie
            Dao<MovieList, Integer> movieListDb = DatabaseManager.getInstance()
                    .getMovieListDao();
            QueryBuilder<MovieList, Integer> queryUpElement = movieListDb
                    .queryBuilder();
            queryUpElement.where()
                    .eq("movie_id", moveUpElement.getMovie().getId()).and()
                    .eq("list_id", parentPanel.getSelecteListId());
            queryUpElement.distinct();
            MovieList listItemUp = (MovieList) movieListDb.query(
                    queryUpElement.prepare()).get(0);

            QueryBuilder<MovieList, Integer> queryDownElement = movieListDb
                    .queryBuilder();
            queryDownElement.where()
                    .eq("movie_id", moveDownElement.getMovie().getId()).and()
                    .eq("list_id", parentPanel.getSelecteListId());
            queryDownElement.distinct();
            MovieList listItemDown = (MovieList) movieListDb.query(
                    queryDownElement.prepare()).get(0);

            int newUpPos = listItemDown.getPosition();
            int newDownPos = listItemUp.getPosition();

            listItemDown.setPosition(4563400);
            listItemUp.setPosition(1035455);

            movieListDb.update(listItemUp);
            movieListDb.update(listItemDown);

            listItemUp.setPosition(newUpPos);
            listItemDown.setPosition(newDownPos);

            movieListDb.update(listItemUp);
            movieListDb.update(listItemDown);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void moveSelectedElementUp() {
        if (selectedElementPosition != 0 && resultPanel.getComponentCount() > 1) {
            switchPositions(
                    (ListElement) resultPanel
                            .getComponent(selectedElementPosition),
                    (ListElement) resultPanel
                            .getComponent(selectedElementPosition - 1),
                    selectedElementPosition - 1);
            revalidate();
            selectedElementPosition = selectedElementPosition - 1;
        }
    }

    public void moveSelectedElementDown() {
        if (selectedElementPosition != resultPanel.getComponentCount() - 1
                && resultPanel.getComponentCount() > 1) {
            switchPositions(
                    (ListElement) resultPanel
                            .getComponent(selectedElementPosition + 1),
                    (ListElement) resultPanel
                            .getComponent(selectedElementPosition),
                    selectedElementPosition);

            revalidate();
            selectedElementPosition = selectedElementPosition + 1;
        }
    }
}
