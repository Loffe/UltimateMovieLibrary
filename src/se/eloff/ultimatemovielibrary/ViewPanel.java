package se.eloff.ultimatemovielibrary;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class ViewPanel extends JPanel {

    private static final long serialVersionUID = 7489639404248359628L;

    // TODO: Fix this ugly hack, pls! Refactoring needed indeed ;P
    public ResultPanel resultPanel;
    protected MovieInfoPanel movieInfoPanel;
    
    private String title;

    public ViewPanel() {
        super();
    }

    public ViewPanel(LayoutManager layout) {
        super(layout);
    }

    public ViewPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public ViewPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public void update() {
        resultPanel.search();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
