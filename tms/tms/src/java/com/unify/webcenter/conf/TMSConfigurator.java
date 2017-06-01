/*
 * TMSConfigurator.java
 *
 * Created on March 13, 2003, 2:07 PM
 */
package com.unify.webcenter.conf;

import com.unify.webcenter.broker.accountsBroker;
import com.unify.webcenter.data.accountsData;
import com.unify.webcenter.data.loginData;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Clase que contiene elementos de configuracion del TMS
 * @author  Gerardo Arroyo Arce
 */
public class TMSConfigurator {

    private static final String PROPERTIES = "/com/unify/webcenter/conf/tms.cfg";
    //private static final String PROPERTIES = "/tmp/dev_tms/uploads/cgf/tms.cfg";
    private static TMSConfigurator conf = null;
    private static String indexPath = "";
    private static String downloadPath = "";
    private static String downloadEmails = "";
    private static String desencryptFiles = "";
    private static String mailhost = "";
    private static String mailer = "";
    private static String default_mail_address = "";
    private static String default_mail_username = "";
    private static String default_mail_password = "";
    private static String accountingmail = "";
    private static String mainURL = "http://daytonasoft.net:8080/tmsE";
    private static String managersEmails = "";
    private static String mailAuthentication = "N";
    private static String mailLanguage = "es";
    private static String version = " V 6.0. Build 110309";
    private static String company = "";
    private static String website = "";
    private static String creator = "";
    private static String trial = "";
    private static final String passPhrase = "Daytona";
    private static String enc = "N";
    private static String sales_mail="";
    private static String newUserReferenceGuide="";
    private static String maxTasksUpload="200";
    private static String svn = "";
    public static String getSales_mail() {
        return sales_mail;
    }

    public static void setSales_mail(String aSales_mail) {
        sales_mail = aSales_mail;
    }

    public static String getNewUserReferenceGuide() {
        return newUserReferenceGuide;
    }

    public static void setNewUserReferenceGuide(String aNewUserReferenceGuide) {
        newUserReferenceGuide = aNewUserReferenceGuide;
    }


    /** Creates a new instance of TMSConfigurator */
    public TMSConfigurator(loginData user) {
        try {
            // Se inicializan las instancias privadas, iniciando con los
            // archivos de configuracion del server.
            InputStream configFile = getClass().getResourceAsStream(PROPERTIES);
            initializeConfiguration();
            System.out.println("Se parsea el archivo de configuracion.");
            // Se parsea el archivo de configuracion.
            parseConfiguration(configFile, user);
            // Se cierran los InputStreams
            configFile.close();

        } catch (IOException e) {
            // Se detecto una excepcion de IO; probablemente el archivo de configuracion
            // no existe.
            System.out.println("TMSConfigurator: " + e.toString());
        }
    }

    /** Creates a new instance of TMSConfigurator */
    public TMSConfigurator() {
        try {
            // Se inicializan las instancias privadas, iniciando con los
            // archivos de configuracion del server.
            InputStream configFile = getClass().getResourceAsStream(PROPERTIES);
            // Se parsea el archivo de configuracion.

            parseConfiguration(configFile);
            // Se cierran los InputStreams
            configFile.close();

        } catch (IOException e) {
            // Se detecto una excepcion de IO; probablemente el archivo de configuracion
            // no existe.
            System.out.println("TMSConfigurator: " + e.toString());
        }
    }

    /** Retorna el nombre del servicio RMI del servidor UNIFY
     * @return RMI Name Service
     */
    public static String getIndexPath() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return indexPath;
    }

    public static String getDownloadPath() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return downloadPath;
    }

    public static String getDownloadEmails() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return downloadEmails;
    }

    public static String getMailHost() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return mailhost;
    }

    public static String getMailer() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return mailer;
    }

    public static String getDefault_mail_address() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return default_mail_address;
    }

    public static String getDefault_mail_username() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return default_mail_username;
    }

    public static String getDefault_mail_password() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return default_mail_password;
    }

    public static String getAccountingmail(loginData user) {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator(user);
        }
        return accountingmail;
    }

    public static String getManagersEmails(loginData user) {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator(user);
        }
        //      return accountingmail;
        return managersEmails;
    }

    public static String getMainURL() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return mainURL;
    }

    public static String getMailAuthentication() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return mailAuthentication;
    }

    public static String getMailLanguage() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return mailLanguage;
    }

    public static String getVersion() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return version;
    }

    public static String getCompany(loginData user) {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator(user);
        }
        return company;
    }

    public static String getWebSite(loginData user) {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator(user);
        }
        return website;
    }

    public static String getCreator(loginData user) {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator(user);
        }
        return creator;
    }

    /** Retorna el nombre del servicio RMI del servidor UNIFY
     * @return RMI Name Service
     */
    public static String getTrial() {
        // Si no existe el singleton de Configuration se crea uno.
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return trial;
    }

    public static String getPassPhrase() {
        return passPhrase;
    }

    public static String getDesencryptFiles() {
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return desencryptFiles;
    }

    public static String getEnc() {
        if (conf == null) {
            conf = new TMSConfigurator();
        }
        return enc;
    }

    public static String getSvn() {
        return svn;
    }

    public static void setSvn(String svn) {
        TMSConfigurator.svn = svn;
    }
    
    

    /**
     * Metodo que se encarga de parsear el archivo en XML de la configuracion
     * y cargar las instancias privadas que correspondan.   
     * @throws IOException  */
    private void parseConfiguration(InputStream configFile, loginData user) throws IOException {
        String separator = File.separator;
        accountsData accountData = new accountsData();
        accountsBroker accountBroker = new accountsBroker();


        accountData = (accountsData) accountBroker.getData(user.getId_account());
        // Se carga el archivo de propiedades.
        Properties prop = new Properties();
        prop.load(configFile);


        // Se obtienen los parametros de configuracion
        company = accountData.getCompany();
        website = accountData.getWebsite();
        creator = accountData.getCreator();

        indexPath = prop.getProperty("indexPath") + "ACC" + user.getId_account() + separator;
        downloadPath = prop.getProperty("downloadPath") + separator + "ACC" + user.getId_account() + separator;
        desencryptFiles = prop.getProperty("desencryptFiles") + separator + accountData.getId() + separator + "archivos" + separator;
        downloadEmails = prop.getProperty("downloadEmails") + separator + "ACC" + user.getId_account() + separator;
        accountingmail = accountData.getAccount_email();
        managersEmails = accountData.getEmail();
        enc = prop.getProperty("enc");
        trial = prop.getProperty("trial");
        version = prop.getProperty("version");
        svn = prop.getProperty("svn");
        newUserReferenceGuide=(prop.getProperty("newUserReferenceGuide"));
        accountBroker.close();
    }

    /**
     * Metodo que se encarga de parsear el archivo en XML de la configuracion
     * y cargar las instancias privadas que correspondan.   
     * @throws IOException  */
    private void parseConfiguration(InputStream configFile) throws IOException {


        // Se carga el archivo de propiedades.
        Properties prop = new Properties();
        prop.load(configFile);

        mailhost = prop.getProperty("mailhost");

        mailer = prop.getProperty("mailer");

        trial = prop.getProperty("trial");

        default_mail_address = prop.getProperty("default_mail_address");

        default_mail_username = prop.getProperty("default_mail_username");

        default_mail_password = prop.getProperty("default_mail_password");

        mainURL = prop.getProperty("mainURL");

        mailAuthentication = prop.getProperty("mailAuthentication");

        mailLanguage = prop.getProperty("mailLanguage");
        enc = prop.getProperty("enc");
        indexPath = prop.getProperty("indexPath");
        version = prop.getProperty("version");
        
        setSales_mail(prop.getProperty("sales_mail"));
        
        setNewUserReferenceGuide(prop.getProperty("newUserReferenceGuide"));
    }

    /**
     * Metodo que se encarga de inicializar las variables de la configuracion
     * y cargar las instancias privadas que correspondan.   
     * @throws IOException  */
    private void initializeConfiguration() throws IOException {
        company = null;
        website = null;
        creator = null;
        accountingmail = null;
        managersEmails = null;
    }
    
    /**
     * @return the maxTasksUpload
     */
    public static String getMaxTasksUpload() {
        return maxTasksUpload;
    }

    /**
     * @param aMaxTasksUpload the maxTasksUpload to set
     */
    public static void setMaxTasksUpload(String aMaxTasksUpload) {
        maxTasksUpload = aMaxTasksUpload;
    }

}