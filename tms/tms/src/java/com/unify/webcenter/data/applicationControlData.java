/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.data;

import java.sql.Timestamp;

/**
 *
 * @author esper
 */
public class applicationControlData extends mainData{
    private int id;
    private int state;
    private java.sql.Timestamp application_date;
    private String comment;
    private int id_task;
    private int id_application_user;
    private int id_organization;
    private int id_account;
    
    private tasksData parentTask;
    private membersData parentMembers;
    private organizationsData parentOrganization;
    private accountsData parentAccount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getApplication_date() {
        return application_date;
    }

    public void setApplication_date(Timestamp application_date) {
        this.application_date = application_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public int getId_application_user() {
        return id_application_user;
    }

    public void setId_application_user(int id_application_user) {
        this.id_application_user = id_application_user;
    }

    public int getId_organization() {
        return id_organization;
    }

    public void setId_organization(int id_organization) {
        this.id_organization = id_organization;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public tasksData getParentTask() {
        return parentTask;
    }

    public void setParentTask(tasksData parentTask) {
        this.parentTask = parentTask;
    }

    public membersData getParentMembers() {
        return parentMembers;
    }

    public void setParentMembers(membersData parentMembers) {
        this.parentMembers = parentMembers;
    }

    public organizationsData getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(organizationsData parentOrganization) {
        this.parentOrganization = parentOrganization;
    }

    public accountsData getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(accountsData parentAccount) {
        this.parentAccount = parentAccount;
    }
    

    
    
}
