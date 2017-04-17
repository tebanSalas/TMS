/*
 * checkSchedules.java
 *
 * Created on April 24, 2003, 10:23 AM
 */

package com.unify.webcenter.tools;

import java.util.*;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.tools.*;
import com.unify.webcenter.conf.TMSConfigurator;
import java.util.ArrayList;

/**
 * Clase que se encarga de llevar a cabo la revision de las agendas y horarios de los
 * miembros de la compania a fin de determinar si las mismas estan llenas o no.
 * Se envia un email a cada members y un correo sumarizado al gerente o personas designadas
 * para tal finalidad.
 *   Este programa esta disennado para ser ejecutado periodicamente a traves de un cron
 * o software equivalente el dia lunes de cada semana y el ultimo dia del mes
 *
 * @author  Gerardo Arroyo
 */
public class checkSchedules {
    private membersBroker memBroker;
    private schedulesBroker scheBroker;
    private calendarBroker calBroker;
    
    /** Creates a new instance of checkSchedules */
    public checkSchedules() {
        memBroker = new membersBroker();
        scheBroker = new schedulesBroker();
        calBroker  = new calendarBroker();
    }
    
    /**
     * Procede con la revision pertinente, recibe un parametro que determina el 
     * tipo de proceso: mensual o diario
     * @param args the command line arguments
     */
    public static void main(String[] args) {                
        checkSchedules checker = new checkSchedules();
        
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("TMS v9.0 Schedules/Calendar Checker");
        
        // Primeramente se verifica si hay al menos un argumento.
        if (args.length != 1) {
            // Faltan o se han excedido los parametros de rigor.
            System.out.println("  Usage: checkSchedules type ");
            System.out.println("   type:   weekly or monthly ");
            System.out.println("   ie: checkSchedules weekly ");
        } else {            
            try {
                // Viene el parametro correspondiente.
                checker.performCheck(args[0]);            
            } catch(Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            } finally {
                checker.closeBrokers();
            }
        }            
        
        System.out.println("Done");
    }
    
    // Metodo principal que lleva a cabo la verificacion de los horarios y las agendas
    private void performCheck(String type) {
        // TODO: TOMAR EL CUENTA EL PARAMETRO PARA EL PROCESO DE CHECKING.        
        
        // La fecha actual.
        Calendar fecInicio = Calendar.getInstance();
        Calendar fecFinHorario = Calendar.getInstance();
        Calendar fecFinAgenda  = Calendar.getInstance();
        
        // De acuerdo al tipo de ejecucion, se mnodifican las fechas finales de 
        // cada tipo.
        if (type.equals("weekly")) {
            fecFinHorario.add(Calendar.DATE,  -7);
            fecFinAgenda.add(Calendar.DATE,  7);
        } else {
            // Cuando se trata de mensual, se regresa y avanza un mes
            fecFinHorario.add(Calendar.MONTH,  -1);
            fecFinAgenda.add(Calendar.MONTH,  1);         
        }
        
        
        // Se obtiene el listado de todos los members que son sujetos a esta revision
        Iterator e = memBroker.getListofCheckSchedulesMembers();
        membersData data;
        Calendar indexDate; 
        Iterator count; // Usado para saber si hay al menos una
        ArrayList temp;
        String fecha, msgTmp;
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");                                

        // Manejo de emails
        sendMail sm = new sendMail();        
        
        // Body del email al member y al administrador general
        StringBuffer memberBody = new StringBuffer("");
        StringBuffer managerBody = new StringBuffer("");
        
        ArrayList account = new ArrayList();
        ArrayList accountData = new ArrayList();
        while (e.hasNext()) {
            data = (membersData) e.next();
            
            // Se resetea el contenido de ese string buffer a fin de evitar
            // acumulacion de textos.
            memberBody = new StringBuffer("");
            
            System.out.println("CHECKING (" + data.getname() + ")");
            managerBody.append("\n" + data.getname() + "\n");
            
            // Para cada dia previo a hoy, verifico que haya al menos una entrada
            // en los horarios
            indexDate = (Calendar) fecFinHorario.clone();
            System.out.println(" Schedules");
            managerBody.append("Horarios\n");
            memberBody.append("Horarios\n");
            int dayOfWeek;
            while (indexDate.getTime().compareTo(fecInicio.getTime()) < 0) {
                // Mientras la fecha que uso no haya pasado a la final

                // Se toma el dia de la semana
                dayOfWeek = indexDate.get(java.util.Calendar.DAY_OF_WEEK);
                
                // Si no se trata del sabado y el domingo
                if (dayOfWeek != java.util.Calendar.SATURDAY &&
                    dayOfWeek != java.util.Calendar.SUNDAY ) {
                
                    fecha = df.format(indexDate.getTime());

                    // La fecha viene parseada de la forma: yyyy-MM-dd
                    count = scheBroker.getList(data.getid(), fecha,data.getId_account());

                    if (count.hasNext()) {
                        // Hay al menos una entrada, quiere decir que esta bien ese dia
                        System.out.println("   " + fecha+ ": OK");
                        msgTmp = "\t" + fecha + "\t Ok\n";
                    } else {
                        // No hay entrada, ese dia no se lleno
                        System.out.println("   " + fecha+ ": FAILED");
                        msgTmp = "\t" + fecha + "\t Faltante\n";
                    }
                    managerBody.append(msgTmp);
                    memberBody.append(msgTmp);
                }
                               
                // Incremento la fecha.
                indexDate.add(Calendar.DATE,  1);
            }
                        
            System.out.println(" Calendars");
            managerBody.append("\n\nAgenda\n");
            memberBody.append("\n\nAgenda\n");

            // Para cada dia posterior a hoy, verifico que haya al menos una entrada
            // en las agendas
            indexDate = (Calendar) fecInicio.clone();            
            while (indexDate.getTime().compareTo(fecFinAgenda.getTime()) < 0) {
                // Mientras la fecha que uso no haya pasado a la final
                
                // Se toma el dia de la semana
                dayOfWeek = indexDate.get(java.util.Calendar.DAY_OF_WEEK);
                
                // Si no se trata del sabado y el domingo
                if (dayOfWeek != java.util.Calendar.SATURDAY &&
                    dayOfWeek != java.util.Calendar.SUNDAY ) {
                

                    temp = calBroker.getListByDate("day", "ASC", data.getid(),indexDate,data.getId_account());

                    fecha = df.format(indexDate.getTime());

                    if (temp.size() > 0) {
                        // Hay al menos una entrada, quiere decir que esta bien ese dia
                        System.out.println("   " + fecha+ ": OK");
                        msgTmp = "\t" + fecha + "\t Ok\n";
                    } else {
                        // No hay entrada, ese dia no se lleno
                        System.out.println("   " + fecha+ ": FAILED");
                        msgTmp = "\t" + fecha + "\t Faltante\n";
                    }

                    managerBody.append(msgTmp);
                    memberBody.append(msgTmp);
                }
                
                // Incremento la fecha.
                indexDate.add(Calendar.DATE,  1);
            }
            
            // Se procede a enviarle el email a este member.    
            sm.sendCheckSchedules(data.getemail_work(), memberBody.toString());
        
            //Se agrego para poder enviar los correos a los gerentes 
            if (!account.contains(new Integer(data.getId_account()))) {
                account.add(new Integer(data.getId_account()));
                accountData.add(data);
            }
                
        } // de los members
      
        ///////////////////////////////////////////////////////////////////////////////
        accountsData dataAccount = new accountsData();
        loginData user = new loginData();
        //Recorre el vector para enviar el correo a los administradores de cada cuenta
        for(int i=0;i<accountData.size();i++){
            dataAccount = new accountsData();
            user = new loginData();
            dataAccount = (accountsData)accountData.get(i);
            user.setId_account(dataAccount.getId());
        // Email al gerente o gerentes.
            sm.sendCheckSchedules(TMSConfigurator.getManagersEmails(user),
                managerBody.toString());
        }
        ////////////////////////////////////////////////////////////////////////////////
    }
    
    
    // Metodo que cierra los brokers abiertos a fin de liberar los recursos.
    private void closeBrokers() {
        memBroker.close();
        scheBroker.close();
        calBroker.close();
    }
}
