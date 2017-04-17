/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.broker;
import com.unify.webcenter.data.*;
import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;

/**
 *
 * @author MARCELA QUINTERO
 */
public class masterReportsBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addOrderBy("id");
        // Query of all the assignments
        Query query = new QueryByCriteria(masterReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result to print each Service
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
        // Query of all the assignments
        Query query = new QueryByCriteria(masterReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
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
        Query query = new QueryByCriteria(masterReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }
    
    // Return the object associated with the key.
    public mainData getData(int id) {
   masterReportData data = new masterReportData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));
        // Query of the exact organization
        Query query = new QueryByCriteria(masterReportData.class, criteria);
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (masterReportData) e.next();
        }

        // We return the object
        return data;
    }

    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {
        return getData(id);
    }
    
            /* Ejecuta el reporte de tareas de acuerdo a los parametros adecuados */
    public java.util.Iterator getMasterReportByConfiguration(int members,            
            String startDate1, String startDate2,
            String sortColumnName, String sortOrder,
            int idAccount) {

              // Se forma un criteria.
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        criteria.addEqualTo("parentTask.id_account", Integer.valueOf("" + idAccount));

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }


        // Si se especifica un rango de fechas de inicio
        criteria.addGreaterOrEqualThan("day", startDate1);
        criteria.addLessOrEqualThan("day", startDate2);       
      
        // se agrega el criterio de miembros
        criteria.addEqualTo("userid", new Integer(members));
        // se agrega el criterio de miembros
        criteria.addEqualTo("parentTask.assigned_to", new Integer(members));
     
        criteria.addOrderByAscending(sortColumnName);
        // Se extraen todas las tareas que satisfagan los criterios especificados
        Query query = new QueryByCriteria(schedulesData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

}
