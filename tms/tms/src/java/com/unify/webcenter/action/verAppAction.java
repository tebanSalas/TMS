/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.applicationBroker;
import com.unify.webcenter.broker.versionAppBroker;
import com.unify.webcenter.broker.versionAppBroker;
import com.unify.webcenter.broker.versionBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.versionAppData;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.form.versionAppForm;
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author esper
 */
public class verAppAction extends Action{
    
    private static Logger logger = Logger.getLogger("com.unify");
    
        public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                 throws IOException, ServletException {
        String action;
        HttpSession session;
        versionAppBroker versionAppBroker;
        String patternStr = ","; 
        String company = null;
        versionAppBroker verAppbroker;
        versionBroker verBroker;
        applicationBroker appBroker;
        
        session = request.getSession(false);
       
        
        if (session == null || session.getAttribute("login") == null) { // Si la sesion es nula, se debe redireccionar al login.
            // forward to display the home page               
            return (mapping.findForward("login"));            
        } else {   
            versionAppBroker = new versionAppBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");  
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
        
                if (form == null) {
                    System.out.println(" Creating new costsForm bean under key " + mapping.getAttribute());
                    form = new versionAppForm();
                }
                
                //ActionErrors errors = new ActionErrors();
                
                versionAppForm thisForm = (versionAppForm) form;
                // fetch action from form
                action = thisForm.getOperation();
                servlet.log("[DEBUG] verAppAction at perform(): Action is " + action);
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
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVerApp")+"</a>");
                    
                    verBroker =  new versionBroker();
                    Iterator e = verBroker.getList();
                    request.setAttribute("listaVersion", e);
                    
                    appBroker =  new applicationBroker();
                    Iterator i = appBroker.getList();
                    request.setAttribute("listaApps", i);
                    
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));
                    
                } else if (action.equals("edit")) {
                    versionAppData data = (versionAppData) versionAppBroker.getData(thisForm.getId());
                    request.setAttribute("title", ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.updateApplication"));
                    PropertyUtils.copyProperties(thisForm, data);
                    
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.updateApplication")+"</a>");
                        
                    thisForm.setOperation("applyEdit");
                     
                    verBroker =  new versionBroker();
                    Iterator e = verBroker.getList();
                    request.setAttribute("listaVersion", e);
                    
                    appBroker =  new applicationBroker();
                    Iterator i = appBroker.getList();
                    request.setAttribute("listaApps", i);
                    
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("listing")) {
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVerApp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = versionAppBroker.getList();
                    request.setAttribute("listaVerApp", e);
                    return (mapping.findForward("listing"));

                } else if (action.equals("delete")) {
                    
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
           
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    ArrayList items = versionAppBroker.getItems(fields,user.getId_account());                                    
                                        
                    request.setAttribute("param", request.getParameter("project"));
                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title", "versionApp");
                    request.setAttribute("do",request.getRequestURI());
                    request.setAttribute("company",company);
                    // forward to display the list
                    
                    
                    return (mapping.findForward("confirmDelete"));              
                                        
                } else if (action.equals("applyDelete")) {
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);  
                    ArrayList items = versionAppBroker.getItems(fields,user.getId_account());   
                    // delete each file entry in DB and your file asociated.
                    versionAppData datatemp = new versionAppData();
                    ListIterator li = items.listIterator();
                    while (li.hasNext()) {
                        datatemp = (versionAppData)li.next();
                        verAppbroker =  new versionAppBroker();
//                        int idTemp = datatemp.getId();
//                        boolean exist = verAppbroker.existApp(idTemp);//(datatemp.getId());
//                        if(!exist){
                            versionAppBroker.delete(datatemp);
                            Iterator e = versionAppBroker.getList();
                            request.setAttribute("listaVerApp", e);
                            request.setAttribute("menuRoute", 
                                "<a href='./admin.do'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                                + "</a>&nbsp;/" +
                                "<a>"+ 
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                            request.setAttribute("company",company);
                            verAppbroker.close();
                            return (mapping.findForward("listing"));
//                        }else{
//                            verAppbroker.close();
//                            return (mapping.findForward("cantDelete"));
//                        }   
                    }
                     
                         
                } else if (action.equals("applyAdd")) {
//                    // Se trata de la aplicacion de un insert en la BD
                    versionAppData data = new versionAppData();
                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);                                              
//                    // Save the information about the file in DB  

                    data.setId(0);
                      //data.setAccount(user.getId_account());
                    versionAppBroker.add(data);
//                    
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVerApp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = versionAppBroker.getList();
                    request.setAttribute("listaVerApp", e);
                    
                    return (mapping.findForward("listing")); 
//                    
                } else if (action.equals("applyEdit")) {
                    // Se trata de la aplicacion de un update en la BD
                    versionAppData data = new versionAppData();
                    PropertyUtils.copyProperties(data, thisForm);   
                    // We add the new record.
                    versionAppBroker.update(data);
                         
                    // Se agrega el link para el menu con la ruta
                   request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVerApp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = versionAppBroker.getList();
                    request.setAttribute("listaVerApp", e);
                    
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
                versionAppBroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }    
}
