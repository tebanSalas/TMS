//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

import java.util.*;

/**
 * Class that represent the form for the table reports
 * @author Administrator
 */
public class masterReportsForm extends ValidatorActionForm {

    private String operation;
    private java.sql.Timestamp end_date;
    private java.sql.Timestamp start_date;
    private int members;
    private String[] projects;
    private java.sql.Timestamp end_due_date;
    private java.sql.Timestamp start_due_date;
    private int completionI;
    private int completionF;
    private String[] status;
    private java.sql.Timestamp end_real_due_date;
    private java.sql.Timestamp start_real_due_date;

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setEnd_date(null);
        setStart_date(null);
        setEnd_due_date(null);
        setStart_due_date(null);
        setEnd_real_due_date(null);
        setStart_real_due_date(null);
        completionF=0;
        completionI=0;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public java.sql.Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(java.sql.Timestamp end_date) {
        this.end_date = end_date;
    }

    public java.sql.Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(java.sql.Timestamp start_date) {
        this.start_date = start_date;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }

    public java.sql.Timestamp getEnd_due_date() {
        return end_due_date;
    }

    public void setEnd_due_date(java.sql.Timestamp end_due_date) {
        this.end_due_date = end_due_date;
    }

    public java.sql.Timestamp getStart_due_date() {
        return start_due_date;
    }

    public void setStart_due_date(java.sql.Timestamp start_due_date) {
        this.start_due_date = start_due_date;
    }

    public int getCompletionI() {
        return completionI;
    }

    public void setCompletionI(int completionI) {
        this.completionI = completionI;
    }

    public int getCompletionF() {
        return completionF;
    }

    public void setCompletionF(int completionF) {
        this.completionF = completionF;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public java.sql.Timestamp getEnd_real_due_date() {
        return end_real_due_date;
    }

    public void setEnd_real_due_date(java.sql.Timestamp end_real_due_date) {
        this.end_real_due_date = end_real_due_date;
    }

    public java.sql.Timestamp getStart_real_due_date() {
        return start_real_due_date;
    }

    public void setStart_real_due_date(java.sql.Timestamp start_real_due_date) {
        this.start_real_due_date = start_real_due_date;
    }
}
