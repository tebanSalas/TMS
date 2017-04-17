/*
 * homeAction.java
 *
 * Created on January 27, 2003, 2:46 PM
 */
package com.unify.webcenter.action;

import java.io.IOException;
import java.util.logging.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.form.*;
import com.unify.webcenter.conf.TMSConfigurator;
import java.sql.Timestamp;
import org.omg.PortableInterceptor.USER_EXCEPTION;

/**
 * Accion principal del Unify Task Management. Se encarga de formar la página de
 * inicio de cada usuario del sistema.
 *
 * @author  Gerardo Arroyo Arce
 */
public class homeAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

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
        tasksBroker brokerTasks;
        topicsBroker brokerTopics;
        reportsBroker brokerReports;
        membersBroker brokerMembers;
        String company = null;
        session = request.getSession(false);
            logger.logp(Level.FINEST, "TMS Login", "performAction",
                    "[DEBUG] homeAction at perform(): Action is login11" );

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page
            return (mapping.findForward("login"));

        } else {
            //Set the selected tab
            if (session.getAttribute("current") != null && session.getAttribute("current").equals("login")) {
                session.setAttribute("current", "home");
            } else if (request.getParameter("home") != null) {
                session.setAttribute("current", "home");
            } else {
                session.setAttribute("current", "project");
            }

            brokerProjects = new projectsBroker();
            brokerTasks = new tasksBroker();
            brokerTopics = new topicsBroker();
            brokerReports = new reportsBroker();
            brokerMembers = new membersBroker();
            // Ya hay una sesion establecida.
            try {

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");

           
                int accountId = 0;
                if (session.getAttribute("account") != null && user.getprofile().equals("4")) {
                    accountId = Integer.parseInt(session.getAttribute("account").toString());
                    user.setId_account(accountId);
                } else {
                    accountId = user.getId_account();
                }

                TMSConfigurator tms = new TMSConfigurator(user);

                company = tms.getCompany(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                // Se agrega el link para el menu con la ruta
                request.setAttribute("menuRoute",
                        "<a href='./home.do'>" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/&nbsp;" + user.gefullname());

                if (form == null) {
                    System.out.println(" Creating new homeForm bean under key " + mapping.getAttribute());
                    form = new homeForm();
                }

                // Se hace el casting del form.
                homeForm thisForm = (homeForm) form;


                // Primero con Proyectos
                sortProjects(request, thisForm, user.getid(), brokerProjects, brokerTasks, brokerTopics, user.getprofile(), accountId);
               // brokerProjects.close();
                // Reports
                //sortReports(request, thisForm, user.getid(), brokerReports, user.getTime_zone());
                sortReports(request, thisForm, user.getid(), brokerReports, user.getTime_zone(), accountId);
                //brokerReports.close();
                // Modify by Jury 30th march, 2004  
               // if (user.getprofile().equals("3") || user.getprofile().equals("4")) {

                //    sortMembers(request, thisForm, user.getid(), brokerMembers, user.getprofile(), accountId);
                //    brokerMembers.close();

              //  } else {


                    // Tasks.
                    sortTasks(request, thisForm, user.getid(), brokerTasks, accountId);
                  //  brokerTasks.close();

                    // Topics
                    sortTopics(request, thisForm, user.getid(), brokerTopics, user.getTime_zone(), accountId);
                    brokerTopics.close();
            //    }
                
                brokerProjects.close();
                brokerReports.close();
                brokerMembers.close();
                brokerTasks.close();
                brokerTopics.close();
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "home.do", "perform",
                        "Fatal Error: " + e.toString());
                e.printStackTrace();

            } finally {
                brokerProjects.close();
                brokerTasks.close();
                brokerTopics.close();
                brokerReports.close();
                brokerMembers.close();

            }
            request.setAttribute("company", company);
            // forward to display the home page
            return (mapping.findForward("home"));
        }
    }

    // Metodo que se encarga de ordernar los proyectos dispuestos.
    private void sortProjects(HttpServletRequest request, homeForm thisForm,
            int memberId, projectsBroker brokerProjects, tasksBroker brokerTasks,
            topicsBroker brokerTopics, String profile, int accountId) {
        // boolean ordenarPorAtrazadas = false;
        // Traer los valores para desplegar la lista de proyectos disponibles.
        // Constituye un listado completo y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto       
        int currentPage = thisForm.getpageProjects();
        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("projects")) {
            if (thisForm.getoperation().equalsIgnoreCase("sort")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageProjects(1);
            }
        }
        if (profile.equals("3") || profile.equals("4")) {
            // Si se trata del usuario admin,siempre le mostramos toda la informacion
            currentPage = 0;
        }
        // Si se quiere ordenar por atrazadas, se cambia el nombre de la columna y se 
        // activa el flag
        if (thisForm.getsortColumnProjects().equalsIgnoreCase("delayed")) {
            thisForm.setsortColumnProjects("name");
        //thisForm.setsortColumnProjects("status");
        // ordenarPorAtrazadas = true;
        }


        // Traemos la lista de todos los projectos que le pertenecen a un miembro
        // determinado.
        Iterator e;
        if (thisForm.gettypeProject() == 0) {
            e = brokerProjects.getListByMember(thisForm.getsortColumnProjects(),
                    thisForm.getsortOrderProjects(),
                    memberId, currentPage, profile, accountId);
        } else {
            e = brokerProjects.getListByMember(thisForm.getsortColumnProjects(),
                    thisForm.getsortOrderProjects(),
                    memberId, currentPage, profile, accountId, thisForm.gettypeProject());
        }


        // Posteriormente cargamos las estadisticas de rigor, pero solo se despliegan
        // si son vistas por el usuario administrador.
        ArrayList lista = new ArrayList();
        projectsData data = new projectsData();
        statisticsData statistics = new statisticsData();

        while (e.hasNext()) {
            data = (projectsData) e.next();

            // if (memberId == 1) {
            /*if (profile.equals("3") || profile.equals("4")) {
                statistics = (statisticsData) brokerTasks.getTotalTasksByProjectStatistics(data.getid());
                // Total de tareas por projecto.
                data.settotalprojecttasks(statistics.gettotaltasks());
                //  data.setstart_date(data.getstart_date().getTime());

                // Total de tareas assignadas por proyecto
                data.settotalassignedprojecttasks(statistics.gettotalassignedtasks());
                data.settotalnotassignedprojecttasks(statistics.gettotalnotassignedtasks());

                //Total de tareas a tiempo por projecto
                data.settotalontimeprojecttasks(statistics.gettotalontimetasks());
                data.settotaldelayedprojecttasks(statistics.gettotaldelayedtasks());

                // Otros totales
                data.settotalrejectedprojecttasks(statistics.gettotalrejectedtasks());
                data.settotalsuspendedprojecttasks(statistics.gettotalsuspendedtasks());
                data.settotalendedprojecttasks(statistics.gettotalendedtasks());

                // Totales de discuciones
                data.settotalTopics(brokerTopics.getTotalOpenTopicsByProject(data.getid(), accountId));
            }*/
            lista.add(data);

        }

        // Si se trata de ordenar por tareas atrazadas
       /* if (ordenarPorAtrazadas) {
        Collections.sort(lista);
        thisForm.setsortColumnProjects("delayed");
        }*/
        request.setAttribute("listProjects", lista);

        // Se setea el total de paginas
        setPages(brokerProjects, request, "listPagesProjects", currentPage);
        if (thisForm.getsource().equalsIgnoreCase("projects") &&
                thisForm.getoperation().equalsIgnoreCase("sort")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderProjects().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderProjects("DESC");
            } else {
                thisForm.setsortOrderProjects("ASC");
            }
        }
        brokerProjects.close();
    }

    // Método que se encarga de ordernar las tareas dispuestos.
    private void sortTasks(HttpServletRequest request, homeForm thisForm,
            int idMember, tasksBroker brokerTasks, int accountId) {
        // Traer los valores para desplegar la lista de tareas disponibles.
        // Constituye un listado completo y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto
        int currentPage = thisForm.getpageTasks();

        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("tasks")) {
            if (thisForm.getoperation().equalsIgnoreCase("sort")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageTasks(1);
            }
        }

        // Se extra el result set y se almacenada en el contexto
        Iterator e;
        if (thisForm.getTypeTask() == 0) {
            e = brokerTasks.getAllListByMemberFiltered(thisForm.getsortColumnTasks(),
                    thisForm.getsortOrderTasks(),
                    idMember, currentPage, accountId);
        } else {
            e = brokerTasks.getListByMemberFiltered(thisForm.getsortColumnTasks(),
                    thisForm.getsortOrderTasks(),
                    idMember, currentPage, accountId, thisForm.getTypeTask());
        }
        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }

        // Se guarda la lista de tareas en el contexto
        request.setAttribute("listTasks", lista);

        // Se setea el total de paginas
        setPages(brokerTasks, request, "listPagesTasks", currentPage);
        

        if (thisForm.getsource().equalsIgnoreCase("tasks") &&
                thisForm.getoperation().equalsIgnoreCase("sort")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderTasks().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderTasks("DESC");
            } else {
                thisForm.setsortOrderTasks("ASC");
            }
        }
        brokerTasks.close();
    }

    // Método que se encarga de ordernar las topics dispuestos.
    private void sortTopics(HttpServletRequest request, homeForm thisForm,
            int idMember, topicsBroker brokerTopics, String timeZone, int accountId) {

        // Traer los valores para desplegar la lista de topics disponibles.
        // Constituye un listado completo y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto
        int currentPage = thisForm.getpageTasks();

        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("topics")) {
            if (thisForm.getoperation().equalsIgnoreCase("sort")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageTopics(1);
            }
        }

        // Se extra el result set y se almacenada en el contexto
        Iterator e;
        e = brokerTopics.getListByMember(thisForm.getsortColumnTopics(),
                thisForm.getsortOrderTopics(),
                idMember, currentPage, accountId);

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        topicsData topicData = new topicsData();
        while (e.hasNext()) {
            topicData = (topicsData) e.next();
            topicData.setlast_post(Timestamp.valueOf(topicData.convTimeZone(topicData.getlast_post(), timeZone)));
            lista.add(topicData);
        }

        request.setAttribute("listTopics", lista);

        // Se setea el total de paginas
        setPages(brokerTopics, request, "listPagesTopics", currentPage);

        if (thisForm.getsource().equalsIgnoreCase("topics") &&
                thisForm.getoperation().equalsIgnoreCase("sort")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderTopics().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderTopics("DESC");
            } else {
                thisForm.setsortOrderTopics("ASC");
            }
        }
    }


    // Método que se encarga de ordernar las reports dispuestos.
    private void sortReports(HttpServletRequest request, homeForm thisForm, int idMember, reportsBroker brokerReports, String timeZone, int accountId) {
        // Traer los valores para desplegar la lista de reports disponibles.
        // Constituye un listado completo y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto
        // Se extra el result set y se almacenada en el contexto
        int currentPage = thisForm.getpageReports();

        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("reports")) {
            if (thisForm.getoperation().equalsIgnoreCase("sort")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageReports(1);
            }
        }

        Iterator e;
        e = brokerReports.getListByMember(thisForm.getsortColumnReports(),
                thisForm.getsortOrderReports(),
                idMember, currentPage, accountId);

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        reportsData reportData = new reportsData();
        while (e.hasNext()) {
            reportData = (reportsData) e.next();
            reportData.setcreated(Timestamp.valueOf(reportData.convTimeZone(reportData.getcreated(), timeZone)));
            reportData.setend_date_due(Timestamp.valueOf(reportData.convTimeZone(reportData.getend_date_due(), timeZone)));
            reportData.setend_date_end(Timestamp.valueOf(reportData.convTimeZone(reportData.getend_date_end(), timeZone)));
            reportData.setend_date_start(Timestamp.valueOf(reportData.convTimeZone(reportData.getend_date_start(), timeZone)));
            reportData.setstart_date_due(Timestamp.valueOf(reportData.convTimeZone(reportData.getstart_date_due(), timeZone)));
            reportData.setstart_date_end(Timestamp.valueOf(reportData.convTimeZone(reportData.getstart_date_end(), timeZone)));
            reportData.setstart_date_start(Timestamp.valueOf(reportData.convTimeZone(reportData.getstart_date_start(), timeZone)));

            lista.add(reportData);
        }

        request.setAttribute("listReports", lista);

        // Se setea el total de paginas
        setPages(brokerReports, request, "listPagesReports", currentPage);

        if (thisForm.getsource().equalsIgnoreCase("reports") &&
                thisForm.getoperation().equalsIgnoreCase("sort")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderReports().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderReports("DESC");
            } else {
                thisForm.setsortOrderReports("ASC");
            }
        }
    }

    // Metodo que se encarga de ordernar los miembros dispuestos.
    private void sortMembers(HttpServletRequest request, homeForm thisForm, int memberId, membersBroker brokerMembers, String profile, int accountId) {
        // Traer los valores para desplegar la lista de miembros disponibles.
        // Constituye un listado completo y para ello nos valemos del broker.                                              
        // Y por ultimo lo agregramos al contexto       

        int currentPage = thisForm.getpageMembers();

        // Si se trata de un sort, SIEMPRE se pasa a la pagina 1     
        if (thisForm.getsource().equalsIgnoreCase("members")) {
            if (thisForm.getoperation().equalsIgnoreCase("sort")) {
                // Es un sort. Siempre se pasa a la pagina 1
                currentPage = 1;
                thisForm.setpageMembers(1);
            }
        }

        if (profile.equals("3") || profile.equals("4")) {
            // Si se trata del usuario admin,siempre le mostramos toda la informacion
            currentPage = 0;
        }

        Iterator e = brokerMembers.getList(thisForm.getsortColumnMembers(),
                thisForm.getsortOrderMembers(), accountId);
//       Iterator e = brokerMembers.getListofCompanyMembers(thisForm.getsortColumnMembers(), 
//                                                          thisForm.getsortOrderMembers(),
//                                                          currentPage);

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        membersData data = new membersData();
        projectsBroker projBroker = new projectsBroker();
        while (e.hasNext()) {
            data = (membersData) e.next();
            data.setownercount(projBroker.getProjectCountByOwner(data.getid(), accountId));
            lista.add(data);
        }

        request.setAttribute("listMembers", lista);

        // Se setea el total de paginas
        setPages(brokerMembers, request, "listPagesMembers", currentPage);

        if (thisForm.getsource().equalsIgnoreCase("members") &&
                thisForm.getoperation().equalsIgnoreCase("sort")) {
            // Negamos el tipo de ordenamiento
            if (thisForm.getsortOrderMembers().equalsIgnoreCase("ASC")) {
                thisForm.setsortOrderMembers("DESC");
            } else {
                thisForm.setsortOrderMembers("ASC");
            }
        }

    }

    // Mètodo que retorna un ArrayList de objetos.
    private ArrayList getListing(MainBroker broker, String sortCol, String sortOrder, int accountId) {
        Iterator e;
        // Se obtiene el iterador sobre todos los elementos de la lista.
        e = broker.getList(sortCol, sortOrder, accountId);

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }

        return lista;
    }

    // Metodo que se encarga de dejar en el context una lista de todas las paginas
    // necesarias para dar cabida a los datos del ultimo comando de sql ejecutado.
    private void setPages(MainBroker broker, HttpServletRequest request,
            String listName, int page) {
        // Se guarda el total de pàginas en el contexto.
        ArrayList listaPages = new ArrayList();
        if (page!=0){
            // Se obtiene el total de registros reales de la ultima consulta.
            int max = broker.getCount().intValue();
            int totalPage = max / MainBroker.GAP_SIZE;
            if ((max % MainBroker.GAP_SIZE) > 0) {
                totalPage++;
            }

            for (int i = 1; i <= totalPage; i++) {
                listaPages.add(new Integer(i));
            }
        }else{
            listaPages.add(new Integer(1));
        }
        request.setAttribute(listName, listaPages);
        broker.close();
    }

    // Metodo que se encarga de dejar en el context una lista de todas las paginas
    // necesarias para dar cabida a los datos del ultimo comando de sql ejecutado.
    private void setPagesSchedules(MainBroker broker, HttpServletRequest request,
            String listName) {
        // Se guarda el total de pàginas en el contexto.
        ArrayList listaPages = new ArrayList();

        // Se obtiene el total de registros reales de la ultima consulta.
        int max = broker.getCount().intValue();
        int totalPage = max / (MainBroker.GAP_SIZE * 3);
        if ((max % (MainBroker.GAP_SIZE * 3)) > 0) {
            totalPage++;
        }

        for (int i = 1; i <= totalPage; i++) {
            listaPages.add(new Integer(i));
        }

        request.setAttribute(listName, listaPages);
        broker.close();
    }
}
