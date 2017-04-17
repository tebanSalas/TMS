//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;

import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;

/**
 * Class that represent the broker for the table assignments
 * @author Administrator
 */
public class assignmentsBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account", new Integer(accountId));

        // Query of all the assignments
        Query query = new QueryByCriteria(assignmentsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result to print each Service
        return allLines.iterator();
    }

    	public java.util.Iterator getList(String sortColumnName, String sortOrder) throws PersistenceBrokerException {
    // New criteria for sortering
    Criteria criteria = new Criteria();
    // We order the result set
    if (sortOrder.equalsIgnoreCase("ASC"))
    criteria.addOrderByAscending(sortColumnName);
    else
    criteria.addOrderByDescending(sortColumnName);
    // Query of all the assignments
    Query query = new QueryByCriteria(assignmentsData.class, criteria);
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
        // Query of all the assignments
        Query query = new QueryByCriteria(assignmentsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {
        assignmentsData data = new assignmentsData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));
        criteria.addEqualTo("id_account", new Integer(idAccount));

        // Query of the exact organization
        Query query = new QueryByCriteria(assignmentsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (assignmentsData) e.next();
        }

        // We return the object
        return data;
    }

    // Return the object associated with the key.
    public mainData getData(int id) {
        assignmentsData data = new assignmentsData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));


        // Query of the exact organization
        Query query = new QueryByCriteria(assignmentsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (assignmentsData) e.next();
        }

        // We return the object
        return data;
    }

    // Regresa una lista ordenada de todos los files que estan asociados a 
    // una tarea.
    public java.util.Iterator getList(String sortColumnName,
            String sortOrder, int parentTask, int page, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se filtra por la tarea asociada, nunca se despliegan todos los
        // files de forma irrestricta.
        criteria.addEqualTo("task", Integer.valueOf("" + parentTask));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Query of all the files
        Query query = new QueryByCriteria(assignmentsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // Se modifica el total de registros retornados para efectos
        // del paginado.
        setCount(new Integer(allLines.size()));

        // Se determinan los limites superior e inferior
        int inicio = (page - 1) * GAP_SIZE;
        int fin = page * GAP_SIZE;

        // now iterate over the result 
        return getCollection(allLines, inicio, fin);
    }

    public void deleteAllReferencesbyAccount(int accountId) throws PersistenceBrokerException {

        // New criteria for search

        Criteria criteria = new Criteria();



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));



        // Query of all the teams

        Query query = new QueryByCriteria(assignmentsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        Iterator e = allLines.iterator();

        // Se borra cada entrada

        while (e.hasNext()) {

            this.delete(e.next());

        }

    }
    
      public void deleteAssignments(int userid, int task, int idAccount) throws PersistenceBrokerException {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("task", new Integer(task));
        criteria.addEqualTo("owner", new Integer(userid));
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of the exact organization
        Query query = new QueryByCriteria(assignmentsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }
}
