/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.tool;

import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    public static void sortListOfMap(List list, final String key, final boolean ascending) {
        
        Collections.sort(list, new Comparator<HashMap>() {

            public int compare(HashMap o1, HashMap o2) {
                double d1 = Double.valueOf(o1.get(key) + "");
                double d2 = Double.valueOf(o2.get(key) + "");
                if (ascending) { 
                    return d1 > d2 ? 0 : 1;
                }
                return d1 < d2 ? 0 : 1; 
            }
        
        });
    }
    
    public static double getHighestValue(HashMap mapScores) {
        double highest = 0;
        double[] arrScores = new double[mapScores.size()];
        int i = 0;
        Iterator iterator = mapScores.values().iterator();
        while (iterator.hasNext()) {
            double value = Double.valueOf(iterator.next() + "");
            arrScores[i] = value;
            i++;
        }
        
        Arrays.sort(arrScores);
        highest = arrScores[arrScores.length - 1];
        
        return highest;
    }
    
    public static double getLowestValue(HashMap mapScores) {
        double lowest = 0;
        double[] arrScores = new double[mapScores.size()];
        int i = 0;
        Iterator iterator = mapScores.values().iterator();
        while (iterator.hasNext()) {
            double value = Double.valueOf(iterator.next() + "");
            arrScores[i] = value;
            i++;
        }
        
        Arrays.sort(arrScores);
        lowest = arrScores[0];
        
        return lowest;
    }
    
}
