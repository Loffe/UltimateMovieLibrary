package se.eloff.ultimatemovielibrary;

import java.awt.Color;
import java.awt.Component;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class PlaylistCellRenderer extends JLabel implements ListCellRenderer {
    public PlaylistCellRenderer() {
        setOpaque(true);
    }
    
    

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if(value.toString().equals(Localization.profileFavoriteList))
            setIcon(Localization.listsFavoriteIcon);
        else if(value.toString().equals(Localization.profileWishList))
            setIcon(Localization.listsWishIcon);
        else if(value.toString().equals(Localization.profileSeenList))
            setIcon(Localization.listsSeenIcon);
        else
        {
            setIcon(new ImageIcon());
            //setBackground(isSelected ? Color.lightGray : Color.lightGray);
        }
        setText(value.toString());
        return this;
    }
}
