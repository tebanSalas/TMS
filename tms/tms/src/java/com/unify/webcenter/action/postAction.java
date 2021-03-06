/*
 * Generated by Flecha Roja Technologies Auto Generator
 *
 * Created on January 9, 2003, 11:31 AM
 */
package com.unify.webcenter.action;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.form.*;
import com.unify.webcenter.conf.TMSConfigurator;
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

    private static Logger logger = Logger.getLogger("com.unify");

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
        String company = null;

        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page

            String topicid = request.getParameter("id");
            if (topicid != null) {
                session = request.getSession(true);
                session.setAttribute("forwardto", "topic");
                session.setAttribute("topicid", topicid);
            }
            
            request.setAttribute("company", company);
            return (mapping.findForward("login"));

        } else {
            broker = new postBroker();
            topicsbroker = new topicsBroker();
            projectsbroker = new projectsBroker();
            membersbroker = new membersBroker();
            try {
                session = request.getSession(false);
                 // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
                servlet.log(" ESTOY EN POSTACTION ACTION ");
                // Si la sesion es nula, se debe redireccionar al login.
                if (session == null || session.getAttribute("login") == null) {
                    // forward to display the home page
                    broker.close();
                    topicsbroker.close();
                    projectsbroker.close();
                    membersbroker.close();

                    // forward to display the home page
                    String topicid = request.getParameter("topic");
                    if (topicid != null) {
                        session = request.getSession(true);
                        session.setAttribute("forwardto", "topic");
                        session.setAttribute("topicid", topicid);
                    }
                    request.setAttribute("company", company);
                    return (mapping.findForward("login"));

                } else {
                    // Ya hay una sesion establecida.

                   

                    if (form == null) {
                        System.out.println(" Creating new postForm bean under key " + mapping.getAttribute());
                        form = new postForm();
                    }

                    postForm thisForm = (postForm) form;

                    // fetch action from form
                    action = ((postForm) form).getOperation();

                    // Se agrega al contexto la informacion del usuario
                    request.setAttribute("userInfo", user);

                    servlet.log("[DEBUG] postAction at perform(): Action is " + action);

                    // Determine what to do
                    // Determine what to do
                    if (action.equals("add")) {
                        // Se cargan los datos del topic
                        int topic_id = thisForm.gettopic();
                        topicsData topicdata = (topicsData) topicsbroker.getData(topic_id,user.getId_account());
                        topicdata.setlast_post(Timestamp.valueOf(topicdata.convTimeZone(topicdata.getlast_post(), user.getTime_zone())));

                        request.setAttribute("topicdata", topicdata);

                        // Se cargan los datos del projecto
                        int project_id = topicdata.getproject();
                        projectsData projectdata = (projectsData) projectsbroker.getData(project_id,user.getId_account());
                        request.setAttribute("projectdata", projectdata);

                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute",
                                "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                                "<a href='./projects.do?operation=view&id=" +
                                projectdata.getid() + "'>" +
                                projectdata.getname() + "</a>&nbsp;/" +
                                "<a href='./topics.do?operation=view&id=" +
                                topicdata.getid() + "'>" +
                                topicdata.getsubject() + "</a>&nbsp;/" +
                                "&nbsp;" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addPost"));

                        // En el caso de la operacion add, se despliega el formulario.
                        request.setAttribute("title", java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addLabel"));
                        thisForm.setOperation("applyAdd");
                        thisForm.setid(0);
                        thisForm.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));

                        //Put the post's list of this topic
                        java.util.Vector e = topicdata.getdetailPost();
                        ArrayList lista = new ArrayList();
                        postData postData = new postData();
                        for (int i = 0; i < e.size(); i++) {
                            postData = (postData)e.get(i);
                            postData.setcreated(Timestamp.valueOf(postData.convTimeZone(postData.getcreated(), user.getTime_zone())));
                            lista.add(postData);
                        }

                        request.setAttribute("lista", lista);
                        request.setAttribute("company", company);
                        return (mapping.findForward("displayAddForm"));

                    } else if (action.equals("view")) {
                        // Si se trata de un view, se debe de cargar los datos del bean
                        // en el formulario.
                        postData data = (postData) broker.getData(thisForm.getid(),user.getId_account());
                        data.setcreated(Timestamp.valueOf(data.convTimeZone(data.getcreated(), user.getTime_zone())));

                        request.setAttribute("title",
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.viewPost"));

                        // We copy all the properties from the form to the bean.
                        PropertyUtils.copyProperties(thisForm, data);
                        thisForm.setOperation("view");
                        request.setAttribute("company", company);
                        return (mapping.findForward("displayViewForm"));

                    } else if (action.equals("delete")) {
                        // Tomamos el bean correspondiente a este objeto a fin
                        // de proceder con su borrado
                        postData data = (postData) broker.getData(thisForm.getid(),user.getId_account());

                        // We delete the object in the DBMS
                        broker.delete(data);
                        request.setAttribute("company", company);
                        // forward to display the list
                        return (mapping.findForward("listing"));

                    } else if (action.equals("applyAdd")) {
                        // Se trata de la aplicacion de un insert en la BD
                        postData data = new postData();

                        data.setid(0);
                        data.setmember(user.getid());
                        data.setmessage(thisForm.getmessage());
                        data.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        data.settopic(thisForm.gettopic());
                            data.setId_account(user.getId_account());
                        // We add the new record.
                        broker.add(data);

                        // Instanciar clase para el envio de correo
                        com.unify.webcenter.tools.sendMail sm =
                                new com.unify.webcenter.tools.sendMail();

                        // Se toma el from con base en el due�o de la discusion
                        membersData from_member = (membersData) membersbroker.getData(user.getid(),user.getId_account());
                        membersData to_member = null;

                        // se toma el id del topic
                        topicsData topicsdata = (topicsData) topicsbroker.getData(data.gettopic(),user.getId_account());

                        /*
                         *  AQUI ES SOLO PARA AVISARLE AL DUENO DEL PROYECTO, AL QUE LO HACE 
                         *  Y AL DUENO DE LA  DISCUSION
                         */
                        projectsData dataProj =
                                (projectsData) projectsbroker.getData(topicsdata.getproject(),user.getId_account());

                        // El to es el dueno del proyecto
                        to_member = (membersData) membersbroker.getData(dataProj.getowner(),user.getId_account());

                        // Se envia el email al dueno del proyecto
                        sm.sendTopics("newPost", from_member, to_member,
                                data.gettopic(), thisForm.getmessage(),user, topicsdata,String.valueOf(session.getAttribute("mainUrl")));


                        if (dataProj.getowner() != topicsdata.getowner()) {
                            // Se envia el email al dueno de la discusion
                            to_member = (membersData) membersbroker.getData(topicsdata.getowner(),user.getId_account());

                            sm.sendTopics("newPost", from_member, to_member,
                                    data.gettopic(), thisForm.getmessage(),user, topicsdata,String.valueOf(session.getAttribute("mainUrl")));
                        }


                        if (dataProj.getowner() != user.getid() &&
                                topicsdata.getowner() != user.getid()) {
                            // Se envia el email al que puso el post, siempre y cuando
                            // no sea el dueno no el del proyecto.
                            to_member = (membersData) membersbroker.getData(user.getid(),user.getId_account());
                             sm.sendTopics("newPost", from_member, to_member,
                                    data.gettopic(), thisForm.getmessage(),user, topicsdata,String.valueOf(session.getAttribute("mainUrl")));
                        }


                        /*  
                         *  ESTAS LINEAS ESTABAN PARA QUE CADA MIEMBRO DEL EQUIPO SUPIERA
                         *  CUANDO SE ABRIA O RESPONDIA A UNA DISCUSION, POR RECOMENDACION 
                         *  AHORA SOLO LE AVISA AL DUENO DEL PROYECTO
                         *                  
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
                        data.gettopic(), thisForm.getmessage());                        
                        }  
                        // Se cierra el broker
                        teamBroker.close();
                         */
                        // Se actualiza la ultima modificacion
                        topicsData tmp = (topicsData) topicsbroker.getData(thisForm.gettopic(),user.getId_account());
                        tmp.setlast_post(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        topicsbroker.update(tmp);

                        // forward to display the list
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("back").getPath() + "?operation=view&id=" + thisForm.gettopic()));
                    //return new ActionForward("/topics.do?operation=view&topic=172");

                    }
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();
                logger.logp(Level.SEVERE, "post.do", "perform",
                        "Fatal Error: " + e.toString());
            } finally {
                broker.close();
                topicsbroker.close();
                projectsbroker.close();
                membersbroker.close();
            }
            // Default if everthing else fails
            request.setAttribute("company", company);
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
