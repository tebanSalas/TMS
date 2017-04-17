/*
 * checkOpenTickets.java
 *
 * Created on 15 de marzo de 2005, 04:18 PM
 */

package com.unify.webcenter.tools;

import java.util.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.tools.*;
import com.unify.webcenter.conf.TMSConfigurator;

/**
 * Clase que se encarga de llevar a cabo la verificacion de tickets que esten abiertos y no 
 * convertidos en tareas.
 * @author  Gerardo Arroyo
 */
public class checkOpenTickets {
    private topicsBroker topics;
    private projectsBroker projBroker;
    private accountsBroker accountBroker;
    
    /** Creates a new instance of checkOpenTickets */
    public checkOpenTickets() {
        topics = new topicsBroker();
        projBroker = new projectsBroker();
        accountBroker= new accountsBroker();
    }
    
    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        topics.close();
        projBroker.close();
        accountBroker.close();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        checkOpenTickets checker = new checkOpenTickets();
        
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 Tickets  Checker");
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
        
        // Se traen todos los proyectos activos.       
        Iterator e = projBroker.getActiveProjects("name", "ASC");
        projectsData proyecto;
        topicsData   topic;
        accountsData account;
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
            account=(accountsData) accountBroker.getData(proyecto.getId_account());
            // Se traen todas las tareas de ese proyecto que esten terminadas y ademas
            // finalizadas.
            Iterator eTopics = topics.getListByProjectOpen("id", "ASC", proyecto.getid());
            
            // Se sacan todas las tareas de este proyecto.
            while (eTopics.hasNext()) {
                topic = (topicsData) eTopics.next();  
System.out.println("\tAtrasada (" + topic.getsubject() + ")");     
                listaTareas.append("<li><em>Proyecto: " + proyecto.getname() + 
              //      "</em> Ticket: " + " <a class=\"LinkText2\" href=\""+TMSConfigurator.getMainURL()+"/topics.do?operation=view&id="+ topic.getid()+ "\">" +topic.getsubject() +"</a>"+ "</li>");
                      "</em> Ticket: " + " <a class=\"LinkText2\" href=\""+account.getMain_url()+"/topics.do?operation=view&id="+ topic.getid()+ "\">" +topic.getsubject() +"</a>"+ "</li>");
                atrasado = true;
            } // While de las tareas
            
            if (atrasado) {              
                message = new StringBuffer("");
                message.append("Estimado usuario: <strong>" + proyecto.getparentOwner().getname() + "</strong><br>");
                message.append("Los siguientes tickets estan abiertos y no han sido convertidos en tareas :</p>");
                message.append("<ul>");
                message.append(listaTareas);
                message.append("</ul>");
                message.append("<br>Por favor proceda con su revision.");                               
                sm.sendCheckTickets(proyecto.getparentOwner().getemail_work(), message.toString());  
 
            } // if
            
        } // de los proyectos
                                          
    }        
}
