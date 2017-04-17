/*
 * filesAction.java
 *
 * Created on February 28, 2003, 11:33 AM
 */
package com.unify.webcenter.action.portal;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import com.unify.webcenter.tools.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.form.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.desencrypter.DesEncrypter;
import com.unify.webcenter.desencrypter.StringEncrypter;
import java.io.FileInputStream;

import java.sql.Timestamp;
import org.apache.struts.upload.FormFile;

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
 * @author Administrator 
 */
public class filesAction extends Action {

    /** Creates a new instance of calendarAction */
    public filesAction() {

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
        String separator = File.separator;

        filesBroker broker;
        accountsBroker brokerAccount;

        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {
            // forward to display the home page

            return (mapping.findForward("portalLogin"));

        } else {
            broker = new filesBroker();
            brokerAccount = new accountsBroker();
            try {
                session = request.getSession();

                loginData user = (loginData) session.getAttribute("loginPortal");
                TMSConfigurator tms= new TMSConfigurator(user);
                //String sub = tms.getDownloadPath();
                String sub= session.getAttribute("downloadPath").toString();
                // Si el string no termina con el separador de files, se agrgega
                if (!sub.endsWith(separator)) {
                    sub = sub + separator;
                }

                if (form == null) {
                    System.out.println(" Creating new filesForm bean under key " + mapping.getAttribute());
                    form = new filesForm();
                }

                filesForm thisForm = (filesForm) form;

                // fetch action from form
                action = thisForm.getOperation();

                servlet.log("[DEBUG] filesAction at perform(): Action is " + action);

                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);
                request.setAttribute("donwloadPath",sub);

                projectsBroker brokerProjects = new projectsBroker();
                request.setAttribute("projectInfo", brokerProjects.getData(
                        thisForm.getproject(), user.getId_account()));
                brokerProjects.close();

                
                System.out.println("ACTION: "+ action);
                // Determine what to do
                if (action.equals("add")) {
                    // En el caso de la operacion add, se despliega el formulario.
                    // Y ademas se envia la lista de tareas publicadas de este proyecto                                        
                    tasksBroker taskBroker = new tasksBroker();
                    Iterator e = taskBroker.getListByProjectPublishedForPortal("name",
                            "ASC",
                            thisForm.getproject(), 0, user.getId_account());
                    taskBroker.close();

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        lista.add(e.next());
                    }
                    // Se guarda la lista de files asociados a esta tarea.
                    request.setAttribute("listTasks", lista);

                    // Tambien se cargan las discusiones publicadas del caso.
                    topicsBroker topBroker = new topicsBroker();
                    e = topBroker.getListByProjectForPortal("last_post",
                            "DESC",
                            thisForm.getproject(), 0, user.getId_account(),0, "1");
                    topBroker.close();

                    // Debemos convertir el iterador en un arraylist.
                    lista = new ArrayList();
                    topicsData topicData = new topicsData();
                    while (e.hasNext()) {
                        topicData = (topicsData) e.next();
                        topicData.setlast_post(Timestamp.valueOf(topicData.convTimeZone(topicData.getlast_post(), user.getTime_zone())));
                        lista.add(topicData);
                    }
                    // Se guarda la lista de files asociados a esta tarea.
                    request.setAttribute("listTopics", lista);


                    return (mapping.findForward("add"));

                } else if (action.equals("applyAdd")) {

                    // Se trata de la aplicacion de un insert en la BD
                    filesData data = new filesData();

                    //retrieve the text data
                    String text = request.getParameter("comments");

                    //retrieve the file representation
                    FormFile file = thisForm.getTheFile();

                    //retrieve the file name

                    String fileName = file.getFileName();
                    // Removemos cualquier caracter invalido
                    fileName = StringManipulator.generateValidFilename(fileName);

                    //retrieve the content type
                    String contentType = file.getContentType();

                    // Retrieve the project id and task (if task exists)
                    int projectid = thisForm.getproject();
                    int taskid = thisForm.gettask();
                    int topicid = thisForm.gettopic();

                    // Se determina si el file esta ligado a un task o a un
                    // topic
                    String seleccion = request.getParameter("radiobutton");
                    if (seleccion.equals("TK")) {
                        topicid = 0;
                    } else {
                        taskid = 0;
                    }


                    //retrieve the file size
                    int size = (file.getFileSize());
                    String dataf = null;

                    try {
                        //retrieve the file data
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        InputStream stream = file.getInputStream();


                        // Save the information about the file in DB                            
                        data.setid(0);
                        data.setcomments(thisForm.getcomments());
                        data.setdate(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        data.settype(file.getContentType());
                        data.setname(fileName);
                        data.setowner(user.getid());
                        data.setproject(thisForm.getproject());
                        data.setpublished("1");
                        data.setsize(new java.math.BigDecimal(file.getFileSize()));
                        data.setstatus(0);
                        data.settask(taskid);
                        data.settopic(topicid);
                        data.setId_account(user.getId_account());
                        data.setupload(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                        broker.add(data);


                        // Save the file
                        fileName = data.getid() + "-" + fileName;
                        //fileName = ""+data.getid();                            
                        String subdir = sub;
                        if (projectid > 0) {
                            subdir = subdir + "PR" + projectid + separator;
                            if (taskid > 0) {
                                subdir = subdir + "TK" + taskid + separator;
                            }
                            if (topicid > 0) {
                                subdir = subdir + "TP" + topicid + separator;
                            }
                        }

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
                        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                        bos.close();

                        //close the stream
                        stream.close();
             servlet.log("[DEBUG] filesPortalAction at perform(): ENC is " + TMSConfigurator.getEnc());
                        
                        if (TMSConfigurator.getEnc()!=null && TMSConfigurator.getEnc().equalsIgnoreCase("Y")) {
                            String passPhrase = TMSConfigurator.getPassPhrase();
                            StringEncrypter desEncrypter = new StringEncrypter(passPhrase);


                            accountsData account = (accountsData) brokerAccount.getData(user.getId_account());


                            String desDecrypted = desEncrypter.decrypt(account.getKey_encriptation());
                            // Create encrypter/decrypter class
                            DesEncrypter encrypter = new DesEncrypter(desDecrypted);

                            String temp = fullpath.replace('.', '-');
                            String[] split = fullpath.split("-");
                            String fullpath2 = subdir + "temp" + split[1].toString();

                            theFile = new File(fullpath);

                            File theFile2 = new File(fullpath2);

                            // Encrypt
                            encrypter.encrypt(new FileInputStream(theFile),
                                    new FileOutputStream(theFile2));

                            //write the file to the file specified
                            bos = new FileOutputStream(theFile);
                            InputStream stream2 = new FileInputStream(theFile2);
                            bytesRead = 0;
                            buffer = new byte[8192];
                            while ((bytesRead = stream2.read(buffer, 0, 8192)) != -1) {
                                bos.write(buffer, 0, bytesRead);
                            }

                            bos.close();
                            stream2.close();
                            theFile2.delete();
                        }

                    } catch (FileNotFoundException fnfe) {
                        servlet.log("Error en fileAction: " + fnfe.toString());
                        broker.close();
                        return null;
                    } catch (IOException ioe) {
                        servlet.log("Error en fileAction: " + ioe.toString());
                        broker.close();
                        return null;
                    }

                    // Regresamos al listado de los documentos
                    return (new ActionForward(mapping.findForward("listFiles").
                            getPath() + "?operation=show&sortOrder=ASC&sortColumn=name&project=" + request.getParameter("project"), true));

                } else if (action.equals("show") || action.equals("sort")) {
                    // Se trata de un showAll de los files asociadas a un proyecto determinado.                    
                    Iterator e;
                    System.out.println(thisForm.gettask());
                    if (thisForm.gettask() == 0) {
                        e = broker.getListByProjectForPortal(thisForm.getsortColumn(),
                                thisForm.getsortOrder(),
                                thisForm.getproject(), 0, user.getId_account());
                    } else {
                        e = broker.getListByTaskForPortal(thisForm.getsortColumn(),
                                thisForm.getsortOrder(),
                                thisForm.gettask(), 0, user.getId_account());
                    }

                    tasksBroker taskbroker= new tasksBroker();
                    topicsBroker topicsbroker= new topicsBroker();

                    tasksData taskdata= new tasksData();
                    topicsData topicdata= new topicsData();
                    filesData filedata= new filesData();

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        filedata=(filesData) e.next();
                        if (filedata.gettask()>0) {
                            taskdata=(tasksData)taskbroker.getData(filedata.gettask());
                            filedata.setTaskName(taskdata.getname());
                        }else{
                            topicdata=(topicsData)topicsbroker.getData(filedata.gettopic());
                            filedata.setTopicName(taskdata.getname());
                        }
                        lista.add(filedata);
                    }
                    topicsbroker.close();
                    taskbroker.close();
                    
                    // Se guarda la lista de files asociados a esta tarea.
                    request.setAttribute("listFiles", lista);

                    if (thisForm.getOperation().equalsIgnoreCase("sort")) {
                        // Negamos el tipo de ordenamiento
                        if (thisForm.getsortOrder().equalsIgnoreCase("ASC")) {
                            thisForm.setsortOrder("DESC");
                        } else {
                            thisForm.setsortOrder("ASC");
                        }
                    }

                    return (mapping.findForward("display"));
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();
                return (mapping.findForward("display"));

            } finally {
                broker.close();
            }

            // Default if everthing else fails
            return (mapping.findForward("display"));
        }
    }
}
