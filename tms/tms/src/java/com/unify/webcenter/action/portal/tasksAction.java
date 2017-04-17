/*
 * tasksAction.java
 *
 * Created on February 28, 2003, 12:11 PM
 */
package com.unify.webcenter.action.portal;

import java.io.IOException;
import java.util.*;
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
public class tasksAction extends Action {

    /** Creates a new instance of tasksAction */
    public tasksAction() {

    }

    /**
     * Handle server requests.
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
        tasksBroker taskBroker;
        tasksStatusLogBroker tslBroker;
        session = request.getSession(false);
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {

            // forward to display the home page
            String taskid = request.getParameter("id");
            String project = request.getParameter("project");
            if (taskid != null) {
                session = request.getSession(true);
                session.setAttribute("forwardto", "task");
                session.setAttribute("taskid", taskid);
                session.setAttribute("projectid", project);
            }
            // forward to display the home page
            return (mapping.findForward("portalLogin"));

        } else {
            tslBroker= new tasksStatusLogBroker();
            taskBroker = new tasksBroker();
             tasksData data = new tasksData();
            // Intanciar clase para el envio de correo
            com.unify.webcenter.tools.sendMail sm = new com.unify.webcenter.tools.sendMail();

            try {
                if (form == null) {
                    System.out.println(" Creating new tasksForm bean under key " + mapping.getAttribute());
                    form = new portalForm();
                }

                portalForm thisForm = (portalForm) form;

                // fetch action from form
                action = thisForm.getOperation();

                System.out.println("ACTION TASKS: "+ action);
                servlet.log("[DEBUG] tasksAction at perform(): Action is " + action);

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);


                projectsBroker brokerProjects = new projectsBroker();
                request.setAttribute("projectInfo", brokerProjects.getData(
                        Integer.parseInt(request.getParameter("project").toString()), user.getId_account()));
                //  Integer.parseInt(request.getParameter("project")),user.getId_account()));                

                brokerProjects.close();

                if (action.equals("showGroup") || action.equals("sortGroup") ||
                        action.equals("showClient") || action.equals("sortClient") ||
                        action.equals("showQuote") || action.equals("sortQuote") ||
                        action.equals("aproved") || action.equals("applyRejected") ||
                        action.equals("applyCloseClient") || action.equals("search")) {
                    Iterator e;

                    String status = "";
                    String reason = "";

                    if (action.equals("aproved")) {
                        // Se trata de la aprobacion de una cotizacion.
                        data = (tasksData) taskBroker.getData(
                                Integer.parseInt(request.getParameter("id")), user.getId_account());

                        // Se pasa a estado aprobado.
                        data.setstatus(8);
                        data.setreply_quotation_member(user.getid());
                        data.setreply_quotation_date(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        taskBroker.update(data);

                    
                                    tasksStatusLogData tasksSL = new tasksStatusLogData();
                                tasksSL.setId_account(user.getId_account());
                                tasksSL.setMember(user.getid());
                                tasksSL.setStatus(data.getstatus());
                                tasksSL.setTask(data.getid());
                                tasksSL.setCreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                                
                                tslBroker.add(tasksSL);
                        // Envio de emails                        
                        status = "quoteacepted";

                    } else if (action.equals("applyRejected")) {
                        // Se trata de la aprobacion de una cotizacion.
                        data = (tasksData) taskBroker.getData(
                                Integer.parseInt(request.getParameter("id")), user.getId_account());

                        // Se pasa a estado rechazada.
                        data.setstatus(7);
                        data.setreply_quotation_member(user.getid());
                        data.setreply_quotation_date(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        taskBroker.update(data);
                    
                        tasksStatusLogData tasksSL = new tasksStatusLogData();
                        tasksSL.setId_account(user.getId_account());
                        tasksSL.setMember(user.getid());
                        tasksSL.setStatus(data.getstatus());
                        tasksSL.setTask(data.getid());
                        tasksSL.setCreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));

                        tslBroker.add(tasksSL);
                        status = "quoterejected";
                        reason = request.getParameter("reason_for_rejected");
                        if (reason == null) {
                            reason = "";
                        }

                    } else if (action.equals("applyCloseClient")) {
                        String cerrar = request.getParameter("closeIt");
                        if (cerrar != null && cerrar.equalsIgnoreCase("1")) {

                            reason = request.getParameter("reason_for_close");
                            if (reason == null) {
                                reason = "";
                            }
                            
                            // Se trata de la terminacion de una tarea
                            data = (tasksData) taskBroker.getData(
                                    Integer.parseInt(request.getParameter("id")), user.getId_account());

                            // Se pasa a estado cerrado por cliente


                             // Se actualiza la tarea correspondiente.
                            String desc = "";
                             if (data.getdescription() != null) {
                                desc = new String(data.getdescription());
                            }
                            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);


                            // Se forma la descripcion del rechazo.
                            desc += "\n------------------------------------------" +
                                    "\nTarea Aprobada el " + df.format(Calendar.getInstance().getTime()) +
                                    " por " + user.gefullname() + "\n" +
                                    reason;
                            data.setdescription(desc);

                            data.setstatus(0);
                            data.setreply_quotation_member(user.getid());
                            data.setreply_quotation_date(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                            taskBroker.update(data);

                            status = "statustaskclosed";

                    
                                    tasksStatusLogData tasksSL = new tasksStatusLogData();
                                tasksSL.setId_account(user.getId_account());
                                tasksSL.setMember(user.getid());
                                tasksSL.setStatus(data.getstatus());
                                tasksSL.setTask(data.getid());
                                tasksSL.setCreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                                
                                tslBroker.add(tasksSL);

                        } else if (request.getParameter("rejectedIt") != null &&
                                request.getParameter("rejectedIt").equalsIgnoreCase("1")) {
                            // El usuario esta rechazando la tarea
                            status = "taskrejected";
                            reason = request.getParameter("reason_for_close");
                            if (reason == null) {
                                reason = "";
                            }
                        }
                    }

                    String tmp = request.getParameter("closeIt");
                    if (action.equals("aproved") || action.equals("applyRejected") ||
                            (action.equals("applyCloseClient") &&
                            (tmp != null || request.getParameter("rejectedIt") != null))) {


                        System.out.println("PASS THRU");
                        projectsBroker projBroker = new projectsBroker();
                        projectsData project = (projectsData) projBroker.getData(
                                Integer.parseInt(request.getParameter("project")), user.getId_account());
                        projBroker.close();


                        // Se le avisa al administrador del proyecto
                        membersBroker memBroker = new membersBroker();
                        membersData mb = (membersData) memBroker.getData(user.getid(), user.getId_account());
                        membersData from_member = (membersData) memBroker.getData(project.getowner(), user.getId_account());
                        System.out.println("send");
                        System.out.println(from_member);
                        System.out.println(mb);
                        System.out.println(status);
                        System.out.println(reason);
                        System.out.println("ID de Sesion: "+ session.getId());
                        
                        if (action.equals("aproved") || action.equals("applyRejected") || (action.equals("applyCloseClient"))){
                        sm.send(status, from_member, mb,
                                Integer.parseInt(request.getParameter("id")),
                                reason, user,String.valueOf(session.getAttribute("mainUrl")));
                        
                        Iterator listaAdm = memBroker.getQuotationMembers(""+user.getId_account());
                                while (listaAdm.hasNext()) {
                                    membersData mb1 = (membersData) listaAdm.next();
                                    sm.send(status, from_member, mb1, 
                                            Integer.parseInt(request.getParameter("id")), reason, user,String.valueOf(session.getAttribute("mainUrl")));
                                }
                                
                         sm.send(status, from_member, project.getparentOwner(), 
                                 Integer.parseInt(request.getParameter("id")), reason, user,String.valueOf(session.getAttribute("mainUrl")));
                                
                        
                        }
                        
                        if (tmp!=null){
                            //Se le notifica al Dueño de la Tarea la Finalización Cliente de la misma
                              data = (tasksData) taskBroker.getData(
                                    Integer.parseInt(request.getParameter("id")), user.getId_account());
                              mb = (membersData) memBroker.getData(data.getassigned_to());
                            sm.send(status, from_member, mb,
                                Integer.parseInt(request.getParameter("id")),
                                reason, user,String.valueOf(session.getAttribute("mainUrl")));
                        }
                            // mb = (membersData) memBroker.getData(1, user.getId_account());
                            mb = (membersData) memBroker.getAdmin(user.getId_account(), 3);
                          System.out.println(mb.getid()+" / "+ mb.getemail_work());
                            sm.send(status, from_member, mb,
                                    Integer.parseInt(request.getParameter("id")),
                                    reason, user,String.valueOf(session.getAttribute("mainUrl")));

                        System.out.println("YA LO ENVIO");
                        if (request.getParameter("rejectedIt") != null) {
                               data = (tasksData) taskBroker.getData(
                                    Integer.parseInt(request.getParameter("id")), user.getId_account());
                         // Se actualiza la tarea correspondiente.
                            String desc = "";
                             if (data.getdescription() != null) {
                                desc = new String(data.getdescription());
                            }
                            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);


                            // Se forma la descripcion del rechazo.
                            desc += "\n------------------------------------------" +
                                    "\nTarea Rechazada el " + df.format(Calendar.getInstance().getTime()) +
                                    " por " + user.gefullname() + "\n" +
                                    reason;
                            data.setdescription(desc);
                            data.setstatus(12);
                            taskBroker.update(data);
                            // Si es rechazada, tambien le aviso al admin
                            
                          //  mb = (membersData) memBroker.getData(1, user.getId_account());
                            mb = (membersData) memBroker.getAdmin(user.getId_account(), 3);
                            sm.send(status, from_member, mb,
                                    Integer.parseInt(request.getParameter("id")),
                                    reason, user,String.valueOf(session.getAttribute("mainUrl")));
                            
                            
                            
                            // Si es rechazada, le aviso al dueño de la tarea
                             mb = (membersData) memBroker.getData(data.getassigned_to(), user.getId_account());
                             sm.send(status, from_member, mb,
                             Integer.parseInt(request.getParameter("id")),
                             reason, user,String.valueOf(session.getAttribute("mainUrl")));
                            
                           
                                                
                                    tasksStatusLogData tasksSL = new tasksStatusLogData();
                                tasksSL.setId_account(user.getId_account());
                                tasksSL.setMember(user.getid());
                                tasksSL.setStatus(data.getstatus());
                                tasksSL.setTask(data.getid());
                                tasksSL.setCreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                                
                                tslBroker.add(tasksSL);
                        }
                        memBroker.close();
                    }

                    // De acuerdo a lo que se despliega, se despliegan o las tareas del grupo
                    // o las del usuario
                    System.out.println("VA A DESPLEGAR");
                    System.out.println(action);
                    if (action.equals("showGroup") || action.equals("sortGroup") || action.equals("applyCloseClient") || action.equals("search")) {
                        if (request.getParameter("type_task") != null) {
                            thisForm.setType_task(Integer.parseInt(request.getParameter("type_task").toString()));
                        }        
                        if (request.getParameter("task") != null) {
                            thisForm.setTask(request.getParameter("task"));
                        }        
                        e = taskBroker.getListByProjectGroupForPortal(thisForm.getsortColumn(),
                                thisForm.getsortOrder(),
                                thisForm.getproject(), 0,
                                user.getid(), user.getId_account(), thisForm.getType_task(), thisForm.getTask());
                    } else if (action.equals("showClient") || action.equals("sortClient")) {

                        // Solo las tareas del cliente
                        e = taskBroker.getListByProjectClientForPortal(thisForm.getsortColumn(),
                                thisForm.getsortOrder(),
                                thisForm.getproject(), 0,
                                user.getid(), user.getId_account());

                    } else {
                        // Se trata de las cotizaciones.
                        e = taskBroker.getListByProjectQuotesForPortal(thisForm.getsortColumn(),
                                thisForm.getsortOrder(),
                                thisForm.getproject(), 0, user.getId_account());


                        // Con base en el perfil del usuario, se determina si puedo ver
                        // o no las cotizaciones a fin de que las apruebe.
                        membersBroker memBroker = new membersBroker();
                        membersData memData = (membersData) memBroker.getData(user.getid(), user.getId_account());
                        memBroker.close();

                        // Se guarda en el contexto
                        request.setAttribute("aproveQuotes", memData.getquotation());
                    }

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        //           tasksData data = (tasksData)e.next();
                        lista.add(e.next());
                        System.out.println("lista tareas");
                    //  System.out.println(data.getdescription());
                    //   System.out.println(data.getcomments());
                    }


                    // Se retorna la lista de las tareas de este proyecto nada mas.
                    request.setAttribute("listTasks", lista);

                    // Se modifica el siguiente sorting
                    if (thisForm.getOperation().equalsIgnoreCase("sortGroup") ||
                            thisForm.getOperation().equalsIgnoreCase("sortQuote") ||
                            thisForm.getOperation().equalsIgnoreCase("sortClient") ||
                            thisForm.getOperation().equalsIgnoreCase("search")) {
                        // Negamos el tipo de ordenamiento
                        if (thisForm.getsortOrder().equalsIgnoreCase("ASC")) {
                            thisForm.setsortOrder("DESC");
                        } else {
                            thisForm.setsortOrder("ASC");
                        }
                    }

                    // Se despliega el vm apropiado de acuerdo al origen
                    if (action.equals("showGroup") || action.equals("sortGroup") || action.equals("applyCloseClient") ||
                            thisForm.getOperation().equalsIgnoreCase("search")) {
                        return (mapping.findForward("displayGroup"));
                    } else if (action.equals("showClient") || action.equals("sortClient")) {
                        return (mapping.findForward("displayClient"));
                    } else {
                        return (mapping.findForward("displayQuotes"));
                    }

                } else if (action.equals(
                        "viewGroup")) {
                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.
                    
                    data = (tasksData) taskBroker.getData(
                            Integer.parseInt(request.getParameter("id")), user.getId_account());

                    request.setAttribute("taskInfo", data);
                    // Y ademas ponemos un listado de los archivos que tiene ligados
                    filesBroker fileBrk = new filesBroker();
                    Iterator e = fileBrk.getListByTaskForPortal("id",
                            "ASC", data.getid(), 0, user.getId_account());
                    fileBrk.close();

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        lista.add(e.next());
                    }

                    // Se guarda la lista de files asociados a esta tarea.
                    request.setAttribute("listFiles", lista);

                    return (mapping.findForward("view"));

                } else if (action.equals(
                        "viewClient")) {
                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.
                    data = (tasksData) taskBroker.getData(
                            Integer.parseInt(request.getParameter("id")), user.getId_account());

                    request.setAttribute("taskInfo", data);

                    return (mapping.findForward("viewClient"));

                } else if (action.equals(
                        "rejected")) {
                    request.setAttribute("id", request.getParameter("id"));
                    request.setAttribute("project", request.getParameter("project"));

                    data = (tasksData) taskBroker.getData(
                            Integer.parseInt(request.getParameter("id")), user.getId_account());

                    request.setAttribute("taskInfo", data);
                              tasksStatusLogData tasksSL = new tasksStatusLogData();
                                tasksSL.setId_account(user.getId_account());
                                tasksSL.setMember(user.getid());
                                tasksSL.setStatus(data.getstatus());
                                tasksSL.setTask(data.getid());
                                tasksSL.setCreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                                
                                tslBroker.add(tasksSL);
                    return (mapping.findForward("confirmReject"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                return (mapping.findForward("home"));

            } finally {
                // Se cierran los brokers definidos.
                tslBroker.close();
                taskBroker.close();
            }

        }
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }
}
