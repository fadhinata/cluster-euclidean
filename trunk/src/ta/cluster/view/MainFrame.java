/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
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
public class MainFrame extends JFrame implements Printable, ActionListener {

    private static final Logger log = Logger.getLogger(MainFrame.class);
    private JTabbedPane panelTabs;
    private JScrollPane scrollStudentGrades;
    private JScrollPane scrollResult;
    private JScrollPane scrollSummary;
    private static MainFrame frame;
    private static ProcessListener listener;
    private ClusterCalculator cc;
    private Configuration config;
    private JDialog loadingDlg;
    private ImageIcon loadingIcon;
    private JButton btnPrint;
    private JButton btnExportCsv;
    private JFileChooser fileChooser;
    private HashMap mapScorePerQuestions;
    private HashMap mapMeanPerQuestion;
    private HashMap mapHighestValues;
    private HashMap mapDeviation;
    private List listStandardScoreStudents;
    private List listEuclideanDistance;

    private final String STR_NEW_LINE = "\n";
    private final String STR_TAB = "    ";
    private final String STR_COMMA = ",";
    
    private MainFrame() {
        super("Cluster Application");
        setBounds(
                Tools.getCenterWidth(Constants.MAIN_FRAME_INITIAL_WIDTH),
                Tools.getCenterHeight(Constants.MAIN_FRAME_INITIAL_HEIGHT),
                Constants.MAIN_FRAME_INITIAL_WIDTH,
                Constants.MAIN_FRAME_INITIAL_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // initComponents();
        listener = new ProcessListener();
        // cc = PlainStudentClusterCalculator.getInstance();
        cc = DatabaseClusterCalculator.getInstance();
        config = Configuration.getInstance();

        loadingIcon = new ImageIcon(getClass().getResource("/ta/cluster/resources/loading-message.gif"));

        loadingDlg = new JDialog(frame, "Cluster Euclidean", true);
        loadingDlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDlg.setSize(300, 150);
        loadingDlg.setLocationRelativeTo(frame);
        loadingDlg.add(new JLabel(loadingIcon));
        
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory() || f.getName().toLowerCase().equals(".csv")) {
                    return true;
                } else {
                    return false;
                }
            } 

            @Override
            public String getDescription() {
                return "Comma Separated Value (*.csv)";
            }
        };
        
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setSelectedFile(new File("cluster-euclidean.csv"));
    }

    private void showLoadingDialog() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                loadingDlg.setVisible(true);
            }
        });
    }

    private void hideLoadingDialog() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                loadingDlg.setVisible(false);
            }
        });
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

        GridBagLayout layoutPanelResult = new GridBagLayout();
        layoutPanelResult.columnWidths = new int[]{0};
        layoutPanelResult.columnWeights = new double[]{1.0};
        layoutPanelResult.rowHeights = new int[]{0};
        layoutPanelResult.rowWeights = new double[]{1.0};

        JPanel panelResult = new JPanel(layoutPanelResult);

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.fill = GridBagConstraints.BOTH;
        panelResult.add(scrollResult, constraint);

        btnPrint = new JButton("Print");
        btnPrint.addActionListener(this);
        btnPrint.setEnabled(false);

        btnExportCsv = new JButton("Export to CSV");
        btnExportCsv.addActionListener(this);
        btnExportCsv.setEnabled(false);
        
        JPanel panelBtn = new JPanel(new GridBagLayout());
        panelBtn.add(btnPrint);
        panelBtn.add(btnExportCsv);
        
        constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.insets = new Insets(5, 5, 5, 5);
        panelResult.add(panelBtn, constraint);

        panelTabs = new JTabbedPane();
        panelTabs.add(scrollStudentGrades, "Nilai Siswa");
        panelTabs.add(panelResult, "Hasil");
        panelTabs.add(scrollSummary, "Ringkasan");

        getContentPane().add(panelTabs, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPrint) {
            log.debug("Prepare print ...");
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            Book book = new Book();
            book.append(MainFrame.this, printerJob.defaultPage());
            printerJob.setPageable(book);        
            if (printerJob.printDialog()) {
                try {
                    printerJob.print();
                    log.debug(printerJob.getPrintService().getName());
                    log.debug("Print completed ...");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.debug("Print cancelled");
                }
            }
        } else if (e.getSource() == btnExportCsv) {
            log.debug("Prepare export to csv");
            int returnVal = fileChooser.showSaveDialog(MainFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if ("".equals(file.getName())) {
                    return;
                }
                
                String path = file.getAbsolutePath();
                log.debug("Path: " + path);
                try {
                    path = path.toLowerCase().endsWith(".csv") ? path : path + ".csv";
                    PrintWriter writer = new PrintWriter(path);
                    String text = buildTextToExport();
                    log.debug("Text to export:");
                    log.debug(text);
                    writer.write(text);
                    writer.close();
                    log.debug("Export completed: " + path);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error(ex, ex);
                }
            } else {
                log.debug("Export cancelled");
            }
        }
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

    // See http://docs.oracle.com/javase/tutorial/2d/printing/examples/PrintDialogExample.java
    // See http://www.java2s.com/Code/Java/2D-Graphics-GUI/PrintinJavaMultipage.htm
    // See http://stackoverflow.com/questions/12794382/how-to-print-strings-with-line-breaks-in-java
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        log.debug("Print ...");

        // Create the Graphics2D object
        Graphics2D g2d = (Graphics2D) graphics;

        // Translate the origin to 0,0 for the top left corner
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Set the drawing color to black
        g2d.setPaint(Color.black);

        String text = buildTextToPrint();
        log.debug("Text to print:");
        log.debug(text);
        
        String[] line = text.split("\n");
        int y = 15;
        Font f = new Font(Font.SERIF, Font.PLAIN, 12);
        graphics.setFont(f);
        for (int i = 0; i < line.length; i++) {
            graphics.drawString(line[i], 5, y);
            y = y + 15;
        }

        // tell the caller that this page is part of the printed document
        return (PAGE_EXISTS);
    }

    private String buildTextToPrint() {
        StringBuilder stringBuilder = new StringBuilder();
        
        // Total score
        stringBuilder.append("Total Score");
        stringBuilder.append(STR_NEW_LINE);
        int num = 1;
        for (int i = 0; i < mapScorePerQuestions.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(": ");
            stringBuilder.append(mapScorePerQuestions.get(num));
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Rata-rata");
        stringBuilder.append(STR_NEW_LINE);
        num = 1;
        for (int i = 0; i < mapMeanPerQuestion.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(": ");
            stringBuilder.append(mapMeanPerQuestion.get(num));
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Standard Deviasi");
        stringBuilder.append(STR_NEW_LINE);
        num = 1;
        for (int i = 0; i < mapDeviation.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(": ");
            stringBuilder.append(mapDeviation.get(num));
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Standard Skor Siswa");
        stringBuilder.append(STR_NEW_LINE);
        for (int i = 0; i < listStandardScoreStudents.size(); i++) {
            HashMap mapStandardScoreStudent = (HashMap) listStandardScoreStudents.get(i);
            StudentModel student = (StudentModel) mapStandardScoreStudent.get("student");
            HashMap mapStandardScore = (HashMap) mapStandardScoreStudent.get("standardScore");
            stringBuilder.append(student.getName());
            stringBuilder.append(STR_NEW_LINE);
            num = 1;
            for (int c = 0; c < mapStandardScore.size(); c++) {
                stringBuilder.append(STR_TAB);
                stringBuilder.append("Soal ");
                stringBuilder.append(num);
                stringBuilder.append(": ");
                stringBuilder.append(mapStandardScore.get(num));
                stringBuilder.append(STR_NEW_LINE);
                num++;
            }
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Euclidean Distance");
        stringBuilder.append(STR_NEW_LINE);
        for (int i = 0; i < listEuclideanDistance.size(); i++) {
             HashMap mapEuclideanDistanceStudent = (HashMap) listEuclideanDistance.get(i);
             StudentModel student = (StudentModel) mapEuclideanDistanceStudent.get("student");
             Object euclideanDistance = mapEuclideanDistanceStudent.get("euclideanDistance");
             stringBuilder.append(student.getName());
             stringBuilder.append(": ");
             stringBuilder.append(euclideanDistance);
             stringBuilder.append(STR_NEW_LINE);
        }
        
        return stringBuilder.toString();
    }
    
    private String buildTextToExport() {
        StringBuilder stringBuilder = new StringBuilder();
        
        // Total score
        stringBuilder.append("Total Score");
        stringBuilder.append(STR_NEW_LINE);
        int num = 1;
        for (int i = 0; i < mapScorePerQuestions.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(STR_COMMA);
            stringBuilder.append(mapScorePerQuestions.get(num));
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Rata-rata");
        stringBuilder.append(STR_NEW_LINE);
        num = 1;
        for (int i = 0; i < mapMeanPerQuestion.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(STR_COMMA);
            stringBuilder.append(mapMeanPerQuestion.get(num));
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Standard Deviasi");
        stringBuilder.append(STR_NEW_LINE);
        num = 1;
        for (int i = 0; i < mapDeviation.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(STR_COMMA);
            stringBuilder.append(mapDeviation.get(num));
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
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

        num = 1;
        for (int i = 0; i < numOfQuestions; i++) {
            List listStandardScore = new ArrayList();
            for (int c = 0; c < listStandardScoreStudents.size(); c++) {
                HashMap mapStandardScoreStudent = (HashMap) listStandardScoreStudents.get(c);
                HashMap mapStandardScore = (HashMap) mapStandardScoreStudent.get("standardScore");
                Object standardScore = mapStandardScore.get(num);
                listStandardScore.add(standardScore);
            }
            mapStandardScoreQuestion.put(num, listStandardScore);
            num++;
        }

        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Standard Skor Siswa");
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append(STR_COMMA);
        for (int i = 0; i < listStudents.size(); i++) {
            stringBuilder.append(listStudents.get(i).getName());
            if (i != (listStudents.size() - 1)) stringBuilder.append(STR_COMMA);
        }
        
        stringBuilder.append(STR_NEW_LINE);
        num = 1;
        for (int i = 0; i < mapStandardScoreQuestion.size(); i++) {
            stringBuilder.append("Soal ");
            stringBuilder.append(num);
            stringBuilder.append(STR_COMMA);
            List listStandardScore = (ArrayList) mapStandardScoreQuestion.get(num);
            for (int c = 0; c < listStandardScore.size(); c++) {
                stringBuilder.append(listStandardScore.get(c));
                if (c != (listStandardScore.size() - 1)) stringBuilder.append(STR_COMMA);
            }
            stringBuilder.append(STR_NEW_LINE);
            num++;
        }
        
        stringBuilder.append(STR_NEW_LINE);
        stringBuilder.append("Euclidean Distance");
        stringBuilder.append(STR_NEW_LINE);
        for (int i = 0; i < listEuclideanDistance.size(); i++) {
             HashMap mapEuclideanDistanceStudent = (HashMap) listEuclideanDistance.get(i);
             StudentModel student = (StudentModel) mapEuclideanDistanceStudent.get("student");
             Object euclideanDistance = mapEuclideanDistanceStudent.get("euclideanDistance");
             stringBuilder.append(student.getName());
             stringBuilder.append(STR_COMMA);
             stringBuilder.append(euclideanDistance);
             stringBuilder.append(STR_NEW_LINE);
        }
        
        return stringBuilder.toString();
    }
    
    private void assignSummaryTextToPanel(PanelTabSummary panel) {

        mapHighestValues = cc.getMapHighestValues();

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
        mapScorePerQuestions = cc.getMapTotalScorePerQuestion();
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
        mapMeanPerQuestion = cc.getMapMeanPerQuestion();
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
        mapDeviation = cc.getMapDeviationStandardPerQuestion();
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
        listStandardScoreStudents = cc.getListStandardScoreQuestionsPerStudent();
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
        listEuclideanDistance = cc.getListEuclideanDistanceStudent();
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

    public final class ProcessListener implements ClusterCalculatorListener {

        public void started(String message) {
            showLoadingDialog();
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

            hideLoadingDialog();
            btnPrint.setEnabled(true);
            btnExportCsv.setEnabled(true);
        }
    }
}
