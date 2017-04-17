/*
 * reportsAction.java
 *
 * Created on March 1, 2003, 6:32 PM
 */
package com.unify.webcenter.action.portal;

import java.io.IOException;
import java.util.*;
import java.sql.Timestamp;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.form.*;

/**
 * <p>This action works with both JSP and Velocity templates.
 * The type of template to be used is defined in the Struts configuration
 * file.</p>
 *
 * <p>The action support an <i>action</i> URL parameter. This URL parameter
 * controls what this action class does. The following values are supported:</p>
 * <ul>
 *   <li>save    Save the record
 *   <li>delete	 Delete the record
 *   <li>edit    Edit the record
 *   <li>show	 Show the record
 * </ul>
 *
 *
 * @author Administrator 
 */
public class reportsAction extends Action {

    /** Creates a new instance of calendarAction */
    public reportsAction() {
    }

    /**
     * Handle server requests
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        String action;
        HttpSession session;
        reportsBroker broker;
        organizationsBroker brokerOrg;
        projectsBroker brokerProjects;
        membersBroker brokerMembers;
        tasksBroker brokerTasks;
        tasksStatusLogBroker brokerTasksLog;
        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {

            // forward to display the home page
            return (mapping.findForward("portalLogin"));
        } else {
            broker = new reportsBroker();
            brokerOrg = new organizationsBroker();
            brokerProjects = new projectsBroker();
            brokerMembers = new membersBroker();
            brokerTasks = new tasksBroker();
            brokerTasksLog = new tasksStatusLogBroker();
            try {
                if (form == null) {
                    System.out.println(" Creating new reportsForm bean under key " + mapping.getAttribute());
                    form = new reportsForm();
                }
                // Se atrapa el formulario
                reportsForm thisForm = (reportsForm) form;

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                request.setAttribute("projectInfo", brokerProjects.getData(
                        Integer.parseInt(request.getParameter("project")), user.getId_account()));

                // fetch action from form
                action = ((reportsForm) form).getOperation();

                servlet.log("[DEBUG] reportsAction at perform(): Action is " + action);

                if (action.equals("add")) {

                    ArrayList membersList = new ArrayList();
                    //ArrayList temp = new ArrayList();
                    Hashtable hashMembers = new Hashtable();
                    // Se obtiene la referencia al member, a fin de saber si el 
                    // puede ejecutar reportes.
                    membersData memData = (membersData) brokerMembers.getData(user.getid(), user.getId_account());
                    System.out.println("autorizado");
                    System.out.println(memData.getreports());
                    if (memData.getreports().equalsIgnoreCase("1")) {
                        // Esta autorizado                    
                        Iterator e = brokerProjects.getListByMemberForProject("name", "ASC", user.getid(), 5, user.getId_account());

                        // Debemos convertir el iterador en un arraylist.
                        ArrayList listap = new ArrayList();
                        projectsData projectData = new projectsData();

                        while (e.hasNext()) {
                            projectData = (projectsData) e.next();
                            brokerTasks.getMembersByProject(projectData.getid(), brokerMembers, hashMembers, user.getId_account());
                            listap.add(projectData);
                        }
                        request.setAttribute("list_projects", listap);
                        //Una vez que han sido procesados todos se procede a sacarlos 
                        // del hash
                        Iterator col = hashMembers.values().iterator();
                        /// MODIFICACION KARLA CARDENAS 30-04-2009 //////////////////////////////////////////
                        /// Si no hay teams publicados se debe cargar la lista de usuarios con el usuario logueado
                        if (col.hasNext()) {
                            while (col.hasNext()) {
                                membersList.add(col.next());
                            }
                        } else {
                            membersData memberL = new membersData();
                            System.out.println("user");
                            System.out.println(user.getid());
                            memberL = (membersData) brokerMembers.getData(user.getid(), user.getId_account());
                            membersList.add(memberL);
                        }
                        /////////////////////////////////////////////////////////////////////////////////
                        Collections.sort(membersList);

                        request.setAttribute("list_members", membersList);
                        //Se define un Calendar.
                        Calendar now = Calendar.getInstance();

                        String month="";
                        if ((now.get(Calendar.MONTH)+ 1) <10){
                            month= "0"+(now.get(Calendar.MONTH)+ 1);
                        }else{
                            month=""+(now.get(Calendar.MONTH)+ 1);
                        }
                        String hoy = "" + now.get(Calendar.YEAR)
                                + "-" + month
                                + "-" + now.get(Calendar.DAY_OF_MONTH);
                        // Se extrae el componente dia, mes y a�o del start_date
                        request.setAttribute("startDate1", hoy);
                        request.setAttribute("startDate2", hoy);
                        request.setAttribute("dueDate1", hoy);
                        request.setAttribute("dueDate2", hoy);
                        request.setAttribute("dueDate3", hoy);
                        request.setAttribute("dueDate4", hoy);

                        // En el caso de la operacion add, se despliega el formulario.
                        return (mapping.findForward("add"));
                    } else {
                        return (mapping.findForward("denied"));
                    }

                } else if (action.equals("execute") || action.equals("sort")) {
                    // Se trata de la ejecucion del reporte, se procede a invocar el
                    // metodo en el broker para generar el reportte de tareas.     

                    tasksData tskData= new tasksData();
                    tasksStatusLogData tslData= new tasksStatusLogData();

                    // Se guardan en el contexto
                    request.setAttribute("list_projects", convertToString(thisForm.getprojects()));
                    request.setAttribute("list_members", convertToString(thisForm.getmembers()));
                    request.setAttribute("list_priorities", convertToString(thisForm.getpriorities()));
                    request.setAttribute("list_status", convertToString(thisForm.getstatus()));

                    Iterator e = broker.getReportByTasksForPortal(
                            thisForm.getprojects(),
                            thisForm.getmembers(),
                            thisForm.getpriorities(), thisForm.getstatus(),
                            request.getParameter("FechaInicio"),
                            request.getParameter("startDate1"), request.getParameter("startDate2"),
                            request.getParameter("FechaEntrega"),
                            request.getParameter("dueDate1"), request.getParameter("dueDate2"), user.getId_account(),
                            request.getParameter("dueDate3"), request.getParameter("dueDate4"),
                            request.getParameter("FechaMovimiento"));

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();

                    Iterator e1;

                    while (e.hasNext()) {
                            tskData= new tasksData();
                            tskData=(tasksData)e.next();
                        if (!request.getParameter("FechaMovimiento").equals("SF")){
                           

                            e1=brokerTasksLog.getListByTasks(tskData.getid(), tskData.getId_account(), "ASC");
                            boolean flag=true;
                            while (e1.hasNext()) {
                                tslData= new tasksStatusLogData();
                                tslData= (tasksStatusLogData)e1.next();
                                if (tslData.getStatus()==3 && flag){
                                    tskData.setStart_status_date(tslData.getCreated());
                                    flag=false;
                                }

                                if(tslData.getStatus()==0 ){
                                    tskData.setEnd_client_status_date(tslData.getCreated());
                                }

                                if(tslData.getStatus()==1){
                                    tskData.setEnd_status_date(tslData.getCreated());
                                }
                            }
                           // tslData= new tasksStatusLogData();
                        }
                        lista.add(tskData);
                    }
                    // Se regresa al contexto la lista de todas las tareas que satisfacen
                    // la consulta estipulada.
                    request.setAttribute("list_report", lista);
                    request.setAttribute("FechaMovimiento",request.getParameter("FechaMovimiento"));

                    // forward to display the list
                    return (mapping.findForward("view"));
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();

            } finally {
                broker.close();
                brokerOrg.close();
                brokerProjects.close();
                brokerMembers.close();
                brokerTasksLog.close();
            }
        }
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }

    /* M�todo que convierte un String[] a un String separado por comas */
    private String convertToString(String[] lista) {
        StringBuffer salida = new StringBuffer();
        for (int i = 0; i < lista.length; i++) {
            salida.append(lista[i] + ",");
        }

        return salida.toString();
    }
    // Retorna la lista de organizaciones 
  /*  private ArrayList getListing(MainBroker myBroker, String sortCol, String sortOrder) {              
    // Se obtiene el iterador sobre todos los elementos de la lista.           
    Iterator e = myBroker.getList(sortCol, sortOrder);
    // Debemos convertir el iterador en un arraylist.
    ArrayList lista = new ArrayList();
    while (e.hasNext()) {
    lista.add(e.next());
    }
    return lista;
    }        */
}
