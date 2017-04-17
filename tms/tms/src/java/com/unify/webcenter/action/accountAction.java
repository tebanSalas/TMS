/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.accountsBroker;
import com.unify.webcenter.broker.membersBroker;

import com.unify.webcenter.broker.organizationsBroker;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts.action.*;
import java.sql.Timestamp;

import javax.servlet.*;
import javax.servlet.http.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.crons.sendNotificationEndOfTrial;
import com.unify.webcenter.crons.sendNotificationEndOfTrialUsers;
import com.unify.webcenter.data.accountsData;
import com.unify.webcenter.data.loginData;

import com.unify.webcenter.data.membersData;
import com.unify.webcenter.data.organizationsData;
import com.unify.webcenter.desencrypter.StringEncrypter;
import com.unify.webcenter.form.accountsForm;
import com.unify.webcenter.tools.StringManipulator;
import com.unify.webcenter.tools.sendMail;
import java.io.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author MARCELA QUINTERO
 */
public class accountAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    public accountAction() {

    }

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        String action;
        HttpSession session;
        accountsBroker broker = new accountsBroker();
        membersBroker memberBroker = new membersBroker();
        organizationsBroker organizationBroker = new organizationsBroker();
        organizationsData organizationData;
        session = request.getSession(false);
        loginData user = new loginData();
        String company = null;

        String separator = File.separator;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the home page
            return (mapping.findForward("login"));

        } else {
            // Se obtiene la referencia al loginData dentro de la session.
            user = (loginData) session.getAttribute("login");
            TMSConfigurator tms= new TMSConfigurator(user);
            company = tms.getCompany(user);
            request.setAttribute("version", tms.getVersion());
            // Intanciar clase para el envio de correo
            com.unify.webcenter.tools.sendMail sm = new com.unify.webcenter.tools.sendMail();
            try {
                if (form == null) {
                    System.out.println(" Creating new membersForm bean under key " + mapping.getAttribute());
                    form = new accountsForm();
                }


                ActionErrors errors = new ActionErrors();

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                accountsForm thisForm = (accountsForm) form;

                // fetch action from form
                action = ((accountsForm) form).getOperation();


                servlet.log("[DEBUG] accountAction at perform(): Action is " + action);


                // Determine what to do
                if (action == null || action.equals("listing")) {

                    request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>");
                    // Traer los valores para desplegar la lista de organizaciones disponibles.
                    // Constituye un listado completo y para ello nos valemos del broker.                                              
                    // Y por ultimo lo agregramos al contexto                 
                    Iterator e = broker.getList("name", "ASC", user.getId_account());
                    ArrayList lista = new ArrayList();
                    accountsData data = new accountsData();
                    while (e.hasNext()) {
                        data = (accountsData) e.next();
                        lista.add(data);
                    }
                    request.setAttribute("list", lista);

                    thisForm.setSortOrder("DESC");

                    request.setAttribute("company", company);
                    return (mapping.findForward("listing"));
                } else if (action.equals("add")) {
                    
        
                    Calendar now = Calendar.getInstance();

                    // Se extrae el componente dia, mes y a?o del start_date
                    request.setAttribute("trial_date", "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DATE));
                    
                    // En el caso de la operacion add, se despliega el formulario.
                    request.setAttribute("title",
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AddAccount"));
                    thisForm.setOperation("applyAdd");
                    thisForm.setCreator("TMS SYSTEM DAYTONASOFT");
                    thisForm.setActive("S");
                    request.setAttribute("company", company);

                    request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>&nbsp;/&nbsp;" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AddAccount"));

                    return (mapping.findForward("displayAddForm"));
                } else if (action.equals("applyAdd")) {


                    organizationData = new organizationsData();


                    // Se validan los datos ingresados
                    errors = thisForm.validate(mapping, request);

                    if (errors.isEmpty()) {

                         String passPhrase = TMSConfigurator.getPassPhrase();
                        // Se trata de la aplicacion de un insert en la BD
                        accountsData data = new accountsData();

                        // We copy all the properties from the form to the bean.                    
                        PropertyUtils.copyProperties(data, thisForm);
                        //retrieve the file representation
                        FormFile file = thisForm.getLink();

                        // Get the URL to the upload dir        
                        String sub = tms.getDownloadPath();
                        String fileName = null;

                        // Si el string no termina con el separador de files, se agrgega
                        if (!sub.endsWith(separator)) {
                            sub = sub + separator;
                        }

                        accountsData data2 = new accountsData();
                        if (file.getFileSize() == 0) {

                            membersData member = new membersData();

                            member = (membersData) memberBroker.getData(user.getid());

                            data2 = (accountsData) broker.getData(member.getId_account());

                            data.setAccount_logo(data2.getAccount_logo());
                            //retrieve the file name
                            fileName = data2.getAccount_logo();
                           
                        } else {
                            data.setAccount_logo(file.getFileName());
                            //retrieve the file name
                            fileName = file.getFileName();
                           
                        }
                        data.setId(0);

                        if (!"S".equals(thisForm.getActive())) {
                            data.setActive("N");
                        }
                        
                          // Create encrypter/decrypter class
                        StringEncrypter desEncrypter = new StringEncrypter(passPhrase);
                        String secretKey = desEncrypter.secretKey();
                        String desEncrypted = desEncrypter.encrypt(secretKey);
                      
                        data.setKey_encriptation(desEncrypted);

                        //Agrega la nueva fecha de prueba
                        if (thisForm.getFinal_trial_date().toString().compareTo("0001-01-01 12:00:00.0")==0 )
                         {
                             data.setFinal_trial_date(null);
                         }else{
                             data.setFinal_trial_date(thisForm.getFinal_trial_date());
                         }
                        
                        broker.add(data);

                        //Agrega la organizacion nueva
                        organizationData.setId_account(data.getId());
                        organizationData.setname(data.getName());
                        organizationData.setcod_pais(0);
                        organizationData.setaddress1(data.getAddress());
                        organizationData.setphone(data.getPhone_1());
                        organizationData.setemail(data.getEmail());
                        organizationData.setcreated(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        organizationBroker.add(organizationData);

                        String[] splitSub = sub.split("ACC" + user.getId_account());
                        if (splitSub.length > 1) {
                            sub = splitSub[0] + "ACC" + data.getId() + separator;
                        }
                        // Save the file                        

                        String subdir = sub;
                        
                        // Removemos cualquier caracter invalido
                        fileName = StringManipulator.generateValidFilename(fileName);

                        String fullpath = subdir + fileName;

                        
                        
                        File theFile = new File(fullpath);
                        // Create directory for the file, if requested.
                        if (theFile.getParentFile() != null) {
                            theFile.getParentFile().mkdirs();
                        }
                        //write the file to the file specified
                        OutputStream bos = new FileOutputStream(theFile);

                        int bytesRead = 0;
                        byte[] buffer = new byte[8192];

                        if (file.getFileSize() > 0) {

                            //retrieve the file data
                            InputStream stream = file.getInputStream();



                            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }


                            //close the stream
                            stream.close();
                        } else {

                            String[] splitSub_ = sub.split("ACC");
                            if (splitSub_.length > 1) {
                                sub = splitSub_[0] + "ACC" + data2.getId() + separator;
                            }
                            // Save the file                        
                            String subdir_ = sub;
                            String fullpath_ = subdir_ + fileName;
                            // Read from a file
                            File sourceimage = new File(fullpath_);

                            // Read from an input stream
                            InputStream in = new FileInputStream(sourceimage);

                            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }

                            in.close();

                        }
                        bos.close();

                    
                        sm = new sendMail();

                        membersData member = new membersData();
                        member = (membersData) memberBroker.getData(user.getid(), user.getId_account());

                        //Envia el correo al dueño de la cuenta
                        sm.send("accountcreated", data, user, member, String.valueOf(session.getAttribute("mainUrl")));

                        request.setAttribute("menuRoute",
                                "<a href='./superAdmin.do'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>");
                        Iterator e = broker.getList("name", "ASC");
                        ArrayList lista = new ArrayList();
                        data = new accountsData();
                        while (e.hasNext()) {
                            data = (accountsData) e.next();
                            lista.add(data);
                        }
                        request.setAttribute("list", lista);

                        thisForm.setSortOrder("DESC");

                        broker.close();
                        memberBroker.close();
                        request.setAttribute("company", company);
                        // forward to display the list
                        return (mapping.findForward("listing"));

                    } else {

                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=add"));
                    }

                } else if (action.equals(
                        "delete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado
                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    ArrayList items = broker.getItems(fields, user.getId_account());

                    request.setAttribute("fromPage", request.getParameter("fromPage"));

                    request.setAttribute("param", "");
                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("title",
                            ResourceBundle.getBundle("ApplicationResources",
                            new Locale(user.getlanguage(), "")).getString("common.Accounts"));
                    request.setAttribute("do", request.getRequestURI());
                    request.setAttribute("company", company);


                    // forward to display the list                     
                    return (mapping.findForward("confirmDelete"));

                } else if (action.equals(
                        "applyDelete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado

                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    String lista = "";
                    for (int i = 0; i < fields.length; i++) {
                        lista += fields[i] + " - ";
                    }
                    ArrayList items = broker.getItems(fields, user.getId_account());

                    // Se procede con el borrado de cada una de ellas.
                    accountsData data;

                    for (int i = 0; i < items.size(); i++) {
                        data = (accountsData) items.get(i);
                        /* BORRAR UN MEMBER IMPLICA ELIMINAR REFERENCIAS EN MULTIPLES TABLAS 
                         * El unico cuidado especial es a nivel projects y tasks en donde el admin
                         * de previo a decidido si se borran o se dejan en no asignados.
                         */
                        // Si es el usuario admin, NO se borra OJO... 
                        //if (data.getid() == 1) {
                        if ((!user.getprofile().equals("4"))) {
                            errors.add("ownerconflict", new ActionError("errors.cannotDeleteAdmin.wrong"));
                            saveErrors(request, errors);
                        } else {
                            // Se borran todas las referencias
                            System.out.println("deleting " + data.getName());
                            broker.deleteAllReferences(data);
                           
                        //}
                        }
                    }
                    if (!errors.isEmpty()) {
                        // Si hay errores se le despliegan al usuario
                        return (new ActionForward(mapping.findForward("listingWithErrors").
                                getPath() + "?operation=listing"));
                    } else {
                        // No hay errores, se redirecciona a la pagina pertinente.
                        request.setAttribute("menuRoute",
                                "<a href='./superAdmin.do'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>");
                        // Traer los valores para desplegar la lista de organizaciones disponibles.
                        // Constituye un listado completo y para ello nos valemos del broker.                                              
                        // Y por ultimo lo agregramos al contexto                 
                        Iterator e = broker.getList("name", "ASC", user.getId_account());
                        ArrayList lista2 = new ArrayList();
                        accountsData data2 = new accountsData();
                        while (e.hasNext()) {
                            data2 = (accountsData) e.next();
                            lista2.add(data2);
                        }
                        request.setAttribute("list", lista2);

                        thisForm.setSortOrder("DESC");


                        request.setAttribute("company", company);
                        return (mapping.findForward("listing"));
                    }
                } else if (action.equals(
                        "edit")) {
                    // Si se trata de un edit, se debe de cargar los datos del bean
                    // en el formulario.

                    accountsData data = (accountsData) broker.getData(thisForm.getId());

                    request.setAttribute("title",
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.editAccount"));

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);
                    Calendar now = Calendar.getInstance();
                    if ( thisForm.getFinal_trial_date()!=null){
                        
                         now.setTime(thisForm.getFinal_trial_date());
                    request.setAttribute("trial_date", "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH)+1) +
                            "-" + + now.get(Calendar.DAY_OF_MONTH));
                    }else{
                        request.setAttribute("trial_date", "");
                    }
                    
                    if (("").equals(thisForm.getShortname())||thisForm.getShortname()==null)
                        thisForm.setShortname("-");
                    thisForm.setOperation("applyEdit");

                    request.setAttribute("userId", request.getParameter("userId"));
                    request.setAttribute("fromPage", request.getParameter("fromPage"));
                    if (user.getprofile().equals("4")) // Se agrega el link para el menu con la ruta
                    {
                        request.setAttribute("menuRoute",
                                "<a href='./superAdmin.do'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>&nbsp;/&nbsp;" +
                                "<a href='./account.do?operation=listing'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Accounts") + "</a>&nbsp;/" + data.getName());
                    } else {
                        request.setAttribute("menuRoute",
                                "<a href='./admin.do'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" + data.getName());
                    }

                    request.setAttribute("company", company);
                    return (mapping.findForward("displayEditForm"));
                } else if (action.equals(
                        "applyEdit")) {


                    // Se validan los datos ingresados
                    errors = thisForm.validate(mapping, request);

                    if (errors.isEmpty()) {
                        // Se trata de la aplicacion de un update en la BD
                        accountsData data = new accountsData();
                        accountsData data2 = new accountsData();

                        PropertyUtils.copyProperties(data, thisForm);
                      
                        data2 = (accountsData) broker.getData(data.getId());

                                                //Se copia la propiedad KeyEncriptation en data2 a data
                        data.setKey_encriptation(data2.getKey_encriptation());
                        
                        // Se obtiene la referencia al loginData dentro de la session
                        // y se actualiza el template que usa
                        user = (loginData) session.getAttribute("login");
                        
                        if (data2.getShortname()!=null)
                        {
                            data.setShortname(data2.getShortname());
                        }else
                        {
                            data.setShortname("-");
                        }

                        if (!"S".equals(thisForm.getActive())) {
                            data.setActive("N");
                        }
                        if (!user.getprofile().equals("4")) {
                            data.setActive(data2.getActive());
                            data.setName(data2.getName());
                            data.setUser_fare(data2.getUser_fare());
                        }
                        
                       

                        //retrieve the file representation
                        FormFile file = thisForm.getLink();
                        if (file.getFileSize() > 0) {
                            //retrieve the file name

                            // Get the URL to the upload dir        
                            String sub = TMSConfigurator.getDownloadPath();//+separator+"ACC"+data.getId()+separator;
                          
                            // Si el string no termina con el separador de files, se agrgega
                            if (!sub.endsWith(separator)) {
                                sub = sub + separator;
                            }
                            String[] splitSub = sub.split("ACC" + user.getId_account());
                            if (splitSub.length > 1) {
                                sub = splitSub[0] + "ACC" + data.getId() + separator;
                            }
                            // Save the file                        
                            String subdir = sub;

                            String oldFileName = data2.getAccount_logo();
                            if (oldFileName!=null){
                            oldFileName = StringManipulator.generateValidFilename(oldFileName);
                          

                            String fullpath = subdir + oldFileName;

                           

                            File theFile = new File(fullpath);
                            theFile.delete();
                                }

                            String fullpath = subdir + oldFileName;

                           
                            File theFile = new File(fullpath);
                            theFile.delete();


                            String fileName = file.getFileName();
                            // Removemos cualquier caracter invalido
                            fileName = StringManipulator.generateValidFilename(fileName);



                            data.setAccount_logo(file.getFileName());
                            //retrieve the file data
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            InputStream stream = file.getInputStream();



                            fullpath = subdir + fileName;
                            theFile = new File(fullpath);

                            // Create directory for the file, if requested.
                            if (theFile.getParentFile() != null) {
                                theFile.getParentFile().mkdirs();
                            }
                            //write the file to the file specified
                            OutputStream bos = new FileOutputStream(theFile);
                            int bytesRead = 0;
                            byte[] buffer = new byte[8192];
                            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }
                            bos.close();

                            //close the stream
                            stream.close();
                        } else {
                            data.setAccount_logo(data2.getAccount_logo());
                        }
                        data.setFinal_trial_date(null);
                       if (thisForm.getFinal_trial_date().toString().compareTo("0001-01-01 12:00:00.0")==0 &&
                                (data2.getFinal_trial_date()!=null))
                       { //Convertir una cuenta de prueba a una licenciada
                            
                            Calendar cal=Calendar.getInstance();
                            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, Integer.parseInt(TMSConfigurator.getTrial()),12,0,0);
                            thisForm.setFinal_trial_date(new java.sql.Timestamp(cal.getTime().getTime()));updateUserExpiration(thisForm.getId(), thisForm.getFinal_trial_date());
                            updateUserExpiration(thisForm.getId(), thisForm.getFinal_trial_date());
                       }else{
                                if (thisForm.getFinal_trial_date().toString().compareTo("0001-01-01 12:00:00.0")!=0 &&
                                (data2.getFinal_trial_date()==null))
                                {
                                    
                                //Convertir una cuenta licenciada a una de prueba
                                    data.setFinal_trial_date(thisForm.getFinal_trial_date());
                                    updateUserExpiration(thisForm.getId(), thisForm.getFinal_trial_date());
                                }else{
                                    if (thisForm.getFinal_trial_date().toString().compareTo("0001-01-01 12:00:00.0")==0 &&
                                        (data2.getFinal_trial_date()==null))
                                    {
                                                //Editar Cuenta Licenciada 
                                            data.setFinal_trial_date(null);
                                    }else{
                                    
                                            data.setFinal_trial_date(thisForm.getFinal_trial_date());
                                            updateUserExpiration(thisForm.getId(), thisForm.getFinal_trial_date());
                                    
                                    }
                                }
                        
                       }

                         
                        // We add the new record.
                        broker.update(data);


                        Iterator e = broker.getList("name", "ASC");
                        ArrayList lista = new ArrayList();
                        data2 = new accountsData();
                        while (e.hasNext()) {
                            data2 = (accountsData) e.next();
                            lista.add(data2);
                        }
                        request.setAttribute("list", lista);

                        thisForm.setSortOrder("DESC");


                        request.setAttribute("company", company);
                        if (data.getId()== user.getId_account()){
                             session.setAttribute("systemName",data.getCreator());
                              tms = new TMSConfigurator(user);
                        }
                        if (user.getprofile().equals("4")) {
                            request.setAttribute("menuRoute",
                                    "<a href='./superAdmin.do'>" +
                                    ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>");

                            // forward to display the list
                            return (mapping.findForward("listing"));
                        } else {
                            request.setAttribute("menuRoute",
                                    "<a href='./admin.do'>" +
                                    ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>");


                            // forward to display the list
                          return (new ActionForward(mapping.findForward("view").
                                getPath() + "?operation=view&account="+data.getId()));
                        }
                    } else {
                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=edit"));
                    }
                } else if (action.equals("view")) {


                    // Si se trata de un view, se debe de cargar los datos del bean
                    // en el formulario.
                    if (request.getParameter("account") != null) {
                        thisForm.setId(Integer.parseInt(request.getParameter("account").toString()));
                    }
                    accountsData data = (accountsData) broker.getData(thisForm.getId());
                    broker.close();
                    request.setAttribute("title",
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.viewAccount"));

                    // We copy all the properties from the form to the bean.
                    PropertyUtils.copyProperties(thisForm, data);

                    thisForm.setOperation("view");

                    if (user.getprofile().equals("4")) {
                        request.setAttribute("menuRoute",
                                "<a href='./superAdmin.do'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>&nbsp;/" +
                                "<a href='./account.do?operation=listing'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Accounts") + "</a>&nbsp;/" + data.getName());
                    } else {
                        request.setAttribute("menuRoute",
                                "<a href='./admin.do'>" +
                                ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/" + data.getName());
                    }

                    request.setAttribute("company", company);
                    return (mapping.findForward("displayViewForm"));

                } else if (action.equals("sort")) {
                    // Traer los valores para desplegar la lista de Cuentas disponibles.
                    // Constituye un listado completo y para ello nos valemos del broker.                                              
                    // Y por ultimo lo agregramos al contexto
                    Iterator e = broker.getList(thisForm.getSortColumn(),
                            thisForm.getSortOrder());

                    ArrayList lista = new ArrayList();
                    accountsData data = new accountsData();
                    while (e.hasNext()) {
                        data = (accountsData) e.next();
                        lista.add(data);
                    }
                    request.setAttribute("list", lista);

                    // Negamos el tipo de ordenamiento
                    if (thisForm.getSortOrder().equalsIgnoreCase("ASC")) {
                        thisForm.setSortOrder("DESC");
                    } else {
                        thisForm.setSortOrder("ASC");
                    }

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./superAdmin.do'>" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.AccountAdministration") + "</a>");

                    // forward to display the list
                    request.setAttribute("company", company);
                    return (mapping.findForward("listing"));

                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "members.do", "perform",
                        "Fatal Error: " + e.toString());

                e.printStackTrace();

            } finally {
                // Se cierran los brokers creados
                broker.close();
                memberBroker.close();
                organizationBroker.close();
            }

        }

        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }

// Retorna la lista de organizaciones 
    /*private ArrayList getListing(String sortCol, String sortOrder, membersBroker broker) {
    Iterator e;
    // Se obtiene el iterador sobre todos los elementos de la lista.
    e = broker.getListofCompanyMembers(sortCol, sortOrder, 0);
    // Debemos convertir el iterador en un arraylist.
    ArrayList lista = new ArrayList();
    while (e.hasNext()) {
    lista.add(e.next());
    }
    return lista;
    }*/
    
    private void updateUserExpiration(int idAccount, Timestamp expirationDate)
    {
        membersBroker memberBroker= new membersBroker();
        Iterator e= memberBroker.getList(idAccount);
        membersData memberData= new membersData();
        while (e.hasNext())
        {
            memberData= (membersData)e.next();
            memberData.setExpired_date(expirationDate);
            memberBroker.update(memberData);
            
        }
        
    }
}