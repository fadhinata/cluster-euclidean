/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.model;

/**
 *
 * @author Matt
 */
public class QuestionModel {
    
    private int number;
    private double score;

    public QuestionModel() {
        
    }
    
    public QuestionModel(int number, double score) {
        this.number = number;
        this.score = score;
    }
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

}
