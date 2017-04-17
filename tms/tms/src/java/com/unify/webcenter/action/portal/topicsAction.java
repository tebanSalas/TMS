/*
 * topicsAction.java
 *
 * Created on February 28, 2003, 1:08 PM
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
import com.unify.webcenter.tools.loadTimeZone;
import java.sql.Timestamp;

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
public class topicsAction extends Action {

    /** Creates a new instance of calendarAction */
    public topicsAction() {

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
        topicsBroker broker;
        postBroker postbroker;
        membersBroker memBroker;
        tasksBroker taskbroker;
        session = request.getSession(false);
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {


            // forward to display the home page
            return (mapping.findForward("portalLogin"));

        } else {
            broker = new topicsBroker();
            postbroker = new postBroker();
            memBroker = new membersBroker();
            taskbroker = new tasksBroker();
            
            // Ya hay una sesion establecida.
            try {
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");

                if (form == null) {
                    System.out.println(" Creating new topicsForm bean under key " + mapping.getAttribute());
                    form = new portalForm();
                }

                portalForm thisForm = (portalForm) form;

                // fetch action from form
                action = thisForm.getOperation();

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                projectsBroker brokerProjects = new projectsBroker();
                request.setAttribute("projectInfo", brokerProjects.getData(
                        thisForm.getproject(), user.getId_account()));
                brokerProjects.close();

                servlet.log("[DEBUG] topicsAction at perform(): Action is " + action);

                if (action == null || (action.equals("show") || action.equals("sort"))) {
                    if (request.getParameter("type_topic") != null) {
                        thisForm.setType_topic(Integer.parseInt(request.getParameter("type_topic").toString()));
                    }
                    System.out.println("Tipo");
                    System.out.println(thisForm.getType_topic());
                    // Si se trata de un showAll
                    Iterator e = broker.getListByProjectForPortal(thisForm.getsortColumn(),
                            thisForm.getsortOrder(),
                            thisForm.getproject(), 0, user.getId_account(), thisForm.getType_topic(), "1");

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    topicsData topicData = new topicsData();

                    while (e.hasNext()) {
                        topicData = (topicsData) e.next();

                        topicData.setlast_post(Timestamp.valueOf(topicData.convTimeZone(topicData.getlast_post(), user.getTime_zone())));

                        lista.add(topicData);
                    }

                    request.setAttribute("listTopics", lista);

                    if (thisForm.getOperation().equalsIgnoreCase("sort")) {
                        // Negamos el tipo de ordenamiento
                        if (thisForm.getsortOrder().equalsIgnoreCase("ASC")) {
                            thisForm.setsortOrder("DESC");
                        } else {
                            thisForm.setsortOrder("ASC");
                        }
                    }

                    return (mapping.findForward("display"));

                } else if (action.equals("view")) {
                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.

                    topicsData data = (topicsData) broker.getData(thisForm.getid(), user.getId_account());
                    data.setlast_post(Timestamp.valueOf(data.convTimeZone(data.getlast_post(), user.getTime_zone())));
                    request.setAttribute("topicInfo", data);
                    int task_id = data.gettasks();
                    tasksData taskdata = (tasksData) taskbroker.getData(task_id, user.getId_account());
                    request.setAttribute("taskdata", taskdata);
                    // Se dispone la lista de post
                    request.setAttribute("lista", postbroker.getListByTopic("created", "DESC", data.getid(), user.getTime_zone(), user.getId_account()));

                    return (mapping.findForward("view"));

                } else if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.
                    // Traer el codigo del projecto al que se lengthva asignar el topic, el codigo
                    // debe de venir por parametros del form de donde se llama esta acci�n.
                    int project_id = thisForm.getproject();

                    thisForm.setproject(project_id);
                    thisForm.setid(0);

                    String subject = request.getParameter("subject");
                    if (subject == null) {
                        subject = "";
                    }
                    request.setAttribute("subject", subject);
                        
                    return (mapping.findForward("add"));

                } else if (action.equals("applyAdd")) {
                    // Se trata de la aplicacion de un insert en la BD
                    topicsData data = new topicsData();
                    topicsBroker topicBroker= new topicsBroker();
                    // Put some values
                    data.setowner(user.getid());
                    data.setlast_post(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                    data.setstatus("1");
                    data.settotasks("0");
                    data.settasks(0);
                    data.setpublished("1");
                    data.setposts(0);
                    data.setproject(thisForm.getproject());
                    data.setId_account(user.getId_account());
                    data.setid(0);
                    data.setsubject(request.getParameter("subject"));


                    // We add the new record.
                    broker.add(data);

                    // add new record in table post with the text message                    
                    postData postdata = new postData();
                    postdata.setid(0);
                    postdata.setmember(user.getid());
                    postdata.setmessage(request.getParameter("message"));
                    postdata.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                    postdata.setId_account(user.getId_account());
                    postdata.settopic(data.getid());


                    // add the new post record
                    postbroker.add(postdata);

                    data= (topicsData)topicBroker.getData(data.getid());

                    // Instanciar clase para el envio de correo
                    com.unify.webcenter.tools.sendMail sm =
                            new com.unify.webcenter.tools.sendMail();

                    // Se toma el from con base en el due�o de la discusion
                    membersData from_member = (membersData) memBroker.getData(user.getid(), user.getId_account());
                    membersData to_member = null;


                    /*
                     *  AQUI ES SOLO PARA AVISARLE AL DUENO DEL PROYECTO, AL CREADOR Y NADIE MAS
                     */
                    projectsBroker projectsbroker = new projectsBroker();
                    projectsData dataProj =
                            (projectsData) projectsbroker.getData(data.getproject(), user.getId_account());
                    projectsbroker.close();

                    // El to es el dueno del proyecto
                    to_member = (membersData) memBroker.getData(dataProj.getowner(), user.getId_account());

                    // Se envia el email al dueno del proyecto.
                    sm.sendTopics("newTopic", from_member, to_member,
                            data.getid(), request.getParameter("message"), user, data,String.valueOf(session.getAttribute("mainUrl")));

                    if (user.getid() != to_member.getid()) {
                        // Se envia el email al creador de la tarea tambien, si y solo si
                        // no son la misma persona
                        sm.sendTopics("newTopicToCreator", from_member, from_member,
                                data.getid(), request.getParameter("message"), user, data,String.valueOf(session.getAttribute("mainUrl")));
                    }

                    /*                    
                    // Se envia un email de esta nueva discusion a cada uno de los miembros 
                    // del equipo de trabajo de este proyecto
                    teamsBroker teamBroker = new teamsBroker();
                    // Se toma la lista de miembros del equipo
                    Iterator e = teamBroker.getListByProject("id", "ASC", 
                    data.getproject(), 0);
                    teamsData teamData;
                    int memberId;
                    // Para cada miembro del equipo, se envia un correo.
                    while (e.hasNext()) {
                    teamData = (teamsData) e.next();
                    // Link al miembro
                    memberId = teamData.getmembers();
                    // Se trae el dato
                    to_member = (membersData) memBroker.getData(memberId);
                    // Se envia el email.
                    sm.send("newTopic", from_member, to_member, 
                    data.getid(), request.getParameter("message"));
                    }
                    // Se cierra el broker
                    teamBroker.close();
                     */
                    //resetToken(request);
                    // forward to display the list
                    return (new ActionForward(mapping.findForward("listTopics").
                            getPath() + "?operation=show&sortOrder=ASC&sortColumn=subject&project=" + data.getproject()));
                  
                    
                    
                }


            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();

            } finally {
                broker.close();
                postbroker.close();
                memBroker.close();
            }
        }

        // Default if everthing else fails
        return (mapping.findForward("home"));
    }
}
