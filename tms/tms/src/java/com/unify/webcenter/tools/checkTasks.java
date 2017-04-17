/*
 * checkTasks.java
 *
 * Created on May 28, 2003, 11:26 AM
 */

package com.unify.webcenter.tools;

import java.util.*;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.tools.*;
import com.unify.webcenter.conf.TMSConfigurator;


/**
 * Clase que se encarga de verificar si las tareas que han terminado han excedido su
 * umbral de tolerancia.
 * @author  Gerardo Arroyo
 */
public class checkTasks {
    private projectsBroker projBroker;
    private tasksBroker    taskBroker;
    private teamsBroker    teamBroker;
    private accountsBroker accountBroker;
    
    /** Creates a new instance of checkTasks */
    public checkTasks() {
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
        checkTasks checker = new checkTasks();
        
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 Task Checker");
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
    private void check() {
        
        // Se traen todos los proyectos activos.
        Iterator e = projBroker.getActiveProjects("name", "ASC");
        projectsData proyecto;
        teamsData    team;
        tasksData    task;
        accountsData account;
        long         end, ini, dif, margen;
        boolean      atrasado = false, atrasadoEquipo = false;
        Calendar hoy = Calendar.getInstance();
        StringBuffer managerBody = new StringBuffer("");
        StringBuffer memberBody = new StringBuffer("");
        sendMail sm = new sendMail();                
        
        
        while (e.hasNext()) {
            proyecto = (projectsData) e.next();
            account=(accountsData) accountBroker.getData(proyecto.getId_account());
            managerBody = new StringBuffer("");
            atrasadoEquipo = false;
            
            // Para cada proyecto se extra el equipo de trabajo.
            Iterator teamIterator = teamBroker.getListByProject("id", "ASC", 
                proyecto.getid(), 0,proyecto.getId_account());
            
            System.out.println("CHECKING (" + proyecto.getname() + - +proyecto.getid() + ")");            

            //String mainURL = TMSConfigurator.getMainURL();
            String mainURL = account.getMain_url();
            // Para cada miembro del equipo
            while (teamIterator.hasNext()) {
                team = (teamsData) teamIterator.next();
                
                memberBody = new StringBuffer("");
                atrasado = false;
                // Se extraen sus tareas asociadas validas y activas.
                Iterator taskIterator = taskBroker.
                        getListByMemberByProjectPublished("name", "ASC", proyecto.getid(),
                        team.getmembers(), 0,team.getId_account());
             
                System.out.println("\tMember (" + team.getparentMember().getname() + ")");                            
                
                //Para cada tarea se analiza si esta excedida.
                while (taskIterator.hasNext()) {
                    task = (tasksData) taskIterator.next();                                        
                    
                    // Solo valen las tareas iniciadas o no iniciadas
                    if (task.getstatus() == 3 || task.getstatus() == 2 ) {
                        
                        System.out.println("\t\tTask (" + task.getname() + ")");            
                        
                        // Se toma la fecha de inicio y fin y se calcula la diferente en tiempo.
                        end = task.getdue_date().getTime();
                        ini = task.getstart_date().getTime();
                        dif= end - ini;  
                        
                        // El margen de retraso seria:
                        //  (diferencia * tolerancia) / 100
                        margen = (dif * task.gettolerance()) / 100;
                        
                        // Se define la maxima fecha permisible                        
                        end = end + margen;
                        if (hoy.getTimeInMillis() > end) {
                            // Ya estas atrasado mas alla del umbral maximo permitido!!
                            System.out.println("\t\t\tAtrasada!!");

                            if (atrasadoEquipo == false) {
                                // Al menos un miembro del equipo esta atrazado.
                                managerBody.append("Proyecto: " + proyecto.getname() + "<br>");
                                atrasadoEquipo = true;
                            }
                            
                            
                            if (atrasado == false) {
                                // Este member no estaba atrasado, se agrega el encabezado para
                                // su email.                                
                                memberBody.append("Proyecto: " + proyecto.getname() + "<br>");
                                managerBody.append("<BLOCKQUOTE>Miembro: " + team.getparentMember().getname() + "<br>");                                
                            }      
                            

                            if (team.getparentMember().getprofile().equals("0") ||
                                team.getparentMember().getprofile().equals("1")) {
                                // Siempre el mensaje de atrazo, pero no es un usuario externo
                                memberBody.append("<BLOCKQUOTE>Tarea (<a href='"+ mainURL 
                                    + "/tasks.do?operation=view&id=" +
                                    task.getid() + "'>" + task.getname() + "</a>):\tAtrasada<br>"+
                                    "</BLOCKQUOTE>");
                            } else {
                                // Se trata de un usuario del cliente
                                memberBody.append("<BLOCKQUOTE>Tarea (<a href='"+ mainURL 
                                    + "/portalTasks.do?operation=view&id=" +
                                    task.getid() + "'>" + task.getname() + "</a>):\tAtrasada<br>"+
                                    "</BLOCKQUOTE>");                                
                            }
                            
                            managerBody.append("<BLOCKQUOTE>Tarea (<a href='"+ mainURL 
                                + "/tasks.do?operation=view&id=" +
                                task.getid() + "'>" + task.getname() + "</a>):\tAtrasada<br>" + "\t Asignada a: " +
                                (task.getassigned_to() == 0 ? "No Asignada" : task.getparentAssigned().getname()) +                                
                                "</BLOCKQUOTE>");
                            
                            // Se cambia el valor
                            atrasado = true;
                        } else {
                            System.out.println("\t\t\tOK!!");
                        }
                    }
                } // Tareas
                
                // Se le envia el email al miembro del equipo si y solo si esta atrazado
                if (atrasado) {
                    // enviar correo al usuario
                    memberBody.insert(0, "Las siguientes tareas est&aacute;n atrasadas m&aacute;s all&aacute; del umbral de tolerancia permitido.<br>");
                    managerBody.append("</BLOCKQUOTE>");
                    sm.sendCheckTasks(team.getparentMember().getemail_work(), 
                        memberBody.toString());                    
                    
                }
            } // del equipo
            
            // Si alguien del proyecto esta atrazado se envia la lista de cada member
            // al administrador
            if (atrasadoEquipo) {
                managerBody.insert(0, "Las siguientes tareas est&aacute;n atrasadas m&aacute;s all&aacute; del umbral de tolerancia permitido.<br>");
                sm.sendCheckTasks(proyecto.getparentOwner().getemail_work(), 
                        managerBody.toString());                    
                
            }

        } // while de proyectos
                   
    }
}
