//   Generated by FlechaRoja Tech Tools (2003) 
package com.unify.webcenter.broker;

import com.unify.webcenter.data.*;

import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.*;
import java.util.*;
import java.util.ArrayList;

/**
 * Class that represent the broker for the table members
 * @author Administrator
 */
public class membersBroker extends MainBroker {

    public java.util.Iterator getList(int accountId) throws PersistenceBrokerException {
          // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account",new Integer(accountId));
        // Query of all the members
        Query query = new QueryByCriteria(membersData.class, criteria);

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
        Query query = new QueryByCriteria(membersData.class, criteria);

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
    criteria.addEqualTo("id_account",new Integer(accountId));
        // Query of all the members
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

      public java.util.Iterator getNonClientMembers(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
    criteria.addEqualTo("id_account",new Integer(accountId)); 
        criteria.addNotEqualTo("profile", "2");
        // Query of all the members
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

    // Return the object associated with the key.
    public mainData getData(int id, int idAccount) {
        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        // We return the object
        return data;
    }
  public com.unify.webcenter.data.mainData getData(int id) {
        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id", new Integer(id));

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        // We return the object
        return data;
    }   
  
    // Check for login a password for the user that are trying to login
    public membersData checkLogin(String username, String password) {
        
        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("login", username);

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);
        
        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        try {
            if (allLines.size() > 0) {
                if (data.getpassword().equalsIgnoreCase(encrypt(password))) {
                    return data;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    // get the data of a member using the email like key
    public membersData getMemberByEmail(String email, int idAccount) {

        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("email_work", email);
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        if (allLines.size() > 0) {
            return data;
        } else {
            return null;
        }
    }

    // get the data of a member using the login column
    public membersData getMemberByLogin(String login) {

        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("login", login);

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        if (allLines.size() > 0) {
            return data;
        } else {
            return null;
        }
    }

    /* Metodo que se encarga de verificar si un login ya existe en la base de datos
     * esto a fin de evitar que posteriormente se haga un ingreso de ids dobles
     * @return true si existe ese login
     */
    public boolean exists(String login) {
        boolean existe = true;

        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("login", login);

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        if (allLines.isEmpty()) {
            existe = false;
        }

        return existe;
    }

    /* Regresa la lista de todos los miembros en los cuales la organizacion
     * indicada es la que esta asignada */
    public java.util.Iterator getListByOrganization(String sortColumnName,
            String sortOrder, int idOrg, int page, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Y donde sea la organizacion y ademas usuarios del cliente
        criteria.addEqualTo("organization", Integer.valueOf("" + idOrg));
        criteria.addEqualTo("profile", "2");
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

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

        
        public java.util.Iterator getListMembersByOrganization(String sortColumnName, 
                String sortOrder, int idOrg, int page, int idAccount) throws PersistenceBrokerException {
		// New criteria for sortering
		Criteria criteria = new Criteria();

                // Y donde sea la organizacion y ademas usuarios del cliente
                criteria.addEqualTo("organization", Integer.valueOf("" + idOrg));               
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
		// We order the result set
		if (sortOrder.equalsIgnoreCase("ASC"))
			criteria.addOrderByAscending(sortColumnName);
		else
			criteria.addOrderByDescending(sortColumnName);
                                
		// Query of all the projects
		Query query = new QueryByCriteria(membersData.class, criteria);
                
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);

                // Se modifica el total de registros retornados para efectos
                // del paginado.
                setCount(new Integer(allLines.size())); 
                
                if (page > 0) {
                    // Se determinan los limites superior e inferior
                    int inicio = (page-1) * GAP_SIZE;
                    int fin = page * GAP_SIZE;

                    // now iterate over the result 
                    return getCollection(allLines, inicio, fin);     
                } else {
                    return allLines.iterator();
                }
	}
        
        public java.util.Iterator getListMemberByOrg(String sortColumnName, 
                String sortOrder, int idOrg,int idmember, int page, int idAccount) throws PersistenceBrokerException {
		// New criteria for sortering
		Criteria criteria = new Criteria();

                // Y donde sea la organizacion y ademas usuarios del cliente
                criteria.addEqualTo("organization", Integer.valueOf("" + idOrg));    
                 criteria.addEqualTo("id", Integer.valueOf("" + idmember));  
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
		// We order the result set
		if (sortOrder.equalsIgnoreCase("ASC"))
			criteria.addOrderByAscending(sortColumnName);
		else
			criteria.addOrderByDescending(sortColumnName);
                                
		// Query of all the projects
		Query query = new QueryByCriteria(membersData.class, criteria);
                
		// ask the broker to retrieve the Extent collection
		Collection allLines = broker.getCollectionByQuery(query);

                // Se modifica el total de registros retornados para efectos
                // del paginado.
                setCount(new Integer(allLines.size())); 
                
                if (page > 0) {
                    // Se determinan los limites superior e inferior
                    int inicio = (page-1) * GAP_SIZE;
                    int fin = page * GAP_SIZE;

                    // now iterate over the result 
                    return getCollection(allLines, inicio, fin);     
                } else {
                    return allLines.iterator();
                }
	}
        
        
        
    /* Regresa la lista de todos los miembros que tiene el profile de ser
     * clientes de la empresa
     */
    public java.util.Iterator getListClientMembers(String sortColumnName,
            String sortOrder, int page, int idAccount) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Y donde sea la organizacion y ademas usuarios del cliente
        criteria.addEqualTo("profile", "2");
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

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

    /* Regresa la lista de todos miembros de la empresa y no del cliente
     */
    public java.util.Iterator getListofCompanyMembers(String sortColumnName,
            String sortOrder, int page,int accountId) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Y donde no sea el profile 2 (usuarios de la empresa)
        // ni 3 GOD MODE
        criteria.addNotEqualTo("profile", "2");
        criteria.addNotEqualTo("profile", "3");

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
        criteria.addEqualTo("id_account",new Integer(accountId));
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

    // Retorna la lista de miembros de una organizacion que pueden recibir
    // una Cotizacion.
    public Iterator getMembersQuotation(int project, int idAccount) {
        Criteria criteria = new Criteria();

        // Y donde sea el profile 2 (usuarios de la empresa)                       
        criteria.addEqualTo("parentMember.quotation", "1");
        criteria.addEqualTo("parentMember.profile", "2");
        criteria.addEqualTo("projects", new Integer(project));
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the projects
        Query query = new QueryByCriteria(teamsData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // Se toma cada miembro del equipo y se extrae su member.
        Iterator e = allLines.iterator();
        ArrayList salida = new ArrayList();
        teamsData data;
        while (e.hasNext()) {
            data = (teamsData) e.next();

            // Se agrega a la salida.
            salida.add(data.getparentMember());
        }

        // Solo los miembros del equipo que deben ser.
        return salida.iterator();
    }

    /* Regresa la lista de todos miembros de la empresa y no del cliente
     * que son sujetos a ser revisados en sus agendas y horarios
     */
    public java.util.Iterator getListofCheckSchedulesMembers()
            throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Y donde no sea el profile 2 (usuarios de la empresa)
        criteria.addNotEqualTo("profile", "2");
        criteria.addNotEqualTo("profile", "3");

        // Y donde sea el indicador de revision igual a 1
        criteria.addEqualTo("ind_check_schedules", "1");

        criteria.addOrderByAscending("name");

        // Query of all the projects
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // Se modifica el total de registros retornados para efectos
        // del paginado.
        setCount(new Integer(allLines.size()));

        return allLines.iterator();
    }

    public static String encrypt(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return byteArrayToHexString(d.digest());
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    /** Mod Jury: Determinar el rol del usuario
     */
    public boolean isProyectAdminMember(int memberId, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();
        boolean value = false;

        // member a ser evaluado
        criteria.addEqualTo("id", Integer.valueOf("" + memberId));
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        if (allLines.size() > 0) {
            Iterator e = allLines.iterator();
            if (e.hasNext()) {
                if (((membersData) e.next()).getprofile().equals("1")) {
                    value = true;
                }
            }
        }
        return value;
    }

    public boolean isAdminUser(int memberId, int idAccount) throws PersistenceBrokerException {

        // New criteria for sortering
        Criteria criteria = new Criteria();
        boolean value = false;

        // member a ser evaluado
        criteria.addEqualTo("id", Integer.valueOf("" + memberId));
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the teams
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        if (allLines.size() > 0) {
            Iterator e = allLines.iterator();
            if (e.hasNext()) {
                if (((membersData) e.next()).getprofile().equals("3") || ((membersData) e.next()).getprofile().equals("4")) {
                    value = true;
                }
            }
        }
        return value;
    }

    public Iterator getProjectAdmin(int adminUser, int idAccount) {
        Criteria criteria = new Criteria();

        if (adminUser == 0) {
            adminUser = 1;
        }
        // Y donde sea el profile 2 (usuarios de la empresa)                              
        criteria.addEqualTo("id", "" + adminUser);
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // Query of all the projects
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // Solo los miembros del equipo que deben ser.
        return allLines.iterator();
    }

    /*
     * Metodo que elimina todas las referencias de un miembro en la base de datos
     */
    public void deleteAllReferences(int memberid, String profile, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();


        //if(memberid != 1) {
        if (!profile.equals("4")) {
            // El usuario admin NUNCA se borra
            // Se agrega el criteria por proyecto.
            criteria.addEqualTo("id", Integer.valueOf("" + memberid));
            criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
            // Query of all the teams
            Query query = new QueryByCriteria(membersData.class, criteria);

            // ask the broker to retrieve the Extent collection
            Collection allLines = broker.getCollectionByQuery(query);

            Iterator e = allLines.iterator();
            membersData data;
            while (e.hasNext()) {
                data = (membersData) e.next();

                /* FASE 1: BORRAR  TEAMS */
                teamsBroker teamBroker = new teamsBroker();
                teamBroker.deleteAllReferences(data.getid(), idAccount);
                teamBroker.close();

                /* FASE 2: BORRAR  REPORTS */
                reportsBroker reportBroker = new reportsBroker();
                reportBroker.deleteAllReferences(data.getid(),idAccount);
                reportBroker.close();

                /* FASE 3: BORRAR  CALENDARS */
                calendarBroker calBroker = new calendarBroker();
                calBroker.deleteAllReferences(data.getid(),idAccount);
                calBroker.close();

                /* FASE 3: BORRAR  SCHEDULES */
                schedulesBroker scheBroker = new schedulesBroker();
                scheBroker.deleteAllReferences(data.getid(),idAccount);
                scheBroker.close();

                /* FASE 4: Borra LOS FILES */
                filesBroker fileBroker = new filesBroker();
                fileBroker.deleteAllReferencesByOwner(data.getid(),idAccount);
                fileBroker.close();

                /* FASE 5: Borra LOS POSTS */
                postBroker pstBroker = new postBroker();
                pstBroker.deleteAllReferences(data.getid(),idAccount);
                pstBroker.close();

                /* FASE 6: TRANSFERIR LOS TOPICS */
                topicsBroker topicBroker = new topicsBroker();
                topicBroker.deleteAllReferencesByOwner(data.getid(),idAccount);
                topicBroker.close();

                /* FASE 7: Si el administrador desea borrar las tareas se aplica}
                 * el delete, o en su defecto un transfer 
                 */
                tasksBroker brokerTask = new tasksBroker();
                brokerTask.deleteAllReferences(data.getid(),idAccount);
                brokerTask.close();

                /* FASE 8: Borrar LOS PROYECTOS */
                projectsBroker projBroker = new projectsBroker();
                projBroker.deleteAllReferences(data.getid(),idAccount);
                projBroker.close();

                /* FASE 9: Borrar la Configuraci�n de Env�o de Correos*/
                configSendReportBroker confBroker = new configSendReportBroker();
                confBroker.deleteAllReferencesbyMember(data.getid(), idAccount);
                confBroker.close();
                
                broker.delete(data);
            }
        }
    }

    /*
     * Metodo que elimina todas las referencias de un miembro en la base de datos
     */
    public void deleteAllReferencesByOrganization(int orgId, int idAccount) throws PersistenceBrokerException {
        // New criteria for search
        Criteria criteria = new Criteria();

        // Se agrega el criteria por proyecto.
        criteria.addEqualTo("organization", Integer.valueOf("" + orgId));
 criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
        // No se borra el usuario administrador
        criteria.addNotEqualTo("id", Integer.valueOf("1"));

        // Query of all the teams
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        Iterator e = allLines.iterator();

        membersData data;
        while (e.hasNext()) {
            data = (membersData) e.next();

            /* FASE 1: BORRAR  TEAMS */
            teamsBroker teamBroker = new teamsBroker();
            teamBroker.deleteAllReferences(data.getid(), idAccount);
            teamBroker.close();
            /* FASE 2: BORRAR  REPORTS */
            reportsBroker reportBroker = new reportsBroker();
            reportBroker.deleteAllReferences(data.getid(),idAccount);
            reportBroker.close();

            /* FASE 3: BORRAR  CALENDARS */
            calendarBroker calBroker = new calendarBroker();
            calBroker.deleteAllReferences(data.getid(),idAccount);
            calBroker.close();

            /* FASE 4: BORRAR  SCHEDULES */
            schedulesBroker scheBroker = new schedulesBroker();
            scheBroker.deleteAllReferences(data.getid(),idAccount);
            scheBroker.close();

            /* FASE 5: Borra LOS FILES */
            filesBroker fileBroker = new filesBroker();
            fileBroker.deleteAllReferencesByOwner(data.getid(),idAccount);
            fileBroker.close();

            /* FASE 6: Borra LOS POSTS */
            postBroker pstBroker = new postBroker();
            pstBroker.deleteAllReferences(data.getid(),idAccount);
            pstBroker.close();

            /* FASE 7: TRANSFERIR LOS TOPICS */
            topicsBroker topicBroker = new topicsBroker();
            topicBroker.deleteAllReferencesByOwner(data.getid(),idAccount);
            topicBroker.close();

            /* FASE 8: ELIMINAR BITACORA */
            tasksStatusLogBroker brokerTaskStatus= new tasksStatusLogBroker();
            brokerTaskStatus.deleteAllReferences(data.getid(),idAccount);
            brokerTaskStatus.close();
            

            /* FASE 9: Si el administrador desea borrar las tareas se aplica}
             * el delete, o en su defecto un transfer 
             */
            tasksBroker brokerTask = new tasksBroker();
            brokerTask.deleteAllReferences(data.getid(),idAccount);
            brokerTask.close();


            /* FASE 10: Borrar LOS PROYECTOS */
            projectsBroker projBroker = new projectsBroker();
            projBroker.deleteAllReferences(data.getid(),idAccount);
            projBroker.close();

            System.out.println("id"+ data.getid());
            broker.delete(data);
        }
    }

    /* Regresa la lista de todos miembros de la empresa y no del cliente
     */
    public java.util.Iterator getListofAdminMembers(String sortColumnName,
            String sortOrder, int page, int account) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // Y donde no sea el profile 2 (usuarios de la empresa)
        // ni 3 GOD MODE
        criteria.addEqualTo("profile", "3");
        criteria.addEqualTo("id_account", new Integer(account));
        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }

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

    public void deleteAllReferencesbyAccount(int accountId) throws PersistenceBrokerException {

        // New criteria for search

        Criteria criteria = new Criteria();



        // Se agrega el criteria por proyecto.

        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId));



        // Query of all the teams

        Query query = new QueryByCriteria(membersData.class, criteria);



        // ask the broker to retrieve the Extent collection

        Collection allLines = broker.getCollectionByQuery(query);



        Iterator e = allLines.iterator();

        // Se borra cada entrada

        while (e.hasNext()) {
            
            this.delete(e.next());

        }

    }
    
      public java.util.Iterator getList(String sortColumnName, String sortOrder, ArrayList members) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }
        criteria.addNotIn("id", members);

        // Query of all the assignments
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result 
        return allLines.iterator();
    }

       public com.unify.webcenter.data.mainData getData(String login, int idAccount) {
        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("login", login);
        criteria.addEqualTo("id_account", new Integer(idAccount));

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS

        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        // We return the object
        return data;
    }
       
    public java.util.Iterator getQuotationMembers(String id_account){
        membersData data = new membersData();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("id_account", id_account);
        criteria.addEqualTo("profile", "3");

        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        
        // If exists the record -MUST EXISTS ALWAYS
            
        return  allLines.iterator();
    } 
       
        // Return the object associated with the key.
    public mainData getAdmin(int idAccount, int profile) {
        membersData data = new membersData();
        Criteria criteria = new Criteria();
            criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount));
            criteria.addEqualTo("profile", Integer.valueOf("" + profile));
        // Query of the exact organization
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
        Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) {
            data = (membersData) e.next();
        }

        // We return the object
        return data;
        }
    
    
    public java.util.Iterator getListMemberApplication() throws PersistenceBrokerException {
          // New criteria for sortering
        Criteria criteria = new Criteria();
        criteria.addEqualTo("permission_app_control",new Integer(1));
        // Query of all the members
        Query query = new QueryByCriteria(membersData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);

        // now iterate over the result to print each Service
        return allLines.iterator();
    }
}
