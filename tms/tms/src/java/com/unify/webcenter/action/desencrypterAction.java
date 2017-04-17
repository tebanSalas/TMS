/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.accountsBroker;
import com.unify.webcenter.broker.filesBroker;
import com.unify.webcenter.broker.projectsBroker;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts.action.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.accountsData;
import com.unify.webcenter.data.loginData;

import com.unify.webcenter.data.projectsData;
import com.unify.webcenter.desencrypter.StringEncrypter;
import com.unify.webcenter.form.encryptForm;
import com.unify.webcenter.tools.StringManipulator;
import com.unify.webcenter.tools.sendMail;
import java.io.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author MARCELA QUINTERO
 */
public class desencrypterAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    public desencrypterAction() {

    }

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        String action;
        HttpSession session;
        session = request.getSession(false);
        loginData user = new loginData();
        accountsBroker brokerAccount;
        projectsBroker projBroker;
        filesBroker fileBroker;
        String company = null;

        String separator = File.separator;
        System.out.println("action");
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page
            return (mapping.findForward("login"));

        } else {
            brokerAccount = new accountsBroker();
            projBroker = new projectsBroker();
            fileBroker = new filesBroker();
            // Se obtiene la referencia al loginData dentro de la session.
            user = (loginData) session.getAttribute("login");
            TMSConfigurator tms = new TMSConfigurator(user);
            company = tms.getCompany(user);

            // Intanciar clase para el envio de correo
            com.unify.webcenter.tools.sendMail sm = new com.unify.webcenter.tools.sendMail();
            try {
                if (form == null) {
                    System.out.println(" Creating new membersForm bean under key " + mapping.getAttribute());
                    form = new encryptForm();
                }


                ActionErrors errors = new ActionErrors();

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);
                request.setAttribute("company", company);

                encryptForm thisForm = (encryptForm) form;

                if (request.getParameter("operation") != null) {
                    action = request.getParameter("operation").toString();
                } else // fetch action from form
                {
                    action = ((encryptForm) form).getOperation();
                }
                servlet.log("[DEBUG] desencrypterAction at perform(): Action is " + action);
                // Determine what to do
                String passPhrase = TMSConfigurator.getPassPhrase();
                if (action.equals("applyDesEncrypt")) {
                    int account = ((encryptForm) form).getAccount();
                    accountsData data = (accountsData) brokerAccount.getData(account);
                    // Create encrypter/decrypter class
                    StringEncrypter desEncrypter = new StringEncrypter(passPhrase);

                    String desDecrypted = desEncrypter.decrypt(data.getKey_encriptation());
                    Iterator e = projBroker.getList(account);
                    projectsData projData = new projectsData();
                    while (e.hasNext()) {

                        projData = new projectsData();
                        projData = (projectsData) e.next();
                        fileBroker.desencryptFilebyProject(projData.getid(), account, desDecrypted);

                    }

                    return (mapping.findForward("main"));
                } else if (action.equals("applyEncrypt")) {

                    int account = ((encryptForm) form).getAccount();

                    accountsData data = (accountsData) brokerAccount.getData(account);
                    // Create encrypter/decrypter class
                    StringEncrypter desEncrypter = new StringEncrypter(passPhrase);

                    String desDecrypted = desEncrypter.decrypt(data.getKey_encriptation());
                    Iterator e = projBroker.getList(account);
                    projectsData projData = new projectsData();
                    while (e.hasNext()) {

                        projData = new projectsData();
                        projData = (projectsData) e.next();
                        fileBroker.encryptFilebyProject(projData.getid(), account, desDecrypted);

                    }
                    return (mapping.findForward("main"));
                } else if (action.equals("confirm")) {
                    if (request.getParameter("account") != null) {
                        accountsData data = (accountsData) brokerAccount.getData(Integer.parseInt(request.getParameter("account").toString()));
                        thisForm.setAccount(data.getId());
                        thisForm.setAccountName(data.getName());
                    }
                    return (mapping.findForward("confirmEncrypt"));
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "members.do", "perform",
                        "Fatal Error: " + e.toString());

                e.printStackTrace();

            } finally {
                // Se cierran los brokers creados

                brokerAccount.close();
            }
        }

        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("main"));
    }
}
