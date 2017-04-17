/*
 * statisticsData.java
 *
 * Created on 29 de abril de 2004, 14:31
 */

package com.unify.webcenter.data;

/**
 *
 * @author  Administrador
 */
public class statisticsData extends mainData {	 
    
    protected int totaltasks = 0;
    protected int totalontimetasks = 0;
    protected int totaldelayedtasks = 0;
    protected int totalassignedtasks = 0;
    protected int totalnotassignedtasks = 0;
    protected int totalsuspendedtasks = 0;
    protected int totalrejectedtasks = 0;
    protected int totalendedtasks = 0;
   
    public void settotaltasks(int val) { totaltasks = val; }
    public void settotalassignedtasks(int val) { totalassignedtasks = val; }
    public void settotalnotassignedtasks(int val) { totalnotassignedtasks = val;  }
    public void settotalontimetasks(int val) { totalontimetasks = val; }
    public void settotaldelayedtasks(int val) { totaldelayedtasks = val; }
    public void settotalrejectedtasks(int val) { totalrejectedtasks = val; }
    public void settotalsuspendedtasks(int val) { totalsuspendedtasks = val; }
    public void settotalendedtasks (int val) { totalendedtasks = val; }
    
    public int gettotaltasks() { return totaltasks; }
    public int gettotalassignedtasks() { return totalassignedtasks;  }
    public int gettotalnotassignedtasks() { return totalnotassignedtasks; }
    public int gettotalontimetasks() { return totalontimetasks;  }
    public int gettotaldelayedtasks() { return totaldelayedtasks; }
    public int gettotalsuspendedtasks() { return totalsuspendedtasks; }
    public int gettotalrejectedtasks() { return totalrejectedtasks; }
    public int gettotalendedtasks() { return totalendedtasks; }
    
    public String toString() {
	 return" totaltasks:" + totaltasks + " totaltasks:" + totaltasks + " totalnotassignedtasks:" + totalnotassignedtasks + " totalontimetasks:" + totalontimetasks + " totaldelayedtasks:" + totaldelayedtasks + " totalrejectedtasks:" + totalrejectedtasks + " totalsuspendedtasks:" + totalsuspendedtasks + " totalendedtasks:" + totalendedtasks; 
    }    
}
