//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.data;

import java.math.BigDecimal;
import java.text.*;
import java.util.Locale;

/**
 * Class that represent the table projects
 * @author Administrator
 */
public class projectsData extends mainData implements java.lang.Comparable {

    private String name;
    protected int organization;
    protected int id;
    protected int owner;
    private int project_id;
    private String sec_projects;
    protected String published_endtask;
    protected String upload_max;
    protected java.sql.Timestamp created;
    protected int status;
    protected String published_assigned;
    protected String published;
    protected String autom_notification;
    protected int priority;
    protected java.sql.Timestamp modified;
    //protected char[] description;
    protected String description;
    protected organizationsData parentOrganizations;
    protected membersData parentOwner;
    private projectsData parentProject;
    protected java.util.Vector detailTeams;
    protected java.util.Vector detailRisks;
    protected java.sql.Timestamp start_date;
    protected java.sql.Timestamp end_date;
    protected int totalprojecttasks;
    protected int totalassignedprojecttasks;
    protected int totalontimeprojecttasks;
    protected int totalnotassignedprojecttasks;
    protected int totaldelayedprojecttasks;
    protected int totalsuspendedprojecttasks;
    protected int totalrejectedprojecttasks;
    protected int totalendedprojecttasks;
    protected int totalTopics;
    private int id_account;
        private String send_email;

    public projectsData() {

        detailTeams = new java.util.Vector();
        detailRisks = new java.util.Vector();
    }

    public projectsData(projectsData val) {

        detailTeams = new java.util.Vector();

        detailRisks = new java.util.Vector();

        this.name = val.getname();

        this.organization = val.getorganization();

        this.id = val.getid();

        this.owner = val.getowner();

        this.published_endtask = val.getpublished_endtask();

        this.upload_max = val.getupload_max();

        this.created = val.getcreated();

        this.status = val.getstatus();

        this.published_assigned = val.getpublished_assigned();

        this.published = val.getpublished();

        this.priority = val.getpriority();
        
        this.autom_notification= val.getautom_notification();

        this.modified = val.getmodified();

        this.description = val.getdescription();

        this.start_date = val.getstart_date();

        this.end_date = val.getend_date();

        this.totalprojecttasks = val.gettotalprojecttasks();
        this.totalassignedprojecttasks = val.gettotalassignedprojecttasks();
        this.totalnotassignedprojecttasks = val.gettotalnotassignedprojecttasks();
        this.totalontimeprojecttasks = val.gettotalontimeprojecttasks();
        this.totaldelayedprojecttasks = val.gettotaldelayedprojecttasks();
        this.totalsuspendedprojecttasks = val.gettotalsuspendedprojecttasks();
        this.totalrejectedprojecttasks = val.gettotalrejectedprojecttasks();
        this.totalendedprojecttasks = val.gettotalendedprojecttasks();
        this.totalTopics = val.gettotalTopics();
    }

    public void setdetailTeams(java.util.Vector val) {
        detailTeams = val;
    }

    public void setdetailRisks(java.util.Vector val) {
        detailRisks = val;
    }

    public void setname(String val) {
        name = val;
    }

    public void setorganization(int val) {
        organization = val;
    }

    public void setid(int val) {
        id = val;
    }

    public void setowner(int val) {
        owner = val;
    }

    public void setpublished_endtask(String val) {
        published_endtask = val;
    }

    public void setupload_max(String val) {
        upload_max = val;
    }

    public void setcreated(java.sql.Timestamp val) {
        created = val;
    }

    public void setstatus(int val) {
        status = val;
    }

    public void setpublished_assigned(String val) {
        published_assigned = val;
    }

    public void setpublished(String val) {
        published = val;
    }

    public void setpriority(int val) {
        priority = val;
    }

    public void setmodified(java.sql.Timestamp val) {
        modified = val;
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

  
    public void setstart_date(java.sql.Timestamp val) {
        start_date = val;
    }

    public void setend_date(java.sql.Timestamp val) {
        end_date = val;
    }

    public void setparentOrganizations(organizationsData val) {
        parentOrganizations = val;
    }

    public void setparentOwner(membersData val) {
        parentOwner = val;
    }

    public void settotalprojecttasks(int val) {
        totalprojecttasks = val;
    }

    public void settotalassignedprojecttasks(int val) {
        totalassignedprojecttasks = val;
    }

    public void settotalnotassignedprojecttasks(int val) {
        totalnotassignedprojecttasks = val;
    }

    public void settotalontimeprojecttasks(int val) {
        totalontimeprojecttasks = val;
    }

    public void settotaldelayedprojecttasks(int val) {
        totaldelayedprojecttasks = val;
    }

    public void settotalsuspendedprojecttasks(int val) {
        totalsuspendedprojecttasks = val;
    }

    public void settotalrejectedprojecttasks(int val) {
        totalrejectedprojecttasks = val;
    }

    public void settotalendedprojecttasks(int val) {
        totalendedprojecttasks = val;
    }

    public void settotalTopics(int val) {
        totalTopics = val;
    }

    public java.util.Vector getdetailTeams() {
        return detailTeams;
    }

    public java.util.Vector getdetailRisks() {
        return detailRisks;
    }

    public String getname() {
        return name;
    }

    public int getorganization() {
        return organization;
    }

    public int getid() {
        return id;
    }

    public int getowner() {
        return owner;
    }

    public String getpublished_endtask() {
        return published_endtask;
    }

    public String getupload_max() {
        return upload_max;
    }

    public java.sql.Timestamp getcreated() {
        return created;
    }

    public int getstatus() {
        return status;
    }

    public String getpublished_assigned() {
        return published_assigned;
    }

    public String getpublished() {
        return published;
    }

    public int getpriority() {
        return priority;
    }

    public java.sql.Timestamp getmodified() {
        return modified;
    }

   /* public char[] getdescription() {
        return description;
    }*/

     public String getdescription() {
        return description;
    }

    public String getFormatedDescription() {
        return (description != null ? new String(description) : "");
    }

    public java.sql.Timestamp getstart_date() {
        return start_date;
    }

    public java.sql.Timestamp getend_date() {
        return end_date;
    }

    public organizationsData getparentOrganizations() {
        return parentOrganizations;
    }

    public membersData getparentOwner() {
        return parentOwner;
    }

    public int gettotalprojecttasks() {
        return totalprojecttasks;
    }

    public int gettotalassignedprojecttasks() {
        return totalassignedprojecttasks;
    }

    public int gettotalnotassignedprojecttasks() {
        return totalnotassignedprojecttasks;
    }

    public int gettotalontimeprojecttasks() {
        return totalontimeprojecttasks;
    }

    public int gettotaldelayedprojecttasks() {
        return totaldelayedprojecttasks;
    }

    public int gettotalsuspendedprojecttasks() {
        return totalsuspendedprojecttasks;
    }

    public int gettotalrejectedprojecttasks() {
        return totalrejectedprojecttasks;
    }

    public int gettotalendedprojecttasks() {
        return totalendedprojecttasks;
    }

    public int gettotalTopics() {
        return totalTopics;
    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedEndDate() {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
        if (end_date == null) {
            return "";
        } else {
            return df.format(end_date);
        }

    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedEndDate_es() {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, new Locale("es"));
        if (end_date == null) {
            return "";
        } else {
            return df.format(end_date);
        }

    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedStartDate() {

        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
        if (start_date == null) {
            return "";
        } else {
            return df.format(start_date);
        }
    }

    // Regresa la fecha de entrega formateada de forma apropiada.
    public String getFormatedStartDate_es() {

        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, new Locale("es"));
        if (start_date == null) {
            return "";
        } else {
            return df.format(start_date);
        }
    }

    public String toString() {
        return " name:" + name + " organization:" + parentOrganizations.getname() + " id:" + id + " owner:" +
                parentOwner.getlogin() + " description:" +
                (description == null ? "" : new String(description));
    }

    //Metodo de comparacion para efectos de hacer un ordenamiento por nombre default
    public int compareTo(Object obj) {
        int tmp = ((projectsData) obj).gettotaldelayedprojecttasks();
        return new Integer(tmp).compareTo(
                new Integer(totaldelayedprojecttasks));
    }

    public String formatRealHour(BigDecimal realHour) {
        String auxRealHours = null;
        String replaceRealHours = realHour.toString().replace('.', '-');
        String[] splitRealHours = replaceRealHours.split("-");
        if (splitRealHours.length > 1) {
            if (splitRealHours[1].length() == 1) {
                auxRealHours = splitRealHours[0].toString() + ":" + splitRealHours[1].toString() + "0";
            } else {
                auxRealHours = splitRealHours[0].toString() + ":" + splitRealHours[1].toString();
            }
        } else {
            auxRealHours = splitRealHours[0].toString() + ":0";
        }
        return auxRealHours;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public projectsData getParentProject() {
        return parentProject;
    }

    public void setParentProject(projectsData parentProject) {
        this.parentProject = parentProject;
    }

    public String getSec_projects() {
        return sec_projects;
    }

    public void setSec_projects(String sec_projects) {
        this.sec_projects = sec_projects;
    }

    public String getSend_email() {
        return send_email;
    }

    public void setSend_email(String send_email) {
        this.send_email = send_email;
    }

    public String getautom_notification() {
        return autom_notification;
    }

    public void setautom_notification(String autom_notification) {
        this.autom_notification = autom_notification;
    }

}