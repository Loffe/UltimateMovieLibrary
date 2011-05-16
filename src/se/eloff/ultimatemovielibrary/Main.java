package se.eloff.ultimatemovielibrary;

import java.sql.SQLException;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaClassyLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

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
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager
            // .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            // UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
            // UIManager.setLookAndFeel(new SyntheticaClassyLookAndFeel());

            // Try this!!!
            UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());

        } catch (Exception e) {
            e.printStackTrace();
        }

        AppFrame app = new AppFrame();
        app.setVisible(true);

    }

}
