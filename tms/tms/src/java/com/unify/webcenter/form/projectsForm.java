//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorActionForm;

/**
* Class that represent the form for the table projects
* @author Administrator
*/
public class projectsForm  extends ValidatorActionForm {

	 private String operation;
	 private String sortColumn;
	 private String sortOrder;
	 private String name;
	 private int organization;
	 private int id;
	 private int owner;
	 private String published_endtask;
	 private String upload_max;
	 private java.sql.Timestamp created;
	 private int status;
	 private String published_assigned;
	 private String published;
	 private int priority;
	 private java.sql.Timestamp modified;
	 //private char[] description;
         private String description;
         private java.sql.Timestamp start_date;
         private java.sql.Timestamp end_date;
         private int typeTask;
         private int typeTopic;
         private int typeProject;
         private int project_id;
         private String sec_projects;
         private int project;
             private String send_email;
         private String sortOrderTasks;
         private String sortColumnTasks;
           private String sortOrderSubProj;
         private String sortColumnSubProj;
         private String sortOrderTopics;
         private String sortColumnTopics;        
         private String sortOrderTeams;
         private String sortColumnTeams;        
         private String sortOrderFiles;
         private String sortColumnFiles;        
         private String sortOrderCosts;
         private String sortColumnCosts;   
         private String sortOrderRisks;    
         private String sortColumnRisks;
         private FormFile theFile;  
         
         private int pageTasks;
         private int pageSubProj;
         private int pageTopics;
         private int pageTeams;
         private int pageFiles;
         private int pageCosts;
         private int pageRisks;
         
         private String source;
         private String autom_notification;
         
         
        private int copy_files_proj;
        private java.sql.Timestamp start_date_task; 
        private java.sql.Timestamp due_date_task;
        private int assigned_to_task;
        private int estimated_time_task;
        private int copy_files_task;
        private int fare_task;
    
         
	 public void setname(String val) { name = cleanInvalidTags(val); } 
	 public void setorganization(int val) { organization = val; } 
	 public void setid(int val) { id = val; } 
	 public void setowner(int val) { owner = val; } 
	 public void setpublished_endtask(String val) { published_endtask = val; } 
	 public void setupload_max(String val) { upload_max = val; } 
	 public void setcreated(java.sql.Timestamp val) { created = val; } 
	 public void setstatus(int val) { status = val; } 
	 public void setpublished_assigned(String val) { published_assigned = val; } 
	 public void setpublished(String val) { published = val; } 
	 public void setpriority(int val) { priority = val; } 
	 public void setmodified(java.sql.Timestamp val) { modified = val; } 
         /*public void setdescription(String val) { description = cleanInvalidTags(val).toCharArray(); } 
	 public void setdescription(char[] val) { 
             if (val != null)
                description = cleanInvalidTags(new String(val)).toCharArray(); 
             else
                 description = " ".toCharArray();
         } */
         public void setdescription(String val) { description = cleanInvalidTags(val); } 
         public void setstart_date (java.sql.Timestamp val) { start_date = val; } 
         public void setend_date (java.sql.Timestamp val) { end_date = val; } 
         
         public void setOperation(String val) { operation =  val; }
	 public void setsortColumn(String val) { sortColumn =  val; }
	 public void setsortOrder(String val) { sortOrder =  val; }

	 public String getname() { return name;}
	 public int getorganization() { return organization;}
	 public int getid() { return id;}
	 public int getowner() { return owner;}
	 public String getpublished_endtask() { return published_endtask;}
	 public String getupload_max() { return upload_max;}
	 public java.sql.Timestamp getcreated() { return created;}
	 public int getstatus() { return status;}
	 public String getpublished_assigned() { return published_assigned;}
	 public String getpublished() { return published;}
	 public int getpriority() { return priority;}
	 public java.sql.Timestamp getmodified() { return modified;}
	 public String getdescription() { 
             if (description != null)
                return new String(description);
             else
                 return "";
         }
	 public java.sql.Timestamp getstart_date() { return start_date; } 
         public java.sql.Timestamp getend_date() { return end_date; } 
         public String getOperation() { return operation; }
	 public String getsortColumn() { return sortColumn; }
	 public String getsortOrder() { return sortOrder; }         
         
         public String getsortColumnTasks() { return sortColumnTasks; }
         public void setsortColumnTasks(String val) { sortColumnTasks = val; }
         public String getsortOrderTasks() { return sortOrderTasks; }
         public void setsortOrderTasks(String val) { sortOrderTasks = val; }

         public String getsortColumnTopics() { return sortColumnTopics; }
         public void setsortColumnTopics(String val) { sortColumnTopics = val; }
         public String getsortOrderTopics() { return sortOrderTopics; }
         public void setsortOrderTopics(String val) { sortOrderTopics = val; }
         
         public String getsortColumnTeams() { return sortColumnTeams; }
         public void setsortColumnTeams(String val) { sortColumnTeams = val; }
         public String getsortOrderTeams() { return sortOrderTeams; }
         public void setsortOrderTeams(String val) { sortOrderTeams = val; }
         
         public String getsortColumnFiles() { return sortColumnFiles; }
         public void setsortColumnFiles(String val) { sortColumnFiles = val; }
         public String getsortOrderFiles() { return sortOrderFiles; }
         public void setsortOrderFiles(String val) { sortOrderFiles = val; }

         public String getsortColumnCosts() { return sortColumnCosts; }
         public void setsortColumnCosts(String val) { sortColumnCosts = val; }
         public String getsortOrderCosts() { return sortOrderCosts; }
         public void setsortOrderCosts(String val) { sortOrderCosts = val; }
         
         public String getsortColumnRisks() { return sortColumnRisks; }
         public void setsortColumnRisks(String val) { sortColumnRisks = val; }
         public String getsortOrderRisks() { return sortOrderRisks; }
         public void setsortOrderRisks(String val) { sortOrderRisks = val; }
         
         public void setsource(String val) { source = val;}
         public String getsource() { return source; }
         
         public void setpageTasks(int val) { pageTasks = val; }
         public int  getpageTasks() { return pageTasks; }

         public void setpageTopics(int val) { pageTopics = val; }
         public int  getpageTopics() { return pageTopics; }

         public void setpageTeams(int val) { pageTeams = val; }
         public int  getpageTeams() { return pageTeams; }
         
         public void setpageFiles(int val) { pageFiles = val; }
         public int  getpageFiles() { return pageFiles; }

         public void setpageCosts(int val) { pageCosts = val; }
         public int  getpageCosts() { return pageCosts; }
         
         public void setpageRisks(int val) { pageRisks = val; }
         public int  getpageRisks() { return pageRisks; }
         
         public void settypeTask(int val) { typeTask = val; }
         public int  gettypeTask() { return typeTask; }
         
         public void settypeTopic(int val) { typeTopic = val; }
         public int  gettypeTopic() { return typeTopic; }         
	 /**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	 public void reset(ActionMapping mapping, HttpServletRequest request) {
		name = "";
		organization = 0;
		id = 0;
		owner = 0;
		published_endtask = "0";
		upload_max = "0";
		created = null;
		status = 2; // Default NOT STARTED
		published_assigned = "0";
		published = "0";
		priority = 0;
		modified = null;
		//description = "N/A".toCharArray();
                description = "N/A";
                start_date = null;
                end_date = null;
		sortColumn = "status";
		sortOrder = "ASC";
                source = ""; 
                operation = "";
                sortColumnTasks = "status";
                sortOrderTasks = "ASC";
                
                sortColumnTopics = "status";
                sortOrderTopics = "DESC";
                
                sortOrderTeams = "DESC";
                sortColumnTeams = "members";   
                
                sortOrderFiles = "DESC";   
                sortColumnFiles = "name";                                    

                sortOrderCosts = "DESC";   
                sortColumnCosts = "units";   
                
                sortOrderRisks = "DESC";   
                sortColumnRisks = "description";       
                
                pageTasks = 1;
                pageTopics = 1;
                pageTeams = 1;
                pageFiles = 1;
                pageCosts = 1;
                pageRisks = 1;
                typeTask = 1;
                typeTopic = 1;
                typeProject = 2;
                
	}

	 public String toString() {
		 return " name:" + name + " organization:" + organization + " id:" + id + " owner:" + owner + " published_endtask:" + published_endtask + " upload_max:" + upload_max + " created:" + created + " status:" + status + " published_assigned:" + published_assigned + " published:" + published + " priority:" + priority + " modified:" + modified + " description:" + description + " start_date:" + start_date + " end_date:" + end_date; 
	 }
         
         // Regresa la descripcion en formato HTML
         private String cleanInvalidTags(String source) { 
             if (source != null) {             
                 StringBuffer temp = new StringBuffer(source);

                // Los tabs tambien       
                int i = temp.indexOf("\\t");
                 while (i > 0) {
                     temp.replace(i,i+2, "   ");
                     i = temp.indexOf("\\t");
                 }             

                // Los tabs tambien       
                i = temp.indexOf("\t");
                 while (i > 0) {
                     temp.replace(i,i+1, "   ");
                     i = temp.indexOf("\t");
                 }             

                 return temp.toString();
             } else
                 return null;
         }

    public String getSortOrderSubProj() {
        return sortOrderSubProj;
    }

    public void setSortOrderSubProj(String sortOrderSubProj) {
        this.sortOrderSubProj = sortOrderSubProj;
    }

    public String getSortColumnSubProj() {
        return sortColumnSubProj;
    }

    public void setSortColumnSubProj(String sortColumnSubProj) {
        this.sortColumnSubProj = sortColumnSubProj;
    }

    public int getPageSubProj() {
        return pageSubProj;
    }

    public void setPageSubProj(int pageSubProj) {
        this.pageSubProj = pageSubProj;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getTypeProject() {
        return typeProject;
    }

    public void setTypeProject(int typeProject) {
        this.typeProject = typeProject;
    }
    
        public String getSec_projects() {
        return sec_projects;
    }

    public void setSec_projects(String sec_projects) {
        this.sec_projects = sec_projects;
    }

    public String getSend_email() {
        return send_email;
    }

    public void setSend_email(String send_email) {
        this.send_email = send_email;
    }

    public String getautom_notification() {
        return autom_notification;
    }

    public void setautom_notification(String autom_notification) {
        this.autom_notification = autom_notification;
    }

    /**
     * @return the copy_files_proj
     */
    public int getCopy_files_proj() {
        return copy_files_proj;
    }

    /**
     * @param copy_files_proj the copy_files_proj to set
     */
    public void setCopy_files_proj(int copy_files_proj) {
        this.copy_files_proj = copy_files_proj;
    }

    /**
     * @return the start_date_task
     */
    public java.sql.Timestamp getStart_date_task() {
        return start_date_task;
    }

    /**
     * @param start_date_task the start_date_task to set
     */
    public void setStart_date_task(java.sql.Timestamp start_date_task) {
        this.start_date_task = start_date_task;
    }

    /**
     * @return the due_date_task
     */
    public java.sql.Timestamp getDue_date_task() {
        return due_date_task;
    }

    /**
     * @param due_date_task the due_date_task to set
     */
    public void setDue_date_task(java.sql.Timestamp due_date_task) {
        this.due_date_task = due_date_task;
    }

    /**
     * @return the assigned_to_task
     */
    public int getAssigned_to_task() {
        return assigned_to_task;
    }

    /**
     * @param assigned_to_task the assigned_to_task to set
     */
    public void setAssigned_to_task(int assigned_to_task) {
        this.assigned_to_task = assigned_to_task;
    }

    /**
     * @return the estimated_time_task
     */
    public int getEstimated_time_task() {
        return estimated_time_task;
    }

    /**
     * @param estimated_time_task the estimated_time_task to set
     */
    public void setEstimated_time_task(int estimated_time_task) {
        this.estimated_time_task = estimated_time_task;
    }

    /**
     * @return the copy_files_task
     */
    public int getCopy_files_task() {
        return copy_files_task;
    }

    /**
     * @param copy_files_task the copy_files_task to set
     */
    public void setCopy_files_task(int copy_files_task) {
        this.copy_files_task = copy_files_task;
    }

    /**
     * @return the fare_task
     */
    public int getFare_task() {
        return fare_task;
    }

    /**
     * @param fare_task the fare_task to set
     */
    public void setFare_task(int fare_task) {
        this.fare_task = fare_task;
    }

    /**
     * @return the theFile
     */
    public FormFile getTheFile() {
        return theFile;
    }

    /**
     * @param theFile the theFile to set
     */
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }
}