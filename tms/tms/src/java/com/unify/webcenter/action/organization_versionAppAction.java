/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.applicationBroker;
import com.unify.webcenter.broker.organization_versionAppBroker;
import com.unify.webcenter.broker.organizationsBroker;
import com.unify.webcenter.broker.versionAppBroker;
import com.unify.webcenter.broker.versionBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.data.membersData;
import com.unify.webcenter.data.organization_versionAppData;
import com.unify.webcenter.data.organizationsData;
import com.unify.webcenter.data.versionAppData;
import com.unify.webcenter.form.organization_versionAppForm;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author esper
 */
public class organization_versionAppAction extends Action{
    
    private static Logger logger = Logger.getLogger("com.unify");
    
        public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                 throws IOException, ServletException {
        String action;
        HttpSession session;
        String patternStr = ","; 
        String company = null;
        organization_versionAppBroker orgaVerAppbroker;
        versionAppBroker verAppBroker;
        
        
        session = request.getSession(false);
       
        
        if (session == null || session.getAttribute("login") == null) { // Si la sesion es nula, se debe redireccionar al login.
            // forward to display the home page               
            return (mapping.findForward("login"));            
        } else {   
            orgaVerAppbroker = new organization_versionAppBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");  
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
        
                if (form == null) {
                    System.out.println(" Creating new costsForm bean under key " + mapping.getAttribute());
                    form = new organization_versionAppForm();
                }
                
                //ActionErrors errors = new ActionErrors();
                
                organization_versionAppForm thisForm = (organization_versionAppForm) form;
                // fetch action from form
                action = thisForm.getOperation();
                servlet.log("[DEBUG] organization_versionApp at perform(): Action is " + action);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);                   
                
// Determine what to do
                if  (action.equals("listing")) {
                    
//                    Iterator e = orgaVerAppbroker.getAppsNOorganizacion(thisForm.getId_organization());
//                    request.setAttribute("listaVerAppsXOrga", e);
                    return (mapping.findForward("listing"));

                } else if (action.equals("add")) {
// Para insertar solo un registro 
//                    organization_versionAppData data = new organization_versionAppData();
//                    PropertyUtils.copyProperties(data, thisForm); 
//                    data.setId_verapp(data.getId());
//                    data.setId(0);
//                    orgaVerAppbroker.add(data);
//                    request.setAttribute("company",company);

//Para insertar varios                     
                    //capturo el valor de la organizacion de la cuenta
                    int idOrga = Integer.parseInt(request.getParameter("id_organization"));
                    int idaccount = Integer.parseInt(request.getParameter("id_account"));
                    //capturo el id de los applicaivos que se van agregar
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);
                    verAppBroker =  new versionAppBroker();
                    ArrayList items = verAppBroker.getItems(fields,user.getId_account());   
                    
                    //creo un objeto de tipo orgaVerApp para guardar el registro 
                    organization_versionAppData datatemp = new organization_versionAppData();
                    
                    versionAppData verAppData = new versionAppData();
                    
                    ListIterator li = items.listIterator();
                    while (li.hasNext()) {
                        verAppData = (versionAppData)li.next();
                        //contruyo el objeto a insertarse
                        datatemp.setId(0);
                        datatemp.setId_verapp(verAppData.getId());
                        datatemp.setId_organization(idOrga);
                        datatemp.setId_account(idaccount);
                            orgaVerAppbroker.add(datatemp);
                            
                    }
                            request.setAttribute("menuRoute", 
                                "<a href='./admin.do'>" +
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                                + "</a>&nbsp;/" +
                                "<a>"+ 
                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                            request.setAttribute("company",company);
                            
                            
                            return (new ActionForward(mapping.findForward("viewOrganization").
                                    getPath() + "?operation=view&id=" + idOrga));
//                                 
                                        
                } else if (action.equals("addVerApps")) {
                    
                    request.setAttribute("title",
                     java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addClientOrg"));
//                   // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./organizations.do?operation=listing'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations") +
                            "</a>&nbsp;/&nbsp;" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addOrganization"));
                    request.setAttribute("company", company);

                    int idOrga = Integer.parseInt(request.getParameter("organization").toString());
//                     idOrga = Integer.parseInt(request.getAttribute("organization").toString());
                    organizationsBroker broker = new organizationsBroker();
                    organizationsData data = (organizationsData) broker.getData(idOrga);
                    request.setAttribute("organization", data);
                    
                    
                    versionAppBroker vab = new versionAppBroker();
                    Iterator i =  vab.getList();
                    
                    ArrayList lista = new ArrayList();
                    versionAppData verAppData = new versionAppData();
                     
                    
                    while (i.hasNext()) {
                        verAppData = (versionAppData) i.next();
                        if(orgaVerAppbroker.getAppsNOorganizacion(idOrga, verAppData.getId()) == true){
                            lista.add(verAppData);
                        }
                        
                    }
                    
                    request.setAttribute("listaVerAppsXOrga", lista);
                    vab.close(); 
                    broker.close();
                    return (mapping.findForward("addVerApps"));

                    
                }else if (action.equals("deleteOrgaVerApp")) {
                    int idOrga = Integer.parseInt(request.getParameter("organization"));
//                    //capturo el id de los applicaivos que se van agregar
                    String[] fields = (request.getParameter("ovacheckedItems")+",").split(patternStr);
//                    verAppBroker =  new versionAppBroker();
                    ArrayList items = orgaVerAppbroker.getItems(fields,user.getId_account());   
//                    //creo un objeto de tipo orgaVerApp para guardar el registro 
                    organization_versionAppData datatemp = new organization_versionAppData();
                    versionAppData verAppData = new versionAppData();
                    ListIterator li = items.listIterator();
                    while (li.hasNext()) {
                        datatemp = (organization_versionAppData)li.next();
                        orgaVerAppbroker.delete(datatemp);
                    }
                    request.setAttribute("menuRoute", 
                        "<a href='./admin.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
                    request.setAttribute("company",company);
                        return (new ActionForward(mapping.findForward("viewOrganization").
                                    getPath() + "?operation=view&id=" + idOrga));
                }else if (action.equals("delete")) {
                    int idOrga = Integer.parseInt(request.getParameter("organization"));
                    organizationsBroker broker = new organizationsBroker();
                    organizationsData data = (organizationsData) broker.getData(idOrga);
                    request.setAttribute("organization", data);
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("ovacheckedItems") + ",").split(",");
                    ArrayList items = orgaVerAppbroker.getItems(fields, user.getId_account());

                    request.setAttribute("ovacheckedItems", request.getParameter("ovacheckedItems"));
                    request.setAttribute("items", items);
                    //request.setAttribute("fromPage", "" + thisForm.getTheDate());                
                    request.setAttribute("title", java.util.ResourceBundle.getBundle("ApplicationResources",
                            new Locale(user.getlanguage(), "")).getString("common.ClientOrganizations"));
                    request.setAttribute("do", request.getRequestURI());
                    request.setAttribute("company", company);
                  //  request.setAttribute("organization",);
                    // forward to display the list 
                    return (mapping.findForward("confirmDelete"));

                }
            }
            catch (Exception e)  {              
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                //logger.logp(Level.SEVERE, "risks.do",  "perform", 
                  //              "Fatal Error: " + e.toString());                  
                e.printStackTrace();
                return (mapping.findForward("listing"));                

            } finally {
                orgaVerAppbroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }    
    
}
