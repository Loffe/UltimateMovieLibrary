package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RatingComponent extends JPanel implements ActionListener {

    private static final long serialVersionUID = 2321500964797490876L;

    private static final int MAX_RATING = 5;

    private int currentRating = 0;

    private RateButton[] buttons;

    public RatingComponent() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        buttons = new RateButton[MAX_RATING];
        for (int i = 0; i < MAX_RATING; i++) {
            buttons[i] = new RateButton();
            buttons[i].addActionListener(this);
            this.add(buttons[i]);
        }
    }

    private class RateButton extends JButton {

        private static final long serialVersionUID = 6739554426559038970L;
        private static final int width = 50;
        private static final int height = 50;

        private boolean active = false;

        public RateButton() {
        }

        public void setActive(boolean active) {
            this.active = active;
            this.repaint();
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(width, height);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(width, height);
        }

        @Override
        protected void paintBorder(Graphics g) {
        }

        @Override
        protected void paintComponent(Graphics g) {
            int cross_w = 10;
            int cross_h = 40;
            int border = 2;
            // super.paintComponent(g);
            g.setColor(new Color(0.9f, 0.5f, 0.3f));
            g.fillRect(width / 2 - cross_w / 2 - border, height / 2 - cross_h
                    / 2 - border, cross_w + 2 * border, cross_h + 2 * border);
            g.fillRect(width / 2 - cross_h / 2 - border, height / 2 - cross_w
                    / 2 - border, cross_h + 2 * border, cross_w + 2 * border);
            if (this.getModel().isRollover()) {
                g.setColor(new Color(255, 230, 40));
            } else if (isActive()) {
                g.setColor(new Color(255, 200, 0));
            } else {
                g.setColor(new Color(255, 255, 255));
            }
            g.fillRect(width / 2 - cross_w / 2, height / 2 - cross_h / 2,
                    cross_w, cross_h);
            g.fillRect(width / 2 - cross_h / 2, height / 2 - cross_w / 2,
                    cross_h, cross_w);

        }

        @Override
        protected void processMouseEvent(MouseEvent e) {

            if (e.getID() == MouseEvent.MOUSE_ENTERED
                    || e.getID() == MouseEvent.MOUSE_EXITED) {

                String actionCommand = e.getID() == MouseEvent.MOUSE_ENTERED ? "hover_entered"
                        : "hover_exited";
                this.fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_PERFORMED, actionCommand));

            }
            super.processMouseEvent(e);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RatingComponent());
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int currentButtonIndex = 0;
        for (int i = 0; i < MAX_RATING; i++) {
            if (e.getSource() == buttons[i]) {
                currentButtonIndex = i;
                break;
            }
        }

        if (e.getActionCommand().equals("hover_exited")) {
            currentButtonIndex = currentRating;
        } else if (e.getActionCommand().equals("hover_entered")) {

        } else {
            currentRating = currentButtonIndex;
        }

        for (int i = 0; i < MAX_RATING; i++) {
            buttons[i].setActive(i <= currentButtonIndex);
        }
    }
}
