/*
 * performanceData.java
 *
 * Created on May 23, 2003, 8:56 AM
 */

package com.unify.webcenter.data;

/**
 * Clase empleada para enviar datos de rendimientos.
 *
 * @author  Gerardo Arroyo
 */
public class performanceData extends mainData {
    private String name;
    private String value;
    private int realValue;
  
    /** Creates a new instance of performanceData */
    public performanceData(String name, String value, int realVal) {
        this.name = name;
        this.value = value;
        this.realValue = realVal;
    }
    
    public String getname() { return name; }
    public String getvalue() { return value; }
    public int    getValue() { return realValue; }
                
}
