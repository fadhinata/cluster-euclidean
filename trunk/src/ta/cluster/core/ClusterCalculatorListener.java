/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.core;

/**
 *
 * @author Matt
 */
public interface ClusterCalculatorListener {
    abstract void started(String message);
    abstract void calculating(String message);
    abstract void finished(String message);
}
