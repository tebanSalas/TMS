//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.data;

import java.util.Date;
/**
* Class that represent the table topics
* @author Administrator
*/
public class topicsData extends mainData {
	 private int id;
         private int project;
	 private int owner;
	 private String totasks;
	 private int tasks;
	 private String status;
	 private String published;
	 private String subject;
	 private java.sql.Timestamp last_post;
	 
	 protected int posts;
         protected membersData parentOwner;
         private projectsData parentProject; 
         protected java.util.Vector detailPost;
         private int id_account;
         private String notified;
	 public void setid(int val) { id = val; } 
	 public void setowner(int val) { owner = val; } 
	 public void settotasks(String val) { totasks = val; } 
	 public void settasks(int val) { tasks = val; } 
	 public void setstatus(String val) { status = val; } 
	 public void setpublished(String val) { published = val; } 
	 public void setsubject(String val) { subject = val; } 
	 public void setlast_post(java.sql.Timestamp val) { last_post = val; } 
	 public void setproject(int val) { project = val; } 
	 public void setposts(int val) { posts = val; } 
         public void setparentOwner(membersData val) {parentOwner = val; }
         public void setdetailPost(java.util.Vector val) {detailPost = val; }

	 public int getid() { return id;}
	 public int getowner() { return owner;}
	 public String gettotasks() { return totasks;}
	 public int gettasks() { return tasks;}
	 public String getstatus() { return status;}
	 public String getpublished() { return published;}
	 public String getsubject() { return subject;}
	 public java.sql.Timestamp getlast_post() { return last_post;}
	 public int getproject() { return project;}
	 public int getposts() { return posts;}
         public membersData getparentOwner() {return parentOwner; }
         public java.util.Vector getdetailPost() {return detailPost; }
         
         /**
          * Metodo que regresa la fecha y hora del ultimo post formateado.
          */
         public String getFormattedLastPost() {
             java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm");
             
             try {
                return format.format(new Date(last_post.getTime()));
             } catch (Exception e) {
                 return "";
             }
         }
         
	 public String toString() {
		 return" id:" + id + " owner:" + owner + " totasks:" + totasks + " tasks:" + tasks + " status:" + status + " published:" + published + " subject:" + subject + " last_post:" + last_post + " project:" + project + " posts:" + posts; 
	 }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getnotified() {
        return notified;
    }

    public void setnotified(String notified) {
        this.notified = notified;
    }

    /**
     * @return the parentProject
     */
    public projectsData getParentProject() {
        return parentProject;
    }

    /**
     * @param parentProject the parentProject to set
     */
    public void setParentProject(projectsData parentProject) {
        this.parentProject = parentProject;
    }
}