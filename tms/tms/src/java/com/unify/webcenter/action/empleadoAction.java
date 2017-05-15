/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.empleadoBroker;
import com.unify.webcenter.broker.projectsBroker;
import com.unify.webcenter.broker.risksBroker;
import com.unify.webcenter.broker.tasksBroker;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.empleadosData;
import com.unify.webcenter.data.loginData;
import com.unify.webcenter.data.projectsData;
import com.unify.webcenter.data.risksData;
import com.unify.webcenter.data.tasksData;
import com.unify.webcenter.form.costsForm;
import com.unify.webcenter.form.empleadoForm;
import com.unify.webcenter.form.risksForm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
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
public class empleadoAction extends Action {
    
    private static Logger logger = Logger.getLogger("com.unify");
    
        public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                 throws IOException, ServletException {
        String action;
        HttpSession session;
        empleadoBroker empleadoBroker;
        String patternStr = ","; 
        String company = null;
        
        session = request.getSession(false);
       
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page               
            return (mapping.findForward("login"));            
            
        } else {   
            empleadoBroker = new empleadoBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");  
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
        
                if (form == null) {
                        System.out.println(" Creating new costsForm bean under key "
                                    + mapping.getAttribute());
                    form = new empleadoForm();
                }

                empleadoForm thisForm = (empleadoForm) form;

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
                        "<a href='./empleados.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.empleado")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.listadoEmp")+"</a>");
                       request.setAttribute("company",company);
                     return (mapping.findForward("displayAddForm"));
                    
                } else if (action.equals("edit")) {
                   
                    return (mapping.findForward("displayAddForm"));

                } else if (action.equals("listing")) {
                    request.setAttribute("menuRoute", 
                        "<a href='./empleados.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.empleado")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.listadoEmp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = empleadoBroker.getList(user.getId_account());
                    request.setAttribute("listaEmpleados", e);
                    
                    return (mapping.findForward("listing"));

                } else if (action.equals("delete")) {
                    
                    return (mapping.findForward("confirmDelete"));                
                                        
                } else if (action.equals("applyDelete")) {
                    
                    
                        return (mapping.findForward("displayAllRisksByProject"));
                         
                } else if (action.equals("applyAdd")) {
//                    // Se trata de la aplicacion de un insert en la BD
                    empleadosData data = new empleadosData();
//                    
//                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(data, thisForm);                                
//                  
//                    // Save the information about the file in DB                            
                    data.setId(0);
                      data.setAccount(user.getId_account());
                    empleadoBroker.add(data);
//                    
                  request.setAttribute("menuRoute", 
                        "<a href='./empleados.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.empleado")
                        + "</a>&nbsp;/" +
                        "<a>"+ 
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.listadoEmp")+"</a>");
                    request.setAttribute("company",company);
                    Iterator e = empleadoBroker.getList(user.getId_account());
                    request.setAttribute("listaEmpleados", e);
                    
                    return (mapping.findForward("listing")); 
//                    
//                } else if (action.equals("applyEdit")) {
//                    // Se trata de la aplicacion de un update en la BD
//                    risksData data = new risksData();
//                    PropertyUtils.copyProperties(data, thisForm);                                
//
//                    // We add the new record.
//                    riskBroker.update(data);
//                         
//                    projectsBroker projBroker = new projectsBroker();
//                    projectsData projectMember = (projectsData)projBroker.
//                            getData(thisForm.getproject(),user.getId_account()); 
//                    projBroker.close();
//
//                    // Se agrega el link para el menu con la ruta
//                    request.setAttribute("menuRoute", 
//                        "<a href='./home.do'>" +
//                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("header.displayStart")
//                        + "</a>&nbsp;/" +
//                        "<a href='./projects.do?operation=view&id=" + 
//                        projectMember.getid() + "'>" + 
//                        projectMember.getname() +"</a>&nbsp;/" +
//                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.allRisks"));
//
//                     // Y por ultimo lo agregramos al contexto para que el usuario vea
//                    // la lista actualizada.
//                     request.setAttribute("company",company);
//                    return (new ActionForward(mapping.findForward("viewProject").
//                        getPath()+"&id="+data.getproject(), true));                
                   
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
                empleadoBroker.close();
            }
             request.setAttribute("company",company);
            // Default if everthing else fails
            return (mapping.findForward("listing"));                
        }        
    }        
}
