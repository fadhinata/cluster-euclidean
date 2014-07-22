/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ta.cluster.core.Configuration;
import ta.cluster.tool.Constants;
import ta.cluster.tool.Tools;

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
        this.setBounds(
                Tools.getCenterWidth(Constants.MAIN_DIALOG_INITIAL_WIDTH),
                Tools.getCenterHeight(Constants.MAIN_DIALOG_INITIAL_HEIGHT),
                Constants.MAIN_DIALOG_INITIAL_WIDTH,
                Constants.MAIN_DIALOG_INITIAL_HEIGHT);
        this.setResizable(false);
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
        panel.setSize(Constants.MAIN_DIALOG_INITIAL_WIDTH, Constants.MAIN_DIALOG_INITIAL_HEIGHT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder("Konfigurasi")));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 0;
        constraint.gridy = 0;
        panel.add(new JLabel("Jumlah siswa", SwingConstants.LEFT), constraint);

        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.HORIZONTAL;

        txtStudents = new JTextField("20");
        txtStudents.setColumns(10);
        txtStudents.setHorizontalAlignment(JTextField.RIGHT);
        txtStudents.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int currentVal = 0;
                    try {
                        currentVal = Integer.parseInt(txtStudents.getText()) + 1;
                    } catch (Exception ex) {
                        currentVal = 0;
                    }

                    if (currentVal >= 0) {
                        txtStudents.setText(currentVal + "");
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int currentVal = 0;
                    try {
                        currentVal = Integer.parseInt(txtStudents.getText()) - 1;
                    } catch (Exception ex) {
                        currentVal = 0;
                    }

                    if (currentVal >= 0) {
                        txtStudents.setText(currentVal + "");
                    }
                }
            }
        });
        panel.add(txtStudents, constraint);

        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 0;
        constraint.gridy = 1;
        panel.add(new JLabel("Jumlah soal", SwingConstants.LEFT), constraint);

        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.LINE_START;
        constraint.insets = new Insets(10, 10, 0, 10);
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;

        txtQuestions = new JTextField("10");
        txtQuestions.setColumns(10);
        txtQuestions.setHorizontalAlignment(JTextField.RIGHT);
        txtQuestions.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    int currentVal = 0;
                    try {
                        currentVal = Integer.parseInt(txtQuestions.getText()) + 1;
                    } catch (Exception ex) {
                        currentVal = 0;
                    }

                    if (currentVal >= 0) {
                        txtQuestions.setText(currentVal + "");
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    int currentVal = 0;
                    try {
                        currentVal = Integer.parseInt(txtQuestions.getText()) - 1;
                    } catch (Exception ex) {
                        currentVal = 0;
                    }

                    if (currentVal >= 0) {
                        txtQuestions.setText(currentVal + "");
                    }
                }
            }
        });
        panel.add(txtQuestions, constraint);

        btnNext = new JButton("Lanjut");
        btnCancel = new JButton("Keluar");

        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);

        JPanel panelBtn = new JPanel(new GridLayout(1, 2));
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
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if ("Lanjut".equals(e.getActionCommand())) {
            String txtStudent = txtStudents.getText();
            String txtQuestion = txtQuestions.getText();

            if ("".equals(txtStudent)) {
                JOptionPane.showMessageDialog(null, "Masukkan jumlah siswa", "Cluster Application", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("".equals(txtQuestion)) {
                JOptionPane.showMessageDialog(null, "Masukkan jumlah soal", "Cluster Application", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numStudents = 0;
            int numQuestions = 0;

            try {
                numStudents = Integer.parseInt(txtStudent);
                numQuestions = Integer.parseInt(txtQuestion);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Isi dengan angka", "Cluster Application", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Configuration config = Configuration.getInstance();
            config.setNumStudets(numStudents);
            config.setNumQuestions(numQuestions);

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    MainDialog.this.dispose();
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                }
            });
        } else if ("Keluar".equals(e.getActionCommand())) {
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
