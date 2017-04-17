/*
 * personalCalendarForm.java
 *
 * Created on March 2, 2003, 4:53 PM
 */

package com.unify.webcenter.form;


import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

import java.util.Calendar ;

/**
 * Forma que representa los datos asociadas a una agenda personal
 *
 * @author  Gerardo Arroyo Arce
 */
public class personalCalendarForm extends ValidatorActionForm {
   private String operation;
   private long   theDate;
   private int    userId;
   private int      projectId;
   private String projectName;
   private String projectowner;
   private java.sql.Timestamp start_date;
   private java.sql.Timestamp end_date;
   private String taskdesc;
   private String owneremail; 
   
    /** Creates a new instance of personalCalendarForm */
    public personalCalendarForm() {
    }
    
    public void setOperation(String val) {operation = val; }
    public void setTheDate(long val) { theDate = val; }
    public void setuserId(int val) { userId = val; }
    public void setProjectId(int val) { projectId = val; }
    public void setprojectName(String val) { projectName = val; }
    public void setprojectowner(String val) { projectowner = val; }
    public void setStart_date(java.sql.Timestamp val) { start_date = val; }
    public void setEnd_date(java.sql.Timestamp val) { end_date = val; }
    public void settaskdesc(String val) { taskdesc = val; }
    public void setowneremail(String val) { owneremail = val; }
    
    public String getOperation() { return operation; }
    public long getTheDate() { return theDate; }
    public int  getuserId() { return userId; }
    public int  getProjecId() { return projectId; }
    public String getprojectName() { return projectName; }
    public String getprojectowner() { return projectowner; }
    public java.sql.Timestamp  getStart_date() { return start_date; }
    public java.sql.Timestamp  getEnd_date() { return end_date; }
    public String gettaskdesc() { return taskdesc; }
    public String getowneremail() { return owneremail; }
    
     /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
     public void reset(ActionMapping mapping, HttpServletRequest request) {
            operation = "goTo";
            theDate = Calendar.getInstance().getTimeInMillis();
    }    
    
}
