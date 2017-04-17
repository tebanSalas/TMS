/*
 * membersAction.java
 *
 * Created on March 1, 2003, 12:29 PM
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
import com.unify.webcenter.tools.loadTimeZone;
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
public class membersAction extends Action {

    /** Creates a new instance of calendarAction */
    public membersAction() {

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
        organizationsBroker orgBroker;
        session = request.getSession(false);


        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {

            // forward to display the home page
            return (mapping.findForward("portalLogin"));

        } else {
            broker = new membersBroker();
            orgBroker = new organizationsBroker();
            try {
                if (form == null) {
                    System.out.println(" Creating new membersForm bean under key " + mapping.getAttribute());
                    form = new membersForm();
                }

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                membersForm thisForm = (membersForm) form;

                projectsBroker brokerProjects = new projectsBroker();
                request.setAttribute("projectInfo", brokerProjects.getData(
                        Integer.parseInt(request.getParameter("project")),user.getId_account()));
                brokerProjects.close();

                // fetch action from form
                action = ((membersForm) form).getOperation();

                servlet.log("[DEBUG] membersAction at perform(): Action is " + action);

                if (action.equals("edit")) {
                    // Si se trata de un edit, se debe de cargar los datos del bean
                    // en el formulario.
                    membersData data = (membersData) broker.getData(thisForm.getid(),user.getId_account());

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);
                    thisForm.setOperation("applyEdit");
                    //Toma las zona horaria
                    loadTimeZone timeZone = new loadTimeZone();
                    ArrayList listTimeZone = new ArrayList();
                    listTimeZone = timeZone.loadValues();

                    // Se guarda la lista de la Zona Horaria
                    request.setAttribute("listTimeZone", listTimeZone);
                    // Se despiega el formulario apropiado.
                    return (mapping.findForward("edit"));

                } else if (action.equals("applyEdit")) {
                    // Se trata de la aplicacion de un update en la BD
                    membersData data = new membersData();

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);
                    data.setprofile("2");  // Siempre son del lado del cliente
                    user.setTemplate(data.getTemplate());

                    request.setAttribute("userInfo", user);


                    // Link de los members a ver si se cambio o no la clave
                    membersData oldData = (membersData) broker.getData(data.getid(),user.getId_account());

                    // Si la clave vieja es diferente de la nueva
                    if (oldData.getpassword().equalsIgnoreCase(thisForm.getpassword()) == false) {
                        data.setpassword(membersData.encrypt(data.getpassword()));
                    }
                    data.setId_account(user.getId_account());
                    loginData login = (loginData) session.getAttribute("loginPortal");
                    login.setTime_zone(data.getTime_zone());
                    //set the permissions
                    data.setind_check_schedules(oldData.getind_check_schedules());
                    data.setInd_exec_report(oldData.getInd_exec_report());
                    data.setind_client_manager(oldData.getind_client_manager());
                    data.setind_end_tasks(oldData.getind_end_tasks());
                    data.setquotation(oldData.getquotation());
                    data.setreports(oldData.getreports());
                    data.setExpired_date(oldData.getExpired_date());
                    data.setRenew_date(oldData.getRenew_date());
                    // We update the record
                    broker.update(data);
                    session.setAttribute("loginPortal", login);
                    // forward to display the list
                    return (mapping.findForward("home"));
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();

            } finally {
                // Se cierran los brokers creados
                broker.close();
                orgBroker.close();
            }
        }
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }
}
