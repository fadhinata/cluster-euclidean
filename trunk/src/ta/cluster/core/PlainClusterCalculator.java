/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import ta.cluster.model.QuestionModel;
import ta.cluster.model.StudentModel;
import ta.cluster.tool.Tools;

/**
 *
 * @author Matt
 */
public class PlainClusterCalculator extends AbstractClusterCalculator {
    
    private static final Logger log = Logger.getLogger(PlainClusterCalculator.class);
    
    private DataStore dataStore;
    private Configuration config;
    private HashMap mapTotalScorePerQuestion;
    private HashMap mapMeanPerQuestion;
    private HashMap mapDeviationStandardPerQuestion;
    private HashMap mapHighestValues;
    private List listStandardScoreQuestionsPerStudent;
    private List listEuclideanDistanceStudent;
    private static PlainClusterCalculator instance;
    private ClusterCalculatorListener listener;
    
    private PlainClusterCalculator() {
        dataStore = DataStore.getInstance();
        config = Configuration.getInstance();
    }
    
    public static PlainClusterCalculator getInstance() {
        if (instance == null) {
            instance = new PlainClusterCalculator();
        }
        return instance;
    }
    
    public void setListener(ClusterCalculatorListener listener) {
        this.listener = listener;
    }
    
    public void beforeCalculation() {
        if (listener != null) listener.started("Start calculation");
    }
    
    public void processCalculation() {
        mapTotalScorePerQuestion = calculateTotalScorePerQuestion();
        mapMeanPerQuestion = calculateMeanPerQuestion();
        mapDeviationStandardPerQuestion = calculateDeviationStandardPerQuestion();
        listStandardScoreQuestionsPerStudent = calculateStandardScoreQuestionsPerStudent();
        listEuclideanDistanceStudent = calculateEuclideanDistanceStudent();
        mapHighestValues = calculateHighestValues();
    }
    
    public void afterCalculation() {
        if (listener != null) listener.finished("Calculation finished");
    }
    
    private HashMap calculateTotalScorePerQuestion() {
        if (listener != null) listener.calculating("Calculate total score per question ...");
        
        List<StudentModel> listStudents = dataStore.getListStudents();
        int numOfQuestions = config.getNumQuestions();
        HashMap mapQuestionScores = new HashMap(numOfQuestions);
        for (StudentModel student : listStudents) {
            List<QuestionModel> listQuestions = student.getListQuestions();
            for (QuestionModel question : listQuestions) {
                int number = question.getNumber();
                double score = question.getScore();
                if (mapQuestionScores.containsKey(number)) {
                    double currentScore = Double.valueOf(mapQuestionScores.get(number) + "") + score;
                    mapQuestionScores.put(number, currentScore);
                } else {
                    mapQuestionScores.put(number, score);
                }
            }
        }
        
        return mapQuestionScores;
    }
    
    private HashMap calculateMeanPerQuestion() {
        if (listener != null) listener.calculating("Calculate mean per question ...");
        
        int numOfStudents = config.getNumStudents();
        int numOfQuestions = config.getNumQuestions();
        
        if (mapTotalScorePerQuestion == null) {
            mapTotalScorePerQuestion = calculateTotalScorePerQuestion();
        }
        
        HashMap mapQuestionMean = new HashMap(numOfQuestions);
        for (int i = 0; i < numOfQuestions; i++) {
            double mean = Double.valueOf(mapTotalScorePerQuestion.get(i + 1) + "") / numOfStudents;
            mapQuestionMean.put(i + 1, mean);
        }
        
        return mapQuestionMean;
    }
    
    private HashMap calculateDeviationStandardPerQuestion() {
        if (listener != null) listener.calculating("Calculate deviation standard per question ...");
        
        int numOfStudents = config.getNumStudents();
        int numOfQuestions = config.getNumQuestions();
        
        if (mapTotalScorePerQuestion == null) {
            mapTotalScorePerQuestion = calculateTotalScorePerQuestion();
        }
        
        if (mapMeanPerQuestion == null) {
            mapMeanPerQuestion = calculateMeanPerQuestion();
        }
        
        HashMap mapStandardDeviation = new HashMap(numOfQuestions);
        for (int i = 0; i < numOfQuestions; i++) {
            int questionNum = i + 1;
            double totalScore = Double.valueOf(mapTotalScorePerQuestion.get(questionNum) + "");
            double mean = Double.valueOf(mapMeanPerQuestion.get(questionNum) + "");
            double deviationStandard = Math.sqrt(((totalScore - mean) * (totalScore - mean)) / (numOfStudents - 1));
            mapStandardDeviation.put(questionNum, deviationStandard);
        }
        
        return mapStandardDeviation;
    }
    
    private List calculateTotalScorePerStudent() {
        if (listener != null) listener.calculating("Calculate total score per student ...");
        
        List<StudentModel> listStudents = dataStore.getListStudents();
        int numOfStudents = config.getNumStudents();
        List listTotalScore = new ArrayList(numOfStudents);
        for (StudentModel student : listStudents) {
            List<QuestionModel> listQuestions = student.getListQuestions();
            double totalScore = 0;
            for (QuestionModel question : listQuestions) {
                totalScore += question.getScore();
            }
            HashMap mapTotalScore = new HashMap();
            mapTotalScore.put("student", student);
            mapTotalScore.put("total", totalScore);
            listTotalScore.add(mapTotalScore);
        }
        
        return listTotalScore;
    }
    
    private List calculateStandardScoreQuestionsPerStudent() {
        if (listener != null) listener.calculating("Calculate standard score question per student ...");
        
        List listStandardScore = new ArrayList();
        List listTotalScore = calculateTotalScorePerStudent();
        
        if (mapMeanPerQuestion == null) {
            mapMeanPerQuestion = calculateMeanPerQuestion();
        }
        
        if (mapDeviationStandardPerQuestion == null) {
            mapDeviationStandardPerQuestion = calculateDeviationStandardPerQuestion();
        }
        
        for (int i = 0; i < listTotalScore.size(); i++) {
            HashMap mapTotalScore = (HashMap) listTotalScore.get(i);
            StudentModel student = (StudentModel) mapTotalScore.get("student");
            double totalScore = Double.valueOf("" + mapTotalScore.get("total"));
            
            HashMap mapStandardScoreQuestion = new HashMap();
            for (int c = 0; c < mapMeanPerQuestion.size(); c++) {
                double mean = Double.valueOf(mapMeanPerQuestion.get(c + 1) + "");
                double deviation = Double.valueOf(mapDeviationStandardPerQuestion.get(c+1) + "");
                double standardScore = (totalScore - mean) / deviation;
                mapStandardScoreQuestion.put(c + 1, standardScore);
            }
            
            HashMap mapStandardScoreStudent = new HashMap();
            mapStandardScoreStudent.put("student", student);
            mapStandardScoreStudent.put("standardScore", mapStandardScoreQuestion);
            listStandardScore.add(mapStandardScoreStudent);
        }
        
        return listStandardScore;
    }
 
    private List calculateEuclideanDistanceStudent() {
        if (listener != null) listener.calculating("Calculate euclidean distance student ...");
        
        List listEuclideanDistance = new ArrayList();
        int numOfStudents = config.getNumStudents();
        
        if (listStandardScoreQuestionsPerStudent == null) {
            listStandardScoreQuestionsPerStudent = calculateStandardScoreQuestionsPerStudent();
        }
        
        for (int i = 0; i < listStandardScoreQuestionsPerStudent.size(); i++) {
            HashMap mapStandardScoreStudent = (HashMap) listStandardScoreQuestionsPerStudent.get(i);
            StudentModel student = (StudentModel) mapStandardScoreStudent.get("student");
            HashMap mapStandardScoreQuestion = (HashMap) mapStandardScoreStudent.get("standardScore");
            
            double highestScore = Tools.getHighestValue(mapStandardScoreQuestion);
            double lowestScore = Tools.getLowestValue(mapStandardScoreQuestion);
            log.debug("highest: " + highestScore + ", lowest: " + lowestScore);
            double euclideanDistance = ((highestScore - lowestScore) * (highestScore - lowestScore)) * numOfStudents;
            
            HashMap mapEuclidean = new HashMap();
            mapEuclidean.put("student", student);
            mapEuclidean.put("euclideanDistance", euclideanDistance);
            listEuclideanDistance.add(mapEuclidean);
        }
        
        return listEuclideanDistance;
    }
    
    private HashMap calculateHighestValues() {
        if (listener != null) listener.calculating("Calculate highest values ...");
        
        double highestScorePerQuestion = Tools.getHighestValue(mapTotalScorePerQuestion);
        double highestMean = Tools.getHighestValue(mapMeanPerQuestion);
        double highestDeviation = Tools.getHighestValue(mapDeviationStandardPerQuestion);
        
        HashMap mapEuclideanDistance = new HashMap();
        for (int i = 0; i < listEuclideanDistanceStudent.size(); i++) {
            HashMap map = (HashMap) listEuclideanDistanceStudent.get(i);
            mapEuclideanDistance.put(i, map.get("euclideanDistance"));
        }
        
        Tools.sortListOfMap(listEuclideanDistanceStudent, "euclideanDistance", true);
        
        HashMap mapHighest = new HashMap();
        mapHighest.put("highestScore", highestScorePerQuestion);
        mapHighest.put("highestMean", highestMean);
        mapHighest.put("highestDeviation", highestDeviation);
        mapHighest.put("highestEuclideanDistance", listEuclideanDistanceStudent.get(0));
        return mapHighest;
    }
    
    public List getListEuclideanDistanceStudent() {
        return listEuclideanDistanceStudent;
    }

    public List getListStandardScoreQuestionsPerStudent() {
        return listStandardScoreQuestionsPerStudent;
    }

    public HashMap getMapDeviationStandardPerQuestion() {
        return mapDeviationStandardPerQuestion;
    }

    public HashMap getMapMeanPerQuestion() {
        return mapMeanPerQuestion;
    }

    public HashMap getMapTotalScorePerQuestion() {
        return mapTotalScorePerQuestion;
    }

    public HashMap getMapHighestValues() {
        return mapHighestValues;
    }

    public static void main(String[] args) {
        StudentModel studentA = new StudentModel("A");
        StudentModel studentB = new StudentModel("B");
        StudentModel studentC = new StudentModel("C");
        
        double scoreA = 2.0093884;
        double scoreB = 1.343892;
        double scoreC = 3.221982;
        
        List list = new ArrayList();
        
        HashMap map = new HashMap();
        map.put("student", studentA);
        map.put("euclideanDistance", scoreA);
        list.add(map);
        
        map = new HashMap();
        map.put("student", studentB);
        map.put("euclideanDistance", scoreB);
        list.add(map);
        
        map = new HashMap();
        map.put("student", studentC);
        map.put("euclideanDistance", scoreC);
        list.add(map);
        
        Tools.sortListOfMap(list, "euclideanDistance", true);
        System.out.println("SORTED: " + list);
    }
    
}
