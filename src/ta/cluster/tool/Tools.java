/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.tool;

import java.awt.Toolkit;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Map.Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /*
     * Paramterized method to sort Map e.g. HashMap or Hashtable in Java
     * throw NullPointerException if Map contains null key
     */
    public static <K extends Comparable, V extends Comparable> Map<K, V> sortByKeys(Map<K, V> map) {
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (K key : keys) {
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }
    
}
