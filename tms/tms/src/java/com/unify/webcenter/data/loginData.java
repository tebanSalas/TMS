/*

 * loginData.java

 *

 * Created on January 27, 2003, 7:09 PM

 */



package com.unify.webcenter.data;



import java.util.*;

/**

 *

 * @author  Administrator

 */

public class loginData extends mainData {

        protected int id;

	protected String username;

        protected String fullname;

        protected String language;

        protected String template;

	protected String profile;

        protected String date;
        protected String allowCloseTasks;
        private String time_zone;
        private int id_account;
        private String ind_exec_report;
        private int viewAppControl;
        private int reportApplications;
        private int AppControl;
        private int versionControl;

        public void setusername(String val) { username = val; } 

        public void setfullname(String val) { fullname = val; } 

	public void setid(int val) { id = val; } 

        public void setlanguage(String val) { language = val; } 

        public void setTemplate(String val) { template = val; }

        public void setprofile(String val) { profile = val; } 
        public void setAllowCloseTasks(String val) { allowCloseTasks = val; } 
        

	
        public String getAllowCloseTasks() { return allowCloseTasks; } 
        public String getusername() { return username;}

        public String gefullname() { return fullname;}

	public int getid() { return id;}

        public String getlanguage() { return language;}

        public String getTemplate() { return template; }

        public String getprofile() { return profile;}

        public String getDate() { return date; }

        public boolean isAdmin() { 
            boolean value = false;
            //if (id == 1) {
            if (profile.equals("3") ||profile.equals("4")) {
                value = true;
            }
            return value;
        }

        public loginData() {

            Calendar now = Calendar.getInstance();

            java.text.DateFormat df = java.text.DateFormat.

                    getDateInstance(java.text.DateFormat.MEDIUM);

                        

            date = df.format(now.getTime());

        }

        

	 public String toString() {

		 return " username:" + username + " id:" + id + " language:" + language; 

	 }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getInd_exec_report() {
        return ind_exec_report;
    }

    public void setInd_exec_report(String ind_exec_report) {
        this.ind_exec_report = ind_exec_report;
    }

    public int getReportApplications() {
        return reportApplications;
    }

    public void setReportApplications(int reportApplications) {
        this.reportApplications = reportApplications;
    }

    

    public int getViewAppControl() {
        return viewAppControl;
    }

    public void setViewAppControl(int viewAppControl) {
        this.viewAppControl = viewAppControl;
    }

    public int getAppControl() {
        return AppControl;
    }

    public void setAppControl(int AppControl) {
        this.AppControl = AppControl;
    }

    public int getVersionControl() {
        return versionControl;
    }

    public void setVersionControl(int versionControl) {
        this.versionControl = versionControl;
    }
    


}