/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;

/**
 *
 * @author MARCELA QUINTERO
 */
public class configSendReportForm  extends ValidatorActionForm {
  private int id_account;
    private int id_master_report;
    private int members;
    private String periodicity;
    private int notification;
    private String format;
    private String operation;
    private String sortColumn;
    private String sortOrder;   
    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public int getId_master_report() {
        return id_master_report;
    }

    public void setId_master_report(int id_master_report) {
        this.id_master_report = id_master_report;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
 public void reset(ActionMapping mapping, HttpServletRequest request) {
		
		id_account = 0;
		id_master_report=0;
                members=0;
                periodicity=" ";
                notification=0;
                format=" ";
                operation=" ";
                sortColumn="members";
                sortOrder="ASC";
	}
 
  public String toString() {
        return " id_account:" + id_account + " id_master_report:" + id_master_report + " periodicidad:" + periodicity +
                " notification:"+notification + " format:"+format;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
