//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;

import org.apache.ojb.broker.query.*;

import org.apache.ojb.broker.*;

import java.util.*;

/**
 * Class that represent the broker for the table topics
 * @author Administrator
 */
public class topicsBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account", new Integer(accountId));
        // Query of all the topics

        Query query = new QueryByCriteria(topicsData.class, criteria);



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
        Query query = new QueryByCriteria(topicsData.class, criteria);

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

        // Query of all the topics

        Query query = new QueryByCriteria(topicsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        // Se modifica el total de registros retornados para efectos

        // del paginado.

        setCount(new Integer(allLines.size()));



        // now iterate over the result 

        return getCollection(allLines, 1, allLines.size());

    }

    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {

        topicsData data = new topicsData();

        Criteria criteria = new Criteria();

        criteria.addEqualTo("id", new Integer(id));

        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query of the exact organization

        Query query = new QueryByCriteria(topicsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (topicsData) e.next();
        }



        // We return the object

        return data;

    }

    // Return the object associated with the key.
    public mainData getData(int id) {

        topicsData data = new topicsData();

        Criteria criteria = new Criteria();

        criteria.addEqualTo("id", new Integer(id));

        // Query of the exact organization

        Query query = new QueryByCriteria(topicsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (topicsData) e.next();
        }



        // We return the object

        return data;

    }
    /* Regresa una lista de todos los topics asociados a un proyecto */

    public java.util.Iterator getListByProject(String sortColumnName,
            String sortOrder, int idProject, int page, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering

        Criteria criteria = new Criteria();



        // We order the result set

        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("project", Integer.valueOf("" + idProject));

        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query of all the topics

        Query query = new QueryByCriteria(topicsData.class, criteria);



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

    /**
     * Regresa una lista de los topics en un proyecto de acuerdo a su tipo.
     */
    public java.util.Iterator getListByProjectByStatus(String sortColumnName,
            String sortOrder, int idProject, int page, int status, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }


        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("project", Integer.valueOf("" + idProject));
        criteria.addEqualTo("status", Integer.valueOf("" + status));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the topics
        Query query = new QueryByCriteria(topicsData.class, criteria);

        // ask the broker to retrieve the Extent collectio
        Collection allLines = broker.getCollectionByQuery(query);

        // Se modifica el total de registros retornados para efecto
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

    
        /**
     * Regresa una lista de los topics en un proyecto de acuerdo a su tipo.
     */
    public java.util.Iterator getListByProject1(String sortColumnName,
            String sortOrder, int idProject, int page, int status, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }


        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("project", Integer.valueOf("" + idProject));
        criteria.addEqualTo("task", Integer.valueOf("0"));
        criteria.addEqualTo("topic", Integer.valueOf("0"));
        // Query of all the topics
        Query query = new QueryByCriteria(topicsData.class, criteria);

        // ask the broker to retrieve the Extent collectio
        Collection allLines = broker.getCollectionByQuery(query);

        // Se modifica el total de registros retornados para efecto
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

    /* Regresa una lista de todos los topics asociados a un proyecto */
    public java.util.Iterator getListByProjectForPortal(String sortColumnName,
            String sortOrder, int idProject, int page, int idAccount, int type, String status) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("project", Integer.valueOf("" + idProject));

        criteria.addEqualTo("published", "1");

        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        if (status != null ){
        criteria.addEqualTo("status", status);
        }

        // Query of all the topics

        QueryByCriteria query = new QueryByCriteria(topicsData.class, criteria);

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            query.addOrderByAscending(sortColumnName);
        } else {
            query.addOrderByDescending(sortColumnName);
        }
        if (sortColumnName.equals("status")) {
            query.addOrderByDescending("last_post");
        }
        if (type != 0) {
            switch (type) {
                case 1:
                    criteria.addEqualTo("status", new Integer("0"));
                    break;
                case 2:
                    criteria.addEqualTo("status", new Integer("1"));
                    break;
            }
        }
        
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

    /* Regresa una lista de todos los topics asociados a un miembro */
    /*   public java.util.Iterator getListByMember(String sortColumnName,
    String sortOrder, int idMember, int page) throws PersistenceBrokerException {
    // New criteria for sortering
    Criteria criteria = new Criteria();
    // We order the result set
    if (sortOrder.equalsIgnoreCase("ASC")) {
    criteria.addOrderByAscending(sortColumnName);
    } else {
    criteria.addOrderByDescending(sortColumnName);
    }
    // Se agrega el criteria por proyecto.
    criteria.addEqualTo("owner", Integer.valueOf("" + idMember));
    // Query of all the topics
    Query query = new QueryByCriteria(topicsData.class, criteria);
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
     */
    /* Regresa una lista de todos los topics asociados a un miembro */
    public java.util.Iterator getListByMember(String sortColumnName,
            String sortOrder, int idMember, int page, int accountId) throws PersistenceBrokerException {

        // New criteria for sortering

        Criteria criteria = new Criteria();



        // We order the result set

        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("owner", Integer.valueOf("" + idMember));


        criteria.addEqualTo("id_account", new Integer(accountId));


        // Query of all the topics

        Query query = new QueryByCriteria(topicsData.class, criteria);



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

    /*
     * Metodo que transfiere todos los topics del usuario dado al due�o del proyecto
     * al que pertenece.
     */
    public void transferAllReferences(int memberId, int idAccount) throws PersistenceBrokerException {

        // New criteria for search

        Criteria criteria = new Criteria();



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("owner", Integer.valueOf("" + memberId));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));


        // Query of all the teams

        Query query = new QueryByCriteria(topicsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        Iterator e = allLines.iterator();



        // Se actualiza cada entrada y se reapunta al duenno del proyecto.

        topicsData data;

        projectsData projData;

        projectsBroker projBroker = new projectsBroker();

        while (e.hasNext()) {

            data = (topicsData) e.next();

            projData = (projectsData) projBroker.getData(data.getproject(), idAccount);

            data.setowner(projData.getowner()); // Usuario admin por definicion!!

            this.update(data);

        }

        projBroker.close();

    }

    /*
     * Metodo que elimina todas las referencias de las topics/post de un proyecto
     */
    public void deleteAllReferencesByProject(int projectId, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por el due�o
        criteria.addEqualTo("project", Integer.valueOf("" + projectId));
        // Se agrega el criteria por la cuenta
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query of all the teams
        Query query = new QueryByCriteria(topicsData.class, criteria);


        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }
    }

    public void deleteAllReferencesByOwner(int memberId, int idAccount) throws PersistenceBrokerException {

        // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por el due�o
        criteria.addEqualTo("owner", Integer.valueOf("" + memberId));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(topicsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }

    }

    public void deleteAllReferencesbyAccount(int accountId) throws PersistenceBrokerException {

        // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por el due�o
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));

        // Query of all the teams
        Query query = new QueryByCriteria(topicsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // Se borra cada entrada
        while (e.hasNext()) {
            this.delete(e.next());
        }

    }

    /**
     * Regresa el total de topicos abiertos en un proyecto dado.
     */
    public int getTotalOpenTopicsByProject(int idProject, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("project", Integer.valueOf("" + idProject));
        criteria.addEqualTo("status", "1");
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the topics
        Query query = new QueryByCriteria(topicsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        return allLines.size();
    }

    /**
     * Metodo que regresa la lista de todos las discusiones que estan abiertas
     * para un proyecto determinado.
     */
    public java.util.Iterator getListByProjectOpen(String sortColumnName,
            String sortOrder, int idProject) throws PersistenceBrokerException {

        // New criteria for sortering

        Criteria criteria = new Criteria();
        criteria.addEqualTo("status", "1");
        criteria.addEqualTo("totasks", "0");

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("project", Integer.valueOf("" + idProject));

        // Query of all the topics
        Query query = new QueryByCriteria(topicsData.class, criteria);
        Collection allLines = broker.getCollectionByQuery(query);

        return allLines.iterator();
    }
}
