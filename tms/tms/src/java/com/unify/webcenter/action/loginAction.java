/*
 * loginAction.java
 *
 * Created on January 27, 2003, 10:24 AM
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
import com.unify.webcenter.conf.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.tools.sendMail;
import org.apache.ojb.broker.query.Criteria;
import org.postgresql.core.v2.QueryExecutorImpl;


/**
 *
 * @author  Administrator
 */
public class loginAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");
    static final long ONE_HOUR = 60 * 60 * 1000L;

     public static long daysBetween(Date d1, Date d2) {        
        if ((d2.getDate() == d1.getDate()) && (d1.getYear() == d2.getYear())) {
            return 1;
        } else {
            return ((d2.getTime() - d1.getTime() + ONE_HOUR) /
                    (ONE_HOUR * 24));
        }
    }


    /** Creates a new instance of loginAction */
    public loginAction() {
        try {
            // Se habilita el servicio de logging a un file.
            FileHandler fh = new FileHandler("tms.log",
                    20000000, 5, true);

            // Se asocia un handler al logger definido.            
            logger.addHandler(fh);

            // Request that every detail gets logged.
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
            logger.logp(Level.SEVERE, "TMS",
                    "constructor", "Fatal Error Creating log file: " +
                    e.toString());
        }

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
        membersBroker broker;
        accountsBroker accountBroker;
        accountsData accountData;
        broker = new membersBroker();

        loginData login = new loginData();

            accountBroker = new accountsBroker();
            accountData = new accountsData();

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

            servlet.log("[DEBUG] loginAction at perform(): Action is " + action);
            logger.logp(Level.FINEST, "TMS Login", "performAction",
                    "[DEBUG] loginAction at perform(): Action is " + action);

            // Cargar los valores del form
            String username = thisForm.getusername();
            String password = thisForm.getpassword();
            String language = thisForm.getlanguage();

        //Set the selected tab 
            session.setAttribute("current","login");
            // Guardar el lenguaje
            if (language.equals("es")) {
                session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale("es", ""));
            } else {
                session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale("en", ""));
            }


            // Determine what to do
            if (action == null || action.equals("display") || action.equals("")) {
                thisForm.setOperation("login");
                return (mapping.findForward("login"));
            } else if (action.equals("login")) {

                session.setAttribute("version", TMSConfigurator.getVersion());
                errors = thisForm.validate(mapping, request);

                if (errors.isEmpty()) {
                    membersData member = (membersData) broker.checkLogin(username, password);

                    long days = check(member.getExpired_date());
                    if (days > 0) {
                        if (session.getAttribute("days") != null) {
                            session.removeAttribute("days");
                        }

                        if (days <= 7) {
                            session.setAttribute("days", String.valueOf(days));
                        }

                        accountData = (accountsData) accountBroker.getData(member.getId_account());
                        session.setAttribute("systemName",accountData.getCreator());
                        
                        if (member != null && (accountData.getActive().equals("S"))) {

                            // Si el usuario NO es del lado del portal, se procede a logearlo
                            if (member.getprofile().equals("2") == false) {
                                // Save our logged-in user in the session,
                                // because we use it again later.
                                session = request.getSession();

                                login.setusername(username);
                                login.setid(member.getid());
                                login.setfullname(member.getname());
                                login.setlanguage(language);
                                login.setTemplate(member.getTemplate());
                                login.setprofile(member.getprofile());
                                if (member.getTime_zone() != null) {
                                    login.setTime_zone(member.getTime_zone());
                                } else {
                                    login.setTime_zone(TimeZone.getDefault().getDisplayName());
                                }

                                login.setId_account(member.getId_account());

                                //Atributo que permite la evaluacion en el menu de reportes 
                                //para activar la opcion de ejecutar reporte de actividades
                                login.setInd_exec_report(member.getInd_exec_report());
                                login.setViewAppControl(member.getPermission_view_app_control());
                                login.setAppControl(member.getPermission_app_control());
                                login.setReportApplications(member.getPermission_report_applications());
                                login.setVersionControl(member.getPermission_version_control());
                                
                                String company = TMSConfigurator.getCompany(login);
                                
                                request.setAttribute("company", company);
                              
                                request.setAttribute("version", TMSConfigurator.getVersion());


                                session.setAttribute("login", login);

                                // Se agrega al contexto la informacion del usuario
                                request.setAttribute("userInfo", login);

                                // Como el logging fue exitoso, se procede a crear un
                                // registro nuevo en la tabla de logs.
                                Calendar calendar = Calendar.getInstance();

                                session.setAttribute("mainUrl", accountData.getMain_url());
                                accountBroker.close();
                                broker.close();
                                String forwardto = (String) session.getAttribute("forwardto");
                                if (forwardto != null) {
                                    if (forwardto.equalsIgnoreCase("task")) {
                                        String taskid = (String) session.getAttribute("taskid");
                                        String projectid = (String) session.getAttribute("projectid");
                                        session.removeAttribute("forwardto");
                                        session.removeAttribute("taskid");
                                        return (new ActionForward(mapping.findForward("back_to_task").getPath() + taskid));
                                    } else if (forwardto.equalsIgnoreCase("topic")) {
                                        String topicid = (String) session.getAttribute("topicid");
                                        session.removeAttribute("forwardto");
                                        session.removeAttribute("topicid");
                                        return (new ActionForward(mapping.findForward("back_to_topic").getPath() + topicid));
                                    } else {
                                        return (mapping.findForward("continue"));
                                    }
                                } else {
                                    return (mapping.findForward("continue"));
                                }
                            } else {
                                // Es un usuario del cliente, no se puede ingresar al portal Administrativo
                                thisForm.setusername(username);
                                errors.add("username", new ActionError("errors.login.invalidAdminSite"));
                                saveErrors(request, errors);
                                return (mapping.findForward("login"));
                            }
                        } else {
                            // Es un usuario del cliente, no se puede ingresar al portal Administrativo
                            thisForm.setusername(username);
                            errors.add("expiration", new ActionError("errors.expiration"));
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
                thisForm.setOperation("login");
                // Se redirecciona a la ventana de login
                return (mapping.findForward("logout"));
            }
//            sendMail sm= new sendMail();
//            sm.sendAM();
        } catch (Exception e) {
            servlet.log("[ERROR] Action at final catch: " + e.getMessage());
            e.printStackTrace();
            logger.logp(Level.SEVERE, "TMS Login", "performAction",
                    "[ERROR] Action at final catch: " + e.getMessage());


        } finally {
            // Se cierran los brokers abiertos
            broker.close();
            accountBroker.close();
        }
        // Default if everthing else fails
        return (mapping.findForward("login"));
    }

    private long check(Date expirationDate) {
        Date date = new Date();
       
        Calendar calendar = Calendar.getInstance();

        date = calendar.getTime();
     
        date.setHours(expirationDate.getHours());
        date.setMinutes(expirationDate.getMinutes());
        date.setSeconds(expirationDate.getSeconds());

        return daysBetween(new Date(date.getTime()), new Date(expirationDate.getTime()));

    }
}
