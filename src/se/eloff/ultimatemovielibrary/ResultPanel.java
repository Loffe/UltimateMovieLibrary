package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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

    private int selectedElementPosition = -1;

    public ResultPanel(ProfilePanel parentPanel) {
        this.parentPanel = parentPanel;

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(resultPanel, BorderLayout.NORTH);

        setViewportView(outerPanel);

        setColumnHeaderView(createHeader());
        getVerticalScrollBar().setUnitIncrement(20);
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
        ratingSortButton.setPreferredSize(new Dimension(300, 20));
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
    public void searchFinished(List<LocalMovie> movies, int searchKey) {
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
        parentPanel.setSelectedElement(element);

        // Tell the selected element that it is selected
        int selectedListId = parentPanel.getSelecteListId();
        if (element != null)
            element.select(orderColumn.equals("position")
                    && selectedListId != 1
                    && selectedListId != Playlist.SEEN_LIST_ID);

        // deselect all other
        int position = 0;
        for (Component elementL : resultPanel.getComponents()) {
            try {
                ListElement listElement = (ListElement) elementL;

                if (listElement != element)
                    listElement.deSelect();
                else
                    selectedElementPosition = position;
                position++;
            } catch (Exception e) {
                System.out.println("fel castning");
            }
        }
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
        System.out.println("moving up, selpos :" + selectedElementPosition);
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
        System.out.println("moving donw, selpos :" + selectedElementPosition);
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
