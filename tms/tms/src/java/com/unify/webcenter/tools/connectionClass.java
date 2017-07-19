/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unify.webcenter.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Alberto Campos
 */
public class connectionClass {
    /** Creates a new instance of connectionClass */
    public connectionClass() {
    }


    public Connection getConnection() {
        try{
            Connection con = null;
         //   Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("org.postgresql.Driver");
            Connection connection;
            //return connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "tms", "H6F8nVrZRM");
          return connection = DriverManager.getConnection("jdbc:postgresql://192.168.0.9:5432/postgres", "tms", "MONO$FUEGO");
            //return con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:db10g", "ecommerce", "ecommerce");
           // return con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.8:1521:SAS", "tms", "tmssocase");
         //   return con = DriverManager.getConnection("jdbc:oracle:thin:@10.5.0.14:1521:mtk", "ecommerce", "ecommerce");
            //return con = DriverManager.getConnection("jdbc:postgresql://192.168.0.8:5432/postgres","tms","Tms");
       //     return con = DriverManager.getConnection("jdbc:postgresql://192.168.0.10:5432/tribu","tms_tribu","TmsTribu");
      //  return connection = DriverManager.getConnection("jdbc:postgresql://192.168.10:5432/postgres", "tms_tribu", "TmsTribu");
           // return con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.9:1521:DESA", "dev_tms", "dev_tms");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        } catch (Exception ex) {
            System.err.println("Se ha producido un error de BD. No se pudo abrir la conexion");
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public void close(Connection con){
        try{
            con.close();
            con =null;
        }catch (Exception ex) {
            System.err.println("Se ha producido un error de BD. No se pudo cerrar la conexion");
            System.err.println(ex.getMessage());
        }
    }
}
