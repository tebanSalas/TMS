/*
 * importMPX.java
 *
 * Created on June 17, 2003, 11:22 AM
 */

package com.unify.webcenter.tools;

import com.tapsterrock.mpp.MPPFile;
import com.tapsterrock.mpx.MPXFile;
import com.tapsterrock.mpx.Task;


import java.util.*;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.form.*;


/**
 *
 * @author  Administrator
 */
public class importMPX {

    private static tasksBroker tb;    
    private static projectsBroker pb;
    private static membersBroker mb;
    private static teamsBroker teamsbroker;
    
    /** Creates a new instance of importMPX */
    public importMPX(String filename, String projectname) {
	//tb = new tasksBroker();
        //pb = new projectsBroker();
    }
    
    /**
     * @param args the command line arguments
     */
   public static void main (String[] args)
   {
      try
      {
         if (args.length != 3)
         {
            System.out.println ("Usage: importMPX <input file name> <description> <user name>");
         }
         else
         {
            query (args[0], args[1], args[2]);
         }
      }

      catch (Exception ex)
      {
         ex.printStackTrace(System.out);
      }
   }

   /**
    * This method performs a set of queries to retrieve information
    * from the an MPP or an MPX file.
    *
    * @param filename name of the MPX file
    * @throws Exception on file read error
    */
   private static void query (String filename, String projectname, String username)
      throws Exception
   {
      MPXFile mpx = null;
      
      try
      {
         mpx = new MPXFile (filename);
      }
      
      catch (Exception ex)
      {
         mpx = null;         
      }         
 
      if (mpx == null)
      {
         try
         {
            mpx = new MPPFile (filename);
         }
      
         catch (Exception ex)
         {
            mpx = null;         
         }                  
      }

      if (mpx == null)
      {
         throw new Exception ("Failed to read file");   
      }
    
      tb = new tasksBroker();
      pb = new projectsBroker();
      mb = new membersBroker();
      teamsbroker = new teamsBroker();
      
      membersData member = mb.getMemberByLogin(username);
      int id = member.getid();
      
      if (id == 0) {
          throw new Exception ("invalid username");   
      }
      
      Calendar calendar = Calendar.getInstance();
      
      projectsData pd = new projectsData();
      pd.setid(0);
      pd.setdescription("<< CAMBIAR ESTA DESCRIPCION >>");
      pd.setname(projectname);
      pd.setorganization(1);
      pd.setowner(id);
      pd.setstatus(2);
      pd.setcreated(new java.sql.Timestamp(calendar.getTimeInMillis())); 
      pd.setmodified(new java.sql.Timestamp(calendar.getTimeInMillis())); 
      pd.setpublished("0");
      pd.setpublished_assigned("0");
      pd.setpublished_endtask("0");
      pd.setupload_max("0");
      
      // Grabar el projecto
      pb.add(pd);
      
      // poner al usuario en el equipo de trabajo
      teamsData teamdata = new teamsData();
      teamdata.setid(0);
      teamdata.setauthorized("0");
      teamdata.setmembers(id);
      teamdata.setprojects(pd.getid());
      teamdata.setpublished("0");          
      teamsbroker.add(teamdata);
      
      // Insertar las tareas
      SimpleDateFormat df = new SimpleDateFormat ("dd/MM/yyyy hh:mm z");
      List allTasks = mpx.getAllTasks();
      Iterator iter = allTasks.iterator();
      Task task;

      tasksData newTask;
      java.sql.Timestamp today;
      
      while (iter.hasNext() == true)
      {
         task = (Task)iter.next();
         //System.out.println ("Task: " + task.getName() + " (" + df.format(task.getStart()) + " - " + df.format(task.getFinish()) + ")");
         if (task.getChildTasks().size() == 0) {
             // crear una nueva instancia
             newTask = new tasksData();
             today = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
             // llenar los datos
             newTask.setproject(pd.getid());
             newTask.setname(task.getName());
             newTask.setdescription(task.getNotes());
             newTask.setfare(new BigDecimal(0));
             newTask.setowner(id);
             newTask.setpredecessor(0);
             newTask.setpredecessor_required("0");
             newTask.setpriority(0);
             newTask.settolerance(0);
             newTask.setassigned_to(id);
             newTask.setactual_time(new BigDecimal(0));
             newTask.setcompletion(0);
             newTask.setpublished("0");
             newTask.setstatus(2);
             newTask.settopic(0);
             newTask.setdue_date(new java.sql.Timestamp(task.getFinish().getTime()));
             newTask.setstart_date(new java.sql.Timestamp(task.getStart().getTime()));
             newTask.setreal_due_date(today);
             newTask.setestimated_time(new BigDecimal(0));
             newTask.setcomments(task.getText1()+task.getText2()+task.getText3()+task.getText4());
             newTask.setcollect("0");
             newTask.setmessage(1);
             newTask.setcreated(today);
             newTask.setmodified(today);
             newTask.setassigned(today);     
             
             tb.add(newTask);
         }
         
      }      
   }



   /**
    * This method lists all tasks defined in the file.
    *
    * @param file MPX file
    */
   private static void listTasks (MPXFile file)
   {
      SimpleDateFormat df = new SimpleDateFormat ("dd/MM/yyyy hh:mm z");
      List allTasks = file.getAllTasks();
      Iterator iter = allTasks.iterator();
      Task task;

      while (iter.hasNext() == true)
      {
         task = (Task)iter.next();
         System.out.println ("Task: " + task.getName() + " (" + df.format(task.getStart()) + " - " + df.format(task.getFinish()) + ")");
      }
      System.out.println ();
   }


   /**
    * This method lists all tasks defined in the file in a hierarchical
    * format, reflecting the parent-child relationships between them.
    *
    * @param file MPX file
    */
   private static void listHierarchy (MPXFile file)
   {
      List tasks = file.getChildTasks();
      Iterator iter = tasks.iterator();
      Task task;

      while (iter.hasNext() == true)
      {
         task = (Task)iter.next();
         System.out.println ("Task: " + task.getName());
         listHierarchy (task, " ");
      }

      System.out.println ();
   }

   /**
    * Helper method called recursively to list child tasks.
    *
    * @param task task whose children are to be displayed
    * @param indent whitespace used to indent hierarchy levels
    */
   private static void listHierarchy (Task task, String indent)
   {
      List tasks = task.getChildTasks(); 
      Iterator iter = tasks.iterator();
      Task child;

      while (iter.hasNext() == true)
      {
         child = (Task)iter.next();
         System.out.println (indent + "Task: " + child.getName());
         listHierarchy (child, indent + " ");
      }
   }

    /* Metodo que se encarga de cerrar todos los brokers creados 
     */
    private void closeBrokers() {
       tb.close();
       pb.close();
    }
   
// End class   
}

