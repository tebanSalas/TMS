/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

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
import com.unify.webcenter.tools.*;

/**
 *
 * @author MARCELA QUINTERO
 */
public class superAdminAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        String action;
        HttpSession session = request.getSession(false);
        String company = null;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the login page
            return (mapping.findForward("login"));
        } else {

            // Si el usuario esta logeado se le despliega el menu
            try {
                 //Set the selected tab 
            session.setAttribute("current","superadmin");
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                request.setAttribute("version", tms.getVersion());
                company = tms.getCompany(user);
                String op = request.getParameter("operation");


                if (user.isAdmin() == false) {
                    // No es un admin, no deberia estar aqui
                    return (mapping.findForward("login"));
                } else if (op != null && op.equals("subOptions")) {
                    // Se agrega al contexto la informacion del usuario
                    request.setAttribute("userInfo", user);
                    request.setAttribute("company", company);
                    request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a> /" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.croms"));
                    // Se redirecciona a la pagina de administracion
                    return (mapping.findForward("subOptions"));
                }else if (op != null && op.equals("options")) {
                     // Se agrega al contexto la informacion del usuario
                    request.setAttribute("userInfo", user);
                    request.setAttribute("company", company);
                    String global = "0";
                    if (request.getParameter("global")!=null){
                        global = request.getParameter("global");
                    }
                    if (global.equals("0")){
                    request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a> /"+
                            "<a href='./superAdmin.do?operation=subOptions'>" +java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.croms") + "</a> /"+java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.options"));
                    }else if (global.equals("1")){
                        request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a> /"+
                            "<a href='./superAdmin.do?operation=subOptions'>" +java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.croms") + "</a> /"+java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.optionsGlobal"));
                    }
                                        request.setAttribute("global", global);

                    // Se redirecciona a la pagina de administracion
                    return (mapping.findForward("options"));
                }else {
                    // Se agrega al contexto la informacion del usuario
                    request.setAttribute("userInfo", user);

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>");
                    request.setAttribute("company", company);
                    // Se redirecciona a la pagina de administracion
                    return (mapping.findForward("main"));
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
