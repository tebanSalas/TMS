/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.data;

/**
 *
 * @author MARCELA QUINTERO
 */
public class configSendReportData extends mainData {

    private int id_account;
    private int id_master_report;
    private int members;
    private String periodicity;
    private int notification;
    private String format;
    private membersData parentMember;
    private membersData notificationMember;
        
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

    public String toString() {
        return " id_account:" + id_account + " id_master_report:" + id_master_report + " periodicidad:" + periodicity +
                " notification:"+notification + " format:"+format;
    }

    public membersData getParentMember() {
        return parentMember;
    }

    public void setParentMember(membersData parentMember) {
        this.parentMember = parentMember;
    }

    public membersData getNotificationMember() {
        return notificationMember;
    }

    public void setNotificationMember(membersData notificationMember) {
        this.notificationMember = notificationMember;
    }
}
