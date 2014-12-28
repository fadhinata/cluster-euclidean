/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import ta.cluster.view.filter.IntDocumentFilter;
import java.awt.Component;
import java.awt.Frame;
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
import org.apache.log4j.Logger;
import ta.cluster.core.ClusterCalculator;
import ta.cluster.core.Configuration;
import ta.cluster.core.DataStore;
import ta.cluster.core.DatabaseClusterCalculator;
import ta.cluster.model.QuestionModel;
import ta.cluster.model.StudentModel;
import ta.cluster.tool.Constants;
import ta.cluster.tool.SimpleValidator;

/**
 *
 * @author Matt
 */
public class PanelTabStudentGrades extends JPanel implements ActionListener {

    private static final Logger log = Logger.getLogger(PanelTabStudentGrades.class);
    private Configuration config;
    private DataStore dataStore;
    private JButton btnProcess;
    private JButton btnBack;

    public PanelTabStudentGrades(Frame frame) {
        this.config = Configuration.getInstance();
        this.dataStore = DataStore.getInstance();
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

                                    if (currentVal >= 0) {
                                        inputText.setText(currentVal + "");
                                    }
                                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                    int currentVal = 0;
                                    try {
                                        currentVal = Integer.parseInt(inputText.getText()) - 1;
                                    } catch (Exception ex) {
                                        currentVal = 0;
                                    }

                                    if (currentVal >= 0) {
                                        inputText.setText(currentVal + "");
                                    }
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

    public void actionPerformed(ActionEvent event) {
        if ("Proses".equals(event.getActionCommand())) {

            log.debug("Process ...");

            new Thread(new Runnable() {

                public void run() {

                    long startTime = System.currentTimeMillis();

                    // Get number of textfields, and store the values
                    ArrayList inputValues = new ArrayList();
                    for (int c = 0; c < PanelTabStudentGrades.this.getComponentCount(); c++) {
                        Component component = PanelTabStudentGrades.this.getComponent(c);
                        // If it is a textfield, get its value
                        if (component instanceof JTextField) {
                            String val = ((JTextField) component).getText();
                            if (val == null || Constants.STR_EMPTY.equals(val)) {
                                JOptionPane.showMessageDialog(null, "Input tidak boleh kosong");
                                return;
                            }
                            inputValues.add(val);
                        }
                    }

                    StudentModel studentModel = null;
                    List<StudentModel> listStudents = new ArrayList<StudentModel>();
                    List<QuestionModel> listQuestions = null;
                    int numOfQuestion = config.getNumQuestions();
                    int questionIterator = 0;
                    int studentIterator = 0;
                    int nextStudent = 0;
                    for (int i = 0; i < inputValues.size(); i++) {
                        String input = String.valueOf(inputValues.get(i));
                        if (i == 0) {   // Then it is an input of student name
                            if (SimpleValidator.containsNumber(input)) {
                                JOptionPane.showMessageDialog(null, "Nama tidak boleh mengandung angka (" + input + ")");
                                return;
                            }
                            studentModel = new StudentModel(input);
                            studentIterator++;
                            nextStudent = (numOfQuestion * studentIterator) + studentIterator;
                            listQuestions = new ArrayList<QuestionModel>();
                            
                        } else {

                            if (i == nextStudent) { // Then it is an input of student name
                                if (SimpleValidator.containsNumber(input)) {
                                    JOptionPane.showMessageDialog(null, "Nama tidak boleh mengandung angka (" + input + ")");
                                    return;
                                }
                                studentModel = new StudentModel(input);
                                studentIterator++;
                                nextStudent = (numOfQuestion * studentIterator) + studentIterator;
                                listQuestions = new ArrayList<QuestionModel>();
                                
                            } else {    // It is an input of question
                                QuestionModel question = new QuestionModel(questionIterator + 1, Double.valueOf(input));
                                listQuestions.add(question);
                                questionIterator++;
                                if (questionIterator == numOfQuestion) {
                                    questionIterator = 0;
                                    studentModel.setListQuestions(listQuestions);
                                    listStudents.add(studentModel);
                                }
                            }
                        }
                    }

                    dataStore.setListStudents(listStudents);

                    // Process calculation
                    // StudentClusterCalculator cc = PlainStudentClusterCalculator.getInstance();
                    final ClusterCalculator cc = DatabaseClusterCalculator.getInstance();
                    cc.setListener(MainFrame.getProcessListener());
                    cc.process();

                    // panelTabs.setEnabledAt(1, true);
                    // btnProcess.setText("Processed");
                    // btnProcess.setEnabled(false);

                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;

                    double seconds = (elapsedTime * 0.001) % 60;
                    int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
                    int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

                    log.debug("Elapsed: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
                }
            }).start();

        } else if ("Kembali".equals(event.getActionCommand())) {
            MainFrame.getFrame().dispose();
            MainDialog mainDialog = MainDialog.getDialog();
            mainDialog.initComponents();
            mainDialog.setVisible(true);
        }
    }
}
