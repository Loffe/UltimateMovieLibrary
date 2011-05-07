package se.eloff.ultimatemovielibrary;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JToggleButton;

import se.eloff.ultimatemovielibrary.AppFrame.GuiPanel;

/**
 * The bottom MenuBar.
 */
public class BotMenuBar extends DefaultMenuBar {

    private static final long serialVersionUID = 8448724914506530255L;

    ArrayList<JToggleButton> toggleButtons = new ArrayList<JToggleButton>();

    /**
     * Constructor. Creates a new MenuBar to use at the Bottom.
     */
    public BotMenuBar() {
        super();
        setLayout(new GridLayout(1, 3));
        setPreferredSize(new Dimension(-1, Localization.menuHeight));

        JToggleButton searchItem = new JToggleButton(
                Localization.menuSearchText);
        searchItem.setFont(new Font(searchItem.getFont().getName(), searchItem
                .getFont().getStyle(), Localization.menuFontSize));
        searchItem.setActionCommand(GuiPanel.Search.toString());
        addButton(searchItem);
        toggleButtons.add(searchItem);

        JToggleButton recommendItem = new JToggleButton(
                Localization.menuRecommendText);
        recommendItem.setActionCommand(GuiPanel.Recommend.toString());
        recommendItem.setFont(new Font(recommendItem.getFont().getName(),
                recommendItem.getFont().getStyle(), Localization.menuFontSize));
        addButton(recommendItem);
        toggleButtons.add(recommendItem);

        JToggleButton profileItem = new JToggleButton(
                Localization.menuProfileText);
        profileItem.setActionCommand(GuiPanel.Profile.toString());
        profileItem.setFont(new Font(profileItem.getFont().getName(),
                profileItem.getFont().getStyle(), Localization.menuFontSize));
        addButton(profileItem);
        toggleButtons.add(profileItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Send the action command to the parent through DefaultMenuBar.
        super.actionPerformed(e);
        Object source = e.getSource();
        // Ignore if not a ToggleButton
        if (!source.getClass().equals(JToggleButton.class))
            return;
        // Go through all ToggleButtons and enable the clicked one, disable
        // the others.
        for (JToggleButton b : toggleButtons) {
            if (source == b) {
                b.setSelected(true);
            } else {
                b.setSelected(false);
            }
        }
    }

}
