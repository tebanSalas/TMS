/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.data;

/**
 *
 * @author Alberto Campos
 */
public class schedulesLogData extends mainData { 
    private int id;
    private int id_account;
    private int task;
    private int member;
    private int status;
    private String old_comment;
    private String new_comment;
    private java.sql.Timestamp old_date;
    private String new_date;
    private java.sql.Timestamp created;
    private membersData parentMember;
    private tasksData parentTask;

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

    /**
     * @return the old_comment
     */
    public String getOld_comment() {
        return old_comment;
    }

    /**
     * @param old_comment the old_comment to set
     */
    public void setOld_comment(String old_comment) {
        this.old_comment = old_comment;
    }

    /**
     * @return the new_comment
     */
    public String getNew_comment() {
        return new_comment;
    }

    /**
     * @param new_comment the new_comment to set
     */
    public void setNew_comment(String new_comment) {
        this.new_comment = new_comment;
    }

    /**
     * @return the old_date
     */
    public java.sql.Timestamp getOld_date() {
        return old_date;
    }

    /**
     * @param old_date the old_date to set
     */
    public void setOld_date(java.sql.Timestamp old_date) {
        this.old_date = old_date;
    }

    /**
     * @return the new_date
     */
    public String getNew_date() {
        return new_date;
    }

    /**
     * @param new_date the new_date to set
     */
    public void setNew_date(String new_date) {
        this.new_date = new_date;
    }

    /**
     * @return the parentTask
     */
    public tasksData getParentTask() {
        return parentTask;
    }

    /**
     * @param parentTask the parentTask to set
     */
    public void setParentTask(tasksData parentTask) {
        this.parentTask = parentTask;
    }
}
