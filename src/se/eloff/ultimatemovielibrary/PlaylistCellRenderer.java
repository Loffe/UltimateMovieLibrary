package se.eloff.ultimatemovielibrary;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class PlaylistCellRenderer extends JLabel implements ListCellRenderer {

    private static final long serialVersionUID = 6176031015989342382L;

    public PlaylistCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        setFont(new Font(getFont().getName(), getFont().getStyle(),
                Localization.playlistDefaultTextSize));
        if (value.toString().equals(Localization.profileAllMoviesList)) {
            setIcon(new ImageIcon());
            setFont(new Font(getFont().getName(), getFont().getStyle(),
                    Localization.playlistAllMoviesTextSize));
        } else if (value.toString().equals(Localization.profileFavoriteList))
            setIcon(Localization.listsFavoriteIcon);
        else if (value.toString().equals(Localization.profileWishList))
            setIcon(Localization.listsWishIcon);
        else if (value.toString().equals(Localization.profileSeenList))
            setIcon(Localization.listsSeenIcon);
        else {
            setIcon(new ImageIcon());
        }
        if (isSelected) {
            setOpaque(true);
            setBackground(Localization.selectedListElementColor);
        } else
            setOpaque(false);

        setText(value.toString());
        return this;
    }
}
