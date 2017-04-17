//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;



import org.apache.ojb.broker.query.*;

import org.apache.ojb.broker.*;

import java.util.*;

/**
 * Class that represent the broker for the table teams
 * @author Administrator
 */
public class teamsBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account", new Integer(accountId));

        // Query of all the teams

        Query query = new QueryByCriteria(teamsData.class, criteria);



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
        Query query = new QueryByCriteria(teamsData.class, criteria);

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

        // Query of all the teams

        Query query = new QueryByCriteria(teamsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        // now iterate over the result 

        return allLines.iterator();

    }


    public java.util.Iterator getListofMembers(String sortColumnName,
            String sortOrder, int page, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();


        // Y donde no sea el profile 2 (usuarios de la empresa)
        // ni 3 GOD MODE
        //       criteria.addNotEqualTo("profile", "2");                
        //       criteria.addNotEqualTo("profile", "3");                

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the projects
        Query query = new QueryByCriteria(membersData.class, criteria);

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

    /* Regresa una lista de todos los miembros de un equipo de un proyecto */
    public java.util.Iterator getListByProjectMember(String sortColumnName,
            String sortOrder, int idProject, int idMember, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("members", Integer.valueOf("" + idMember));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

    
        /* Regresa una lista de todos los miembros de un equipo de un proyecto */
/*    public java.util.Iterator getListByMember(String sortColumnName,
            String sortOrder, int idMember) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        criteria.addEqualTo("members", Integer.valueOf("" + idMember));

        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }
*/
    public java.util.Iterator getListByMember(String sortColumnName,
            String sortOrder, int idMember, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        criteria.addEqualTo("members", Integer.valueOf("" + idMember));
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

    
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
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

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

     public java.util.Iterator getListByProject(String sortColumnName,
            String sortOrder, int idProject, int page) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
       
        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

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
     * Metodo que regresa la lista de members de ese proyecto que son del 
     * usuario y que tienen permiso de cerrar tareas
     */
    public java.util.Iterator getListByProjectWithCloseRole(String sortColumnName,
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
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("parentMember.profile", "2");
        criteria.addEqualTo("parentMember.ind_end_tasks", "S");
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

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
     * Metodo que regresa la lista de members de ese proyecto que son del 
     * usuario y que tienen permiso de cerrar tareas o bien son lider de proyecto
     * del cliente
     */
    public java.util.Iterator getListByProjectWithCloseRoleOrClientManager(String sortColumnName,
            String sortOrder, int idProject, int page) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("parentMember.profile", "2");
        Criteria tipo = new Criteria();
        tipo.addEqualTo("parentMember.ind_end_tasks", "S");
        Criteria tipo2 = new Criteria();
        tipo2.addEqualTo("parentMember.ind_client_manager", "S");
        tipo.addOrCriteria(tipo2);

        criteria.addAndCriteria(tipo);

        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

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

      public java.util.Iterator getListByProjectWithCloseRoleOrClientManager(String sortColumnName,
            String sortOrder, int idProject, int page, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("parentMember.profile", "2");
        Criteria tipo = new Criteria();
        tipo.addEqualTo("parentMember.ind_end_tasks", "S");
        Criteria tipo2 = new Criteria();
        tipo2.addEqualTo("parentMember.ind_client_manager", "S");
        tipo.addOrCriteria(tipo2);

        criteria.addAndCriteria(tipo);

        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);
        
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
    /* Regresa una lista de todos los miembros de un equipo de un proyecto 
    que estan publicados */
    public java.util.Iterator getListByProjectForPortal(String sortColumnName,
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

        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));

        criteria.addEqualTo("published", "1");

criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // No sale el administrator

        criteria.addNotEqualTo("parentMember.profile", "3");



        // Query of all the teams

        Query query = new QueryByCriteria(teamsData.class, criteria);



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

    /* Regresa una lista de todos los members que no forman parte del
    equipo del proyecto indicado. */
    public java.util.Iterator getListByNotInProject(String sortColumnName,
            String sortOrder, int idProject, int page,int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query de todos los miembros que forman parte del projecto dado.
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // Se traen a los miembros actuales de los equipos.
        Collection allTeamMembers = broker.getCollectionByQuery(query);

        // se toma su id y se agrega a una coleccion
        ArrayList actualMembers = new ArrayList();
        Iterator e = allTeamMembers.iterator();

        while (e.hasNext()) {
            actualMembers.add(Integer.valueOf("" + ((teamsData) e.next()).getmembers()));
        }

        // Ahora se hace un NOT IN con la tabla de members
        Criteria membersCriteria = new Criteria();


        // Si hay al menos un miembro se agrega el not it
        if (actualMembers.size() > 0) {
            membersCriteria.addNotIn("id", actualMembers);
        }

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            membersCriteria.addOrderByAscending(sortColumnName);
        } else {
            membersCriteria.addOrderByDescending(sortColumnName);
        }

        // Ni el administrador
        membersCriteria.addNotEqualTo("profile", "3");
         membersCriteria.addNotEqualTo("profile", "4");
membersCriteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query de todos los miembros que forman parte del projecto dado.
        Query queryMembers = new QueryByCriteria(membersData.class, membersCriteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(queryMembers);

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

    
    
      /* Regresa una lista de todos los members que no forman parte del
    equipo del proyecto indicado. */
    public java.util.Iterator getListMembersInProject(String sortColumnName,
            String sortOrder, int idProject, int page,int idAccount, int id) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query de todos los miembros que forman parte del projecto dado.
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // Se traen a los miembros actuales de los equipos.
        Collection allTeamMembers = broker.getCollectionByQuery(query);

        // se toma su id y se agrega a una coleccion
        ArrayList actualMembers = new ArrayList();
        Iterator e = allTeamMembers.iterator();

        while (e.hasNext()) {
            actualMembers.add(Integer.valueOf("" + ((teamsData) e.next()).getmembers()));
        }
        
        criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + id));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query de todos los miembros que forman parte del projecto dado.
        query = new QueryByCriteria(teamsData.class, criteria);

        // Se traen a los miembros actuales de los equipos.
        allTeamMembers = broker.getCollectionByQuery(query);

        // se toma su id y se agrega a una coleccion
        ArrayList actualMembersTeam = new ArrayList();
        e = allTeamMembers.iterator();

        while (e.hasNext()) {
            actualMembersTeam.add(Integer.valueOf("" + ((teamsData) e.next()).getmembers()));
        }

        
        // Ahora se hace un NOT IN con la tabla de members
        Criteria membersCriteria = new Criteria();


        // Si hay al menos un miembro se agrega el not it
        if (actualMembers.size() > 0) {
            membersCriteria.addIn("id", actualMembers);
        }

        // Si hay al menos un miembro se agrega el not it
        if (actualMembersTeam.size() > 0) {
            membersCriteria.addNotIn("id", actualMembersTeam);
        }

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            membersCriteria.addOrderByAscending(sortColumnName);
        } else {
            membersCriteria.addOrderByDescending(sortColumnName);
        }

        // Ni el administrador
        membersCriteria.addNotEqualTo("profile", "3");
         membersCriteria.addNotEqualTo("profile", "4");
membersCriteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query de todos los miembros que forman parte del projecto dado.
        Query queryMembers = new QueryByCriteria(membersData.class, membersCriteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(queryMembers);

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
    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {

        teamsData data = new teamsData();

        Criteria criteria = new Criteria();

        criteria.addEqualTo("id", new Integer(id));
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));


        // Query of the exact organization

        Query query = new QueryByCriteria(teamsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (teamsData) e.next();
        }



        // We return the object

        return data;

    }
    
      // Return the object associated with the key.
    public mainData getData(int id) {

        teamsData data = new teamsData();

        Criteria criteria = new Criteria();

        criteria.addEqualTo("id", new Integer(id));



        // Query of the exact organization

        Query query = new QueryByCriteria(teamsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (teamsData) e.next();
        }



        // We return the object

        return data;

    }

    /* Regresa true si el member dado pertenece al equipo.  */
    public boolean isMember(int idProject, int memberId, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering

        Criteria criteria = new Criteria();

        boolean value = false;



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));

        criteria.addEqualTo("members", Integer.valueOf("" + memberId));
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams

        Query query = new QueryByCriteria(teamsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        if (allLines.size() > 0) {
            value = true;
        }



        return value;



    }


    /*  Lista los miembros de un equipo a excepcion de un miembro  en particular */
    public java.util.Iterator getListByInProject(String sortColumnName,
            String sortOrder, int idProject, int idMember, int page, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + idProject));
        criteria.addNotEqualTo("members", Integer.valueOf("" + idMember));
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

        // Query de todos los miembros que forman parte del projecto dado.
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // Se traen a los miembros actuales de los equipos.
        Collection allLines = broker.getCollectionByQuery(query);

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
     * Metodo que elimina todas las referencias de un member en todos los teams.
     */
    public void deleteAllReferences(int memberId, int idAccount) throws PersistenceBrokerException {

        // New criteria for search

        Criteria criteria = new Criteria();



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("members", Integer.valueOf("" + memberId));
criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));


        // Query of all the teams

        Query query = new QueryByCriteria(teamsData.class, criteria);



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



        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));


        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        Iterator e = allLines.iterator();

        // Se borra cada entrada

        while (e.hasNext()) {

            this.delete(e.next());

        }

    }

    /*
     * Metodo que elimina todas las referencias de un member en todos los teams.
     */
    public void deleteAllReferencesByProject(int projectId, int idAccount) throws PersistenceBrokerException {

        // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("projects", Integer.valueOf("" + projectId));
          // Se agrega el criteria por cuenta
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query of all the teams
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        // Se borra cada entrada

        while (e.hasNext()) {
            this.delete(e.next());
        }

    }

    /**
     * Metodo que se encarga de copiar todos los miembros del equipo de un proyecto origen a
     * uno destino.
     */
    public void copyAllTeamToProject(int originalProject, int targetProject,int idAccount) throws PersistenceBrokerException {
        Iterator e = this.getListByProject("id", "ASC", originalProject, 0, idAccount);
        teamsData data;
        while (e.hasNext()) {
            data = new teamsData((teamsData) e.next());
             data.setId_account(idAccount);
            data.setprojects(targetProject);
            this.add(data);
        }
    }
    
      /*  Lista los miembros de un equipo a excepcion de un miembro  en particular */
    public java.util.Iterator getListProjectsUser(int idMember, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("members", Integer.valueOf("" + idMember));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));

        // Query de todos los miembros que forman parte del projecto dado.
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // Se traen a los miembros actuales de los equipos.
        Collection allLines = broker.getCollectionByQuery(query);

        setCount(new Integer(allLines.size()));
        return allLines.iterator();

    }

  
}
