/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.model;

import java.util.ArrayList;
import java.util.List;
import ta.cluster.tool.Constants;

/**
 *
 * @author Matt
 */
public class Student {
    
    private String name;
    private List<Question> listQuestions;

    public Student() {
        this.name = Constants.STR_EMPTY;
        this.listQuestions = new ArrayList<Question>();
    }
    
    public Student(String name) {
        this.name = name;
        this.listQuestions = new ArrayList<Question>();
    }
    
    public Student(String name, List<Question> listQuestions) {
        this.name = name;
        this.listQuestions = listQuestions;
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

}
