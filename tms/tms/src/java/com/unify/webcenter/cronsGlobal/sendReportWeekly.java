/*
 * checkTasks.java
 *
 * Created on May 28, 2003, 11:26 AM
 */
package com.unify.webcenter.cronsGlobal;

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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Clase que se encarga de verificar si las tareas que han terminado han excedido su
 * umbral de tolerancia.
 * @author  Gerardo Arroyo
 */
public class sendReportWeekly {

    private configSendReportBroker reportBroker;
    private masterReportsBroker masterReportBroker;
    private schedulesBroker scheduleBroker;
    private accountsBroker accountBroker;
 private reportsBroker rbroker;
    /** Creates a new instance of checkTasks */
    public sendReportWeekly() {
        reportBroker = new configSendReportBroker();
        masterReportBroker = new masterReportsBroker();
        scheduleBroker = new schedulesBroker();
        accountBroker = new accountsBroker();
         rbroker = new reportsBroker();
    }

    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        reportBroker.close();
        masterReportBroker.close();
        scheduleBroker.close();
        accountBroker.close();
          rbroker.close();
    }

    /**
     * @param args the command line arguments
     * El envio de los correos los hace teniendo en cuenta los parametros de la
     * Fecha Inicio y la Fecha Final, en este caso la Fecha Inicio sera la fecha
     * que se ejecuta la clase menos 8 dias y la fecha final seria el dia que se 
     * ejecuta la clase menos 1 dia
     */
    public static void main(String[] args) {
          new Locale("es", "");
        sendReportWeekly checker = new sendReportWeekly();
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 Send Report Weekly");
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
        sendMail sm = new sendMail();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        configSendReportData reportData = new configSendReportData();
        String Company = null;
        Iterator e = reportBroker.getDailyList("members", "ASC", "S",2);

        Iterator f = null;
  Iterator t = null;

        schedulesData scheduleData;
        tasksData dataTask;

        ArrayList lista = new ArrayList();

        BigDecimal totGenHour = new BigDecimal("0");
        BigDecimal totGenMin = new BigDecimal("0");

        BigDecimal totEst = new BigDecimal("0");
        BigDecimal totAcum = new BigDecimal("0");

        BigDecimal totHour = new BigDecimal("0");
        BigDecimal totMin = new BigDecimal("0");


        ArrayList repetidos = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String fechaF = df.format(cal.getTime());
        System.out.println(fechaF);
        cal.add(Calendar.DATE, -7);
        String fechaI = df.format(cal.getTime());
        System.out.println(fechaI);
        accountsData accountData = new accountsData();

        while (e.hasNext()) {
            lista= new ArrayList();
            reportData = new configSendReportData();
            reportData = (configSendReportData) e.next();
            accountData = (accountsData) accountBroker.getData(reportData.getId_account());
            Company = accountData.getCompany();
            f = masterReportBroker.getMasterReportByConfiguration(reportData.getMembers(),
                    fechaI,
                    fechaF,
                    "taskid", "ASC", reportData.getId_account());
                        repetidos = new ArrayList();
            lista=new ArrayList();
              totGenHour = new BigDecimal("0");
            totGenMin = new BigDecimal("0");
            totEst = new BigDecimal("0");
            totAcum = new BigDecimal("0");
            totHour = new BigDecimal("0");
            totMin = new BigDecimal("0");
            while (f.hasNext()) {
                scheduleData = new schedulesData();
                dataTask = new tasksData();
                scheduleData = (schedulesData) f.next();
                dataTask = (tasksData) scheduleData.getparentTask();

                if (!repetidos.contains(new Integer(dataTask.getid()))) {
                    scheduleBroker = new schedulesBroker();
                    totHour = scheduleBroker.getTotalTaskRealHoursbyDate(dataTask.getid(), fechaI, fechaF, dataTask.getId_account());
                    totMin = scheduleBroker.getTotalTaskRealMinutesbyDate(dataTask.getid(), fechaI, fechaF, dataTask.getId_account());
                    scheduleBroker.close();
                    totGenHour = totGenHour.add(totHour);
                    totGenMin = totGenMin.add(totMin);
                    boolean band = false;
                    String[] split_min = null;
                    String replace_min = "";
                    while (!band) {
                        if (totMin.compareTo(new BigDecimal("59")) == 1) {
                            totMin = totMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totHour = totHour.add(new BigDecimal(split_min[0]));
                            totMin = new BigDecimal("0." + split_min[1]);
                            totMin = totMin.multiply(new BigDecimal("60"));
                            if (totMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }
                    scheduleData.setRealtime_hours(Integer.parseInt(totHour.toString()));
                    scheduleData.setRealtime_minutes(Integer.parseInt(totMin.toString()));

                    dataTask.setTotalScheduledHours(new BigDecimal(totHour + "." + totMin));
                    repetidos.add(new Integer(dataTask.getid()));
                    totEst = this.addTime(dataTask.getestimated_time(), totEst);

                    totAcum = this.addTime(dataTask.getactual_time(), totAcum);
                    scheduleData.setparentTask(dataTask);
                    lista.add(scheduleData);
                }
            }
            boolean band = false;
            String[] split_min = null;
            String replace_min = "";
            while (!band) {
                if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                    totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                    replace_min = totGenMin.toString().replace('.', ',');
                    split_min = replace_min.split(",");
                    totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                    totGenMin = new BigDecimal("0." + split_min[1]);
                    totGenMin = totGenMin.multiply(new BigDecimal("60"));
                    if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                        replace_min = totGenMin.toString().replace('.', ',');
                        split_min = replace_min.split(",");
                        totGenMin = new BigDecimal(split_min[0]);
                    }
                } else {
                    band = true;
                }
            }
            String totalEmpleadas = "00:00";
            if (totGenMin.toString().length() == 1) {
                totalEmpleadas = totGenHour + ":0" + totGenMin;
            } else {
                totalEmpleadas = totGenHour + ":" + totGenMin;
            }
  t = rbroker.getMasterReportByTasks(reportData.getMembers(),
                    "name", "ASC", reportData.getId_account(), repetidos);
            while (t.hasNext()) {
                scheduleData = new schedulesData();
                dataTask = new tasksData();
                dataTask = (tasksData) t.next();
                System.out.println(dataTask.getid());
                System.out.println(dataTask.getname());
                System.out.println("***");
                totEst = this.addTime(dataTask.getestimated_time(), totEst);
                totAcum = this.addTime(dataTask.getactual_time(), totAcum);
                scheduleData.settaskid(dataTask.getid());
                scheduleData.setparentTask(dataTask);
                scheduleData.setAuxRealtimeMinutes("0");
                scheduleData.setHour_end("0");
                scheduleData.setHour_start("0");
                scheduleData.setId_account(dataTask.getId_account());
                scheduleData.setRealtime_hours(0);
                scheduleData.setRealtime_minutes(0);
                scheduleData.setcomments("-");
                scheduleData.setdate("-");
                scheduleData.sethourid(0);
                scheduleData.setuserid(dataTask.getassigned_to());
                lista.add(scheduleData);
            }
          

            if (!lista.isEmpty()) {
                // Se regresa al contexto la lista de todas las tareas que satisfacen
                // la consulta estipulada.

                HSSFWorkbook wb = (HSSFWorkbook) excelFile.excelFile(Company,
                        reportData.getParentMember().getname(), fechaI,
                        fechaF,
                        lista,
                        totalEmpleadas,
                        totEst,
                        totAcum,
                        "es");

                try {
                     FileOutputStream out = new FileOutputStream("Informe_Laboral_Semanal_"+reportData.getParentMember().getname()+".xls");
                   
                    // write the workbook to the output stream,
                    // remembering to close our file
                    wb.write(out);
                    out.flush();
                    out.close();
                    sm.sendReport("Informe Labores Semanal " + reportData.getParentMember().getname(), reportData.getNotificationMember().getemail_work(), out,reportData.getParentMember().getname(),"Semanal");
                    File theFile2 = new File("Informe_Laboral_Semanal_"+reportData.getParentMember().getname()+".xls");
                    theFile2.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public java.math.BigDecimal addTime(BigDecimal time, BigDecimal tot) {
        BigDecimal salida = new BigDecimal(0);
        // Para evitar una division por 0
        if (time.compareTo(new BigDecimal(0.0)) == 0 ||
                time.compareTo(new BigDecimal(0)) == 0 ||
                time.compareTo(new BigDecimal(0.0)) == 0 ||
                time.compareTo(new BigDecimal(0)) == 0) {
            salida = tot;
        } else {
            System.out.println("---");
            System.out.println(time);
            System.out.println(tot);
            BigDecimal hour = new BigDecimal(0);
            BigDecimal min = new BigDecimal(0);
            BigDecimal hourtot = new BigDecimal(0);
            BigDecimal mintot = new BigDecimal(0);
            String value1 = null;
            String value2 = null;
            if (time.toString().replace('.', ',').split(",").length > 1) {
                String[] split_min = null;
                String[] split_mintot = null;
                String replace_min = time.toString().replace('.', ',');
                split_min = replace_min.split(",");
                hour = new BigDecimal(split_min[0]);
                min = new BigDecimal(split_min[1]);
                if (split_min[1].toString().length() == 1) {
                    min = min.multiply(new BigDecimal(10));
                } else {
                    value1 = split_min[1].toString();
                    value2 = "0" + min;
                }

                if (tot.toString().replace('.', ',').split(",").length > 1) {
                    String replace_mintot = tot.toString().replace('.', ',');
                    split_mintot = replace_mintot.split(",");
                    hourtot = new BigDecimal(split_mintot[0]);
                    mintot = new BigDecimal(split_mintot[1]);
                    if (split_mintot[1].toString().length() == 1) {
                        mintot = mintot.multiply(new BigDecimal(10));
                    }
                }else
                    hourtot=tot;


                min = min.add(mintot);
                hour = hour.add(hourtot);

                boolean band = false;
                while (!band) {

                    if (min.compareTo(new BigDecimal("59")) == 1) {
                        min = min.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                        replace_min = min.toString().replace('.', ',');
                        split_min = replace_min.split(",");
                        hour = hour.add(new BigDecimal(split_min[0]));
                        min = new BigDecimal("0." + split_min[1]);
                        min = min.multiply(new BigDecimal("60"));
                        if (min.toString().replace('.', ',').split(",").length > 1) {
                            replace_min = min.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            min = new BigDecimal("0" + split_min[0]);
                        }
                    } else {
                        band = true;
                    }
                }
                /* if ((value1 != null || value2 != null || "".equals(value1) || "".equals(value2)) && value1.equals(value2)) {
                if (min.toString().length() == 1) {
                salida = new BigDecimal(hour + ".0" + min);
                } else {
                salida = new BigDecimal(hour + "." + min);
                }
                } else {*/
                if (min.toString().length() == 1) {
                    salida = new BigDecimal(hour + ".0" + min);
                } else {
                    salida = new BigDecimal(hour + "." + min);
                }
            //}
            } else {
                salida = tot.add(time);
            }
        }

        return salida;
    }
}
