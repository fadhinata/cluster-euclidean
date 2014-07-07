/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package najla.project.cluster.model;

import java.util.List;

/**
 *
 * @author Matt
 */
public class Student {
    
    private String name;
    private List<Question> listQuestions;

    public Student() {
        
    }
    
    public Student(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(List<Question> listQuestions) {
        this.listQuestions = listQuestions;
    }

    @Override
    public String toString() {
        return "Student(" + this.name + ": " + this.listQuestions + ")";
    }
}
