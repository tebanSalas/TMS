//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorActionForm;
import org.apache.struts.util.MessageResources;


import org.apache.struts.upload.FormFile;
import java.io.File;
import javax.mail.internet.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.data.tasksData;
import com.unify.webcenter.data.topicsData;

/**
* Class that represent the form for the table files
* @author Administrator
*/
public class filesForm  extends ValidatorActionForm {

        // use for upload
        private String operation;
        private int id;
        private int project;
        private int task;
        private int topic;
        private String comments;
        protected FormFile theFile;        
        private String fromPage;
	private String sortColumn;
	private String sortOrder;
        private String to_replicate;

        // use for edit and view
        protected String name;
        protected int owner;
        protected java.sql.Timestamp date;
        protected int status;
        protected String published;
        protected String type;
        protected java.sql.Timestamp upload;
        protected java.math.BigDecimal size;

        public void setOperation(String val) { operation =  val; }
        public void setid(int val) { id = val; } 
        public void setproject(int val) { project = val; } 
        public void settask(int val) { task = val; } 
        public void setcomments(String val) { comments = val; } 
        public void setTheFile(FormFile theFile) { this.theFile = theFile; }
        public void setname(String val) { name = val; } 
        public void setowner(int val) { owner = val; } 
        public void setdate(java.sql.Timestamp val) { date = val; } 
        public void setstatus(int val) { status = val; } 
        public void setpublished(String val) { published = val; } 
        public void settype(String val) { type = val; } 
        public void setupload(java.sql.Timestamp val) { upload = val; } 
        public void setsize(java.math.BigDecimal val) { size = val; } 
        public void setfromPage(String val) { fromPage = val; }
	public void setsortColumn(String val) { sortColumn =  val; }
	public void setsortOrder(String val) { sortOrder =  val; }        
	public void settopic(int val) { topic = val; } 
        
        public String getOperation() { return operation; }
        public int getid() { return id; }
        public int getproject() { return project; }
	public int gettask() { return task; }
        public String getcomments() { return comments; }
        public FormFile getTheFile() { return theFile; }
        //------------------------------------------------------------------
        public String getname() { return name; }
        public int getowner() { return owner; }
        public java.sql.Timestamp getdate() { return date; }
        public int getstatus() { return status; }
        public String getpublished() { return published; }
        public String gettype() { return type; }
        public java.sql.Timestamp getupload() { return upload;}
        public java.math.BigDecimal getsize() { return size;}
        public String getfromPage() { return fromPage; }
	public String getsortColumn() { return sortColumn; }
	public String getsortOrder() { return sortOrder; }        
        public int gettopic() { return topic; }

        public String getTo_replicate() {return to_replicate;}
        public void setTo_replicate(String to_replicate) {
            this.to_replicate = to_replicate;
        }
        
        

        public String getPrefixType() {
            String prefix;
            
            if (type.equalsIgnoreCase("application/msword")) prefix = "doc";
            //else if (type.equalsIgnoreCase("")) prefix = "mdb";
            else if (type.equalsIgnoreCase("application/vnd.ms-powerpoint")) prefix = "ppt"; 
            else if (type.equalsIgnoreCase("application/vnd.ms-excel")) prefix = "xls"; 
            else if (type.equalsIgnoreCase("application/pdf")) prefix = "pdf"; 
            else if (type.equalsIgnoreCase("application/postscript")) prefix = "ai"; 
            //else if (type.equalsIgnoreCase("")) prefix = "eps";
            //else if (type.equalsIgnoreCase("")) prefix = "ttf";
            else if (type.equalsIgnoreCase("image/gif")) prefix = "gif";
            else if (type.equalsIgnoreCase("image/jpeg")) prefix = "jpg";
            else if (type.equalsIgnoreCase("image/pjpeg")) prefix = "jpg";
            else if (type.equalsIgnoreCase("image/png")) prefix = "png";
            //else if (type.equalsIgnoreCase("")) prefix = "psd";
            else if (type.equalsIgnoreCase("text/plain")) prefix = "txt";
            else if (type.equalsIgnoreCase("application/x-javascript")) prefix = "js"; 
            else if (type.equalsIgnoreCase("text/html")) prefix = "html";
            else if (type.equalsIgnoreCase("application/x-httpd-php")) prefix = "php";
            else if (type.equalsIgnoreCase("application/zip")) prefix = "zip";
            //else if (type.equalsIgnoreCase("")) prefix = "rar";
            else prefix = "default";

            return prefix;
            
        }
        
        
        public String getPath(loginData user) {
            String relpath = "";            
            String separator = java.io.File.separator;

            // Get the URL to the upload dir        
            String subdir = TMSConfigurator.getDownloadPath();
            // Si el string no termina con el separador de files, se agrgega
            if (!subdir.endsWith(separator)) subdir = subdir + separator;

            // delete each file entry in DB and your file asociated.
            String fileName = "";

            fileName = getid()+"-"+getname();                            
            if (getproject() > 0 ) {
                subdir = subdir+"PR"+getproject()+separator;
                if (gettask() > 0 ) subdir = subdir+"TK"+gettask()+separator;
                if (gettopic() > 0 ) subdir = subdir+"TP"+gettopic()+separator;
            }

            // make path
            relpath = subdir+fileName;

            return relpath;
        }         

        /**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	 public void reset(ActionMapping mapping, HttpServletRequest request) {
		task = 0;
                topic = 0;
		comments = " ";
		project = 0;                
	}

	 public String toString() {
		 return " task:" + task + " comments:" + comments + " project:" + project ; 
	 }
}