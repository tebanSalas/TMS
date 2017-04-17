/*
 * performanceDetail.java
 *
 * Created on June 18, 2003, 3:20 PM
 */

package com.unify.webcenter.data;

import java.io.*;
import java.util.*;
import java.math.BigDecimal;

/**
 * Clase que representa el desgloce de rendimiento de un usuario.
 * @author  Gerardo Arroyo
 */
public class performanceDetail implements Serializable {
    private ArrayList listTasks;
    private BigDecimal total_estimated_hours;
    private BigDecimal total_real_hours;
    private BigDecimal total_performance;
    private BigDecimal total_deficit;
    private BigDecimal total_estimated_hours_transit;
    private BigDecimal total_real_hours_transit;
    private BigDecimal total_performance_transit;
    private BigDecimal total_deficit_transit;    

    private String name;
    
    /** Creates a new instance of performanceDetail */
    public performanceDetail(ArrayList tasks, BigDecimal estimated,
        BigDecimal real, BigDecimal perf, BigDecimal deficit, String name,
        BigDecimal estimated_transit, BigDecimal real_transit, BigDecimal perf_transit, BigDecimal deficit_transit) {
            listTasks = tasks;
            total_estimated_hours = estimated;
            total_real_hours = real; 
            total_performance = perf;
            total_deficit = deficit;
            
            total_estimated_hours_transit = estimated_transit;
            total_real_hours_transit = real_transit; 
            total_performance_transit = perf_transit;
            total_deficit_transit = deficit_transit;            
            this.name = name;
    }
    
  public performanceDetail(ArrayList tasks, BigDecimal estimated,
        BigDecimal real, BigDecimal perf, BigDecimal deficit, String name) {
            listTasks = tasks;
            total_estimated_hours = estimated;
            total_real_hours = real; 
            total_performance = perf;
            total_deficit = deficit;
            
            total_estimated_hours_transit = new BigDecimal(0);
            total_real_hours_transit = new BigDecimal(0); 
            total_performance_transit = new BigDecimal(0);
            total_deficit_transit = new BigDecimal(0);            
            this.name = name;
    }    
    
    public performanceDetail() {
        listTasks = new ArrayList();
    }
    
    public ArrayList getlistTasks() { return listTasks; }
    public BigDecimal gettotal_estimated_hours() { return total_estimated_hours; }
    public BigDecimal gettotal_real_hours() { 
        if (total_real_hours == null) 
            return new BigDecimal(0);
        else
            return total_real_hours; 
    }
    public BigDecimal gettotal_performance() { return total_performance; }
    public BigDecimal gettotal_deficit() { return total_deficit; }
    
    public void addToTaskList(tasksData val) {
        listTasks.add(val);
        
        total_real_hours = total_real_hours.add(new BigDecimal(val.getCalendarTime()));
        
        if (val.getestimated_time() != null)
            total_estimated_hours = total_estimated_hours.add(val.getestimated_time());
    }
    public String getname() { return name; }

    
   public BigDecimal gettotal_estimated_hours_transit() { return total_estimated_hours_transit; }
    public BigDecimal gettotal_real_hours_transit() { 
        if (total_real_hours_transit == null) 
            return new BigDecimal(0);
        else
            return total_real_hours_transit; 
    }
    public BigDecimal gettotal_performance_transit() { return total_performance_transit; }
    public BigDecimal gettotal_deficit_transit() { return total_deficit_transit; }

   
    public String formatRealHour(BigDecimal realHour) {
        return realHour.toString().replace('.', ':');     
    }
}
