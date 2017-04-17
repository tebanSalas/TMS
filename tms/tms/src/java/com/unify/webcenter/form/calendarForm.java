//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;
 
/**
* Class that represent the form for the table calendar
* @author Administrator
*/
public class calendarForm  extends ValidatorActionForm {

	 private String operation;
	 private String sortColumn;
	 private String sortOrder;
	 private String subject;
	 private int eventype;
	 private java.sql.Timestamp stamp;
	 //private char[] description;
         private String description;
	 private java.sql.Timestamp duration;
	 private String username;
	 private int member;
	 private int id; 
         protected String   guests;

                  private String searchProject;
         private String searchCode;
         private String searchTask;
	 public void setsubject(String val) { subject = val; } 
	 public void seteventype(int val) { eventype = val; } 
	 public void setstamp(java.sql.Timestamp val) { stamp = val; } 
	 /*public void setdescription(char[] val) { description = val; } 
         public void setdescription(String  val) { description = val.toCharArray(); } */
         public void setdescription(String  val) { description = val; } 
	 public void setduration(java.sql.Timestamp val) { duration = val; } 
	 public void setusername(String val) { username = val; } 
	 public void setmember(int val) { member = val; } 
	 public void setid(int val) { id = val; } 
	 public void setOperation(String val) { operation =  val; }
	 public void setsortColumn(String val) { sortColumn =  val; }
	 public void setsortOrder(String val) { sortOrder =  val; }
         public void setguests(String val) { guests = val; } 

	 public String getsubject() { return subject;}
	 public int geteventype() { return eventype;}
	 public java.sql.Timestamp getstamp() { return stamp;}
	 public String getdescription() { 
             if (description != null)
                return new String(description);
             else 
                 return "";
         }
	 public java.sql.Timestamp getduration() { return duration;}
	 public String getusername() { return username;}
	 public int getmember() { return member;}
	 public int getid() { return id;}
	 public String getOperation() { return operation; }
	 public String getsortColumn() { return sortColumn; }
	 public String getsortOrder() { return sortOrder; }
         public String getguests() { return guests; }

	 /**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	 public void reset(ActionMapping mapping, HttpServletRequest request) {
		subject = "";
		eventype = 0;
		stamp = null;
		
		duration = null;
		username = "";
		member = 0;
		id = 0;
		sortColumn = "";
		sortOrder = "DESC";
	}

	 public String toString() {
		 return " subject:" + subject + " eventype:" + eventype + " stamp:" + stamp + " description:" + description + " duration:" + duration + " username:" + username + " member:" + member + " id:" + id; 
	 }

    public String getSearchProject() {
        return searchProject;
    }

    public void setSearchProject(String searchProject) {
        this.searchProject = searchProject;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(String searchTask) {
        this.searchTask = searchTask;
    }
}