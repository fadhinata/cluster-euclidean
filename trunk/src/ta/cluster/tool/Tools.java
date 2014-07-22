/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.tool;

import java.awt.Toolkit;

/**
 *
 * @author Matt
 */
public class Tools {
    
    private static Toolkit toolkit;
    
    static {
        toolkit = Toolkit.getDefaultToolkit();
    }
    
    public static int getScreenHeight() {
        return (int) toolkit.getScreenSize().getHeight();
    }
    
    public static int getScreenWidth() {
        return (int) toolkit.getScreenSize().getWidth();
    }
 
    public static int getCenterHeight(double windowHeight) {
        return (int) (getScreenHeight() - windowHeight) / 2;
    }
    
    public static int getCenterWidth(double windowWidth) {
        return (int) (getScreenWidth() - windowWidth) / 2;
    }
    
    public static double addValues() {
        double value = 0;
        return value;
    }
    
}
