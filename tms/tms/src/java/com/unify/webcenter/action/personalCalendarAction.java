/*
 * personalCalendarAction.java
 *
 * Created on March 2, 2003, 4:47 PM
 */
package com.unify.webcenter.action;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import com.unify.webcenter.tools.*;
import com.unify.webcenter.form.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.conf.TMSConfigurator;

/**
 * Clase que representa las acciones propias de una agenda personal
 * @author  Gerardo Arroyo Arce
 */
public class personalCalendarAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    private boolean Conflict(int member, java.sql.Timestamp dateTemp, int HoraIni, int HoraFin, int id, boolean edit, int idAccount) {

        calendarBroker calBrokerTemp = new calendarBroker();
        calendarData dataTemp;
        Iterator e = calBrokerTemp.getList(member, dateTemp, id, edit,idAccount);
        while (e.hasNext()) {
            dataTemp = (calendarData) e.next();
            if (!((HoraIni >= dataTemp.gethour_end()) ||
                    (HoraFin <= dataTemp.gethour_start()))) {
                calBrokerTemp.close();
                return true;
            }
        }
        calBrokerTemp.close();
        return false;
    }

    /** Creates a new instance of personalCalendarAction */
    public personalCalendarAction() {

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
        calendarBroker calBroker;
        tasksBroker taskBroker;
        String company = null;
        session = request.getSession(false);
        int taskid;

        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.getAttribute("login") == null) {

            // forward to display the home page
            return (mapping.findForward("login"));
        } else {
                 //Set the selected tab 
            session.setAttribute("current","calendar");
            calBroker = new calendarBroker();
            taskBroker = new tasksBroker();
            try {
                if (form == null) {
                    System.out.println(" Creating new personalCalendarForm bean under key " + mapping.getAttribute());
                    form = new personalCalendarForm();
                }

                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms = new TMSConfigurator(user);
                company = tms.getCompany(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                // Se agrega el link para el menu con la ruta
                request.setAttribute("menuRoute",
                        "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/&nbsp;" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayCalendar"));

                ActionErrors errors = new ActionErrors();
                // Se toma el formulario.
                personalCalendarForm thisForm = (personalCalendarForm) form;

                servlet.log("[DEBUG] personalCalendarAction at perform(): Operation is " + thisForm.getOperation());
                WebCalendar calendar = null;

                if (thisForm.getOperation().equals("goTo")) {
                    // Se trata de al dia en cuestion

                    Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(thisForm.getTheDate());

                    // Se crea el calendario
                    calendar = new WebCalendar(date);
                    calendar.setLocate(user.getlanguage());

                    // Se resetea al valor anterior
                    date.setTimeInMillis(thisForm.getTheDate());

                    // Se procesa cada dia.
                    ArrayList dias = calendar.getDays();

                    // Se guarda el id del proyecto
                    int proj_id;

                    ArrayList content;
                    WebDay day;

                    projectsData data = new projectsData();
                    projectsBroker projBroker = new projectsBroker();
                    calendarData calData = new calendarData();
                    tasksData taskData = new tasksData();
                    membersBroker memberBroker = new membersBroker();

                    //Informacion el nombre el usuario
                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());

                    Calendar datevalue = Calendar.getInstance();

                    for (int i = 0; i < dias.size(); i++) {
                        day = (WebDay) dias.get(i);

                        // Ahora se procede a determinar si hay eventos para esa fecha del
                        // usuario actual.
                        content = calBroker.getListByDate("hour_start", "ASC",
                                thisForm.getuserId(),
                                day.getThisDay(),user.getId_account());

                        if (content.size() > 0) {
                            day.setEvents(true);
                            // Si se trata del mismo dia que estamos viendo
                            if ((day.getThisDay().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) &&
                                    (day.getThisDay().get(Calendar.YEAR) == date.get(Calendar.YEAR))) {

                                for (int j = 0; j < content.size(); j++) {

                                    // Modificado by Jury 3/31/04  Guardar datos del proyecto
                                    calData = (calendarData) content.get(j);
                                    data = (projectsData) projBroker.getData(((tasksData) taskBroker.getData(calData.gettask(),user.getId_account())).getproject(),user.getId_account());

                                    calData.setprojectId(data.getid());
                                    calData.setprojectName(data.getname());
                                    taskData = (tasksData) taskBroker.getData(calData.gettask(),user.getId_account());
                                    calData.setstart_date(taskData.getstart_date());

                                    calData.setend_date(taskData.getdue_date());
                                    if (data.getowner() == 0) {
                                        calData.setprojectowner("");
                                        calData.setowneremail("");
                                    } else {
                                        calData.setprojectowner(((membersData) memberBroker.getData(data.getowner(),user.getId_account())).getname());
                                        calData.setowneremail(((membersData) memberBroker.getData(data.getowner(),user.getId_account())).getemail_work());
                                    }
                                    if (taskData != null) {
                                        calData.settaskdesc(taskData.getname());
                                    } else {
                                        calData.settaskdesc("");
                                    }

                                    content.set(j, calData);
                                }
                                request.setAttribute("appointments", content);
                            }
                        }

                    }

                    projBroker.close();
                    memberBroker.close();

                    // Se dispone el calendario en la pagina
                    request.setAttribute("calendar", calendar);
                    request.setAttribute("company", company);
                    return (mapping.findForward("displayCalendar"));

                } else if (thisForm.getOperation().equals("addEvent")) {


                    // Se guarda en el contexto la fecha de hoy debidamente
                    // formateada
                    Calendar fecha = Calendar.getInstance();
                    fecha.setTimeInMillis(thisForm.getTheDate());
                    java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                    // Se agrega al contexto
                    request.setAttribute("date", df.format(fecha.getTime()));

                    // Se trae la lista de tareas.
                    Iterator e = taskBroker.getListByMemberFilteredForCalendar("name", "ASC", thisForm.getuserId(), 0,user.getId_account());
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        lista.add(e.next());
                    }
                    membersBroker memberBroker = new membersBroker();
                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());
                    memberBroker.close();

                    request.setAttribute("listaTasks", lista);
                    request.setAttribute("task", "0");
                    request.setAttribute("hour_start", "01");
                    request.setAttribute("hour_end", "01");
                    request.setAttribute("emails", "");
                    request.setAttribute("subject", "");
                    request.setAttribute("description", "");
                    //request.setAttribute("member", " ");
                    request.setAttribute("member", request.getParameter("member"));
                    request.setAttribute("min_start", "00");
                    request.setAttribute("min_end", "00");
                    request.setAttribute("emails", "");
                    request.setAttribute("listContacts", "");
                    request.setAttribute("id", "0");
                    // Se trata de agregar una nueva entrada al calendario
                    thisForm.setOperation("applyAdd");
                    request.setAttribute("company", company);
                    return (mapping.findForward("addEvent"));

                } else if (thisForm.getOperation().equals("addEvent1")) {

                    // Se guarda en el contexto la fecha de hoy debidamente
                    // formateada
                    Calendar fecha = Calendar.getInstance();
                    fecha.setTimeInMillis(thisForm.getTheDate());

                    java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                    // Se agrega al contexto
                    request.setAttribute("date", df.format(fecha.getTime()));

                    // Se trae la lista de tareas.
                    Iterator e = taskBroker.getListByMemberFilteredForCalendar("name", "ASC", thisForm.getuserId(), 0,user.getId_account());
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        lista.add(e.next());
                    }
                    membersBroker memberBroker = new membersBroker();
                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());
                    memberBroker.close();

                    request.setAttribute("listaTasks", lista);
                    request.setAttribute("userId", request.getParameter("userId"));
                    request.setAttribute("subject", request.getParameter("subject"));
                    request.setAttribute("description", request.getParameter("description"));
                    request.setAttribute("member", request.getParameter("member"));
                    request.setAttribute("id", request.getParameter("id"));
                    request.setAttribute("hour_start", request.getParameter("hour_start"));
                    request.setAttribute("hour_end", request.getParameter("hour_end"));
                    request.setAttribute("min_start", request.getParameter("min_start"));
                    request.setAttribute("min_end", request.getParameter("min_end"));
                    request.setAttribute("task", request.getParameter("task"));
                    System.out.println("contenido de emails en personalCalendar: " + request.getParameter("emails"));
                    System.out.println("contenido de listaContacts en personalCalendar: " + request.getParameter("listaContacts"));
                    request.setAttribute("emails", request.getParameter("emails"));
                    request.setAttribute("listaContacts", request.getParameter("listaContacts"));
                    thisForm.setOperation("applyAdd");
                    request.setAttribute("company", company);
                    return (mapping.findForward("addEvent"));

                } else if (thisForm.getOperation().equals("editEvent")) {
                    // Se trata de agregar una nueva entrada al calendario
                    thisForm.setOperation("applyEdit");

                    // Se toma la referencia al objeto, para cargar los 
                    // datos del formulario.                

                    calendarData calData = (calendarData) calBroker.getData(
                            Integer.parseInt(request.getParameter("id")),user.getId_account());

                    System.out.println("adfssdf");
                    System.out.println(calData.getdescription());
                    // Se guardan los datos en el context
                    request.setAttribute("subject", calData.getsubject());
                    if (calData.getdescription() != null) {
                        request.setAttribute("description", new String(calData.getdescription()));
                    } else {
                        request.setAttribute("description", "");
                    }

                    request.setAttribute("member", "" + calData.getmember());
                    request.setAttribute("id", "" + calData.getid());
                    request.setAttribute("hour_start", "" + calData.gethour_start());
                    request.setAttribute("hour_end", "" + calData.gethour_end());
                    request.setAttribute("min_start", "" + calData.getmin_start());
                    request.setAttribute("min_end", "" + calData.getmin_end());
                    request.setAttribute("task", "" + calData.gettask());
                    request.setAttribute("emails", calData.getguests());
                    //Obtiene la lista de contactos, apartir de la lista de emails 
                    membersData data;
                    membersBroker memBroker = new membersBroker();
                    String[] fields = (calData.getguests() + ";").split(";");
                    if (calData.getguests() != null) {
                        ArrayList items = memBroker.getItems(fields,user.getId_account());
                        String listemail[] = new String[items.size()];
                        for (int i = 0; i < items.size(); i++) {
                            data = (membersData) items.get(i);
                            listemail[i] = data.getname() + " <" + data.getemail_work() + ">";
                        }
                        request.setAttribute("listContacts", listemail);
                    }

                    membersBroker memberBroker = new membersBroker();
                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());
                    memberBroker.close();

                    // Se trae la lista de tareas.
                    Iterator e = taskBroker.getListByMemberFilteredForCalendar("name", "DESC", thisForm.getuserId(), 0,user.getId_account());
                    ArrayList lista = new ArrayList();
                    while (e.hasNext()) {
                        lista.add(e.next());
                    }

                    request.setAttribute("listaTasks", lista);

                    // Se guarda en el contexto la fecha de hoy debidamente
                    // formateada
                    Calendar fecha = Calendar.getInstance();
                    fecha.setTimeInMillis(thisForm.getTheDate());

                    java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                    // Se agrega al contexto
                    request.setAttribute("date", df.format(fecha.getTime()));
                    request.setAttribute("company", company);
                    return (mapping.findForward("addEvent"));

                } else if (thisForm.getOperation().equals("applyAdd") ||
                        thisForm.getOperation().equals("applyEdit")) {
                    // Se trata de aplicar una entrada en la BD

                    Calendar fecha = Calendar.getInstance();
                    fecha.setTimeInMillis(thisForm.getTheDate());
                    fecha.set(Calendar.HOUR, 1);
                    fecha.set(Calendar.MINUTE, 1);
                    fecha.set(Calendar.SECOND, 1);
                    fecha.set(Calendar.MILLISECOND, 10);
                    fecha.set(Calendar.AM_PM, Calendar.AM);

                    calendarData data = new calendarData();
                    data.setdate(new java.sql.Timestamp(fecha.getTimeInMillis()));
                    data.setdescription(request.getParameter("description"));
                    data.sethour_end(Integer.parseInt(request.getParameter("hour_end")));
                    data.sethour_start(Integer.parseInt(request.getParameter("hour_start")));
                    data.setmin_end(Integer.parseInt(request.getParameter("min_end")));
                    data.setmin_start(Integer.parseInt(request.getParameter("min_start")));
                    data.setmember(thisForm.getuserId());
                    data.setsubject(request.getParameter("subject"));
                    data.setguests(request.getParameter("emails"));
                    data.setId_account(user.getId_account());
                    /*******/
                    boolean edit = false;
                    if (thisForm.getOperation().equals("applyEdit")) {
                        edit = true;
                    }

                    if (Conflict(thisForm.getuserId(), data.getdate(), data.gethour_start(), data.gethour_end(),
                            Integer.parseInt(request.getParameter("id")), edit,user.getId_account())) {
                        System.out.println("error de translape");
                        {
                            request.setAttribute("company", company);
                            return (mapping.findForward("displayConflict"));
                        }
                    }

                    if (data.gethour_start() >= data.gethour_end()) {
                        // No se puede insertar una tarea nueva como rechazada :>> Jury
                        errors.add("HourConflict",
                                new ActionError("errors.personalcalendar.hourNotSuperior"));
                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=addEvent&theDate=" + thisForm.getTheDate() +
                                "&userId=" + thisForm.getuserId() +
                                "&username=" + request.getParameter("username")));
                    }

                    if (request.getParameter("subject") == null || request.getParameter("subject").equalsIgnoreCase("")) {
                        // No se puede insertar una tarea nueva como rechazada :>> Jury
                        errors.add("Required",
                                new ActionError("calendar.subject"));
                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=addEvent&theDate=" + thisForm.getTheDate() +
                                "&userId=" + thisForm.getuserId() +
                                "&username=" + request.getParameter("username")));
                    }


                    if (request.getParameter("task") == null) {
                        // No se puede insertar una tarea nueva como rechazada :>> Jury
                        errors.add("RequiredTasks",
                                new ActionError("calendar.tasks"));
                        saveErrors(request, errors);
                        request.setAttribute("company", company);
                        return (new ActionForward(mapping.findForward("displayAddFormError").
                                getPath() + "?operation=addEvent&theDate=" + thisForm.getTheDate() +
                                "&userId=" + thisForm.getuserId() +
                                "&username=" + request.getParameter("username")));
                    }

                    /******/
                    data.settask(Integer.parseInt(request.getParameter("task")));
                    projectsBroker projBroker = new projectsBroker();
                    projectsData projData = (projectsData) projBroker.getData(((tasksData) taskBroker.getData(data.gettask(),user.getId_account())).getproject(),user.getId_account());
                    projBroker.close();
                    data.setprojectId(projData.getid());
                    data.setprojectName(projData.getname());
                    membersBroker memberBroker = new membersBroker();
                    data.setprojectowner(((membersData) memberBroker.getData(projData.getowner(),user.getId_account())).getname());
                    memberBroker.close();

                    if (thisForm.getOperation().equals("applyAdd")) {
                        data.setid(0);
                    } else {
                        data.setid(Integer.parseInt(request.getParameter("id")));
                    }

                    if (thisForm.getOperation().equals("applyAdd")) {
                        calBroker.add(data);
                    } else {
                        calBroker.update(data);
                    }

                    String[] fields = (request.getParameter("emails") + ";").split(";");

                    String lista = "";
                    for (int i = 0; i < fields.length; i++) {
                        lista += fields[i] + " - ";
                        fields[i] = fields[i].trim();
                    }
                    membersBroker memBroker = new membersBroker();
                    ArrayList items = memBroker.getItems(fields,user.getId_account());

                    // Intanciar clase para el envio de correo
                    com.unify.webcenter.tools.sendMail sm = new com.unify.webcenter.tools.sendMail();

                    // Y para cada member se procede con su insercion.
                    membersData from_member = (membersData) memBroker.getData(user.getid(),user.getId_account());
                    for (int i = 0; i < items.size(); i++) {
                        membersData to_member = (membersData) items.get(i);
                        sm.send("meetingrequest", from_member,
                                to_member, data.getid(), new String(data.getdescription()),user, String.valueOf(session.getAttribute("mainUrl")));
                    }
                    request.setAttribute("company", company);
                    return (new ActionForward(mapping.findForward("toDate").
                            getPath() + "?operation=goTo&theDate=" + thisForm.getTheDate() +
                            "&userId=" + thisForm.getuserId() +
                            "&username=" + request.getParameter("username")));

                } else if (thisForm.getOperation().equals("delete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado

                    // get the string with ids separate by "," character, and split in array os strings
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");
                    ArrayList items = calBroker.getItems(fields,user.getId_account());

                    membersBroker memberBroker = new membersBroker();
                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());
                    request.setAttribute("userId", "" + thisForm.getuserId());
                    memberBroker.close();

                    request.setAttribute("checkedItems", request.getParameter("checkedItems"));
                    request.setAttribute("items", items);
                    request.setAttribute("fromPage", "" + thisForm.getTheDate());
                    request.setAttribute("title", java.util.ResourceBundle.getBundle("ApplicationResources",
                            new Locale(user.getlanguage(), "")).getString("common.Event"));
                    request.setAttribute("do", request.getRequestURI());
                    request.setAttribute("company", company);
                    // forward to display the list 
                    return (mapping.findForward("confirmDelete"));

                } else if (thisForm.getOperation().equals("applyDelete")) {
                    // Tomamos el bean correspondiente a este objeto a fin
                    // de proceder con su borrado                    
                    String[] fields = (request.getParameter("checkedItems") + ",").split(",");


                    String lista = "";
                    for (int i = 0; i < fields.length; i++) {
                        lista += fields[i] + " - ";
                    }
                    ArrayList items = calBroker.getItems(fields,user.getId_account());

                    // Se procede con el borrado de cada una de ellas.
                    calendarData dataDelete;
                    for (int i = 0; i < items.size(); i++) {
                        dataDelete = (calendarData) items.get(i);

                        // We delete the object in the DBMS
                        calBroker.delete(dataDelete);
                    }

                    membersBroker memberBroker = new membersBroker();


                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());
                    memberBroker.close();
                    request.setAttribute("company", company);
                    return (new ActionForward(mapping.findForward("toDate").
                            getPath() + "?operation=goTo&userId=" + thisForm.getuserId() +
                            "&theDate=" + request.getParameter("fromPage") + "&username=" + request.getParameter("username")));

                } else if (thisForm.getOperation().equals("viewEvent")) {
                    // Se trata de agregar una nueva entrada al calendario

                    // Se guarda en el contexto la fecha de hoy debidamente
                    // formateada
                    Calendar fecha = Calendar.getInstance();
                    fecha.setTimeInMillis(thisForm.getTheDate());

                    java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                    // Se agrega al contexto
                    request.setAttribute("date", df.format(fecha.getTime()));

                    membersBroker memberBroker = new membersBroker();
                    request.setAttribute("username", ((membersData) memberBroker.getData(thisForm.getuserId(),user.getId_account())).getname());
                    memberBroker.close();

                    // Se toma la referencia al objeto, para cargar los 
                    // datos del formulario.                

                    calendarData calData = (calendarData) calBroker.getData(
                            Integer.parseInt(request.getParameter("id")),user.getId_account());

                    // Se guardan los datos en el context
                    request.setAttribute("subject", calData.getsubject());
                    request.setAttribute("description", new String(calData.getdescription()));
                    request.setAttribute("member", "" + calData.getmember());
                    request.setAttribute("id", "" + calData.getid());
                    request.setAttribute("hour_start", "" + calData.gethour_start());
                    request.setAttribute("hour_end", "" + calData.gethour_end());
                    request.setAttribute("min_start", "" + calData.getmin_start());
                    request.setAttribute("min_end", "" + calData.getmin_end());
                    request.setAttribute("task", "" + calData.gettask());
                    request.setAttribute("operation1", "viewEvent");
                    request.setAttribute("emails", "");
                    request.setAttribute("listaContacts", request.getParameter("listaContacts"));
                    request.setAttribute("taskdescription", ((tasksData) taskBroker.getData(calData.gettask(),user.getId_account())).getname());
                    request.setAttribute("company", company);
                    return (mapping.findForward("viewEvent"));
                }

            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                e.printStackTrace();
                logger.logp(Level.SEVERE, "personalCalendar.do", "perform",
                        "Fatal Error: " + e.toString());

            } finally {
                calBroker.close();
                taskBroker.close();
            }
        }
        request.setAttribute("company", company);
        return (mapping.findForward("displayCalendar"));
    }
}
