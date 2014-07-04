/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

	public MainFrame(int students, int questions) {
		super("Cluster");
		setSize(640, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		init(students, questions);
	}

	public void init(int students, int questions) {
		
        int rows = students + 1;
        int cols = questions + 1;
        
		JPanel panel = new JPanel();
		panel.setSize(640, 480);
		panel.setLayout(new GridBagLayout());
        
		for (int row = 0; row < rows; row++) {
			
			for (int col = 0; col < cols; col++) {
				if (row == 0) {
                    
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.gridx = col;
                    constraints.gridy = row;
                    constraints.insets = new Insets(10, 10, 0, 10);
                    constraints.anchor = GridBagConstraints.LINE_START;
                    if (col == 0) {
                        panel.add(new JLabel("NAMA"), constraints);
                    } else {
                        panel.add(new JLabel("SOAL " + col), constraints);
                    }
                    
				} else {

                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.gridx = col;
                    constraints.gridy = row;
                    constraints.insets = (row == (rows - 1)) ? new Insets(10, 10, 10, 10) : new Insets(10, 10, 0, 10);
                    JTextField inputText = new JTextField();
                    
                    if (col == 0) {
                        inputText.setColumns(30);
                    } else {
                        inputText.setColumns(10);
                    }
                    panel.add(inputText, constraints);

				}
			}
		}
        
		JScrollPane scrollpane = new JScrollPane(panel);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
	}

	public static void main(String args[]) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainFrame frame = new MainFrame(40, 10);
                frame.setVisible(true);
            }
        });
        
	}
}
