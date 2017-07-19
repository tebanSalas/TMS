/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.broker;


import com.unify.webcenter.data.applicationControlData;
import com.unify.webcenter.data.empleadosData;
import com.unify.webcenter.data.mainData;
import com.unify.webcenter.data.membersData;
import com.unify.webcenter.data.organization_versionAppData;
import com.unify.webcenter.data.organizationsData;
import com.unify.webcenter.data.tasksData;
import com.unify.webcenter.tools.connectionClass;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 *
 * @author esper
 */
public class applicationControlBroker extends MainBroker{

    public mainData getData(int id, int idAccount) {
        applicationControlData data = new applicationControlData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id", new Integer(id)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount)); // esto es lo mismo que la linea anterior
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (applicationControlData) e.next();
        }

        // We return the object
        return data;
    }

    public mainData getData(int id) {
        applicationControlData data = new applicationControlData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id", new Integer(id)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (applicationControlData) e.next();
        }

        // We return the object
        return data;
    }

    public Iterator getList(int accountId) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
       
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId)); // esto es lo mismo que la linea anterior
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }

    public Iterator getList(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
       
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId)); // esto es lo mismo que la linea anterior
         // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }        
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }

    public Iterator getList(String sortColumnName, String sortOrder) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
       
         // We order the result set
        if (sortOrder.equalsIgnoreCase("ASC")) {
            criteria.addOrderByAscending(sortColumnName);
        } else {
            criteria.addOrderByDescending(sortColumnName);
        }        
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    
    
    public Iterator listOrganizationXVerApp(int idVerapp) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
       
        criteria.addEqualTo("id_verapp", Integer.valueOf("" + idVerapp)); // esto es lo mismo que la linea anterior
        // Query of the exact organization
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    //metodo para obtener todas las tareas de una orhanización en estado no aplicado o aplicado temporal
    public Iterator applicationsByOrga(int idOrga) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        //criteria.addOrderByAscending("");
        criteria.addEqualTo("id_organization", Integer.valueOf("" + idOrga)); // esto es lo mismo que la linea anterior
        criteria.addLessOrEqualThanField("state", ""+1);
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    
    
    //metodo para tener la lista de (aplicationControl) de las organizaciones en las que ya se aplicó
    public Iterator getListOrganizationsApplied(int idTask) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        //criteria.addOrderByAscending("");
        criteria.addEqualTo("id_task", Integer.valueOf("" + idTask)); // esto es lo mismo que la linea anterior
        //criteria.addEqualTo("state", ""+2);
        criteria.addEqualTo("state", Integer.valueOf("" + 2));
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    
    //metodo para tener la lista de (aplicationControl) de las organizaciones en las que aun no se aplica
    public Iterator getListOrganizationsUnApplied(int idTasks) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        //criteria.addOrderByAscending("");
        criteria.addEqualTo("id_task", Integer.valueOf("" + idTasks)); // esto es lo mismo que la linea anterior
        criteria.addLessOrEqualThanField("state", ""+1);
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    
    public Iterator getApplicacionesPorTarea(int task) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        //criteria.addOrderByAscending("");
        criteria.addEqualTo("id_task", Integer.valueOf("" + task)); // esto es lo mismo que la linea anterior
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    
    
    //metodo que ejecuta el reporte de las tareas aplicadas
    public Iterator TasksAppliedReport(int idOrga, int idUser, Timestamp startDate, Timestamp endDate, int page) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        //criteria.addOrderByAscending("");
        if(idOrga!=0){
           criteria.addEqualTo("id_organization", Integer.valueOf("" + idOrga));
        }
        if(idUser!=0){
           criteria.addEqualTo("id_application_user", Integer.valueOf("" + idUser));
        }
        if(startDate!=null && endDate!=null){
            criteria.addGreaterOrEqualThan("application_date", startDate);
            criteria.addLessOrEqualThan("application_date", endDate);
            
        }else if(startDate==null && endDate!=null){
           criteria.addLessOrEqualThan("application_date", endDate); //todas las mejores que esa fecha
           
        }else if(startDate!=null && endDate==null){
           criteria.addGreaterOrEqualThan("application_date", startDate); //todas las mayores 
        }
        //que esten en aplicado o aplicado temporal
        criteria.addGreaterOrEqualThanField("state", ""+1);
        
        //ordenadas de la mas nueva a la más vieja
        criteria.addOrderByDescending("application_date");
        
        
        
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        //Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        setCount(new Integer(allLines.size()));


        if (page > 0) {
            // Se determinan los limites superior e inferior
            int inicio = (page - 1) * 100;
            int fin = page * 100;

            // now iterate over the result 
            return getCollection(allLines, inicio, fin);
        } else {
            return allLines.iterator();
        }
    }
    
   
    
    public ArrayList getAppsByOperationNumber(int id_orga) throws PersistenceBrokerException {
         Connection conn = null;
        ResultSet rs = null;
        Statement stmt1 = null;
       
//        projectsData task= new projectsData();
        String query="";
        query="SELECT application_control.id, application_control.state, application_control.application_date, application_control.comment,application_control.id_task, application_control.id_application_user, application_control.id_organization, application_control.id_account " +
              "FROM tms.application_control " +
               "INNER JOIN tms.tasks ON (application_control.id_task = tasks.id) " +
               "WHERE (tasks.status=0 OR tasks.status=1) AND application_control.state<=1 AND application_control.id_organization = "+id_orga+" AND tasks.version_control=0 AND tasks.operation_number!=0"+
               "order by tasks.operation_number asc";
            
        connectionClass _conecctionClass = new connectionClass();
        conn = _conecctionClass.getConnection();
        ArrayList listPropias= new ArrayList();
        membersBroker m = new membersBroker();
        organizationsBroker o = new organizationsBroker();
        tasksBroker t = new tasksBroker();
        
        try {             
            stmt1 = conn.createStatement();
            rs = stmt1.executeQuery(query);
            
            while (rs.next()) {
               applicationControlData appControlTemp = new applicationControlData();
               appControlTemp.setId(rs.getInt("id"));
               appControlTemp.setComment(rs.getString("comment"));
               appControlTemp.setApplication_date(rs.getTimestamp("application_date"));
               appControlTemp.setState(rs.getInt("state"));
               appControlTemp.setId_task(rs.getInt("id_task"));
               appControlTemp.setId_application_user(rs.getInt("id_application_user"));
               appControlTemp.setId_organization(rs.getInt("id_organization"));
               appControlTemp.setId_account(rs.getInt("id_account"));
               
               appControlTemp.setParentTask( (tasksData) t.getData(appControlTemp.getId_task(), appControlTemp.getId_account()));
               appControlTemp.setParentMembers( (membersData) m.getData(appControlTemp.getId_application_user()));
               appControlTemp.setParentOrganization((organizationsData) o.getData(appControlTemp.getId_organization()));
               
               if(appControlTemp.getParentTask().getparentProject().getorganization() == appControlTemp.getId_organization()){
                   listPropias.add(appControlTemp);
               }
               
               
            }   
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            ex1.printStackTrace();
        }
        return listPropias;
    }
    
    public ArrayList getAppsByVersionControl(int id_orga) throws PersistenceBrokerException {
         Connection conn = null;
        ResultSet rs = null;
        Statement stmt1 = null;
       
//        projectsData task= new projectsData();
        String query="";
        query="SELECT application_control.id, application_control.state, application_control.application_date, application_control.comment,application_control.id_task, application_control.id_application_user, application_control.id_organization, application_control.id_account " +
              "FROM tms.application_control " +
               "INNER JOIN tms.tasks ON (application_control.id_task = tasks.id) " +
               "WHERE application_control.state<=1 AND application_control.id_organization = "+id_orga+" AND tasks.status=0 AND tasks.version_control!=0 AND tasks.operation_number!=0"+
               "order by tasks.version_control asc";
            
        connectionClass _conecctionClass = new connectionClass();
        conn = _conecctionClass.getConnection();
        ArrayList listPropias= new ArrayList();
        membersBroker m = new membersBroker();
        organizationsBroker o = new organizationsBroker();
        tasksBroker t = new tasksBroker();
        
        try {             
            stmt1 = conn.createStatement();
            rs = stmt1.executeQuery(query);
            
            while (rs.next()) {
               applicationControlData appControlTemp = new applicationControlData();
               appControlTemp.setId(rs.getInt("id"));
               appControlTemp.setComment(rs.getString("comment"));
               appControlTemp.setApplication_date(rs.getTimestamp("application_date"));
               appControlTemp.setState(rs.getInt("state"));
               appControlTemp.setId_task(rs.getInt("id_task"));
               appControlTemp.setId_application_user(rs.getInt("id_application_user"));
               appControlTemp.setId_organization(rs.getInt("id_organization"));
               appControlTemp.setId_account(rs.getInt("id_account"));
               
               appControlTemp.setParentTask( (tasksData) t.getData(appControlTemp.getId_task(), appControlTemp.getId_account()));
               appControlTemp.setParentMembers( (membersData) m.getData(appControlTemp.getId_application_user()));
               appControlTemp.setParentOrganization((organizationsData) o.getData(appControlTemp.getId_organization()));
               
               if(appControlTemp.getParentTask().getparentProject().getorganization() != appControlTemp.getId_organization()){
                listPropias.add(appControlTemp);
               } 
               
            }   
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException ex1) {
            ex1.printStackTrace();
        }
        return listPropias;
    }
    
    
    public mainData getApplicationByTaskAndOga(int task, int orga) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        applicationControlData data = new applicationControlData(); 
        //criteria.addOrderByAscending("");
        criteria.addEqualTo("id_task", Integer.valueOf("" + task)); // esto es lo mismo que la linea anterior
        criteria.addEqualTo("id_organization", Integer.valueOf("" + orga));
        // Query of the exact organization
        Query query = new QueryByCriteria(applicationControlData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        if (e.hasNext()) { //recorre la colección
            data = (applicationControlData) e.next();
        }

        // We return the object
        return data;
    }
    
    public boolean existInAppControl(int task, int id_orga, int id_account) throws PersistenceBrokerException {
        // New criteria for sortering
        Criteria criteria = new Criteria();

        criteria.addEqualTo("id_task", Integer.valueOf("" + task));
        criteria.addEqualTo("id_organization", Integer.valueOf("" + id_orga));
        criteria.addEqualTo("id_account", Integer.valueOf("" + id_account));
        
        // Query of all the tasks
        Query query = new QueryByCriteria(applicationControlData.class, criteria);

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query);
         Iterator e = allLines.iterator();
        // If exists the record -MUST EXISTS ALWAYS
        // We return the object
         return e.hasNext();

        
    } 
    
      
    
}
