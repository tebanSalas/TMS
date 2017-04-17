//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

import java.util.*;
/**
* Class that represent the form for the table reports
* @author Administrator
*/
public class reportsForm  extends ValidatorActionForm {

	 private String operation;
	 private String sortColumn;
	 private String sortOrder;
	 private String name;
	 private java.sql.Timestamp end_date;
         private java.sql.Timestamp start_date;                  
         
	 private int id;
	 private int owner;
         
	 private java.sql.Timestamp created;
	 private String[] projects;
	 private String[] members;
         private String[] organizations;
	 private String[] status;
	 private String[] priorities;
         private String[] typetasks;
         
         private String spreadFix;
         private String query;

         private String tareaCobrable;
         private String costoCobrable;

         private int pages;
         private String lastPage; 
         private int publish;
                 
	 public void setname(String val) { name = val; } 
	 public void setend_date(java.sql.Timestamp val) { end_date = val; } 
	 public void setid(int val) { id = val; } 
	 public void setowner(int val) { owner = val; } 
	 public void setcreated(java.sql.Timestamp val) { created = val; } 
	 public void setprojects(String[] val) { projects = val; } 
	 public void setmembers(String[] val) { members = val; } 
	 public void setstatus(String[] val) { status = val; } 
	 public void setstart_date(java.sql.Timestamp val) { start_date = val; } 
	 public void setpriorities(String[] val) { priorities = val; } 
         public void settypetasks(String[] val) { typetasks = val; }          
         public void setorganizations(String[] val) { organizations = val; } 
	 public void setOperation(String val) { operation =  val; }
	 public void setsortColumn(String val) { sortColumn =  val; }
	 public void setsortOrder(String val) { sortOrder =  val; }
         public void setspreadfix(String val) { spreadFix =  val; }
         

	 public String getname() { return name;}
	 public java.sql.Timestamp getend_date() { return end_date;}
	 public int getid() { return id;}
	 public int getowner() { return owner;}
	 public java.sql.Timestamp getcreated() { return created;}
	 public String[] getprojects() { return projects;}
	 public String[] getmembers() { return members;}
	 public String[] getstatus() { return status;}
	 public java.sql.Timestamp getstart_date() { return start_date;}
	 public String[] getpriorities() { return priorities;}
         public String[] gettypetasks() { return typetasks;}
         public String[] getorganizations() { return organizations;}
	 public String getOperation() { return operation; }
	 public String getsortColumn() { return sortColumn; }
	 public String getsortOrder() { return sortOrder; }
         public String getspreadfix() { return spreadFix; }
         

	 /**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	 public void reset(ActionMapping mapping, HttpServletRequest request) {
		name = "";
		end_date = null;
		id = 0;
		owner = 0;
		created = null;
//		projects = "";
//		members = new ArrayList();
//		status = "";
		start_date = null;
//		priorities = "";
		sortColumn = "id";
		sortOrder = "DESC";
                spreadFix = " ";
                pages = 1;
	}

	 public String toString() {
		 return " name:" + name + " end_date:" + end_date + " id:" + id + " owner:" + owner + " created:" + created + " projects:" + projects + " members:" + members + " status:" + status + " start_date:" + start_date + " priorities:" + priorities; 
	 }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the tareaCobrable
     */
    public String getTareaCobrable() {
        return tareaCobrable;
    }

    /**
     * @param tareaCobrable the tareaCobrable to set
     */
    public void setTareaCobrable(String tareaCobrable) {
        this.tareaCobrable = tareaCobrable;
    }

    /**
     * @return the costoCobrable
     */
    public String getCostoCobrable() {
        return costoCobrable;
    }

    /**
     * @param costoCobrable the costoCobrable to set
     */
    public void setCostoCobrable(String costoCobrable) {
        this.costoCobrable = costoCobrable;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pageFiles) {
        this.pages = pageFiles;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @return the publish
     */
    public int getPublish() {
        return publish;
    }

    /**
     * @param publish the publish to set
     */
    public void setPublish(int publish) {
        this.publish = publish;
    }
}