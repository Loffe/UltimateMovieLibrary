package se.eloff.ultimatemovielibrary;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchPanel extends JPanel implements MovieSearchClient,
        DocumentListener {
    private javax.swing.JScrollPane jScrollPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JPanel resultPanel;
    private int lastSearchId;

    public SearchPanel() {
        initComponents();
        search();
    }

    private void initComponents() {
        jScrollPanel = new javax.swing.JScrollPane();
        searchTextField = new javax.swing.JTextField();
        titleLabel = new javax.swing.JLabel("Title");
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        jScrollPanel.setName("searchResults");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(
                this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPanel,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 1024,
                        Short.MAX_VALUE)
                .addGroup(
                        javax.swing.GroupLayout.Alignment.TRAILING,
                        mainPanelLayout
                                .createSequentialGroup()
                                .addContainerGap(53, Short.MAX_VALUE)
                                .addComponent(titleLabel)
                                .addGap(18, 18, 18)
                                .addComponent(searchTextField,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        255,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)));
        mainPanelLayout
                .setVerticalGroup(mainPanelLayout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                mainPanelLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                mainPanelLayout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(
                                                                titleLabel)
                                                        .addComponent(
                                                                searchTextField,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                jScrollPanel,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                217, Short.MAX_VALUE)));
        jScrollPanel.setViewportView(resultPanel);

        // add a listener to the input field
        searchTextField.getDocument().addDocumentListener(this);
    }

    @Override
    public void searchFinished(List<Movie> movies, int searchKey) {
        if (lastSearchId == searchKey) {
            resultPanel.removeAll();
            if (movies.isEmpty())
                resultPanel.add(new JLabel("No Match"));
            else {
                for (Movie movie : movies) {

                    resultPanel.add(new ListElement(movie));
                //    System.out.println("recieving restults, movie: "
                //            + movie.getName());
                }
            }
            jScrollPanel.updateUI();
        }
    }

    private void search() {
        resultPanel.removeAll();
        resultPanel.add(new JLabel("Loading Movies"));
        jScrollPanel.updateUI();
        lastSearchId = MovieSearchProvider.searchByName(
                searchTextField.getText(), this);
    }

    // input field actions
    @Override
    public void changedUpdate(DocumentEvent arg0) {
        search();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        search();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        search();
    }
    
    public static void main(String[] args) {
        SearchPanel panel = new SearchPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
