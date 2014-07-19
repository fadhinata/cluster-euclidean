/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ta.cluster.view.MainDialog;

/**
 *
 * @author Matt
 */
public class MainApplication {
    
    public MainApplication() {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainDialog mainDialog = new MainDialog();
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

