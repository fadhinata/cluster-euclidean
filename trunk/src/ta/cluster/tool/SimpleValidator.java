/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.tool;

/**
 *
 * @author Matt
 */
public class SimpleValidator {
    
    /**
     * Check if object is numeric
     * @param any
     * @return boolean numeric
     */
    public static boolean isNumber(Object any) {
        try {
            Double.parseDouble(String.valueOf(any));
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    /**
     * Check if object contains number
     * @param any
     * @return 
     */
    public static boolean containsNumber(Object any) {
        if (String.valueOf(any).matches(".*\\d.*")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check if object equals empty string 
     * @param any
     * @return true if object equals empty string
     */
    public static boolean isEmpty(Object any) {
        if ("".equals(any)) return true;
        else return false;
    }
 
    /**
     * Check if object is NULL
     * @param any
     * @return true if object is NULL
     */
    public static boolean isNull(Object any) {
        if (any == null) return true;
        else return false;
    }
    
    /**
     * Check if a number is greater than other number
     * @param number to be compared
     * @param comparison the comparison number
     * @return true if number is greater than its comparison
     */
    public static boolean isGreater(double number, double comparison) {
        if (number > comparison) return true;
        else return false;
    }
    
    /**
     * Check if a number is smaller than other number
     * @param number to be compared
     * @param comparison the comparison number
     * @return true if number is smaller than its comparison
     */
    public static boolean isSmaller(double number, double comparison) {
        if (number < comparison) return true;
        else return false;
    }
    
    /**
     * Check if a number is greater than or equals other number
     * @param number to be compared
     * @param comparison the comparison number
     * @return true if number is greater than or equals its comparison
     */
    public static boolean isGreaterEqual(double number, double comparison) {
        if (number >= comparison) return true;
        else return false;
    }
    
    /**
     * Check if a number is smaller than or equals other number
     * @param number to be compared
     * @param comparison the comparison number
     * @return true if number is smaller than or equals its comparison
     */
    public static boolean isSmallerEqual(double number, double comparison) {
        if (number <= comparison) return true;
        else return false;
    }
    
}
