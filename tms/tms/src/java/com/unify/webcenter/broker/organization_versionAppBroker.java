/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.broker;

import com.unify.webcenter.data.empleadosData;
import com.unify.webcenter.data.mainData;
import com.unify.webcenter.data.organization_versionAppData;
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
public class organization_versionAppBroker extends MainBroker{

    public mainData getData(int id, int idAccount) {
        organization_versionAppData data = new organization_versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id", new Integer(id)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        criteria.addEqualTo("id_account", Integer.valueOf("" + idAccount)); // esto es lo mismo que la linea anterior
        // Query of the exact organization
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (organization_versionAppData) e.next();
        }

        // We return the object
        return data;
    }

    public mainData getData(int id) {
        organization_versionAppData data = new organization_versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id", new Integer(id)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        
        // Query of the exact organization
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (organization_versionAppData) e.next();
        }

        // We return the object
        return data;
    }

    public Iterator getList(int accountId) throws PersistenceBrokerException {
         Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
       
        criteria.addEqualTo("id_account", Integer.valueOf("" + accountId)); // esto es lo mismo que la linea anterior
        // Query of the exact organization
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

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
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

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
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    
    //Metodo que trae todos los applicativos relacionados a una version
    public Iterator getAppsXorganizacion(int idOrga) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteri
        criteria.addEqualTo("id_organization", new Integer(idOrga));
// ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
                // We return the object
        return e;
    }
    //Metodo que trae todos los applicativos que no estan relacionados a una version
     public boolean getAppsNOorganizacion(int idOrga, int verApp) throws PersistenceBrokerException {
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id_verapp",new Integer(verApp));
        criteria.addEqualTo("id_organization",new Integer(idOrga));
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); 
        if (e.hasNext()) { //recorre la colección
            return false; //si está
        }else{
           return true; // no está
           
        }
    }
     
    public boolean existVerApp(int id_verApp) {
        organization_versionAppData data = new organization_versionAppData();  //todos los datos que vienen de la bd
        Criteria criteria = new Criteria(); /// es como la definicion del where en la consulta
        criteria.addEqualTo("id_verapp", new Integer(id_verApp)); // es como un tipo de join, diciendo que el "id" sea igual al id que me pasan por parametro
        
        // Query of the exact organization
        Query query = new QueryByCriteria(organization_versionAppData.class, criteria); // a que data pertenece el criteria

        // ask the broker to retrieve the Extent collection
        Collection allLines = broker.getCollectionByQuery(query); // construye la sentencia de sql y la ejecuta
        Iterator e = allLines.iterator(); //lo pasa a un tipo de arreglo
        // If exists the record -MUST EXISTS ALWAYS
        if (e.hasNext()) { //recorre la colección
            data = (organization_versionAppData) e.next();
        }
        if(data.getId_verapp() == id_verApp){
            return true;
        }else{
           return false;
        }
    }
}
