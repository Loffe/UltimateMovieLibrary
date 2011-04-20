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
import javax.swing.UIManager;

public class RatingButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 2321500964797490876L;

    private static final int MAX_RATING = 5;

    private int currentRating = 0;

    private RateButton[] buttons;

    public RatingButton() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        buttons = new RateButton[MAX_RATING];
        for (int i = 0; i < MAX_RATING; i++) {
            buttons[i] = new RateButton();
            buttons[i].addActionListener(this);
            this.add(buttons[i]);
        }
        this.setEnabled(false);
        this.setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Make sure no big button is painted
    }

    public int getRating() {
        return this.currentRating;
    }

    public void setRating(int rating) {
        if (rating < 0 || rating > MAX_RATING)
            throw new IllegalArgumentException("rating must be in interval 0.."
                    + MAX_RATING);

        this.currentRating = rating;
        activateRating(rating);
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
            currentButtonIndex = currentRating - 1;
        } else if (e.getActionCommand().equals("hover_entered")) {

        } else {
            currentRating = currentButtonIndex + 1;
            fireActionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, "rating changed"));
        }

        activateRating(currentButtonIndex + 1);
    }

    private void activateRating(int rating) {
        for (int i = 0; i < MAX_RATING; i++) {
            buttons[i].setActive(i < rating);
        }
    }

    private class RateButton extends JButton {

        private static final long serialVersionUID = 6739554426559038970L;
        private static final int width = 50;
        private static final int height = 50;

        private final Color fillColorActive = new Color(255, 200, 0);
        private final Color fillColorHigh = new Color(255, 230, 40);
        private final Color fillColorInactive = new Color(230, 230, 192);

        private final Color borderColorActive = new Color(230, 128, 80);
        private final Color borderColorHigh = new Color(255, 150, 80);
        private final Color borderColorInactive = new Color(220, 220, 180);

        private boolean active = false;

        public RateButton() {
            this.setBorderPainted(false);
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
        protected void paintComponent(Graphics g) {
            g.setColor(UIManager.getColor("Panel.background"));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            int cross_w = 10;
            int cross_h = 40;
            int border = 2;

            if (this.getModel().isRollover())
                g.setColor(borderColorHigh);
            else if (isActive())
                g.setColor(borderColorActive);
            else
                g.setColor(borderColorInactive);

            g.fillRect(width / 2 - cross_w / 2 - border, height / 2 - cross_h
                    / 2 - border, cross_w + 2 * border, cross_h + 2 * border);
            g.fillRect(width / 2 - cross_h / 2 - border, height / 2 - cross_w
                    / 2 - border, cross_h + 2 * border, cross_w + 2 * border);

            if (this.getModel().isRollover())
                g.setColor(fillColorHigh);
            else if (isActive())
                g.setColor(fillColorActive);
            else
                g.setColor(fillColorInactive);

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
        final RatingButton rating = new RatingButton();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(rating);
        frame.pack();
        frame.setVisible(true);

        rating.setRating(2);
        rating.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked rating " + rating.getRating());
            }
        });

    }
}
