/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.List;
import ta.cluster.model.Student;

/**
 *
 * @author Matt
 */
public class DataStore {
 
    private static DataStore instance;
    private List<Student> listStudents;

    private DataStore() {
        
    }
    
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    public List<Student> getListStudents() {
        return listStudents;
    }

    public void setListStudents(List<Student> listStudents) {
        this.listStudents = listStudents;
    }

}
