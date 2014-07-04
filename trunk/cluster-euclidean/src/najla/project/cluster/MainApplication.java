/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import najla.project.cluster.view.MainDialog;

/**
 *
 * @author Ideapad
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

