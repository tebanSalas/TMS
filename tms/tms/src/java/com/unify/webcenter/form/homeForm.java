/*
 * homeForm.java
 *
 * Created on January 27, 2003, 6:29 PM
 */

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

/** 
 * Representa el formulario asociado a la pagina de inicio
 *
 * @author  Gerardo Arroyo
 */
public class homeForm extends ValidatorActionForm {
      private String sortOrderProjects;
      private String sortColumnProjects;
  
      private String sortOrderTasks;
      private String sortColumnTasks;
      
      private String sortOrderSubProj;
      private String sortColumnSubProj;


      private String sortOrderTopics;
      private String sortColumnTopics;

      private String sortOrderReports;
      private String sortColumnReports;

      private String sortOrderSchedules;
      private String sortColumnSchedules;
      
      private String sortOrderMembers;
      private String sortColumnMembers;
    
      private int pageTasks;
      private int pageSubProj;
      private int pageTopics;
      private int pageProjects;
      private int pageReports;
      private int pageSchedules;
      private int pageMembers;
      private int typeProject;
       private int typeTask;
      
      private String source;
      
      private String operation;
    
    /** Creates a new instance of homeForm */
    public homeForm() {
    }
    
    public String getsortColumnProjects() { return sortColumnProjects; }
    public void setsortColumnProjects(String val) { sortColumnProjects = val; }
    public String getsortOrderProjects() { return sortOrderProjects; }
    public void setsortOrderProjects(String val) { sortOrderProjects = val; }
    
    public String getsortColumnTasks() { return sortColumnTasks; }
    public void setsortColumnTasks(String val) { sortColumnTasks = val; }
    public String getsortOrderTasks() { return sortOrderTasks; }
    public void setsortOrderTasks(String val) { sortOrderTasks = val; }

    public String getsortColumnTopics() { return sortColumnTopics; }
    public void setsortColumnTopics(String val) { sortColumnTopics = val; }
    public String getsortOrderTopics() { return sortOrderTopics; }
    public void setsortOrderTopics(String val) { sortOrderTopics = val; }

    public String getsortColumnReports() { return sortColumnReports; }
    public void setsortColumnReports(String val) { sortColumnReports = val; }
    public String getsortOrderReports() { return sortOrderReports; }
    public void setsortOrderReports(String val) { sortOrderReports = val; }
    
    public String getsortColumnSchedules() { return sortColumnSchedules; }
    public void setsortColumnSchedules(String val) { sortColumnSchedules = val; }
    public String getsortOrderSchedules() { return sortOrderSchedules; }
    public void setsortOrderSchedules(String val) { sortOrderSchedules = val; }

    public String getsortColumnMembers() { return sortColumnMembers; }
    public void setsortColumnMembers(String val) { sortColumnMembers = val; }
    public String getsortOrderMembers() { return sortOrderMembers; }
    public void setsortOrderMembers(String val) { sortOrderMembers = val; }

    
    public void setsource(String val) { source = val;}
    public String getsource() { return source; }

    public void setpageTasks(int val) { pageTasks = val;}
    public int getpageTasks() { return pageTasks; }

    public void setpageTopics(int val) { pageTopics = val;}
    public int getpageTopics() { return pageTopics; }

    public void setpageProjects(int val) { pageProjects = val;}
    public int getpageProjects() { return pageProjects; }
    
    public void setpageReports(int val) { pageReports = val;}
    public int getpageReports() { return pageReports; }    

    public void setpageSchedules(int val) { pageSchedules = val;}
    public int getpageSchedules() { return pageSchedules; }    
    
    public void setpageMembers(int val) { pageMembers = val;}
    public int getpageMembers() { return pageMembers; } 
    
    
    public void settypeProject(int val) { typeProject = val;}
    public int gettypeProject() { return typeProject; } 
    
    
    public void setoperation(String val) { operation = val;}
    public String getoperation() { return operation; }
    
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
     public void reset(ActionMapping mapping, HttpServletRequest request) {
            sortColumnProjects = "delayed";
            sortOrderProjects = "ASC";
            sortColumnTasks = "status";
            sortOrderTasks = "ASC";
            sortColumnTopics = "status";
            sortOrderTopics = "DESC";
            sortColumnReports = "name";
            sortOrderReports = "DESC";
            sortColumnSchedules = "day";
            sortOrderSchedules = "DESC"; 
            sortColumnMembers = "name";
            sortOrderMembers = "ASC";
            source = "";  
            operation = "";
            pageTasks = 1;
            pageTopics = 1;
            pageProjects = 1;
            pageReports = 1;
            pageSchedules = 1;
            pageMembers = 1;
            typeProject = 2;
            typeTask=1;
    }    
    
    public int getadminuser() {
        return 1;
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

    public int getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(int typeTask) {
        this.typeTask = typeTask;
    }
}
