//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.data;

/**
* Class that represent the table risks
* @author Administrator
*/
public class risksData extends mainData {
	 protected int id;
         protected String description;
	 protected int probability;
	 protected int impact;
	 //protected char[] todoaction;
         protected String todoaction;
	 protected String planb;
	 protected int task;
         protected String taskdesc;
	 protected int project;
private int id_account;
         public risksData() {
             
         }
         
         // Constructor que crea una nueva instancia de un riskData, pero poniendo
         // a 0 su id
         public risksData(risksData val) {
             this.id = 0;
             this.impact = val.getimpact();
             this.probability = val.getprobability();
             this.project = val.getproject();
             this.task = val.gettask();
             this.taskdesc = val.gettaskdesc();
             this.todoaction = val.gettodoaction();
             this.description = val.getdescription();
             this.planb = val.getplanb();
         }
         
         public void setid(int val) { id = val; } 
	 public void setdescription (String val) { description = val; } 
	 public void setprobability (int val) { probability = val; } 
	 public void setimpact(int val) { impact = val; } 
	 /*public void settodoaction(char[] val) { todoaction = val; } 
         public void settodoaction(String val) { todoaction = val.toCharArray(); } */
         public void settodoaction(String val) { todoaction = val; } 
	 public void setplanb(String val) { planb = val; } 
	 public void settask(int val) { task = val; } 
	 public void setproject(int val) { project = val; } 
         public void settaskdesc(String val) { taskdesc = val; }
         
         public int getid() { return id; } 
         public String getdescription() { return description;}
         public int getprobability() { return probability;}
         public int getimpact() { return impact;}
         //public char[] gettodoaction() { return todoaction;}
         public String gettodoaction() { return todoaction;}
         public String getFormatedTodoAction() { 
             if (todoaction != null)
                return new String(todoaction);
             else
                 return "";
         }
         public String getplanb() { return planb; }
         public int gettask() { return task; } 
	 public int getproject() { return project; } 
	 public String gettaskdesc() { return taskdesc; }
         
	 public String toString() {
		 return " id:" + id + " description:" + description + " probability:" + probability +
                 " impact:" + impact + " todoaction:" + todoaction + " planb:" + planb + " task:" + task + " project:" + project + " taskdesc:" + taskdesc; 
	 }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }
}