/*
 * costsData.java
 *
 * Created on April 2, 2003, 6:06 PM
 */

package com.unify.webcenter.data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.*;


/**
 * Clase que representa a la tabla Cost del TMS
 * @author  Gerardo Arroyo
 */
public class costsData extends mainData {
    
    protected int id;
    protected int project;
    //protected char[] description;
    protected String description;
    protected int units;
    protected int tasks;
    protected java.math.BigDecimal standard_cost;
    protected java.math.BigDecimal real_cost;
    private int id_account;
    private String chargeable;
    private java.sql.Timestamp additional_costs_date;
    
    
    /** Creates a new instance of costsData */
    public costsData() {
    }

    public costsData(costsData val) {
        this.id = 0;
        this.project = val.getProject();
        this.description = val.getDescription();
        this.units = val.getUnits();
        this.tasks = val.getTasks();        
        this.standard_cost = val.getStandard_Cost();
        this.real_cost = val.getReal_Cost();
    }
    
    
    // Setters
    public void setId(int val) { id = val; }
    public void setProject(int val) { project = val; }
    /*public void setDescription(String val) { 
        if (val != null)
            description = val.toCharArray(); 
    }
    public void setDescription(char[] val) { description = val; }*/
    public void setDescription(String val) { description = val; }
    public void setUnits(int val) { units = val; }
    public void setTasks(int val) { tasks = val; }
    public void setStandard_Cost(java.math.BigDecimal val) { standard_cost = val; }
    public void setReal_Cost(java.math.BigDecimal val) { real_cost = val; }

    
    // Getters
    public int getId() { return id; }
    public int getProject() { return project; }
   // public char[] getDescription() { return description; }
     public String getDescription() { return description; }
    public String getFormatedDescription() { return new String(description); }
    public int getUnits() { return units; }
    public int getTasks() { return tasks; }
    public java.math.BigDecimal getStandard_Cost() { return standard_cost; }
    public java.math.BigDecimal getReal_Cost() { return real_cost; }
    
    public String getFormatedTotalStandard_Cost() { 
           DecimalFormat n = new DecimalFormat("###,###,###,##0.00");
            double money = (standard_cost.multiply(new java.math.BigDecimal(units)))
                    .doubleValue();
            
            return n.format(money);                                     
    }
    
    public String getFormatedTotalReal_Cost() { 
           DecimalFormat n = new DecimalFormat("###,###,###,##0.00");
            double money = (real_cost.multiply(new java.math.BigDecimal(units)))
                    .doubleValue();
            
            return n.format(money);                                     
    }
    
    public String toString() { return (new String(description)).toString(); }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
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
