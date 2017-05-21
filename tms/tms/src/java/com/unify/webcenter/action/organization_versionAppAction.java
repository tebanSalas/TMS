/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.applicationBroker;
import com.unify.webcenter.broker.organization_versionAppBroker;
import com.unify.webcenter.broker.versionAppBroker;
import com.unify.webcenter.broker.versionBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.data.organization_versionAppData;
import com.unify.webcenter.form.organization_versionAppForm;
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
        
        versionBroker verBroker;
        applicationBroker appBroker;
        
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
                    
                    Iterator e = orgaVerAppbroker.getAppsNOorganizacion(thisForm.getId_organization());
                    request.setAttribute("listaVerAppsXOrga", e);
                    return (mapping.findForward("listing"));

                } else if (action.equals("add")) {
                    organization_versionAppData data = new organization_versionAppData();
                    PropertyUtils.copyProperties(data, thisForm); 
                    data.setId_verapp(data.getId());
                    data.setId(0);
                    orgaVerAppbroker.add(data);
                    
                    request.setAttribute("company",company);
                    
                    //agregar el return correcto
                    return (mapping.findForward("viewOrganization")); 
                   
                    
//                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);  
//                    ArrayList items = orgaVerAppbroker.getItems(fields,user.getId_account());   
//                    // delete each file entry in DB and your file asociated.
//                    organization_versionAppData datatemp = new organization_versionAppData();
//                    ListIterator li = items.listIterator();
//                    while (li.hasNext()) {
//                        datatemp = (organization_versionAppData)li.next();
//                        datatemp.setId(0);
//                            orgaVerAppbroker.add(datatemp);
//                            
//                    }
//                            request.setAttribute("menuRoute", 
//                                "<a href='./admin.do'>" +
//                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
//                                + "</a>&nbsp;/" +
//                                "<a>"+ 
//                                java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
//                            request.setAttribute("company",company);
//                            
//                            return (mapping.findForward("listing"));
                    
//                                 
                                        
                } else if (action.equals("applyDelete")) {
                    
                            
                            return (mapping.findForward("listing"));
//                
                }else if (action.equals("deleteOrgaVerApp")) {
                    
//                    organization_versionAppData ovaD = new organization_versionAppData();
//                    int id = Integer.parseInt(request.getParameter("idAppByOrga").toString());
//                    organization_versionAppBroker ovaB =  new organization_versionAppBroker();
//                    ovaD = (organization_versionAppData) ovaB.getData(id);
//                    int a =  3;
                    
                    String[] fields = (request.getParameter("checkedItems")+",").split(patternStr);  
                    ArrayList items = orgaVerAppbroker.getItems(fields,user.getId_account());   
                    // delete each file entry in DB and your file asociated.
                    organization_versionAppData datatemp = new organization_versionAppData();
                    ListIterator li = items.listIterator();
                    orgaVerAppbroker = new organization_versionAppBroker();
                    while (li.hasNext()) {
                        datatemp = (organization_versionAppData)li.next();                        
                            orgaVerAppbroker.delete(datatemp);                                                      
                        }
                    
//                    Iterator e = OrgaVerAppBroker.getList();
//                    request.setAttribute("listaAplicativos", e);
//                    request.setAttribute("menuRoute", 
//                        "<a href='./admin.do'>" +
//                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Administration")
//                        + "</a>&nbsp;/" +
//                        "<a>"+ 
//                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.crudApp")+"</a>");
//                    request.setAttribute("company",company);
                    orgaVerAppbroker.close();        
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
                orgaVerAppbroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }    
    
}
