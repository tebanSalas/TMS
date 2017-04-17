/*
 * WebChart.java
 *
 * Created on 22 de febrero de 2005, 09:40 AM
 */

package com.unify.webcenter.charts;

import javax.servlet.http.HttpSession;

import java.io.PrintWriter;

import java.util.*;
import java.awt.*;

import org.jfree.data.*;
import org.jfree.data.category.*;
import org.jfree.data.general.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.plot.*;
import org.jfree.chart.entity.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.urls.*;
import org.jfree.chart.servlet.*;
import org.jfree.ui.RectangleInsets;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;

import javax.servlet.http.*;

/**
 * Clase que se encarga de generar los principales graficos del SOS, tomando como
 * base la informacion de las oportunidades y otros aspectos.
 *
 * Basado en el trabajo de Richard Atkinson en el 2004.
 *
 * @author  Gerardo Arroyo
 */
public class WebChart {
   
    
    /** Creates a new instance of WebChart */
    public WebChart() {
    }
    
    
    /**
     * Metodo que se encarga de generar un grafico de PIE de las tareas de un proyecto
     * de acuerdo a su estado
     */

    public static String generatePieChart(HttpSession session, PrintWriter pw, String project, int idAccount) {
        String filename = null;
        try {
            
            tasksBroker taskBroker =  new tasksBroker();                        
            Iterator e = taskBroker.getListByProject(Integer.parseInt(project),idAccount);
            taskBroker.close();
            // ----------------------------------------------------------------
            // Se procesa cada una de las tareas que estan disponibles.
            // Y con base en eso se llevan los contadores correspondientes.
            // ----------------------------------------------------------------
            int tipo;
            long forecast=0,pipe=0, na=0, outlook=0;
            long[] contadores = new long[15];
            
            for (int i=0; i < 15; i++) contadores[i]=0;
            tasksData tkData;
            while (e.hasNext()) {
                    tkData = (tasksData)e.next();
                    tipo = tkData.getstatus();
                    contadores[tipo]++;
                             
            }
            //  Create and populate a PieDataSet
            DefaultPieDataset data = new DefaultPieDataset();       
            loginData user = (loginData) session.getAttribute("login");

            java.util.ResourceBundle bundle =  java.util.ResourceBundle.getBundle("ApplicationResources",
                    new Locale(user.getlanguage(),""));
            
            if (contadores[0] > 0 ) data.setValue(bundle.getString("common.statusClientEnd"), new Long(contadores[0]));
            if (contadores[1] > 0 ) data.setValue(bundle.getString("common.statusEnd"), new Long(contadores[1]));
            if (contadores[2] > 0 ) data.setValue(bundle.getString("common.statusNotStarted"), new Long(contadores[2]));
            if (contadores[3] > 0 ) data.setValue(bundle.getString("common.statusStarted"), new Long(contadores[3]));
            if (contadores[4] > 0 ) data.setValue(bundle.getString("common.statusSuspended"), new Long(contadores[4]));
            if (contadores[5] > 0 ) data.setValue(bundle.getString("common.statusQuoteGraph"), new Long(contadores[5]));
            if (contadores[6] > 0 ) data.setValue(bundle.getString("common.statusQuoteSendedGraph"), new Long(contadores[6]));
            if (contadores[7] > 0 ) data.setValue(bundle.getString("common.statusQuoteRejectedGraph"), new Long(contadores[7]));
            if (contadores[8] > 0 ) data.setValue(bundle.getString("common.statusQuoteAceptedGraph"), new Long(contadores[8]));
            if (contadores[9] > 0 ) data.setValue(bundle.getString("common.statusCharge"), new Long(contadores[9]));
            if (contadores[10] > 0 ) data.setValue(bundle.getString("common.statusChargeSended"), new Long(contadores[10]));
            if (contadores[11] > 0 ) data.setValue(bundle.getString("common.statusProcessingCharge"), new Long(contadores[11]));
            if (contadores[12] > 0 ) data.setValue(bundle.getString("common.statusRejected"), new Long(contadores[12]));
            if (contadores[13] > 0 ) data.setValue(bundle.getString("common.qualitycontrol"), new Long(contadores[13]));
            if (contadores[14] > 0 ) data.setValue(bundle.getString("common.standBy"), new Long(contadores[14]));
            
     
            //  Create the chart object
            PiePlot3D plot = new PiePlot3D(data);
            plot.setInsets(new RectangleInsets(0, 5, 5, 5));
//            plot.setURLGenerator(new StandardPieURLGenerator("xy_chart.jsp","section"));
            plot.setToolTipGenerator(new StandardPieToolTipGenerator());
            plot.setForegroundAlpha(0.5F);

            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBackgroundPaint(java.awt.Color.white);

            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 400, 275, info, null);

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();

 //       } catch (NoDataException e) {
 //               System.out.println(e.toString());
//                filename = "public_nodata_500x300.png";
        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }    
    
    /**
     * Metodo que regresa un pie de las tareas atrazadas o a tiempo de un proyecto dado.
     */
    public static String generatePieChartTasks(HttpSession session, PrintWriter pw, String project) {
        String filename = null;
        try {
            
            tasksBroker taskBroker =  new tasksBroker();              
            statisticsData statistics = new statisticsData();
            statistics = (statisticsData) taskBroker.getTotalTasksByProjectStatistics(Integer.parseInt(project));
            taskBroker.close();
            
            loginData user = (loginData) session.getAttribute("login");

            java.util.ResourceBundle bundle =  java.util.ResourceBundle.getBundle("ApplicationResources",
                    new Locale(user.getlanguage(),""));
                       
            
            // ----------------------------------------------------------------
            // Se procesa cada una de las tareas que estan disponibles.
            // Y con base en eso se llevan los contadores correspondientes.
            // ----------------------------------------------------------------    
            DefaultPieDataset data = new DefaultPieDataset();   
            
            data.setValue(bundle.getString("common.totalOnTimeTasks"), new Long(statistics.gettotalontimetasks()));
            
            // Correccion en calculo de las tareas
            int atrasadas = statistics.gettotaldelayedtasks() -  
                    statistics.gettotalrejectedtasks() -
                    statistics.gettotalsuspendedtasks();
            if (atrasadas < 0) atrasadas = 0;
            
            data.setValue(bundle.getString("common.totalDelayedTasks"), new Long(atrasadas));
            
            //  Create the chart object
            PiePlot3D plot = new PiePlot3D(data);
            plot.setInsets(new RectangleInsets(0, 5, 5, 5));
//            plot.setURLGenerator(new StandardPieURLGenerator("xy_chart.jsp","section"));
            plot.setToolTipGenerator(new StandardPieToolTipGenerator());
            plot.setForegroundAlpha(0.5F);

            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBackgroundPaint(java.awt.Color.white);

            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 350, 225, info, null);

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();

 //       } catch (NoDataException e) {
 //               System.out.println(e.toString());
//                filename = "public_nodata_500x300.png";
        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }        
    
    /*
    public static String generateBarChartTop5(HttpSession session, PrintWriter pw) {
        String filename = null;
        try {
            opportunitiesBroker opBroker =  new opportunitiesBroker();            
            Iterator e = opBroker.getListActiveOpportunities("cost", "DESC");
            opBroker.close();

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            //  Create the chart object
            CategoryAxis3D categoryAxis = new CategoryAxis3D("Oportunidades");
            ValueAxis valueAxis = new NumberAxis("Monto");
            BarRenderer3D renderer = new BarRenderer3D();            
            
            opportunitiesData opData;
            int i = 0;
            Random r = new Random();

            while (e.hasNext() && i < 5) {
                    opData = (opportunitiesData)e.next();                    
                    dataset.addValue(new Long(opData.getcost().longValue()), "Hits", opData.getname());                       
                    i++;                    
            }

//            renderer.setItemURLGenerator(new StandardCategoryURLGenerator("xy_chart.jsp","series","section"));


             // Se usa un custom renderer
             CustomRenderer customrenderer = new CustomRenderer(
                    new Paint[] {Color.red, Color.blue, Color.green, Color.yellow,
                    Color.orange, Color.cyan, Color.magenta, Color.blue
                    });

            customrenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            
            Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, customrenderer);
            plot.setForegroundAlpha(0.5F);            
            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false); 


            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 600, 400, info, null);

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info);
            pw.flush();

        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }    
    */
    
    /**
     * Metodo que genera el grafico de distribucion de oportunidades por Territorio
     */
    /*
    public static String generateBarOpportunitiesByTerritory(HttpSession session, PrintWriter pw) {
        String filename = null;
        try {
            opportunitiesBroker opBroker =  new opportunitiesBroker();            
            Iterator e = opBroker.getListActiveOpportunities("id", "DESC");
            opBroker.close();

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            //  Create the chart object
            CategoryAxis3D categoryAxis = new CategoryAxis3D("Territorio");
            ValueAxis valueAxis = new NumberAxis("Oportunidades");
            Hashtable tablaResultado = new Hashtable();
            opportunitiesData opData;
            String key;
            Long valor;
            while (e.hasNext()) {
                    opData = (opportunitiesData)e.next();  
                    key = opData.getparentOrganization().getparentTerritory().getname();
                    
                    if (tablaResultado.containsKey(key)) {
                        // Si la entrada ya esta en la tabla, solo se le suma uno
                        valor = (Long)tablaResultado.get(key);
                        tablaResultado.put(key, new Long(valor.longValue() + 1));
                    } else {
                        tablaResultado.put(key, new Long(1));
                    }
            }
            
            // Ahora se genera el dataset
            Enumeration llaves = tablaResultado.keys();
            
            while (llaves.hasMoreElements()) {
                key = (String) llaves.nextElement();
                
                dataset.addValue( (Long) tablaResultado.get(key), 
                     "Total", 
                    key);                       

            }
             // Se usa un custom renderer
             CustomRenderer customrenderer = new CustomRenderer(
                    new Paint[] {Color.red, Color.blue, Color.green, Color.yellow,
                    Color.orange, Color.cyan, Color.magenta, Color.blue
                    });

            customrenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            
            Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, customrenderer);
            plot.setForegroundAlpha(0.5F);            
            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false); 
 
            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 350, 225, info, null);

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info);
            pw.flush();

        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }        
    
    */
    
    /**
     * Metodo que genera el grafico de distribucion de oportunidades por monto
     */
    /*
    public static String generateBarOpportunitiesByAmmount(HttpSession session, PrintWriter pw) {
        String filename = null;
        try {
            opportunitiesBroker opBroker =  new opportunitiesBroker();            
            Iterator e = opBroker.getListActiveOpportunities("cost", "DESC");
            opBroker.close();

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            //  Create the chart object
            CategoryAxis3D categoryAxis = new CategoryAxis3D("Monto");
            ValueAxis valueAxis = new NumberAxis("Oportunidades");
            BarRenderer3D renderer = new BarRenderer3D();          
            
            // Arreglo que contiene el desglose por precios, siempre es un arreglo
            // de TOTAL_DIVISIONES
            long detalleOp[] = new long[10];
            
            // Se deja en 0 para inicializar el arreglo
            for (int i=0; i < 10;i++) detalleOp[i]=0;
            long maximo, i = 0, step=0;
            long pos;
             
            opportunitiesData opData;
            while (e.hasNext()) {
                    opData = (opportunitiesData)e.next();  
                    
                    if (i==0) {
                        // Si es el primer registro y como esta ordenado por monto 
                        // descente, sabemos cual es el maximo valor y con eso, el
                        // step value que corresponde.
                        maximo = opData.getcost().longValue();
                        step = maximo / 10;
                        i++;
                    }
                    
                    // Se toma el valor correspondiente del monto y se coloca
                    // en la casilla apropiada del arreglo                   
                    pos = opData.getcost().longValue()%10;
                    if (opData.getcost().longValue()/10 == 0) pos--;
                    
                    if (pos < 0) pos = 0;
                    detalleOp[(int)pos] = detalleOp[(int)pos]+1;
            }
            
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            for (int index=0; index< 10; index++) {                                
                // El valor del key es la etiqueta en la parte inferior del grafico
                // y el primer valor del addValue es el del eje Y
                dataset.addValue( new Long(detalleOp[index]), 
                "Total", 
                    ""+nf.format(step*index) + " a " + nf.format(step*(index+1)));                       
            }
             // Se usa un custom renderer
             CustomRenderer customrenderer = new CustomRenderer(
                    new Paint[] {Color.red, Color.blue, Color.green, Color.yellow,
                    Color.orange, Color.cyan, Color.magenta, Color.blue
                    });

            customrenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            customrenderer.setItemLabelsVisible(true);
            Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, customrenderer);
            plot.setForegroundAlpha(0.5F);            
            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false); 

            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 350, 225, info, null);
        

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info);
            pw.flush();

        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }        
/*               
    
  /**
     * Metodo que se encarga de generar un grafico de PIE de las oportunidades
     * de acuerdo a su temperatura y el vendedor asignado.
     */
    /*
    public static String generatePieChartBySalesman(HttpSession session, PrintWriter pw,
        String salesman) {
        String filename = null;
        try {
            opportunitiesBroker opBroker =  new opportunitiesBroker();
            
            // Se traen todas las oportunidades activas de un vendedor
            Iterator e = opBroker.getListByMember("name", "ASC", 
                Integer.parseInt(salesman),
                "1",0);

            opBroker.close();
            // ----------------------------------------------------------------
            // Se procesa cada una de las oportunidades que estan disponibles.
            // Y con base en eso se llevan los contadores correspondientes.
            // ----------------------------------------------------------------
            String tipo;
            long forecast=0,pipe=0, na=0, outlook=0;
            opportunitiesData opData;
            while (e.hasNext()) {
                    opData = (opportunitiesData)e.next();
                    tipo = opData.gettype();
                     if (tipo.equals("F")) {
                       forecast++;
                    } else if (tipo.equals("P")) {
                       pipe++;
                    } else if (tipo.equals("-")) {
                        na++;
                    } else {
                        outlook++;
                    }
                             
            }
            //  Create and populate a PieDataSet
            DefaultPieDataset data = new DefaultPieDataset();                 
            data.setValue("Pipeline", new Long(pipe+na));            
            data.setValue("Forecast", new Long(forecast));            
            data.setValue("Outlook", new Long(outlook));
     
            //  Create the chart object
            PiePlot3D plot = new PiePlot3D(data);
            plot.setInsets(new Insets(0, 5, 5, 5));
//            plot.setURLGenerator(new StandardPieURLGenerator("xy_chart.jsp","section"));
            plot.setToolTipGenerator(new StandardPieItemLabelGenerator());
            plot.setForegroundAlpha(0.5F);

            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBackgroundPaint(java.awt.Color.white);

            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 350, 225, info, null);

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info);
            pw.flush();

 //       } catch (NoDataException e) {
 //               System.out.println(e.toString());
//                filename = "public_nodata_500x300.png";
        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }    
      */  
    
  /**
     * Metodo que genera el grafico de distribucion de oportunidades por monto por vendedor
     */
    /*
    public static String generateBarOpportunitiesByAmmountSalesman(HttpSession session, PrintWriter pw,
        String salesman) {
        String filename = null;
        try {
            opportunitiesBroker opBroker =  new opportunitiesBroker();
            
            // Se traen todas las oportunidades activas de un vendedor
            Iterator e = opBroker.getListByMember("cost", "DESC", 
                Integer.parseInt(salesman),
                "1",0);

            opBroker.close();

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            //  Create the chart object
            CategoryAxis3D categoryAxis = new CategoryAxis3D("Monto");
            ValueAxis valueAxis = new NumberAxis("Oportunidades");
            BarRenderer3D renderer = new BarRenderer3D();          
            
            // Arreglo que contiene el desglose por precios, siempre es un arreglo
            // de TOTAL_DIVISIONES
            long detalleOp[] = new long[10];
            
            // Se deja en 0 para inicializar el arreglo
            for (int i=0; i < 10;i++) detalleOp[i]=0;
            long maximo, i = 0, step=0;
            long pos;
             
            opportunitiesData opData;
            while (e.hasNext()) {
                    opData = (opportunitiesData)e.next();  
                    
                    if (i==0) {
                        // Si es el primer registro y como esta ordenado por monto 
                        // descente, sabemos cual es el maximo valor y con eso, el
                        // step value que corresponde.
                        maximo = opData.getcost().longValue();
                        step = maximo / 10;
                        i++;
                    }
                    
                    // Se toma el valor correspondiente del monto y se coloca
                    // en la casilla apropiada del arreglo                   
                    pos = opData.getcost().longValue()%10;
                    if (opData.getcost().longValue()/10 == 0) pos--;
                    
                    if (pos < 0) pos = 0;
                    detalleOp[(int)pos] = detalleOp[(int)pos]+1;
            }
            
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            for (int index=0; index< 10; index++) {                                
                // El valor del key es la etiqueta en la parte inferior del grafico
                // y el primer valor del addValue es el del eje Y
                dataset.addValue( new Long(detalleOp[index]), "Total", 
                    ""+nf.format(step*index) + " a " + nf.format(step*(index+1)));                       
            }
             // Se usa un custom renderer
             CustomRenderer customrenderer = new CustomRenderer(
                    new Paint[] {Color.red, Color.blue, Color.green, Color.yellow,
                    Color.orange, Color.cyan, Color.magenta, Color.blue
                    });

            customrenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            
            Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, customrenderer);
            plot.setForegroundAlpha(0.5F);            
            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false); 


            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 350, 225, info, null);
        

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info);
            pw.flush();

        } catch (Exception e) {
                System.out.println("Exception - " + e.toString());
                e.printStackTrace(System.out);
                filename = "public_error_500x300.png";
        }
        return filename;
    }        
    */

}
