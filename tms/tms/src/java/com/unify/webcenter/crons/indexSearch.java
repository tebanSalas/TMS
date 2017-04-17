/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.crons;

/**
 *
 * @author MARCELA QUINTERO
 */
/*
 * checkTasks.java
 *
 * Created on May 28, 2003, 11:26 AM
 */
import java.util.*;

import com.unify.webcenter.broker.*;
import com.unify.webcenter.data.*;

public class indexSearch {

    private accountsBroker brokerAccount;
    private indexData dataIndex;
    /** Creates a new instance of checkTasks */
    public indexSearch() {
        brokerAccount = new accountsBroker();
        dataIndex = new indexData();
    }

    /* Metodo que cierra todos los brokers a la base de datos. */
    private void closeBrokers() {
        brokerAccount.close();
    }

    /**
     * @param args the command line arguments
     * El envio de los correos los hace teniendo en cuenta los parametros de la
     * Fecha Inicio y la Fecha Final, en este caso la Fecha Inicio sera la fecha
     * que se ejecuta la clase menos 366 dias y la fecha final seria el dia que se 
     * ejecuta la clase menos 1 dia
     */
    public static void main(String[] args) {
        new Locale("es", "");
        indexSearch checker = new indexSearch();
        // Metodo principal de chequeo.
        System.out.println("-----------------------------------");
        System.out.println("     TMS v3.1 Index Search");
        System.out.println("-----------------------------------");

        try {
            checker.check();

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace(System.out);

        } finally {
            checker.closeBrokers();
        }

        System.out.println("Done!");
    }

    /* Metodo que se encarga de revisar todas las tareas y ver cuales estan atrazadas */
    public void check() {
        //Tomal todas las cuentas
        Iterator e = brokerAccount.getList("id", "ASC");

        accountsData dataAccount;

        while (e.hasNext()) {
            dataAccount = new accountsData();
            dataAccount = (accountsData) e.next();
                // Se procede con la indexacion de los datos
            dataIndex.indexInfo(dataAccount.getId());
        }
    }
}
