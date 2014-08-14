/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import ta.cluster.view.filter.IntDocumentFilter;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;
import ta.cluster.core.ClusterCalculator;
import ta.cluster.core.Configuration;
import ta.cluster.core.DataStore;
import ta.cluster.model.Question;
import ta.cluster.model.Student;
import ta.cluster.tool.Constants;
import ta.cluster.tool.SimpleValidator;

/**
 *
 * @author Matt
 */
public class PanelTabStudentGrades extends JPanel implements ActionListener {

    private Configuration config;
    private DataStore dataStore;
    private JButton btnProcess;
    private JButton btnBack;
    
    public PanelTabStudentGrades() {
        config = Configuration.getInstance();
        dataStore = DataStore.getInstance();
        this.setSize(Constants.MAIN_FRAME_INITIAL_HEIGHT, Constants.MAIN_FRAME_INITIAL_WIDTH);
        initComponents();
    }
    
    private void initComponents() {
        
        int numOfQuestions = config.getNumQuestions();
        int numOfStudents = config.getNumStudents();
        
        int rows = numOfStudents + 1;
        int cols = numOfQuestions + 1;
        
        this.setLayout(new GridBagLayout());
		for (int row = 0; row < rows; row++) {
			
			for (int col = 0; col < cols; col++) {
				
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = col;
                constraints.gridy = row;
                constraints.insets = new Insets(10, 10, 0, 10);
                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                if (row == 0) {
                    
                    if (col == 0) {
                        this.add(new JLabel("NAMA"), constraints);
                    } else {
                        this.add(new JLabel("SOAL " + col), constraints);
                    }
                    
				} else {
                    
                    final JTextField inputText = new JTextField();
                    if (col == 0) {
                        inputText.setColumns(30);
                    } else {
                        inputText.setColumns(10);
                        inputText.setText("0");
                        inputText.setHorizontalAlignment(JTextField.RIGHT);
                        inputText.addKeyListener(new KeyAdapter() {

                            @Override
                            public void keyPressed(KeyEvent e) {
                                
                                if (e.getKeyCode() == KeyEvent.VK_UP) {
                                    int currentVal = 0;
                                    try {    
                                        currentVal = Integer.parseInt(inputText.getText()) + 1;
                                    } catch (Exception ex) {
                                        currentVal = 0;
                                    }
                                    
                                    if (currentVal >= 0) inputText.setText(currentVal + "");
                                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                    int currentVal = 0;
                                    try {    
                                        currentVal = Integer.parseInt(inputText.getText()) - 1;
                                    } catch (Exception ex) {
                                        currentVal = 0;
                                    }
                                    
                                    if (currentVal >= 0) inputText.setText(currentVal + "");
                                }
                            }
                            
                        });                        
                    
                        // Set filter number on input text
                        PlainDocument doc = (PlainDocument) inputText.getDocument();
                        doc.setDocumentFilter(new IntDocumentFilter());
                    }
                    this.add(inputText, constraints);
				}
			}
		}
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = rows;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        
        JPanel panel = new JPanel(new GridLayout(1, 2));
        
        btnProcess = new JButton("Proses");
        btnProcess.addActionListener(this);
        
        btnBack = new JButton("Kembali");
        btnBack.addActionListener(this);
        
        panel.add(btnProcess);
        panel.add(btnBack);
        
        this.add(panel, constraints);
        
    }

    public void actionPerformed(ActionEvent e) {
        if ("Proses".equals(e.getActionCommand())) {
            
            System.out.println("Process ...");
            
            int numOfStudent = config.getNumStudents();
            int numOfQuestion = config.getNumQuestions();
            
            System.out.println("STUDENTS: " + numOfStudent);
            System.out.println("QUESTIONS: " + numOfQuestion);
            
            // Get number of textfields, and store the values
            ArrayList inputValues = new ArrayList();
            for (int c = 0; c < this.getComponentCount(); c++) {
                Component comp = this.getComponent(c);
                // If it is a textfield, get its value
                if (comp instanceof JTextField) {
                    String val = ((JTextField) comp).getText();
                    if (val == null || Constants.STR_EMPTY.equals(val)) {
                        JOptionPane.showMessageDialog(null, "Input tidak boleh kosong");
                        return;
                    }
                    inputValues.add(val);
                }
            }
            System.out.println("inputValues.size(): " + inputValues.size());
            System.out.println(inputValues);
            Student student = null;
            List<Student> listStudents = new ArrayList<Student>();
            List<Question> listQuestions = null;
            int questionIterator = 0;
            int studentIterator = 0;
            int nextStudent = 0;
            for (int i = 0; i < inputValues.size(); i++) {
                
                if (i == 0) {
                    // Then it is an input of student name
                    if (SimpleValidator.isNumber(inputValues.get(i))) {
                        JOptionPane.showMessageDialog(null, "Nama tidak boleh angka");
                        return;
                    }
                    student = new Student(inputValues.get(i) + "");
                    studentIterator++;
                    nextStudent = (numOfQuestion * studentIterator) + studentIterator ;
                    listQuestions = new ArrayList<Question>();
                } else {
                    
                    if (i == nextStudent) {
                        // Then it is an input of student name
                        student = new Student(inputValues.get(i) + "");
                        studentIterator++;
                        nextStudent = (numOfQuestion * studentIterator) + studentIterator ;
                        listQuestions = new ArrayList<Question>();
                    } else {
                        // It is an input of question
                        Question question = new Question(questionIterator + 1, Double.valueOf(inputValues.get(i) + ""));
                        listQuestions.add(question);
                        questionIterator++;
                        if (questionIterator == numOfQuestion) {
                            questionIterator = 0;
                            student.setListQuestions(listQuestions);
                            listStudents.add(student);
                        }
                    }
                }
            }
            
            System.out.println("listStudents: " + listStudents);
            dataStore.setListStudents(listStudents);
            
            // Process calculation
            ClusterCalculator cc = ClusterCalculator.getInstance();
            cc.setListener(MainFrame.getProcessListener());
            cc.processCalculation();
            
            // panelTabs.setEnabledAt(1, true);
            // btnProcess.setText("Processed");
            // btnProcess.setEnabled(false);
            
        } else if ("Kembali".equals(e.getActionCommand())) {
            MainFrame.getFrame().dispose();
            MainDialog mainDialog = MainDialog.getDialog();
            mainDialog.initComponents();
            mainDialog.setVisible(true);
        }
    }
    
}
