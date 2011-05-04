package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.LayoutStyle;

public class RecommendPanel extends ViewPanel {

    private static final long serialVersionUID = 4596307634677007370L;

    private JButton refreshButton;

    private static final int NUMOFMOVIES = 3;

    public RecommendPanel() {
        setTitle(Localization.recommendTitle);
        initComponents();
        resultPanel.search();
    }

    private void initComponents() {
        refreshButton = new JButton(Localization.recommendRefreshButtonText,
                Localization.recommendRefreshButtonIcon);
        refreshButton.setToolTipText(Localization.toolTipsRefresh);
        resultPanel = new ResultPanel() {
            {
                setColumnHeaderView(null);
            }

            @Override
            public void search() {
                // TODO better search in progress function, maybe some rotating
                // thingy
                // or so
                // resultPanel.removeAll();
                // resultPanel.add(new
                // JLabel(Localization.searchInProgressText));
                // jScrollPanel.updateUI();
                lastSearchId = MovieSearchProvider.getFeaturedMovies(
                        NUMOFMOVIES, this, "rating", false);
            }
        };

        GroupLayout mainPanelLayout = new GroupLayout(this);
        this.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(resultPanel,
                GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE).addGroup(
                GroupLayout.Alignment.CENTER,
                mainPanelLayout.createSequentialGroup()
                // .addContainerGap(53, Short.MAX_VALUE)
                        .addComponent(refreshButton)
        // .addGap(54, 54, 54)));
                ));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING)
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        mainPanelLayout.createSequentialGroup()
                                .addContainerGap().addGroup(
                                        mainPanelLayout.createParallelGroup(
                                                GroupLayout.Alignment.CENTER)
                                                .addComponent(refreshButton))
                                .addPreferredGap(
                                        LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resultPanel,
                                        GroupLayout.DEFAULT_SIZE, 217,
                                        Short.MAX_VALUE)));

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                resultPanel.search();
            }
        });
    }
}
