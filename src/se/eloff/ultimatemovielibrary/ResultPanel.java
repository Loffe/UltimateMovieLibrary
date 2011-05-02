package se.eloff.ultimatemovielibrary;

import java.util.List;

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
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
        header.add(new JLabel("Titel"));
        header.add(new JLabel("|"));
        header.add(new JLabel("Sedd"));
        header.add(new JLabel("|"));
        header.add(new JLabel("Betyg"));
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
