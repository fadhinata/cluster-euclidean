/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Matt
 */
public interface ClusterCalculator {
    public void process();
    public void setListener(ClusterCalculatorListener listener);
    public List getListEuclideanDistanceStudent();
    public List getListStandardScoreQuestionsPerStudent();
    public HashMap getMapDeviationStandardPerQuestion();
    public HashMap getMapMeanPerQuestion();
    public HashMap getMapTotalScorePerQuestion();
    public HashMap getMapHighestValues();
}
