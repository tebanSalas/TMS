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
import com.unify.webcenter.crons.*;

/**
 *
 * @author MARCELA QUINTERO
 */
public class cronsWorkReportAction extends Action {

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
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                request.setAttribute("version", tms.getVersion());
                company = tms.getCompany(user);
                String op = request.getParameter("operation");

                request.setAttribute("global", "0");
                request.setAttribute("menuRoute",
                        "<a href='./superAdmin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a> /" +
                        "<a href='./superAdmin.do?operation=subOptions'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.croms") + "</a> /" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.options"));
                request.setAttribute("userInfo", user);
                request.setAttribute("company", company);

                if (user.isAdmin() == false) {
                    // No es un admin, no deberia estar aqui
                    return (mapping.findForward("login"));
                } else if (op != null && op.equals("daily")) {
                    sendReportDaily checker = new sendReportDaily();
                    checker.check();
                } else if (op != null && op.equals("weekly")) {
                    sendReportWeekly checker = new sendReportWeekly();
                    checker.check();
                } else if (op != null && op.equals("byweekly")) {
                    sendReportByWeekly checker = new sendReportByWeekly();
                    checker.check();
                } else if (op != null && op.equals("monthly")) {
                    sendReportMonthly checker = new sendReportMonthly();
                    checker.check();
                } else if (op != null && op.equals("annual")) {
                    sendReportAnnual checker = new sendReportAnnual();
                    checker.check();
                } else {
                    // Se redirecciona a la pagina de administracion
                    return (mapping.findForward("main"));
                }
                // Se redirecciona a la pagina de administracion
                return (mapping.findForward("options"));
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
