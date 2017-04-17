/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.tools;

import com.unify.webcenter.broker.membersBroker;
import com.unify.webcenter.broker.reportsBroker;
import com.unify.webcenter.broker.type_tasksBroker;
import com.unify.webcenter.broker.tasksBroker;
import com.unify.webcenter.broker.teamsBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.costsData;
import com.unify.webcenter.data.membersData;
import com.unify.webcenter.data.projectsData;
import com.unify.webcenter.data.reportTasksData;
import com.unify.webcenter.data.schedulesData;
import com.unify.webcenter.data.tasksData;
import com.unify.webcenter.data.type_tasksData;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.poi.hssf.usermodel.*;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author MARCELA QUINTERO
 */
public class excelFile {

    //Method to return the status
    public static String getStatus(int value, String language) {
        String status = "";
        switch (value) {
            case 0: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusClientEnd");
            }
            break;
            case 1: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusEnd");
            }
            break;
            case 2: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusNotStarted");
            }
            break;
            case 3: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusStarted");
            }
            break;
            case 4: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusSuspended");
            }
            break;
            case 5: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusQuote");
            }
            break;
            case 6: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusQuoteSended");
            }
            break;
            case 7: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusQuoteRejected");
            }
            break;
            case 8: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusQuoteAcepted");
            }
            break;
            case 9: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusCharge");
            }
            break;
            case 10: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusChargeSended");
            }
            break;
            case 11: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusProcessingCharge");
            }
            break;
            case 12: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.statusRejected");
            }
            break;
            case 13: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.qualitycontrol");
            }
            break;
            case 14: {
                status = java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.standBy");
            }
            break;
        }
        return status;
    }

    //Method to call a excelfile to generate the excel
   /* public static void reportActivitiesExcelFile(String company, String nombre, String fechaI, String fechaF, ArrayList lista, String totalEmpleadas, BigDecimal totEst, BigDecimal totAcum, HttpServletResponse response, String language) {
     new excelFile(company, nombre, fechaI, fechaF, lista, totalEmpleadas, totEst, totAcum, response, language);
     }*/
    public static HSSFWorkbook excelFile(String company, String nombre, String fechaI, String fechaF, ArrayList lista, String totalEmpleadas, BigDecimal totEst, BigDecimal totAcum, String language) {
        Iterator ite = lista.iterator();
        schedulesData data1 = new schedulesData();
        tasksData tdata = new tasksData();
        System.out.print("Q: " + lista.size());
        while (ite.hasNext()) {
            data1 = (schedulesData) ite.next();
            if (!data1.equals(null)) {

                tdata = (tasksData) data1.getparentTask();
                System.out.println("ID: " + data1.getid());
                System.out.println("DESC: " + tdata.getdescription());
                System.out.println("NOMBRE TAREAS: " + tdata.getdescription());
                System.out.println("NAME: " + tdata.getname());
                System.out.println("ASSINGNED: " + tdata.getassigned());
                System.out.println("ASSINGNED TO: " + tdata.getassigned_to());
                System.out.println("-------------------------");
            }
        }

        short rownum;
        // create a new workbook object; note that the workbook
        // and the file are two separate things until the very
        // end, when the workbook is written to the file.
        HSSFWorkbook wb = new HSSFWorkbook();

        // create a new worksheet
        HSSFSheet ws = wb.createSheet();


        // create a row object reference for later use
        HSSFRow r = null;

        // create a cell object reference
        HSSFCell c = null;

        // create two cell styles - formats
        //need to be defined before they are used
        //Style for tittles
        HSSFCellStyle cs1 = wb.createCellStyle();
        //Style for fields like columns or the name or dates
        HSSFCellStyle cs2 = wb.createCellStyle();
        //Style for the data
        HSSFCellStyle cs3 = wb.createCellStyle();
        //Style for tables
        HSSFCellStyle cs4 = wb.createCellStyle();
        HSSFCellStyle cs5 = wb.createCellStyle();
        HSSFCellStyle cs6 = wb.createCellStyle();
        HSSFCellStyle cs7 = wb.createCellStyle();
        //Style for record      
        HSSFCellStyle cs8 = wb.createCellStyle();
        HSSFCellStyle cs9 = wb.createCellStyle();
        HSSFCellStyle cs10 = wb.createCellStyle();

        HSSFCellStyle cs11 = wb.createCellStyle();

        HSSFCellStyle cs12 = wb.createCellStyle();
        HSSFCellStyle cs13 = wb.createCellStyle();

        // create two font objects for formatting
        //Font for tittle
        HSSFFont f1 = wb.createFont();
        //set font 1 to 14 point bold type
        f1.setFontHeightInPoints((short) 14);
        f1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f2 = wb.createFont();
        //set font 2 to 10 point red type
        f2.setFontHeightInPoints((short) 10);
        f2.setBoldweight((short) HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f3 = wb.createFont();
        //set font 2 to 10 point red type
        f3.setFontHeightInPoints((short) 10);
        f3.setColor((short) HSSFFont.COLOR_NORMAL);

        //Font for the rest of information
        HSSFFont f4 = wb.createFont();
        //set font 2 to 10 point red type
        f4.setFontHeightInPoints((short) 8);
        f4.setColor((short) HSSFFont.COLOR_NORMAL);

        //for cell style 1, use font 1 and set data format
        cs1.setFont(f1);
        cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 2, use font 2, set a thin border, text format
        cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs2.setFont(f2);

        //for cell style 3, use font 3, text format
        cs3.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs3.setFont(f3);

        //for cell style 11, use font 3, text format
        cs11.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs11.setFont(f2);
        cs11.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 11, use font 3, text format
        cs12.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs12.setFont(f4);
        cs12.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //for cell style 3, use font 3, text format
        cs4.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs4.setFont(f2);
        cs4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs4.setWrapText(true);

        //for cell style 3, use font 3, text format
        cs5.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs5.setFont(f2);
        cs5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs5.setBorderTop(HSSFCellStyle.BORDER_THIN);

        cs6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderTop(HSSFCellStyle.BORDER_THIN);

        cs13.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs13.setFont(f2);

        cs7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderTop(HSSFCellStyle.BORDER_THIN);


        cs8.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs8.setFont(f2);
        cs8.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs8.setWrapText(true);

        cs9.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs9.setWrapText(true);

        cs10.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs10.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cs10.setWrapText(true);

        // set the sheet name in Unicode
        wb.setSheetName(0, java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.informeDiarioLabores"),
                HSSFWorkbook.ENCODING_UTF_16);

        //Create new row for the tittle
        r = ws.createRow(0);
        c = r.createCell((short) (5));

        c.setCellStyle(cs1);
        // set the cell's string value to "Test"            
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(company);

        //Create new row for subtittle
        r = ws.createRow(1);
        c = r.createCell((short) (5));
        c.setCellStyle(cs1);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.informeDiarioLabores"));

        //Create new row for the name
        r = ws.createRow(2);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.name") + " :");

        //Create column to assign the name of the member 
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(nombre);

        //Create new row for the dates
        r = ws.createRow(3);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.startDate") + " :");

        //Create column to assign the Initial Date of the report 
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(fechaI);

        r = ws.createRow(4);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.endDate") + " :");

        //Create column to assign the Final Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(fechaF);

        //Create table of the report            
        r = ws.createRow(6);

        c = r.createCell((short) (3));
        c.setCellStyle(cs13);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue("                    " + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.Plan"));
        c = r.createCell((short) (4));
        c.setCellStyle(cs7);

        // set the cell's string value to tittle       
        c = r.createCell((short) (5));
        c.setCellStyle(cs13);
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue("                                 " + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.Ejecucion"));
        c = r.createCell((short) (6));
        c.setCellStyle(cs5);
        c = r.createCell((short) (7));
        c.setCellStyle(cs5);
        c = r.createCell((short) (8));
        c.setCellStyle(cs7);

        // set the cell's string value to tittle

        c = r.createCell((short) (9));
        c.setCellStyle(cs5);
        c = r.createCell((short) (10));
        c.setCellStyle(cs5);
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.Resultado"));
        c = r.createCell((short) (11));
        c.setCellStyle(cs7);

        // set the cell's string value for the data
        ws.setColumnWidth((short) 0, (short) 10000);
        r = ws.createRow(7);
        c = r.createCell((short) (0));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.AreaOperativa"));

        ws.setColumnWidth((short) 1, (short) 2000);
        c = r.createCell((short) (1));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.TaskId"));

        ws.setColumnWidth((short) 2, (short) 10000);
        c = r.createCell((short) (2));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.taskName"));

        /*        ws.setColumnWidth((short) 3, (short) 1000);
         c = r.createCell((short) (3));
         c.setCellStyle(cs4);
         // set the cell's string value to tittle
         c.setEncoding(HSSFCell.ENCODING_UTF_16);
         c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.SE"));
         */
        ws.setColumnWidth((short) 3, (short) 3500);
        c = r.createCell((short) (3));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.deliverDate"));

        ws.setColumnWidth((short) 4, (short) 3800);
        c = r.createCell((short) (4));
        c.setCellStyle(cs4);

        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Estimadas") + " hh:mm");

        ws.setColumnWidth((short) 5, (short) 3800);
        c = r.createCell((short) (5));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Empleadas") + " hh:mm");

        ws.setColumnWidth((short) 6, (short) 3800);
        c = r.createCell((short) (6));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Acumuladas") + " hh:mm");

        ws.setColumnWidth((short) 7, (short) 2500);
        c = r.createCell((short) (7));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.avance"));

        ws.setColumnWidth((short) 8, (short) 3500);
        c = r.createCell((short) (8));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Status"));

        ws.setColumnWidth((short) 9, (short) 3200);
        c = r.createCell((short) (9));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.finalization"));

        ws.setColumnWidth((short) 10, (short) 4000);
        c = r.createCell((short) (10));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.diasDiferencia"));

        ws.setColumnWidth((short) 11, (short) 2500);
        c = r.createCell((short) (11));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.eficiencia"));

        // create a sheet with data rows
        rownum = 8;

        schedulesData data = new schedulesData();
        if (lista.size() > 0) {
            for (int i = 0; i < lista.size(); i++) {
                data = new schedulesData();
                data = (schedulesData) lista.get(i);
                // create a row
                r = ws.createRow(rownum);
                r.setRowNum((short) rownum);
                // create six cells (0-12) to display de data
                c = r.createCell((short) 0);
                c.setCellStyle(cs10);
                c.setCellValue(data.getparentTask().getparentProject().getname());

                c = r.createCell((short) 1);
                c.setCellStyle(cs9);
                c.setCellValue(data.getparentTask().getid());

                c = r.createCell((short) 2);
                c.setCellStyle(cs10);
                c.setCellValue(data.getparentTask().getname());

                /*   c = r.createCell((short) 3);
                 c.setCellStyle(cs9);
                 c.setCellValue("0");*/

                c = r.createCell((short) 3);
                c.setCellStyle(cs9);
                c.setCellValue(data.getparentTask().getFormatedDueDate());

                c = r.createCell((short) 4);
                c.setCellStyle(cs9);
                c.setCellValue(data.getparentTask().formatRealHour(data.getparentTask().getestimated_time()));

                c = r.createCell((short) 5);
                c.setCellStyle(cs9);
                c.setCellValue(String.valueOf(data.getRealtime_hours()) + ":" + data.formatTime(data.getRealtime_minutes()));

                c = r.createCell((short) 6);
                c.setCellStyle(cs9);
                c.setCellValue(data.getparentTask().formatRealHour(data.getparentTask().getactual_time()));

                c = r.createCell((short) 7);
                c.setCellStyle(cs9);
                c.setCellValue(data.getparentTask().getcompletion() + "%");

                c = r.createCell((short) 8);
                c.setCellStyle(cs9);
                c.setCellValue(getStatus(data.getparentTask().getstatus(), language));

                //1. Se visualiza cuando el status de la tarea este en finalizado o finalizado cliente
                if (data.getparentTask().getstatus() == 0 || data.getparentTask().getstatus() == 1) {
                    c = r.createCell((short) 9);
                    c.setCellStyle(cs9);
                    c.setCellValue(data.getparentTask().getFormatedRealDueDate());

                    c = r.createCell((short) 10);
                    c.setCellStyle(cs9);
                    long days = ((data.getparentTask().getdue_date().getTime() - data.getparentTask().getreal_due_date().getTime() + (60 * 60 * 1000L)) / ((60 * 60 * 1000L) * 24));
                    c.setCellValue(days);
                    c = r.createCell((short) 11);
                    c.setCellStyle(cs9);
                    c.setCellValue(data.getparentTask().getEfficiency().toString() + "%");

                } else {
                    //2. Si esta en otro status que no sea finalizado o finalizado clientela resta
                    //la fecha dentrega - sysdate osea la fecha que emite reporte 
                    c = r.createCell((short) 9);
                    c.setCellStyle(cs9);
                    c.setCellValue("-");

                    c = r.createCell((short) 10);
                    c.setCellStyle(cs9);
                    c.setCellValue("-");

                    c = r.createCell((short) 11);
                    c.setCellStyle(cs9);
                    c.setCellValue("-");

                }


                // advance a row
                rownum++;
            }

            // use some formulas
            r = ws.createRow(rownum);
            rownum++;
            c = r.createCell((short) 5);
            c.setCellStyle(cs8);
            c.setCellValue(totalEmpleadas);

            c = r.createCell((short) 4);
            c.setCellStyle(cs8);
            c.setCellValue(data.getparentTask().formatRealHour(totEst));

            c = r.createCell((short) 6);
            c.setCellStyle(cs8);
            c.setCellValue(data.getparentTask().formatRealHour(totAcum));

            r = ws.createRow(rownum++);
            c = r.createCell((short) 0);
            c.setCellStyle(cs11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.DefinicionesFormulas"));


            r = ws.createRow(rownum++);
            c = r.createCell((short) 0);
            c.setCellStyle(cs11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.AreaOperativa"));

            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.AreaOperDef"));
            c.setCellStyle(cs12);

            r = ws.createRow(rownum++);
            c = r.createCell((short) 0);
            c.setCellStyle(cs11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.TaskId"));

            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.taskIdDef"));
            c.setCellStyle(cs12);

            r = ws.createRow(rownum++);
            c = r.createCell((short) 0);
            c.setCellStyle(cs11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.eficiencia"));

            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.EficienciaDef"));
            c.setCellStyle(cs12);

            r = ws.createRow(rownum++);
            c = r.createCell((short) 0);
            c.setCellStyle(cs11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.diasDiferencia"));

            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.diasDifDef"));
            c.setCellStyle(cs12);

        }
        return wb;
    }

    public static HSSFWorkbook excelFileReporteGastos(String company, String fechaI, String fechaF, ArrayList tasksList, ArrayList projectsList, ArrayList costsList, String language, String all,
            String[] members,
            String[] priorities, String[] status, String[] typeTasks, String indFecInicioRango,
            String sortColumnName, String sortOrder,
            String query, String cobrable) {

        short rownum;
        membersData mData = new membersData();
        membersBroker mBroker = new membersBroker();
        reportsBroker broker = new reportsBroker();
        // create a new workbook object; note that the workbook
        // and the file are two separate things until the very
        // end, when the workbook is written to the file.
        HSSFWorkbook wb = new HSSFWorkbook();

        // create a new worksheet
        HSSFSheet ws = wb.createSheet();


        // create a row object reference for later use
        HSSFRow r = null;

        // create a cell object reference
        HSSFCell c = null;

        // create two cell styles - formats
        //need to be defined before they are used
        //Style for tittles
        HSSFCellStyle cs1 = wb.createCellStyle();
        //Style for fields like columns or the name or dates
        HSSFCellStyle cs2 = wb.createCellStyle();
        //Style for the data
        HSSFCellStyle cs3 = wb.createCellStyle();
        //Style for tables
        HSSFCellStyle cs4 = wb.createCellStyle();
        HSSFCellStyle cs5 = wb.createCellStyle();
        HSSFCellStyle cs6 = wb.createCellStyle();
        HSSFCellStyle cs7 = wb.createCellStyle();
        //Style for record
        HSSFCellStyle cs8 = wb.createCellStyle();
        HSSFCellStyle cs9 = wb.createCellStyle();
        HSSFCellStyle cs10 = wb.createCellStyle();

        HSSFCellStyle cs11 = wb.createCellStyle();

        HSSFCellStyle cs12 = wb.createCellStyle();
        HSSFCellStyle cs13 = wb.createCellStyle();

        // create two font objects for formatting
        //Font for tittle
        HSSFFont f1 = wb.createFont();
        //set font 1 to 14 point bold type
        f1.setFontHeightInPoints((short) 14);
        f1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f2 = wb.createFont();
        //set font 2 to 10 point red type
        f2.setFontHeightInPoints((short) 10);
        f2.setBoldweight((short) HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f3 = wb.createFont();
        //set font 2 to 10 point red type
        f3.setFontHeightInPoints((short) 10);
        f3.setColor((short) HSSFFont.COLOR_NORMAL);

        //Font for the rest of information
        HSSFFont f4 = wb.createFont();
        //set font 2 to 10 point red type
        f4.setFontHeightInPoints((short) 8);
        f4.setColor((short) HSSFFont.COLOR_NORMAL);

        //for cell style 1, use font 1 and set data format
        cs1.setFont(f1);
        cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 2, use font 2, set a thin border, text format
        cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs2.setFont(f2);

        //for cell style 3, use font 3, text format
        cs3.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs3.setFont(f3);

        //for cell style 11, use font 3, text format
        cs11.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs11.setFont(f2);
        cs11.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 11, use font 3, text format
        cs12.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs12.setFont(f4);
        cs12.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //for cell style 3, use font 3, text format
        cs4.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs4.setFont(f2);
        cs4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs4.setWrapText(true);

        //for cell style 3, use font 3, text format
        cs5.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs5.setFont(f2);
        cs5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs5.setBorderTop(HSSFCellStyle.BORDER_THIN);

        cs6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderTop(HSSFCellStyle.BORDER_THIN);

        cs13.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs13.setFont(f2);

        cs7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderTop(HSSFCellStyle.BORDER_THIN);


        cs8.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs8.setFont(f2);
        cs8.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs8.setWrapText(true);

        cs9.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs9.setWrapText(true);

        cs10.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs10.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cs10.setWrapText(true);

        // set the sheet name in Unicode

        wb.setSheetName(0, java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.expensesReport"),
                HSSFWorkbook.ENCODING_UTF_16);

        ws.setColumnWidth((short) 0, (short) 500);
        ws.setColumnWidth((short) 1, (short) 12512);
        ws.setColumnWidth((short) 2, (short) 2854);
        ws.setColumnWidth((short) 3, (short) 5565);
        ws.setColumnWidth((short) 4, (short) 3183);
        ws.setColumnWidth((short) 5, (short) 3220);
        ws.setColumnWidth((short) 6, (short) 2049);
        ws.setColumnWidth((short) 7, (short) 2342);
        ws.setColumnWidth((short) 8, (short) 2525);
        ws.setColumnWidth((short) 9, (short) 2525);
        ws.setColumnWidth((short) 10, (short) 2341);
        ws.setColumnWidth((short) 11, (short) 2305);
        ws.setColumnWidth((short) 12, (short) 2552);
        ws.setColumnWidth((short) 13, (short) 2342);
        //Create new row for the tittle
        r = ws.createRow(0);
        c = r.createCell((short) (5));

        c.setCellStyle(cs1);
        // set the cell's string value to "Test"
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(company);

        //Create new row for subtittle
        r = ws.createRow(1);
        c = r.createCell((short) (5));
        c.setCellStyle(cs1);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.expensesReport"));


        //Create new row for the dates
        r = ws.createRow(2);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.startDate") + " :");

        //Create column to assign the Initial Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);

        if (!all.equalsIgnoreCase("ALL")) {
            c.setCellValue(fechaI);
        } else {
            c.setCellValue("TODAS LAS FECHAS");
        }

        r = ws.createRow(4);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.endDate") + " :");

        //Create column to assign the Final Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        if (!all.equalsIgnoreCase("ALL")) {
            c.setCellValue(fechaF);
        } else {
            c.setCellValue("TODAS LAS FECHAS");
        }

        Iterator e = projectsList.iterator();
        DecimalFormat df = new DecimalFormat("#.###");

        double totalesEstimado = 0;
        double totalesCostoReal = 0;
        double totalesUtilidad = 0;
        double totalesCobrableSi = 0;
        double totalesCobrableNo = 0;

        int rownumb = 6;
        double suma_util_proy = 0;
        while (e.hasNext()) {
            System.out.println("ASDASDASDASD2222");
            double costoEstimadoGlobal = 0;
            double costoRealGlobal = 0;
            double utilidadGlobal = 0;
            double cobrableSiGlobal = 0;
            double cobrableNoGlobal = 0;
            suma_util_proy = 0;

            projectsData pData = (projectsData) e.next();
            r = ws.createRow(rownumb);

            //Create column to assign the Final Date of the report


            c = r.createCell((short) (1));
            c.setCellStyle(cs2);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue("Costos Operativos: " + pData.getname());

            rownumb++;
            r = ws.createRow(rownumb);

            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.IdTarea"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 2);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Status"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 3);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.assignedTo"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("tasksForm.estimatedTime.displayName"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 5);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("tasksForm.actualTime.displayName"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.HourCost"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 7);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.EstimatedCost"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 8);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.RealCost"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 9);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Fare"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 10);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.TotalValue"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Income"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 12);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableYes"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 13);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableNo"));
            c.setCellStyle(cs4);

            //Iterator e1= tasksList.iterator();
            Iterator e1 = broker.getProjectReportInformeGastos(pData.getId_account(), pData.getid(),
                    members, priorities, status, typeTasks, indFecInicioRango, fechaI, fechaF, sortColumnName, sortOrder, query, cobrable).iterator();
            rownumb++;
            double sce = 0;
            double scr = 0;
            double su = 0;
            double scs = 0;
            double scn = 0;

            while (e1.hasNext()) {

                tasksData tData = (tasksData) e1.next();


                r = ws.createRow(rownumb);

                //Get ID-Tarea
                c = r.createCell((short) (1));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(tData.getid() + "-" + tData.getname());


                //Get Estado
                c = r.createCell((short) (2));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(getStatus1(tData.getstatus()));


                mData = new membersData();

                //Get Horas Asignado A
                mData = (membersData) mBroker.getData(tData.getassigned_to());
                tData.setparentAssigned(mData);
                c = r.createCell((short) (3));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(mData.getname());

                //Get Horas Estimadas
                c = r.createCell((short) (4));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);


                StringTokenizer st = new StringTokenizer(tData.getestimated_time().toString(), "=.");
                String[] str1 = new String[st.countTokens()];

                int i = 0;
                while (st.hasMoreTokens()) {
                    str1[i] = st.nextToken();
                    i++;
                }

                if (str1.length == 1) {
                    c.setCellValue(str1[0] + ":00");
                } else {

                    c.setCellValue(str1[0] + ":" + str1[1]);
                }

                //Get Horas Acumuladas
                c = r.createCell((short) (5));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);

                st = new StringTokenizer(tData.getactual_time().toString(), "=.");
                String[] str2 = new String[st.countTokens()];

                i = 0;
                while (st.hasMoreTokens()) {
                    str2[i] = st.nextToken();
                    i++;
                }

                if (str2.length == 1) {
                    c.setCellValue(str2[0] + ":00");

                } else {
                    if (str2[1].length() == 1) {
                        String s1 = str2[1];
                        str2[1] = s1 + "0";
                    }
                    c.setCellValue(str2[0] + ":" + str2[1]);
                }


                //Costo por Hora
                c = r.createCell((short) (6));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                if (mData.getCost() != null) {
                    c.setCellValue(mData.getCost().doubleValue());
                }

                //Costo Estimado
                c = r.createCell((short) (7));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(tData.getEstimatedCost().doubleValue() / 10);

                if (mData.getCost() != null && str1.length > 1) {
                    c.setCellValue(mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60)));
                    sce += mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60));
                } else {
                    c.setCellValue(0);
                }

                //Costo Real
                c = r.createCell((short) (8));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);


                if (mData.getCost() != null && str2.length > 1) {
                    c.setCellValue(mData.getCost().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60)));
                    scr += mData.getCost().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60));
                } else {
                    c.setCellValue(0);
                }

                //Costo Tarifa
                c = r.createCell((short) (9));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(tData.getfare().toString());


                //Valor Total=(Costo Estimado*Horas Acumuladas)
                c = r.createCell((short) (10));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);



                if (str1.length > 1 && str2.length > 1 && mData.getCost() != null) {
                    // c.setCellValue((mData.getCost().doubleValue()*(Double.parseDouble(str1[0])+ (Double.parseDouble(str1[1])/60)))*
                    //         (Double.parseDouble(str2[0])+ (Double.parseDouble(str2[1])/60)));

                    if (tData.getcollect().equals("1")) { //Por ejecución: Tarifa * Horas Acumuladas
                        c.setCellValue((tData.getfare().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60))));
                    } else if (tData.getcollect().equals("2"))//Por cotización: Tarifa * Horas Estimadas
                    {
                        c.setCellValue((tData.getfare().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60))));
                    }
                } else {
                    c.setCellValue(0);
                }


                //Utilidad= (Valor Total - Costo Real)
                c = r.createCell((short) (11));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);


                if (str1.length > 1 && str2.length > 1 && mData.getCost() != null) {
                    suma_util_proy = suma_util_proy + ((mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60)))
                            * (Double.parseDouble(str2[0]) + (Double.parseDouble(str1[1]) / 60))) - (mData.getCost().doubleValue()
                            * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60)));

                    c.setCellValue(((mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60)))
                            * (Double.parseDouble(str2[0]) + (Double.parseDouble(str1[1]) / 60))) - (mData.getCost().doubleValue()
                            * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60))));
                } else {
                    c.setCellValue(0);
                }

                if (str1.length > 1 && str2.length > 1 && mData.getCost() != null) {

                    su += (((mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60)))
                            * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60))) - (mData.getCost().doubleValue()
                            * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60))));
                    System.out.println("DETALLE LINEA " + mData.getname() + " , " + tData.getname() + "COSTO: " + mData.getCost().doubleValue() + " OTROS " + Double.parseDouble(str1[0]) + " / " + Double.parseDouble(str1[1]) / 60 + "O TROS 2da " + Double.parseDouble(str2[0]) + " / " + Double.parseDouble(str2[1]) / 60 + " TOTAL " + su);
                } else {
                    c.setCellValue(0);
                }

                //Cobrable Si
                c = r.createCell((short) (12));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                double val = 0;

                if (tData.getcollect().equals("1")) {
                    if (mData.getCost() != null && str1.length > 1) {
                        val = (mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60)));
                        scs += (mData.getCost().doubleValue() * (Double.parseDouble(str1[0]) + (Double.parseDouble(str1[1]) / 60)));
                    } else {
                        val = 0;
                    }

                } else if (tData.getcollect().equals("2")) {
                    if (mData.getCost() != null && str2.length > 1) {
                        val = mData.getCost().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60));
                        scs += mData.getCost().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60));
                    } else {
                        val = 0;
                    }

                }
                c.setCellValue(val);

                //Cobrable No
                c = r.createCell((short) (13));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                double val1 = 0;

                if (tData.getcollect().equals("0") && str2.length > 1 && mData.getCost() != null) {
                    val1 = (mData.getCost().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60)));
                    scn += mData.getCost().doubleValue() * (Double.parseDouble(str2[0]) + (Double.parseDouble(str2[1]) / 60));
                }
                c.setCellValue(val1);
                rownumb++;


            }


            r = ws.createRow(rownumb);
            c = r.createCell((short) 6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Total"));
            c.setCellStyle(cs4);


            c = r.createCell((short) 7);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(sce);
            c.setCellStyle(cs4);

            c = r.createCell((short) 8);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(scr);
            c.setCellStyle(cs4);

            c = r.createCell((short) 11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            //System.out.println("SUMA UTIL "+su);
            //c.setCellValue(su);
            c.setCellValue(suma_util_proy);
            c.setCellStyle(cs4);

            c = r.createCell((short) 12);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(scs);
            c.setCellStyle(cs4);

            c = r.createCell((short) 13);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(scn);
            c.setCellStyle(cs4);

            rownumb = rownumb + 2;
            r = ws.createRow(rownumb);

            c = r.createCell((short) (1));
            c.setCellStyle(cs3);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Costs"));

            rownumb++;
            r = ws.createRow(rownumb);
            c = r.createCell((short) (1));
            c.setCellStyle(cs4);                                        // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.IdTarea"));

            c = r.createCell((short) (2));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Date"));


            c = r.createCell((short) (3));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.assignedTo"));

            c = r.createCell((short) (4));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Reports.DescripcionGasto"));

            c = r.createCell((short) (5));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Units"));

            c = r.createCell((short) (6));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.StandardCost"));

            c = r.createCell((short) (7));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.RealCost"));

            c = r.createCell((short) (8));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.EstimatedCost"));

            c = r.createCell((short) (9));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.RealCost"));

            c = r.createCell((short) (10));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableYes"));

            c = r.createCell((short) (11));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableNo"));
            Iterator e2 = costsList.iterator();

            rownumb++;


            double costoEsti = 0;
            double costoReal = 0;
            double cobrableSi = 0;
            double cobrableNo = 0;

            while (e2.hasNext()) {
                r = ws.createRow(rownumb);
                costsData cData = (costsData) e2.next();

                if (cData.getProject() == pData.getid()) {

                    //Id-Nombre de la Tarea
                    tasksBroker tBroker = new tasksBroker();
                    tasksData tData = (tasksData) tBroker.getData(cData.getTasks());
                    c = r.createCell((short) (1));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(tData.getid() + "-" + tData.getname());

                    //Fecha del Costo
                    Calendar now = Calendar.getInstance();
                    now.setTimeInMillis(cData.getAdditional_costs_date().getTime());
                    String cal = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);

                    c = r.createCell((short) (2));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cal);

                    //Nombre Miembro Asignado a la Tarea
                    mData = (membersData) mBroker.getData(tData.getassigned_to());
                    c = r.createCell((short) (3));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(mData.getname());

                    //Descripción del C
                    c = r.createCell((short) (4));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cData.getDescription());

                    //Unidades
                    c = r.createCell((short) (5));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cData.getUnits());

                    //Costo Estimado Unitario
                    c = r.createCell((short) (6));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cData.getStandard_Cost().doubleValue());

                    //Costo Real Unitario
                    c = r.createCell((short) (7));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cData.getReal_Cost().doubleValue());

                    //Costo Estimado (Costo Estimado Unitario * Unidades)
                    c = r.createCell((short) (8));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cData.getStandard_Cost().doubleValue() * cData.getUnits());
                    costoEsti += (cData.getStandard_Cost().doubleValue() * cData.getUnits());

                    //Costo Real (Costo Real Unitario * Unidades)
                    c = r.createCell((short) (9));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    c.setCellValue(cData.getReal_Cost().doubleValue() * cData.getUnits());
                    costoReal += (cData.getReal_Cost().doubleValue() * cData.getUnits());

                    //Cobrable Si
                    c = r.createCell((short) (10));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);

                    if (!cData.getChargeable().equals("0")) {
                        c.setCellValue(cData.getReal_Cost().doubleValue() * cData.getUnits());
                        cobrableSi += cData.getReal_Cost().doubleValue() * cData.getUnits();

                    } else {
                        c.setCellValue("0");
                    }

                    //Cobrable No
                    c = r.createCell((short) (11));
                    c.setCellStyle(cs3);
                    // set the cell's string value to tittle
                    c.setEncoding(HSSFCell.ENCODING_UTF_16);
                    if (cData.getChargeable().equals("0")) {
                        c.setCellValue(cData.getReal_Cost().doubleValue() * cData.getUnits());
                        cobrableNo += (cData.getReal_Cost().doubleValue() * cData.getUnits());
                    } else {
                        c.setCellValue("0");
                    }
                    rownumb++;
                    mBroker.close();
                    tBroker.close();

                }


            }

            r = ws.createRow(rownumb);
            c = r.createCell((short) 7);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Total"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 8);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(costoEsti);
            c.setCellStyle(cs4);

            c = r.createCell((short) 9);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(costoReal);
            c.setCellStyle(cs4);

            c = r.createCell((short) 10);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(cobrableSi);
            c.setCellStyle(cs4);

            c = r.createCell((short) 11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(cobrableNo);
            c.setCellStyle(cs4);

            rownumb = rownumb + 3;
            r = ws.createRow(rownumb);

            c = r.createCell((short) (1));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.StandardCost"));

            c = r.createCell((short) (2));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);


            costoEstimadoGlobal += sce + costoEsti;
            totalesEstimado += costoEstimadoGlobal;
            c.setCellValue(costoEstimadoGlobal);


            c = r.createCell((short) (3));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.RealCost"));

            totalesCostoReal += scr + costoReal;
            costoRealGlobal += scr + costoReal;
            c = r.createCell((short) (4));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(costoRealGlobal);


            c = r.createCell((short) (5));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Income"));

            //totalesUtilidad+=su-(scn+cobrableNo);
            totalesUtilidad += suma_util_proy - (scn + cobrableNo);
            //utilidadGlobal+=su-(scn+cobrableNo);
            utilidadGlobal += suma_util_proy - (scn + cobrableNo);
            c = r.createCell((short) (6));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(utilidadGlobal);

            c = r.createCell((short) (7));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableYes"));

            totalesCobrableSi += scs + cobrableSi;
            cobrableSiGlobal += scs + cobrableSi;
            c = r.createCell((short) (8));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(cobrableSiGlobal);

            c = r.createCell((short) (9));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableNo"));

            totalesCobrableNo += scn + cobrableNo;
            cobrableNoGlobal += scn + cobrableNo;
            c = r.createCell((short) (10));
            c.setCellStyle(cs4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(cobrableNoGlobal);

            rownumb = rownumb + 3;
        }
        r = ws.createRow(rownumb);

        c = r.createCell((short) (1));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.CostoGlobal"));

        rownumb++;
        r = ws.createRow(rownumb);
        c = r.createCell((short) (1));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.StandardCost"));

        c = r.createCell((short) (2));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalesEstimado);

        c = r.createCell((short) (3));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.report.RealCost"));

        c = r.createCell((short) (4));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalesCostoReal);

        c = r.createCell((short) (5));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Income"));

        c = r.createCell((short) (6));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalesUtilidad);

        c = r.createCell((short) (7));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableYes"));

        c = r.createCell((short) (8));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalesCobrableSi);

        c = r.createCell((short) (9));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.chargeableNo"));

        c = r.createCell((short) (10));
        c.setCellStyle(cs4);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalesCobrableNo);


        return wb;
    }

    public static HSSFWorkbook excelFileInformeTiempoConsolidado(String company, String fechaI, String fechaF, ArrayList projectsList, String language, String all,
            String[] members,
            String[] priorities, String[] status, String[] typeTasks, String indFecInicioRango,
            String sortColumnName, String sortOrder,
            String query) {

        reportsBroker broker = new reportsBroker();
        membersBroker mBroker = new membersBroker();
        short rownum;
        // create a new workbook object; note that the workbook
        // and the file are two separate things until the very
        // end, when the workbook is written to the file.
        HSSFWorkbook wb = new HSSFWorkbook();

        // create a new worksheet
        HSSFSheet ws = wb.createSheet();


        // create a row object reference for later use
        HSSFRow r = null;

        // create a cell object reference
        HSSFCell c = null;

        // create two cell styles - formats
        //need to be defined before they are used
        //Style for tittles
        HSSFCellStyle cs1 = wb.createCellStyle();
        //Style for fields like columns or the name or dates
        HSSFCellStyle cs2 = wb.createCellStyle();
        //Style for the data
        HSSFCellStyle cs3 = wb.createCellStyle();
        //Style for tables
        HSSFCellStyle cs4 = wb.createCellStyle();
        HSSFCellStyle cs5 = wb.createCellStyle();
        HSSFCellStyle cs6 = wb.createCellStyle();
        HSSFCellStyle cs7 = wb.createCellStyle();
        //Style for record
        HSSFCellStyle cs8 = wb.createCellStyle();
        HSSFCellStyle cs9 = wb.createCellStyle();
        HSSFCellStyle cs10 = wb.createCellStyle();

        HSSFCellStyle cs11 = wb.createCellStyle();

        HSSFCellStyle cs12 = wb.createCellStyle();
        HSSFCellStyle cs13 = wb.createCellStyle();

        // create two font objects for formatting
        //Font for tittle
        HSSFFont f1 = wb.createFont();
        //set font 1 to 14 point bold type
        f1.setFontHeightInPoints((short) 14);
        f1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f2 = wb.createFont();
        //set font 2 to 10 point red type
        f2.setFontHeightInPoints((short) 10);
        f2.setBoldweight((short) HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f3 = wb.createFont();
        //set font 2 to 10 point red type
        f3.setFontHeightInPoints((short) 10);
        f3.setColor((short) HSSFFont.COLOR_NORMAL);

        //Font for the rest of information
        HSSFFont f4 = wb.createFont();
        //set font 2 to 10 point red type
        f4.setFontHeightInPoints((short) 8);
        f4.setColor((short) HSSFFont.COLOR_NORMAL);

        //for cell style 1, use font 1 and set data format
        cs1.setFont(f1);
        cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 2, use font 2, set a thin border, text format
        cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs2.setFont(f2);

        //for cell style 3, use font 3, text format
        cs3.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs3.setFont(f3);

        //for cell style 11, use font 3, text format
        cs11.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs11.setFont(f2);
        cs11.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 11, use font 3, text format
        cs12.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs12.setFont(f4);
        cs12.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //for cell style 3, use font 3, text format
        cs4.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs4.setFont(f2);
        cs4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs4.setWrapText(true);

        //for cell style 3, use font 3, text format
        cs5.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs5.setFont(f2);
        cs5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs5.setBorderTop(HSSFCellStyle.BORDER_THIN);

        cs6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs6.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);

        cs13.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs13.setFont(f2);

        cs7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderTop(HSSFCellStyle.BORDER_THIN);


        cs8.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs8.setFont(f2);
        cs8.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs8.setWrapText(true);

        cs9.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs9.setWrapText(true);

        cs10.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs10.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cs10.setWrapText(true);

        // set the sheet name in Unicode
        wb.setSheetName(0, java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.consolidatedTimeReport"),
                HSSFWorkbook.ENCODING_UTF_16);

        ws.setColumnWidth((short) 0, (short) 500);
        ws.setColumnWidth((short) 1, (short) 2107);
        ws.setColumnWidth((short) 2, (short) 11965);
        ws.setColumnWidth((short) 3, (short) 2572);
        ws.setColumnWidth((short) 4, (short) 3322);
        ws.setColumnWidth((short) 5, (short) 2215);
        ws.setColumnWidth((short) 6, (short) 2572);
        ws.setColumnWidth((short) 7, (short) 2822);
        ws.setColumnWidth((short) 8, (short) 3786);
        ws.setColumnWidth((short) 9, (short) 8858);
        ws.setColumnWidth((short) 10, (short) 1510);
        ws.setColumnWidth((short) 11, (short) 2858);

        //Create new row for the tittle
        r = ws.createRow(0);
        c = r.createCell((short) (5));

        c.setCellStyle(cs1);
        // set the cell's string value to "Test"
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(company);

        //Create new row for subtittle
        r = ws.createRow(1);
        c = r.createCell((short) (5));
        c.setCellStyle(cs1);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.consolidatedTimeReport"));


        //Create new row for the dates
        r = ws.createRow(2);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.startDate") + " :");

        //Create column to assign the Initial Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        if (!all.equalsIgnoreCase("ALL")) {
            c.setCellValue(fechaI);
        } else {
            c.setCellValue("TODAS LAS FECHAS");
        }


        r = ws.createRow(4);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.endDate") + " :");

        //Create column to assign the Final Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        if (!all.equalsIgnoreCase("ALL")) {
            c.setCellValue(fechaF);
        } else {
            c.setCellValue("TODAS LAS FECHAS");
        }


        Iterator e = projectsList.iterator();

        int totalHorasProyecto = 0;
        int totalMinutosProyecto = 0;
        double totalValorProyectos = 0;

        System.out.println("aquiXXXXX");
        int rownumb = 6;
        while (e.hasNext()) {
            projectsData pData = (projectsData) e.next();
            r = ws.createRow(rownumb);


            //Create column to assign the Final Date of the report
            c = r.createCell((short) (1));
            c.setCellStyle(cs2);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue("Proyecto: " + pData.getid() + "-" + pData.getname());


            rownumb++;
            r = ws.createRow(rownumb);
            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Id"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 2);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Task"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 3);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.day"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.startHour"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 5);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.endHour"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("tasksForm.estimatedTime.displayName"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 7);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.workedTime"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 8);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.assignedTo"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 9);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("tasksForm.comments.displayName"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 10);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Fare"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.TotalValue"));
            c.setCellStyle(cs4);


            //Iterator e1= schList.iterator();
            Iterator e1 = broker.getProjectReportInformeCostos(pData.getId_account(), pData.getid(), members, priorities, status, typeTasks, indFecInicioRango, fechaI, fechaF, sortColumnName, sortOrder, query).iterator();
            //tasksBroker tskBroker= new tasksBroker();

            rownumb++;
            int horasEmpleadas = 0;
            int minutosEmpleados = 0;
            double total = 0;

            while (e1.hasNext()) {
                schedulesData schData = (schedulesData) e1.next();
                tasksData tskData = (tasksData) schData.getparentTask();//tskBroker.getData(schData.gettaskid()); 


                // if (tskData.getproject()== pData.getid()){

                r = ws.createRow(rownumb);

                //Id
                c = r.createCell((short) (1));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(tskData.getid());


                //Nombre Tarea
                c = r.createCell((short) (2));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(tskData.getname());



                c = r.createCell((short) (3));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(schData.getdate());


                //Hora Inicio
                c = r.createCell((short) (4));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(schData.getHour_start());


                //Hora Final
                c = r.createCell((short) (5));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(schData.getHour_end());


                //Horas Estimadas
                c = r.createCell((short) (6));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);


                StringTokenizer st = new StringTokenizer(tskData.getestimated_time().toString(), "=.");
                String[] str2 = new String[st.countTokens()];

                int i = 0;
                while (st.hasMoreTokens()) {
                    str2[i] = st.nextToken();
                    i++;
                }

                if (str2.length == 1) {
                    c.setCellValue(str2[0] + ":00");
                } else {
                    if (str2[1].length() == 1) {
                        String s1 = str2[1];
                        str2[1] = s1 + "0";
                    }
                    c.setCellValue(str2[0] + ":" + str2[1]);
                }

                //Horas Empleadas**
                c = r.createCell((short) (7));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);

                c.setCellValue(schData.getRealtime_hours() + ":" + schData.getRealtime_minutes());
                horasEmpleadas += schData.getRealtime_hours();
                minutosEmpleados += schData.getRealtime_minutes();
                totalHorasProyecto += schData.getRealtime_hours();
                totalMinutosProyecto += schData.getRealtime_minutes();

                //Nombre del miembro Asignado A
                membersData mData = (membersData) mBroker.getData(tskData.getassigned_to());

                c = r.createCell((short) (8));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(mData.getname());

                //Comentario
                c = r.createCell((short) (9));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(schData.getcomments());


                //Tarifa
                c = r.createCell((short) (10));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(tskData.getfare().intValue());

                //Valor Total (Horas Empleadas * Tarifa)
                c = r.createCell((short) (11));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);


                double min = (schData.getRealtime_hours() * 60) + schData.getRealtime_minutes();

                c.setCellValue(tskData.getfare().doubleValue() * (min / 60));
                total += tskData.getfare().doubleValue() * (min / 60);



                rownumb++;
                //}

            }

            System.out.println("aqui3");
            r = ws.createRow(rownumb);
            c = r.createCell((short) 6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.SubtotalsWorked"));
            c.setCellStyle(cs4);


            totalValorProyectos += total;
            int horasExtra = minutosEmpleados / 60;
            horasEmpleadas += horasExtra;
            if ((minutosEmpleados % 60) != 0) {
                minutosEmpleados = minutosEmpleados % 60;
            }

            c = r.createCell((short) 7);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(horasEmpleadas + ":" + minutosEmpleados);
            c.setCellStyle(cs4);

            c = r.createCell((short) 10);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.SubtotalsFare"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 11);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(total);
            c.setCellStyle(cs4);

            rownumb = rownumb + 2;
        }
        r = ws.createRow(rownumb);

        c = r.createCell((short) 7);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.TotalProjectsHours"));
        c.setCellStyle(cs4);


        int horasExtra = totalMinutosProyecto / 60;
        totalHorasProyecto += horasExtra;
        totalMinutosProyecto = totalMinutosProyecto % 60;

        c = r.createCell((short) 8);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalHorasProyecto + ":" + totalMinutosProyecto);
        c.setCellStyle(cs4);


        c = r.createCell((short) 10);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.TotalProjectsValue"));
        c.setCellStyle(cs4);

        c = r.createCell((short) 11);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(totalValorProyectos);
        c.setCellStyle(cs4);

        broker.close();
        mBroker.close();
        System.out.println("aqui4");
        return wb;
    }

    public static HSSFWorkbook excelFileInformeDetalladoDeTareasporDia(String company, String fechaI, String fechaF, ArrayList dateList, String language, String total, String all) {
        short rownum;
        // create a new workbook object; note that the workbook
        // and the file are two separate things until the very
        // end, when the workbook is written to the file.
        HSSFWorkbook wb = new HSSFWorkbook();

        // create a new worksheet
        HSSFSheet ws = wb.createSheet();


        // create a row object reference for later use
        HSSFRow r = null;

        // create a cell object reference
        HSSFCell c = null;

        membersBroker memBroker = new membersBroker();
        tasksBroker taskBroker = new tasksBroker();
        tasksData taskData = new tasksData();
        membersData memData = new membersData();
        // create two cell styles - formats
        //need to be defined before they are used
        //Style for tittles
        HSSFCellStyle cs1 = wb.createCellStyle();
        //Style for fields like columns or the name or dates
        HSSFCellStyle cs2 = wb.createCellStyle();
        //Style for the data
        HSSFCellStyle cs3 = wb.createCellStyle();
        //Style for tables
        HSSFCellStyle cs4 = wb.createCellStyle();
        HSSFCellStyle cs5 = wb.createCellStyle();
        HSSFCellStyle cs6 = wb.createCellStyle();
        HSSFCellStyle cs7 = wb.createCellStyle();
        //Style for record
        HSSFCellStyle cs8 = wb.createCellStyle();
        HSSFCellStyle cs9 = wb.createCellStyle();
        HSSFCellStyle cs10 = wb.createCellStyle();

        HSSFCellStyle cs11 = wb.createCellStyle();

        HSSFCellStyle cs12 = wb.createCellStyle();
        HSSFCellStyle cs13 = wb.createCellStyle();

        // create two font objects for formatting
        //Font for tittle
        HSSFFont f1 = wb.createFont();
        //set font 1 to 14 point bold type
        f1.setFontHeightInPoints((short) 14);
        f1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f2 = wb.createFont();
        //set font 2 to 10 point red type
        f2.setFontHeightInPoints((short) 10);
        f2.setBoldweight((short) HSSFFont.BOLDWEIGHT_BOLD);

        //Font for the rest of information
        HSSFFont f3 = wb.createFont();
        //set font 2 to 10 point red type
        f3.setFontHeightInPoints((short) 10);
        f3.setColor((short) HSSFFont.COLOR_NORMAL);

        //Font for the rest of information
        HSSFFont f4 = wb.createFont();
        //set font 2 to 10 point red type
        f4.setFontHeightInPoints((short) 8);
        f4.setColor((short) HSSFFont.COLOR_NORMAL);

        //for cell style 1, use font 1 and set data format
        cs1.setFont(f1);
        cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 2, use font 2, set a thin border, text format
        cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs2.setFont(f2);

        //for cell style 3, use font 3, text format
        cs3.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs3.setFont(f3);

        //for cell style 11, use font 3, text format
        cs11.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs11.setFont(f2);
        cs11.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //for cell style 11, use font 3, text format
        cs12.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs12.setFont(f4);
        cs12.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //for cell style 3, use font 3, text format
        cs4.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs4.setFont(f2);
        cs4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs4.setWrapText(true);

        System.out.println("A1");
        //for cell style 3, use font 3, text format
        cs5.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs5.setFont(f2);
        cs5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs5.setBorderTop(HSSFCellStyle.BORDER_THIN);


        cs6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs6.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs6.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);

        cs13.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs13.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs13.setFont(f2);

        cs7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs7.setBorderTop(HSSFCellStyle.BORDER_THIN);


        cs8.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        cs8.setFont(f2);
        cs8.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs8.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs8.setWrapText(true);

        cs9.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs9.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs9.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs9.setWrapText(true);

        cs10.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs10.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cs10.setWrapText(true);

        // set the sheet name in Unicode
        wb.setSheetName(0, java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.consolidatedTimeReport"),
                HSSFWorkbook.ENCODING_UTF_16);

        System.out.println("A2");
        ws.setColumnWidth((short) 0, (short) 500);
        ws.setColumnWidth((short) 1, (short) 1610);
        ws.setColumnWidth((short) 2, (short) 9154);
        ws.setColumnWidth((short) 3, (short) 1610);
        ws.setColumnWidth((short) 4, (short) 4500);
        ws.setColumnWidth((short) 5, (short) 2572);
        ws.setColumnWidth((short) 6, (short) 2572);
        ws.setColumnWidth((short) 7, (short) 2572);
        ws.setColumnWidth((short) 8, (short) 2572);
        ws.setColumnWidth((short) 9, (short) 2809);
        ws.setColumnWidth((short) 10, (short) 13786);

        //Create new row for the tittle
        r = ws.createRow(0);

        c = r.createCell((short) (5));

        c.setCellStyle(cs1);
        // set the cell's string value to "Test"
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(company);

        //Create new row for subtittle
        r = ws.createRow(1);
        c = r.createCell((short) (5));
        c.setCellStyle(cs1);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.detailedTasksReportByDay"));

        //Create new row for the dates
        r = ws.createRow(2);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.startDate") + " :");

        //Create column to assign the Initial Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);

        if (!all.equalsIgnoreCase("ALL")) {
            c.setCellValue(fechaI);
        } else {
            c.setCellValue("TODAS LAS FECHAS");
        }

        r = ws.createRow(4);
        c = r.createCell((short) (4));
        c.setCellStyle(cs2);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.endDate") + " :");

        //Create column to assign the Final Date of the report
        c = r.createCell((short) (5));
        c.setCellStyle(cs3);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);

        if (!all.equalsIgnoreCase("ALL")) {
            c.setCellValue(fechaF);
        } else {
            c.setCellValue("TODAS LAS FECHAS");
        }


        System.out.println("A3");
        Iterator e = dateList.iterator();
        reportTasksData data = null;
        int rownumb = 6;
        while (e.hasNext()) {

            data = (reportTasksData) e.next();
            r = ws.createRow(rownumb);

            c = r.createCell((short) 1);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Id"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 2);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.projectName"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 3);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Id"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 4);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.Task"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 5);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.day"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.startHour"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 7);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.endHour"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 8);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.workedTime"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 9);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.assignedTo"));
            c.setCellStyle(cs4);

            c = r.createCell((short) 10);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("tasksForm.comments.displayName"));
            c.setCellStyle(cs4);


            System.out.println("A4");
            tasksBroker tskBroker = new tasksBroker();

            rownumb++;
            Iterator e1 = data.getlistScheduleTasks().iterator();
            schedulesData sData = new schedulesData();

            while (e1.hasNext()) {
                System.out.println("A5");
                sData = new schedulesData();
                sData = (schedulesData) e1.next();
                r = ws.createRow(rownumb);

                //Id
                c = r.createCell((short) (1));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getid());


                //Nombre Proyecto
                c = r.createCell((short) (2));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getparentTask().getparentProject().getname());
                //Id Tarea
                c = r.createCell((short) (3));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getparentTask().getid());

                System.out.println("A6");
                //Nombre Tarea
                c = r.createCell((short) (4));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getparentTask().getname());

                System.out.println("A6.1");
                //Dia
                c = r.createCell((short) (5));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getdate());

                System.out.println("A6.2");
                //Hora Inicio
                c = r.createCell((short) (6));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getHour_start());

                System.out.println("A6.3");
                //Hora Fin
                c = r.createCell((short) (7));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getHour_end());

                System.out.println("A6.4");
                //Horas Empleadas
                c = r.createCell((short) (8));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);

                System.out.println("A6.5");
                String min = "";
                if (sData.getRealtime_minutes() == 0) {
                    min = "00";
                } else {
                    min = String.valueOf(sData.getRealtime_minutes());
                }
                c.setCellValue(sData.getRealtime_hours() + ":" + min);
                memData = new membersData();
                taskData = new tasksData();

                taskData = (tasksData) taskBroker.getData(sData.getparentTask().getid());
                memData = (membersData) memBroker.getData(taskData.getassigned_to());

                System.out.println("A6.6.1 " + memData.getlogin());
                //Asignado A
                c = r.createCell((short) (9));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                //c.setCellValue(sData.getparentTask().getassigned_to()+" , "+ sData.getid()) ;
                c.setCellValue(memData.getlogin());
                System.out.println("A7");
                //Comentarios
                c = r.createCell((short) (10));
                c.setCellStyle(cs6);
                // set the cell's string value to tittle
                c.setEncoding(HSSFCell.ENCODING_UTF_16);
                c.setCellValue(sData.getcomments());

                rownumb++;

            }

            r = ws.createRow(rownumb);
            c = r.createCell((short) (7));
            c.setCellStyle(cs6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.SubtotalsWorked") + "(hh:mm)");

            c = r.createCell((short) (8));
            c.setCellStyle(cs6);
            // set the cell's string value to tittle
            c.setEncoding(HSSFCell.ENCODING_UTF_16);
            c.setCellValue(data.gettotal_real_hours());

            rownumb++;


        }

        System.out.println("A7");
        rownumb++;
        r = ws.createRow(rownumb);
        c = r.createCell((short) (7));
        c.setCellStyle(cs6);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(language, "")).getString("common.totalsWorked") + "(hh:mm)");

        c = r.createCell((short) (8));
        c.setCellStyle(cs6);
        // set the cell's string value to tittle
        c.setEncoding(HSSFCell.ENCODING_UTF_16);
        c.setCellValue(total);
        rownumb++;

        taskBroker.close();
        memBroker.close();
        return wb;
    }

    public static ArrayList readMassiveTaskSheet(FormFile file, String worksheetName, projectsData projectsdata) throws ParseException {
        String error = "";
        String error_row = "";
        type_tasksBroker typeBroker = new type_tasksBroker();
        teamsBroker teamBroker = new teamsBroker();
        membersBroker memBroker = new membersBroker();

        ArrayList tasks = new ArrayList();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet worksheet = workbook.getSheet(worksheetName);
            tasksData data = new tasksData();
            type_tasksData typeData = new type_tasksData();
            membersData member = new membersData();
            int row = 0;
            boolean flag = true;
            boolean flag2 = true;
            boolean isOnRange = true;
            String strValue = "";
            String estimated = "";
            double douValue = 0;
            double douValue1 = 0;
            double douValue2 = 0;
            String day = "";
            String month = "";
            String year = "";

            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            HSSFCell cellA1;
            ArrayList values = new ArrayList();
            values.add("Tarea");
            values.add("Descripción Tarea");
            values.add("Asignado A");
            values.add("Fecha Inicio");
            values.add("Fecha de Entrega");
            values.add("Prioridad");
            values.add("Horas Estimadas");
            values.add("Minutos Estimados");
            values.add("Tarifa");
            values.add("Tipo de Tarea");
            int counter = 0;

            Iterator rowIter = worksheet.rowIterator();

            if (isOnRange) {
                rowIter = worksheet.rowIterator();
                while (rowIter.hasNext()) {
                    data = new tasksData();
                    data.setId_account(projectsdata.getId_account());
                    data.setproject(projectsdata.getid());
                    HSSFRow row1 = (HSSFRow) rowIter.next();
                    estimated = "";
                    if (row != 0) {
                        flag = true;
                        error_row = "//    Error al insertar la línea: " + (row + 1);
                        for (int cn = 0; cn < 10; cn++) {

                            cellA1 = row1.getCell((short) cn);
                            if (cellA1 == null) {
                                flag = false;
                                error_row += " , en el campo ";
                                error_row += "El campo no es alfanumérico";
                            } else {
                                switch (cellA1.getCellType()) {
                                    case HSSFCell.CELL_TYPE_STRING:
                                        if (cellA1.getStringCellValue() == null) {
                                            flag = false;
                                            error_row += " , en el campo ";
                                            error_row += "El campo no es alfanumérico";
                                        }
                                        strValue = cellA1.getStringCellValue();

                                        if (cn != 3) {
                                            if (cn != 4) {
                                                counter = 0;
                                                for (int i = 0; i < strValue.length(); i++) {
                                                        
                                                    if (!Character.isLetter(strValue.charAt(i))) {
                                                        if (!Character.isDigit(strValue.charAt(i))) {
                                                            counter++;
                                                        }
                                                    }
                                                }

                                                if (strValue.length() == counter) {
                                                    flag = false;
                                                    error_row += " , en el campo " + values.get(cn) + ": ";
                                                    error_row += "El campo no es alfanumérico";
                                                }
                                            }
                                        }

                                        if (cn == 5 || cn == 6 || cn == 7 || cn == 8 || cn == 9) {
                                            flag = false;
                                            error_row += " , en el campo " + values.get(cn) + ": ";
                                            error_row += "El campo no es un número Entero";
                                        }

                                        if (cn == 0) {
                                            if (strValue.length() >= 155) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El campo supera el límite de 150 caracteres";
                                            } else if (strValue.length() < 5) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El campo no supera los 5 caracteres";
                                            }
                                        }

                                        if (cn == 1) {
                                            if (strValue.length() >= 600) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El campo supera el límite de 150 caracteres";
                                            } else if (strValue.length() < 5) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El campo no supera los 5 caracteres";
                                            }
                                        }

                                        if (cn == 3 || cn == 4) {
                                            try {
                                                String[] date = strValue.split("-");
                                                Timestamp ts = Timestamp.valueOf(strValue + " 12:00:00.00");

                                                Calendar cal = Calendar.getInstance();
                                                cal.setTimeInMillis(ts.getTime());


                                                // System.out.println("LINEA "+row +" DAY "+ date[3]+ " MONTH "+ ts.getMonth()+ " MAX DAY "+ cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                                                if (ts.getMonth() == 0) {
                                                    flag = false;
                                                    error_row += " , en el campo " + values.get(cn) + ": ";
                                                    error_row += "El formato de la fecha es inválido";
                                                } else {
                                                    if (Integer.parseInt(date[2]) > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {

                                                        flag = false;
                                                        error_row += " , en el campo " + values.get(cn) + ": ";
                                                        error_row += "El formato de la fecha es inválido";
                                                    }
                                                }

                                            } catch (Exception e) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El formato de la fecha es inválido";
                                            }
                                        }
                                        if (cn == 2) {
                                            member = (membersData) memBroker.getMemberByLogin(strValue);
                                            if (member == null){
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El Miembro "+ strValue+" no existe";
                                                flag=false;
                                            }
                                                
                                        }

                                        if (cn == 0) {
                                            System.out.println("NOMBRES " + strValue);
                                        }
                                        if (flag) {
                                            switch (cn) {
                                                case 0:
                                                    data.setname(strValue);
                                                    break;
                                                case 1:
                                                    data.setdescription(strValue);
                                                    break;
                                                case 2:
                                                    data.setassigned_to(member.getid());
                                                    break;
                                                case 3:
                                                    data.setstart_date(Timestamp.valueOf(strValue + " 12:00:00.00"));
                                                    break;
                                                case 4:
                                                    data.setdue_date(Timestamp.valueOf(strValue + " 12:00:00.00"));
                                                    break;
                                            }
                                        }
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        if (cn == 0 || cn == 1 || cn == 2 || cn == 3 || cn == 4) {
                                            flag = false;
                                            error_row += " , en el campo " + values.get(cn) + ": ";
                                            error_row += "El campo no es alfanumérico";
                                        }

                                        douValue = cellA1.getNumericCellValue();
                                        douValue1 = Math.floor(douValue);
                                        douValue2 = douValue - douValue1;

                                        if (douValue2 != 0) {
                                            flag = false;
                                            error_row += " , en el campo " + values.get(cn) + ": ";
                                            error_row += "El campo no es un número Entero";
                                        }

                                        /* if (cn == 7 || cn == 6 || cn == 2 || cn== 5 || cn==8 || cn==9){
                                         if (douValue < 0)
                                         error_row+=" , en el campo "+ values.get(cn)+": ";
                                         error_row+="El valor no puede ser un número Negativo";
                                                
                                         }*/

                                        if (cn == 2) {
                                            if (!teamBroker.isMember(projectsdata.getid(), (int) douValue, projectsdata.getId_account())) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El Miembro " + (int) douValue + " no pertenece al Equipo de Trabajo del Projecto";
                                            }
                                        }
                                        if (cn == 7) {
                                            if (douValue > 60) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El valor no se encuentra dentro del rango de 0 a 60 min";
                                            }
                                        }

                                        if (cn == 9) {
                                            typeData = (type_tasksData) typeBroker.getData((int) douValue);
                                            if (typeData.getid() == 0) {
                                                flag = false;
                                                error_row += " , en el campo " + values.get(cn) + ": ";
                                                error_row += "El Tipo de Tarea no existe";
                                            }
                                        }
                                        if (flag) {
                                            switch (cn) {
                                                case 2:
                                                    data.setassigned_to((int) douValue);
                                                    break;
                                                case 5:
                                                    data.setpriority((int) douValue);
                                                    break;
                                                case 6:
                                                    estimated = "" + (int) douValue + dfs.getDecimalSeparator();
                                                    break;
                                                case 7:
                                                    estimated += "" + (int) +douValue;
                                                    break;
                                                case 8:
                                                    data.setfare(new BigDecimal(douValue));
                                                    break;
                                                case 9:
                                                    data.settype_task((int) douValue);
                                                    break;
                                            }
                                        }
                                        break;
                                    case HSSFCell.CELL_TYPE_FORMULA:
                                        System.out.println("Campo FORM " + cellA1.getCellFormula());
                                        break;
                                    case HSSFCell.CELL_TYPE_BOOLEAN:
                                        System.out.println("Campo BOOL");
                                        break;
                                    case HSSFCell.CELL_TYPE_ERROR:
                                        flag = false;
                                        error_row += " , en el campo " + values.get(cn) + ": ";
                                        error_row += " el formato de celda está mal definido";
                                        break;
                                    case HSSFCell.CELL_TYPE_BLANK:
                                        flag = false;
                                        error_row += " , en el campo " + values.get(cn) + ": ";
                                        error_row += "El campo está vacío";
                                        System.out.println(error);
                                }
                            }
                        }
                        if (flag) {
                            if (data.getstart_date().after(data.getdue_date())) {
                                flag = false;
                                error_row += " , la Fecha de Inicio es mayor a la Fecha de Entrega. ";
                            }
                        }



                        error_row += " <br> ";
                        if (flag) {
                            System.out.println("ESTIMATED " + estimated + "ROW " + row);
                            if (row != 0) {
                                data.setestimated_time(new BigDecimal(estimated));
                            }

                            error_row = "";
                            tasks.add(data);
                        } else {

                            flag2 = false;
                            error += error_row;
                            System.out.println(error_row);
                        }
                    }
                    row++;


                }
            }
            int maxTasks = Integer.parseInt(TMSConfigurator.getMaxTasksUpload());
            System.out.println("Error! " + row + " / " + maxTasks);
            if (!flag2 || (row > maxTasks)) {
                if (row > maxTasks) {
                    error = "El Total de Tareas a superado las 200. Favor disminuir la cantidad.";
                }
                tasks.clear();
                data.setHasErrors(true);
                data.setname("Error");
                data.setdescription(error);
                tasks.add(data);
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            typeBroker.close();
            teamBroker.close();
            memBroker.close();
        } catch (IOException e) {
            e.printStackTrace();
            typeBroker.close();
            teamBroker.close();
            memBroker.close();
        }
        typeBroker.close();
        teamBroker.close();
        memBroker.close();

        System.out.println("TAMAÑO " + tasks.size());
        return tasks;
    }

    public static String getStatus1(int status) {

        String str = "Finalizado por Cliente";
        switch (status) {
            case 0:
                str = "Finalizado por Cliente";
                break;
            case 1:
                str = "Finalizado";
                break;
            case 2:
                str = "No Iniciado";
                break;
            case 3:
                str = "Iniciado";
                break;
            case 4:
                str = "Suspendido";
                break;
            case 5:
                str = "Cotización";
                break;
            case 6:
                str = "Cotización Enviada";
                break;
            case 7:
                str = "Cotización Rechazada";
                break;
            case 8:
                str = "Cotización Aceptada";
                break;
            case 9:
                str = "Cobrar";
                break;
            case 10:
                str = "Cobro Enviado";
                break;
            case 11:
                str = "Procesando Cobro";
                break;
            case 12:
                str = "Cotizacion Rechazada";
                break;
            case 13:
                str = "Control de Calidad";
                break;
            case 14:
                str = "Standby";
                break;
        }
        return str;
    }
}
