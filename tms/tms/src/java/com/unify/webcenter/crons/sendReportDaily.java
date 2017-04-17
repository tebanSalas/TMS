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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Clase que se encarga de verificar si las tareas que han terminado han excedido su
 * umbral de tolerancia.
 * @author  Gerardo Arroyo
 */
public class sendReportDaily {

    private configSendReportBroker reportBroker;
    private masterReportsBroker masterReportBroker;
    private schedulesBroker scheduleBroker;
    private accountsBroker accountBroker;

    /** Creates a new instance of checkTasks */
    public sendReportDaily() {
        reportBroker = new configSendReportBroker();
        masterReportBroker = new masterReportsBroker();
        scheduleBroker = new schedulesBroker();
        accountBroker = new accountsBroker();
    }

    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        reportBroker.close();
        masterReportBroker.close();
        scheduleBroker.close();
        accountBroker.close();
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
        sendReportDaily checker = new sendReportDaily();
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 Send Report Daily");
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
        Iterator e = reportBroker.getDailyList("members", "ASC", "D",1);

        Iterator f = null;

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
        String fechaI = df.format(cal.getTime());
        //System.out.println(fechaI);
        //System.out.println(fechaF);
        accountsData accountData = new accountsData();
        

        
        while (e.hasNext()) {
            lista= new ArrayList();
            reportData = new configSendReportData();
            reportData = (configSendReportData) e.next();
            accountData = (accountsData) accountBroker.getData(reportData.getId_account());
            Company = accountData.getCompany();
            lista.clear();
            f = masterReportBroker.getMasterReportByConfiguration(reportData.getMembers(),
                    fechaI,
                    fechaF,
                    "taskid", "ASC", reportData.getId_account());

            
            while (f.hasNext()) {
                scheduleData = new schedulesData();
                dataTask = new tasksData();
                scheduleData = (schedulesData) f.next();
                    
                dataTask = (tasksData) scheduleData.getparentTask();
                System.out.println("A1");
                System.out.println(scheduleData.getparentTask().getparentAssigned().getname());
                System.out.println("A2");
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
                System.out.println("A3");
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
                System.out.println("A4");
                            band = true;
                        }
                System.out.println("A5");
                    }
                    scheduleData.setRealtime_hours(Integer.parseInt(totHour.toString()));
                    scheduleData.setRealtime_minutes(Integer.parseInt(totMin.toString()));

                System.out.println("A6");
                    dataTask.setTotalScheduledHours(new BigDecimal(totHour + "." + totMin));
                    repetidos.add(new Integer(dataTask.getid()));
                    totEst = this.addTime(dataTask.getestimated_time(), totEst);
                    totAcum = this.addTime(dataTask.getactual_time(), totAcum);
                    scheduleData.setparentTask(dataTask);
                    lista.add(scheduleData);
                System.out.println("A7");
                }
            }
            boolean band = false;
            String[] split_min = null;
            String replace_min = "";
                System.out.println("A8");
            while (!band) {
                if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                System.out.println("A9");
                    totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                    replace_min = totGenMin.toString().replace('.', ',');
                    split_min = replace_min.split(",");
                    totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                    totGenMin = new BigDecimal("0." + split_min[1]);
                    totGenMin = totGenMin.multiply(new BigDecimal("60"));
                    if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                System.out.println("A10");
                        replace_min = totGenMin.toString().replace('.', ',');
                        split_min = replace_min.split(",");
                        totGenMin = new BigDecimal(split_min[0]);
                    }
                } else {
                System.out.println("A11");
                    band = true;
                }
            }
            String totalEmpleadas = "00:00";
                System.out.println("A12");
            if (totGenMin.toString().length() == 1) {
                totalEmpleadas = totGenHour + ":0" + totGenMin;
            } else {
                totalEmpleadas = totGenHour + ":" + totGenMin;
            }

                System.out.println("A13");
            // Se regresa al contexto la lista de todas las tareas que satisfacen
            // la consulta estipulada.
            if (!repetidos.isEmpty() ) {
                HSSFWorkbook wb = (HSSFWorkbook) excelFile.excelFile(Company,
                        reportData.getParentMember().getname(), fechaI,
                        fechaF,
                        lista,
                        totalEmpleadas,
                        totEst,
                        totAcum,
                        "es");

                try {

                           //OutputStream out = new OutputStream();
                    FileOutputStream out = new FileOutputStream("Informe_Laboral_Diario_" + reportData.getParentMember().getname() + ".xls");

                    // write the workbook to the output stream,
                    // remembering to close our file
                    wb.write(out);
                    out.flush();
                    out.close();
                    //sm.sendReport("Informe Labores Diario " + reportData.getParentMember().getname(), reportData.getNotificationMember().getemail_work(), out, reportData.getParentMember().getname(), "Diario");
              //      sm.sendReport("Informe Labores Diario " + reportData.getParentMember().getname(), reportData.getNotificationMember().getemail_work(), out, reportData.getParentMember().getname(), "Diario");
                    
                    File theFile2 = new File("Informe_Laboral_Diario_" + reportData.getParentMember().getname() + ".xls");
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
                }

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
                /*  if ((value1 != null || value2 != null || "".equals(value1) || "".equals(value2)) && value1.equals(value2)) {
                salida = new BigDecimal(hour + ".0" + min);
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
