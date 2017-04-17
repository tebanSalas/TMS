/*
 * risksData.java
 *
 * Created on April 14, 2004
 */

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

/**
 * Clase que representa a la tabla Cost del TMS
 * @author  Gerardo Arroyo
 */
public class risksForm extends ValidatorActionForm {
    private String sortColumn;
    private String sortOrder;    
    private String operation;
    private int id;
    private String description;
    private int probability;
    private int impact;
    //private char[] todoaction;
    private String todoaction;
    private String planb;
    private int task;
    private int project;    
    private String taskdesc;
    private String fromPage;
    
    /** Creates a new instance of risksData */
    public risksForm() {
    }
    
    // Setters
        public void setid(int val) { id = val; }
	public void setdescription(String val) { description = val; }
	public void setprobability(int val) { probability = val; }
        public void setimpact(int val) { impact = val; }
	/*public void settodoaction(char[] val) { todoaction = val; }
        public void settodoaction(String val) { todoaction = val.toCharArray(); }*/
        public void settodoaction(String val) { todoaction = val; }
	public void setplanb(String val) { planb = val; }
        public void settask(int val) { task = val; }
        public void setproject(int val) { project = val; }
	public void setsortColumn(String val) { sortColumn =  val; }
        public void setsortOrder(String val) { sortOrder =  val; }
        public void setoperation(String val) { operation = val; }
        public void settaskdesc(String val) { taskdesc = val; }
        public void setfromPage(String val) { fromPage = val; }
    
    // Getters
        public int getid() { return id; }
        public String getdescription() { return description; }
	public int getprobability() { return probability; }
	public int getimpact() { return impact; }
        public String gettodoaction() { 
            if (todoaction != null)
                return new String(todoaction); 
            else
                return "";
        }
        public String getplanb() { return planb; }
	public int gettask() { return task; }
        public int getproject() { return project; }
	public String getsortColumn() { return sortColumn; }
        public String getsortOrder() { return sortOrder; }
        public String getoperation() { return operation; }
        public String gettaskdesc() { return taskdesc; }
        public String getfromPage() { return fromPage; } 
        
        public String toString() { 
		return " id:" + id + " description:" + description + " probability:" + probability + " impact:" + impact + " todoaction:" + todoaction + " planb:" + planb + " task:" + task; 
	}
    
        /**
        * Reset all properties to their default values.
        *
        * @param mapping The mapping used to select this instance
        * @param request The servlet request we are processing
        */
        public void reset(ActionMapping mapping, HttpServletRequest request) {
                id = 0;
  	        description = "";
		probability = 0;
		impact = 0;

		planb = "";
                task = 0;
                project = 0;
                taskdesc = "";
                fromPage = "viewProject";
                sortColumn = "description";
                sortOrder = "DESC";
        }    
}
