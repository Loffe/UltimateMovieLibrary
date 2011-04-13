package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RatingComponent extends JPanel {

    private static final long serialVersionUID = 2321500964797490876L;

    private RateButton[] buttons;

    public RatingComponent() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        buttons = new RateButton[5];
        for (int i = 0; i < 5; i++) {
            buttons[i] = new RateButton();
            this.add(buttons[i]);
        }
    }

    private class RateButton extends JButton {

        private static final long serialVersionUID = 6739554426559038970L;
        private static final int width = 50;
        private static final int height = 50;

        public RateButton() {
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
            //super.paintComponent(g);
            g.setColor(new Color(0.9f, 0.5f, 0.3f));
            g.fillRect(width / 2 - cross_w / 2 - border, height / 2 - cross_h
                    / 2 - border, cross_w + 2 * border, cross_h + 2 * border);
            g.fillRect(width / 2 - cross_h / 2 - border, height / 2 - cross_w
                    / 2 - border, cross_h + 2 * border, cross_w + 2 * border);
            if (this.getModel().isRollover()) {
                g.setColor(new Color(255, 230, 40));
            } else {
                g.setColor(new Color(255, 200, 0));
            }
            g.fillRect(width / 2 - cross_w / 2, height / 2 - cross_h / 2,
                    cross_w, cross_h);
            g.fillRect(width / 2 - cross_h / 2, height / 2 - cross_w / 2,
                    cross_h, cross_w);

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RatingComponent());
        frame.pack();
        frame.setVisible(true);
    }
}
