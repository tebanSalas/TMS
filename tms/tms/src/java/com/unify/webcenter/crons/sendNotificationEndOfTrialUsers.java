/*
 * checkTasks.java
 *
 * Created on May 28, 2003, 11:26 AM
 */
package com.unify.webcenter.crons;

import java.util.*;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.tools.excelFile;
import com.unify.webcenter.tools.sendMail;
import com.unify.webcenter.conf.TMSConfigurator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Clase que se encarga de verificar si las tareas que han terminado han excedido su
 * umbral de tolerancia.
 * @author  Gerardo Arroyo
 */
public class sendNotificationEndOfTrialUsers {

    private schedulesBroker scheduleBroker;
    private accountsBroker accountBroker;
    private membersBroker memberBroker;
    /** Creates a new instance of checkTasks */
    public sendNotificationEndOfTrialUsers() {
        scheduleBroker = new schedulesBroker();
        accountBroker = new accountsBroker();
        memberBroker=  new membersBroker();
    }

    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        scheduleBroker.close();
        accountBroker.close();
        memberBroker.close();
    }

    /**
     * @param args the command line arguments
     * El envio de los correos los hace teniendo en cuenta los parametros de la
     * Fecha Inicio y la Fecha Final, en este caso la Fecha Inicio sera la fecha
     * que se ejecuta la clase menos 1 dia y la fecha final seria el dia que se 
     * ejecuta la clase menos 1 dia
     */
    public static void main(String[] args) {
        new Locale("es", "");
        sendNotificationEndOfTrialUsers checker = new sendNotificationEndOfTrialUsers();
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 Send Notification End of Trial");
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

    public void check() {
        sendMail sm = new sendMail();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        ArrayList userList= new ArrayList();
        ArrayList userExpired= new ArrayList();
        
        
        membersData mdata= new membersData();
        Iterator e= accountBroker.getList("name", "ASC");
        
        int days[]= {5,4,3,2,1};
        int days0[]= {-5,-4,-3,-2,-1};
        while (e.hasNext()) {
            
            userList.clear();
            userExpired.clear();
            accountsData adata= new accountsData();
            adata= (accountsData)e.next();
                   
            if (adata.getFinal_trial_date()!=null)
            {
            cal.setTime(adata.getFinal_trial_date());
             if( memberBroker.getList(adata.getId()).hasNext()){
                if (dateComparer(cal, days))
                 {
                    sm.sendNotificationOfUsers(adata, memberBroker.getList(adata.getId()), 0, adata.getMain_url());
                 }
                if (dateComparer(cal, days0))
                 {
                     sm.sendNotificationOfUsers(adata, memberBroker.getList(adata.getId()), 1, adata.getMain_url());
                 }
             }
            }else{
                Iterator e0= memberBroker.getList(adata.getId());
                while (e0.hasNext()){
                    mdata= (membersData)e0.next();
                    cal.setTime(mdata.getExpired_date());
                     if (dateComparer(cal, days)){
                        userList.add(mdata);
                     }
                    
                     if (dateComparer(cal, days0)){
                        userExpired.add(mdata);
                     }
                }
            class fechaComparador implements Comparator {
                Calendar now= Calendar.getInstance(); 
                Calendar cal= Calendar.getInstance(); 
                Calendar cal1= Calendar.getInstance(); 
                  public int compare(Object o1, Object o2) {
                    membersData md1 = (membersData) o1;
                    membersData md2 = (membersData) o2;
                    
                    cal.setTime(md1.getExpired_date());
                    cal1.setTime(md2.getExpired_date());

                    
                    return cal.compareTo(cal1);
                  }
                }
                Collections.sort(userList, new fechaComparador());
                Collections.sort(userExpired, new fechaComparador());
                
                
                if(userList.size()!=0){
                sm.sendNotificationOfUsers(adata, userList.iterator(), 0, adata.getMain_url());}
                
                if (userExpired.size()!=0){
                sm.sendNotificationOfUsers(adata, userExpired.iterator(), 1, adata.getMain_url());
                }
            }    
        }
    }
    

    
    private boolean dateComparer(Calendar date, int days[])
    {
        Calendar now = Calendar.getInstance();
        boolean flag=false;
        if (date.get(Calendar.YEAR)== now.get(Calendar.YEAR)){
            
            for(int i=0; i<days.length; i++){
                if (date.get(Calendar.DAY_OF_YEAR) == (now.get(Calendar.DAY_OF_YEAR)+days[i]))
                {
                    System.out.println(date.get(Calendar.DAY_OF_YEAR)+ " " +now.get(Calendar.DAY_OF_YEAR)
                            + " "+days[i]);
                    flag=true;
                }
            }
        }
        return flag;
    }
    
    
    
    

    
}
