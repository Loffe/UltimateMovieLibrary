package se.eloff.ultimatemovielibrary;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class ViewPanel extends JPanel {

    private static final long serialVersionUID = 7489639404248359628L;

    protected ResultPanel resultPanel;

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
}
