/*
 * Generated by Flecha Roja Technologies Auto Generator
 *
 * Created on January 9, 2003, 11:31 AM
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
import com.unify.webcenter.tools.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
public class masterReportsAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of calendarAction */
    public masterReportsAction() {

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
        reportsBroker broker;
        organizationsBroker brokerOrg;
        projectsBroker brokerProjects;
        membersBroker brokerMembers;
        type_tasksBroker brokerTaskType;
        teamsBroker brokerTeams;
        schedulesBroker scheduleBroker;
        String company = null;

        session = request.getSession(false);

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the home page
            return (mapping.findForward("login"));
        } else {
            broker = new reportsBroker();
            brokerOrg = new organizationsBroker();
            brokerProjects = new projectsBroker();
            brokerMembers = new membersBroker();
            brokerTaskType = new type_tasksBroker();
            brokerTeams = new teamsBroker();
            try {

                if (form == null) {
                    System.out.println(" Creating new reportsForm bean under key " + mapping.getAttribute());
                    form = new masterReportsForm();
                }
                // Se atrapa el formulario
                masterReportsForm thisForm = (masterReportsForm) form;

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                            
                company = tms.getCompany(user);
                request.setAttribute("version",  tms.getVersion());
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                // fetch action from form
                action = ((masterReportsForm) form).getOperation();

                servlet.log("[DEBUG] masterReportsAction at perform(): Action is " + action);
                // Se agrega el link para el menu con la ruta

                if (action != null && action.equals("add")) {
                    // Se agrega el link para el menu con la ruta
                    System.out.println("add aqui");
                    request.setAttribute("menuRoute",
                            "<a href='./reports.do'>" +
                            ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.reports") +
                            "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.addReport"));

                    /* El caso de la lista de miembros se debe modificar para que refleje las reglas nuevas
                    solicitadas por Luis Cardenas. A saber:
                    1.Si el usuario es ADMIN, entonces debe quedar tal cual esta hoy dia, es decir, 
                    que le muestra todos los usuarios para hacer el reporte.
                    2.El el usuario es un lider de proyecto, debe mostrarle solo los usuarios que forman parte de
                    sus equipos de trabajo.
                    3.Si el usuario es un razo, el usuario por default es el usuario mismo, no como ahora que 
                    un razo puede emitir un reporte de cualquier miembro.
                     */
                    boolean band = false;
                    if (request.getParameter("member") != null) {
                        session.setAttribute("member", new Integer(request.getParameter("member").toString()));
                        band = true;
                    }
                    if (user.isAdmin()) {
                        session.setAttribute("list_members", getListingMembers(brokerMembers, "name", "ASC", user.getId_account(), session, band));
                        request.setAttribute("list_projects", getListingProjects(brokerProjects, "name", "ASC", user.getId_account(), Integer.parseInt(session.getAttribute("member").toString())));
                    } else {
                        teamsData teamValue;
                        teamsData teamValue2;
                        Iterator e = brokerTeams.getListByMember("id", "ASC", user.getid(), user.getId_account());
                        projectsData projectData;
                        ArrayList listProject = new ArrayList();
                    /*    Hashtable hashMembers = new Hashtable();
                        while (e.hasNext()) {
                            teamValue = new teamsData();
                            teamValue = (teamsData) e.next();
                            projectData = new projectsData();
                            projectData = (projectsData) brokerProjects.getData(teamValue.getprojects(), user.getId_account());
                            listProject.add(projectData);
                            Iterator e2 = brokerTeams.getListByProject("id", "ASC", teamValue.getprojects(), 0, user.getId_account());
                            while (e2.hasNext()) {
                                teamValue2 = new teamsData();
                                teamValue2 = (teamsData) e2.next();
                                hashMembers.put("" + teamValue2.getmembers(),
                                        brokerMembers.getData(teamValue2.getmembers(), user.getId_account()));
                            }
                        }
                        membersData mem = (membersData) brokerMembers.getData(user.getid(), user.getId_account());
                        hashMembers.put("" + user.getid(), mem);*/
                        //Una vez que han sido procesados todos se procede a sacarlos 
                        // del hash
                       /* Iterator col = hashMembers.values().iterator();
                        ArrayList lista = new ArrayList();*/
                        // Se pasan a un iterador y se ordenan para finalmente agregarlos
                        // al contexto.
                        /*while (col.hasNext()) {
                            lista.add(col.next());
                        }
                        Collections.sort(lista);*/
                        brokerProjects.getMembersByProject2(String.valueOf(user.getid()), listProject, user.getId_account());
                        request.setAttribute("list_projects", listProject);
                       /* if (user.getprofile().equals("1")) {
                            request.setAttribute("list_members", lista);
                        } else {*/
                            // Es un usuario raso, se debe mostrar en la lista solo a el.
                            ArrayList lista2 = new ArrayList();
                            membersData mem2 = (membersData) brokerMembers.getData(user.getid(), user.getId_account());
                            lista2.add(mem2);
                            request.setAttribute("list_members", lista2);
                        //}
                    }
                    //Se define un Calendar.
                    Calendar now = Calendar.getInstance();
                    String hoy = "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DAY_OF_MONTH);

                    if (request.getParameter("startDate1") == null && request.getParameter("startDate2") == null) {
                        // Se extrae el componente dia, mes y a�o del start_date
                        request.setAttribute("startDate1", hoy);
                        request.setAttribute("startDate2", hoy);
                    } else {
                        request.setAttribute("startDate1", request.getParameter("startDate1"));
                        request.setAttribute("startDate2", request.getParameter("startDate2"));
                    }

                    request.setAttribute("dueDate1", hoy);
                    request.setAttribute("dueDate2", hoy);
                    request.setAttribute("endDate1", hoy);
                    request.setAttribute("endDate2", hoy);
                    System.out.println("por aca entro");
                    System.out.println(request.getParameter("global"));
                    request.setAttribute("global", request.getParameter("global"));
                    Iterator e = brokerTaskType.getList("description", "ASC", user.getId_account());
                    ArrayList listaTipoTasks = new ArrayList();
                    while (e.hasNext()) {
                        listaTipoTasks.add(e.next());
                    }
                    request.setAttribute("listTypeTasks", listaTipoTasks);


                    // En el caso de la operacion add, se despliega el formulario.
                    request.setAttribute("company", company);
                    return (mapping.findForward("displayAddForm"));

                } else if (action != null && (action.equals("execute") || action.equals("sort"))) {
                    // Se trata de la ejecucion del reporte, se procede a invocar el
                    // metodo en el broker para generar el reportte de tareas.     
                    System.out.println("exe aqui");
                    // Se guardan en el contexto
                    request.setAttribute("list_projects", convertToString(thisForm.getProjects()));
                    request.setAttribute("list_status", convertToString(thisForm.getStatus()));
                    //Se define un Calendar.
                    Calendar now = Calendar.getInstance();
                    String hoy = "" + now.get(Calendar.YEAR) +
                            "-" + (now.get(Calendar.MONTH) + 1) +
                            "-" + now.get(Calendar.DAY_OF_MONTH);
                    // Y el resto de parametros del reporte                  
                    request.setAttribute("startDate1", hoy);
                    request.setAttribute("startDate2", hoy);
                    request.setAttribute("FechaEntrega", request.getParameter("FechaEntrega"));
                    request.setAttribute("dueDate1", request.getParameter("dueDate1"));
                    request.setAttribute("dueDate2", request.getParameter("dueDate2"));
                    request.setAttribute("FechaFinal", request.getParameter("FechaFinal"));
                    request.setAttribute("endDate1", request.getParameter("endDate1"));
                    request.setAttribute("endDate2", request.getParameter("endDate2"));
                    request.setAttribute("global", request.getParameter("global"));

                    Iterator e = broker.getMasterReportByTasks(thisForm.getProjects(), thisForm.getMembers(),
                            thisForm.getStatus(), thisForm.getCompletionI(), thisForm.getCompletionF(),
                            request.getParameter("startDate1"), request.getParameter("startDate2"),
                            request.getParameter("FechaEntrega"),
                            request.getParameter("dueDate1"), request.getParameter("dueDate2"),
                            request.getParameter("FechaFinal"),
                            request.getParameter("endDate1"), request.getParameter("endDate2"),
                            "name", "ASC", user.getId_account());

                    membersData memberData = (membersData) brokerMembers.getData(thisForm.getMembers(), user.getId_account());

                    String fechaI = request.getParameter("startDate1");
                    String fechaF = request.getParameter("startDate2");
                    // Debemos convertir el iterador en un arraylist.
                    ArrayList lista = new ArrayList();
                    schedulesData data = new schedulesData();
                    BigDecimal totGenHour = new BigDecimal("0");
                    BigDecimal totGenMin = new BigDecimal("0");

                    BigDecimal totEst = new BigDecimal("0");

                    BigDecimal totAcum = new BigDecimal("0");

                    BigDecimal totHour = new BigDecimal("0");
                    BigDecimal totMin = new BigDecimal("0");
                    tasksData dataTask = new tasksData();

                    ArrayList repetidos = new ArrayList();
                    //ArrayList listaSchedule = new ArrayList();
                    while (e.hasNext()) {
                        data = new schedulesData();
                        dataTask = new tasksData();
                        data = (schedulesData) e.next();
                        dataTask = (tasksData) data.getparentTask();
                        if (!repetidos.contains(new Integer(dataTask.getid()))) {
                            scheduleBroker = new schedulesBroker();
                            totHour = scheduleBroker.getTotalTaskRealHoursbyDate(dataTask.getid(), fechaI, fechaF, dataTask.getId_account());
                            totMin = scheduleBroker.getTotalTaskRealMinutesbyDate(dataTask.getid(), fechaI, fechaF, dataTask.getId_account());
                            scheduleBroker.close();
                            totGenHour = totGenHour.add(totHour);
                            totGenMin = totGenMin.add(totMin);
                            boolean band = false;
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
                            data.setRealtime_hours(Integer.parseInt(totHour.toString()));
                            data.setRealtime_minutes(Integer.parseInt(totMin.toString()));
                            dataTask.setTotalScheduledHours(new BigDecimal(totHour + "." + totMin));
                            repetidos.add(new Integer(dataTask.getid()));
                            totEst = this.addTime(dataTask.getestimated_time(), totEst);
                            totAcum = this.addTime(dataTask.getactual_time(), totAcum);
                            data.setparentTask(dataTask);
                            lista.add(data);
                        }
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
                    String totalEmpleadas = "00:00";
                    if (totGenMin.toString().length() == 1) {
                        totalEmpleadas = totGenHour + ":0" + totGenMin;
                    } else {
                        totalEmpleadas = totGenHour + ":" + totGenMin;
                    }
                    if (request.getParameter("global")!=null && request.getParameter("global").equals("1")) {
                        System.out.println("antes del llamado");
                        e = broker.getMasterReportByTasks(thisForm.getMembers(),
                                "name", "ASC", user.getId_account(), repetidos);
                        System.out.println("despues del llamado");
                        while (e.hasNext()) {
                            data = new schedulesData();
                            dataTask = new tasksData();
                            dataTask = (tasksData) e.next();
                            System.out.println(dataTask.getid());
                            System.out.println(dataTask.getname());
                            System.out.println("***");
                            totEst = this.addTime(dataTask.getestimated_time(), totEst);
                            totAcum = this.addTime(dataTask.getactual_time(), totAcum);
                            data.settaskid(dataTask.getid());
                            data.setparentTask(dataTask);
                            data.setAuxRealtimeMinutes("0");
                            data.setHour_end("0");
                            data.setHour_start("0");
                            data.setId_account(dataTask.getId_account());
                            data.setRealtime_hours(0);
                            data.setRealtime_minutes(0);
                            data.setcomments("-");
                            data.setdate("-");
                            data.sethourid(0);
                            data.setuserid(dataTask.getassigned_to());
                            lista.add(data);
                        }
                        System.out.println("salio del while");
                    }
                    request.setAttribute("company", company);

                    session.removeAttribute("member");

                    // Se regresa al contexto la lista de todas las tareas que satisfacen
                    // la consulta estipulada.
                    HSSFWorkbook wb = (HSSFWorkbook) excelFile.excelFile(company, memberData.getname(), fechaI, fechaF, lista, totalEmpleadas, totEst, totAcum, user.getlanguage());
                    response.setContentType("application/x-download");
                    response.setHeader("Content-Disposition", "attachment; filename=InformeLabores.xls");
                    try {
                        OutputStream out = response.getOutputStream();
                        // write the workbook to the output stream,
                        // remembering to close our file
                        wb.write(out);
                        out.flush();
                        out.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "reports.do", "perform",
                        "Fatal Error: " + e.toString());
                e.printStackTrace();

            } finally {
                broker.close();
                brokerOrg.close();
                brokerProjects.close();
                brokerMembers.close();
                brokerTaskType.close();
            }
        }
        request.setAttribute("company", company);
        // Default if everthing else fails
        return (mapping.findForward("main"));
    }

    // Retorna la lista de organizaciones 
    private ArrayList getListingProjects(projectsBroker myBroker, String sortCol, String sortOrder, int accountId, int member) {
        // Se obtiene el iterador sobre todos los elementos de la lista.           
        Iterator e = myBroker.getListByMember(sortCol, sortOrder, member, accountId);

        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }

        return lista;
    }


    // Retorna la lista de members 
    private ArrayList getListingMembers(MainBroker myBroker, String sortCol, String sortOrder, int accountId, HttpSession session, boolean band) {
        // Se obtiene el iterador sobre todos los elementos de la lista.           
        Iterator e = myBroker.getList(sortCol, sortOrder, accountId);
        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        while (e.hasNext()) {
            lista.add(e.next());
        }
        if (!band) {
            if (lista.size() > 0) {
                membersData data = new membersData();
                data = (membersData) lista.get(0);
                session.setAttribute("member", String.valueOf(data.getid()));
            }
        }
        return lista;
    }

    /* M�todo que convierte un String[] a un String separado por comas */
    private String convertToString(String[] lista) {
        StringBuffer salida = new StringBuffer();
        for (int i = 0; i < lista.length; i++) {
            salida.append(lista[i] + ",");
        }
        return salida.toString();
    }

    public java.math.BigDecimal addTime(BigDecimal time, BigDecimal tot) {
        BigDecimal salida = new BigDecimal(0);
        // Para evitar una division por 0
        if (time.compareTo(new BigDecimal(0.0)) == 0 ||
                time.compareTo(new BigDecimal(0)) == 0 ||
                time.compareTo(new BigDecimal(0.0)) == 0 ||
                time.compareTo(new BigDecimal(0)) == 0) {
            salida = tot;
        } else {
            System.out.println("---");
            System.out.println(time);
            System.out.println(tot);
            BigDecimal hour = new BigDecimal(0);
            BigDecimal min = new BigDecimal(0);
            BigDecimal hourtot = new BigDecimal(0);
            BigDecimal mintot = new BigDecimal(0);
            String value1 = null;
            String value2 = null;
            if (time.toString().replace('.', ',').split(",").length > 1) {
                String[] split_min = null;
                String[] split_mintot = null;
                String replace_min = time.toString().replace('.', ',');
                split_min = replace_min.split(",");
                hour = new BigDecimal(split_min[0]);
                min = new BigDecimal(split_min[1]);
                if (split_min[1].toString().length() == 1) {
                    min = min.multiply(new BigDecimal(10));
                } else {
                    value1 = split_min[1].toString();
                    value2 = "0" + min;
                }

                if (tot.toString().replace('.', ',').split(",").length > 1) {
                    String replace_mintot = tot.toString().replace('.', ',');
                    split_mintot = replace_mintot.split(",");
                    hourtot = new BigDecimal(split_mintot[0]);
                    mintot = new BigDecimal(split_mintot[1]);
                    if (split_mintot[1].toString().length() == 1) {
                        mintot = mintot.multiply(new BigDecimal(10));
                    }
                }

                min = min.add(mintot);
                hour = hour.add(hourtot);

                boolean band = false;
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
                            min = new BigDecimal("0" + split_min[0]);
                        }
                    } else {
                        band = true;
                    }
                }
                /*  if ((value1 != null || value2 != null || "".equals(value1) || "".equals(value2)) && value1.equals(value2)) {
                salida = new BigDecimal(hour + ".0" + min);
                } else {*/
                if (min.toString().length() == 1) {
                    salida = new BigDecimal(hour + ".0" + min);
                } else {
                    salida = new BigDecimal(hour + "." + min);
                }
            //}
            } else {
                salida = tot.add(time);
            }
        }

        return salida;
    }
}