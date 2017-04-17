/*
 * checkFinishedTasks.java
 *
 * Created on 7 de enero de 2005, 03:15 PM
 */

package com.unify.webcenter.tools;

import java.util.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.tools.*;
import com.unify.webcenter.conf.TMSConfigurator;
/**
 *
 * @author  Gerardo Arroyo
 */
public class checkFinishedTasks {
    private projectsBroker projBroker;
    private tasksBroker    taskBroker;
    private teamsBroker    teamBroker;
    private accountsBroker accountBroker;
    
    /** Creates a new instance of checkFinishedTasks */
    public checkFinishedTasks() {
        projBroker = new projectsBroker();
        taskBroker = new tasksBroker();
        teamBroker = new teamsBroker();
        accountBroker= new accountsBroker();
    }
    
    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        projBroker.close();
        taskBroker.close();
        teamBroker.close();
        accountBroker.close();

    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        checkFinishedTasks checker = new checkFinishedTasks();
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("   TMS v3.1 Task Finished Checker");
        System.out.println("-----------------------------------");
        try {
            checker.check();
            
        } catch (Exception e) {
            System.out.println(e.toString());            
            e.printStackTrace(System.out);            
            
        } finally {
            checker.closeBrokers();
        }
        
        System.out.println("Done!");        

    }
    
   /* Metodo que se encarga de revisar todas las tareas y ver cuales estan atrazadas */
    public void check() {
        
        ResourceBundle labels = ResourceBundle.getBundle("ApplicationResources", new Locale(TMSConfigurator.getMailLanguage(), ""));
        // Se traen todos los proyectos activos.
        Iterator e = projBroker.getActiveProjects("name", "ASC");
        projectsData proyecto;
        teamsData    team;
        tasksData    task;
        accountsData account;
        accountsData account_data;
        
        accountsBroker accountBroker= new accountsBroker();
        long         end, ini, dif, margen;
        boolean      atrasado = false;
        Calendar hoy = Calendar.getInstance();
        StringBuffer message = new StringBuffer(""), listaTareas = new StringBuffer();
        sendMail sm = new sendMail();                
        
        // Se revisan todos los proyectos
        while (e.hasNext()) {
            proyecto = (projectsData) e.next();            
            listaTareas = new StringBuffer("");
            System.out.println("CHECKING (" + proyecto.getname() + ")");                        
            atrasado = false;
            // Se traen todas las tareas de ese proyecto que esten terminadas y ademas
            // finalizadas.
            Iterator eTareas = taskBroker.getListByProjectEnded("id", "name", proyecto.getid(), 0);
            account_data= new accountsData();
            account_data=(accountsData)accountBroker.getData(proyecto.getId_account());
            // Se sacan todas las tareas de este proyecto.
            while (eTareas.hasNext()) {
                task = (tasksData) eTareas.next();  
System.out.println("\tCHECKING (" + task.getname() + ")");               
              //  listaTareas.append("\t" + "<a class=\"LinkText2\" href=\""+TMSConfigurator.getMainURL()+"/portalTasks.do?operation=viewGroup&id="+ task.getid()+ "&project="+proyecto.getid()+ "\">"+ task.getid() + " - " +task.getname() +" </a> "+ "<br><br>");
       listaTareas.append("\t" + "<a class=\"LinkText2\" href=\""+account_data.getMain_url()+"/portalTasks.do?operation=viewGroup&id="+ task.getid()+ "&project="+proyecto.getid()+ "\">"+ task.getid() + " - " +task.getname() +" </a> "+ "<br><br>");

                atrasado = true;
            } // While de las tareas
            
            if (atrasado) {
              
                // Para cada proyecto se extra el equipo de trabajo.
                Iterator teamIterator = teamBroker.getListByProjectWithCloseRoleOrClientManager("id", "ASC", 
                    proyecto.getid(), 0);
                // Para cada miembro del equipo que tiene los permisos.
                while (teamIterator.hasNext()) {
                    team = (teamsData) teamIterator.next();
                    account= (accountsData)accountBroker.getData(team.getId_account());
                    System.out.println("\t\tSending to (" +  team.getparentMember().getname() + ")");                      
                    message = new StringBuffer("");
                    message.append("Estimado usuario: " + team.getparentMember().getname() + "<br>");
                    message.append(labels.getString("email.finishedtasksnotification.message")+ "<br><br>");
                    message.append(listaTareas);
                    message.append("<br><b>"+ labels.getString("email.taskfinalized.important").toUpperCase()+"</b>"+" ");      
                    message.append(labels.getString("email.taskfinalized.body")+" ");
                    message.append(labels.getString("common.statusEnd").toUpperCase()+" ");
                    message.append(labels.getString("email.taskfinalized.body1"));
                     message.append("<br><br>"+"Muchas gracias" + "<br><br>");
                     message.append("Esta notificación fue generada por:"+account.getDescription()+ "<br>");
                //    message.append("Para ver su Home Page, visite: "+ "<a class=\"LinkText\" href=\""+TMSConfigurator.getMainURL()+"/portalLogin.do\">"+TMSConfigurator.getMainURL()+"/login.do</a>" );
                    message.append("Para ver su Home Page, visite: "+ "<a class=\"LinkText\" href=\""+account.getMain_url()+"/portalLogin.do\">"+account.getMain_url()+"/login.do</a>" );

                    sm.sendCheckTasks(team.getparentMember().getemail_work(), message.toString());  
                }
            } // if
        } // de los proyectos                               
    }    
    
}
