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

/**
 * <p>A simple action that handles the display and editing of an
 * addres record. This action works with both JSP and Velocity templates.
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
public class type_tasksAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of type_tasksAction */
    public type_tasksAction() {

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
        type_tasksBroker broker;
        HttpSession session = request.getSession(false);
        String company = null;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the home page
            return (mapping.findForward("login"));

        } else {
            broker = new type_tasksBroker();
            try {
                if (form == null) {
                    System.out.println(" Creating new task type bean under key " + mapping.getAttribute());
                    form = new type_tasksForm();
                }

                type_tasksForm thisForm = (type_tasksForm) form;

                // fetch action from form
                action = thisForm.getoperation();

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                servlet.log("[DEBUG] type_tasksAction at perform(): Action is " + action);

                // Determine what to do
                if (action == null || action.equals("listing")) {
                    // Traer los valores para desplegar la lista de tipos de tareas disponibles.
                    // Constituye un listado completo y para ello nos valemos del broker.                                              
                    // Y por ultimo lo agregramos al contexto
                    request.setAttribute("list_type_tasks", this.getListing("description", "ASC", broker, user.getId_account()));
                    thisForm.setsortOrder("DESC");

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") + "</a>");
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("listing"));

                } else if (action.equals("sort")) {
                    // Traer los valores para desplegar la lista de tipos de tareas disponibles.
                    // Constituye un listado completo y para ello nos valemos del broker.                                              
                    // Y por ultimo lo agregramos al contexto
                    request.setAttribute("list_type_tasks",
                            this.getListing(thisForm.getsortColumn(),
                            thisForm.getsortOrder(),
                            broker, user.getId_account()));


                    // Negamos el tipo de ordenamiento
                    if (thisForm.getsortOrder().equalsIgnoreCase("ASC")) {
                        thisForm.setsortOrder("DESC");
                    } else {
                        thisForm.setsortOrder("ASC");
                    }

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") + "</a>");
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("listing"));

                } else if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.
                    request.setAttribute("title",
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addType_Task"));
                    thisForm.setoperation("applyAdd");
                    thisForm.setid(0);

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" +
                            "<a href='./type_tasks.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") +
                            "</a>&nbsp;/&nbsp;" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addType_Task"));
                    request.setAttribute("company", company);
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("edit")) {
                    // Si se trata de un edit, se debe de cargar los datos del bean
                    // en el formulario.
                    type_tasksData data = (type_tasksData) broker.getData(thisForm.getid(), user.getId_account());

                    request.setAttribute("title",
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.editType_Task"));

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);
                    thisForm.setoperation("applyEdit");

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") +
                            "</a>&nbsp;/" + "<a href='./type_tasks.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") + "</a>&nbsp;/&nbsp;" +
                            data.getdescription());
                    request.setAttribute("company", company);
                    return (mapping.findForward("displayEditForm"));

                } else if (action.equals("delete")) {
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    ArrayList items = broker.getItems(fields, user.getId_account());

                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title", java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks"));
                    request.setAttribute("do", request.getRequestURI());
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("confirmDelete"));

                } else if (action.equals("applyDelete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
                    type_tasksData dataDelete;

                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    ArrayList items = broker.getItems(fields, user.getId_account());

                    // Se procede con el borrado de cada una de ellas.
                    for (int i = 0; i < items.size(); i++) {
                        dataDelete = (type_tasksData) items.get(i);

                        // We delete the object in the DBMS
                        broker.delete(dataDelete);
                    }

                    // Y por ultimo lo agregramos al contexto para que el usuario vea
                    // la lista actualizada.
                    request.setAttribute("list_type_tasks", this.getListing("description", "ASC", broker, user.getId_account()));
                    thisForm.setsortOrder("DESC");

                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") + "</a>");
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("listing"));

                } else if (action.equals("applyAdd")) {
                    // Se trata de la aplicacion de un insert en la BD
                    type_tasksData data = new type_tasksData();

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);
                    data.setId_account(user.getId_account());
                    // We add the new record.
                    broker.add(data);

                    // Y por ultimo lo agregramos al contexto para que el usuario vea
                    // la lista actualizada.
                    request.setAttribute("list_type_tasks", this.getListing("description", "ASC", broker, user.getId_account()));
                    thisForm.setsortOrder("DESC");

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" +
                            "<a href='./type_tasks.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") + "</a>");

                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("listing"));

                } else if (action.equals("applyEdit")) {
                    // Se trata de la aplicacion de un update en la BD
                    type_tasksData data = new type_tasksData();

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);
                    //data.setId_account(user.getId_account());
                    // We add the new record.
                    broker.update(data);

                    // Y por ultimo lo agregramos al contexto para que el usuario vea
                    // la lista actualizada.
                    request.setAttribute("list_type_tasks", this.getListing("description", "ASC", broker, user.getId_account()));
                    thisForm.setsortOrder("DESC");


                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" +
                            "<a href='./type_tasks.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Type_Tasks") + "</a>");
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("listing"));

                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "type_tasks.do", "perform",
                        "Fatal Error: " + e.toString());

                e.printStackTrace();

            } finally {
                // Se cierran los brokers
                broker.close();
            }
        }
        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }

    // Retorna la lista de tipos de tareas 
    private ArrayList getListing(String sortCol, String sortOrder, type_tasksBroker broker, int accountId) {
        Iterator e;
        // Se obtiene el iterador sobre todos los elementos de la lista.

        if (sortCol.equals("")) {
            e = broker.getList(accountId);
        } else {
            e = broker.getList(sortCol, sortOrder, accountId);
        }

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }

        return lista;
    }


    // Metodo que se encarga de dejar en el context una lista de todas las paginas
    // necesarias para dar cabida a los datos del ultimo comando de sql ejecutado.
    private void setPages(MainBroker broker, HttpServletRequest request,
            String listName) {
        // Se guarda el total de pàginas en el contexto.
        ArrayList listaPages = new ArrayList();

        // Se obtiene el total de registros reales de la ultima consulta.
        int max = broker.getCount().intValue();
        int totalPage = max / broker.GAP_SIZE;
        if ((max % MainBroker.GAP_SIZE) > 0) {
            totalPage++;
        }

        for (int i = 1; i <= totalPage; i++) {
            listaPages.add(new Integer(i));
        }

        request.setAttribute(listName, listaPages);
    }
}
