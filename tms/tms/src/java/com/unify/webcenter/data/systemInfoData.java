/*
 * systemInfoData.java
 *
 * Created on February 18, 2003, 10:53 AM
 */

package com.unify.webcenter.data;

/**
 * Clase que representa una entrada de informacion del sistema.
 * @author  Gerardo Arroyo Arce
 */
public class systemInfoData extends mainData {
    private String name;
    private String value;
    
    /** Creates a new instance of systemInfoData */
    public systemInfoData(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getname() { return name; }
    public String getvalue() { return value; }
            
}
