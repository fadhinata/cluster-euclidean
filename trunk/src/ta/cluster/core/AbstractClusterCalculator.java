/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

/**
 *
 * @author Matt
 */
public abstract class AbstractClusterCalculator implements ClusterCalculator {

    public abstract void beforeCalculation();
    public abstract void processCalculation();
    public abstract void afterCalculation();
    
    public void process() {
        beforeCalculation();
        processCalculation();
        afterCalculation();
    }
    
}
