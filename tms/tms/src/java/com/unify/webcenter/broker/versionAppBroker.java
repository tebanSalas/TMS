/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.broker;

import com.unify.webcenter.data.empleadosData;
import com.unify.webcenter.data.mainData;
import com.unify.webcenter.data.versionAppData;
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
public class versionAppBroker extends MainBroker{

    public mainData getData(int id, int idAccount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public mainData getData(int id) {
        versionAppData data = new versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id", new Integer(id)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        
        // Query of the exact organization
        Query query = new QueryByCriteria(versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (versionAppData) e.next();
        }

        // We return the object
        return data;
    }

    public Iterator getList(int accountId) throws PersistenceBrokerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterator getList(String sortColumnName, String sortOrder, int accountId) throws PersistenceBrokerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        Query query = new QueryByCriteria(versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }

    public boolean existApp(int id_application) {
        versionAppData data = new versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id_application", new Integer(id_application)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        
        // Query of the exact organization
        Query query = new QueryByCriteria(versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (versionAppData) e.next();
        }
        if(data.getId_application() == id_application){
            return true;
        }else{
           return false;
        }

        
    }
    public boolean existVersion(int id_version) {
        versionAppData data = new versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id_version", new Integer(id_version)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        
        // Query of the exact organization
        Query query = new QueryByCriteria(versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (versionAppData) e.next();
        }
        if(data.getId_version() == id_version){
            return true;
        }else{
           return false;
        }
    }
    
    public Iterator getList() throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        Query query = new QueryByCriteria(versionAppData.class, criteria); // a que data pertenece el criteria
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        return e;
    }
    
    public boolean exist(int idVer, int idApp) {
        versionAppData data = new versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id_application", new Integer(idApp)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        criteria.addEqualTo("id_version", new Integer(idVer));
        // Query of the exact organization
        Query query = new QueryByCriteria(versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            return true;
        }else{
            return false;
        }

        // We return the object
        
    }
}
