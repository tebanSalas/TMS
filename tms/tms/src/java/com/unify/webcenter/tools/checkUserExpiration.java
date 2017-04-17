/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.tools;

import com.unify.webcenter.broker.accountsBroker;
import com.unify.webcenter.broker.membersBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.accountsData;
import com.unify.webcenter.data.membersData;
import java.util.*;
import java.text.*;

/**
 *
 * @author MARCELA QUINTERO
 */
public class checkUserExpiration {

    private membersBroker memberBroker;
private accountsBroker accountBroker;
    /** Creates a new instance of checkTasks */
    public checkUserExpiration() {
        memberBroker = new membersBroker();
accountBroker= new accountsBroker();
    }

    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        memberBroker.close();
accountBroker.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        checkUserExpiration checker = new checkUserExpiration();

        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 User Expiration Checker");
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
    static final long ONE_HOUR = 60 * 60 * 1000L;

    public static long daysBetween(Date d1, Date d2) {
        System.out.println(new Date(d1.getTime()));
        System.out.println(new Date(d2.getTime()));
        System.out.println((d2.getTime() - d1.getTime() + ONE_HOUR) /
                (ONE_HOUR * 24));
        return ((d2.getTime() - d1.getTime() + ONE_HOUR) /
                (ONE_HOUR * 24));
    }
    /* Metodo que se encarga de revisar todas las tareas y ver cuales estan atrazadas */

    private void check() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Date date2 = new Date();

        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();

        // Se traen todos los miembros.
        Iterator e = memberBroker.getList("name", "ASC");
        membersData member;
        accountsData account;
        //Calendar hoy = Calendar.getInstance();
        StringBuffer managerBody = new StringBuffer("");
        StringBuffer memberBody = new StringBuffer("");
        sendMail sm = new sendMail();


        while (e.hasNext()) {  
            member = new membersData();
            account = new accountsData();
            member = (membersData) e.next();
            account=(accountsData) accountBroker.getData(member.getId_account());
            
            
            System.out.println("CHECKING (" + member.getname() + -+member.getid() + -+member.getId_account() + ")");

            //String mainURL = TMSConfigurator.getMainURL();
            String mainURL = account.getMain_url();
            managerBody = new StringBuffer("");

            memberBody = new StringBuffer("");

            // Se toma la fecha de hoy y la fecha de expiracion y se calcula la diferencia
            date2 = member.getExpired_date();
            long days = daysBetween(new Date(date.getTime()), new Date(date2.getTime()));
           
            if (days == 1) {
                System.out.println("un dia antes");
                memberBody.append("Cuenta: " + member.getname() + "<br>");
                memberBody.insert(0, "La cuenta esta apunto de expirar le queda " + days + ".<br>");
                sm.sendCheckTasks(member.getemail_work(), memberBody.toString());

                managerBody.append("<BLOCKQUOTE>Miembro: " + member.getname() + "<br>");
                managerBody.insert(0, "La cuenta esta apunto de expirar le queda "+days+".<br>");
                
                sm.sendCheckTasks(account.getEmail(),
                        managerBody.toString());
                  sm.sendCheckTasks(account.getEmail(),
                        managerBody.toString());

            }
            if (days == 0) {
                System.out.println("ultimo dia");
                 memberBody.append("Cuenta: " + member.getname() + "<br>");
                memberBody.insert(0, "La cuenta esta apunto de expirar le queda " + days + ".<br>");
                sm.sendCheckTasks(member.getemail_work(), memberBody.toString());

                managerBody.append("<BLOCKQUOTE>Miembro: " + member.getname() + "<br>");
                managerBody.insert(0, "La cuenta esta apunto de expirar le queda "+days+".<br>");
                
                sm.sendCheckTasks(account.getEmail(),
                        managerBody.toString());
                  sm.sendCheckTasks(account.getEmail(),
                        managerBody.toString());
            }
            if (days == 7) {
                System.out.println("a una semana");
                 memberBody.append("Cuenta: " + member.getname() + "<br>");
                memberBody.insert(0, "La cuenta esta apunto de expirar le queda " + days + ".<br>");
                sm.sendCheckTasks(member.getemail_work(), memberBody.toString());

                managerBody.append("<BLOCKQUOTE>Miembro: " + member.getname() + "<br>");
                managerBody.insert(0, "La cuenta esta apunto de expirar le queda "+days+".<br>");
                
                sm.sendCheckTasks(account.getEmail(),
                        managerBody.toString());
                  sm.sendCheckTasks(account.getEmail(),
                        managerBody.toString());
            }
        /*if (hoy.getTimeInMillis() > end) {
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
        memberBody.append("<BLOCKQUOTE>Tarea (<a href='" + mainURL + "/tasks.do?operation=view&id=" +
        task.getid() + "'>" + task.getname() + "</a>):\tAtrasada<br>" +
        "</BLOCKQUOTE>");
        } else {
        // Se trata de un usuario del cliente
        memberBody.append("<BLOCKQUOTE>Tarea (<a href='" + mainURL + "/portalTasks.do?operation=view&id=" +
        task.getid() + "'>" + task.getname() + "</a>):\tAtrasada<br>" +
        "</BLOCKQUOTE>");
        }
        managerBody.append("<BLOCKQUOTE>Tarea (<a href='" + mainURL + "/tasks.do?operation=view&id=" +
        task.getid() + "'>" + task.getname() + "</a>):\tAtrasada<br>" + "\t Asignada a: " +
        (task.getassigned_to() == 0 ? "No Asignada" : task.getparentAssigned().getname()) +
        "</BLOCKQUOTE>");
        // Se cambia el valor
        atrasado = true;
        } else {
        System.out.println("\t\t\tOK!!");
        }
        // Se le envia el email al miembro del equipo si y solo si esta atrazado
        if (atrasado) {
        // enviar correo al usuario
        memberBody.insert(0, "Las siguientes tareas est&aacute;n atrasadas m&aacute;s all&aacute; del umbral de tolerancia permitido.<br>");
        managerBody.append("</BLOCKQUOTE>");
        sm.sendCheckTasks(team.getparentMember().getemail_work(),
        memberBody.toString());
        }
        // Si alguien del proyecto esta atrazado se envia la lista de cada member
        // al administrador
        if (atrasadoEquipo) {
        managerBody.insert(0, "Las siguientes tareas est&aacute;n atrasadas m&aacute;s all&aacute; del umbral de tolerancia permitido.<br>");
        sm.sendCheckTasks(proyecto.getparentOwner().getemail_work(),
        managerBody.toString());
        }
         */


        // while de miembros


        }
    }
}
