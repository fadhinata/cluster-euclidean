/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import ta.cluster.dao.StudentDao;
import ta.cluster.dao.StudentQuestionMappingDao;
import ta.cluster.entity.Student;
import ta.cluster.entity.StudentQuestionMapping;
import ta.cluster.model.QuestionModel;
import ta.cluster.model.StudentModel;
import ta.cluster.tool.Tools;

/**
 *
 * @author Matt
 */
public class DatabaseClusterCalculator extends AbstractClusterCalculator {
    
    private static final Logger log = Logger.getLogger(DatabaseClusterCalculator.class);
    
    private static DatabaseClusterCalculator instance;
    private DataStore dataStore;
    private HashMap mapTotalScorePerQuestion;
    private HashMap mapMeanPerQuestion;
    private HashMap mapDeviationStandardPerQuestion;
    private HashMap mapHighestValues;
    private List listStandardScoreQuestionsPerStudent;
    private List listEuclideanDistanceStudent;
    private ClusterCalculatorListener listener;
    
    private StudentDao studentDao;
    private StudentQuestionMappingDao studentQuestionMappingDao;
    
    private DatabaseClusterCalculator() {
        dataStore = DataStore.getInstance();
    }
    
    public static DatabaseClusterCalculator getInstance() {
        if (instance == null) {
            instance = new DatabaseClusterCalculator();
        }
        return instance;
    }
    
    public void setListener(ClusterCalculatorListener listener) {
        this.listener = listener;
    }
    
    public void beforeCalculation() {
        if (listener != null) listener.started("Start calculation");

        studentDao = new StudentDao();
        studentQuestionMappingDao = new StudentQuestionMappingDao();
        
        try {
            // Clear table student
            studentDao.deleteAll();
            log.debug("Clear table student success");
            
            try {
                
                // Clear table student question mapping
                studentQuestionMappingDao.deleteAll();
                log.debug("Clear table student question mapping success");

                for (StudentModel s : dataStore.getListStudents()) {
                    log.debug("Name: " + s.getName());
                    Student student = new Student();
                    student.setName(s.getName());
                    try {                    
                        studentDao.beginTransaction();
                        studentDao.insert(student);
                        log.debug("Insert student success");

                        long studentId = student.getId();
                        log.debug("Student id: " + studentId);

                        for (QuestionModel q : s.getListQuestions()) {
                            log.debug("Question: " + q.getNumber());
                            int number = q.getNumber();
                            double score = q.getScore();

                            StudentQuestionMapping studentQuestionMapping = new StudentQuestionMapping();
                            studentQuestionMapping.setQuestionNumber(number);
                            studentQuestionMapping.setScore(score);
                            studentQuestionMapping.setStudentId(studentId);

                            try {
                                studentQuestionMappingDao.beginTransaction();
                                studentQuestionMappingDao.insert(studentQuestionMapping);
                                log.debug("Insert student question mapping success");
                                log.debug("Student question mapping id: " + studentQuestionMapping.getId());

                                studentQuestionMappingDao.commitTransaction();
                            } catch (Exception e) {
                                log.error("Insert student question mapping failed, rollback transaction", e);
                                studentQuestionMappingDao.rollbackTransaction();
                                throw e;
                            }
                        }

                        studentDao.commitTransaction();
                    } catch (Exception e) {
                        log.error("Insert student failed, rollback transaction", e);
                        studentDao.rollbackTransaction();
                    }
                }

            } catch (Exception e) {
                log.error("Clear table student question mapping failed", e);
                studentQuestionMappingDao.rollbackTransaction();
            }
            
        } catch (Exception e) {
            log.error("Clear table student failed", e);
            studentDao.rollbackTransaction();
        }        
    }
    
    public void processCalculation() {
        calculateTotalScorePerQuestion();
        calculateMeanPerQuestion();
        calculateDeviationStandardPerQuestion();
        calculateStandardScoreQuestionsPerStudent();
        calculateEuclideanDistanceStudent();
        calculateHighestValues();
    }
    
    public void afterCalculation() {
        if (listener != null) listener.finished("Calculation finished");
        
    }
    
    private void calculateTotalScorePerQuestion() {
        if (listener != null) listener.calculating("Calculate total score per question ...");
        
        Query query = studentQuestionMappingDao.createNativeQuery(" SELECT A.question_number, SUM(A.score) FROM " 
                + StudentQuestionMapping.TABLE_NAME + " A "
                + " GROUP BY A.question_number ");
        
        mapTotalScorePerQuestion = new HashMap();
        List<Object[]> resultList = query.getResultList();
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = resultList.get(i);
            Object number = result[0];
            Object score = result[1];
            mapTotalScorePerQuestion.put(number, score);
        }
    }
    
    private void calculateMeanPerQuestion() {
        if (listener != null) listener.calculating("Calculate mean per question ...");
        
        Query query = studentQuestionMappingDao.createNativeQuery(" SELECT A.question_number, AVG(A.score) FROM " 
                + StudentQuestionMapping.TABLE_NAME + " A "
                + " GROUP BY A.question_number ");
        
        mapMeanPerQuestion = new HashMap();
        List<Object[]> resultList = query.getResultList();
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = resultList.get(i);
            Object number = result[0];
            Object score = result[1];
            mapMeanPerQuestion.put(number, score);
        }
    }
    
    private void calculateDeviationStandardPerQuestion() {
        if (listener != null) listener.calculating("Calculate deviation standard per question ...");
        
        Query query = studentQuestionMappingDao.createNativeQuery(" SELECT A.question_number, " 
                + " COUNT(DISTINCT(A.student_id)) AS num_student, "
                + " SUM(A.score) AS score_total, "
                + " AVG(A.score) AS score_mean, "
                + " SQRT(((SUM(A.score) - AVG(A.score)) * (SUM(A.score) - AVG(A.score))) / (COUNT(DISTINCT(A.student_id)) - 1)) AS deviation_standart "
                + " FROM t_student_question_mapping A"
                + " GROUP BY A.question_number ");
        
        mapDeviationStandardPerQuestion = new HashMap();
        List<Object[]> resultList = query.getResultList();
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = resultList.get(i);
            Object question = result[0];
            // Object studentCount = result[1];
            // Object scoreTotal = result[2];
            // Object scoreMean = result[3];
            Object deviation = result[4];
            
            mapDeviationStandardPerQuestion.put(question, deviation);
        }
    }
        
    private void calculateStandardScoreQuestionsPerStudent() {
        if (listener != null) listener.calculating("Calculate standard score question per student ...");
        
        studentQuestionMappingDao.beginTransaction();
        Query query = studentQuestionMappingDao.createNativeQuery("CALL p_calculate_standart_score_question_per_student()");
        List<Object[]> resultList = query.getResultList();
        studentQuestionMappingDao.flush();
        studentQuestionMappingDao.commitTransaction();
        
        listStandardScoreQuestionsPerStudent = new ArrayList();
        HashMap map = null;
        HashMap mapStandartScore = null;
        Object currentStudentId = null;
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = resultList.get(i);
            Object studentId = result[0];
            Object studentName = result[1];
            double question = Double.valueOf(result[2] + "");
            Object standartScore = result[3];
            
            if (currentStudentId == null) {
                map = new HashMap();
                map.put("student", new StudentModel(studentName + ""));
                
                mapStandartScore = new HashMap();
                mapStandartScore.put((int)question, standartScore);
                currentStudentId = studentId;
            } else {
                if (!studentId.equals(currentStudentId)) {
                    map.put("standardScore", mapStandartScore);
                    listStandardScoreQuestionsPerStudent.add(map);
                    currentStudentId = studentId;
                    
                    map = new HashMap();
                    map.put("student", new StudentModel(studentName + ""));
                    
                    mapStandartScore = new HashMap();
                    mapStandartScore.put((int)question, standartScore);
                } else {
                    mapStandartScore.put((int)question, standartScore);
                }
            }
            
            if (i == resultList.size() -1) {
                map.put("standardScore", mapStandartScore);
                listStandardScoreQuestionsPerStudent.add(map);
            }
        }
    }
 
    private void calculateEuclideanDistanceStudent() {
        if (listener != null) listener.calculating("Calculate euclidean distance student ...");
        
        studentQuestionMappingDao.beginTransaction();
        Query query = studentQuestionMappingDao.createNativeQuery("CALL p_calculate_euclidean_distance_student()");
        List<Object[]> resultList = query.getResultList();
        studentQuestionMappingDao.flush();
        studentQuestionMappingDao.commitTransaction();
        
        listEuclideanDistanceStudent = new ArrayList();
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = resultList.get(i);
            // Object studentId = result[0];
            Object studentName = result[1];
            Object euclideanDistance = result[2];
            
            HashMap map = new HashMap();
            map.put("student", new StudentModel(studentName + ""));
            map.put("euclideanDistance", euclideanDistance);
            listEuclideanDistanceStudent.add(map);
        }
        
    }
    
    private void calculateHighestValues() {
        if (listener != null) listener.calculating("Calculate highest values ...");
        
        Tools.sortListOfMap(listEuclideanDistanceStudent, "euclideanDistance", true);
        mapHighestValues = new HashMap();
        mapHighestValues.put("highestScore", Tools.getHighestValue(mapTotalScorePerQuestion));
        mapHighestValues.put("highestMean", Tools.getHighestValue(mapMeanPerQuestion));
        mapHighestValues.put("highestDeviation", Tools.getHighestValue(mapDeviationStandardPerQuestion));
        mapHighestValues.put("highestEuclideanDistance", listEuclideanDistanceStudent.get(0));
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

}
