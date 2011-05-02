package se.eloff.ultimatemovielibrary;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class ResultPanel extends JScrollPane implements
        MovieSearchClient {

    private static final long serialVersionUID = -7751458552907067506L;
    private JPanel resultPanel;
    protected int lastSearchId;

    public ResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.PAGE_AXIS));
        setViewportView(resultPanel);

        setColumnHeaderView(createHeader());
        getVerticalScrollBar().setUnitIncrement(20);
    }

    private JComponent createHeader() {
        Box header = Box.createHorizontalBox();

        JLabel titleLabel = new JLabel(Localization.searchOrderButtonMovieTitle);
        titleLabel.setPreferredSize(new Dimension(200, 20));
        header.add(titleLabel);

        JLabel yearLabel = new JLabel(Localization.searchOrderButtonMovieYear);
        header.add(yearLabel);

        header.add(Box.createHorizontalGlue());

        JLabel dummyLabel = new JLabel();
        dummyLabel.setPreferredSize(new Dimension(180, 20));
        header.add(dummyLabel);

        JLabel ratingLabel = new JLabel(
                Localization.searchOrderButtonMovieRating);
        ratingLabel.setPreferredSize(new Dimension(300, 20));
        header.add(ratingLabel);

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

    public abstract void search();
}
