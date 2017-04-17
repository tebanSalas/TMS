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
import com.unify.webcenter.tools.*;

/**
 *
 * @author  Administrator
 */
public class forgotAction extends Action {
   
   
    private static Logger logger  = Logger.getLogger("com.unify");    
    
    /** Creates a new instance of loginAction */
    public forgotAction() {
               
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

        broker = new membersBroker(); 
        try {
            session = request.getSession();
            session.setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale("es", ""));
                loginData user = (loginData) session.getAttribute("login");
            ActionErrors errors = new ActionErrors();

            if (form == null) {
                   System.out.println(" Creating new forgotForm bean under key "
                               + mapping.getAttribute());
                   form = new forgotForm();
            }
            forgotForm thisForm = (forgotForm) form;

            // fetch action from form
            action = thisForm.getOperation();                        
            String email = thisForm.getemail();

            servlet.log("[DEBUG] forgotAction at perform(): Action is " + action);
            logger.logp(Level.FINEST, "TMS Forgot password", "performAction",
                        "[DEBUG] forgotAction at perform(): Action is " + action);

            // Determine what to do
            if ( action == null || action.equals("display") || action.equals("") ) {                             
                thisForm.setOperation("send");
                return (mapping.findForward("forgot"));
            } 
            else if (action.equals("send")) {
                 errors = thisForm.validate(mapping, request);                

                 if (errors.isEmpty()) {                                                   

                   // membersData member = broker.getMemberByEmail(email, user.getId_account());                    
                    
                     //Se realiza la búsqueda del Miembro por nombre de usuario
                      membersData member = broker.getMemberByLogin(email);  
                     if (member != null) {

                        //errors.add("username", new ActionError("errors.login.invalidAdminSite"));
                        sendMail sm = new sendMail();
                        sm.sendForgot(member);
                        return (mapping.findForward("info"));
                    }                    
                    else {
                        thisForm.setemail(email);
                        errors.add("username", new ActionError("errors.forgot.notfound"));
                        saveErrors(request, errors);
                        return (mapping.findForward("forgot"));
                    } 
                 }
                 else {
                        thisForm.setemail(email);
                        saveErrors(request, errors);
                        return (mapping.findForward("forgot"));
                 }
            } 
        }
        catch (Exception e)  {              
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();
                logger.logp(Level.SEVERE, "TMS Login", "performAction",
                            "[ERROR] Action at final catch: " + e.getMessage());
        } finally {
            // Se cierran los brokers abiertos
            broker.close();
        }
    // Default if everthing else fails
    return (mapping.findForward("forgot"));
    }
}
