/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ta.cluster.model.Question;
import ta.cluster.model.Student;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Matt
 */
public class TestClusterCalculator {
    
    @Before
    public void init() {
        
        Configuration config = Configuration.getInstance();
        config.setNumQuestions(3);
        config.setNumStudets(3);
        
        List<Student> listStudents = new ArrayList<Student>();
        
        List<Question> listQuestions = new ArrayList<Question>();
        listQuestions.add(new Question(1, 90));
        listQuestions.add(new Question(2, 80));
        listQuestions.add(new Question(3, 70));
        
        listStudents.add(new Student("Muhammad", listQuestions));
        
        listQuestions = new ArrayList<Question>();
        listQuestions.add(new Question(1, 100));
        listQuestions.add(new Question(2, 100));
        listQuestions.add(new Question(3, 80));
        
        listStudents.add(new Student("Ali", listQuestions));
        
        listQuestions = new ArrayList<Question>();
        listQuestions.add(new Question(1, 90));
        listQuestions.add(new Question(2, 70));
        listQuestions.add(new Question(3, 70));
        
        listStudents.add(new Student("Najla", listQuestions));
        
        DataStore dataStore = DataStore.getInstance();
        dataStore.setListStudents(listStudents);
        
    }
    
    @Test
    public void testCalculateTotalScorePerQuestion() {
        System.out.println("testCalculateTotalScorePerQuestion()");
        ClusterCalculator cluster = ClusterCalculator.getInstance();
        HashMap mapQuestionScores = cluster.calculateTotalScorePerQuestion();
        System.out.println("TOTAL SCORE QUESTIONS: " + mapQuestionScores);
    }
    
    @Test
    public void testCalculateMeanPerQuestion() {
        System.out.println("testCalculateMeanPerQuestion()");
        ClusterCalculator cluster = ClusterCalculator.getInstance();
        HashMap mapQuestionMean = cluster.calculateMeanPerQuestion();
        System.out.println("MEAN QUESTIONS: " + mapQuestionMean);
    }
    
    @Test
    public void testCalculateDeviationStandardPerQuestion() {
        System.out.println("testCalculateDeviationStandardPerQuestion()");
        ClusterCalculator cluster = ClusterCalculator.getInstance();
        HashMap mapDeviationStandard = cluster.calculateDeviationStandardPerQuestion();
        System.out.println("DEVIATION STANDARD QUESTIONS " + mapDeviationStandard);
    }
    
    @Test
    public void testCalculateTotalScorePerStudent() {
        System.out.println("testCalculateTotalScorePerStudent()");
        ClusterCalculator cluster = ClusterCalculator.getInstance();
        List listTotalScore = cluster.calculateTotalScorePerStudent();
        System.out.println("TOTAL SCORE STUDENTS: " + listTotalScore);
    }
    
    @Test
    public void testCalculateStandardScoreQuestionsPerStudent() {
        System.out.println("testCalculateStandardScoreQuestionsPerStudent()");
        ClusterCalculator cluster = ClusterCalculator.getInstance();
        List listStandardScore = cluster.calculateStandardScoreQuestionsPerStudent();
        System.out.println("STANDARD SCORE STUDENTS: " + listStandardScore);
    }
    
    @Test
    public void testCalculateEuclideanDistanceStudent() {
        System.out.println("testCalculateEuclideanDistanceStudent()");
        ClusterCalculator cluster = ClusterCalculator.getInstance();
        List listEculideanDistance = cluster.calculateEuclideanDistanceStudent();
        System.out.println("EUCLIDEAN DISTANCE STUDENT: " + listEculideanDistance);
    }
    
}
