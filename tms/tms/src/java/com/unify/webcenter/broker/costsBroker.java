/*
 * costsBroker.java
 *
 * Created on April 4, 2003, 12:10 PM
 */
package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;
import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;

/**
 * Clase que se encarga de abstraer el Broker de la tabla de Costs
 *
 * @author  Gerardo Arroyo Arce
 */
public class costsBroker extends MainBroker {

    /** Creates a new instance of costsBroker */
    public costsBroker() {

    }

    public com.unify.webcenter.data.mainData getData(int id, int idAccount) {
        costsData data = new costsData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of the exact organization
        Query query = new QueryByCriteria(costsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (costsData) e.next();
        }

        // We return the object
        return data;
    }
    public com.unify.webcenter.data.mainData getData(int id) {
        costsData data = new costsData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));

        // Query of the exact organization
        Query query = new QueryByCriteria(costsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (costsData) e.next();
        }

        // We return the object
        return data;
    }    
    
    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        return getList("description", "DESC", accountId);
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
        Query query = new QueryByCriteria(costsData.class, criteria);

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
        // Query of all the calendar
        Query query = new QueryByCriteria(costsData.class, criteria);


        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

 /*   public java.util.Iterator getList(String sortColumnName, String sortOrder, int accountId, int memberId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        criteria.addEqualTo("id_account", new Integer(accountId));
        // Query of all the calendar
        Query query = new QueryByCriteria(costsData.class, criteria);


        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }*/

    // Regresa una lista ordenada de todos los files que estan asociados a 
    // un proyecto
    public java.util.Iterator getListByProject(String sortColumnName,
            String sortOrder, int projectId, int page, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se filtra por la tarea asociada, nunca se despliegan todos los
        // files de forma irrestricta.
        criteria.addEqualTo("project", Integer.valueOf("" + projectId));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Query of all the files
        Query query = new QueryByCriteria(costsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        // Se modifica el total de registros retornados para efectos
        // del paginado.
        setCount(new Integer(allLines.size()));

        if (page > 0) {
            // Se determinan los limites superior e inferior
            int inicio = (page - 1) * GAP_SIZE;
            int fin = page * GAP_SIZE;

            // now iterate over the result 
            return getCollection(allLines, inicio, fin);
        } else {
            return allLines.iterator();
        }
    }
    /*
     * Metodo que elimina todas las referencias de las tareas de un proyecto
     */

    public void deleteAllReferencesByProject(int projectId, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();
        // Se agrega el criteria por el due�o
        criteria.addEqualTo("project", Integer.valueOf("" + projectId));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query of all the teams
        Query query = new QueryByCriteria(costsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();

        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }

    /**
     * Metodo que se encarga de copiar todos los costos de un proyecto origen a
     * uno destino.
     */
    public void copyAllCostsToProject(int originalProject, int targetProject, int idAccount) throws PersistenceBrokerException {
        Iterator e = this.getListByProject("id", "ASC", originalProject, 0, idAccount);
        costsData data;
        while (e.hasNext()) {
            data = new costsData((costsData) e.next());
            data.setProject(targetProject);
            this.add(data);
        }
    }
}
