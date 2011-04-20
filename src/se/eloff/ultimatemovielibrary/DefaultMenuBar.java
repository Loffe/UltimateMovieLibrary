package se.eloff.ultimatemovielibrary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;

public class DefaultMenuBar extends JButton implements ActionListener {

    private static final long serialVersionUID = -545054522738548223L;

    private ArrayList<JButton> buttons = new ArrayList<JButton>();

    public DefaultMenuBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setEnabled(false);
        setBorderPainted(false);
    }

    public void addButton(JButton button) {
        button.addActionListener(this);
        buttons.add(button);
        add(button);
    }

    public void addButtons(ArrayList<JButton> buttons) {
        for (JButton b : buttons) {
            addButton(b);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (JButton b : buttons) {
            if (source == b) {
                fireActionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_PERFORMED, e.getActionCommand()));
            }
        }

    }

}
