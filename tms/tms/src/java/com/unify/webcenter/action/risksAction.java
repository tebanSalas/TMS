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

public class risksAction extends Action {
    
    //private static Logger logger  = Logger.getLogger("com.unify");    
        
    /** Creates a new instance of costsAction */
    public risksAction() {
        
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
        risksBroker riskBroker;
        String patternStr = ","; 
        String company = null;
        
        session = request.getSession(false);
       
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page               
            return (mapping.findForward("login"));            
            
        } else {   
            riskBroker = new risksBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
        
                if (form == null) {
                        System.out.println(" Creating new costsForm bean under key "
                                    + mapping.getAttribute());
                    form = new costsForm();
                }

                risksForm thisForm = (risksForm) form;

                // fetch action from form
                action = thisForm.getoperation();

                servlet.log("[DEBUG] risksAction at perform(): Action is " + action);
                
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);                   

                // Determine what to do
                if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.                    
                    thisForm.setoperation("applyAdd");
                    thisForm.setid(0);
                                   
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.getData(thisForm.getproject(),user.getId_account()); 
                    projBroker.close();
                    
                    // Se traen las tareas asociadas a este proyecto
                    tasksBroker taskBroker = new tasksBroker();
                    Iterator e = taskBroker.getListByProject("name", "ASC", thisForm.getproject(),
                        0,user.getId_account());
                    taskBroker.close();
                    thisForm.setfromPage(request.getParameter("fromPage"));
                    request.setAttribute("fromPage", thisForm.getfromPage());
                    // Se cargan todas las tareas de ese proyecto en una lista para
                    // que se despleguien en el combo de tareas asociadas.
                    ArrayList listaTasks = new ArrayList();
                    while (e.hasNext()) {
                        listaTasks.add(e.next());
                    }                    
                    request.setAttribute("listaTasks", listaTasks);
                    

                     request.setAttribute("menuRoute", 
                        "<a href='./home.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        + "</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addRisk"));
 request.setAttribute("company",company);
                     return (mapping.findForward("displayAddForm"));
                    
                } else if (action.equals("edit")) {
                    // Si se trata de un edit, se debe de cargar los datos del bean
                    // en el formulario.

                    risksBroker risBroker = new risksBroker();
                    risksData data = (risksData)risBroker.getData(thisForm.getid(),user.getId_account());
                    
                    PropertyUtils.copyProperties(thisForm, data);                
                     thisForm.setoperation("applyEdit");
                    
                    projectsBroker projBroker = new projectsBroker();

                    projectsData projectMember = (projectsData)projBroker.
                            getData(thisForm.getproject(),user.getId_account()); 
                    projBroker.close();
                                                        
            
                    // Se traen las tareas asociadas a este proyecto
                    tasksBroker taskBroker = new tasksBroker();
                    Iterator e = taskBroker.getListByProject("name", "ASC", thisForm.getproject(),
                        0,user.getId_account());
                    taskBroker.close();
                    
                    // Se cargan todas las tareas de ese proyecto en una lista para
                    // que se despleguien en el combo de tareas asociadas.
                    ArrayList listaTasks = new ArrayList();
                    while (e.hasNext()) {
                        listaTasks.add(e.next());
                    }                    
                    request.setAttribute("listaTasks", listaTasks);
                    
                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        + "</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addRisk"));
                     request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("view")) {
                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.
                    risksData data = (risksData)riskBroker.getData(thisForm.getid(),user.getId_account());

                    request.setAttribute("title",java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.viewRisk"));
                    request.setAttribute("description", data.getdescription());
                    request.setAttribute("probability", "" + data.getprobability());
                    request.setAttribute("impact", "" + data.getimpact());
                    request.setAttribute("todoaction", data.getFormatedTodoAction());
                    request.setAttribute("planb", data.getplanb());
                    request.setAttribute("task", "" + data.gettask());
                    request.setAttribute("taskdesc", data.gettaskdesc());
                    
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectsdata = (projectsData) projBroker.getData(thisForm.getproject(),user.getId_account());

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>"+java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")+"</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectsdata.getid() + "'>" + 
                        projectsdata.getname() +"</a>&nbsp;/" +                                     
                        "&nbsp;"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.viewRisk"));                        
                    
                    request.setAttribute("projectdata",projectsdata);
                    projBroker.close();
                     request.setAttribute("company",company);
                    return (mapping.findForward("displayViewForm"));

                } else if (action.equals("delete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
           
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    ArrayList items = riskBroker.getItems(fields,user.getId_account());                                    
                                        
                    request.setAttribute("param", request.getParameter("project"));
                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title", "Risks");
                    request.setAttribute("do",request.getRequestURI());
                    request.setAttribute("company",company);
                    // forward to display the list
                    return (mapping.findForward("confirmDelete"));                
                                        
                } else if (action.equals("applyDelete")) {
                    
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    String lista = "";
                    for (int i=0; i < fields.length; i++) {            
                        lista += fields[i]+" - ";            
                    }                    
                    ArrayList items = riskBroker.getItems(fields,user.getId_account());                                    
                   
                    // delete each file entry in DB and your file asociated.
                    risksData datatemp = new risksData();
                    
                    ListIterator li = items.listIterator();
                    while (li.hasNext()) {
                        datatemp = (risksData)li.next(); 
                        riskBroker.delete(datatemp);                        
                    }
                    if (thisForm.getfromPage().equalsIgnoreCase("allRisks")) {
                        
                        projectsBroker projBroker = new projectsBroker();
                        projectsData projectMember = (projectsData)projBroker.
                                getData(thisForm.getproject(),user.getId_account()); 
                        projBroker.close();
                        
                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute", 
                            "<a href='./home.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                            + "</a>&nbsp;/" +
                            "<a href='./projects.do?operation=view&id=" + 
                            projectMember.getid() + "'>" + 
                            projectMember.getname() +"</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.allRisks"));
                          request.setAttribute("company",company);
                        return (mapping.findForward("displayAllRisksByProject"));
                         
                    } else {
                        // return to the page that list the costs...
                         request.setAttribute("company",company);
                        return (new ActionForward(mapping.findForward("viewProject").
                            getPath()+"&id="+request.getParameter("param"), false));
                    }
                } else if (action.equals("applyAdd")) {
                    // Se trata de la aplicacion de un insert en la BD
                    risksData data = new risksData();
                    
                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);                                
                  
                    // Save the information about the file in DB                            
                    data.setid(0);
                      data.setId_account(user.getId_account());
                    riskBroker.add(data);
                    
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.
                            getData(thisForm.getproject(),user.getId_account()); 
                    projBroker.close();

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        + "</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.allRisks"));
                         
                     // If the page from this action was called is a project
                     request.setAttribute("company",company);
                    return (new ActionForward(mapping.findForward("viewProject").
                        getPath()+"&id="+thisForm.getproject(), true));  
                    
                } else if (action.equals("applyEdit")) {
                    // Se trata de la aplicacion de un update en la BD
                    risksData data = new risksData();
                    PropertyUtils.copyProperties(data, thisForm);                                

                    // We add the new record.
                    riskBroker.update(data);
                         
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.
                            getData(thisForm.getproject(),user.getId_account()); 
                    projBroker.close();

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        + "</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.allRisks"));

                     // Y por ultimo lo agregramos al contexto para que el usuario vea
                    // la lista actualizada.
                     request.setAttribute("company",company);
                    return (new ActionForward(mapping.findForward("viewProject").
                        getPath()+"&id="+data.getproject(), true));                
                   
                } else if (action.equals("showAll") || action.equals("sortAll")) {
                    // Se trata de un showAll de las tareas asociadas a un proyecto determinado.                    
                    Iterator e ;
                    e = riskBroker.getListByProject(thisForm.getsortColumn(),
                            thisForm.getsortOrder(),
                            thisForm.getproject(), 0,user.getId_account());
                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    risksData data = new risksData();
                    tasksBroker taskBroker = new tasksBroker();
                    while (e.hasNext()) {
                       data = (risksData) e.next();
                       if (data.gettask() == 0)
                           data.settaskdesc("");
                       else
                           data.settaskdesc(((tasksData) taskBroker.getData(data.gettask(),user.getId_account())).getname());
                       lista.add(data);                        
                    }
                    taskBroker.close();
                     
                    // Se guarda la lista de files asociados a esta tarea.
                    request.setAttribute("listrisks", lista);
                   if (thisForm.getoperation().equalsIgnoreCase("sortAll")) {
                       // Negamos el tipo de ordenamiento
                       if (thisForm.getsortOrder().equalsIgnoreCase("ASC"))
                           thisForm.setsortOrder("DESC");
                       else
                           thisForm.setsortOrder("ASC");
                   }                                    
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.
                        getData(thisForm.getproject(),user.getId_account()); 
                    projBroker.close();
                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        +"</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.allRisks"));
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAllRisksByProject")); 
                }

            }
            catch (Exception e)  {              
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                //logger.logp(Level.SEVERE, "risks.do",  "perform", 
                  //              "Fatal Error: " + e.toString());                  
                e.printStackTrace();
                return (mapping.findForward("listing"));                

            } finally {
                riskBroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }        
   
}

