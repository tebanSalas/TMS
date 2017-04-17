/*
 * performanceForm.java
 *
 * Created on February 24, 2003, 12:47 PM
 */

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

import java.util.*;

/**
 * Clase que se encarga de abstraer el formulario empleado en la generacion de los
 * reportes de performance.
 *
 * @author  Gerardo Arroyo Arce
 */
public class performanceForm extends ValidatorActionForm {
     private String operation;
     private String sortColumn;
     private String sortOrder;
     private int project;
     private int member;
     private String end_date;
     private String start_date;     
     private String type;

     public void setOperation(String val) { operation =  val; }
     public void setsortColumn(String val) { sortColumn =  val; }
     public void setsortOrder(String val) { sortOrder =  val; }     
     public void setend_date(String val) { end_date = val; } 
     public void setstart_date(String val) { start_date = val; }
     public void setproject(int val) { project = val;}
     public void setmember(int val) { member = val; }
     public void settype(String val) { type = val; }
     
     public String getOperation() { return operation; }
     public String getsortColumn() { return sortColumn; }
     public String getsortOrder() { return sortOrder; }
     public String getstart_date() { return start_date;}
     public String getend_date() { return end_date;}
     public int getproject() { return project;}
     public int getmember() { return member; }     
     public String gettype() { return type; }
     
    /** Creates a new instance of performanceForm */
    public performanceForm() {
    }
    
     /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
     public void reset(ActionMapping mapping, HttpServletRequest request) {
            end_date = null;
            start_date = null;
            sortColumn = "name";
            sortOrder = "DESC";
            operation = "execute";
    }
}
