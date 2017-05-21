/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.applicationBroker;
import com.unify.webcenter.broker.versionAppBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.data.applicationData;
import com.unify.webcenter.data.versionAppData;
import com.unify.webcenter.form.applicationForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author esper
 */
public class applicationAction extends Action{
    
    private static Logger logger = Logger.getLogger("com.unify");
    
        public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                 throws IOException, ServletException {
        String action;
        HttpSession session;
        applicationBroker applicationBroker;
        String patternStr = ","; 
        String company = null;
        versionAppBroker verAppbroker;
        
        session = request.getSession(false);
       
        
        if (session == null || session.getAttribute("login") == null) { // Si la sesion es nula, se debe redireccionar al login.
            // forward to display the home page               
            return (mapping.findForward("login"));            
        } else {   
            applicationBroker = new applicationBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");  
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
        
                if (form == null) {
                    System.out.println(" Creating new costsForm bean under key " + mapping.getAttribute());
                    form = new applicationForm();
                }
                
                ActionErrors errors = new ActionErrors();
                
                applicationForm thisForm = (applicationForm) form;
                // fetch action from form
                action = thisForm.getOperation();
                servlet.log("[DEBUG] applicationAction at perform(): Action is " + action);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);                   
                
// Determine what to do
                if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.                    
                    thisForm.setOperation("applyAdd");
                    thisForm.setId(0);
                    
                    // Se cargan todas las tareas de ese proyecto en una lista para
                    // que se despleguien en el combo de tareas asociadas.                  
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addApplication")+"</a>");
                  
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));
                    
                } else if (action.equals("edit")) {
                    applicationData data = (applicationData) applicationBroker.getData(thisForm.getId());
                    request.setAttribute("title", ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.updateApplication"));
                    PropertyUtils.copyProperties(thisForm, data);
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.updateApplication")+"</a>");
                        
                    thisForm.setOperation("applyEdit");
                     
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("listing")) {
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = applicationBroker.getList();
                    request.setAttribute("listaAplicativos", e);
                    return (mapping.findForward("listing"));

                } else if (action.equals("delete")) {
                    
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
           
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    ArrayList items = applicationBroker.getItems(fields,user.getId_account());                                    
                                        
                    request.setAttribute("param", request.getParameter("project"));
                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title", "Aplicativos");
                    request.setAttribute("do",request.getRequestURI());
                    request.setAttribute("company",company);
                    // forward to display the list
                    
                    
                    return (mapping.findForward("confirmDelete"));              
                                        
                } else if (action.equals("applyDelete")) {
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);  
                    ArrayList items = applicationBroker.getItems(fields,user.getId_account());   
                    // delete each file entry in DB and your file asociated.
                    applicationData datatemp = new applicationData();
                    ListIterator li = items.listIterator();
                    verAppbroker =  new versionAppBroker();
                    while (li.hasNext()) {
                        datatemp = (applicationData)li.next();
                        
                        int idTemp = datatemp.getId();
                        boolean exist = verAppbroker.existApp(idTemp);//(datatemp.getId());
                        if(!exist){
                            applicationBroker.delete(datatemp);                                                      
                        }else{
                            
                            return (mapping.findForward("cantDelete"));
                        }
                    }
                    Iterator e = applicationBroker.getList();
                    request.setAttribute("listaAplicativos", e);
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                    request.setAttribute("company",company);
                    verAppbroker.close();        
                    return (mapping.findForward("listing"));
                    
                         
                } else if (action.equals("applyAdd")) {
//                    // Se trata de la aplicacion de un insert en la BD
                    applicationData data = new applicationData();
                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);                                              
//                    // Save the information about the file in DB                            
                    data.setId(0);
                      //data.setAccount(user.getId_account());
                    applicationBroker.add(data);
//                    
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = applicationBroker.getList();
                    request.setAttribute("listaAplicativos", e);
                    
                    return (mapping.findForward("listing")); 
//                    
                } else if (action.equals("applyEdit")) {
                    // Se trata de la aplicacion de un update en la BD
                    applicationData data = new applicationData();
                    PropertyUtils.copyProperties(data, thisForm);   
                    // We add the new record.
                    applicationBroker.update(data);
                         
                    // Se agrega el link para el menu con la ruta
                   request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = applicationBroker.getList();
                    request.setAttribute("listaAplicativos", e);
                    
                    return (mapping.findForward("listing"));                
                   
                } 

            }
            catch (Exception e)  {              
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                //logger.logp(Level.SEVERE, "risks.do",  "perform", 
                  //              "Fatal Error: " + e.toString());                  
                e.printStackTrace();
                return (mapping.findForward("listing"));                

            } finally {
                applicationBroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }    
}
