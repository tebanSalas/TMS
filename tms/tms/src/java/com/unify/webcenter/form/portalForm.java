/*
 * portalForm.java
 *
 * Created on February 28, 2003, 9:23 AM
 */

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;


/**
 * Clase que abstrae el formulario del portal del cliente
 * @author  Gerardo Arroyo Arce
 */
public class portalForm extends ValidatorActionForm {
     private String operation;
     private String sortColumn;
     private String sortOrder;    
     private int    project;
     private int    id;
     private int type_task;
     private int type_topic;
     private String task;
    
    /** Creates a new instance of portalForm */
    public portalForm() {
        type_task = 3;
        type_topic = 2;
        task = "%";
    }

     public void setOperation(String val) { operation =  val; }
     public void setsortColumn(String val) { sortColumn =  val; }
     public void setsortOrder(String val) { sortOrder =  val; }
     public void setproject(int val) { project =  val; }
     public void setid(int val) { id =  val; }

     public String getOperation() { return operation; }
     public String getsortColumn() { return sortColumn; }
     public String getsortOrder() { return sortOrder; }
     public int    getproject() { return project; }
     public int    getid() { return id; }

     /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
     public void reset(ActionMapping mapping, HttpServletRequest request) {
            sortColumn = "";
            sortOrder = "DESC";
    }

    public int getType_task() {
        return type_task;
    }

    public void setType_task(int type_task) {
        this.type_task = type_task;
    }

    public int getType_topic() {
        return type_topic;
    }

    public void setType_topic(int type_topic) {
        this.type_topic = type_topic;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
     
}
