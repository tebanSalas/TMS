/*
 * loginForm.java
 *
 * Created on January 27, 2003, 11:05 AM
 */

package com.unify.webcenter.form;

//   Generated by FlechaRoja Tech Tools (2003) 

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;

/**
* Class that represent the form for the table files
* @author Administrator
*/
public class loginForm  extends ValidatorActionForm {

        private String operation;
	private String username;
	private String password;
        private String language;

	public void setOperation(String val) { operation =  val; }
        public void setusername(String val) { username = val; } 
	public void setpassword(String val) { password = val; } 
        public void setlanguage(String val) { language = val; } 

	public String getOperation() { return operation; }
        public String getusername() { return username;}
	public String getpassword() { return password;}
        public String getlanguage() { return language;}
        
        /**
        * Reset all properties to their default values.
        *
        * @param mapping The mapping used to select this instance
        * @param request The servlet request we are processing
        */
        public void reset(ActionMapping mapping, HttpServletRequest request) {
            username = "";
            password = "";
            language = "es";
            operation = "";
	}

	 public String toString() {
		 return " username:" + username + " password:" + password + " language:" + language; 
	 }
}