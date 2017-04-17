/*
 * WebDay.java
 *
 * Created on March 2, 2003, 4:29 PM
 */

package com.unify.webcenter.tools;

import java.util.Calendar;
/**
 * Clase que representa a un d�a en un WebCalendar
 * @author  Gerardo Arroyo Arce
 */
public class WebDay implements java.io.Serializable {
    private Calendar thisDay;  // El dia que representa esta instancia
    private boolean isToday = false;
    private boolean hasEvents = false;
    private boolean isCurrentMonth = false;
    
    /** Creates a new instance of WebDay
     * @param selectedDay La fecha enviada por el constructor del calendario
     * @param theDay El dia que se esta creando
     */
    public WebDay(Calendar selectedDay, Calendar theDay) {
        thisDay = (Calendar)theDay.clone();        
        
        // Si esta fecha correspondia al dia actual
        Calendar now = Calendar.getInstance();
        if ( (now.get(Calendar.DAY_OF_YEAR) == theDay.get(Calendar.DAY_OF_YEAR)) &&
             (now.get(Calendar.YEAR) == theDay.get(Calendar.YEAR)))
            isToday = true;
        
        if (theDay.get(Calendar.MONTH) == selectedDay.get(Calendar.MONTH))
            isCurrentMonth = true;
    }
    
    
    /**
     * @return
     */    
    public boolean isToday() { return isToday; }
    
    /**
     * @return
     */    
    public boolean hasEvents() { return hasEvents; }
    
    /**
     * @param val
     */    
    public void setEvents(boolean val) { hasEvents = val; }
    
    /**
     * @return
     */    
    public String getDayLabel() { return "" + thisDay.get(Calendar.DAY_OF_MONTH); };
    
    /**
     * @return
     */    
    public Calendar getThisDay() { return thisDay; }
    
    public boolean isCurrentMonth() { return isCurrentMonth; }
    
    public long getTime() { return thisDay.getTimeInMillis(); }
}
