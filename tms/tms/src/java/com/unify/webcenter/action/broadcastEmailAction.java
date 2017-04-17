/*
 * broadcastEmail.java
 *
 * Created on 22 de marzo de 2005, 11:11 AM
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
/**
 * Accion que se ejecuta cuando el usuario administrador desea hacer un envio generalizado
 * a los diferentes usuarios del TMS.
 * @author  Gerardo Arroyo
 */
public class broadcastEmailAction extends Action {
    
   private static Logger logger  = Logger.getLogger("com.unify");    
   
    /** Creates a new instance of broadcastEmail */
    public broadcastEmailAction() {
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
        HttpSession session  = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
                       
            // forward to display the login page
            return (mapping.findForward("login"));                        
        } else {
            
            // Si el usuario esta logeado se le despliega el menu
            try {
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                
                if (user.isAdmin() == false) {
                    // No es un admin, no deberia estar aqui
                    return (mapping.findForward("login"));                        
                } else {                
                    // Se agrega al contexto la informacion del usuario
                    request.setAttribute("userInfo", user);                

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./broadcastEmail.do'>" + 
                        java.util.ResourceBundle.getBundle("ApplicationResources", 
                                new Locale(user.getlanguage(),"")).getString("common.sendEmails")
                        + "</a>");

                    // Tomamos la operacion que el usuario desea realizar.
                    String op = request.getParameter("operation");
                    
                    if (op != null && op.equals("main")) {
                       
                        // Regresamos a la pagina donde se piden los parametros de envio
                        // de correo.
                        return (mapping.findForward("main"));     
                        
                    } else if (op != null && op.equals("send")) {       
                        String subject, body, sendTo;
                                              
                        subject = request.getParameter("subject");
                        body = request.getParameter("body");
                        sendTo = request.getParameter("sendTo");
                        
                        membersBroker members = new membersBroker();
                        Iterator e = members.getList("id", "ASC",user.getId_account());
                        
                        // Se procesan todos los members y con base en el tipo
                        // de envio, asi se carga el arreglo de direcciones
                        ArrayList emails = new ArrayList();
                        membersData mem;
                        String email;
                        while (e.hasNext()) {
                            mem = (membersData) e.next();
                            email = mem.getemail_work();
                            
                            if (email != null && email.length() > 0) {
                                // No queremos direcciones nulas o con un tamano de 0
                                if (sendTo.equals("IN") && mem.getprofile().equals("2") == false) {
                                    // Usuarios internos
                                    emails.add(mem.getemail_work());
                                } else if (sendTo.equals("EX") && mem.getprofile().equals("2")) {
                                    // Usuarios externos
                                    emails.add(mem.getemail_work());
                                }  else if (sendTo.equals("ALL")) {
                                    // Todos
                                    emails.add(mem.getemail_work());
                                }
                            }
                        }
                        members.close();
                        
                        // Instanciamos el objeto para el envio de emails.
                        com.unify.webcenter.tools.sendMail sm = new com.unify.webcenter.tools.sendMail();                 
                        
                        sm.sendBroadcast(emails, subject, body);
                        
                        
                        // Se trata de la operacion send!
                        return (mapping.findForward("info"));                        
                    }
                }
            }
            catch (Exception e)  {        
                e.printStackTrace();
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "broadcastEmail.do",  "perform", 
                        "Fatal Error: " +e.toString());                  
                
            }
        }

        // Default if everthing else fails
        return (mapping.findForward("main"));
    }    
}
