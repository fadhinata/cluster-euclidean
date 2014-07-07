/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster.controller;

import java.util.List;
import najla.project.cluster.model.Student;

/**
 *
 * @author Matt
 */
public class DataController {
 
    private static DataController instance;
    private List<Student> listStudents;

    private DataController() {
        
    }
    
    public static DataController getInstance() {
        if (instance == null) {
            instance = new DataController();
        }
        return instance;
    }
    
    public List<Student> getListStudent() {
        return listStudents;
    }

    public void setListStudent(List<Student> listStudents) {
        this.listStudents = listStudents;
    }

}
