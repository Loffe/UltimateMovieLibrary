package se.eloff.ultimatemovielibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class ResultPanel extends JScrollPane implements
        MovieSearchClient, ActionListener {

    private static final long serialVersionUID = -7751458552907067506L;
    private JPanel resultPanel;

    protected int lastSearchId;

    private SortButton[] sortButtons = new SortButton[3];
    protected String orderColumn = "name";
    private boolean orderAscending;

    public ResultPanel() {

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
                Localization.searchOrderButtonMovieTitle, "name", SortButton.State.Asc);
        titleSortButton.setPreferredSize(new Dimension(200, 20));
        titleSortButton.addActionListener(this);
        titleSortButton.setToolTipText(Localization.searchOrderButtonMovieTitleToolTip);

        header.add(titleSortButton);

        SortButton yearSortButton = new SortButton(
                Localization.searchOrderButtonMovieYear, "year", SortButton.State.Desc);
        yearSortButton.setPreferredSize(new Dimension(100, 20));
        yearSortButton.addActionListener(this);
        yearSortButton.setToolTipText(Localization.searchOrderButtonMovieYearToolTip);
        header.add(yearSortButton);

        header.add(Box.createHorizontalGlue());

        JLabel dummyLabel = new JLabel();
        dummyLabel.setPreferredSize(new Dimension(180, 20));
        header.add(dummyLabel);

        SortButton ratingSortButton = new SortButton(
                Localization.searchOrderButtonMovieRating, "rating", SortButton.State.Desc);
        ratingSortButton.setPreferredSize(new Dimension(300, 20));
        ratingSortButton.addActionListener(this);
        ratingSortButton.setToolTipText(Localization.searchOrderButtonMovieRatingToolTip);
        header.add(ratingSortButton);

        sortButtons[0] = titleSortButton;
        sortButtons[1] = yearSortButton;
        sortButtons[2] = ratingSortButton;

        return header;
    }

    @Override
    public void searchFinished(List<Movie> movies, int searchKey) {
        if (lastSearchId == searchKey) {
            resultPanel.removeAll();
            if (movies.isEmpty()) {
                resultPanel.add(new JLabel(Localization.searchNoMatchText));
                repaint();
            } else {
                for (Movie movie : movies) {
                    resultPanel.add(new ListElement2(movie));
                }
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
        orderColumn = button.getColumnName();
        orderAscending = button.isOrderAscending();

        search();
    }

    String getOrderColumn() {
        return orderColumn;
    }

    boolean isOrderAscending() {
        return orderAscending;
    }

    public abstract void search();
}
