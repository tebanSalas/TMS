/*
 * administrationAction.java
 *
 * Created on February 17, 2003, 4:47 PM
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.accountsBroker;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.conf.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.form.adminForm;

/**
 * Action que se encarga de desplegar el menu relativo a la administracion del
 * sitio e informacion adicional.
 *
 * @author  Gerardo Arroyo Arce
 */
public class administrationAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of administrationAction */
    public administrationAction() {

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

        String company = null;
        HttpSession session = request.getSession(false);

        accountsBroker accountBroker;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the login page
            return (mapping.findForward("login"));
        } else {
            
            accountBroker = new accountsBroker();
            
            // Si el usuario esta logeado se le despliega el menu
            try {
 //Set the selected tab 
            session.setAttribute("current","admin");
                if (form == null) {
                    System.out.println(" Creating new membersForm bean under key " + mapping.getAttribute());
                    form = new adminForm();
                }

                adminForm thisForm = (adminForm) form;

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");

                TMSConfigurator tms = new TMSConfigurator(user);
                request.setAttribute("version", TMSConfigurator.getVersion());

                company =tms.getCompany(user);

                if (user.isAdmin() == false) {
                    // No es un admin, no deberia estar aqui
                    return (mapping.findForward("login"));
                } else {
                    // Se agrega al contexto la informacion del usuario
                    request.setAttribute("userInfo", user);

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>");


                    String op = request.getParameter("operation");

                    System.out.println("Operacion en administrationAction: "+op);
                    if (op != null && op.equals("info")) {
                        // Se despliega el template correspondiente a la informacion
                        // del sistema.

                        // Se agregan las propiedades del sistema para ser desplegadas.
                        ArrayList properties = new ArrayList();

                        properties.add(new systemInfoData("Operative System",
                                System.getProperty("os.name") + " " +
                                System.getProperty("os.version")));

                        properties.add(new systemInfoData("Arquitecture",
                                System.getProperty("os.arch")));

                        properties.add(new systemInfoData("Java VM",
                                System.getProperty("java.version") + " build " +
                                System.getProperty("java.class.version")));

                        properties.add(new systemInfoData("Java Provider",
                                System.getProperty("java.vendor")));

                        properties.add(new systemInfoData("Java Home",
                                System.getProperty("java.home")));

                        properties.add(new systemInfoData("Server Type",
                                this.getServlet().getServletContext().getServerInfo()));

                        properties.add(new systemInfoData("Server Name",
                                request.getServerName()));

                        properties.add(new systemInfoData("Server Port",
                                "" + request.getServerPort()));

                        // Se agrega al contexto
                        request.setAttribute("list", properties);
                        request.setAttribute("company", company);
                        return (mapping.findForward("info"));
                    } else if (op != null && op.equals("changeData")) {
                        
                        if (request.getParameter("account") != null) {
                            session.setAttribute("account", request.getParameter("account"));
                            thisForm.setSelected(Integer.parseInt(request.getParameter("account").toString()));
                            user.setId_account(Integer.parseInt(request.getParameter("account").toString()));
                            session.setAttribute("login", user);
                             accountsData accountData = (accountsData)accountBroker.getData(Integer.parseInt(request.getParameter("account").toString()));
                             session.setAttribute("systemName",accountData.getCreator());
                             session.setAttribute("mainUrl",accountData.getMain_url());
                             System.out.println("NEW URL: "+session.getAttribute("mainUrl") );
                            tms = new TMSConfigurator(user);
                        }                        
                        company = TMSConfigurator.getCompany(user);
                        Iterator e = accountBroker.getList("id", "ASC");
                        ArrayList lista = new ArrayList();
                        accountsData data = new accountsData();
                        while (e.hasNext()) {
                            data = new accountsData();
                            data = (accountsData) e.next();
                            if(data.getId()== user.getId_account()){
                                
                                request.setAttribute("activeAccount", "0");
                                if(data.getFinal_trial_date()!=null){
                                    request.setAttribute("activeAccount", "1");
                                }
                            }
                            lista.add(data);
                        }
                        accountBroker.close();
                        request.setAttribute("listAccount", lista);
                        request.setAttribute("company", company);
                        // Se redirecciona a la pagina de administracion
                        return (mapping.findForward("main"));
                    } else {
                        if (session.getAttribute("account") != null) {
                            thisForm.setSelected(Integer.parseInt(session.getAttribute("account").toString()));
                        }
                        Iterator e = accountBroker.getList("id", "ASC");
                        ArrayList lista = new ArrayList();
                        accountsData data = new accountsData();
                        while (e.hasNext()) {
                            data = new accountsData();
                            data = (accountsData) e.next();
                            lista.add(data);
                        }
                        
                        e = accountBroker.getList("id", "ASC");
                        lista = new ArrayList();
                        data = new accountsData();
                        while (e.hasNext()) {
                            data = new accountsData();
                            data = (accountsData) e.next();
                            if(data.getId()== user.getId_account()){
                                
                                request.setAttribute("activeAccount", "0");
                                if(data.getFinal_trial_date()!=null){
                                    request.setAttribute("activeAccount", "1");
                                }
                            }
                            lista.add(data);
                        }
                        
                        
                        request.setAttribute("listAccount", lista);

                        request.setAttribute("company", company);
                        accountBroker.close();
                        // Se redirecciona a la pagina de administracion
                        return (mapping.findForward("main"));
                    }
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "admin.do", "perform",
                        "Fatal Error: " + e.toString());

            }
        }
        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("main"));
    }
}
