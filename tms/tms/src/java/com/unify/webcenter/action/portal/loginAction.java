/*
 * loginAction.java
 *
 * Created on January 27, 2003, 10:24 AM
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
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.crons.sendNotificationEndOfTrial;
import com.unify.webcenter.form.*;

/**
 *
 * @author  Administrator
 */
public class loginAction extends Action {

    /** Creates a new instance of loginAction */
    public loginAction() {

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
        membersBroker broker = new membersBroker();
        accountsBroker accBroker = new accountsBroker();
        try {
            session = request.getSession();
            ActionErrors errors = new ActionErrors();

            if (form == null) {
                System.out.println(" Creating new loginForm bean under key " + mapping.getAttribute());
                form = new loginForm();
            }

            loginForm thisForm = (loginForm) form;

            // fetch action from form
            action = thisForm.getOperation();

            servlet.log("[DEBUG] loginAction at JURY PORTAL perform(): Action is " + action);

            // Cargar los valores del form
            String username = thisForm.getusername();
            String password = thisForm.getpassword();
            String language = thisForm.getlanguage();

            // Guardar el lenguaje
            if (language.equals("es")) {
                session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale("es", ""));
            } else {
                session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale("en", ""));
            }



            // Determine what to do
            if (action == null || action.equals("display")) {
                thisForm.setOperation("login");

                return (mapping.findForward("login"));
            } else if (action.equals("login")) {

                errors = thisForm.validate(mapping, request);

                if (errors.isEmpty()) {

                    membersData member = broker.checkLogin(username, password);
                    accountsData account = (accountsData)accBroker.getData(member.getId_account());
                    broker.close();
                    accBroker.close();
                    if (member != null) {

                        // Si el usuario DEBE ser del lado del portal
                        if (member.getprofile().equals("2")) {

                            // Se debe de verificar que el usuario tenga al menos un proyecto.
                            projectsBroker brokerProjects = new projectsBroker();
                            Iterator e = brokerProjects.getListByOrganizationForPortal("name",
                                    "DESC", member.getorganization(), 0, member.getId_account());
                            brokerProjects.close();

                            // Debemos convertir el iterador en un arraylist.
                            ArrayList lista = new ArrayList();
                            while (e.hasNext()) {
                                lista.add(e.next());
                            }
                           
                             if (lista.size() > 0) {
                            // Se define como proyecto default el primero
                               request.setAttribute("projectInfo",(projectsData)lista.get(0));

                            // Save our logged-in user in the session,
                            // because we use it again later.
                            session = request.getSession();
                            loginData login = new loginData();
                            login.setusername(username);
                            login.setid(member.getid());
                            login.setfullname(member.getname());
                            login.setlanguage(language);
                            login.setTemplate(member.getTemplate());
                            login.setAllowCloseTasks(member.getind_end_tasks());
                            if (member.getTime_zone() != null) {
                                login.setTime_zone(member.getTime_zone());
                            } else {
                                login.setTime_zone(TimeZone.getDefault().getDisplayName());
                            }
 
                            login.setId_account(member.getId_account());

                            session.setAttribute("loginPortal", login);
                            
                            String company = TMSConfigurator.getCompany(login);
                            session.setAttribute("systemName", account.getCreator());
                            session.setAttribute("company", company);
                            session.setAttribute("mainUrl", account.getMain_url() );

                            TMSConfigurator tms= new TMSConfigurator(login);
                            session.setAttribute("downloadPath", tms.getDownloadPath());
                            // Se agrega al contexto la informacion del usuario
                            request.setAttribute("userInfo", login);

                                String forwardto = (String) session.getAttribute("forwardto");
                                if (forwardto != null) {
                                    if (forwardto.equalsIgnoreCase("task")) {
                                        String taskid = (String) session.getAttribute("taskid");
                                        String projectid = (String) session.getAttribute("projectid");
                                        
                                        session.removeAttribute("forwardto");
                                        session.removeAttribute("taskid");
                                        session.removeAttribute("projectid");
                                        
                                        request.setAttribute("id", taskid);
                                        request.setAttribute("project", projectid);
                                        request.setAttribute("systemName", account.getCreator());
                                        System.out.println("MAP "+mapping.findForward("back_to_task_portal").getPath() + taskid + 
                                                mapping.findForward("back_to_topic_portal").getPath() + projectid);
                                        return (new ActionForward(mapping.findForward("back_to_task_portal").getPath() + taskid + 
                                                mapping.findForward("back_to_topic_portal").getPath() + projectid));
                                    
                                    } else {
                                        return (mapping.findForward("continue"));
                                    }
                                } else {
                                    return (mapping.findForward("continue"));
                                }
                         } else {
                               errors.add("username", new ActionError("errors.thereAreNoProjects.wrong"));
                                saveErrors(request, errors);
                                return (mapping.findForward("login"));                            
                            }    
                        } else {
                            // Es un usuario del cliente, no se puede ingresar al portal Administrativo
                            thisForm.setusername(username);
                            errors.add("username", new ActionError("errors.login.invalidClientSite"));
                            saveErrors(request, errors);
                            return (mapping.findForward("login"));
                        }

                    } else {
                        thisForm.setusername(username);
                        errors.add("username", new ActionError("errors.login.wrong"));
                        saveErrors(request, errors);
                        return (mapping.findForward("login"));
                    }
                } else {
                    thisForm.setusername(username);
                    saveErrors(request, errors);
                    return (mapping.findForward("login"));
                }

            } else if (action.equals("logout")) {
                // Se destruye la session.
                session.invalidate();

                // Se redirecciona a la ventana de login
                return (mapping.findForward("logout"));
            }
        } catch (Exception e) {
            servlet.log("[ERROR] Action at final catch: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Se cierran los brokers abiertos
            broker.close();
        }
        // Default if everthing else fails
        return (mapping.findForward("login"));
    }
}
