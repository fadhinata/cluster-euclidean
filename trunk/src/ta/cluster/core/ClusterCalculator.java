/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ta.cluster.model.Question;
import ta.cluster.model.Student;

/**
 *
 * @author Matt
 */
public class ClusterCalculator {
    
    private DataStore dataStore;
    private Configuration config;
    private HashMap mapTotalScorePerQuestion;
    private HashMap mapMeanPerQuestion;
    private HashMap mapDeviationStandardPerQuestion;
    private HashMap mapHighestValues;
    private List listStandardScoreQuestionsPerStudent;
    private List listEuclideanDistanceStudent;
    private static ClusterCalculator instance;
    private ClusterCalculatorListener listener;
    
    private ClusterCalculator() {
        dataStore = DataStore.getInstance();
        config = Configuration.getInstance();
    }
    
    public static ClusterCalculator getInstance() {
        if (instance == null) {
            instance = new ClusterCalculator();
        }
        return instance;
    }
    
    public void setListener(ClusterCalculatorListener listener) {
        this.listener = listener;
    }
    
    public void processCalculation() {
        if (listener != null) listener.started("Start calculation");
        
        mapTotalScorePerQuestion = calculateTotalScorePerQuestion();
        mapMeanPerQuestion = calculateMeanPerQuestion();
        mapDeviationStandardPerQuestion = calculateDeviationStandardPerQuestion();
        listStandardScoreQuestionsPerStudent = calculateStandardScoreQuestionsPerStudent();
        listEuclideanDistanceStudent = calculateEuclideanDistanceStudent();
        mapHighestValues = calculateHighestVaules();
        
        if (listener != null) listener.finished("Calculation finished");
    }
    
    public HashMap calculateTotalScorePerQuestion() {
        if (listener != null) listener.calculating("Calculate total score per question ...");
        
        List<Student> listStudents = dataStore.getListStudents();
        int numOfQuestions = config.getNumQuestions();
        HashMap mapQuestionScores = new HashMap(numOfQuestions);
        for (Student student : listStudents) {
            List<Question> listQuestions = student.getListQuestions();
            for (Question question : listQuestions) {
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
    
    public HashMap calculateMeanPerQuestion() {
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
    
    public HashMap calculateDeviationStandardPerQuestion() {
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
            double totalScore = Double.valueOf(mapTotalScorePerQuestion.get(i + 1) + "");
            double mean = Double.valueOf(mapMeanPerQuestion.get(i + 1) + "");
            double deviationStandard = Math.sqrt(((totalScore - mean) * (totalScore - mean)) / (numOfStudents - 1));
            mapStandardDeviation.put(i + 1, deviationStandard);
        }
        
        return mapStandardDeviation;
    }
    
    public List calculateTotalScorePerStudent() {
        if (listener != null) listener.calculating("Calculate total score per student ...");
        
        List<Student> listStudents = dataStore.getListStudents();
        int numOfStudents = config.getNumStudents();
        List listTotalScore = new ArrayList(numOfStudents);
        for (Student student : listStudents) {
            List<Question> listQuestions = student.getListQuestions();
            double totalScore = 0;
            for (Question question : listQuestions) {
                totalScore += question.getScore();
            }
            HashMap mapTotalScore = new HashMap();
            mapTotalScore.put("student", student);
            mapTotalScore.put("total", totalScore);
            listTotalScore.add(mapTotalScore);
        }
        
        return listTotalScore;
    }
    
    public List calculateStandardScoreQuestionsPerStudent() {
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
            Student student = (Student) mapTotalScore.get("student");
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
 
    public List calculateEuclideanDistanceStudent() {
        if (listener != null) listener.calculating("Calculate euclidean distance student ...");
        
        List listEuclideanDistance = new ArrayList();
        int numOfStudents = config.getNumStudents();
        
        if (listStandardScoreQuestionsPerStudent == null) {
            listStandardScoreQuestionsPerStudent = calculateStandardScoreQuestionsPerStudent();
        }
        
        for (int i = 0; i < listStandardScoreQuestionsPerStudent.size(); i++) {
            HashMap mapStandardScoreStudent = (HashMap) listStandardScoreQuestionsPerStudent.get(i);
            Student student = (Student) mapStandardScoreStudent.get("student");
            HashMap mapStandardScoreQuestion = (HashMap) mapStandardScoreStudent.get("standardScore");
            
            double highestScore = getHighestScore(mapStandardScoreQuestion);
            double lowestScore = getLowestScore(mapStandardScoreQuestion);
            double euclideanDistance = ((highestScore - lowestScore) * (highestScore - lowestScore)) * numOfStudents;
            
            HashMap mapEuclidean = new HashMap();
            mapEuclidean.put("student", student);
            mapEuclidean.put("euclideanDistance", euclideanDistance);
            listEuclideanDistance.add(mapEuclidean);
        }
        
        return listEuclideanDistance;
    }
    
    public HashMap calculateHighestVaules() {
        double highestScorePerQuestion = getHighestScore(mapTotalScorePerQuestion);
        double highestMean = getHighestScore(mapMeanPerQuestion);
        double highestDeviation = getHighestScore(mapDeviationStandardPerQuestion);
        
        HashMap mapEuclideanDistance = new HashMap();
        for (int i = 0; i < listEuclideanDistanceStudent.size(); i++) {
            HashMap map = (HashMap) listEuclideanDistanceStudent.get(i);
            mapEuclideanDistance.put(i, map.get("euclideanDistance"));
        }
        double highestEuclideanDistance = getHighestScore(mapEuclideanDistance);
        
        HashMap mapHighest = new HashMap();
        mapHighest.put("highestScore", highestScorePerQuestion);
        mapHighest.put("highestMean", highestMean);
        mapHighest.put("highestDeviation", highestDeviation);
        mapHighest.put("highestEuclideanDistance", highestEuclideanDistance);
        return mapHighest;
    }
    
    private double getHighestScore(HashMap mapScores) {
        double highest = 0;
        double[] arrScores = new double[mapScores.size()];
        int i = 0;
        Iterator iterator = mapScores.values().iterator();
        while (iterator.hasNext()) {
            double value = Double.valueOf(iterator.next() + "");
            arrScores[i] = value;
            i++;
        }
        
        Arrays.sort(arrScores);
        highest = arrScores[arrScores.length - 1];
        
        return highest;
    }
    
    private double getLowestScore(HashMap mapScores) {
        double lowest = 0;
        double[] arrScores = new double[mapScores.size()];
        int i = 0;
        Iterator iterator = mapScores.values().iterator();
        while (iterator.hasNext()) {
            double value = Double.valueOf(iterator.next() + "");
            arrScores[i] = value;
            i++;
        }
        
        Arrays.sort(arrScores);
        lowest = arrScores[0];
        
        return lowest;
    }
    
    public List getListEuclideanDistanceStudent() {
        return listEuclideanDistanceStudent;
    }

    public void setListEuclideanDistanceStudent(List listEuclideanDistanceStudent) {
        this.listEuclideanDistanceStudent = listEuclideanDistanceStudent;
    }

    public List getListStandardScoreQuestionsPerStudent() {
        return listStandardScoreQuestionsPerStudent;
    }

    public void setListStandardScoreQuestionsPerStudent(List listStandardScoreQuestionsPerStudent) {
        this.listStandardScoreQuestionsPerStudent = listStandardScoreQuestionsPerStudent;
    }

    public HashMap getMapDeviationStandardPerQuestion() {
        return mapDeviationStandardPerQuestion;
    }

    public void setMapDeviationStandardPerQuestion(HashMap mapDeviationStandardPerQuestion) {
        this.mapDeviationStandardPerQuestion = mapDeviationStandardPerQuestion;
    }

    public HashMap getMapMeanPerQuestion() {
        return mapMeanPerQuestion;
    }

    public void setMapMeanPerQuestion(HashMap mapMeanPerQuestion) {
        this.mapMeanPerQuestion = mapMeanPerQuestion;
    }

    public HashMap getMapTotalScorePerQuestion() {
        return mapTotalScorePerQuestion;
    }

    public void setMapTotalScorePerQuestion(HashMap mapTotalScorePerQuestion) {
        this.mapTotalScorePerQuestion = mapTotalScorePerQuestion;
    }

    public HashMap getMapHighestValues() {
        return mapHighestValues;
    }

    public void setMapHighestValues(HashMap mapHighestValues) {
        this.mapHighestValues = mapHighestValues;
    }

}
