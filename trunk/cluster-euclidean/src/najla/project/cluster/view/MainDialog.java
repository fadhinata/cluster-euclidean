/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import najla.project.cluster.core.Configuration;

/**
 *
 * @author Matt
 */
public class MainDialog extends JDialog implements ActionListener {
    
    private JButton btnNext;
    private JButton btnCancel;
    private JTextField txtStudents;
    private JTextField txtQuestions;
    
    public MainDialog() {
        this.setTitle("Cluster Application");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(300, 150);
        this.setResizable(false);
        this.setTitle("Cluster Application");
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
            
        });
        
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setSize(300, 150);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder("Configuration")));
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 0;
        constraint.gridy = 0;
        panel.add(new JLabel("Number of students", SwingConstants.LEFT), constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        
        txtStudents = new JTextField();
        txtStudents.setColumns(10);
        panel.add(txtStudents, constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 0;
        constraint.gridy = 1;
        panel.add(new JLabel("Number of questions", SwingConstants.LEFT), constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        
        txtQuestions = new JTextField();
        txtQuestions.setColumns(10);
        panel.add(txtQuestions, constraint);
        
        btnNext = new JButton("Next");
        btnCancel = new JButton("Cancel");
        
        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);
        
        JPanel panelBtn = new JPanel(new GridLayout(1,2));
        panelBtn.add(btnNext);
        panelBtn.add(btnCancel);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 10, 10);
        constraint.gridx = 1;
        constraint.gridy = 2;
        
        panel.add(panelBtn, constraint);
        
        getContentPane().add(panel, BorderLayout.CENTER);
    }
    
    private void gotoMainFrame() {

        final int numStudents = Integer.valueOf(txtStudents.getText());
        final int numQuestions = Integer.valueOf(txtQuestions.getText());

        Configuration config = Configuration.getInstance();
        config.setNumStudets(numStudents);
        config.setNumQuestions(numQuestions);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainDialog.this.dispose();
                MainFrame mainFrame = new MainFrame(numStudents, numQuestions);
                mainFrame.setVisible(true);
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        if ("Next".equals(e.getActionCommand())) {
            gotoMainFrame();
        } else if ("Cancel".equals(e.getActionCommand())) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainDialog dialog = new MainDialog();
                dialog.setVisible(true);
            }
        });
        
    }

}
