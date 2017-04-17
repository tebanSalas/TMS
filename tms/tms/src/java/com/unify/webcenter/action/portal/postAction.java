/*
 * postAction.java
 *
 * Created on February 28, 2003, 5:44 PM
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
public class postAction extends Action {

    /** Creates a new instance of calendarAction */
    public postAction() {

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
        postBroker broker;
        projectsBroker projectsbroker;
        topicsBroker topicsbroker;
        membersBroker membersbroker;

        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {
            // forward to display the home page

            return (mapping.findForward("portalLogin"));

        } else {
            broker = new postBroker();
            topicsbroker = new topicsBroker();
            projectsbroker = new projectsBroker();
            membersbroker = new membersBroker();
            try {

                // Ya hay una sesion establecida.
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");

                if (form == null) {
                    System.out.println(" Creating new postForm bean under key " + mapping.getAttribute());
                    form = new portalForm();
                }

                portalForm thisForm = (portalForm) form;
                request.setAttribute("projectInfo", projectsbroker.getData(
                        thisForm.getproject(),user.getId_account()));

                // fetch action from form
                action = thisForm.getOperation();

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                servlet.log("[DEBUG] postAction  at perform(): Action is " + action);

                // Determine what to do
                if (action.equals("add")) {
                    // Se cargan los datos del topic
                    int topic_id = thisForm.getid();
                    topicsData topicdata = (topicsData) topicsbroker.getData(topic_id,user.getId_account());
                    topicdata.setlast_post(Timestamp.valueOf(topicdata.convTimeZone(topicdata.getlast_post(), user.getTime_zone())));
                    request.setAttribute("topicInfo", topicdata);

                    thisForm.setOperation("applyAdd");
                    thisForm.setid(0);

                    //Put the post's list of this topic
                    java.util.Vector e = topicdata.getdetailPost();
                    ArrayList lista = new ArrayList();
                    postData postData = new postData();
                    for (int i = 0; i < e.size(); i++) {
                        postData = (postData) e.get(i);
                        postData.setcreated(Timestamp.valueOf(postData.convTimeZone(postData.getcreated(), user.getTime_zone())));
                        lista.add(postData);
                    }

                    request.setAttribute("lista", lista);

                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("applyAdd")) {
                    // Se trata de la aplicacion de un insert en la BD
                    postData data = new postData();

                    data.setid(0);
                    data.setId_account(user.getId_account());
                    data.setmember(user.getid());
                    data.setmessage(request.getParameter("message"));
                    data.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                    data.settopic(thisForm.getid());

                    // We add the new record.
                    broker.add(data);

                    // Instanciar clase para el envio de correo
                    com.unify.webcenter.tools.sendMail sm = new com.unify.webcenter.tools.sendMail();

                    // Se toma el from con base en el due�o de la discusion
                    membersData from_member = (membersData) membersbroker.getData(user.getid(),user.getId_account());
                    membersData to_member = null;

                    // se toma el id del topic
                    topicsData topicsdata = (topicsData) topicsbroker.getData(data.gettopic(),user.getId_account());
                    /*
                     *  AQUI ES SOLO PARA AVISARLE AL DUENO DEL PROYECTO, AL QUE LO HACE 
                     *  Y AL DUENO DE LA DISCUSION
                     */
                    projectsData dataProj = (projectsData) projectsbroker.getData(topicsdata.getproject(),user.getId_account());

                    // El to es el dueno del proyecto
                    to_member = (membersData) membersbroker.getData(dataProj.getowner(),user.getId_account());

                    // Se envia el email al dueno del proyecto
                    sm.sendTopics("newPost", from_member, to_member,
                            data.gettopic(), request.getParameter("message"),user, topicsdata,String.valueOf(session.getAttribute("mainUrl")));

                    if (dataProj.getowner() != topicsdata.getowner()) {
                        // Se envia el email al dueno de la discusion
                        to_member = (membersData) membersbroker.getData(topicsdata.getowner(),user.getId_account());

                        sm.sendTopics("newPost", from_member, to_member,
                                data.gettopic(), request.getParameter("message"),user, topicsdata,String.valueOf(session.getAttribute("mainUrl")));
                    }

                    if (dataProj.getowner() != user.getid() &&
                            topicsdata.getowner() != user.getid()) {
                        // Se envia el email al que puso el post, siempre y cuando
                        // no sea el dueno no el del proyecto.
                        to_member = (membersData) membersbroker.getData(user.getid(),user.getId_account());
                        sm.send("newPost", from_member, to_member,
                                data.gettopic(), request.getParameter("message"),user,String.valueOf(session.getAttribute("mainUrl")));
                    }

                    /*                    
                    // Se envia un email de esta nueva discusion a cada uno de los miembros 
                    // del equipo de trabajo de este proyecto
                    teamsBroker teamBroker = new teamsBroker();
                    // Se toma la lista de miembros del equipo
                    Iterator e = teamBroker.getListByProject("id", "ASC", 
                    topicsdata.getproject(), 0);
                    teamsData teamData;
                    int memberId;
                    // Para cada miembro del equipo, se envia un correo.
                    while (e.hasNext()) {
                    teamData = (teamsData) e.next();
                    // Link al miembro
                    memberId = teamData.getmembers();
                    // Se trae el dato
                    to_member = (membersData)membersbroker.getData(memberId);
                    // Se envia el email.
                    sm.send("newPost", from_member, to_member, 
                    data.gettopic(), request.getParameter("message") );
                    }
                    // Se cierra el broker
                    teamBroker.close();
                     */
                    // Se actualiza la ultima modificacion
                    topicsData topicdata = (topicsData) topicsbroker.getData(thisForm.getid(),user.getId_account());
                    topicdata.setlast_post(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                    topicsbroker.update(topicdata);

                    // forward to display the list
                    return (new ActionForward(mapping.findForward("listTopics").
                            getPath() + "?operation=show&sortOrder=ASC&sortColumn=subject&project=" + topicdata.getproject()));

                }
            } catch (Exception e) {

                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();
                return (mapping.findForward("home"));
            } finally {
                broker.close();
                topicsbroker.close();
                projectsbroker.close();
                membersbroker.close();
            }
            // Default if everthing else fails
            return (mapping.findForward("home"));
        }
    }

    // Retorna la lista de organizaciones 
    private ArrayList getListing(String sortCol, String sortOrder, postBroker broker, int accountId) {
        Iterator e;
        // Se obtiene el iterador sobre todos los elementos de la lista.

        if (sortCol.equals("")) {
            e = broker.getList(accountId);
        } else {
            e = broker.getList(sortCol, sortOrder,accountId);
        }

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }

        return lista;
    }
}
