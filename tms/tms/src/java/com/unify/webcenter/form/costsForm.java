/*
 * costsData.java
 *
 * Created on April 2, 2003, 6:06 PM
 */

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

/**
 * Clase que representa a la tabla Cost del TMS
 * @author  Gerardo Arroyo
 */
public class costsForm extends ValidatorActionForm {
    private String sortColumn;
    private String sortOrder;    
    private String operation;
    private int id;
    private int project;
    //private char[] description;
    private String description;
    private int units;
    private int tasks;
    private java.math.BigDecimal standard_cost;
    private java.math.BigDecimal real_cost;
    private String chargeable;
    private java.sql.Timestamp additional_costs_date;
    /** Creates a new instance of costsData */
    public costsForm() {
    }
    
    // Setters
    public void setId(int val) { id = val; }
    public void setProject(int val) { project = val; }
    /*public void setDescription(char[] val) { description = val; }
    public void setDescription(String val) { description = val.toCharArray(); }*/
    public void setDescription(String val) { description = val; }
    public void setUnits(int val) { units = val; }
    public void setTasks(int val) { tasks = val; }
    public void setStandard_Cost(java.math.BigDecimal val) { standard_cost = val; }
    public void setReal_Cost(java.math.BigDecimal val) { real_cost = val; }
    public void setsortColumn(String val) { sortColumn =  val; }
    public void setsortOrder(String val) { sortOrder =  val; }
    public void setOperation(String val) { operation = val; }
    
    // Getters
    public int getId() { return id; }
    public int getProject() { return project; }
    public String getDescription() { 
        if  (description != null)
            return new String(description);
        else
            return "";
    }
    public int getUnits() { return units; }
    public int getTasks() { return tasks; }
    public java.math.BigDecimal getStandard_Cost() { return standard_cost; }
    public java.math.BigDecimal getReal_Cost() { return real_cost; }
    public String getsortColumn() { return sortColumn; }
    public String getsortOrder() { return sortOrder; }
    public String getOperation() { return operation; }
    
    
    public String toString() { return (new String(description)).toString(); }
    
     /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
     public void reset(ActionMapping mapping, HttpServletRequest request) {
            id = 0;
            project = 0;
//            description = "";
            units = 1;
            tasks = 0;
            standard_cost = new java.math.BigDecimal(0);
            real_cost = new java.math.BigDecimal(0);
            sortColumn = "";
            sortOrder = "DESC";
    }

    public String getChargeable() {
        return chargeable;
    }

    public void setChargeable(String chargeable) {
        this.chargeable = chargeable;
    }


    public java.sql.Timestamp getAdditional_costs_date() {
        return additional_costs_date;
    }

    public void setAdditional_costs_date(java.sql.Timestamp additional_costs_date) {
        this.additional_costs_date = additional_costs_date;
    }

}
