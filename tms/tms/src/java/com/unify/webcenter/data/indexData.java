/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.data;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.beanutils.*;

import org.textmining.text.extraction.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.conf.*;
import com.unify.webcenter.search.TMSDocument;

/**
 *
 * @author MARCELA QUINTERO
 */
public class indexData extends mainData {

    private static Logger logger = Logger.getLogger("com.unify");

    public indexData() {
    }

    /**
     *  Metodo que se encarga de llevar a cabo la indexacion de la informacion de la base
     * de datos.
     */
    public void indexInfo(int idAccount) {
        try {
            String separator = File.separator;
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
            String path = TMSConfigurator.getIndexPath() + "ACC" + idAccount + separator;

            IndexWriter writerTasks = new IndexWriter(path + "indexTasks", new StandardAnalyzer(), true);

            System.out.println(" 1.Indexing Tasks...");

            // Se indexan todos los elementos de la base de datos, primeramente las
            // tareas
            int tt = indexDocsTasks(writerTasks, eTask);
            logger.logp(Level.SEVERE, "Indexing", "tasks",
                    "totalTasks: " + String.valueOf(tt));


            // Se optimiza el indice y se cierra
            writerTasks.optimize();
            writerTasks.close();


            /******************************************************************
            PAS0 #02  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerProjects = new IndexWriter(path + "indexProjects", new StandardAnalyzer(), true);

            System.out.println(" 2.Indexing Projects...");

            // Se procede con la indexacion
            projectsBroker projBroker = new projectsBroker();
            Iterator eProject = projBroker.getList("id", "ASC", idAccount);
            projBroker.close();
            int tp = indexDocsProjects(writerProjects, eProject);
            logger.logp(Level.SEVERE, "Indexing", "projects",
                    "totalProjects: " + String.valueOf(tp));

            // Se optimiza el indice y se cierra
            writerProjects.optimize();
            writerProjects.close();

            /******************************************************************
            PAS0 #03  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerMembers = new IndexWriter(path + "indexMembers", new StandardAnalyzer(), true);

            System.out.println(" 3.Indexing Members... b");

            // Se procede con la indexacion
            membersBroker memBroker = new membersBroker();
            Iterator eMembers = memBroker.getList("id", "ASC", idAccount);
            memBroker.close();
            int tm = indexDocsMembers(writerMembers, eMembers);
            logger.logp(Level.SEVERE, "Indexing", "members",
                    "totalMembers: " + String.valueOf(tm));

            // Se optimiza el indice y se cierra
            writerMembers.optimize();
            writerMembers.close();


            /******************************************************************
            PAS0 #04  INDEXACION DE CALENDARS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            /*IndexWriter writerCalendar = new IndexWriter(path + "indexCalendars", new StandardAnalyzer(), true);

            System.out.println(" 4.Indexing Calendars...");

            // Se procede con la indexacion
            calendarBroker calBroker = new calendarBroker();
            Iterator eCalendar = calBroker.getList("id", "ASC", idAccount);
            calBroker.close();
            int tc = indexDocsCalendars(writerCalendar, eCalendar);
            logger.logp(Level.SEVERE, "Indexing", "calendar",
                    "totalCalendars: " + String.valueOf(tc));
            // Se optimiza el indice y se cierra
            writerCalendar.optimize();
            writerCalendar.close();


            /******************************************************************
            PAS0 #05  INDEXACION DE SCHEDULES
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            /*IndexWriter writerSchedules = new IndexWriter(path + "indexSchedules", new StandardAnalyzer(), true);

            System.out.println(" 5.Indexing Schedules...");

            // Se procede con la indexacion
            schedulesBroker scheduleBroker = new schedulesBroker();
            Iterator eSchedules = scheduleBroker.getList("taskid", "ASC", idAccount);
            scheduleBroker.close();
            int ts = indexDocsSchedules(writerSchedules, eSchedules);
            logger.logp(Level.SEVERE, "Indexing", "schedules",
                    "totalSchedules: " + String.valueOf(ts));
            // Se optimiza el indice y se cierra
            writerSchedules.optimize();
            writerSchedules.close();


            /******************************************************************
            PAS0 #06  INDEXACION DE POSTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerPosts = new IndexWriter(path + "indexPosts", new StandardAnalyzer(), true);

            System.out.println(" 6.Indexing Posts...");

            // Se procede con la indexacion
            postBroker postsBroker = new postBroker();
            Iterator ePosts = postsBroker.getList("id", "ASC", idAccount);
            postsBroker.close();
            int tps = indexDocsPosts(writerPosts, ePosts);
            logger.logp(Level.SEVERE, "Indexing", "posts",
                    "totalPosts: " + String.valueOf(tps));
            // Se optimiza el indice y se cierra
            writerPosts.optimize();
            writerPosts.close();

            /******************************************************************
            PAS0 #07  INDEXACION DE FILES
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerFiles = new IndexWriter(path + "indexFiles", new StandardAnalyzer(), true);

            System.out.println(" 7.Indexing Files...");

            // Se procede con la indexacion
            filesBroker fileBroker = new filesBroker();
            Iterator eFiles = fileBroker.getList("id", "ASC", idAccount);
            fileBroker.close();
            int tf = indexDocsFiles(writerFiles, eFiles);
            logger.logp(Level.SEVERE, "Indexing", "files",
                    "totalFiles: " + String.valueOf(tf));
            // Se optimiza el indice y se cierra
            writerFiles.optimize();
            writerFiles.close();




            // Fecha de Terminacion de indexacion
            Date end = new Date();

            // Se despliega la cantidad de tiempo invertido en el proceso
            System.out.print("Done in: ");
            System.out.print(end.getTime() - start.getTime());
            System.out.println(" total milliseconds.");

            logger.logp(Level.SEVERE, "Indexing", "IndexDocs", "totalTime", "" + (end.getTime() - start.getTime()));

        } /*catch (Exception e) {
            System.out.println(" Caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            logger.logp(Level.SEVERE, "Indexing", "IndexDocs",
                    "Fatal Error: " + e.toString() );*/
            catch (NullPointerException e){
                 System.out.println(" Caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            logger.logp(Level.SEVERE, "Indexing", "NULL POINTER",
                    "Fatal Error: " + e.toString() );
            }catch (Exception e) {
                 System.out.println(" Caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            logger.logp(Level.SEVERE, "Indexing", "IndexDocs",
                    "Fatal Error: " + e.toString() );
            
        }
    }

    public   void indexInfo(int idAccount, HttpServletRequest request) {
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
            String path = TMSConfigurator.getIndexPath();

            IndexWriter writerTasks = new IndexWriter(path + "indexTasks", new StandardAnalyzer(), true);

            System.out.println(" 1.Indexing Tasks...");

            // Se indexan todos los elementos de la base de datos, primeramente las
            // tareas
            request.setAttribute("totalTasks", "" + indexDocsTasks(writerTasks, eTask));


            // Se optimiza el indice y se cierra
            writerTasks.optimize();
            writerTasks.close();


            /******************************************************************
            PAS0 #02  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerProjects = new IndexWriter(path + "indexProjects", new StandardAnalyzer(), true);

            System.out.println(" 2.Indexing Projects...");

            // Se procede con la indexacion
            projectsBroker projBroker = new projectsBroker();
            Iterator eProject = projBroker.getList("id", "ASC", idAccount);
            projBroker.close();
            request.setAttribute("totalProjects", "" + indexDocsProjects(writerProjects, eProject));

            // Se optimiza el indice y se cierra
            writerProjects.optimize();
            writerProjects.close();

            /******************************************************************
            PAS0 #03  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerMembers = new IndexWriter(path + "indexMembers", new StandardAnalyzer(), true);

            System.out.println(" 3.Indexing Members... a");

            // Se procede con la indexacion
            membersBroker memBroker = new membersBroker();
            Iterator eMembers = memBroker.getList("id", "ASC", idAccount);
            memBroker.close();
            request.setAttribute("totalMembers", "" + indexDocsMembers(writerMembers, eMembers));

            // Se optimiza el indice y se cierra
            writerMembers.optimize();
            writerMembers.close();


            /******************************************************************
            PAS0 #04  INDEXACION DE CALENDARS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            /*IndexWriter writerCalendar = new IndexWriter(path + "indexCalendars", new StandardAnalyzer(), true);

            System.out.println(" 4.Indexing Calendars...");

            // Se procede con la indexacion
            calendarBroker calBroker = new calendarBroker();
            Iterator eCalendar = calBroker.getList("id", "ASC", idAccount);
            calBroker.close();
            request.setAttribute("totalCalendars", "" + indexDocsCalendars(writerCalendar, eCalendar));

            // Se optimiza el indice y se cierra
            writerCalendar.optimize();
            writerCalendar.close();


            /******************************************************************
            PAS0 #05  INDEXACION DE SCHEDULES
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            /*IndexWriter writerSchedules = new IndexWriter(path + "indexSchedules", new StandardAnalyzer(), true);

            System.out.println(" 5.Indexing Schedules...");

            // Se procede con la indexacion
            schedulesBroker scheduleBroker = new schedulesBroker();
            Iterator eSchedules = scheduleBroker.getList("taskid", "ASC", idAccount);
            scheduleBroker.close();
            request.setAttribute("totalSchedules", "" + indexDocsSchedules(writerSchedules, eSchedules));

            // Se optimiza el indice y se cierra
            writerSchedules.optimize();
            writerSchedules.close();

            */
            /******************************************************************
            PAS0 #06  INDEXACION DE POSTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerPosts = new IndexWriter(path + "indexPosts", new StandardAnalyzer(), true);

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
            IndexWriter writerFiles = new IndexWriter(path + "indexFiles", new StandardAnalyzer(), true);

            System.out.println(" 7.Indexing Files...");

            // Se procede con la indexacion
            filesBroker fileBroker = new filesBroker();
            Iterator eFiles = fileBroker.getList("id", "ASC", idAccount);
            fileBroker.close();
            request.setAttribute("totalFiles", "" + indexDocsFiles(writerFiles, eFiles));

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
            logger.logp(Level.SEVERE, "indexData", "index Info",
                    "Fatal Error: " + e.toString());

        }
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * tareas
     */
    private static int indexDocsTasks(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        tasksData data;
        while (e.hasNext()) {
            data = (tasksData) e.next();
            System.out.println("  Adding 1:" + data.getid() + "...");
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
    private static int indexDocsProjects(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        projectsData data;
        while (e.hasNext()) {
            data = (projectsData) e.next();
            System.out.println("  Adding 2:" + data.getid() + "...");
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
    private static int indexDocsMembers(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        membersData data;
        while (e.hasNext()) {
            data = (membersData) e.next();
            System.out.println("  Adding 3:" + data.getid() + " | "+data.getname()+ " | "+data.gettitle()+ " | "+data.getcomments()+"...");
            try{
            // Se agrega al index
            writer.addDocument(TMSDocument.Document(data.getid(),
                    "2",
                    data.getname(),  data.gettitle() == null ? "" : new String(data.gettitle()) + " - " + data.getname()+ "( " +  data.getcomments() == null ? "" : new String(data.getcomments()) + " )", data.toString()));
            }catch (NullPointerException e2) {
                System.out.println("ERROR NOMBRE ARCH " + data.getid()+ e2.getMessage());
            }
            i++;
            System.out.println(" PASO ...");
            
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * calendars
     */
   /* private static int indexDocsCalendars(IndexWriter writer, Iterator e) throws Exception {
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
 /*   private static int indexDocsSchedules(IndexWriter writer, Iterator e) throws Exception {
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
    private static int indexDocsPosts(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        postData data;
        while (e.hasNext()) {
            data = (postData) e.next();
            System.out.println("  Adding 4:" + data.getid() + "...");
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
    private static int indexDocsFiles(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        filesData data;

        while (e.hasNext()) {
            data = (filesData) e.next();
            System.out.println("  Adding 5:" + data.getname() + "...");
            i++;
            // Contenido del archivo a ser indexado
            String contenido = "";

            // Se obtiene el tipo del archivo
            String type = data.getPrefixType();
            try {
                // indexa por su nombre, tipo y comentarios
                contenido = data.getname() + " " + type + " " +
                        data.getcomments();

            } catch (Exception er) {
                System.out.println("Fail Reading File During indexation: " +
                        er.toString());
                logger.logp(Level.SEVERE, "Indexing", "IndexDocsFile",
                        "Fatal Error: " + er.toString());

                contenido = data.getname() + " " + data.gettype() + " " + data.getcomments();
            }

            try {
                // Se agrega al index
                writer.addDocument(TMSDocument.Document(data.getid(),
                        "7",
                        data.getname(), data.getcomments(), contenido));
            } catch (Exception e2) {
                System.out.println("Error Indexing File.." + data.getid());
            }
        }
        return i;
    }
}
