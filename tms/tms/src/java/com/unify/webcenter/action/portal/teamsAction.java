/*
 * teamsAction.java
 *
 * Created on February 28, 2003, 9:48 AM
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

/**
 * Clase que representa la accion de los equipos en el portal del cliente
 * @author  Gerardo Arroyo Arce
 */
public class teamsAction extends Action {
   
    /** Creates a new instance of calendarAction */
    public teamsAction() {
	
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
        teamsBroker brokerTeam;

        session = request.getSession(false);
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {
                           
            // forward to display the home page
            return (mapping.findForward("portalLogin"));            
            
        } else {   
            brokerTeam = new teamsBroker();
            try {
                session = request.getSession();

                if (form == null) {
                        System.out.println(" Creating new teamsForm bean under key "
                                    + mapping.getAttribute());
                    form = new portalForm();
                }

                portalForm thisForm = (portalForm) form;

                // fetch action from form
                action = thisForm.getOperation();
                

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);           
                
                projectsBroker brokerProjects = new projectsBroker();
                request.setAttribute("projectInfo", brokerProjects.getData(
                        thisForm.getproject(),user.getId_account()));                
                brokerProjects.close();                         

                if (action.equals("show") || action.equals("sort")) {               
                    // Se trata de un show o un sort
                    Iterator e = brokerTeam.getListByProjectForPortal(thisForm.getsortColumn(),
                                            thisForm.getsortOrder(), 
                                            thisForm.getproject(), 
                                            0,user.getId_account());

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                       lista.add(e.next());
                    }

                   request.setAttribute("list_teams", lista);


                   if (thisForm.getOperation().equalsIgnoreCase("sort")) {
                       // Negamos el tipo de ordenamiento
                       if (thisForm.getsortOrder().equalsIgnoreCase("ASC"))
                           thisForm.setsortOrder("DESC");
                       else
                           thisForm.setsortOrder("ASC");
                   }                
                   
                   // se regresa la lista
                   return (mapping.findForward("display"));
                }
                
            } catch (Exception e)  {              
                System.err.println("[ERROR] Action at final catch: " + e.getMessage());
//                e.printStackTrace();
                
            } finally {
                // Se cierran los brokers
                brokerTeam.close();              
            }
        }
        // Default if everthing else fails
        return (mapping.findForward("display"));
    }
   
}