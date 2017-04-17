/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.data;

/**
 *
 * @author Alberto Campos
 */
public class tasksStatusLogData extends mainData { 
    private int id;
    private int id_account;
    private int task;
    private int member;
    private int status;
    private java.sql.Timestamp created;
    private membersData parentMember;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreated() {
        return created;
    }

    public void setCreated(java.sql.Timestamp created) {
        this.created = created;
    }

    public membersData getParentMember() {
        return parentMember;
    }

    public void setParentMember(membersData parentMember) {
        this.parentMember = parentMember;
    }

    public int getStatus() {
        return status;
    }
}
