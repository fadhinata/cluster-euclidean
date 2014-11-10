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
public class StudentModel {
    
    private String name;
    private List<QuestionModel> listQuestions;

    public StudentModel() {
        this.name = Constants.STR_EMPTY;
        this.listQuestions = new ArrayList<QuestionModel>();
    }
    
    public StudentModel(String name) {
        this.name = name;
        this.listQuestions = new ArrayList<QuestionModel>();
    }
    
    public StudentModel(String name, List<QuestionModel> listQuestions) {
        this.name = name;
        this.listQuestions = listQuestions;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuestionModel> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(List<QuestionModel> listQuestions) {
        this.listQuestions = listQuestions;
    }

}
