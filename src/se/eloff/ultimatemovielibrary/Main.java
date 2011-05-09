package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;

import javax.swing.UIManager;

public class Main {

    /**
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException,
            ClassNotFoundException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppFrame app = new AppFrame();
        app.setVisible(true);

    }

}
