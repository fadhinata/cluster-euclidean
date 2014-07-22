/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ta.cluster.core.Configuration;
import ta.cluster.tool.Constants;

/**
 *
 * @author Matt
 */
public class PanelTabResult extends JPanel {

    private Configuration config;

    private JPanel panelScore;
    private JPanel panelMean;
    private JPanel panelDeviation;
    private JPanel panelStandardScoreStudent;
    private JPanel panelEuclideanDistance;
    
    public PanelTabResult() {
        config = Configuration.getInstance();
        this.setSize(Constants.MAIN_FRAME_INITIAL_HEIGHT, Constants.MAIN_FRAME_INITIAL_WIDTH);
        initComponents();
    }
    
    private void initComponents() {
        
        int numOfQuestions = config.getNumQuestions();
        int numOfStudents = config.getNumStudents();
        
        panelScore = createPanelWithComponents("Total Skor", numOfQuestions, 2);
        panelMean = createPanelWithComponents("Rata-rata", numOfQuestions, 2);
        panelDeviation = createPanelWithComponents("Standard Deviasi", numOfQuestions, 2);
        panelStandardScoreStudent = createPanelStandartScoreStudent("Standard Skor Siswa", numOfQuestions + 1, numOfStudents + 1);
        panelEuclideanDistance = createPanelEuclideanDistance("Euclidean Distance", numOfStudents, 2);
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0};
        this.setLayout(gridBagLayout);
        
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.BOTH;
        this.add(panelScore, constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.BOTH;
        this.add(panelMean, constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 2;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.BOTH;
        this.add(panelDeviation, constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 3;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.BOTH;
        this.add(panelStandardScoreStudent, constraint);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 4;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.BOTH;
        this.add(panelEuclideanDistance, constraint);
        
    }

    private JPanel createPanelWithComponents(String title, int rows, int cols) {
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(title)));
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);
        
        for (int row = 0; row < rows; row++) {
            
            for (int col = 0; col < cols; col++) {
                
                GridBagConstraints constraint = new GridBagConstraints();
                constraint.anchor = GridBagConstraints.FIRST_LINE_START;
                constraint.insets = (row == (rows - 1)) ? new Insets(10, 10, 10, 10) : new Insets(10, 10, 0, 10);
                constraint.gridx = col;
                constraint.gridy = row;
                if (col == 0) {
                    panel.add(new JLabel("SOAL " + (row + 1)), constraint);
                } else {
                    JTextField inputText = new JTextField();
                    inputText.setColumns(15);
                    inputText.setHorizontalAlignment(JTextField.RIGHT);
                    panel.add(inputText, constraint);
                }

            }
            
        }
        
        return panel;
    }
    
    private JPanel createPanelStandartScoreStudent(String title, int rows, int cols) {
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(title)));
        panel.setLayout(new GridBagLayout());
        
        for (int row = 0; row < rows; row++) {
            
            for (int col = 0; col < cols; col++) {
                
                GridBagConstraints constraint = new GridBagConstraints();
                constraint.anchor = GridBagConstraints.LINE_START;
                constraint.insets = (row == (rows - 1)) ? new Insets(10, 10, 10, 10) : new Insets(10, 10, 0, 10);
                constraint.gridx = col;
                constraint.gridy = row;
                
                if (row == 0) {
                    
                    if (col == 0) {
                        panel.add(new JLabel(""), constraint);
                    } else {
                        JTextField inputText = new JTextField("");
                        inputText.setColumns(30);
                        
                        panel.add(inputText, constraint);
                    }
                    
                } else {
                    
                    if (col == 0) {
                        panel.add(new JLabel("SOAL " + row), constraint);
                    } else {
                        JTextField inputText = new JTextField("");
                        inputText.setColumns(30);
                        inputText.setHorizontalAlignment(JTextField.RIGHT);
                        
                        panel.add(inputText, constraint);
                    }
                    
                }
                
            }
            
        }
        
        return panel;
    }
    
    private JPanel createPanelEuclideanDistance(String title, int rows, int cols) {
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(title)));
        panel.setLayout(new GridBagLayout());
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                
                GridBagConstraints constraint = new GridBagConstraints();
                constraint.anchor = GridBagConstraints.LINE_START;
                constraint.insets = (row == (rows - 1)) ? new Insets(10, 10, 10, 10) : new Insets(10, 10, 0, 10);
                constraint.gridx = col;
                constraint.gridy = row;
                
                JTextField inputText = new JTextField();
                inputText.setColumns(30);
                if (col > 0) {
                    inputText.setColumns(15);
                    inputText.setHorizontalAlignment(JTextField.RIGHT);
                }
                
                panel.add(inputText, constraint);
            }
        }
        
        return panel;
    }

    public JPanel getPanelScore() {
        return panelScore;
    }

    public JPanel getPanelDeviation() {
        return panelDeviation;
    }

    public JPanel getPanelEuclideanDistance() {
        return panelEuclideanDistance;
    }

    public JPanel getPanelMean() {
        return panelMean;
    }

    public JPanel getPanelStandardScoreStudent() {
        return panelStandardScoreStudent;
    }
    
}
