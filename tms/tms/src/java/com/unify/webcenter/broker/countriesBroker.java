//   Generated by FlechaRoja Tech Tools (2003) 

package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;

import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;

/**
* Class that represent the broker for the table countries
* @author Administrator
*/
public class countriesBroker  extends MainBroker {

	public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
          // New criteria for sortering
        Criteria criteria = new Criteria();
		// Query of all the countries
		Query query = new QueryByCriteria(countriesData.class, criteria);

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
        Query query = new QueryByCriteria(countriesData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

	public java.util.Iterator getList(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
		// New criteria for sortering
		Criteria criteria = new Criteria();

		// We order the result set
		if (sortOrder.equalsIgnoreCase("ASC"))
			criteria.addOrderByAscending(sortColumnName);
		else
			criteria.addOrderByDescending(sortColumnName);

		// Query of all the countries
		Query query = new QueryByCriteria(countriesData.class, criteria);

		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);

		// now iterate over the result 
		return allLines.iterator();
	}

       public com.unify.webcenter.data.mainData getData(int id) {
        countriesData data = new countriesData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));

        // Query of the exact organization
        Query query = new QueryByCriteria(countriesData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (countriesData) e.next();
        }

        // We return the object
        return data;
    }    
	// Return the object associated with the key.
	public mainData getData(int id, int idAccount) {
		countriesData data = new countriesData();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("id", new Integer(id));

		// Query of the exact organization
		Query query = new QueryByCriteria(countriesData.class, criteria);

		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);
		Iterator e = allLines.iterator();
		// If exists the record -MUST EXISTS ALWAYS
		if (e.hasNext())
			data = (countriesData) e.next();

		// We return the object
		return data;
	}



 }