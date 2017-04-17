/*
 * BrokerAccess.java
 *
 * Created on January 9, 2003, 10:09 AM
 */
  
package com.unify.webcenter.broker;


import org.apache.ojb.broker.*;
import org.apache.ojb.broker.query.*;
import java.util.ArrayList;

/**
 *  Main Broker Clase 
 * @author  Gerardo Arroyo Arce
 */
public abstract class MainBroker {
    public static int GAP_SIZE = 5;
    protected PersistenceBroker broker;   
    private Integer totalRowsLastStatement = new Integer(0);
    
    /** Creates a new instance of BrokerAccess */
    public MainBroker() {
       // Se inicializa el broker para la interaccin con la BD
       broker = PersistenceBrokerFactory.defaultPersistenceBroker();                
    }
    
    
    public void close() {
        try {
            broker.close();
        } catch (Exception e) {
            System.out.println("Error closing broker: " + e.toString());
        }
    }
    
    public void add(Object ojb) throws PersistenceBrokerException {
        try {
            broker.retrieveAllReferences(ojb);    
            broker.beginTransaction();
            broker.store(ojb);    
            broker.commitTransaction();        
        } catch (PersistenceBrokerException e) {
            broker.abortTransaction();      
            throw new PersistenceBrokerException(e.toString());
        }
    }
    
    public void delete(Object ojb) throws PersistenceBrokerException { 
        try {
            broker.beginTransaction();
            broker.delete(ojb);   
            broker.commitTransaction();     
        } catch (PersistenceBrokerException e) {
            broker.abortTransaction();
            throw new PersistenceBrokerException(e.toString());
        }                     
    }
    
    public void update(Object ojb) throws PersistenceBrokerException {
        try {
            broker.retrieveAllReferences(ojb);
            broker.beginTransaction();
            broker.store(ojb);    
            broker.commitTransaction();        
        } catch (PersistenceBrokerException e) {
            broker.abortTransaction();
            throw new PersistenceBrokerException(e.toString());
        }             
    }
    
    // return the list of items
    public ArrayList getItems(String[] e, int idAccount) {
        // Debemos convertir el iterador en un arraylist.
        ArrayList lista = new ArrayList();
        for (int i=0; i < e.length; i++) {            
            lista.add(getData(Integer.parseInt(e[i])));            
        }
               
        return lista;
        
    }
    
    /* Mètodo que retorna un iterador con todos los objetos en el rango especificado */
    public java.util.Iterator getCollection(java.util.Collection c, int init, int end) {
        ArrayList output = new ArrayList();
        
        Object[] ojb = c.toArray();
        int max = ojb.length;
              
        // Se toma el tamaño maximo del array y se asegura que no haya under o overflows       
        if (end > max) end = max;
        if (init > max) init = max;
        
        for (int i = init; i < end; i++) 
            output.add(ojb[i]);        
        
        // Se regresa un iterador sobre el resultado
        return output.iterator();        
   }
    
    public final Integer getCount() { return totalRowsLastStatement; }
    
    protected final void setCount(Integer total) { totalRowsLastStatement = total; }

    public abstract com.unify.webcenter.data.mainData getData(int id, int idAccount);
        public abstract com.unify.webcenter.data.mainData getData(int id);
    public abstract java.util.Iterator getList(int accountId) throws PersistenceBrokerException;
    //public abstract java.util.Iterator getList(String sortColumnName,String sortOrder, int accountId,int memberId) throws PersistenceBrokerException;
    public abstract java.util.Iterator getList(String sortColumnName,String sortOrder, int accountId) throws PersistenceBrokerException;
    public abstract java.util.Iterator getList(String sortColumnName,String sortOrder) throws PersistenceBrokerException;
}
