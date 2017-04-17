/*
 * tms2mpx.java
 *
 * Created on June 16, 2003, 10:52 AM
 */

package com.unify.webcenter.tools;

import com.tapsterrock.mpx.*;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.*;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Clase que se encarga de convertir un proyecto de TMS a un equivalente en formato
 * MS Project 2002. 
 *
 * @author  Gerardo Arroyo Arce
 */
public class tms2mpx extends HttpServlet {
        
    
    /** Creates a new instance of tms2mpx */
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
                
    }
    
    
    /** Destroys the servlet.
     */
    public void destroy() {
    }    
    
    
    /*
     * Este metodo se encarga de crear el archivo en Project tomando como base
     * el id del proyecto de TMS que viene como parametro.
     */ 
    public void convert2Project(int projectID, OutputStream out, MPXFile file) throws Exception, com.tapsterrock.mpx.MPXException{
        // Se crea inicialmente el archivo de salida.
        this.createMPXFile(file);
        
        SimpleDateFormat df = new SimpleDateFormat ("dd-MM-yyyy");
        projectsBroker projBroker= new projectsBroker();
        teamsBroker teamBroker = new teamsBroker();
        tasksBroker taskBroker = new tasksBroker();
        
        
        // Se extrae la referencia al proyecto en cuestion.
        projectsData projData = (projectsData) projBroker.getData(projectID);
        
        // Se define el header del project File
        ProjectHeader header = file.getProjectHeader();
                
        // Nombre del manager
        header.setManager(projData.getparentOwner().getname());
        
        // Y el subject 
        header.setSubject(projData.getname());
        
        // Se extraen los Resources asociados a este proyecto, que viene a ser la lista 
        // del team, los mismos se guardan en un hash por el id para ligarlos a las
        // tareas posteriormente
        Iterator e = teamBroker.getListByProject("id", "ASC", projectID, 0);
        teamsData teamInfo;
        Hashtable listaRecursos = new Hashtable();
        while (e.hasNext()) {
            teamInfo = (teamsData) e.next();
            
            // Se crea el recurso asociado a este miembro del equipo.
            Resource resource1 = file.addResource();
            resource1.setName(teamInfo.getparentMember().getname());                                    
    
            // Se agrega a la lista de recursos
            listaRecursos.put(""+teamInfo.getmembers(), resource1);
        }
        
        
        // Ahora se extraen todas las tareas del proyecto, a fin de procesarlas.
        e = taskBroker.getListByProject("start_date","ASC",projectID,0);
        tasksData taskInfo;
        
        // Se define una variable de tipo fecha            
        Calendar date = Calendar.getInstance();        
        Calendar dateEnd = Calendar.getInstance();   
        int i = 1;
        
        // Hashtable para el link de las tareas predecesoras.
        Hashtable tableList = new Hashtable();
        
        // Se procesan todas las tareas.
        while (e.hasNext()) {
            taskInfo = (tasksData) e.next();
            
            Task task1 = file.addTask();
            task1.setName (taskInfo.getname());

            // Fecha de Inicio
            date.setTimeInMillis(taskInfo.getstart_date().getTime());
            
            // Se trae la fecha de inicio.
            task1.setStart(df.parse(date.get(Calendar.DAY_OF_MONTH) + "-" + 
                (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR)));
            
            
            // Se toma la fecha de finalizacion a fin de calcular los dias de separacion
            dateEnd.setTimeInMillis(taskInfo.getdue_date().getTime());
            
            // Proteccion contra errores. Si la fecha de fin es antes de la de inicio
            // se actualiza como fecha final la misma de inicio.
            if (dateEnd.before(date))
                dateEnd.setTimeInMillis(date.getTimeInMillis());
            
            
            task1.setFinish(df.parse(dateEnd.get(Calendar.DAY_OF_MONTH) + "-" + 
                (dateEnd.get(Calendar.MONTH) + 1) + "-" + dateEnd.get(Calendar.YEAR)));
            

            // Se guarda la diferencia en dias
            task1.setDuration(new MPXDuration (this.dayDiff(dateEnd.getTime(),
                date.getTime()) + 1, TimeUnit.DAYS));
            
            // Y el tipo de Constraint Type
            task1.setConstraintType(ConstraintType.
                    getInstance(ConstraintType.START_NO_EARLIER_THAN));
            
            if (i == 1) {
                // Como las tareas estar ordenadas por fecha, se toma el del 
                //primero para setear el proyecto
                header.setStartDate(df.parse(date.get(Calendar.DAY_OF_MONTH) + "-" + 
                (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR)));
                i = 2;
            }
            
            // Se define el porcentaje de completitud
            task1.setPercentageComplete(taskInfo.getcompletion());                       
            
            if (taskInfo.getassigned_to() > 0) {
                // Se le asocia el recurso correspondiente, para ello se extrae del
                // hash con base en el id de la tarea, siempre y cuando el ID sea
                // mayor a 0 (0 es NO ASIGNADO)
                Resource tmp = (Resource) listaRecursos.get("" + taskInfo.getassigned_to());                
                if (tmp != null) {    
                    task1.addResourceAssignment(tmp);                    
                }
            }
            
            // Se agrega al hash, usando como key el id de la tarea
            tableList.put("" + taskInfo.getid(), task1);
        }
        
        // Una vez procesadas todas las tareas, se continua haciendo el link entre las tareas
        // que son predecesoras o no.
        e = taskBroker.getListByProject("start_date","ASC",projectID,0);
        
        while (e.hasNext()) {
            taskInfo = (tasksData) e.next();
            
            if (taskInfo.getpredecessor() > 0) {
                // Esta tarea tiene como predecesora a otra.                
                Task task1 = (Task) tableList.get("" + taskInfo.getid());
                Task taskPred = (Task) tableList.get("" + taskInfo.getpredecessor());
                
                Relation rel1 = task1.addPredecessor(taskPred);
                
                // Si la tarea requiere que este concluida la anterior, se le indica
                // que la relacion es FINISH_START
                if (taskInfo.getpredecessor_required().equals("1"))
                    rel1.setType(Relation.FINISH_START);
                else
                    rel1.setType(Relation.START_START);                                
            }
        }
        
        // Se cierran los brokers
        projBroker.close();
        teamBroker.close();
        taskBroker.close();

        
        // Se escribe el archivo al output
        file.write(out);
    }
    
    
    /* 
     * Metodo que crea el archivo en formato MPX
     */
    private void createMPXFile(MPXFile file) throws Exception {
      //
      // Configure the file to automatically generate identifiers for tasks.
      //
      file.setAutoTaskID(true);
      file.setAutoTaskUniqueID(true);

      //
      // Configure the file to automatically generate identifiers for resources.
      //
      file.setAutoResourceID(true);
      file.setAutoResourceUniqueID(true);

      //
      // Configure the file to automatically generate outline levels
      // and outline numbers.
      //
      file.setAutoOutlineLevel(true);
      file.setAutoOutlineNumber(true);

      //
      // Configure the file to automatically generate WBS labels
      //
      file.setAutoWBS(true);

      //
      // Add a default calendar called "Standard"
      //
      BaseCalendar cal = file.addDefaultBaseCalendar();
      cal.setWorkingDay(1, true); // Domingo se trabaja
      cal.setWorkingDay(7, true); // Sabado se trabaja
  
//      
    }       
    
    /*
     * Metodo que regresa la diferencia en dias entre dos fechas
     **/
    private int dayDiff(Date d2, Date d1) {
        //getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT.
        //86400000 == 24 * 60 * 60 * 1000                         
        int days =(int)( (d2.getTime()-d1.getTime()) / 86400000 );
        
        return days;

    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Get parameters
        String id = request.getParameter("id");
        
        OutputStream out = response.getOutputStream();

        // Set the headers.
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=" +id + ".mpx");

        // Send the file.
        tms2mpx converter = new tms2mpx();    
        
        try {
            MPXFile file= new MPXFile ();
            converter.convert2Project(Integer.parseInt(id), out, file);
        } catch (Exception e) {
            System.out.println("TMS2MPX: " + e.toString());
            e.printStackTrace();
        }
        
        out.close();
    }
    
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
}
