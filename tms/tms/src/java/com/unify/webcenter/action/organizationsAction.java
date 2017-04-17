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
import java.sql.Timestamp;

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
public class organizationsAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of organizationAction */
    public organizationsAction() {

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
        organizationsBroker broker;
        projectsBroker brokerProjects;
        membersBroker brokerMembers;
        String company = null;

        HttpSession session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the home page
            return (mapping.findForward("login"));

        } else {
 //Set the selected tab 
            session.setAttribute("current","organizations");
            broker = new organizationsBroker();
            brokerProjects = new projectsBroker();
            brokerMembers = new membersBroker();

            ActionErrors errors = new ActionErrors();

            try {
                if (form == null) {
                    System.out.println(" Creating new organizationsForm bean under key " + mapping.getAttribute());
                    form = new organizationsForm();
                }

                organizationsForm thisForm = (organizationsForm) form;

                // fetch action from form
                action = thisForm.getOperation();

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                servlet.log("[DEBUG] organizationsAction at perform(): Action is " + action);

                // Determine what to do
                if (action == null || action.equals("listing")) {
                    if(user.getprofile().equals("4") || user.getprofile().equals("3")) {
                    // Traer los valores para desplegar la lista de organizaciones disponibles.
                    // Constituye un listado completo y para ello nos valemos del broker.                                              
                    // Y por ultimo lo agregramos al contexto
                    request.setAttribute("list_organizations", this.getListing("name", "ASC", broker, user.getId_account()));
                    thisForm.setsortOrder("DESC");

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>");

                    // forward to display the list
                    request.setAttribute("company", company);
                    return (mapping.findForward("listing"));
                    }else{
                        // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>");

                    // forward to display the list
                    request.setAttribute("company", company);
                    return (mapping.findForward("accessDenied"));
                    }
                } else if (action.equals("sort")) {
                    // Traer los valores para desplegar la lista de organizaciones disponibles.
                    // Constituye un listado completo y para ello nos valemos del broker.                                              
                    // Y por ultimo lo agregramos al contexto
                    request.setAttribute("list_organizations",
                            this.getListing(thisForm.getsortColumn(),
                            thisForm.getsortOrder(), broker, user.getId_account()));


                    // Negamos el tipo de ordenamiento
                    if (thisForm.getsortOrder().equalsIgnoreCase("ASC")) {
                        thisForm.setsortOrder("DESC");
                    } else {
                        thisForm.setsortOrder("ASC");
                    }

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>");

                    // forward to display the list
                    request.setAttribute("company", company);
                    return (mapping.findForward("listing"));

                } else if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.
                    request.setAttribute("title",
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addClientOrg"));
                    thisForm.setoperation("applyAdd");
                    thisForm.setid(0);
                    thisForm.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") +
                            "</a>&nbsp;/&nbsp;" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addOrganization"));
                    request.setAttribute("company", company);

                    ArrayList lista = new ArrayList();
                    countriesBroker countryBroker = new countriesBroker();
                    Iterator e = countryBroker.getList("description", "ASC", user.getId_account());

                    while (e.hasNext()) {
                        lista.add(e.next());
                    }
                    countryBroker.close();

                    request.setAttribute("list", lista);

                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("edit")) {
                    // Si se trata de un edit, se debe de cargar los datos del bean
                    // en el formulario.
                    organizationsData data = (organizationsData) broker.getData(thisForm.getid(), user.getId_account());

                    request.setAttribute("title",
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.editClientOrg"));

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);
                    thisForm.setoperation("applyEdit");

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>&nbsp;/&nbsp;" +
                            "<a href='./organizations.do?operation=view&id=" +
                            data.getid() + "'>" + data.getname() + "</a>" +
                            "&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.editClientOrg"));

                    request.setAttribute("company", company);

                    ArrayList lista = new ArrayList();
                    countriesBroker countryBroker = new countriesBroker();
                    Iterator e = countryBroker.getList("description", "ASC", user.getId_account());

                    while (e.hasNext()) {
                        lista.add(e.next());
                    }
                    countryBroker.close();

                    request.setAttribute("list", lista);

                    return (mapping.findForward("displayEditForm"));

                } else if (action.equals("view") || action.equals("sortChilds") || action.equals("paging")) {
                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.
                    organizationsData data = (organizationsData) broker.getData(thisForm.getid(), user.getId_account());

                    request.setAttribute("title",
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.viewClientOrganization"));

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);

//                    thisForm.setoperation("view");

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>&nbsp;/&nbsp;" +
                            data.getname());

                    // En esta vista se deben de listar todos los proyectos
                    // Primero con Proyectos
                    sortProjects(request, thisForm, data.getid(), brokerProjects, user.getId_account());

                    // Ordenamos los members
                    sortMembers(request, thisForm, data.getid(), brokerMembers, user.getTime_zone(), user.getId_account());

                    request.setAttribute("company", company);
                    return (mapping.findForward("displayViewForm"));

                } else if (action.equals("delete")) {

                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    ArrayList items = broker.getItems(fields, user.getId_account());

                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    //request.setAttribute("fromPage", "" + thisForm.getTheDate());                
                    request.setAttribute("title", java.util.ResourceBundle.getBundle("ApplicationResources",
                            new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations"));
                    request.setAttribute("do", request.getRequestURI());
                    request.setAttribute("company", company);
                    // forward to display the list 
                    return (mapping.findForward("confirmDelete"));

                } else if (thisForm.getOperation().equals("applyDelete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado                    
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    String lista = "";
                    for (int i = 0; i < fields.length; i++) {
                        lista += fields[i] + " - ";
                    }
                    ArrayList items = broker.getItems(fields, user.getId_account());

                    // Se procede con el borrado de cada una de ellas.
                    organizationsData dataDelete;
                    for (int i = 0; i < items.size(); i++) {
                        dataDelete = (organizationsData) items.get(i);

                        // We delete the object in the DBMS
                        broker.deleteAllReferences(dataDelete.getid(), user.getId_account());
                    }
                    request.setAttribute("list_organizations", this.getListing("name", "ASC", broker, user.getId_account()));
                    thisForm.setsortOrder("DESC");
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("listing"));

                } else if (action.equals("applyAdd")) {
                    errors = thisForm.validate(mapping, request);
                    if (errors.isEmpty()) {
                        // Se trata de la aplicacion de un insert en la BD
                        organizationsData data = new organizationsData();

                        // We copy all the properties from the form to the bean.
                        PropertyUtils.copyProperties(data, thisForm);
                        data.setId_account(user.getId_account());
                        // We set the create date & time
                        data.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));

                        // We add the new record.
                        broker.add(data);

                        // Y por ultimo lo agregramos al contexto para que el usuario vea
                        // la lista actualizada.
                        request.setAttribute("list_organizations", this.getListing("name", "ASC", broker, user.getId_account()));
                        thisForm.setsortOrder("DESC");

                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute",
                                "<a href='./home.do'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                                "<a href='./organizations.do?operation=listing'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>");
                        request.setAttribute("company", company);
                        // forward to display the list
                        return (mapping.findForward("listing"));
                    } else {
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=add"));
                    }

                } else if (action.equals("applyEdit")) {
                    errors = thisForm.validate(mapping, request);
                    if (errors.isEmpty()) {
                        // Se trata de la aplicacion de un update en la BD
                        organizationsData data = new organizationsData();

                        // We copy all the properties from the form to the bean.
                        PropertyUtils.copyProperties(data, thisForm);
                        data.setId_account(user.getId_account());
                        // We add the new record.
                        broker.update(data);

                        // Y por ultimo lo agregramos al contexto para que el usuario vea
                        // la lista actualizada.
                        request.setAttribute("list_organizations", this.getListing("name", "ASC", broker, user.getId_account()));
                        thisForm.setsortOrder("DESC");


                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute",
                                "<a href='./home.do'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                                "<a href='./organizations.do?operation=listing'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>");
                        request.setAttribute("company", company);
                        // forward to display the list
                        return (mapping.findForward("listing"));
                    } else {
                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=edit"));
                    }

                } else if (action.equals("sortAll")) {

                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.                 
                    organizationsData data = (organizationsData) broker.getData(thisForm.getid(), user.getId_account());
                    request.setAttribute("title",
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.viewClientOrganization"));

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);
                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") + "</a>&nbsp;/&nbsp;" +
                            data.getname());

                    if (request.getParameter("typeOp") != null) {
                        thisForm.setTypeProject(Integer.parseInt(request.getParameter("typeOp").toString()));
                    }
                    System.out.println("ss");
                        System.out.print(thisForm.getsortColumnProjects());
                    Iterator e;
                    if (thisForm.getTypeProject() == 0) {
                        e = brokerProjects.getListByOrganization(thisForm.getsortColumnProjects(),
                                thisForm.getsortOrderProjects(),
                                data.getid(), 0, user.getId_account());
                    } else {
                        e = brokerProjects.getListByOrganization(thisForm.getsortColumnProjects(),
                                thisForm.getsortOrderProjects(),
                                 data.getid(), 0, user.getId_account(), thisForm.getTypeProject());
                    }
                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        lista.add(e.next());
                    }
                    request.setAttribute("listProjects", lista);
                    request.setAttribute("company", company);
                    // forward to display the list 
                    return (mapping.findForward("displayAll"));

                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "organizations.do", "perform",
                        "Fatal Error: " + e.toString());

                e.printStackTrace();

            } finally {
                // Se cierran los brokers
                broker.close();
                brokerProjects.close();
                brokerMembers.close();
            }
        }
        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }

    // Retorna la lista de organizaciones 
    private ArrayList getListing(String sortCol, String sortOrder, organizationsBroker broker, int accountId) {
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

    // Metodo que se encarga de ordernar los proyectos dispuestos.
    private void sortProjects(HttpServletRequest request, organizationsForm thisForm,
            int orgId, projectsBroker brokerProjects, int idAccount) {
        // Traer los valores para desplegar la lista de proyectos disponibles.
        // Constituye un listado completo y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto       
        int currentPage = thisForm.getpageProjects();

        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("members")) {
            if (thisForm.getOperation().equalsIgnoreCase("sortChilds")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageProjects(1);
            }
        }


        Iterator e;
        if (thisForm.getTypeProject() == 0) {
            e = brokerProjects.getListByOrganization(thisForm.getsortColumnProjects(),
                    thisForm.getsortOrderProjects(),
                    orgId, currentPage, idAccount);
        } else {
            e = brokerProjects.getListByOrganization(thisForm.getsortColumnProjects(),
                    thisForm.getsortOrderProjects(),
                    orgId, currentPage, idAccount, thisForm.getTypeProject());
        }


        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }

        request.setAttribute("listProjects", lista);

        // Se setea el total de paginas
        setPages(brokerProjects, request, "listPagesProjects");


        if (thisForm.getsource().equalsIgnoreCase("projects") &&
                thisForm.getOperation().equalsIgnoreCase("sortChilds")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderProjects().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderProjects("DESC");
            } else {
                thisForm.setsortOrderProjects("ASC");
            }
        }

    }

    // Metodo que se encarga de ordernar los miembros dispuestos.
    private void sortMembers(HttpServletRequest request, organizationsForm thisForm,
            int orgId, membersBroker brokerMembers, String timeZone, int idAccount) {
        // Traer los valores para desplegar la lista de members disponibles.
        // Constituye un listado completo por organizacion y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto     
        int currentPage = thisForm.getpageMembers();

        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("members")) {
            if (thisForm.getOperation().equalsIgnoreCase("sortChilds")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageMembers(1);
            }
        }

        Iterator e = brokerMembers.getListByOrganization(thisForm.getsortColumnMembers(),
                thisForm.getsortOrderMembers(),
                orgId, currentPage, idAccount);

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        membersData memberData = new membersData();
        while (e.hasNext()) {
            memberData = (membersData) e.next();
            memberData.setcreated(Timestamp.valueOf(memberData.convTimeZone(memberData.getcreated(), timeZone)));
            lista.add(memberData);
        }
        request.setAttribute("listMembers", lista);

        // Se setea el total de paginas
        setPages(brokerMembers, request, "listPagesMembers");

        if (thisForm.getsource().equalsIgnoreCase("members") &&
                thisForm.getOperation().equalsIgnoreCase("sortChilds")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderMembers().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderMembers("DESC");
            } else {
                thisForm.setsortOrderMembers("ASC");
            }
        }

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

