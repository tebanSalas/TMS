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
    private tasksData id_task;
    private membersData id_application_user;
    private organizationsData id_organization;
    private accountsData id_account;

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

    public tasksData getId_task() {
        return id_task;
    }

    public void setId_task(tasksData id_task) {
        this.id_task = id_task;
    }

    public membersData getId_application_user() {
        return id_application_user;
    }

    public void setId_application_user(membersData id_application_user) {
        this.id_application_user = id_application_user;
    }

    public organizationsData getId_organization() {
        return id_organization;
    }

    public void setId_organization(organizationsData id_organization) {
        this.id_organization = id_organization;
    }

    public accountsData getId_account() {
        return id_account;
    }

    public void setId_account(accountsData id_account) {
        this.id_account = id_account;
    }
    
    
}
