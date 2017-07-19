/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.action;

import com.unify.webcenter.broker.MainBroker;
import com.unify.webcenter.broker.applicationControlBroker;
import com.unify.webcenter.broker.membersBroker;
import com.unify.webcenter.broker.organizationsBroker;
import com.unify.webcenter.broker.tasksBroker;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.conf.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.form.applicationControlForm;
import com.unify.webcenter.form.applicationForm;
import com.unify.webcenter.tools.*;
import java.sql.Timestamp;

/**
 *
 * @author MARCELA QUINTERO
 */
public class applicationControlAction extends Action{

    private static Logger logger = Logger.getLogger("com.unify");

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        String action;
        HttpSession session = request.getSession(false);
        String company = null;
        organizationsBroker orgaBroker;
        applicationControlBroker appControlBroker;
        tasksBroker taskBroker;
        membersBroker memBroker;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the login page
            return (mapping.findForward("login"));
        } else {
            orgaBroker = new organizationsBroker();
            appControlBroker =  new applicationControlBroker();
            taskBroker = new tasksBroker();
            memBroker = new membersBroker();
            // Si el usuario esta logeado se le despliega el menu
            try {
                 //Set the selected tab 
                session.setAttribute("current","viewAppControl");
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                request.setAttribute("version", tms.getVersion());
                company = tms.getCompany(user);
                String op = request.getParameter("operation");
                if (form == null) {
                    System.out.println(" Creating new costsForm bean under key " + mapping.getAttribute());
                    form = new applicationControlForm();
                }
                
                ActionErrors errors = new ActionErrors();
                
                applicationControlForm thisForm = (applicationControlForm) form;
                servlet.log("[DEBUG] applicationAction at perform(): Action is " + op);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user); 

                if (user.getViewAppControl() != 1) {
                    // No es un admin, no deberia estar aqui
                    return (mapping.findForward("login"));
                } else {
                   //que hacer ??                                        
                    if(op != null && op.equals("appControl")) {
                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.managementControlApplications")+"</a>");
                    
                        request.setAttribute("company", company);
                        
                        Iterator orgas= orgaBroker.getList("NAME", "ASC", user.getId_account());
                        request.setAttribute("orgaList", orgas);
                        return (mapping.findForward("organizations"));
                        
                    }else if (op != null && op.equals("tasksXorga")){
                        // Se agrega el link para el menu con la ruta
                        request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.managementControlApplications")+"</a>");
                    
                        request.setAttribute("company", company);
                        

                         int id_organization = Integer.parseInt(request.getParameter("organization").toString());
                         
                         //Proceso de creación de  la listas de tareas que se deben replicar y aplicar en la organización seleccionada
                         //en controles ya las tareas vienen filtradas para la organización especifica, y en estado de aplicacion NA o AT
                         
                       
                         ArrayList listReplicas = appControlBroker.getAppsByVersionControl(id_organization);
                         ArrayList listPropias = appControlBroker.getAppsByOperationNumber(id_organization);
                         
//ponemos en el request la lista de las tareas que cumplen con lo establecido
                         request.setAttribute("listaReplicas", listReplicas); 
                         request.setAttribute("listaPropias", listPropias); 
                         return (mapping.findForward("displayTasksApplication"));
                         
                    }else if(op != null && op.equals("confirmApplication")){
                        
                       int id_Application = Integer.parseInt(request.getParameter("id"));
                       applicationControlData app = (applicationControlData)appControlBroker.getData(id_Application);
                       app.setId_application_user(user.getid());
                       app.setApplication_date(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                       
                        membersBroker vab =  new membersBroker();
                        membersData mem = (membersData)vab.getData(app.getId_application_user());
                        vab.close();
                        request.setAttribute("mem", mem);
                        
                       
                       
                       request.setAttribute("infoApp", app); 
                       return (mapping.findForward("displayConfirmApplication"));
                       
                       
                    }else if(op != null && op.equals("applyApplication")){
                        //obtengo el registro correspondiente a esa aplicacion
                       int id_Application = Integer.parseInt(request.getParameter("id"));
                       String description = request.getParameter("description");
                       applicationControlData app = (applicationControlData)appControlBroker.getData(id_Application);
                       
                       //obtenemos nuevamente las listas para validar que se esté aplicando en orden 
                       ArrayList propias = appControlBroker.getAppsByOperationNumber(app.getId_organization());
                       ArrayList replicas = appControlBroker.getAppsByVersionControl(app.getId_organization());
                       
                       applicationControlData primerReplica;
                       applicationControlData primerPropia ;
                       
                       //se pone en aplicado solo en 2 casos, cuando sea la primer tareade replica, o cuando no hayan replicas y sea la primer propia
                       
                        if(!replicas.isEmpty()){
                           primerReplica = (applicationControlData)replicas.get(0);
                           if(primerReplica.getId_task()==app.getId_task()){
                               app.setState(2);
                           }else{
                               app.setState(1);
                           }
                        }else{
                            if(!propias.isEmpty()){
                                primerPropia = (applicationControlData) propias.get(0);
                                if(primerPropia.getId_task() == app.getId_task() ){
                                    app.setState(2);
                                }else{
                                    app.setState(1);
                                }
                            }
                        }
                       
                       
                       //Se completan los campos restantes para la informaci{on de la tarea
                       app.setId_application_user(user.getid());
                       app.setApplication_date(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                       app.setComment(description);
                       appControlBroker.update(app);
                       
                       request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.managementControlApplications")+"</a>");
                    
                        request.setAttribute("company", company);
                       
                       ArrayList  listPropias= appControlBroker.getAppsByOperationNumber(app.getId_organization());
                       ArrayList listReplicas= appControlBroker.getAppsByVersionControl(app.getId_organization());
                       
                       request.setAttribute("listaReplicas", listReplicas); 
                       request.setAttribute("listaPropias", listPropias); 
                       return (mapping.findForward("displayTasksApplication"));
                       
//                       return (new ActionForward(mapping.findForward("displayAppControl").
//                                    getPath() + "?operation=tasksXorga" ));
                       
                    }else if(op != null && op.equals("versionControl")){
                        request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.managementVersionControl")+"</a>");
                    
                        request.setAttribute("company", company);
                        
                        Iterator i = taskBroker.getTasksVersionControl(user.getId_account());
                        request.setAttribute("tasks", i);
                        return (mapping.findForward("displayVersionControl"));
                                                
                    }else if(op != null && op.equals("applyVersionControl")){
                       request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.managementVersionControl")+"</a>");
                    
                        request.setAttribute("company", company);
                        
                        int id_task = Integer.parseInt(request.getParameter("id"));
                        tasksData task = (tasksData)taskBroker.getData(id_task);
                        request.setAttribute("task", task);
                        return (mapping.findForward("displayApplyVersionControl"));
                        
                    }else if(op != null && op.equals("registerVersionControl")){
                        int id_task = Integer.parseInt(request.getParameter("id"));
                        int versionControl = Integer.parseInt(request.getParameter("versionControl"));
                        tasksData task = (tasksData)taskBroker.getData(id_task);
                        task.setVersion_control(versionControl);
                        taskBroker.update(task);
                        
                        request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.managementVersionControl")+"</a>");
                    
                        request.setAttribute("company", company);
                        
                        Iterator i = taskBroker.getTasksVersionControl(user.getId_account());
                        request.setAttribute("tasks", i);
                        return (mapping.findForward("displayVersionControl"));
                        
                    }else if(op != null && op.equals("DisplayfilterReportApplied")){
                        
                        request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.reportAplications")+"</a>");
                    
                        request.setAttribute("company", company);
                        
                        //traemos la lista de las organizaciones
                        Iterator orgas = orgaBroker.getList("name", "ASC", user.getId_account());
                        request.setAttribute("organizations", orgas);
                        
                        //traemos los usuarios
                        Iterator users = memBroker.getListMemberApplication();
                        request.setAttribute("list_users", users);
                        
                        Calendar now = Calendar.getInstance();
                    String hoy = "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DAY_OF_MONTH);
                    // Se extrae el componente dia, mes y a?o del start_date
                    request.setAttribute("startDate", hoy);
                    request.setAttribute("endDate", hoy);
                    
                    thisForm.setPageTasks(1);
                    thisForm.setSource("");
                    return (mapping.findForward("displayfilterReportApplied"));  
                    
                    
                }else if(op != null && op.equals("executeReportApplied")){
                    if (errors.isEmpty()) {
                        request.setAttribute("menuRoute", 
                        "<a href='./AppControl.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.appControl")
                        + "</a>&nbsp;/"+ "<a>"+
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(),"")).getString("common.Permission.reportAplications")+"</a>");
                    
                        request.setAttribute("company", company);
                        
                        Timestamp startDate;
                        Timestamp endDate;
                        
                        
                        
                        if(request.getParameter("startDate").toString().equalsIgnoreCase("")){
                            startDate = null;
                        }else{
                            startDate = Timestamp.valueOf(request.getParameter("startDate") + " 00:00:00.00");
                        }                        
                        
                        if(request.getParameter("endDate").toString().equalsIgnoreCase("")){
                           endDate = null;
                        }else{
                           endDate = Timestamp.valueOf(request.getParameter("endDate") + " 23:59:00.00");
                        }
                        if(startDate!=null && endDate!=null){
                            if (startDate.after(endDate)) {
                            // La fecha final debe ser superior a la inicial 
                            errors.add("DateConflict",
                                    new ActionError("errors.task.dueDateNotsuperior"));
                            saveErrors(request, errors);
                            request.setAttribute("company", company);
                            return (new ActionForward(mapping.findForward("displayAppControl").
                                    getPath() + "?operation=DisplayfilterReportApplied"));
                            }
                        }
                        
                        
                        
                        int idOrga = Integer.parseInt(request.getParameter("id_organization"));
                        int iduser = Integer.parseInt(request.getParameter("id_application_user"));
                        
                        // los pongo de nuevo para poderlo tener cuando se hace la paginación
                        request.setAttribute("startDate", startDate);
                        request.setAttribute("endDate", endDate);
                        request.setAttribute("id_organization", idOrga+"");
                        request.setAttribute("id_application_user", iduser+"");
                        
                        
           //paginación
                        int currentPage = Integer.parseInt(request.getParameter("pageTasks"));
                        request.setAttribute("currentPage", currentPage+"");

                        Iterator e= appControlBroker.TasksAppliedReport(idOrga,iduser, startDate, endDate,currentPage);
                        //request.setAttribute("applied", e);

                        ArrayList lista = new ArrayList();
                        while (e.hasNext()) {
                            lista.add(e.next());
                        }

                        request.setAttribute("applied", lista);
                        setPages(appControlBroker, request, "listPagesApplied");
                        
                        return (mapping.findForward("executeReportApplied"));
                        
                     }else {
                        // Se detecto un error de validacion, se retorna
                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddEditError").
                                getPath() + "?operation=DisplayfilterReportApplied"));
                    }
                }else if(op != null && op.equals("returnTask")){
                    int idtask = Integer.parseInt(request.getParameter("idTask"));
                    return (new ActionForward(mapping.findForward("displayTaskView").
                                    getPath() + "?operation=view&id=" + idtask));
                    
                }
                
                    
                    
                    
                    
                    else{
                        request.setAttribute("menuRoute",
                            "<a href='./AppControl.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Permission.appControl") + "</a>");
                    request.setAttribute("company", company);
                        // Se redirecciona a la pagina de administracion
                        return (mapping.findForward("main"));
                    }
                    
                    
                    
                    
                }
                
             
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "admin.do", "perform",
                        "Fatal Error: " + e.toString());

            }finally {
                orgaBroker.close();
                appControlBroker.close();
                taskBroker.close();
                memBroker.close();
            }
        }
        
        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("main"));
    }
    
    private void setPages(MainBroker broker, HttpServletRequest request,
            String listName) {
        // Se guarda el total de pÃ ginas en el contexto.
        ArrayList listaPages = new ArrayList();

        // Se obtiene el total de registros reales de la ultima consulta.
        int max = broker.getCount().intValue();


        int totalPage = max / 100;
        if ((max % 100) > 0) {
            totalPage++;
        }

        for (int i = 1; i <= totalPage; i++) {
            listaPages.add(new Integer(i));
        }

        request.setAttribute(listName, listaPages);
    }



  
    
   
}
