/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import ta.cluster.model.QuestionModel;
import ta.cluster.model.StudentModel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matt
 */
public class TestDatabaseClusterCalculator {
    
    private static final Logger log = Logger.getLogger(TestDatabaseClusterCalculator.class);
    private static ClusterCalculator cluster = null;
    
    @BeforeClass
    public static void init() {
        log.debug("Initialize ...");
        
        Configuration config = Configuration.getInstance();
        config.setNumQuestions(3);
        config.setNumStudets(3);
        
        List<StudentModel> listStudents = new ArrayList<StudentModel>();
        
        List<QuestionModel> listQuestions = new ArrayList<QuestionModel>();
        listQuestions.add(new QuestionModel(1, 1));
        listQuestions.add(new QuestionModel(2, 5));
        listQuestions.add(new QuestionModel(3, 4));
        
        listStudents.add(new StudentModel("Muhammad", listQuestions));
        
        listQuestions = new ArrayList<QuestionModel>();
        listQuestions.add(new QuestionModel(1, 4));
        listQuestions.add(new QuestionModel(2, 5));
        listQuestions.add(new QuestionModel(3, 3));
        
        listStudents.add(new StudentModel("Ali", listQuestions));
        
        listQuestions = new ArrayList<QuestionModel>();
        listQuestions.add(new QuestionModel(1, 5));
        listQuestions.add(new QuestionModel(2, 3));
        listQuestions.add(new QuestionModel(3, 4));
        
        listStudents.add(new StudentModel("Najla", listQuestions));
        
        DataStore dataStore = DataStore.getInstance();
        dataStore.setListStudents(listStudents);
        
        cluster = DatabaseClusterCalculator.getInstance();
        cluster.process();
    }
    
    @Test
    public void testAssertTotalScorePerQuestion() {
        log.debug("Test assert total score per question ...");
        HashMap mapQuestionScores = cluster.getMapTotalScorePerQuestion();
        log.debug("Result: " + mapQuestionScores);
        
        Assert.assertEquals(10.0, mapQuestionScores.get(1));
        Assert.assertEquals(13.0, mapQuestionScores.get(2));
        Assert.assertEquals(11.0, mapQuestionScores.get(3));
    }
    
    @Test
    public void testAssertMeanPerQuestion() {
        log.debug("Test assert mean per question ...");
        HashMap mapQuestionMean = cluster.getMapMeanPerQuestion();
        log.debug("Result: " + mapQuestionMean); 
        
        Assert.assertEquals(3.3333333333333335, mapQuestionMean.get(1));
        Assert.assertEquals(4.333333333333333, mapQuestionMean.get(2));
        Assert.assertEquals(3.6666666666666665, mapQuestionMean.get(3));
    }
    
    @Test
    public void testAssertDeviationStandardPerQuestion() {
        log.debug("Test assert deviation standard per question ...");
        HashMap mapDeviationStandard = cluster.getMapDeviationStandardPerQuestion();
        log.debug("Result: " + mapDeviationStandard);
        
        Assert.assertEquals(4.714045207910316, mapDeviationStandard.get(1));
        Assert.assertEquals(6.128258770283413, mapDeviationStandard.get(2));
        Assert.assertEquals(5.185449728701349, mapDeviationStandard.get(3));
    }
    
    @Test
    public void testAssertStandardScoreQuestionsPerStudent() {
        log.debug("Test assert standard score question per student ...");
        List listStandardScore = cluster.getListStandardScoreQuestionsPerStudent();
        log.debug("Result: " + listStandardScore); 
        
        for (int i = 0; i < listStandardScore.size(); i++) {
            HashMap map = (HashMap) listStandardScore.get(i);
            StudentModel student = (StudentModel) map.get("student");
            HashMap standardScore = (HashMap) map.get("standardScore");
            if ("Muhammad".equals(student.getName())) {
                Assert.assertEquals(1.4142135623730951, standardScore.get(1));
                Assert.assertEquals(0.9246780984747159, standardScore.get(2));
                Assert.assertEquals(1.2213662584131275, standardScore.get(3));
                
            } else if ("Najla".equals(student.getName())) {
                Assert.assertEquals(1.8384776310850237, standardScore.get(1));
                Assert.assertEquals(1.2510350744069685, standardScore.get(2));
                Assert.assertEquals(1.6070608663330626, standardScore.get(3));
                
            } else if ("Ali".equals(student.getName())) {
                Assert.assertEquals(1.8384776310850237, standardScore.get(1));
                Assert.assertEquals(1.2510350744069685, standardScore.get(2));
                Assert.assertEquals(1.6070608663330626, standardScore.get(3));
                
            }
        }
    }
    
    @Test
    public void testAssertEuclideanDistanceStudent() {
        log.debug("Test assert euclidean distance student ...");
        List listEculideanDistance = cluster.getListEuclideanDistanceStudent();
        log.debug("Result: " + listEculideanDistance);
        
        for (int i = 0; i < listEculideanDistance.size(); i++) {
            HashMap map = (HashMap) listEculideanDistance.get(i);
            StudentModel student = (StudentModel) map.get("student");
            double euclideanDistance = Double.valueOf(map.get("euclideanDistance") + "");
            if ("Muhammad".equals(student.getName())) {
                Assert.assertEquals(0.718934911242604, euclideanDistance);
            } else if ("Najla".equals(student.getName())) {
                Assert.assertEquals(1.0352662721893504, euclideanDistance);
            } else if ("Ali".equals(student.getName())) {
                Assert.assertEquals(1.0352662721893504, euclideanDistance);
            }
        }
    }
    
}
