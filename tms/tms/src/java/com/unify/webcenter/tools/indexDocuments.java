/*
 * indexDocuments.java
 *
 * Created on May 28, 2003, 8:26 AM
 */
package com.unify.webcenter.tools;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.pdfbox.searchengine.lucene.LucenePDFDocument;

import org.textmining.text.extraction.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.index.IndexWriter;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;
import com.unify.webcenter.conf.*;
import com.unify.webcenter.search.*;

/**
 * Clase que se encarga de indexar todos los documentos y demas elementos del
 * Task Management System.
 *
 * @author  Gerardo Arroyo Arce
 */
public class indexDocuments {

    private static Logger logger = Logger.getLogger("com.unify");

    /** Creates a new instance of indexDocuments */
    public indexDocuments() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        indexDocuments indexer = new indexDocuments();

        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.0 Indexer Subsystem");
        System.out.println("-----------------------------------");
        indexer.indexInfo();
        System.out.println("Done");
    }

    /**
     *  Metodo que se encarga de llevar a cabo la indexacion de la informacion de la base
     * de datos.
     */
    private void indexInfo() {
        try {
            // Se extran todas las tareas.
            tasksBroker taskBroker = new tasksBroker();
            Iterator eTask = taskBroker.getList("id", "ASC");
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
            indexDocsTasks(writerTasks, eTask);


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
            Iterator eProject = projBroker.getList("id", "ASC");
            projBroker.close();
            indexDocsProjects(writerProjects, eProject);

            // Se optimiza el indice y se cierra
            writerProjects.optimize();
            writerProjects.close();

            /******************************************************************
            PAS0 #03  INDEXACION DE PROJECTS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerMembers = new IndexWriter(path + "indexMembers", new StandardAnalyzer(), true);

            System.out.println(" 3.Indexing Members...");

            // Se procede con la indexacion
            membersBroker memBroker = new membersBroker();
            Iterator eMembers = memBroker.getList("id", "ASC");
            memBroker.close();
            indexDocsMembers(writerMembers, eMembers);

            // Se optimiza el indice y se cierra
            writerMembers.optimize();
            writerMembers.close();


            /******************************************************************
            PAS0 #04  INDEXACION DE CALENDARS
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerCalendar = new IndexWriter(path + "indexCalendars", new StandardAnalyzer(), true);

            System.out.println(" 4.Indexing Calendars...");

            // Se procede con la indexacion
            calendarBroker calBroker = new calendarBroker();
            Iterator eCalendar = calBroker.getList("id", "ASC");
            calBroker.close();
            indexDocsCalendars(writerCalendar, eCalendar);

            // Se optimiza el indice y se cierra
            writerCalendar.optimize();
            writerCalendar.close();


            /******************************************************************
            PAS0 #05  INDEXACION DE SCHEDULES
             *******************************************************************/
            // Se crea un nuevo indice, con base en un StandardAnalyzer, se sobreescribe
            // el indice anterior si es que existe.
            IndexWriter writerSchedules = new IndexWriter(path + "indexSchedules", new StandardAnalyzer(), true);

            System.out.println(" 5.Indexing Schedules...");

            // Se procede con la indexacion
            schedulesBroker scheduleBroker = new schedulesBroker();
            Iterator eSchedules = scheduleBroker.getList("taskid", "ASC");
            scheduleBroker.close();
            indexDocsSchedules(writerSchedules, eSchedules);

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
            Iterator ePosts = postsBroker.getList("id", "ASC");
            postsBroker.close();
            indexDocsPosts(writerPosts, ePosts);

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
            Iterator eFiles = fileBroker.getList("id", "ASC");
            fileBroker.close();
            indexDocsFiles(writerFiles, eFiles);

            // Se optimiza el indice y se cierra
            writerFiles.optimize();
            writerFiles.close();




            // Fecha de Terminacion de indexacion
            Date end = new Date();

            // Se despliega la cantidad de tiempo invertido en el proceso
            System.out.print("Done in: ");
            System.out.print(end.getTime() - start.getTime());
            System.out.println(" total milliseconds.");


        } catch (Exception e) {
            System.out.println(" Caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            logger.logp(Level.SEVERE, "searchAction.do", "perform",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * tareas
     */
    public static int indexDocsTasks(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        tasksData data;
        while (e.hasNext()) {
            data = (tasksData) e.next();
            System.out.println("  Adding 6:" + data.getid() + "...");
            i++;
            try {
                // Se agrega al index
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
    public static int indexDocsProjects(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        projectsData data;
        while (e.hasNext()) {
            data = (projectsData) e.next();
            System.out.println("  Adding 7:" + data.getid() + "...");
            i++;
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
    public static int indexDocsMembers(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        membersData data;
        while (e.hasNext()) {
            data = (membersData) e.next();
            System.out.println("  Adding 8:" + data.getid() + "...");

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
    public static int indexDocsCalendars(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        calendarData data;
        while (e.hasNext()) {
            data = (calendarData) e.next();
            System.out.println("  Adding 9:" + data.getid() + "...");
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
                System.out.println("Error Indexing Calendar.." + data.getid());
            }
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * schedules
     */
    public static int indexDocsSchedules(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        schedulesData data;
        while (e.hasNext()) {
            data = (schedulesData) e.next();
            System.out.println("  Adding 10:" + data.getid() + "...");
            i++;
            try {
                // Se agrega al index
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
    public static int indexDocsPosts(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        postData data;
        while (e.hasNext()) {
            data = (postData) e.next();
            System.out.println("  Adding 11:" + data.getid() + "...");
            i++;
            try {
                // Se agrega al index
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
                System.out.println("Error Indexing Post.." + data.getid());
            }
        }
        return i;
    }

    /* Mètodo que se encarga de llevar a cabo la indexacion de la base de datos de las
     * files
     */
    public static int indexDocsFiles(IndexWriter writer, Iterator e) throws Exception {
        int i = 0;
        filesData data;

        // Empleados para extraer el cuerpo del archivo de tipo DOC y PDF
        WordExtractor extractor = new WordExtractor();


        while (e.hasNext()) {
            data = (filesData) e.next();
            System.out.println("  Adding 12:" + data.getname() + "...");
            i++;


            // Se obtiene la ruta al archivo en cuestion, a fin de que el mismo
            // pueda ser indexado.
            String fileName = data.getid() + "-" + data.getname();

            // La ruta donde quedan los files esta dada en el configurador
            String subdir = TMSConfigurator.getDownloadPath() + File.separator;

            if (!subdir.endsWith(File.separator)) {
                subdir = subdir + File.separator;
            }

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

            // Ya va con la ruta bien formada
            String fullpath = subdir;
            fullpath += fileName;

            // Contenido del archivo a ser indexado
            String contenido = "";

            // Se obtiene el tipo del archivo
            String type = data.getPrefixType();
            try {
                if (type.equals("doc")) {
                    FileInputStream in = new FileInputStream(fullpath);
                    contenido = extractor.extractText(in);
                    in.close();
                } else if (type.equals("pdf")) {
                    FileInputStream in = new FileInputStream(fullpath);
                ///    contenido = LucenePDFDocument.getDocument(in).toString();
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
                } else {
                    // Si no es formato entendible, se procede a indexar solo
                    // su nombre, tipo y comentarios
                    contenido = data.getname() + " " + type + " " +
                            data.getcomments();
                }
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
                System.out.println("Error Indexing Files.." + data.getid());
            }
        }
        return i;
    }
}
