/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import ta.cluster.view.MainDialog;

/**
 *
 * @author Matt
 */
public class MainApplication {

    private static Logger log = Logger.getLogger(MainApplication.class);

    public MainApplication() {
        log.debug("Main application started ...");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainDialog mainDialog = MainDialog.getDialog();
                mainDialog.initComponents();
                mainDialog.setVisible(true);
            }
        });
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        MainApplication main = new MainApplication();
    }
}
