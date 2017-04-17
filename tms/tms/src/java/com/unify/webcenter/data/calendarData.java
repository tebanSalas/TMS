//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.data;

import java.util.Locale;

/**
 * Class that represent the table calendar
 * @author Administrator
 */
public class calendarData extends mainData {

    protected String subject;
    //protected char[] description;
    protected String description;
    protected java.sql.Timestamp date;
    protected int member;
    protected int id;
    protected int hour_start;
    protected int hour_end;
    protected int min_start;
    protected int min_end;
    protected int task;
    protected String guests;
    private int id_account;
    protected int projectId;
    protected String projectName;
    protected java.sql.Timestamp start_date;
    protected java.sql.Timestamp end_date;
    protected String projectowner;
    protected String owneremail;
    protected String taskdesc;

    public void setsubject(String val) {
        subject = val;
    }

    public void setdate(java.sql.Timestamp val) {
        date = val;
    }

    /*public void setdescription(char[] val) {
    description = val;
    }
    public void setdescription(String val) {
    description = val.toCharArray();
    }*/
    public void setdescription(String val) {
        description = val;
    }

    public void setmember(int val) {
        member = val;
    }

    public void setid(int val) {
        id = val;
    }

    public void sethour_start(int val) {
        hour_start = val;
    }

    public void sethour_end(int val) {
        hour_end = val;
    }

    public void setmin_start(int val) {
        min_start = val;
    }

    public void setmin_end(int val) {
        min_end = val;
    }

    public void settask(int val) {
        task = val;
    }

    public void setprojectId(int val) {
        projectId = val;
    }

    public void setstart_date(java.sql.Timestamp val) {
        start_date = val;
    }

    public void setend_date(java.sql.Timestamp val) {
        end_date = val;
    }

    public void setprojectowner(String val) {
        projectowner = val;
    }

    public void setowneremail(String val) {
        owneremail = val;
    }

    public void settaskdesc(String val) {
        taskdesc = val;
    }

    public void setprojectName(String val) {
        projectName = val;
    }

    public void setguests(String val) {
        guests = val;
    }

    public String getsubject() {
        return subject;
    }

    public java.sql.Timestamp getdate() {
        return date;
    }

    /*public char[] getdescription() {
    return description;
    }*/
    public String getdescription() {
        return description;
    }

    public int getmember() {
        return member;
    }

    public int getid() {
        return id;
    }

    public int gethour_start() {
        return hour_start;
    }

    public int gethour_end() {
        return hour_end;
    }

    public int getmin_start() {
        return min_start;
    }

    public int getmin_end() {
        return min_end;
    }

    public int gettask() {
        return task;
    }

    public int getprojectId() {
        return projectId;
    }

    public java.sql.Timestamp getstart_date() {
        return start_date;
    }

    public java.sql.Timestamp getend_date() {
        return end_date;
    }

    public String getprojectowner() {
        return projectowner;
    }

    public String getowneremail() {
        return owneremail;
    }

    public String gettaskdesc() {
        return taskdesc;
    }

    public String getprojectName() {
        return projectName;
    }

    public String getguests() {
        return guests;
    }

    public String getHourStart() {

        String salida = "" + hour_start;
        if (salida.length() == 1) {
            salida = "0" + salida;
        }

        return salida;
    }

    public String getHourEnd() {

        String salida = "" + hour_end;
        if (salida.length() == 1) {
            salida = "0" + salida;
        }
        return salida;
    }

    public String getMinStart() {

        String salida = "" + min_start;
        if (salida.length() == 1) {
            salida = salida + "0";
        }
        return salida;
    }

    public String getMinEnd() {

        String salida = "" + min_end;
        if (salida.length() == 1) {
            salida = salida + "0";
        }
        return salida;
    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedStartDate() {
        try {
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
            return df.format(start_date);
        } catch (Exception e) {
            return "";
        }

    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedEndDate() {
        try {
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
            return df.format(end_date);
        } catch (Exception e) {
            return "";
        }

    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedStartDate_es() {
        try {
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, new Locale("es"));
            return df.format(start_date);
        } catch (Exception e) {
            return "";
        }
    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedDate() {
        try {
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
            return df.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedEndDate_es() {
        try {
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, new Locale("es"));
            return df.format(end_date);
        } catch (Exception e) {
            return "";
        }

    }

    // Metodo que obtiene la representacion String de este calendario.
    public String toString() {

        return subject + " " + description + " " + date + " " +
                member + " " + id;

    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }
}
