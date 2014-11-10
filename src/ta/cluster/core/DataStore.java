/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.List;
import ta.cluster.model.StudentModel;

/**
 *
 * @author Matt
 */
public class DataStore {
 
    private static DataStore instance;
    private List<StudentModel> listStudents;

    private DataStore() {
        
    }
    
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
    
    public List<StudentModel> getListStudents() {
        return listStudents;
    }

    public void setListStudents(List<StudentModel> listStudents) {
        this.listStudents = listStudents;
    }

}
