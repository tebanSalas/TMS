/*
 * costsAction.java
 *
 * Created on April 4, 2003, 12:09 PM
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
 * @author Gerardo Arroyo Arce
 */

public class costsAction  extends Action {
    
    private static Logger logger  = Logger.getLogger("com.unify");    
        
    /** Creates a new instance of costsAction */
    public costsAction() {
        
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
        String patternStr = ","; 
        costsBroker costBroker;
        
        session = request.getSession(false);
         String company = null;
       
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page
                
           request.setAttribute("company",company);
            return (mapping.findForward("login"));            
            
        } else { 
            costBroker = new costsBroker();
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

                costsForm thisForm = (costsForm) form;

                // fetch action from form
                action = thisForm.getOperation();

                servlet.log("[DEBUG] costsAction at perform(): Action is " + action);
                
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);                   
                
                // Determine what to do
                if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.                    
                    thisForm.setOperation("applyAdd");
                    thisForm.setId(0);
                    
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.
                            getData(thisForm.getProject(), user.getId_account()); 
                    projBroker.close();
                    
                    // Se traen las tareas asociadas a este proyecto
                    tasksBroker taskBroker = new tasksBroker();
                    Iterator e = taskBroker.getListByProject("name", "ASC", thisForm.getProject(),
                        0, user.getId_account());
                    taskBroker.close();
                    
                     Calendar now = Calendar.getInstance();

                    // Se carga el Calendar con la fecha actual
                    request.setAttribute("costs_date", "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH)+1) +
                            "-" + now.get(Calendar.DATE));
                    
                    
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
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addCosts"));
                                      request.setAttribute("company",company);                      
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("edit")) {
                    // Si se trata de un edit, se debe de cargar los datos del bean
                    // en el formulario.
                    costsData data = (costsData)costBroker.getData(thisForm.getId(), user.getId_account());

                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.
                            getData(thisForm.getProject(), user.getId_account()); 
                    projBroker.close();
                    
                    // Se traen las tareas asociadas a este proyecto
                    tasksBroker taskBroker = new tasksBroker();
                    Iterator e = taskBroker.getListByProject("name", "ASC", thisForm.getProject(),
                        0, user.getId_account());
                    taskBroker.close();
                    
                    
                    // Se cargan todas las tareas de ese proyecto en una lista para
                    // que se despleguien en el combo de tareas asociadas.
                    ArrayList listaTasks = new ArrayList();
                    while (e.hasNext()) {
                        listaTasks.add(e.next());
                    }                    
                    request.setAttribute("listaTasks", listaTasks);
                    
                     // Se carga el Calendar con la fecha del Costo Adicional
                    Calendar now= Calendar.getInstance();
                    now.setTime(new java.sql.Timestamp(data.getAdditional_costs_date().getTime()));
                    request.setAttribute("costs_date", "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH)+1) +
                            "-" + now.get(Calendar.DATE));


                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        + "</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addCosts"));
                    
                    
                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);                
                     request.setAttribute("company",company);
                    thisForm.setOperation("applyEdit");
                    return (mapping.findForward("displayAddForm"));

/*                } else if (action.equals("view")) {
                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.
                    costsData data = (costsData)costBroker.getData(thisForm.getId());                

                    // Tambien se debe traer el dato del owner.
                    membersData memData = (membersData)memBroker.getData(data.getowner());               
                    request.setAttribute("owner", memData);                

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);                
                    thisForm.setOperation("view");

                    
                    if (thisForm.getfromPage().equals("viewProject")) {
                        projectsBroker projBroker = new projectsBroker();
                        projectsData projectMember = (projectsData)projBroker.getData(thisForm.getproject()); 
                        projBroker.close();
                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute", 
                            "<a href='./home.do'>"+
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                            +"</a>&nbsp;/" +
                            "<a href='./projects.do?operation=view&id=" + 
                            projectMember.getid() + "'>" + 
                            projectMember.getname() +"</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addFiles"));
                    } else { 
                        tasksBroker taskBroker = new tasksBroker();
                        tasksData dataTask = (tasksData)taskBroker.getData(thisForm.gettask()); 
                        taskBroker.close();
                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute", 
                            "<a href='./home.do'>"+
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                            +"</a>&nbsp;/" +
                            "<a href='./tasks.do?operation=view&id=" + 
                            dataTask.getid() + "'>" + 
                            dataTask.getname() +"</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addFiles"));
                        
                    }
                    
                    
                    return (mapping.findForward("displayViewForm"));
*/
                } else if (action.equals("delete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
           
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    ArrayList items = costBroker.getItems(fields, user.getId_account());                                    
                                        
                    request.setAttribute("param", request.getParameter("project"));
                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title", "");
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
                    ArrayList items = costBroker.getItems(fields, user.getId_account());                                    
                   
                    // delete each file entry in DB and your file asociated.
                    costsData datatemp = new costsData();
                    
                    ListIterator li = items.listIterator();
                    while (li.hasNext()) {
                        datatemp = (costsData)li.next(); 
                        costBroker.delete(datatemp);                        
                    }
                     request.setAttribute("company",company);
                    // return to the page that list the costs...
                    return (new ActionForward(mapping.findForward("viewProject").
                        getPath()+"&id="+request.getParameter("param"), false));
                    
                } else if (action.equals("applyAdd")) {

                    // Se trata de la aplicacion de un insert en la BD
                    costsData data = new costsData();
                    
                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);                                

                    // Se setean los valores reales y estandards
                    data.setReal_Cost(new java.math.BigDecimal(request.getParameter("real_cost")));
                    data.setStandard_Cost(new java.math.BigDecimal(request.getParameter("standard_cost")));                    
                    
                    // Save the information about the file in DB  

                    System.out.println("Data:"+data.getAdditional_costs_date().toString());
                    System.out.println("ThisForm:"+thisForm.getAdditional_costs_date().toString());
                    data.setId(0);
                    data.setId_account(user.getId_account());
                    /*data.setProject(thisForm.getProject());
                    data.setTasks(thisForm.getTasks());
                    data.setDescription(thisForm.getDescription());
                    data.setUnits(thisForm.getUnits());*/
                    costBroker.add(data);
                    request.setAttribute("company",company);
                    // If the page from this action was called is a project
                    return (new ActionForward(mapping.findForward("viewProject").
                        getPath()+"&id="+thisForm.getProject(), true));                

                } else if (action.equals("applyEdit")) {
                    // Se trata de la aplicacion de un update en la BD
                    costsData data = new costsData();
                    PropertyUtils.copyProperties(data, thisForm);                                

                    // Se setean los valores reales y estandards
                    data.setReal_Cost(new java.math.BigDecimal(request.getParameter("real_cost")));
                    data.setStandard_Cost(new java.math.BigDecimal(request.getParameter("standard_cost")));                    
                    data.setId_account(user.getId_account());

                    System.out.println("Data:"+data.getAdditional_costs_date().toString());
                    System.out.println("ThisForm:"+thisForm.getAdditional_costs_date().toString());
                    // We add the new record.
                    costBroker.update(data);
                    request.setAttribute("company",company);
                    // Y por ultimo lo agregramos al contexto para que el usuario vea
                    // la lista actualizada.
                    return (new ActionForward(mapping.findForward("viewProject").
                        getPath()+"&id="+data.getProject(), true));    
                    
                } else if (action.equals("showAll") || action.equals("sortAll")) {
                    // Se trata de un showAll de las tareas asociadas a un proyecto determinado.                    
                    Iterator e ;
                    e = costBroker.getListByProject(thisForm.getsortColumn(),
                            thisForm.getsortOrder(),
                            thisForm.getProject(), 0, user.getId_account());

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                       lista.add(e.next());
                    }
                    // Se guarda la lista de files asociados a esta tarea.
                    request.setAttribute("listCosts", lista);

                   if (thisForm.getOperation().equalsIgnoreCase("sortAll")) {
                       // Negamos el tipo de ordenamiento
                       if (thisForm.getsortOrder().equalsIgnoreCase("ASC"))
                           thisForm.setsortOrder("DESC");
                       else
                           thisForm.setsortOrder("ASC");
                   }                                    
                   
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projectMember = (projectsData)projBroker.
                        getData(thisForm.getProject(), user.getId_account()); 
                    projBroker.close();
                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute", 
                        "<a href='./home.do'>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
                        +"</a>&nbsp;/" +
                        "<a href='./projects.do?operation=view&id=" + 
                        projectMember.getid() + "'>" + 
                        projectMember.getname() +"</a>&nbsp;/" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.allCosts"));
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAllCostsByProject")); 
                }

            }
            catch (Exception e)  {              
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "costs.do",  "perform", 
                                "Fatal Error: " + e.toString());                  
                e.printStackTrace();
                return (mapping.findForward("listing"));                

            } finally {
               costBroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }

}