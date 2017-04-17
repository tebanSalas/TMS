/*
 * homeAction.java
 *
 * Created on March 1, 2003, 1:07 PM
 */
package com.unify.webcenter.action.portal;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.form.*;

/**
 * Accion principal del Unify Task Management. Se encarga de formar la página de
 * inicio de cada usuario del sistema.
 *
 * @author  Gerardo Arroyo Arce
 */
public class homeAction extends Action {

    /** Creates a new instance of homeAction */
    public homeAction() {

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


        // Este action se encarga de procesar la pagina de inicio de cada usuario
        // para ello se basa en tomar los listing con sorts de cada action de projects,
        // tasks, topics, reports and schedules                                     
        HttpSession session;
        projectsBroker brokerProjects;
tasksBroker taskBroker;
        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("loginPortal") == null) {

            return (mapping.findForward("portalLogin"));

        } else {
            brokerProjects = new projectsBroker();
                            taskBroker = new tasksBroker();
            // Ya hay una sesion establecida.
            try {
 if (form == null) {
                        System.out.println(" Creating new tasksForm bean under key "
                                    + mapping.getAttribute());
                    form = new portalForm();
                }

                portalForm thisForm = (portalForm) form;
                
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("loginPortal");
                TMSConfigurator tms= new TMSConfigurator(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                membersBroker memBroker = new membersBroker();
                membersData data = (membersData) memBroker.getData(user.getid());

                // Se guarda en el contexto una lista de todos los proyectos
                // en los cuales este usuario es due?o o miembro del equipo.
                Iterator e = brokerProjects.getListByOrganizationForPortal("name",
                        "DESC", data.getorganization(), 0, user.getId_account());

                // Debemos convertir el iterador en un arraylist.
                ArrayList lista = new ArrayList();
                ArrayList totalTasks = new ArrayList();
                ArrayList totalOnTimeTasks = new ArrayList();

                projectsData dataTmp;

                
                boolean isMember = false;
                Vector v;
                while (e.hasNext()) {
                    dataTmp= new projectsData();
                    dataTmp = (projectsData) e.next();
                    isMember = false;
                    v = dataTmp.getdetailTeams();
                    for (int i = 0; i < v.size() && isMember == false; i++) {
                        if (((teamsData) v.get(i)).getmembers() == user.getid()) {
                            isMember = true;
                        }
                    }
                    if (isMember) {
                        // Se agrega a cada lista
                        lista.add(dataTmp);
                        // Lista de total de tareas y on time
                        totalTasks.add(Integer.valueOf("" + taskBroker.getTotalTasksPublishedByProject(dataTmp.getid(), user.getId_account())));
                        totalOnTimeTasks.add(Integer.valueOf("" + taskBroker.getTotalTasksOnTimePublishedByProject(dataTmp.getid(), user.getId_account())));
                    }
                }
                // Se cierra el broker
                // Se guarda la lista de proyectos disponibles
                request.setAttribute("listProjects", lista);
                request.setAttribute("listTotalTasks", totalTasks);
                request.setAttribute("listOnTimeTasks", totalOnTimeTasks);
              


                String accion = request.getParameter("operation");


                if (accion != null && accion.equals("change")) {
                    // Se trata de un change de proyecto
                    request.setAttribute("projectInfo", brokerProjects.getData(
                            Integer.parseInt(request.getParameter("newProject"))));
                }
                if (accion != null && accion.equals("view")) {
                    // Se trata de un change de proyecto
               
                    projectsData projectData_ = new projectsData();
                    projectData_ = (projectsData) brokerProjects.getData(
                            Integer.parseInt(request.getParameter("newProject")));

                    projectData_.settotalprojecttasks(taskBroker.getTotalTasksPublishedByOneProject(projectData_.getid(), projectData_.getId_account()));

                    projectData_.settotalontimeprojecttasks(taskBroker.getTotalTasksOnTimePublishedByProject(projectData_.getid(), projectData_.getId_account()));

                    request.setAttribute("projectInfo", projectData_);
                    
                    Iterator p = brokerProjects.getListSubProjPortal(Integer.parseInt(request.getParameter("newProject")), user.getId_account());

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList listasub = new ArrayList();
                    
                    projectsData projectData = new projectsData();
               /*-------------------------------------------------*/     
                  
                    boolean isMemberSub = false;
                Vector s;
                while (p.hasNext()) {                   
                    projectData = (projectsData) p.next();
                    isMemberSub = false;
                    v = projectData.getdetailTeams();
                    for (int i = 0; i < v.size() && isMemberSub == false; i++) {
                        if (((teamsData) v.get(i)).getmembers() == user.getid()) {
                            isMemberSub = true;
                        }
                    }
                    if (isMemberSub) {
                        // Se agrega a cada lista
                      
                        projectData.settotalprojecttasks(taskBroker.getTotalTasksPublishedByOneProject(projectData.getid(), projectData.getId_account()));

                        projectData.settotalontimeprojecttasks(taskBroker.getTotalTasksOnTimePublishedByProject(projectData.getid(), projectData.getId_account()));
                         
                        listasub.add(projectData);
                    }
                }
               
                    // Se retorna la lista de las tareas de este proyecto nada mas.
                    request.setAttribute("listSubProj", listasub);
                    request.setAttribute("listProjects", lista);


                    return (mapping.findForward("displayViewForm"));
                } else if (accion != null && accion.equals("reportFile")) {
                    // Se trata del reporte de los archivos ligados a las tareas de este proyecto

                    // Traemos todas las tareas asociadas a este proyecto que esten iniciadas y terminadas
                  
                    Iterator e2 = taskBroker.getEndedStartedListByProject(
                            Integer.parseInt(request.getParameter("id").toString()), user.getId_account());
                    taskBroker.close();

                    tasksData task;
                    ArrayList filesList = new ArrayList();
                    while (e2.hasNext()) {
                        task = (tasksData) e2.next();
                        // Se traen todos los archivos ligados
                        Vector files = task.getfilesList();
                        filesData dataFile;
                        for (int i = 0; i < files.size(); i++) {
                            dataFile = (filesData) files.get(i);

                            if (dataFile.getpublished().equalsIgnoreCase("1")) {
                                filesList.add(dataFile);
                            }
                        }


                    }

                    java.util.Collections.sort(filesList);
                    request.setAttribute("listFiles", filesList);
               
                    request.setAttribute("projectInfo", brokerProjects.getData(
                            Integer.parseInt(request.getParameter("id"))));

                    return (mapping.findForward("displayDetailFiles"));
                } else {
                        System.out.println("PROYINFO12");
             System.out.println(lista.get(0));
                 //Se guarda en el contexto el dato del proyecto que se esta viendo actualmente.
                 request.setAttribute("projectInfo", lista.get(0));
                }
             System.out.println("PROYINFO");
             System.out.println(lista.size());
            // Se define como proyecto default el primero
            request.setAttribute("project", "" +((projectsData)lista.get(0)).getid());

            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
//                e.printStackTrace();

            } finally {
                brokerProjects.close();
                taskBroker.close();
            }

            // forward to display the home page
            return (mapping.findForward("home"));
        }
    }
}
