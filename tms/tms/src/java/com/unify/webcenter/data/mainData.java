/*
 * mainData.java
 *
 * Created on February 19, 2003, 11:14 AM
 */
package com.unify.webcenter.data;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Clase ancestro de todos los datas de Unify WebCenter
 * @author  Gerardo Arroyo
 */
public class mainData implements java.io.Serializable {

    /** Creates a new instance of mainData */
    public mainData() {
    }

    public String toString() {
        return "";
    }

    public int getid() {
        return 0;
    }

    public String convTimeZone(Timestamp time, String sourceTZ) {
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        DateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {            
        sdf.setTimeZone(TimeZone.getTimeZone(sourceTZ));       
        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }       
        return sdf.format(time);
        
    }
}
