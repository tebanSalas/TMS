/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.data;

/**
 *
 * @author MARCELA QUINTERO
 */
public class masterReportData extends mainData {
   private int id;
	 private String name;
	 private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
	 
 public String toString() {
		 return" id:" + id + " name:" + name + " description:" + description; 
	 }

}
