/*
 * Calendar.java
 *
 * Created on March 2, 2003, 3:54 PM
 */

package com.unify.webcenter.tools;

import java.util.*;
/**
 * Clase que representa a un WebCalendar para el uso de la agenda de los empleados
 * @author  Gerardo Arroyo Arce
 */
public class WebCalendar implements java.io.Serializable { 
    private ArrayList theDays;  // Lista de dias
    private Calendar  currentDate;    
    private String    locate = "es";
    
    /** Creates a new instance of Calendar
     * @param thisMonth
     */
    public WebCalendar(Calendar thisMonth) {
        // Se guarda la fecha dada
        this.currentDate = (Calendar)thisMonth.clone();
        
        // set the calendar to the first day of the month
        thisMonth.set(Calendar.DAY_OF_MONTH,1);         


        // Se define como hora default las 12:00:00
        thisMonth.set(Calendar.HOUR, 1);
        thisMonth.set(Calendar.MINUTE,1);
        thisMonth.set(Calendar.SECOND,1);
        thisMonth.set(Calendar.MILLISECOND,10);
        thisMonth.set(Calendar.AM_PM, Calendar.AM);
 
        // Se define la fecha de hoy
        Calendar today = Calendar.getInstance(); //used to highlight today's date cell in the calendar
    
        Calendar endOfMonth = (Calendar)thisMonth.clone();
        endOfMonth.set(Calendar.DATE,endOfMonth.getActualMaximum(Calendar.DATE));
        endOfMonth.add(Calendar.DATE,7-endOfMonth.get(Calendar.DAY_OF_WEEK));
    
        // set calendar to first day of the first week (may move back 1 month)
        thisMonth.add(Calendar.DATE,1-thisMonth.get(Calendar.DAY_OF_WEEK));         
        
        // Se crean todos los dias necesarios y se crea el arrayList
        theDays = new ArrayList();
        WebDay data;
        do {
          for (int i=0; i < 7; i++) {
              // Se crea un nuevo WebDay con base en la fecha de hoy y la
              // que esta siendo procesada.
              data = new WebDay(currentDate, thisMonth);
              
              // Se agrega a la lista de dias.
              theDays.add(data);  
            
              // Siguiente dia.
              thisMonth.add(Calendar.DATE,1);
          }        
        } while(thisMonth.getTime().before(endOfMonth.getTime()));            
        
    }       
    
    /**
     * @return
     */    
    public int getDay() {
        return currentDate.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * @return
     */    
    public String getMonth() { 
        Locale mylocate = new Locale(locate,"");
        String month = "";
        
        int mes = currentDate.get(Calendar.MONTH) + 1;
        switch (mes) {
            case 1:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.January");
                break;
            case 2:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.February");
                break;
            case 3:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.March");
                
                break;
            case 4:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.April");
                
                break;
            case 5:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.May");
                
                break;                
            case 6:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.June");
                
                break;
            case 7:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.July");
                
                break;
            case 8:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.August");
                
                break;
            case 9:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.September");
                
                break;
            case 10:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.October");
                
                break;
            case 11:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.November");
                
                break;
            case 12:
                month = java.util.ResourceBundle.getBundle("ApplicationResources", 
                    mylocate).getString("common.December");
                
                break;                
        }                
       
        return month;
    }
    
    /**
     * @return
     */    
    public int getYear() { return currentDate.get(Calendar.YEAR); }
    
    /**
     * @return
     */    
    public ArrayList getDays() {return theDays; }
    
    /**
     * @return
     */    
    public long getTodayTime() {
        return Calendar.getInstance().getTimeInMillis();
    }
    
    public long getDateTime() {
        return currentDate.getTimeInMillis();
    }
    
    /**
     * @return
     */    
    public long getPrevMonth() {
        Calendar temp = (Calendar)currentDate.clone();
        
        // Se resetea al primer dia del mes.
        temp.set(Calendar.DATE, 1);
        temp.add(Calendar.MONTH, -1);
        
        return temp.getTimeInMillis();
    }
    
    /**
     * @return
     */    
    public long getNextMonth() {
        Calendar temp = (Calendar)currentDate.clone();
        
        // Se resetea al primer dia del mes.
        temp.set(Calendar.DATE, 1);
        temp.add(Calendar.MONTH, 1);
        
        return temp.getTimeInMillis();        
    }
    
    // Regresa la fecha de entrega formateada de forma apropiada.
     public String getFormatedDate() { 
         java.text.DateFormat df = java.text.DateFormat.
                getDateInstance(java.text.DateFormat.MEDIUM);
         
         return df.format(currentDate.getTime());
     }         
     
     public void setLocate(String idiom) {
         locate = idiom;
     }
    
}
