/*
 * searchAction.java
 *
 * Created on March 11, 2003, 3:33 PM
 */
package com.unify.webcenter.search;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.lucene.queryParser.QueryParser;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import java.io.IOException;
import java.io.BufferedReader;

import org.textmining.text.extraction.*;
import org.pdfbox.searchengine.lucene.LucenePDFDocument;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Hits;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.conf.*;

/**
 *  Accion encargado de llevar a cabo la busqueda dentro del indice 
 * todos los elementos del TMS procesados
 * @author  Gerardo Arroyo
 */
public class searchAction extends Action {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of searchAction */
    public searchAction() {
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
        HttpSession session = request.getSession();
        String company = null;
        // Si la sesion es nula, se debe redireccionar al login.
        if (session == null || session.isNew() ||
                session.getAttribute("login") == null) {

            // forward to display the login page
            return (mapping.findForward("login"));
        } else {

            // Si el usuario esta logeado se le despliega el menu
            try {
                 //Set the selected tab 
            session.setAttribute("current","search");
                // Se obtiene la referencia al loginData dentro de la session.
                loginData user = (loginData) session.getAttribute("login");
                TMSConfigurator tms= new TMSConfigurator(user);
                company = tms.getCompany(user);
                // Se agrega al contexto la informacion del usuario
                request.setAttribute("userInfo", user);

                // Se agrega el link para el menu con la ruta
                request.setAttribute("menuRoute",
                        "<a href='./home.do'>" + java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displayStart") + "</a>&nbsp;/&nbsp;" +
                        java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("header.displaySearch"));


                // Se lee la operacion que se envia
                String operation = request.getParameter("operation");
request.setAttribute("company", company);
        
                if (operation == null || operation.equals("")) {
                    // Se redirecciona a la pagina de pedir los parametros
                    return (mapping.findForward("params"));

                } else if (operation.equals("index")) {
                    indexData dataIndex = new indexData();
                    // Se procede con la indexacion de los datos
                    dataIndex.indexInfo(user.getId_account(), request);

                    // Se agrega el link para el menu con la ruta
                    request.setAttribute("menuRoute",
                            "<a href='./admin.do'>" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.Administration") + "</a>&nbsp;/&nbsp;" +
                            java.util.ResourceBundle.getBundle("ApplicationResources", new Locale(user.getlanguage(), "")).getString("common.IndexerStatistics"));
                    return (mapping.findForward("indexing"));

                } else {
                    // Se trata de la ejecucion de una busqueda.
                    String todasClases, projects, tasks, members, files,
                            calendars, schedules, topics;

                    String queryString = "";

                    // Se lee el query dado por el usuario
                    queryString = request.getParameter("query");

                    request.setAttribute("query", queryString);

                    // Se lee el tipo de consulta
                    todasClases = request.getParameter("all");
                    projects = request.getParameter("projects");
                    tasks = request.getParameter("tasks");
                    members = request.getParameter("members");
                    files = request.getParameter("files");
                    //calendars = request.getParameter("calendars");
                    //schedules = request.getParameter("schedules");
                    topics = request.getParameter("topics");

                    // Enumerationesta lista quedan todos los documentos de cualquier
                    // tipo que satisfacen la consulta.
                    ArrayList lista = new ArrayList();

                    // Se procede a ejecutar la busqueda de acuerdo al tipo que el
                    // usuario ha indicado.
                    if (todasClases != null || projects != null) {
                        search("indexProjects", queryString, lista);

                    }
                    if (todasClases != null || tasks != null) {
                        search("indexTasks", queryString, lista);

                    }
                    if (todasClases != null || members != null) {
                        search("indexMembers", queryString, lista);

                    }
                    /*if (todasClases != null || calendars != null) {
                        search("indexCalendars", queryString, lista);

                    }
                    if (todasClases != null || schedules != null) {
                        search("indexSchedules", queryString, lista);

                    }*/
                    if (todasClases != null || topics != null) {
                        search("indexPosts", queryString, lista);

                    }
                    if (todasClases != null || files != null) {
                        search("indexFiles", queryString, lista);

                    }

                    // Se ordena la lista con base en el score.
                    Collections.sort(lista);
                    request.setAttribute("lista", lista);
                    // Se redirecciona a la pagina de resultados.
                    return (mapping.findForward("results"));
                }
            } catch (Exception e) {
                servlet.log("[ERROR] Action at final catch: " + e.getMessage());
                logger.logp(Level.SEVERE, "searchAction.do", "perform",
                        "Fatal Error: " + e.toString());
            }
        }

        // Default if everthing else fails
        return (mapping.findForward("params"));
    }

    /* M�todo que se encarga de llevar a cabo una busqueda en el Index que se envia
     * como par�metro */
    private void search(String indexName, String queryString, ArrayList listaDocs) throws Exception {
        String path = TMSConfigurator.getIndexPath();
        System.out.println("path search");
        System.out.println(path);
        System.out.println("ind: "+indexName);
        Searcher searcher = new IndexSearcher(path + indexName);
        Analyzer analyzer = new StandardAnalyzer();
        //Query query = QueryParser.parse(queryString, "contents", analyzer);
        QueryParser queryParser = new QueryParser("contents", analyzer);

        Query query = queryParser.parse(queryString);
        Hits hits = searcher.search(query);
//        System.out.println(hits.length() + " total matching documents");
        String id;

        for (int i = 0; i < hits.length(); i++) {
            Document doc = hits.doc(i);

            // Se agrega a la lista de resultados, el presente documento.
            listaDocs.add(new searchData(
                    Long.parseLong(doc.get("id")),
                    Integer.parseInt(doc.get("userId")),
                    (hits.score(i) * 100),
                    doc.get("category"),
                    doc.get("header"),
                    doc.get("summary")));

//            System.out.println("MATCH: " +  id + " Score: " + (hits.score(i)*100) + "%");
        }

    }

    /**
     *  Metodo que se encarga de llevar a cabo la indexacion de la informacion de la base
     * de datos.
     */
/*      private void indexInfo(int idAccount, HttpServletRequest request) {
        try {
            // Se extran todas las tareas.
            tasksBroker taskBroker = new tasksBroker();
            Iterator eTask = taskBroker.getList("id", "ASC", idAccount);
            taskBroker.close();

            // Se toma la fecha actual para efectos de calcular la duracion del proceso
            Date start = new Date();

            System.out.println("Indexing Database... please wait");


            /******************************************************************
            PAS0 #01  INDEXACION DE TAREAS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            String path = TMSConfigurator.getIndexPath();

            IndexWriter writerTasks = new IndexWriter(path + "indexTasks", new StandardAnalyzer(), true);

            System.out.println(" 1.Indexing Tasks...");

            // Se indexan todos los elementos de la base de datos, primeramente las
            // tareas
            request.setAttribute("totalTasks", "" + indexDocsTasks(writerTasks, eTask, idAccount));


            // Se optimiza el indice y se cierra
            writerTasks.optimize();
            writerTasks.close();


            /******************************************************************
            PAS0 #02  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            IndexWriter writerProjects = new IndexWriter(path + "indexProjects", new StandardAnalyzer(), true);

            System.out.println(" 2.Indexing Projects...");

            // Se procede con la indexacion
            projectsBroker projBroker = new projectsBroker();
            Iterator eProject = projBroker.getList("id", "ASC", idAccount);
            projBroker.close();
            request.setAttribute("totalProjects", "" + indexDocsProjects(writerProjects, eProject, idAccount));

            // Se optimiza el indice y se cierra
            writerProjects.optimize();
            writerProjects.close();

            /******************************************************************
            PAS0 #03  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            IndexWriter writerMembers = new IndexWriter(path + "indexMembers", new StandardAnalyzer(), true);

            System.out.println(" 3.Indexing Members...");

            // Se procede con la indexacion
            membersBroker memBroker = new membersBroker();
            Iterator eMembers = memBroker.getList("id", "ASC", idAccount);
            memBroker.close();
            request.setAttribute("totalMembers", "" + indexDocsMembers(writerMembers, eMembers, idAccount));

            // Se optimiza el indice y se cierra
            writerMembers.optimize();
            writerMembers.close();


            /******************************************************************
            PAS0 #04  INDEXACION DE CALENDARS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            IndexWriter writerCalendar = new IndexWriter(path + "indexCalendars", new StandardAnalyzer(), true);

            System.out.println(" 4.Indexing Calendars...");

            // Se procede con la indexacion
            calendarBroker calBroker = new calendarBroker();
            Iterator eCalendar = calBroker.getList("id", "ASC", idAccount);
            calBroker.close();
            request.setAttribute("totalCalendars", "" + indexDocsCalendars(writerCalendar, eCalendar, idAccount));

            // Se optimiza el indice y se cierra
            writerCalendar.optimize();
            writerCalendar.close();


            /******************************************************************
            PAS0 #05  INDEXACION DE SCHEDULES
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            IndexWriter writerSchedules = new IndexWriter(path + "indexSchedules", new StandardAnalyzer(), true);

            System.out.println(" 5.Indexing Schedules...");

            // Se procede con la indexacion
            schedulesBroker scheduleBroker = new schedulesBroker();
            Iterator eSchedules = scheduleBroker.getList("taskid", "ASC", idAccount);
            scheduleBroker.close();
            request.setAttribute("totalSchedules", "" + indexDocsSchedules(writerSchedules, eSchedules));

            // Se optimiza el indice y se cierra
            writerSchedules.optimize();
            writerSchedules.close();


            /******************************************************************
            PAS0 #06  INDEXACION DE POSTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            IndexWriter writerPosts = new IndexWriter(path + "indexPosts", new StandardAnalyzer(), true);

            System.out.println(" 6.Indexing Posts...");

            // Se procede con la indexacion
            postBroker postsBroker = new postBroker();
            Iterator ePosts = postsBroker.getList("id", "ASC", idAccount);
            postsBroker.close();
            request.setAttribute("totalPosts", "" + indexDocsPosts(writerPosts, ePosts));

            // Se optimiza el indice y se cierra
            writerPosts.optimize();
            writerPosts.close();

            /******************************************************************
            PAS0 #07  INDEXACION DE FILES
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
/*            IndexWriter writerFiles = new IndexWriter(path + "indexFiles", new StandardAnalyzer(), true);

            System.out.println(" 7.Indexing Files...");

            // Se procede con la indexacion
            filesBroker fileBroker = new filesBroker();
            Iterator eFiles = fileBroker.getList("id", "ASC", idAccount);
            fileBroker.close();
            request.setAttribute("totalFiles", "" + indexDocsFiles(writerFiles, eFiles,
                    this.getServlet().getServletContext()));

            // Se optimiza el indice y se cierra
            writerFiles.optimize();
            writerFiles.close();




            // Fecha de Terminacion de indexacion
            Date end = new Date();

            // Se despliega la cantidad de tiempo invertido en el proceso
            System.out.print("Done in: ");
            System.out.print(end.getTime() - start.getTime());
            System.out.println(" total milliseconds.");

            request.setAttribute("totalTime", "" + (end.getTime() - start.getTime()));

        } catch (Exception e) {
            System.out.println(" Caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            logger.logp(Level.SEVERE, "searchAction.do", "perform",
                    "Fatal Error: " + e.toString());

        }
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * tareas
     */
/*    public static int indexDocsTasks(IndexWriter writer, Iterator e, int idAccount) throws Exception {
        int i = 0;
        tasksData data;
        while (e.hasNext()) {
            data = (tasksData) e.next();
            System.out.println("  Adding :" + data.getid() + "...");
            System.out.println(data.toString());
            i++;
            // Se agrega al index
            try {
                if (data.getdescription() != null) {
                    writer.addDocument(TMSDocument.Document(data.getid(),
                            "6", data.getname(), new String(data.getdescription()), data.toString()));
                } else {
                    writer.addDocument(TMSDocument.Document(data.getid(),
                            "6", data.getname(), "", data.toString()));
                }
            } catch (Exception e2) {
                System.out.println("Error Indexing Tasks.." + data.getid());
            }
        }

        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * proyectos
     */
/*    public static int indexDocsProjects(IndexWriter writer, Iterator e, int idAccount) throws Exception {
        int i = 0;
        projectsData data;
        while (e.hasNext()) {
            data = (projectsData) e.next();
            System.out.println("  Adding :" + data.getid() + "...");
            i++;
            // Se agrega al index
            try {
                if (data.getdescription() != null) {
                    writer.addDocument(TMSDocument.Document(data.getid(),
                            "1",
                            data.getname(), new String(data.getdescription()), data.toString()));
                } else {
                    writer.addDocument(TMSDocument.Document(data.getid(),
                            "1",
                            data.getname(), "", data.toString()));
                }
            } catch (Exception e2) {
                System.out.println("Error Indexing Schedule.." + data.getid());
            }
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * members
     */
 /*   public static int indexDocsMembers(IndexWriter writer, Iterator e, int idAccount) throws Exception {
        int i = 0;
        membersData data;
        while (e.hasNext()) {
            data = (membersData) e.next();
            System.out.println("  Adding :" + data.getid() + "...");

            // Se agrega al index
            writer.addDocument(TMSDocument.Document(data.getid(),
                    "2",
                    data.getname(), data.gettitle() + " - " + data.getname() + "( " + data.getcomments() + " )", data.toString()));
            i++;
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * calendars
     */
/*    public static int indexDocsCalendars(IndexWriter writer, Iterator e, int idAccount) throws Exception {
        int i = 0;
        calendarData data;
        while (e.hasNext()) {
            data = (calendarData) e.next();
            System.out.println("  Adding :" + data.getid() + "...");
            i++;
            // Se agrega al index
            try {
                if (data.getdescription() != null) {
                    writer.addDocument(TMSDocument.Document(data.getdate().getTime(),
                            data.getmember(),
                            "3",
                            data.getsubject(), new String(data.getdescription()), data.toString()));
                } else {
                    writer.addDocument(TMSDocument.Document(data.getdate().getTime(),
                            data.getmember(),
                            "3",
                            data.getsubject(), "", data.toString()));
                }
            } catch (Exception e2) {
                System.out.println("Error Indexing Schedule.." + data.getid());
            }
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * schedules
     */
 /*   public static int indexDocsSchedules(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        schedulesData data;
        while (e.hasNext()) {
            data = (schedulesData) e.next();
            System.out.println("  Adding :" + data.getid() + "...");
            i++;
            // Se agrega al index
            try {
                if (data.getcomments() != null) {
                    writer.addDocument(TMSDocument.Document(data.getid(),
                            data.getuserid(),
                            "4",
                            "" + data.getdate(), new String(data.getcomments()), data.toString()));
                } else {
                    writer.addDocument(TMSDocument.Document(data.getid(),
                            data.getuserid(),
                            "4",
                            "" + data.getdate(), new String(data.getcomments()), data.toString()));
                }
            } catch (Exception e2) {
                System.out.println("Error Indexing Schedule.." + data.getid());
            }
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * posts
     */
/*    public static int indexDocsPosts(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        postData data;
        while (e.hasNext()) {
            data = (postData) e.next();
            System.out.println("  Adding :" + data.getid() + "...");
            i++;
            // Se agrega al index
            try {
                if (data.getmessage() != null) {
                    writer.addDocument(TMSDocument.Document(data.gettopic(),
                            "5",
                            data.getparentMember().getname(), new String(data.getMessage()), data.toString()));
                } else {
                    writer.addDocument(TMSDocument.Document(data.gettopic(),
                            "5",
                            data.getparentMember().getname(), "", data.toString()));
                }
            } catch (Exception e2) {
                System.out.println("Error Indexing Posts.." + data.getid());
            }
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * files
     */
/*    public static int indexDocsFiles(IndexWriter writer, Iterator e,
            ServletContext context) throws Exception {
        int i = 0;
        filesData data;

        // Empleados para extraer el cuerpo del archivo de tipo DOC y PDF
        WordExtractor extractor = new WordExtractor();


        while (e.hasNext()) {
            data = (filesData) e.next();
            System.out.println("  Adding :" + data.getname() + "...");
            i++;

            // Se obtiene la ruta al archivo en cuestion, a fin de que el mismo
            // pueda ser indexado.
            String fileName = data.getid() + "-" + data.getname();
            String subdir = "uploads" + File.separator;
            if (data.getproject() > 0) {
                subdir = subdir + "PR" + data.getproject() + File.separator;
                if (data.gettask() > 0) {
                    subdir = subdir + "TK" + data.getid() +
                            File.separator;
                } else if (data.gettopic() > 0) {
                    subdir = subdir + "TP" + data.getid() +
                            File.separator;
                }
            }

            String fullpath = context.getRealPath(subdir + fileName);

            // Contenido del archivo a ser indexado
            String contenido = "";

            // Se obtiene el tipo del archivo
            String type = data.getPrefixType();
            try {
             /*   if (type.equals("doc")) {
                    FileInputStream in = new FileInputStream(fullpath);
                    contenido = extractor.extractText(in);
                    in.close();
                } else if (type.equals("pdf")) {
                    FileInputStream in = new FileInputStream(fullpath);
                    contenido = LucenePDFDocument.getDocument(in).toString();
                    in.close();
                } else if (type.equals("txt") || (type.equals("php") || type.equals("js") || type.equals("xls"))) {
                    BufferedReader in2 = new BufferedReader(new FileReader(fullpath));

                    StringBuffer content = new StringBuffer();
                    contenido = "";
                    while (contenido != null) {
                        content.append(contenido + " ");
                        contenido = in2.readLine();
                    }
                    in2.close();
                    contenido = content.toString();
                } else {*/
                    // Si no es formato entendible, se procede a indexar solo
                    // su nombre, tipo y comentarios
/*                    contenido = data.getname() + " " + type + " " +
                            data.getcomments();
                //}
            } catch (Exception er) {
                System.out.println("Fail Reading File During indexation: " +
                        er.toString());
                logger.logp(Level.SEVERE, "searchAction.do", "perform",
                        "Fatal Error: " + er.toString());

                contenido = data.getname() + " " + data.getcomments();
            }

            try {
                // Se agrega al index
                writer.addDocument(TMSDocument.Document(data.getid(),
                        "7",
                        data.getname(), data.getcomments(), contenido));
            } catch (Exception e2) {
                System.out.println("Error Indexing Schedule.." + data.getid());
            }
        }
        return i;
    }*/
}
