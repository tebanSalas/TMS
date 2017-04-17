/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.broker;

import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.data.*;

import com.unify.webcenter.tools.StringManipulator;
import java.io.File;
import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;

/**
 *
 * @author MARCELA QUINTERO
 */
public class accountsBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(accountId));
        // Query of all the assignments
        Query query = new QueryByCriteria(accountsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

   //     broker.close();
        // now iterate over the result to print each Service
        return allLines.iterator();
    }

    public java.util.Iterator getList(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
        return getList(sortColumnName, sortOrder);
    }

    public java.util.Iterator getList(String sortColumnName, String sortOrder) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Query of all the assignments
        Query query = new QueryByCriteria(accountsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
       // broker.close();

        // now iterate over the result 
        return allLines.iterator();
    }

    // Return the object associated with the key.
    public mainData getData(int id) {
        accountsData data = new accountsData();
        Criteria criteria = new Criteria();
        System.out.println("ACCOUNT GET");
        criteria.addEqualTo("id", new Integer(id));

        // Query of the exact organization
        Query query = new QueryByCriteria(accountsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
       
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (accountsData) e.next();
        }

        // We return the object
        return data;
    }
    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {
        return getData(id);
    }


    /* Metodo que se encarga de verificar si un login ya existe en la base de datos
     * esto a fin de evitar que posteriormente se haga un ingreso de ids dobles
     * @return true si existe ese login
     */
    public boolean exists(String login) {
        boolean existe = true;

        System.out.println("ACC6");
        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("login", login);

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
    //    broker.close();

        if (allLines.isEmpty()) {
            existe = false;
        }

        return existe;
    }

    /*
     * Metodo que elimina todas las referencias de un miembro en la base de datos
     */
    public void deleteAllReferences(accountsData data) throws PersistenceBrokerException {
        
        /* FASE 5: Borra LOS POSTS */
        postBroker pstBroker = new postBroker();
        pstBroker.deleteAllReferencesbyAccount(data.getId());
        pstBroker.close();
        
        /* FASE 6: TRANSFERIR LOS TOPICS */
        topicsBroker topicBroker = new topicsBroker();
        topicBroker.deleteAllReferencesbyAccount(data.getId());
        topicBroker.close();
        
  /* FASE 4: Borra LOS FILES */
        filesBroker fileBroker = new filesBroker();
        fileBroker.deleteAllReferencesbyAccount(data.getId());
        fileBroker.close();
        
        /* FASE 0: BORRAR  TEAMS */
        teamsBroker teamBroker = new teamsBroker();
        teamBroker.deleteAllReferencesbyAccount(data.getId());
        teamBroker.close();
        
        /* FASE 1: BORRAR  MEMBERS */
        membersBroker memberBroker = new membersBroker();
        memberBroker.deleteAllReferencesbyAccount(data.getId());
        memberBroker.close();
        
        /* FASE 2: BORRAR  REPORTS */
        reportsBroker reportBroker = new reportsBroker();
        reportBroker.deleteAllReferencesbyAccount(data.getId());
        reportBroker.close();
        
        /* FASE 3: BORRAR  CALENDARS */
        calendarBroker calBroker = new calendarBroker();
        calBroker.deleteAllReferencesbyAccount(data.getId());
        calBroker.close();
        
        /* FASE 3: BORRAR  SCHEDULES */
        schedulesBroker scheBroker = new schedulesBroker();
        scheBroker.deleteAllReferencesbyAccount(data.getId());
        scheBroker.close();
      
        
        /* FASE 7: Si el administrador desea borrar las tareas se aplica}
         * el delete, o en su defecto un transfer 
         */
        tasksBroker brokerTask = new tasksBroker();
        brokerTask.deleteAllReferencesbyAccount(data.getId());
        brokerTask.close();
        
        /* FASE 8: Borrar LOS PROYECTOS */
              projectsBroker projBroker = new projectsBroker();
        projBroker.deleteAllReferencesbyAccount(data.getId());
        projBroker.close();
        /* FASE 9: BORRAR  Assignments */
        assignmentsBroker assignmentBroker = new assignmentsBroker();
        assignmentBroker.deleteAllReferencesbyAccount(data.getId());
        assignmentBroker.close();
        /* FASE 10: BORRAR  Organizations */
        organizationsBroker organizationBroker = new organizationsBroker();
        organizationBroker.deleteAllReferencesbyAccount(data.getId());
        organizationBroker.close();

        /* FASE 11: BORRAR Type  Tasks */
        type_tasksBroker typetasksBroker = new type_tasksBroker();
        typetasksBroker.deleteAllReferencesbyAccount(data.getId()) ;
        typetasksBroker.close();

        /* FASE 12: BORRAR Config Send Report*/
        configSendReportBroker csrBroker= new configSendReportBroker();
        csrBroker.deleteAllReferencesbyAccount(data.getId());
        csrBroker.close();

        String separator = File.separator;
        String sub = TMSConfigurator.getDownloadPath();
        String[] splitSub = sub.split("ACC");
        if (splitSub.length > 1) {
            sub = splitSub[0] + "ACC" + data.getId() + separator;
        }


        // Si el string no termina con el separador de files, se agrgega

        if (!sub.endsWith(separator)) {
            sub = sub + separator;
        }

        String fullpath = "";

        String subdir = "";

        File theFile;



        // delete each file entry in DB and your file asociated.
        String fileName = "";
        // Se borra cada entrada

        subdir = sub;
//        fileName = StringManipulator.generateValidFilename(data.getAccount_logo());


        // Se forma la ruta del archivo fisico

        fullpath = subdir + fileName;

fullpath= fullpath.replace("\\", "/");
System.out.println(subdir);

        theFile = new File(fullpath);

        theFile.delete();


        this.delete(data);


    }
}
