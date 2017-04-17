/*
 * performanceAction.java
 *
 * Created on February 24, 2003, 12:44 PM
 */
package com.unify.webcenter.action;

import java.io.IOException;
import java.util.*;
import java.sql.Timestamp;
import javax.servlet.*;
import javax.servlet.http.*;

import java.math.BigDecimal;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.form.*;
import com.unify.webcenter.conf.TMSConfigurator;

/**
 * Clase que se encarga del manejo de los reportes de rendimiento
 * @author  Gerardo Arroyo
 */
public class performanceAction extends Action {

    /** Creates a new instance of performanceAction */
    public performanceAction() {

    }

    /**
     * Handle server requests
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
        projectsBroker projBroker;
        membersBroker memBroker;
        tasksBroker taskBroker;
        schedulesBroker schBroker;
        String company = null;
        tasksData data = new tasksData();

        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {
            // forward to display the home page
            return (mapping.findForward("login"));
        } else {
            projBroker = new projectsBroker();
            memBroker = new membersBroker();
            taskBroker = new tasksBroker();
            schBroker = new schedulesBroker();
            // Se encuentra logeado. 
            try {

                if (form == null) {
                    System.out.println(" Creating new performanceForm bean under key " + mapping.getAttribute());
                    form = new performanceForm();
                }
                // Se atrapa el formulario
                performanceForm thisForm = (performanceForm) form;

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                // fetch action from form
                action = ((performanceForm) form).getOperation();

                servlet.log("[DEBUG] performanceAction at perform(): Action is " + action);

                if (action.equals("view")) {
                    //Se define un Calendar.
                    Calendar now = Calendar.getInstance();
                    String hoy = "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DAY_OF_MONTH);

                    // Se guarda la fecha de hoy.
                    thisForm.setstart_date(hoy);
                    thisForm.setend_date(hoy);
                    thisForm.setOperation("execute");

                    // Se debe de extraer el proyecto que viene como parametro
                    projectsData projData = (projectsData) projBroker.getData(thisForm.getproject(), user.getId_account());
                    request.setAttribute("projectName", projData.getname());

                    // Y ademas el nombre del miembro que se esta viendo
                    membersData memData = (membersData) memBroker.getData(thisForm.getmember(), user.getId_account());
                    request.setAttribute("memberName", memData.getname());

                    // Se carga el objeto correspondiente al proyecto
                    projectsData projectMember = (projectsData) projBroker.getData(thisForm.getproject(), user.getId_account());
                    request.setAttribute("assignedProject", projectMember);

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./projects.do?operation=view&id=" +
                            projData.getid() + "'>" +
                            projData.getname() + "</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.performance"));

                    request.setAttribute("company", company);
                    // En el caso de la operacion add, se despliega el formulario.
                    return (mapping.findForward("view"));
                } else if (action.equals("viewAdmin")) {
                    //Se define un Calendar.
                    Calendar now = Calendar.getInstance();
                    String hoy = "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DAY_OF_MONTH);

                    // Se guarda la fecha de hoy.
                    thisForm.setstart_date(hoy);
                    thisForm.setend_date(hoy);
                    thisForm.setOperation("executeAdmin");

                    // Se debe de extrae el id del usuario que viene como parametro
                    int idMember = thisForm.getmember();

                    // Y ademas el nombre del miembro que se esta viendo
                    membersData memData = (membersData) memBroker.getData(idMember, user.getId_account());
                    request.setAttribute("memberName", memData.getname());

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.performance") +
                            " " + memData.getname());

                    request.setAttribute("company", company);
                    // En el caso de la operacion add, se despliega el formulario.
                    return (mapping.findForward("viewAdmin"));

                } else if (action.equals("viewAdminLoad")) {
                    //Se define un Calendar.
                    Calendar now = Calendar.getInstance();
                    String hoy = "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DAY_OF_MONTH);

                    // Se guarda la fecha de hoy.
                    thisForm.setstart_date(hoy);
                    thisForm.setend_date(hoy);
                    thisForm.setOperation("executeAdminLoad");

                    // Se debe de extrae el id del usuario que viene como parametro
                    int idMember = thisForm.getmember();

                    // Y ademas el nombre del miembro que se esta viendo
                    membersData memData = (membersData) memBroker.getData(idMember, user.getId_account());
                    request.setAttribute("memberName", memData.getname());

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.load") +
                            " " + memData.getname());

                    request.setAttribute("company", company);
                    // En el caso de la operacion add, se despliega el formulario.
                    return (mapping.findForward("viewAdminLoad"));


                } else if (action.equals("execute") || action.equals("sort")) {
                    // Se trata de la ejecucion del reporte, se procede a invocar el
                    // metodo en el broker para generar el reportte de tareas.     

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).
                            getString("header.displayStart") + "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources",
                            new Locale(user.getlanguage(), "")).getString("common.Reports.reportResultsName"));

                    // Se debe de extraer el proyecto que viene como parametro
                    projectsData projData = (projectsData) projBroker.getData(thisForm.getproject(), user.getId_account());
                    request.setAttribute("projectName", projData.getname());

                    // Y ademas el nombre del miembro que se esta viendo
                    membersData memData = (membersData) memBroker.getData(thisForm.getmember(), user.getId_account());
                    request.setAttribute("memberName", memData.getname());

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/" +
                            "<a href='./projects.do?operation=view&id=" +
                            projData.getid() + "'>" +
                            projData.getname() + "</a>&nbsp;/" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.performance"));


                    Iterator e = taskBroker.getPerformanceReport(thisForm.getproject(),
                            thisForm.getmember(),
                            thisForm.gettype(),
                            Timestamp.valueOf(thisForm.getstart_date() + " 12:00:00.00"),
                            Timestamp.valueOf(thisForm.getend_date() + " 12:00:00.00"),
                            thisForm.getsortColumn(),
                            thisForm.getsortOrder(), user.getId_account());

                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    BigDecimal sumEstimated = new BigDecimal(0);
                    BigDecimal sumReal = new BigDecimal(0);
                    BigDecimal sumPerformance = new BigDecimal(0);
                    BigDecimal sumDeficit = new BigDecimal(0);

                    /* BigDecimal totGenHour = new BigDecimal("0");
                    BigDecimal totGenMin = new BigDecimal("0");*/
                    int contador = 0;

                    BigDecimal hour = new BigDecimal(0);
                    BigDecimal min = new BigDecimal(0);

                    BigDecimal hourEstim = new BigDecimal(0);
                    BigDecimal minEstim = new BigDecimal(0);
                    boolean band = false;
                    while (e.hasNext()) {
                        data = (tasksData) e.next();
                        BigDecimal totHour = schBroker.getTotalTaskRealHours(data.getid(), user.getId_account());
                        BigDecimal totMin = schBroker.getTotalTaskRealMinutes(data.getid(), user.getId_account());

                        boolean band_min = false;
                        String[] split_min = null;
                        String replace_min = "";
                        while (!band_min) {
                            if (totMin.compareTo(new BigDecimal("59")) == 1) {
                                totMin = totMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                replace_min = totMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totHour = totHour.add(new BigDecimal(split_min[0]));
                                totMin = new BigDecimal("0." + split_min[1]);
                                totMin = totMin.multiply(new BigDecimal("60"));
                                if (totMin.toString().replace('.', ',').split(",").length > 1) {
                                    replace_min = totMin.toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    totMin = new BigDecimal(split_min[0]);
                                }
                            } else {
                                band_min = true;
                            }
                        }

                        if (totMin.toString().length() == 1) {
                            data.setTotalScheduledHours(new BigDecimal(totHour + ".0" + totMin));
                        } else {
                            data.setTotalScheduledHours(new BigDecimal(totHour + "." + totMin));
                        }

                        /*Se splitea el tiempo estimado para poder sumar los valores de los minutos                                    
                        begin 
                         */
                        System.out.println(" antes getTotalScheduledHours");
                        System.out.println(data.getTotalScheduledHours());
                        System.out.println(sumReal);
                        if (data.getTotalScheduledHours().toString().replace('.', ',').split(",").length > 1) {
                            split_min = null;
                            replace_min = data.getTotalScheduledHours().toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            hour = new BigDecimal(split_min[0].toString());
                            min = new BigDecimal(split_min[1].toString());
                            if (split_min[1].toString().length() == 1) {
                                min = min.multiply(new BigDecimal(10));
                            }
                            if (sumReal.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = sumReal.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                hourEstim = new BigDecimal(split_min[0].toString());
                                minEstim = new BigDecimal(split_min[1].toString());
                                hour = hour.add(hourEstim);
                                min = min.add(minEstim);
                                split_min = null;
                                replace_min = "";
                                band = false;
                                while (!band) {
                                    if (min.compareTo(new BigDecimal("59")) == 1) {
                                        min = min.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                        replace_min = min.toString().replace('.', ',');
                                        split_min = replace_min.split(",");
                                        hour = hour.add(new BigDecimal(split_min[0]));
                                        min = new BigDecimal("0." + split_min[1]);
                                        min = min.multiply(new BigDecimal("60"));
                                        if (min.toString().replace('.', ',').split(",").length > 1) {
                                            replace_min = min.toString().replace('.', ',');
                                            split_min = replace_min.split(",");
                                            min = new BigDecimal(split_min[0]);
                                        }
                                    } else {
                                        band = true;
                                    }
                                }
                                if (min.toString().length() == 1) {
                                    sumReal = new BigDecimal(hour.toString() + ".0" + min.toString());
                                } else {
                                    sumReal = new BigDecimal(hour.toString() + "." + min.toString());
                                }
                            } else {
                                if (min.toString().length() == 1) {
                                    sumReal = sumReal.add(new BigDecimal(hour.toString() + ".0" + min.toString()));
                                } else {
                                    sumReal = sumReal.add(new BigDecimal(hour.toString() + "." + min.toString()));
                                }
                            }

                        } else {
                            sumReal = sumReal.add(data.getTotalScheduledHours());
                        }
                        System.out.println("despues getTotalScheduledHours");
                        System.out.println(sumReal);
                        /*end*/

                        /*Se splitea el tiempo estimado para poder sumar los valores de los minutos                                    
                        begin 
                         */
                        System.out.println(" antes getestimated_time");
                        System.out.println(data.getestimated_time());
                        System.out.println(sumEstimated);
                        if (data.getestimated_time().toString().replace('.', ',').split(",").length > 1) {
                            split_min = null;
                            replace_min = data.getestimated_time().toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            hour = new BigDecimal(split_min[0].toString());
                            min = new BigDecimal(split_min[1].toString());
                            if (split_min[1].toString().length() == 1) {
                                min = min.multiply(new BigDecimal(10));
                            }
                            if (sumEstimated.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = sumEstimated.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                hourEstim = new BigDecimal(split_min[0].toString());
                                minEstim = new BigDecimal(split_min[1].toString());
                                hour = hour.add(hourEstim);
                                min = min.add(minEstim);
                                split_min = null;
                                replace_min = "";
                                band = false;
                                while (!band) {
                                    if (min.compareTo(new BigDecimal("59")) == 1) {
                                        min = min.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                        replace_min = min.toString().replace('.', ',');
                                        split_min = replace_min.split(",");
                                        hour = hour.add(new BigDecimal(split_min[0]));
                                        min = new BigDecimal("0." + split_min[1]);
                                        min = min.multiply(new BigDecimal("60"));
                                        if (min.toString().replace('.', ',').split(",").length > 1) {
                                            replace_min = min.toString().replace('.', ',');
                                            split_min = replace_min.split(",");
                                            min = new BigDecimal(split_min[0]);
                                        }
                                    } else {
                                        band = true;
                                    }
                                }
                                if (min.toString().length() == 1) {
                                    sumEstimated = new BigDecimal(hour.toString() + ".0" + min.toString());
                                } else {
                                    sumEstimated = new BigDecimal(hour.toString() + "." + min.toString());
                                }
                            } else {
                                if (min.toString().length() == 1) {
                                    sumEstimated = sumEstimated.add(new BigDecimal(hour.toString() + ".0" + min.toString()));
                                } else {
                                    sumEstimated = sumEstimated.add(new BigDecimal(hour.toString() + "." + min.toString()));
                                }
                            }

                        } else {
                            sumEstimated = sumEstimated.add(data.getestimated_time());
                        }

                        System.out.println("despues getestimated_time");
                        System.out.println(sumEstimated);
                        /*end*/

                        sumPerformance = sumPerformance.add(data.getperformance());

                        sumDeficit = sumDeficit.add(data.getdeficit());

                        lista.add(data);
                        contador++;
                    }
                    // Se regresa al contexto la lista de todas las tareas que satisfacen
                    // la consulta estipulada.
                    request.setAttribute("list_report", lista);
                    request.setAttribute("total_estimated_hours", sumEstimated.toString().replace('.', ':'));
                    request.setAttribute("total_real_hours", sumReal.toString().replace('.', ':'));


                    // Para evitar una division por 0
                    if (contador == 0) {
                        contador = 1;
                    }

                    // Promedio para el rendimiento y el deficit
                    request.setAttribute("total_performance",
                            sumPerformance.divide(new BigDecimal(contador),
                            java.math.BigDecimal.ROUND_DOWN));

                    request.setAttribute("total_deficit",
                            sumDeficit.divide(new BigDecimal(contador),
                            java.math.BigDecimal.ROUND_DOWN));


                    if (thisForm.getOperation().equalsIgnoreCase("sort")) {
                        // Negamos el tipo de ordenamiento
                        if (thisForm.getsortOrder().equalsIgnoreCase("ASC")) {
                            thisForm.setsortOrder("DESC");
                        } else {
                            thisForm.setsortOrder("ASC");
                        }
                    }


                    if (thisForm.gettype().equalsIgnoreCase("byProject")) {
                        // Si se trata de una vista por proyecto, se debe agregar un
                        // sumario.

                        Iterator iter = taskBroker.getPerformanceSummaryByMember(thisForm.getproject(),
                                Timestamp.valueOf(thisForm.getstart_date() + " 12:00:00.00"),
                                Timestamp.valueOf(thisForm.getend_date() + " 12:00:00.00"), user.getId_account());


                        // Se extrae la columna en cuestion
                        ArrayList members = new ArrayList();
                        membersData dataMember;
                        while (iter.hasNext()) {
                            Object[] arr = (Object[]) iter.next();

                            // Se extrae el nombre y el deficit
                            dataMember = (membersData) memBroker.getData(((Integer) arr[0]).intValue(), user.getId_account());
                            members.add(new performanceData(dataMember.getname(),
                                    ((BigDecimal) arr[1]).toString(),
                                    ((BigDecimal) arr[1]).intValue()));
                        }
                        request.setAttribute("list_members", members);
                    }
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("execute"));

                } else if (action.equals("executeAdmin")) {
                    // Se trata de la ejecucion del reporte generalizado para todos los
                    // proyectos ligados a este cliente
                    // Se debe de extrae el id del usuario que viene como parametro
                    int idMember = thisForm.getmember();

                    // Y ademas el nombre del miembro que se esta viendo
                    membersData memData = (membersData) memBroker.getData(idMember, user.getId_account());
                    request.setAttribute("memberName", memData.getname());
                    // Se pone el menu
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.performance") +
                            " " + memData.getname());


                    // Lista de instancias de tipo performanceDetail
                    ArrayList listProjects = new ArrayList();

                    // Se va a proceder por cada proyecto asociado a este miembro
                    Iterator iterProjects = projBroker.getListByMember("name",
                            "DESC", idMember, 0, user.getprofile(), user.getId_account());

                    projectsData projData = new projectsData();

                    BigDecimal sumGenEstimated = new BigDecimal(0);
                    BigDecimal sumGenReal = new BigDecimal(0);
                    BigDecimal sumGenEstimated_transit = new BigDecimal(0);
                    BigDecimal sumGenReal_transit = new BigDecimal(0);

                    BigDecimal sumGenDif = new BigDecimal(0);
                    BigDecimal sumGenDif_transit = new BigDecimal(0);

                    BigDecimal totGenHour = new BigDecimal("0");
                    BigDecimal totGenMin = new BigDecimal("0");

                    // Contadores generales de las tareas de acuerdo a su estado
                    long totalTareasFinalizadas = 0, totalTareasRechazadas = 0, totalTareasAtrazadas = 0;

                    while (iterProjects.hasNext()) {
                            System.out.println("entro al iterproject");
                      
                        projData = new projectsData();
                        projData = (projectsData) iterProjects.next();
                        ArrayList lista = taskBroker.getCompletePerformanceReport(projData.getid(),
                                idMember,
                                Timestamp.valueOf(thisForm.getstart_date() + " 12:00:00.00"),
                                Timestamp.valueOf(thisForm.getend_date() + " 12:00:00.00"),
                                "name",
                                "ASC", user.getId_account());
                        // Variables de control de tiempos finalizados y en transito
                        BigDecimal sumEstimated = new BigDecimal(0);
                        BigDecimal sumReal = new BigDecimal(0);
                        BigDecimal sumPerformance = new BigDecimal(0);
                        BigDecimal sumDeficit = new BigDecimal(0);

                        BigDecimal sumEstimated_transit = new BigDecimal(0);
                        BigDecimal sumReal_transit = new BigDecimal(0);
                        BigDecimal sumPerformance_transit = new BigDecimal(0);
                        BigDecimal sumDeficit_transit = new BigDecimal(0);
                        //////////////////////
                        BigDecimal totHour = new BigDecimal("0");
                        BigDecimal totMin = new BigDecimal("0");
                        /////////////////////
                        int contadorFinalizadas = 0, contadorTransito = 0;
                        Iterator e = lista.iterator();
                        BigDecimal hour = new BigDecimal(0);
                        BigDecimal min = new BigDecimal(0);

                        BigDecimal hourEstim = new BigDecimal(0);
                        BigDecimal minEstim = new BigDecimal(0);
                        boolean band = false;
                        while (e.hasNext()) {
                                System.out.println("entro al e");
                      
                            data = new tasksData();
                            data = (tasksData) e.next();
                            // Se lleva el contador de las tareas en transito
                            if (data.getstatus() != 0 && data.getstatus() != 1) {
                                contadorTransito++;
                                sumPerformance_transit = sumPerformance_transit.add(data.getPerformanceUsingSchedule());
                                //sumEstimated_transit = sumEstimated_transit.add(data.getestimated_time());
                                 /*Se splitea el tiempo estimado para poder sumar los valores de los minutos                                    
                                begin 
                                 */
                                System.out.println(" antes getestimated_time transit");
                                System.out.println(data.getestimated_time());
                                System.out.println(sumEstimated_transit);
                                if (data.getestimated_time().toString().replace('.', ',').split(",").length > 1) {
                                    String[] split_min = null;
                                    String replace_min = data.getestimated_time().toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    hour = new BigDecimal(split_min[0].toString());
                                    min = new BigDecimal(split_min[1].toString());
                                    if (split_min[1].toString().length() == 1) {
                                        min = min.multiply(new BigDecimal(10));
                                    }
                                    if (sumEstimated_transit.toString().replace('.', ',').split(",").length > 1) {
                                        replace_min = sumEstimated_transit.toString().replace('.', ',');
                                        split_min = replace_min.split(",");
                                        hourEstim = new BigDecimal(split_min[0].toString());
                                        minEstim = new BigDecimal(split_min[1].toString());
                                        hour = hour.add(hourEstim);
                                        min = min.add(minEstim);
                                        split_min = null;
                                        replace_min = "";
                                        band = false;
                                        while (!band) {
                                            if (min.compareTo(new BigDecimal("59")) == 1) {
                                                min = min.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                                replace_min = min.toString().replace('.', ',');
                                                split_min = replace_min.split(",");
                                                hour = hour.add(new BigDecimal(split_min[0]));
                                                min = new BigDecimal("0." + split_min[1]);
                                                min = min.multiply(new BigDecimal("60"));
                                                if (min.toString().replace('.', ',').split(",").length > 1) {
                                                    replace_min = min.toString().replace('.', ',');
                                                    split_min = replace_min.split(",");
                                                    min = new BigDecimal(split_min[0]);
                                                }
                                            } else {
                                                band = true;
                                            }
                                        }
                                        if (min.toString().length() == 1) {

                                            sumEstimated_transit = new BigDecimal(hour.toString() + ".0" + min.toString());
                                        } else {
                                            sumEstimated_transit = new BigDecimal(hour.toString() + "." + min.toString());
                                        }
                                    } else {
                                        if (min.toString().length() == 1) {
                                            sumEstimated_transit = sumEstimated_transit.add(new BigDecimal(hour.toString() + ".0" + min.toString()));
                                        } else {
                                            sumEstimated_transit = sumEstimated_transit.add(new BigDecimal(hour.toString() + "." + min.toString()));
                                        }
                                    }
                                } else {
                                    sumEstimated_transit = sumEstimated_transit.add(data.getestimated_time());
                                }

                                System.out.println("despues getestimated_time transit");
                                System.out.println(sumEstimated_transit);
                                /*end*/

                                String formatTotalScheduleHour = data.formatRealHour(data.getTotalScheduledHours());
                                String[] splitRealtransit = formatTotalScheduleHour.split(":");
                                if (splitRealtransit.length > 1) {
                                    totHour = totHour.add(new BigDecimal(splitRealtransit[0].toString()));
                                    totMin = totMin.add(new BigDecimal(splitRealtransit[1].toString()));
                                } else {
                                    totHour = totHour.add(new BigDecimal(formatTotalScheduleHour));
                                }
                                band = false;
                                String[] split_min = null;
                                String replace_min = "";
                                while (!band) {
                                    if (totMin.compareTo(new BigDecimal("59")) == 1) {
                                        totMin = totMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                        replace_min = totMin.toString().replace('.', ',');
                                        split_min = replace_min.split(",");
                                        totHour = totHour.add(new BigDecimal(split_min[0]));
                                        totMin = new BigDecimal("0." + split_min[1]);
                                        totMin = totMin.multiply(new BigDecimal("60"));
                                        if (totMin.toString().replace('.', ',').split(",").length > 1) {
                                            replace_min = totMin.toString().replace('.', ',');
                                            split_min = replace_min.split(",");
                                            totMin = new BigDecimal(split_min[0]);
                                        }
                                    } else {
                                        band = true;
                                    }
                                }
                              System.out.println("sumRealTransit***********");    
                              System.out.println(sumReal_transit);
                              System.out.println(totHour);
                              System.out.println(totMin);
                                if (totMin.toString().length() == 1) {
                                    sumReal_transit = sumReal_transit.add(new BigDecimal(totHour.toString() + ".0" + totMin.toString()));
                                } else {
                                    sumReal_transit = sumReal_transit.add(new BigDecimal(totHour.toString() + "." + totMin.toString()));
                                }
                               System.out.println(sumReal_transit);
                                sumDeficit_transit = sumDeficit_transit.add(data.getdeficit());
                                sumGenDif_transit = sumGenDif_transit.add(data.getVariance2());
                            } else {
                                /*Se splitea el tiempo estimado para poder sumar los valores de los minutos                                    
                                begin 
                                 */
                                System.out.println(" antes getestimated_time");
                                System.out.println(data.getestimated_time());
                                System.out.println(sumEstimated);
                                if (data.getestimated_time().toString().replace('.', ',').split(",").length > 1) {
                                    String[] split_min = null;
                                    String replace_min = data.getestimated_time().toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    hour = new BigDecimal(split_min[0].toString());
                                    min = new BigDecimal(split_min[1].toString());
                                    if (split_min[1].toString().length() == 1) {
                                        min = min.multiply(new BigDecimal(10));
                                    }
                                    if (sumEstimated.toString().replace('.', ',').split(",").length > 1) {
                                        replace_min = sumEstimated.toString().replace('.', ',');
                                        split_min = replace_min.split(",");
                                        hourEstim = new BigDecimal(split_min[0].toString());
                                        minEstim = new BigDecimal(split_min[1].toString());
                                        hour = hour.add(hourEstim);
                                        min = min.add(minEstim);
                                        split_min = null;
                                        replace_min = "";
                                        band = false;
                                        while (!band) {
                                            if (min.compareTo(new BigDecimal("59")) == 1) {
                                                min = min.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                                replace_min = min.toString().replace('.', ',');
                                                split_min = replace_min.split(",");
                                                hour = hour.add(new BigDecimal(split_min[0]));
                                                min = new BigDecimal("0." + split_min[1]);
                                                min = min.multiply(new BigDecimal("60"));
                                                if (min.toString().replace('.', ',').split(",").length > 1) {
                                                    replace_min = min.toString().replace('.', ',');
                                                    split_min = replace_min.split(",");
                                                    min = new BigDecimal(split_min[0]);
                                                }
                                            } else {
                                                band = true;
                                            }
                                        }
                                        if (min.toString().length() == 1) {
                                            sumEstimated = new BigDecimal(hour.toString() + ".0" + min.toString());
                                        } else {
                                            sumEstimated = new BigDecimal(hour.toString() + "." + min.toString());
                                        }
                                    } else {
                                        if (min.toString().length() == 1) {
                                            sumEstimated = sumEstimated.add(new BigDecimal(hour.toString() + ".0" + min.toString()));
                                        } else {
                                            sumEstimated = sumEstimated.add(new BigDecimal(hour.toString() + "." + min.toString()));
                                        }
                                    }
                                } else {
                                    sumEstimated = sumEstimated.add(data.getestimated_time());
                                }

                                System.out.println("despues getestimated_time");
                                System.out.println(sumEstimated);
                                /*end*/

                                sumReal = sumReal.add(data.getTotalScheduledHours());
                                sumDeficit = sumDeficit.add(data.getdeficit());
                                sumPerformance = sumPerformance.add(data.getPerformanceUsingSchedule());
                                sumGenDif = sumGenDif.add(data.getVariance2());

                                contadorFinalizadas++;
                            }

                            // Sumamos los contadores generales de tareas.
                            if (data.getstatus() == 1) {
                                totalTareasFinalizadas++;
                            }
                            if (data.getstatus() == 12) {
                                totalTareasRechazadas++;
                            }
                            if (data.isLate()) {
                                totalTareasAtrazadas++;
                            }
                        }

                        /*Formatea sumEstimated y suma a las horas por si los minutos 
                         * son mas de 60                         
                         */
                        System.out.println("antes de formatear sum Estimated");
                        System.out.println(sumEstimated);

                        totGenHour = new BigDecimal("0");
                        totGenMin = new BigDecimal("0");
                        String formatSumReal = formatHour(sumEstimated);
                        String[] splitSumReal = formatSumReal.split(":");
                        if (splitSumReal.length > 1) {
                            totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                            totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                        } else {
                            totGenHour = totGenHour.add(new BigDecimal(formatSumReal));
                        }
                        band = false;
                        String[] split_min = null;
                        String replace_min = "";
                        while (!band) {
                            if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                                totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                                totGenMin = new BigDecimal("0." + split_min[1]);
                                totGenMin = totGenMin.multiply(new BigDecimal("60"));
                                if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                    replace_min = totGenMin.toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    totGenMin = new BigDecimal(split_min[0]);
                                }
                            } else {
                                band = true;
                            }
                        }
                        if (totGenMin.toString().length() == 1) {
                            sumEstimated = new BigDecimal(totGenHour + ".0" + totGenMin);
                        } else {
                            sumEstimated = new BigDecimal(totGenHour + "." + totGenMin);
                        }

                        System.out.println("despues de formatear sum Estimated");
                        System.out.println(sumEstimated);

                        // Sumatorias Generales de Estimado y Real
                        sumGenEstimated = sumGenEstimated.add(sumEstimated);

                        /*Formatea sumReal y suma a las horas por si los minutos 
                         * son mas de 60                         
                         */
                        System.out.println("antes de formatear sum real");
                        System.out.println(sumReal);

                        totGenHour = new BigDecimal("0");
                        totGenMin = new BigDecimal("0");
                        formatSumReal = formatHour(sumReal);
                        splitSumReal = formatSumReal.split(":");
                        if (splitSumReal.length > 1) {
                            totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                            totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                        } else {
                            totGenHour = totGenHour.add(new BigDecimal(formatSumReal));
                        }
                        band = false;
                        split_min = null;
                        replace_min = "";
                        while (!band) {
                            if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                                totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                                totGenMin = new BigDecimal("0." + split_min[1]);
                                totGenMin = totGenMin.multiply(new BigDecimal("60"));
                                if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                    replace_min = totGenMin.toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    totGenMin = new BigDecimal(split_min[0]);
                                }
                            } else {
                                band = true;
                            }
                        }
                        if (totGenMin.toString().length() == 1) {
                            sumReal = new BigDecimal(totGenHour + ".0" + totGenMin);
                        } else {
                            sumReal = new BigDecimal(totGenHour + "." + totGenMin);
                        }

                        System.out.println("despues de formatear sum real");
                        System.out.println(sumReal);

                        // Sumatorias Generales de Real
                        sumGenReal = sumGenReal.add(sumReal);

                        /*Formatea sumEstimated_transit y suma a las horas por si los minutos 
                         * son mas de 60                         
                         */
                        System.out.println("antes de formatear sum estimated transit");
                        System.out.println(sumEstimated_transit);

                        totGenHour = new BigDecimal("0");
                        totGenMin = new BigDecimal("0");
                        formatSumReal = formatHour(sumEstimated_transit);
                        splitSumReal = formatSumReal.split(":");
                        if (splitSumReal.length > 1) {
                            totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                            totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                        } else {
                            totGenHour = totGenHour.add(new BigDecimal(formatSumReal));
                        }
                        band = false;
                        split_min = null;
                        replace_min = "";
                        while (!band) {
                            if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                                totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                                totGenMin = new BigDecimal("0." + split_min[1]);
                                totGenMin = totGenMin.multiply(new BigDecimal("60"));
                                if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                    replace_min = totGenMin.toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    totGenMin = new BigDecimal(split_min[0]);
                                }
                            } else {
                                band = true;
                            }
                        }
                        if (totGenMin.toString().length() == 1) {
                            sumEstimated_transit = new BigDecimal(totGenHour + ".0" + totGenMin);
                        } else {
                            sumEstimated_transit = new BigDecimal(totGenHour + "." + totGenMin);
                        }
                        System.out.println("despues de formatear sum estimated transit");
                        System.out.println(sumEstimated_transit);

                        // Sumatorias Generales de Estimado
                        sumGenEstimated_transit = sumGenEstimated_transit.add(sumEstimated_transit);

                        /*Formatea sumReal_transit y suma a las horas por si los minutos 
                         * son mas de 60                         
                         */
                        System.out.println("antes de formatear sum real transit");
                        System.out.println(sumReal_transit);

                        totGenHour = new BigDecimal("0");
                        totGenMin = new BigDecimal("0");
                        formatSumReal = formatHour(sumReal_transit);
                        splitSumReal = formatSumReal.split(":");
                        if (splitSumReal.length > 1) {
                            totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                            totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                        } else {
                            totGenHour = totGenHour.add(new BigDecimal(formatSumReal));
                        }
                        band = false;
                        split_min = null;
                        replace_min = "";
                        while (!band) {
                            if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                                totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                                totGenMin = new BigDecimal("0." + split_min[1]);
                                totGenMin = totGenMin.multiply(new BigDecimal("60"));
                                if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                    replace_min = totGenMin.toString().replace('.', ',');
                                    split_min = replace_min.split(",");
                                    totGenMin = new BigDecimal(split_min[0]);
                                }
                            } else {
                                band = true;
                            }
                        }
                        if (totGenMin.toString().length() == 1) {
                            sumReal_transit = new BigDecimal(totGenHour + ".0" + totGenMin);
                        } else {
                            sumReal_transit = new BigDecimal(totGenHour + "." + totGenMin);
                        }

                        System.out.println("despues de formatear sum real transit");
                        System.out.println(sumReal_transit);

                        // Sumatorias Generales de Transit
                        sumGenReal_transit = sumGenReal_transit.add(sumReal_transit);

                        // Para evitar una division por 0
                        if (contadorTransito == 0) {
                            contadorTransito = 1;
                        }
                        if (contadorFinalizadas == 0) {
                            contadorFinalizadas = 1;
                        }
                         System.out.println("Lista");
                        System.out.println(lista.size());
                         System.out.println("contador Finalizadas");
                        System.out.println(contadorFinalizadas);
                        if (lista.size() > 0) {
                           
                            // Se agrega este proyecto a la lista de proyectos, siempre
                            // y cuando tenga minimo una tarea
                            listProjects.add(new performanceDetail(lista,
                                    sumEstimated, sumReal, sumPerformance.divide(new BigDecimal(contadorFinalizadas),
                                    java.math.BigDecimal.ROUND_DOWN),
                                    sumDeficit.divide(new BigDecimal(contadorFinalizadas),
                                    java.math.BigDecimal.ROUND_DOWN),
                                    projData.getname(),
                                    sumEstimated_transit, sumReal_transit,
                                    sumPerformance_transit.divide(new BigDecimal(contadorFinalizadas),
                                    java.math.BigDecimal.ROUND_DOWN),
                                    sumDeficit_transit.divide(new BigDecimal(contadorFinalizadas),
                                    java.math.BigDecimal.ROUND_DOWN)));

                        }
                          System.out.println("va otra vez");
                      
                    }

                    String sumGenReal_ = null;
                    if (sumGenReal.toString().length() == 1) {
                        sumGenReal_ = sumGenReal.toString() + ".00";
                    } else {
                        sumGenReal_ = sumGenReal.toString();
                    }

                    sumGenReal_ = sumGenReal_.toString().replace('.', ':');

                    /*Formatea sumGenReal_transit y suma a las horas por si los minutos 
                     * son mas de 60                         
                     */
                    System.out.println("antes de formatear sum Gen Real_");
                    System.out.println(sumGenReal_);

                    totGenHour = new BigDecimal("0");
                    totGenMin = new BigDecimal("0");

                    String[] splitSumReal = sumGenReal_.split(":");

                    if (splitSumReal.length > 1) {
                        totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                        totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                    } else {
                        totGenHour = totGenHour.add(new BigDecimal(sumGenReal_));
                    }
                    boolean band = false;
                    String[] split_min = null;
                    String replace_min = "";
                    while (!band) {
                        if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                            totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totGenMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                            totGenMin = new BigDecimal("0." + split_min[1]);
                            totGenMin = totGenMin.multiply(new BigDecimal("60"));
                            if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }
                    if (totGenMin.toString().length() == 1) {
                        sumGenReal_ = totGenHour.toString() + ":0" + totGenMin.toString();
                    } else {
                        sumGenReal_ = totGenHour.toString() + ":" + totGenMin.toString();
                    }

                    System.out.println("despues de formatear sum Gen Real_");
                    System.out.println(sumGenReal_);

                    String sumGenReal_transit_ = null;
                    /*Formatea sumGenReal_transit y suma a las horas por si los minutos 
                     * son mas de 60                         
                     */
                    System.out.println("antes de formatear sumGenReal_transit_");
                    System.out.println(sumGenReal_transit_);

                    totGenHour = new BigDecimal("0");
                    totGenMin = new BigDecimal("0");
                    if (sumGenReal_transit.toString().length() == 1) {
                        sumGenReal_transit_ = sumGenReal_transit.toString() + ".00";
                    } else {
                        sumGenReal_transit_ = sumGenReal_transit.toString();
                    }
                    sumGenReal_transit_ = sumGenReal_transit_.toString().replace('.', ':');//projData.formatRealHour(sumGenReal_transit);

                    splitSumReal = null;
                    splitSumReal = sumGenReal_transit_.split(":");

                    if (splitSumReal.length > 0) {
                        totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                        totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                    } else {
                        totGenHour = totGenHour.add(new BigDecimal(sumGenReal_transit_));
                    }
                    band = false;
                    split_min = null;
                    replace_min = "";
                    while (!band) {
                        if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                            totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totGenMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                            totGenMin = new BigDecimal("0." + split_min[1]);
                            totGenMin = totGenMin.multiply(new BigDecimal("60"));
                            if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }
                    if (totGenMin.toString().length() == 1) {
                        sumGenReal_transit_ = totGenHour.toString() + ":0" + totGenMin.toString();
                    } else {
                        sumGenReal_transit_ = totGenHour.toString() + ":" + totGenMin.toString();
                    }

                    System.out.println("despues de formatear sumGenReal_transit_");
                    System.out.println(sumGenReal_transit_);

                    String sumGenEstimated_ = null;

                    /*Formatea sumGenReal_transit y suma a las horas por si los minutos 
                     * son mas de 60                         
                     */
                    System.out.println("antes de formatear sumGenEstimated_");
                    System.out.println(sumGenEstimated_);

                    totGenHour = new BigDecimal("0");
                    totGenMin = new BigDecimal("0");
                    if (sumGenEstimated.toString().length() == 1) {
                        sumGenEstimated_ = sumGenEstimated.toString() + ".00";//projData.formatRealHour(sumGenReal);
                    } else {
                        sumGenEstimated_ = sumGenEstimated.toString();
                    }
                    sumGenEstimated_ = sumGenEstimated_.toString().replace('.', ':');

                    totGenHour = new BigDecimal("0");
                    totGenMin = new BigDecimal("0");

                    splitSumReal = sumGenEstimated_.split(":");
                    if (splitSumReal.length > 0) {
                        totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                        totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                    } else {
                        totGenHour = totGenHour.add(new BigDecimal(sumGenEstimated_));
                    }
                    band = false;
                    split_min = null;
                    replace_min = "";
                    while (!band) {
                        if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                            totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totGenMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                            totGenMin = new BigDecimal("0." + split_min[1]);
                            totGenMin = totGenMin.multiply(new BigDecimal("60"));
                            if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }
                    if (totGenMin.toString().length() == 1) {
                        sumGenEstimated_ = totGenHour.toString() + ":0" + totGenMin.toString();
                    } else {
                        sumGenEstimated_ = totGenHour.toString() + ":" + totGenMin.toString();
                    }
                    System.out.println("despues de formatear sumGenEstimated_");
                    System.out.println(sumGenEstimated_);

                    String sumGenEstimated_transit_ = null;
                    /*Formatea sumGenReal_transit y suma a las horas por si los minutos 
                     * son mas de 60                         
                     */
                    System.out.println("antes de formatear sumGenEstimated_transit_");
                    System.out.println(sumGenEstimated_);

                    totGenHour = new BigDecimal("0");
                    totGenMin = new BigDecimal("0");
                    if (sumGenEstimated_transit.toString().length() == 1) {
                        sumGenEstimated_transit_ = sumGenEstimated_transit.toString() + ".00";
                    } else {
                        sumGenEstimated_transit_ = sumGenEstimated_transit.toString();
                    }
                    sumGenEstimated_transit_ = sumGenEstimated_transit_.toString().replace('.', ':');//projData.formatRealHour(sumGenReal_transit);
                    splitSumReal = null;
                    splitSumReal = sumGenEstimated_transit_.split(":");

                    if (splitSumReal.length > 0) {
                        totGenHour = totGenHour.add(new BigDecimal(splitSumReal[0].toString()));
                        totGenMin = totGenMin.add(new BigDecimal(splitSumReal[1].toString()));
                    } else {
                        totGenHour = totGenHour.add(new BigDecimal(sumGenEstimated_transit_));
                    }
                    band = false;
                    split_min = null;
                    replace_min = "";
                    while (!band) {
                        if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                            totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totGenMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                            totGenMin = new BigDecimal("0." + split_min[1]);
                            totGenMin = totGenMin.multiply(new BigDecimal("60"));
                            if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }

                    if (totGenMin.toString().length() == 1) {
                        sumGenEstimated_transit_ = totGenHour.toString() + ":0" + totGenMin.toString();
                    } else {
                        sumGenEstimated_transit_ = totGenHour.toString() + ":" + totGenMin.toString();
                    }

                    System.out.println("despues de formatear sumGenEstimated_transit_");
                    System.out.println(sumGenEstimated_transit_);

                    /*Formatea sumGenDif y suma a las horas por si los minutos 
                     * son mas de 60                         
                     */
                    System.out.println("antes de formatear sum Gen Dif");
                    System.out.println(sumGenDif);

                    String sumGenDif_ = null;
                    totGenHour = new BigDecimal("0");
                    String format = "";

                    String[] split_val = null;

                    if (sumGenDif.toString().startsWith("-")) {
                        split_val = sumGenDif.toString().split("-");
                        sumGenDif = new BigDecimal(split_val[1].toString());
                        format = "-";
                    }

                    totGenMin = sumGenDif;

                    band = false;
                    split_min = null;
                    replace_min = "";
                    while (!band) {
                        if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                            totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totGenMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                            totGenMin = new BigDecimal("0." + split_min[1]);
                            totGenMin = totGenMin.multiply(new BigDecimal("60"));
                            if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }
                    if (totGenMin.toString().length() == 1) {
                        sumGenDif_ = format + totGenHour + ":0" + totGenMin;
                    } else {
                        sumGenDif_ = format + totGenHour + ":" + totGenMin;
                    }

                    System.out.println("despues de formatear sum Gen Dif");
                    System.out.println(sumGenDif);

                    /*Formatea sumGenDif_transit y suma a las horas por si los minutos 
                     * son mas de 60                         
                     */
                    System.out.println("antes de formatear sum Gen Dif transit");
                    System.out.println(sumGenDif_transit);

                    format = "";

                    totGenHour = new BigDecimal("0");

                    String sumGenDif_transit_ = null;

                    split_val = null;
                    if (sumGenDif_transit.toString().startsWith("-")) {
                        split_val = sumGenDif_transit.toString().split("-");
                        sumGenDif_transit = new BigDecimal(split_val[1].toString());
                        format = "-";
                    }

                    totGenMin = sumGenDif_transit;

                    band = false;
                    split_min = null;
                    replace_min = "";
                    while (!band) {
                        if (totGenMin.compareTo(new BigDecimal("59")) == 1) {
                            totGenMin = totGenMin.divide(new BigDecimal(60), 2, BigDecimal.ROUND_UP);
                            replace_min = totGenMin.toString().replace('.', ',');
                            split_min = replace_min.split(",");
                            totGenHour = totGenHour.add(new BigDecimal(split_min[0]));
                            totGenMin = new BigDecimal("0." + split_min[1]);
                            totGenMin = totGenMin.multiply(new BigDecimal("60"));
                            if (totGenMin.toString().replace('.', ',').split(",").length > 1) {
                                replace_min = totGenMin.toString().replace('.', ',');
                                split_min = replace_min.split(",");
                                totGenMin = new BigDecimal(split_min[0]);
                            }
                        } else {
                            band = true;
                        }
                    }
                    if (totGenMin.toString().length() == 1) {
                        sumGenDif_transit_ = format + totGenHour + ":0" + totGenMin;
                    } else {
                        sumGenDif_transit_ = format + totGenHour + ":" + totGenMin;
                    }

                    System.out.println("despues de formatear sum Gen Dif transit");
                    System.out.println(sumGenDif_transit);

                    // Variables de ambiente
                    request.setAttribute("total_estimated_hours", sumGenEstimated_);
                    request.setAttribute("total_real_hours", sumGenReal_);
                    request.setAttribute("total_diferencia", "" + sumGenDif_);

                    request.setAttribute("total_estimated_hours_transit", sumGenEstimated_transit_);
                    request.setAttribute("total_real_hours_transit", sumGenReal_transit_);
                    request.setAttribute("total_diferencia_transit", "" + sumGenDif_transit_);

                    request.setAttribute("list_projects", listProjects);

                    // Contadores generales de tareas
                    request.setAttribute("totalTareasFinalizadas", "" + totalTareasFinalizadas);
                    request.setAttribute("totalTareasRechazadas", "" + totalTareasRechazadas);
                    request.setAttribute("totalTareasAtrazadas", "" + totalTareasAtrazadas);
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("executeAdmin"));

                } else if (action.equals("executeAdminLoad")) {
                    // Se trata de la ejecucion del reporte generalizado para todos los
                    // proyectos ligados a este cliente


                    // Se debe de extrae el id del usuario que viene como parametro
                    int idMember = thisForm.getmember();

                    // Y ademas el nombre del miembro que se esta viendo
                    membersData memData = (membersData) memBroker.getData(idMember, user.getId_account());
                    request.setAttribute("memberName", memData.getname());

                    // Se pone el menu
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.load") +
                            " " + memData.getname());

                    calendarBroker calBroker = new calendarBroker();
                    ArrayList listado = calBroker.getLoadByUser(idMember,
                            Timestamp.valueOf(thisForm.getstart_date() + " 12:00:00.00"),
                            Timestamp.valueOf(thisForm.getend_date() + " 12:00:00.00"), user.getId_account());
                    calBroker.close();

                    request.setAttribute("list_projects", listado);

                    BigDecimal sumEstimated = new BigDecimal(0);
                    BigDecimal sumReal = new BigDecimal(0);
                    BigDecimal sumVariance = new BigDecimal(0);

                    // Se procesan todos los registros para sacar el sum
                    performanceDetail perfData;
                    int totalTareas = 0, totalDelayed = 0;
                    int performance = 0;
                    ArrayList tareas;
                    for (int i = 0; i < listado.size(); i++) {
                        perfData = (performanceDetail) listado.get(i);
                        try {

                            tareas = perfData.getlistTasks();
                            totalTareas += tareas.size();

                            // Contamos cuantas tareas estan tarde
                            for (int y = 0; y < tareas.size(); y++) {
                                if (((tasksData) tareas.get(y)).isLate()) {
                                    totalDelayed++;
                                }
                            }
                            sumEstimated = sumEstimated.add(perfData.gettotal_estimated_hours());
                            sumReal = sumReal.add(perfData.gettotal_real_hours());
                            sumVariance = sumVariance.add(perfData.gettotal_real_hours().subtract(perfData.gettotal_estimated_hours()));
                        } catch (Exception eIn) {
                            System.out.println(eIn.toString());
                        }
                    }

                    // La funcion de desempeno es total tareas - atrazadas/total tareas  * 100
                    performance = ((totalTareas - totalDelayed) / (totalTareas == 0 ? 1 : totalTareas)) * 100;

                    // Se guardan en el contexto variables de sumario
                    request.setAttribute("totalTasks", "" + totalTareas);
                    request.setAttribute("totalDelayedTasks", "" + totalDelayed);
                    request.setAttribute("performance", "" + performance);
                    request.setAttribute("total_estimated_hours", sumEstimated);
                    request.setAttribute("total_real_hours", sumReal);
                    request.setAttribute("total_variance", "" + sumVariance);
                    request.setAttribute("company", company);
                    // forward to display the list
                    return (mapping.findForward("executeAdminLoad"));

                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();

            } finally {
                projBroker.close();
                memBroker.close();
                taskBroker.close();
                schBroker.close();
            }
        }
        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("listing"));
    }

    public String formatHour(BigDecimal realHour_v) {
        String aux = realHour_v.toString();
        System.out.println("formatHour");
        System.out.println(aux.trim());

        if ((aux.replace('.', ':').split(":").length > 0) && aux.length() > 1 && !aux.equals("0")) {
            return aux.replace('.', ':');
        } else {

            return aux.concat(":0");
        }
    }
}
