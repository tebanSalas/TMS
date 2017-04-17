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
public class configSendReportBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addOrderBy("members");
        // Query of all the assignments
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result to print each Service
        return allLines.iterator();
    }
   public java.util.Iterator getList(String sortColumnName, String sortOrder, int id_report) throws PersistenceBrokerException {
          // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
        criteria.addEqualTo("id_master_report", new Integer(id_report));
       
        // Query of all the assignments
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

    public java.util.Iterator getList(String sortColumnName, String sortOrder, int id_report, int idAccount) throws PersistenceBrokerException {
          // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
        criteria.addEqualTo("id_master_report", new Integer(id_report));
        criteria.addEqualTo("id_account", new Integer(idAccount));
        // Query of all the assignments
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

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
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }
    // Return the object associated with the key.
    public mainData getData(int id) {
        return null;
    }

    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {
        return null;
    }

    // Return the object associated with the key.
    public mainData getData(int idaccount, int member, int idreport) {
        configSendReportData data = new configSendReportData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account", new Integer(idaccount));
        criteria.addEqualTo("id_master_report", new Integer(idreport));
        criteria.addEqualTo("members", new Integer(member));
        // Query of the exact organization
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (configSendReportData) e.next();
        }

        // We return the object
        return data;
    }
    
     public java.util.Iterator getDailyList(String sortColumnName, String sortOrder, String periodicity, int id) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
        criteria.addEqualTo("periodicity",periodicity);
                criteria.addEqualTo("id_master_report",new Integer(id));

        // Query of all the assignments
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

    public void deleteAllReferencesbyAccount(int id)  throws PersistenceBrokerException {

           // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("ID_ACCOUNT",new Integer(id));

        // Query of all the teams
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // Se borran cada entrada y se reapunta al duenno del proyecto.
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }
    
    
    public void deleteAllReferencesbyMember(int id, int account)  throws PersistenceBrokerException {

           // New criteria for searcher
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("members",new Integer(id));
        criteria.addEqualTo("id_account",new Integer(account));

        // Query of all the teams
        Query query = new QueryByCriteria(configSendReportData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // Se borran cada entrada y se reapunta al duenno del proyecto.
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }
}
