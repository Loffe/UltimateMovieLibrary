package se.eloff.ultimatemovielibrary;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AppFrame extends JFrame {

    private static final long serialVersionUID = 5297734322373835993L;

    public AppFrame() throws HeadlessException {
        this.setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Ultimate Movie Library");
        setIconImage(new ImageIcon("img/video_16.png").getImage());
        this.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        try {
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(this);
            } else {
                // Can't run fullscreen, need to bodge around it (setSize to
                // screen size, etc)
            }
            this.setVisible(true);
            // Your business logic here
        } finally {
            gd.setFullScreenWindow(null);
        }

    }
}
