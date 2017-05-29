/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.versionAppBroker;
import com.unify.webcenter.broker.versionBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.applicationData;
import com.unify.webcenter.data.versionData;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.data.risksData;
import com.unify.webcenter.data.versionAppData;
import com.unify.webcenter.form.versionForm;
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
public class versionAction extends Action{
    
    private static Logger logger = Logger.getLogger("com.unify");
    
        public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                 throws IOException, ServletException {
        String action;
        HttpSession session;
        versionBroker versionBroker;
        String patternStr = ","; 
        String company = null;
        versionAppBroker verAppbroker;
        
        session = request.getSession(false);
       
        
        if (session == null || session.getAttribute("login") == null) { // Si la sesion es nula, se debe redireccionar al login.
            // forward to display the home page               
            return (mapping.findForward("login"));            
        } else {   
            versionBroker = new versionBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");  
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
        
                if (form == null) {
                    System.out.println(" Creating new costsForm bean under key " + mapping.getAttribute());
                    form = new versionForm();
                }
                
                ActionErrors errors = new ActionErrors();
                
                versionForm thisForm = (versionForm) form;
                // fetch action from form
                action = thisForm.getOperation();
                servlet.log("[DEBUG] empleadoAction at perform(): Action is " + action);
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
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.addVersion")+"</a>");
                  
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));
                    
                } else if (action.equals("edit")) {
                    versionData data = (versionData) versionBroker.getData(thisForm.getId());
                    request.setAttribute("title", ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.editAccount"));
                    PropertyUtils.copyProperties(thisForm, data);
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.updateVersion")+"</a>");
                        thisForm.setOperation("applyEdit");
                     
                    request.setAttribute("company",company);
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("listing")) {
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVersion")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = versionBroker.getList();
                    request.setAttribute("listaVersiones", e);
                    return (mapping.findForward("listing"));

                } else if (action.equals("delete")) {
                    
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
           
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    ArrayList items = versionBroker.getItems(fields,user.getId_account());                                    
                                        
                    request.setAttribute("param", request.getParameter("project"));
                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title", "Version");
                    request.setAttribute("do",request.getRequestURI());
                    request.setAttribute("company",company);
                    // forward to display the list
                    return (mapping.findForward("confirmDelete"));              
                                        
                } else if (action.equals("applyDelete")) {
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);  
                    ArrayList items = versionBroker.getItems(fields,user.getId_account());   
                    // delete each file entry in DB and your file asociated.
                    versionData datatemp = new versionData();
                    ListIterator li = items.listIterator();
                    verAppbroker =  new versionAppBroker();
                    while (li.hasNext()) {
                        datatemp = (versionData)li.next();
                        int idTemp = datatemp.getId();
                        boolean exist = verAppbroker.existVersion(idTemp);//(datatemp.getId());
                        if(!exist){
                            versionBroker.delete(datatemp);
                        }else{
                            return (mapping.findForward("cantDelete")); 
                        }
                    }
                        Iterator e = versionBroker.getList();
                            request.setAttribute("listaVersiones", e);
                            request.setAttribute("menuRoute", 
                                "<a href='./admin.do'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                                + "</a>&nbsp;/" +
                                "<a>"+ 
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVersion")+"</a>");
                        request.setAttribute("company",company);
                        verAppbroker.close();
                        return (mapping.findForward("listing"));
                                             
                } else if (action.equals("applyAdd")) {
                        errors = thisForm.validate(mapping, request);
                        if (errors.isEmpty()) {
    //                    // Se trata de la aplicacion de un insert en la BD
                        versionData data = new versionData();
                        // We copy all the properties from the form to the bean.
                        PropertyUtils.copyProperties(data, thisForm);                                              
    //                    // Save the information about the file in DB                            
                        data.setId(0);
                        if (data.getDescription().equals("")){
                            data.setDescription("N/A");
                        }
                          //data.setAccount(user.getId_account());
                        versionBroker.add(data);
    //                    
                        request.setAttribute("menuRoute", 
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                            + "</a>&nbsp;/" +
                            "<a>"+ 
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVersion")+"</a>");
                        request.setAttribute("company",company);
                        Iterator e = versionBroker.getList();
                        request.setAttribute("listaVersiones", e);
                        //return (mapping.findForward("listing")); 
                        return (new ActionForward(mapping.findForward("listing")));
                        }else {
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=add"));
                    }  
                    
//                    
                } else if (action.equals("applyEdit")) {
                    errors = thisForm.validate(mapping, request);
                        if (errors.isEmpty()) {
                    // Se trata de la aplicacion de un update en la BD
                    versionData data = new versionData();
                    PropertyUtils.copyProperties(data, thisForm);   
                    // We add the new record.
                    if (data.getDescription().equals("")){
                            data.setDescription("N/A");
                        }
                    versionBroker.update(data);
                         
                    // Se agrega el link para el menu con la ruta
                   request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudVersion")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = versionBroker.getList();
                    request.setAttribute("listaVersiones", e);
                    
                    return (mapping.findForward("listing"));                
                   }else {
                        saveErrors(request, errors);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=edit"));
                    }  
                } else if (action.equals("showAll") || action.equals("sortAll")) {
                    
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
                versionBroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }     
    
}
