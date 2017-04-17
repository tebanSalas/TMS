/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.form;

/**
 *
 * @author Alberto Campos 
 */
public class copyProjectForm {
    /*Project*/
    private String name;
    private java.sql.Timestamp start_date;
    private java.sql.Timestamp end_date;
    private int copy_content;
    
    /*Tasks*/
    private java.sql.Timestamp start_date_task;
    private java.sql.Timestamp end_date_task;
    private int assigned_to;
    private java.math.BigDecimal estimated;
    protected java.math.BigDecimal fare;
    private int copy_content_task;
    
    
}
