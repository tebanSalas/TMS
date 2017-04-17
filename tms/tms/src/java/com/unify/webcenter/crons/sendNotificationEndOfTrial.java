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
import java.sql.Timestamp;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Clase que se encarga de verificar si las tareas que han terminado han excedido su
 * umbral de tolerancia.
 * @author  Gerardo Arroyo
 */
public class sendNotificationEndOfTrial {

    private schedulesBroker scheduleBroker;
    private accountsBroker accountBroker;
    private membersBroker memberBroker;
    /** Creates a new instance of checkTasks */
    public sendNotificationEndOfTrial() {
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
        sendNotificationEndOfTrial checker = new sendNotificationEndOfTrial();
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
        
        ArrayList userList= new ArrayList();
        
        membersData mdata= new membersData();
        Iterator e= accountBroker.getList("name", "ASC");
        
        membersBroker memberBroker= new membersBroker();
        int days[]= {7,3,1};
        int days0[]= {-5,-3,-1};
        
        while (e.hasNext()) {
            
            accountsData adata= new accountsData();
            adata= (accountsData)e.next();
            Iterator e0= memberBroker.getList(adata.getId());
            int count=0;
            
            while (e0.hasNext()) { count++; userList.add((membersData)e0.next()); }
                   
            if (adata.getFinal_trial_date()!=null)
            {
            
        System.out.println(adata.getFinal_trial_date().toString());
                cal.setTime(adata.getFinal_trial_date());
                for(int i=0; i<3; i++){
                    if (dateComparer(cal, days[i]))
                     {
                        sm.sendNotification(adata, days[i], count, 0, adata.getMain_url());
                     }
                    if (dateComparer(cal, days0[i]))
                     {
                         sm.sendNotification(adata, days[i], count, 1, adata.getMain_url());
                     }
                }
                
                if (dateComparer(cal, -10))
                {
       
                    sm.sendNotification(adata, 10, count, 2, adata.getMain_url());
                }
            
            }
        }
    }
    
    //Se obtiene la fecha de vencimiento más lejana.
    private Calendar maxDate(membersData mdata, ArrayList userList){
        
        Calendar cal= Calendar.getInstance();
        Iterator i = userList.iterator();
                while(i.hasNext())
                {
                    mdata= new membersData();
                    mdata= (membersData)i.next();
                        if (mdata.getExpired_date().before(cal.getTime()))
                        {
                            cal.setTime(mdata.getExpired_date());
                        }
                }
        return cal;
    }
    
    //Se compara la fecha de expiración con respecto a la del servidor dias antes  dias despues.
    private boolean dateComparer(Calendar date, int daysLeft)
    {
        
        Calendar now = Calendar.getInstance();
        boolean flag=false;
        if (date.get(Calendar.YEAR)== now.get(Calendar.YEAR)){
            if (date.get(Calendar.DAY_OF_YEAR) == (now.get(Calendar.DAY_OF_YEAR)+daysLeft))
            {
                System.out.println(date.get(Calendar.DAY_OF_YEAR)+ " " +now.get(Calendar.DAY_OF_YEAR)
                        + " "+daysLeft);
                flag=true;
            }
        }
        return flag;
    }

    
}
