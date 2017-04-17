
package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;
import java.sql.Timestamp;




import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 *
 * @author Alberto Campos
 */
public class tasksStatusLogBroker extends MainBroker {


    public java.util.Iterator getList(int id) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));
        // Query of all the tasks
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result to print each Service
        return allLines.iterator();
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
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        // now iterate over the result 
        return allLines.iterator();
    }

    public java.util.Iterator getList(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
        criteria.addEqualTo("id_account", new Integer(accountId));
        // Query of all the tasks
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }
//
//    public int getOrganization(int idProject, int idAccount) {
//        Criteria criteria = new Criteria();
//        projectsData projectsData = new projectsData();
//        // Se obtiene el iterador sobre todos los elementos de la lista.
//        criteria.addEqualTo("id", Integer.valueOf("" + idProject));
//        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
//        Query query = new QueryByCriteria(projectsData.class, criteria);
//
//        // Debemos convertir el iterador en un arraylist.
//        ArrayList lista = new ArrayList();
//        Collection allLines = broker.getCollectionByQuery(query);
//        Iterator e = allLines.iterator();
//        if (e.hasNext()) {
//            projectsData = (projectsData) e.next();
//        }
//        return projectsData.getorganization();
//    }


    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {
        tasksStatusLogData data = new tasksStatusLogData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of the exact organization
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (tasksStatusLogData) e.next();
        }
        // We return the object
        return data;
    }

    // Return the object associated with the key.
    public mainData getData(int id) {
        tasksStatusLogData data = new tasksStatusLogData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));

        // Query of the exact organization
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (tasksStatusLogData) e.next();
        }
        // We return the object
        return data;
    }

    /* Regresa una lista de todos los registros de la bitcota de una tarea en especifico */
    public java.util.Iterator getListByTasks(int task, int idAccount, String order) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("task", Integer.valueOf("" + task));
        // Se agrega el criteria por Cuenta.
        criteria.addEqualTo("ID_ACCOUNT", Integer.valueOf("" + idAccount));

        if (order.equals("DESC")){
            criteria.addOrderByDescending("created");
        }else{
            criteria.addOrderByAscending("created");
        }
        // Query of all the tasks
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        return allLines.iterator();
    }
    
        public void deleteAllReferencesbyTask(int taskId,int accountId) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();
        
        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));
        criteria.addEqualTo("task", Integer.valueOf("" + taskId));
        // Query of all the teams
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);          
        Iterator e = allLines.iterator();
        // Se borra cada entrada
            while (e.hasNext()) {
                this.delete(e.next());
            }
        }
        
            /*
     * Metodo que elimina todas las referencias de las tareas de un proyecto
     */
    public void deleteAllReferencesByProject(int projectId, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();
        // Se agrega el criteria por el due?o
        criteria.addEqualTo("project", Integer.valueOf("" + projectId));
        // Se agrega el criteria por el due?o
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query of all the teams
        Query query = new QueryByCriteria(tasksData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();
        // Se borra cada entrada
        tasksData taskDel;
        while (e.hasNext()) {
            taskDel = (tasksData) e.next();
            criteria = new Criteria();
            
             // Se agrega el criteria por la tarea
            criteria.addEqualTo("task", Integer.valueOf("" + taskDel.getid()));
         // Se agrega el criteria por la cuenta
            criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
            
            query = new QueryByCriteria(tasksStatusLogData.class, criteria);
             // ask the broker to retrieve the Extent collection
            Collection allLines1 = broker.getCollectionByQuery(query);

            Iterator e1 = allLines1.iterator();
            // Se borra cada entrada
            tasksStatusLogData tasksLog;
            while (e1.hasNext()) {
                tasksLog= (tasksStatusLogData) e1.next();
                this.delete(tasksLog);
            }
            
        }
    }
    
        public void deleteAllReferences(int memberId, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por el due?o
        criteria.addEqualTo("member", Integer.valueOf("" + memberId));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(tasksStatusLogData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        tasksStatusLogBroker tslBroker = new tasksStatusLogBroker();

        // Se borra cada entrada
        tasksStatusLogData tasksl;
        while (e.hasNext()) {
            tasksl = (tasksStatusLogData) e.next();

            // Se borran los registros de la bitacora
            tslBroker.deleteAllReferencesbyTask(tasksl.getid(), idAccount);

            this.delete(tasksl);
        }
        tslBroker.close();

        // Despu?s de borrar todas las tareas que son de proyectos donde el usuario es el
        // el due?o, se toman las tareas asignadas a l, de proyectos de otros usuarios, donde el no es el dueno, y se
        // reasignan estas a el dueno del proyecto. PATO

        Criteria criteria2 = new Criteria();
        criteria2.addEqualTo("assigned_to", Integer.valueOf("" + memberId));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query de las tareas signadas al usuario donde el NO es el dueno del proyecto
        Query query2 = new QueryByCriteria(tasksData.class, criteria2);

        // ask the broker to retrieve the Extent collection
        Collection allLines2 = broker.getCollectionByQuery(query2);

        Iterator w = allLines2.iterator();

        // Se reasigna cada tarea al dueno del proyecto
        tasksData taskn;
        while (w.hasNext()) {
            taskn = (tasksData) w.next();
            taskn.setassigned_to(taskn.getowner());
            taskn.setname("REF. " + taskn.getparentAssigned().getlogin() + ": " + taskn.getname());
            this.update(taskn);
        }

    }

}