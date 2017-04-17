/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author MARCELA QUINTERO
 */
public class reportTasksData implements Serializable {

    private ArrayList listScheduleTasks;
    private String total_real_hours;



    /** Creates a new instance of performanceDetail */
    public reportTasksData(ArrayList scheduleTasks,
            String real_h) {
        listScheduleTasks = scheduleTasks;
        total_real_hours = real_h;
    }

    public reportTasksData() {
        listScheduleTasks = new ArrayList();
    }

    public ArrayList getlistScheduleTasks() {
        return listScheduleTasks;
    }

    public String gettotal_real_hours() {
        if (total_real_hours == null) {
            return "0";
        } else {
            return total_real_hours;
        }
    }

    public void setTotal_real_hours(String total_real_hours) {
        this.total_real_hours = total_real_hours;
    }

    public void addToTaskList(schedulesData val) {
        listScheduleTasks.add(val);
    }


    public String formatRealHour(BigDecimal realHour) {
        return realHour.toString().replace('.', ':');
    }

   
}
