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
import org.apache.log4j.Logger;
import ta.cluster.core.ClusterCalculator;
import ta.cluster.core.ClusterCalculatorListener;
import ta.cluster.core.Configuration;
import ta.cluster.core.DatabaseClusterCalculator;
import ta.cluster.model.StudentModel;
import ta.cluster.tool.Constants;
import ta.cluster.tool.Tools;

/**
 * 
 * @author Matt
 */
public class MainFrame extends JFrame {

    private static final Logger log = Logger.getLogger(MainFrame.class);
    private JTabbedPane panelTabs;
    private JScrollPane scrollStudentGrades;
    private JScrollPane scrollResult;
    private JScrollPane scrollSummary;
    private static MainFrame frame;
    private static ProcessListener listener;
    private ClusterCalculator cc;
    private Configuration config;

    private MainFrame() {
        super("Cluster Application");
        setBounds(
                Tools.getCenterWidth(Constants.MAIN_FRAME_INITIAL_WIDTH),
                Tools.getCenterHeight(Constants.MAIN_FRAME_INITIAL_HEIGHT),
                Constants.MAIN_FRAME_INITIAL_WIDTH,
                Constants.MAIN_FRAME_INITIAL_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // initComponents();
        listener = new ProcessListener();
        // cc = PlainStudentClusterCalculator.getInstance();
        cc = DatabaseClusterCalculator.getInstance();
        config = Configuration.getInstance();
    }

    public static MainFrame getFrame() {
        if (frame == null) {
            frame = new MainFrame();
        }
        return frame;
    }

    public void initComponents() {
        // Clear main panel
        getContentPane().removeAll();

        scrollStudentGrades = new JScrollPane(new PanelTabStudentGrades(this));
        scrollResult = new JScrollPane(new PanelTabResult());
        scrollSummary = new JScrollPane(new PanelTabSummary());

        panelTabs = new JTabbedPane();
        panelTabs.add(scrollStudentGrades, "Nilai Siswa");
        panelTabs.add(scrollResult, "Hasil");
        panelTabs.add(scrollSummary, "Ringkasan");

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
                MainFrame frame = MainFrame.getFrame();
                frame.initComponents();
                frame.setVisible(true);
            }
        });

    }

    public static ProcessListener getProcessListener() {
        return listener;
    }

    public final class ProcessListener implements ClusterCalculatorListener {

        public void started(String message) {
            log.debug(message);
        }

        public void calculating(String message) {
            log.debug(message);
        }

        public void finished(String message) {
            log.debug(message);

            PanelTabResult panelTabResult = (PanelTabResult) scrollResult.getViewport().getComponent(0);

            assignScoreValuesToPanel(panelTabResult.getPanelScore());
            assignMeanValuesToPanel(panelTabResult.getPanelMean());
            assignDeviationValuesToPanel(panelTabResult.getPanelDeviation());
            assignStandardScoreStudentsToPanel(panelTabResult.getPanelStandardScoreStudent());
            assignEuclideanDistanceToPanel(panelTabResult.getPanelEuclideanDistance());

            PanelTabSummary panelTabSummary = (PanelTabSummary) scrollSummary.getViewport().getComponent(0);
            assignSummaryTextToPanel(panelTabSummary);

            panelTabs.setSelectedIndex(1);
        }
    }

    private void assignSummaryTextToPanel(PanelTabSummary panel) {

        HashMap mapHighestValues = cc.getMapHighestValues();

        HashMap mapEuclideanDistance = (HashMap) mapHighestValues.get("highestEuclideanDistance");

        StringBuilder sb = new StringBuilder();
        sb.append("Modus yang diperoleh dari data diatas adalah sebagai berikut:").append("\n")
            .append("- Scorenya             : ").append(mapHighestValues.get("highestScore")).append("\n")
            .append("- Rata-rata scorenya   : ").append(mapHighestValues.get("highestMean")).append("\n")
            .append("- Standart deviasinya  : ").append(mapHighestValues.get("highestDeviation")).append("\n")
            .append("- Siswa yang nilainya  : ").append(mapEuclideanDistance.get("euclideanDistance")).append(" (").append(((StudentModel) mapEuclideanDistance.get("student")).getName()).append(")");

        JTextArea textAreaSummary = panel.getTextAreaSummary();
        textAreaSummary.setText(sb.toString());
    }

    private void assignScoreValuesToPanel(JPanel toPanel) {
        log.debug("Assign score values to panel ... ");
        HashMap mapScorePerQuestions = cc.getMapTotalScorePerQuestion();
        log.debug("Map score per question: ");
        log.debug(mapScorePerQuestions + "");
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

    private void assignMeanValuesToPanel(JPanel toPanel) {
        log.debug("Assign mean values to panel ...");
        HashMap mapMeanPerQuestion = cc.getMapMeanPerQuestion();
        log.debug("Map mean per question: ");
        log.debug("" + mapMeanPerQuestion);
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

    private void assignDeviationValuesToPanel(JPanel toPanel) {
        log.debug("Assign deviation values to panel ...");
        HashMap mapDeviation = cc.getMapDeviationStandardPerQuestion();
        log.debug("Map deviation:");
        log.debug("" + mapDeviation);
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

    private void assignStandardScoreStudentsToPanel(JPanel toPanel) {
        log.debug("Assign standard score student to panel ...");
        List listStandardScoreStudents = cc.getListStandardScoreQuestionsPerStudent();
        log.debug("List standard score students: ");
        log.debug("" + listStandardScoreStudents);

        // Create list of students
        List<StudentModel> listStudents = new ArrayList<StudentModel>();
        for (int i = 0; i < listStandardScoreStudents.size(); i++) {
            HashMap mapStandardScoreStudent = (HashMap) listStandardScoreStudents.get(i);
            StudentModel student = (StudentModel) mapStandardScoreStudent.get("student");
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
            for (int c = 0; c < listStandardScoreStudents.size(); c++) {
                HashMap mapStandardScoreStudent = (HashMap) listStandardScoreStudents.get(c);
                HashMap mapStandardScore = (HashMap) mapStandardScoreStudent.get("standardScore");
                Object standardScore = mapStandardScore.get(num);
                listStandardScore.add(standardScore);
            }
            mapStandardScoreQuestion.put(num, listStandardScore);
        }

        Component[] components = toPanel.getComponents();
        int textFieldCount = 0;
        int studentCount = 0;
        int num = 1;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JTextField) {
                if (textFieldCount < listStudents.size()) {
                    StudentModel student = listStudents.get(textFieldCount);
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

    private void assignEuclideanDistanceToPanel(JPanel toPanel) {
        log.debug("Assign euclidean distance to panel ...");
        List listEuclideanDistance = cc.getListEuclideanDistanceStudent();
        log.debug("List euclidean distance:");
        log.debug("" + listEuclideanDistance);

        Component[] components = toPanel.getComponents();
        int textFieldIterator = 0;
        int count = 0;
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JTextField) {
                HashMap mapEuclideanDistanceStudent = (HashMap) listEuclideanDistance.get(count);
                StudentModel student = (StudentModel) mapEuclideanDistanceStudent.get("student");
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
