/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import java.awt.BorderLayout;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ta.cluster.core.ClusterCalculator;
import ta.cluster.core.ClusterCalculatorListener;
import ta.cluster.core.Configuration;
import ta.cluster.model.Student;
import ta.cluster.tool.Constants;
import ta.cluster.tool.Tools;

/**
 * 
 * @author Matt
 */
public class MainFrame extends JFrame {

    private JScrollPane scrollStudentGrades;
    private JScrollPane scrollResult;
    private JScrollPane scrollSummary;
    
    private static MainFrameListener listener;
    private ClusterCalculator cc;
    private Configuration config;
    
	public MainFrame() {
		super("Cluster Application");
    	setBounds(
                Tools.getCenterWidth(Constants.MAIN_FRAME_INITIAL_WIDTH), 
                Tools.getCenterHeight(Constants.MAIN_FRAME_INITIAL_HEIGHT), 
                Constants.MAIN_FRAME_INITIAL_WIDTH, 
                Constants.MAIN_FRAME_INITIAL_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
        listener = new MainFrameListener();
        cc = ClusterCalculator.getInstance();
        config = Configuration.getInstance();
	}

	private void initComponents() {
		
		scrollStudentGrades = new JScrollPane(new PanelTabStudentGrades());
        scrollResult = new JScrollPane(new PanelTabResult());
        scrollSummary = new JScrollPane(new PanelTabSummary());
        
        JTabbedPane panelTabs = new JTabbedPane();
        panelTabs.add(scrollStudentGrades, "Nilai Siswa");
        panelTabs.add(scrollResult, "Hasil");
        panelTabs.add(scrollSummary, "Ringkasan");
        //panelTabs.setEnabledAt(1, false);
        
		getContentPane().add(panelTabs, BorderLayout.CENTER);
	}

	public static void main(String args[]) {
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Configuration config = Configuration.getInstance();
        config.setNumStudets(3);
        config.setNumQuestions(3);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
        
	}
    
    public static MainFrameListener getListener() {
        return listener;
    }
    
    public final class MainFrameListener implements ClusterCalculatorListener {

        public void started(String message) {
            System.out.println(message);
        }

        public void calculating(String message) {
            System.out.println(message);
        }

        public void finished(String message) {
            System.out.println(message);
            
            PanelTabResult panelTabResult = (PanelTabResult) scrollResult.getViewport().getComponent(0);
            
            assignScoreValues(panelTabResult.getPanelScore());
            assignMeanValues(panelTabResult.getPanelMean());
            assignDeviationValues(panelTabResult.getPanelDeviation());
            assignStandardScoreStudents(panelTabResult.getPanelStandardScoreStudent());
            assignEuclideanDistance(panelTabResult.getPanelEuclideanDistance());
            
            PanelTabSummary panelTabSummary = (PanelTabSummary) scrollSummary.getViewport().getComponent(0);
            assignSummaryText(panelTabSummary);
        }
        
    }
    
    private void assignSummaryText(PanelTabSummary panel) {
        
        HashMap mapHighestValues = cc.getMapHighestValues();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Modus yang diperoleh dari data diatas adalah sebagai berikut:")
                .append("\n")
                .append("- Scorenya             : ").append(mapHighestValues.get("highestScore"))
                .append("\n")
                .append("- Rata-rata scorenya   : ").append(mapHighestValues.get("highestMean"))
                .append("\n")
                .append("- Standart deviasinya  : ").append(mapHighestValues.get("highestDeviation"))
                .append("\n")
                .append("- Siswa yang nilainya  : ").append(mapHighestValues.get("highestEuclideanDistance"));
        
        JTextArea textAreaSummary = panel.getTextAreaSummary();
        textAreaSummary.setText(sb.toString());
    }
    
    private void assignScoreValues(JPanel toPanel) {
        HashMap mapScorePerQuestions = cc.getMapTotalScorePerQuestion();
        Component[] components = toPanel.getComponents();
        int num = 1;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JTextField) {
                Object score = mapScorePerQuestions.get(num);
                ((JTextField) component).setText(score + "");
                num++;
            }
        }
    }
    
    private void assignMeanValues(JPanel toPanel) {
        HashMap mapMeanPerQuestion = cc.getMapMeanPerQuestion();
        Component[] components = toPanel.getComponents();
        int num = 1;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JTextField) {
                Object score = mapMeanPerQuestion.get(num);
                ((JTextField) component).setText(score + "");
                num++;
            }
        }
    }
    
    private void assignDeviationValues(JPanel toPanel) {
        HashMap mapDeviation = cc.getMapDeviationStandardPerQuestion();
        Component[] components = toPanel.getComponents();
        int num = 1;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JTextField) {
                Object score = mapDeviation.get(num);
                ((JTextField) component).setText(score + "");
                num++;
            }
        }
    }
    
    private void assignStandardScoreStudents(JPanel toPanel) {
        
        List listStandardScoreStudents = cc.getListStandardScoreQuestionsPerStudent();
        System.out.println("listStandardScoreStudents: " + listStandardScoreStudents);
        
        // Create list of students
        List<Student> listStudents = new ArrayList<Student>();
        for (int i = 0; i < listStandardScoreStudents.size(); i++) {
            
            HashMap mapStandardScoreStudent = (HashMap) listStandardScoreStudents.get(i);
            Student student = (Student) mapStandardScoreStudent.get("student");
            listStudents.add(student);

        }
        
        // Create map of standad score
        int numOfQuestions = config.getNumQuestions();
        HashMap mapStandardScoreQuestion = new HashMap();
        for (int i = 0; i < numOfQuestions; i++) {
            mapStandardScoreQuestion.put(i + 1, new ArrayList());
        }
        
        for (int i = 0; i < numOfQuestions; i++) {
            List listStandardScore = new ArrayList();
            int num = i + 1;
            System.out.println("question: " + num);
            for (int c = 0; c < listStandardScoreStudents.size(); c++) {
                System.out.println("c : " + c);
                HashMap mapStandardScoreStudent = (HashMap) listStandardScoreStudents.get(c);
                HashMap mapStandardScore = (HashMap) mapStandardScoreStudent.get("standardScore");
                Object standardScore = mapStandardScore.get(num);
                listStandardScore.add(standardScore);
            }
            
            mapStandardScoreQuestion.put(num, listStandardScore);
        }
        
        System.out.println("listStudents: " + listStudents);
        System.out.println("mapStandardScore: " + mapStandardScoreQuestion);
        
        Component[] components = toPanel.getComponents();
        int textFieldCount = 0;
        int studentCount = 0;
        int num = 1;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            //System.out.println("comp: " + component.getClass());
                            
            if (component instanceof JTextField) {
                if (textFieldCount < listStudents.size()) {    
                    Student student = listStudents.get(textFieldCount);
                    ((JTextField) component).setText(student.getName());
                } else {
                    ArrayList listStandardScore = (ArrayList) mapStandardScoreQuestion.get(num);
                    ((JTextField) component).setText(listStandardScore.get(studentCount) + "");
                    
                    studentCount++;
                    
                    if (studentCount == listStudents.size()) {
                        studentCount = 0;
                        num++;
                    }
                }
                
                textFieldCount++;
            }
        }
    }
    
    private void assignEuclideanDistance(JPanel toPanel) {
        List listEuclideanDistance = cc.getListEuclideanDistanceStudent();
        Component[] components = toPanel.getComponents();
        int textFieldIterator = 0;
        int count = 0;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JTextField) {
                HashMap mapEuclideanDistanceStudent = (HashMap) listEuclideanDistance.get(count);
                Student student = (Student) mapEuclideanDistanceStudent.get("student");
                Object euclideanDistance = mapEuclideanDistanceStudent.get("euclideanDistance");
                
                if (textFieldIterator == 0) {
                    ((JTextField) component).setText(student.getName());
                } else {
                    ((JTextField) component).setText(euclideanDistance + "");
                }
                
                textFieldIterator++;
                
                if (textFieldIterator == 2) {
                    textFieldIterator = 0;
                    count++;
                }
                
            }
        }
    }
    
    
}
