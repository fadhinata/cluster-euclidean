/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.view;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import ta.cluster.tool.Constants;

/**
 *
 * @author Matt
 */
public class PanelTabSummary extends JPanel {

    private JTextArea textAreaSummary;
    
    public PanelTabSummary() {this.setSize(Constants.MAIN_FRAME_INITIAL_HEIGHT, Constants.MAIN_FRAME_INITIAL_WIDTH);
        initComponents();
    }
    
    private void initComponents() {
        this.setLayout(new GridLayout());
        
        textAreaSummary = new JTextArea();
        textAreaSummary.setEditable(false);
        this.add(textAreaSummary);
    }

    public JTextArea getTextAreaSummary() {
        return textAreaSummary;
    }
    
}
