/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import najla.project.cluster.controller.DataController;
import najla.project.cluster.model.Question;
import najla.project.cluster.model.Student;

/**
 * 
 * @author Matt
 */
public class MainFrame extends JFrame implements ActionListener {

    private JPanel panel;
    private DataController dataController;
    private Student[] students;
    private Question[] questions;
    
	public MainFrame(int numOfStudents, int numOfQuestions) {
		super("Cluster");
		setSize(640, 480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents(numOfStudents, numOfQuestions);
	}

	private void initComponents(int numOfStudents, int numOfQuestion) {
		
        int rows = numOfStudents + 1;
        int cols = numOfQuestion + 1;
        
		panel = new JPanel();
		panel.setSize(640, 480);
		panel.setLayout(new GridBagLayout());
        
        students = new Student[numOfStudents];
        questions = new Question[numOfQuestion];
        
        dataController = DataController.getInstance();
        
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
                    constraints.insets = new Insets(10, 10, 0, 10);
                    JTextField inputText = new JTextField();
                    
                    if (col == 0) {
                        students[row - 1] = new Student();
                        students[row - 1].setName("");
                        
                        inputText.setColumns(30);
                        inputText.setText(students[row - 1].getName());
                    } else {
                        questions[col - 1] = new Question();
                        questions[col - 1].setNumber(col);
                        questions[col - 1].setScore(0);
                        
                        inputText.setColumns(10);
                        inputText.setText(questions[col - 1].getScore() + "");
                    }
                    panel.add(inputText, constraints);

				}
			}
		}
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = rows;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.LINE_START;
        
        JButton btnProcess = new JButton("Process");
        btnProcess.addActionListener(this);
        panel.add(btnProcess, constraints);
        
		JScrollPane scrollpane = new JScrollPane(panel);
        
        JPanel panelScores = new JPanel();
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.add(scrollpane, "Student Grades");
        tabs.add(panelScores, "Scores");
        
		getContentPane().add(tabs, BorderLayout.CENTER);
	}

    public void actionPerformed(ActionEvent e) {
        if ("Process".equals(e.getActionCommand())) {
            System.out.println("Process ...");
            
            System.out.println("STUDENTS: " + students.length);
            System.out.println("QUESTIONS: " + questions.length);
            
            // Get number of textfields, and store the values
            ArrayList inputValues = new ArrayList();
            for (int c = 0; c < panel.getComponentCount(); c++) {
                Component comp = panel.getComponent(c);
                // If it is a textfield, get its value
                if (comp instanceof JTextField) {
                    String val = ((JTextField) comp).getText();
                    if (val == null || "".equals(val)) {
                        JOptionPane.showMessageDialog(null, "Value can't be empty");
                        break;
                    }
                    inputValues.add(val);
                }
            }
            System.out.println("inputValues.size(): " + inputValues.size());
            System.out.println(inputValues);
            
            List<Student> listStudents = new ArrayList<Student>();
            List<Question> listQuestions = null;
            int questionIterator = 0;
            int studentIterator = 0;
            int nextStudent = 0;
            for (int i = 0; i < inputValues.size(); i++) {
                
                if (i == 0) {
                    // Then it is an input of student name
                    students[studentIterator] = new Student(inputValues.get(i) + "");
                    studentIterator++;
                    nextStudent = (questions.length * studentIterator) + studentIterator ;
                    listQuestions = new ArrayList<Question>();
                } else {
                    
                    if (i == nextStudent) {
                        // Then it is an input of student name
                        students[studentIterator] = new Student(inputValues.get(i) + "");
                        studentIterator++;
                        nextStudent = (questions.length * studentIterator) + studentIterator ;
                        listQuestions = new ArrayList<Question>();
                    } else {
                        // It is an input of question
                        Question question = new Question(questionIterator + 1, Double.valueOf(inputValues.get(i) + ""));
                        listQuestions.add(question);
                        questionIterator++;
                        if (questionIterator == questions.length) {
                            questionIterator = 0;
                            students[studentIterator - 1].setListQuestions(listQuestions);
                            listStudents.add(students[studentIterator - 1]);
                        }
                    }
                }
            }
            
            System.out.println("listStudents: " + listStudents);
            dataController.setListStudent(listStudents);
            
        }
    }
    
	public static void main(String args[]) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainFrame frame = new MainFrame(5, 5);
                frame.setVisible(true);
            }
        });
        
	}

}
