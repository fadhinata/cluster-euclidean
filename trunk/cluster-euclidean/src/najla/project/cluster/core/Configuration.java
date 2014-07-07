/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster.core;

/**
 *
 * @author Matt
 */
public class Configuration {
    
    private static Configuration instance;
    private int numStudents;
    private int numQuestions;

    private Configuration() {
        
    }
    
    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    
    public int getNumStudents() {
        return numStudents;
    }
    
    public void setNumStudets(int numStudents) {
        this.numStudents = numStudents;
    }
    
    public int getNumQuestions() {
        return numQuestions;
    }
    
    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }
    
}
