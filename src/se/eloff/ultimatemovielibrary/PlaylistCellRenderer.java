package se.eloff.ultimatemovielibrary;

import java.awt.Component;
import java.awt.Font;

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
        
        setText(value.toString());
        setIcon(null);
        setFont(new Font(getFont().getName(), Font.PLAIN,
                Localization.playlistDefaultTextSize));
        
        if (value.toString().equals(Localization.profileAllMoviesList)) {
            setFont(new Font(getFont().getName(), getFont().getStyle(),
                    Localization.playlistAllMoviesTextSize));
        } else if (value.toString().equals(Localization.profileFavoriteList)) {
            setIcon(Localization.listsFavoriteIcon);
            setFont(new Font(getFont().getName(), Font.BOLD, getFont()
                    .getSize()));
        } else if (value.toString().equals(Localization.profileWishList)) {
            setIcon(Localization.listsWishIcon);
            setFont(new Font(getFont().getName(), Font.BOLD, getFont()
                    .getSize()));
        } else if (value.toString().equals(Localization.profileSeenList)) {
            setIcon(Localization.listsSeenIcon);
            setFont(new Font(getFont().getName(), Font.BOLD, getFont()
                    .getSize()));
        }
        
        if (isSelected) {
            setOpaque(true);
            setBackground(Localization.selectedListElementColor);
        } else {
            setOpaque(false);
        }
        return this;
    }
}
