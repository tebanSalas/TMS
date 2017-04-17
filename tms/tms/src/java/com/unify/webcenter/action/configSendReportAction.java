/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.ArrayList;

/**
 *
 * @author MARCELA QUINTERO
 */
public class configSendReportAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of configSendReportAction */
    public configSendReportAction() {

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
        membersBroker memberBroker;
        configSendReportBroker configBroker;
        masterReportsBroker reportBroker;
        session = request.getSession(false);
        String company = null;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page

            request.setAttribute("company", company);
            return (mapping.findForward("login"));

        } else {
            memberBroker = new membersBroker();
            configBroker = new configSendReportBroker();
            reportBroker = new masterReportsBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("login");

                company = TMSConfigurator.getCompany(user);
                if (form == null) {
                    System.out.println(" Creating new costsForm bean under key " + mapping.getAttribute());
                    form = new configSendReportForm();
                }

                configSendReportForm thisForm = (configSendReportForm) form;

                // fetch action from form
                action = thisForm.getOperation();

                servlet.log("[DEBUG] configSendReportAction at perform(): Action is " + action);

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                // Determine what to do
                if (action.equals("view")) {
                    Iterator r =null;
                    r= reportBroker.getList("name", "ASC");
                    ArrayList listaReport = new ArrayList();
                    masterReportData dataReport = new masterReportData();                   
                    boolean band = false;
                      if (request.getParameter("reports")!=null){
                        session.setAttribute("report", new Integer(request.getParameter("reports").toString()));
                        band=true;
                    }
                    while (r.hasNext()) {
                        dataReport = new masterReportData();
                        dataReport = (masterReportData) r.next();                      
                        if (!band){
                            session.setAttribute("report", String.valueOf(dataReport.getId()));                       
                            band=true;
                        }
                        listaReport.add(dataReport);
                    }
                    
                    request.setAttribute("listReport", listaReport);
                    
                    Iterator e = null;
                    e = configBroker.getList("members", "ASC", Integer.parseInt(session.getAttribute("report").toString()),user.getId_account());
                    ArrayList lista = new ArrayList();
                    configSendReportData data = new configSendReportData();
                    ArrayList members = new ArrayList();
                    while (e.hasNext()) {
                        data = new configSendReportData();
                        data = (configSendReportData) e.next();
                        members.add(new Integer(data.getMembers()));
                        lista.add(data);
                    }
                    Iterator m = null;
                    m = memberBroker.getList("login", "ASC", user.getId_account());
                    ArrayList listMembers = new ArrayList();
                    membersData memberData = new membersData();
                    while (m.hasNext()) {
                        data = new configSendReportData();
                        memberData = new membersData();
                        memberData = (membersData) m.next();
                        data.setFormat("N");
                        data.setId_account(user.getId_account());
                        data.setId_master_report(1);
                        data.setNotification(0);
                        data.setParentMember(memberData);
                        data.setPeriodicity("N");
                        if (!members.contains(new Integer(memberData.getid())))
                            lista.add(data);
                        listMembers.add(memberData);
                    }

                    request.setAttribute("list", lista);

                    request.setAttribute("listMember", listMembers);

                    thisForm.setSortColumn("DESC");
                    thisForm.setOperation("applyAdd");
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ConfigSendReport"));
                    // forward to display the list 
                    request.setAttribute("company", company);
                    // En el caso de la operacion add, se despliega el formulario.                    
                    return (mapping.findForward("view"));
                } else if (action.equals("applyAdd")) {
                    configSendReportData data = new configSendReportData();
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    String lista = "";                    
                    String[] splitLista=null;
                    for (int i = 0; i < fields.length; i++) {
                        lista = fields[i];
                        System.out.println("lista");
                        System.out.println(lista);
                        splitLista = lista.split("-");
                        System.out.println(splitLista.length);
                        data.setId_account(user.getId_account());
                        data.setId_master_report(Integer.parseInt(session.getAttribute("report").toString()));
                        data.setMembers(Integer.parseInt(splitLista[0].toString()));
                        data.setPeriodicity(splitLista[1].toString());
                        data.setNotification(Integer.parseInt(splitLista[2].toString()));
                        data.setFormat(splitLista[3].toString());
                        configBroker.add(data);
                    }             
                    request.setAttribute("company", company);
                    session.removeAttribute("report");
                    // regresa al menu         
                    return (mapping.findForward("main"));
                }/*else  if (action.equals("change")) {
                      Iterator r =null;
                   r= reportBroker.getList("name", "ASC");
                      ArrayList listaReport = new ArrayList();
                    masterReportData dataReport = new masterReportData();                   
                    boolean band = false;
                    System.out.println("aca");
                    System.out.println(request.getParameter("reports"));
                    if (request.getParameter("reports")!=null){
                        session.setAttribute("report", new Integer(request.getParameter("reports").toString()));
                        band=true;
                    }
                    while (r.hasNext()) {
                        dataReport = new masterReportData();
                        dataReport = (masterReportData) r.next();                      
                        if (!band){
                            session.setAttribute("report", new Integer(dataReport.getId()));                        
                            band=true;
                        }
                        listaReport.add(dataReport);
                    }
                    
                    request.setAttribute("listReport", listaReport);
                    
                    Iterator e = null;
                    e = configBroker.getList("members", "ASC", Integer.parseInt(session.getAttribute("report").toString()));
                    ArrayList lista = new ArrayList();
                    configSendReportData data = new configSendReportData();
                    ArrayList members = new ArrayList();
                    while (e.hasNext()) {
                        data = new configSendReportData();
                        data = (configSendReportData) e.next();
                        members.add(new Integer(data.getMembers()));
                        lista.add(data);
                    }
                    Iterator m = null;
                    m = memberBroker.getList("login", "ASC", user.getId_account());
                    ArrayList listMembers = new ArrayList();
                    membersData memberData = new membersData();
                    while (m.hasNext()) {
                        data = new configSendReportData();
                        memberData = new membersData();
                        memberData = (membersData) m.next();
                        data.setFormat("N");
                        data.setId_account(user.getId_account());
                        data.setId_master_report(1);
                        data.setNotification(0);
                        data.setParentMember(memberData);
                        data.setPeriodicity("N");
                        if (!members.contains(new Integer(memberData.getid())))
                            lista.add(data);
                        listMembers.add(memberData);
                    }

                    request.setAttribute("list", lista);

                    request.setAttribute("listMember", listMembers);

                    thisForm.setSortColumn("DESC");
                    thisForm.setOperation("applyAdd");
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.ConfigSendReport"));
                    // forward to display the list 
                    request.setAttribute("company", company);
                    // En el caso de la operacion add, se despliega el formulario.                    
                    return (mapping.findForward("view"));
                } */
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "configSendReport.do", "main",
                        "Fatal Error: " + e.toString());
                e.printStackTrace();
                return (mapping.findForward("main"));

            } finally {
                memberBroker.close();
                configBroker.close();
                reportBroker.close();
            }
            request.setAttribute("company", company);
            // Default if everthing else fails
            return (mapping.findForward("main"));
        }
    }
}
