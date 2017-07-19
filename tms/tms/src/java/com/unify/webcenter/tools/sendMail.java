/* sendMail.java
 *
 * Created on March 10, 2003, 11:25 AM
 */
package com.unify.webcenter.tools;

import java.io.*;
import java.util.Properties;
import java.util.Date;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;

import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.tools.*;
import java.util.logging.*;

import com.unify.webcenter.conf.TMSConfigurator;

import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Administrator
 */
public class sendMail {

    private String mailhost = "";
    private String mailer = "";
    private ResourceBundle labels;
    private Properties props;
    private Session session;
    private static Logger logger = Logger.getLogger("com.unify");
    private Transport transport;
    private final boolean debug = true;

    /** Creates a new instance of sendMail */
    public sendMail() {
        //labels = ResourceBundle.getBundle("ApplicationResources", Locale.ENGLISH);
        labels = ResourceBundle.getBundle("ApplicationResources",
                new Locale(TMSConfigurator.getMailLanguage(), ""));
        mailhost = TMSConfigurator.getMailHost();
        mailer = TMSConfigurator.getMailer();

        // Get a Session object
        props = System.getProperties();
      //  props.put("mail.smtp.starttls.enable", "true");
       // props.put("mail.smtp.ssl.trust", "mail.daytonasoft.com");
        // Inicializa STARTLS
       // props.put("mail.smtp.starttls.enable", "true");
              //          props.put("mail.smtp.ssl.enable", "false");
             //   props.put("mail.smtp.starttls.enable", "false");
       // props.put("mail.smtp.starttls.enable", "true");
          //      props.put("mail.smtp.port", "587");
        if (TMSConfigurator.getMailAuthentication().equalsIgnoreCase("Y")) {
            session = Session.getInstance(props, new MyAuthenticator());
            props.put("mail.smtp.auth", "true");
        } else {
            session = Session.getInstance(props);
        }

    }

        private void setUpMail(int port){
        try {
            
              Properties props = System.getProperties();
              
                
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
          /*  if (mailhost != null) {
                props.put("mail.smtps.host", mailhost);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.ssl.enable", "false");
                props.put("mail.smtp.starttls.enable", "true");
            }*/
           // if (port == 465) {
                        props.setProperty("mail.transport.protocol", "smtp");     
               props.setProperty("mail.host", "smtp.gmail.com");  
               props.put("mail.smtp.auth", "true");  
               props.put("mail.smtp.port", "465");  
               props.put("mail.smtp.socketFactory.port", "465");  
               props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
               props.put("mail.smtp.socketFactory.fallback", "false");  
          //  }
            session = Session.getDefaultInstance(props);
            if (debug) {
                session.setDebug(false);;
            }
            
            
            if (port==465)
                transport = session.getTransport("smtps");
            else
                transport = session.getTransport("smtp");
            
           // transport.connect(mailhost, 587, TMSConfigurator.getDefault_mail_username(), TMSConfigurator.getDefault_mail_password());
        transport.connect(mailhost, port, TMSConfigurator.getDefault_mail_username(), TMSConfigurator.getDefault_mail_password());
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
        } catch (MessagingException ex) {
            ex.printStackTrace();
           
        }
         
    }
    
    /**
     * Metodo que se ejecuta para enviar diversos emails del sistema.
     */
    public void send(String operation, accountsData to_account, loginData login, membersData member, String url) {
        String extra = to_account.getName();

        String from = null, to = null, cc = null, bcc = null;
        System.out.println("SEND2 "+ TMSConfigurator.getMailHost());

        System.out.println("Login: "+ member.getlogin());
        
        setUpMail(587);
        
        //, url = null;
        //String protocol = null, host = null, user = null, password = null,
        String subject = null;
        //String record = null;
        // name of folder in which to record mail
        boolean debug = false;
        int toid = 0;

        subject = labels.getString("email." + operation + ".subject") + " " + String.valueOf(to_account.getId()) + "-" + to_account.getName();

        // En caso de que algo venga en nulo
        //if (to_account.getEmail() == null) {
        from = TMSConfigurator.getDefault_mail_address();
        //} else {
        //    from = to_account.getEmail();
        //}

        // Si "to" es null, es que el correo se toma de la configuraci?n, esto
        // solo para el caso del correo al departamento de CxC.
        if (member.getemail_work() == null) {
            to = TMSConfigurator.getManagersEmails(login);
            toid = 0;
        } else {
            to = member.getemail_work();
            toid = member.getid();
        }

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");

            // El text extra se debe encodear por aquello de los espacios extra
            // lo mismo deberia hacerse con el header.
            //extra = java.net.URLEncoder.encode(extra,"UTF-8");

            extra = MimeUtility.encodeText(extra, "iso-8859-1", "Q");
            extra = java.net.URLEncoder.encode(extra, "UTF-8");

        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }

            // Se le agrega a todo los correos, una copia al o los administradores
            if (bcc != null) {
                bcc += ";" + TMSConfigurator.getManagersEmails(login);
            } else {
                bcc = TMSConfigurator.getManagersEmails(login);
            }
            msg.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(bcc, false));


            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());



            // create and fill the first message part (text part)
            MimeBodyPart mbp1 = new MimeBodyPart();

            collect(operation, 0, member.getid(), to_account.getId(), toid, extra, mbp1, login, url);

            // create and fill the second message part (html part)
            MimeBodyPart mbp2 = new MimeBodyPart();
            // Use setText(text, charset), to show it off !

            collect(operation, 1, member.getid(), to_account.getId(), toid, extra, mbp2, login, url);

            // create the Multipart and its parts to it
            Multipart mp = new MimeMultipart("alternative");
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            msg.setContent(mp);


            // send the message
            transport.sendMessage(msg, msg.getAllRecipients());
//Transport.send(msg);

        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

     /**
     * Metodo que se ejecuta para enviar diversos emails del sistema.
     */
    public void sendTopics(String operation, membersData from_member, membersData to_member,
            int id, String extra, loginData login, topicsData topics, String url) {
        String from = null, to = null, cc = null, bcc = null;
                //, url = null;
        setUpMail(587);
        //String protocol = null, host = null, user = null, password = null,
        String subject = null;
        //String record = null;	// name of folder in which to record mail
        boolean debug = false;
        int toid = 0;
        // Get SUBJECT

                            System.out.println("Operation: "+ operation);
        System.out.println("Login: "+ to_member.getlogin());

        if (operation.equals("taskassignment") || operation.equals("prioritytaskchange") ||
                operation.equals("statustaskchange") || operation.equals("duedatetaskchange") ||
                operation.equals("statustaskclosed")) {

            subject = labels.getString("email." + operation + ".subject") + " " + String.valueOf(id) + "-" + extra;

        } else if (operation.equals("quotesended") || operation.equals("quoterejected") || operation.equals("quoteacepted")) {

            // Si se trata de la operacion de envio de cotizaciones, el titulo se varia
            subject = labels.getString("email." + operation + ".subject");

            try {

                tasksBroker taskBroker = new tasksBroker();
                tasksData task = (tasksData) taskBroker.getData(id);

                taskBroker.close();
                subject += ": " + task.getname();

            } catch (Exception eBroker) {
                System.out.println("sendMail Exception: " + eBroker.toString());
            }
        } else {
            subject = labels.getString("email." + operation + ".subject");
        }


        // En caso de que algo venga en nulo
        if (from_member == null) {
            from = TMSConfigurator.getDefault_mail_address();
        } else {

            from = from_member.getemail_work();
        }
        // Si "to" es null, es que el correo se toma de la configuraci?n, esto
        // solo para el caso del correo al departamento de CxC.
        if (operation.equals("accounting")) {
            to = TMSConfigurator.getAccountingmail(login);
            toid = 0;
        } else {
            to = to_member.getemail_work();
            toid = to_member.getid();
        }


        // Si se trata de cerrado por el cliente, se envia una copia oculta al admin
        if (operation.equals("statustaskclosed")) {

            bcc = TMSConfigurator.getDefault_mail_address();
        }

        if ((from.equals(to) && from.equals(cc)) || (operation.equals("newTopic")) || (operation.equals("newTopicToCreator")) ||
                (operation.equals("newPost")) || (operation.equals("meetingrequest")) || (operation.equals("operation.equals")) || (operation.equals("alterUser")) || (operation.equals("alterUserPass"))) {
            cc = null;
        } else {
            cc = from;
        }

        if (from.equals(to) && from.equals(cc)){
            cc = null;
        }

        // El from es modificado a fin de que se emplee el usuario definido en
        // en el archivo de configuracion del tms y evitar hacer mail relay
        // con empresas externas. Pero se le agrega al email en su subject,
        // el nombre a partir del cual es enviado.
        if (from_member != null) {
            subject +=": "+ topics.getsubject()+" (" + from_member.getname() + ")";
        }

        from = TMSConfigurator.getDefault_mail_address();



        if (extra != null) {
            try {

                subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
                // El text extra se debe encodear por aquello de los espacios extra
                // lo mismo deberia hacerse con el header.
                //extra = java.net.URLEncoder.encode(extra,"UTF-8");

                extra = MimeUtility.encodeText(extra, "iso-8859-1", "Q");
                extra = java.net.URLEncoder.encode(extra, "UTF-8");

            } catch (Exception e) {
                logger.logp(Level.SEVERE, "send.do", "send",
                        "Fatal Error: " + e.toString());
                e.printStackTrace();
            }
        }

        System.out.println("From: "+ from +" To: "+ to +" Cc: " + cc +" Bcc: " + bcc);
        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }
            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);
            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }

            if ((!operation.equals("alterUser")) && (!operation.equals("alterUserPass"))) {

                // Se le agrega a todo los correos, una copia al o los administradores
                if (bcc != null) {
                    bcc += "," + TMSConfigurator.getManagersEmails(login);
                } else {
                    bcc = TMSConfigurator.getManagersEmails(login);
                }
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }

            msg.setSubject(subject);
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            if (operation.equals("meetingrequest")) {
                //int type, fromid;
                MimeBodyPart mbp;
                String addr = "";
                try {
                   // addr = TMSConfigurator.getMainURL() + "/mail.do?operation=meetingrequest&id=" + id + "&fromid=" + from_member.getid() +
                    addr = url + "/mail.do?operation=meetingrequest&id=" + id + "&fromid=" + from_member.getid() +
                     "&toid=" + to_member.getid() + "&type=1&userAccount=" + login.getId_account();

                    URL news = new URL(addr);
                    BufferedReader in = new BufferedReader(new InputStreamReader(news.openStream()));

                    String line;
                    StringBuffer sb = new StringBuffer();

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    msg.setContent(sb.toString(), "text/html");
                } catch (Exception e) {
                    logger.logp(Level.SEVERE, "send.do", "send",
                            "Fatal Error: " + e.toString());
//                     e.printStackTrace();
                }

            } else {

                // create and fill the first message part (text part)
                MimeBodyPart mbp1 = new MimeBodyPart();
                collect(operation, 0, id, from_member.getid(), toid, extra, mbp1, login, url);
                // create and fill the second message part (html part)
                MimeBodyPart mbp2 = new MimeBodyPart();
                // Use setText(text, charset), to show it off !

                collect(operation, 1, id, from_member.getid(), toid, extra, mbp2, login, url);

                // create the Multipart and its parts to it
                Multipart mp = new MimeMultipart("alternative");
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);

                // add the Multipart to the message
                msg.setContent(mp);
            }
            // send the message

           // Transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

            /**
     * Metodo que se ejecuta para enviar diversos emails del sistema.
     */
    public void sendTopicsGroup(String operation, membersData from_member, membersData to_member,
            String extra, loginData login, ArrayList topics, String project, String url) {
        String from = null, to = null, cc = null, bcc = null;
                //, url = null;
        setUpMail(587);
        //String protocol = null, host = null, user = null, password = null,
        String subject = null;
        //String record = null;	// name of folder in which to record mail
        boolean debug = false;
        int toid = 0;
        // Get SUBJECT


            // Si se trata de la operacion de envio de cotizaciones, el titulo se varia
            subject = labels.getString("email." + operation + ".subject")+ " en "+ project + " (" + login.gefullname() + ")";

              // En caso de que algo venga en nulo
       // if (from_member == null) {
            from = TMSConfigurator.getDefault_mail_address();
      //  } else {

        //    from = from_member.getemail_work();
       // }


            to = to_member.getemail_work();
            toid = to_member.getid();

        try {


            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }
            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(from));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));


            if ((!operation.equals("alterUser")) && (!operation.equals("alterUserPass"))) {

                // Se le agrega a todo los correos, una copia al o los administradores
                if (bcc != null) {
                    bcc += "," + TMSConfigurator.getManagersEmails(login);
                } else {
                    bcc = TMSConfigurator.getManagersEmails(login);
                }
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }

            msg.setSubject(subject);
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());


                        String text = "";
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

            text += labels.getString("html.html");
            text += labels.getString("html.head");
            text += labels.getString("html.metainf");
            text += labels.getString("html.head0");
            text += labels.getString("html.body");
            text += labels.getString("email.header");
            text += labels.getString("html.br");
            text += labels.getString("html.br");

           // text += labels.getString("html.arrow") + TMSConfigurator.getMainURL();
            text += labels.getString("html.arrow") + url;
            text += labels.getString("html.arrow0");
            text += labels.getString("html.face");

            text += labels.getString("email.tickets.newTicketsHeader");
            text += labels.getString("html.face0");

                text += labels.getString("html.hr");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
            text += labels.getString("email.endoftrial.header");
            text += labels.getString("html.face0");
           text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
            text += labels.getString("email.tickets.newTickets");
            text += labels.getString("html.face0");
           text += labels.getString("html.br");
            text += labels.getString("html.br");

                text += labels.getString("html.hr");
            text += labels.getString("html.table");
                text += labels.getString("html.tr");
                text += labels.getString("html.th");
             //   text += labels.getString("html.bullet") ;
             //   text += TMSConfigurator.getMainURL()+ labels.getString("html.bullet0") ;
                text += labels.getString("common.project")+": "+project;
                text += labels.getString("html.th0");

                text += labels.getString("html.tr0");

            Iterator e= topics.iterator();
            topicsData topicData = new topicsData();
            while(e.hasNext()){
                topicData= (topicsData)e.next();

            text += labels.getString("html.tr");
                text += labels.getString("html.tdc");
           //     text += labels.getString("html.bullet") ;
           //     text += TMSConfigurator.getMainURL()+ labels.getString("html.bullet0") ;
           //     text += "               "+topicData.getid()+ " - <a href=\""+TMSConfigurator.getMainURL()+"/topics.do?operation=view&id="+topicData.getid() +"\"'>"+ topicData.getsubject()+"</a>";
                text += "               "+topicData.getid()+ " - <a href=\""+url+"/topics.do?operation=view&id="+topicData.getid() +"\"'>"+ topicData.getsubject()+"</a>";
                text += labels.getString("html.td0");
                text += labels.getString("html.tr0");
            }

            text += labels.getString("html.table0");
                text += labels.getString("html.hr");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
           text += labels.getString("email.tickets.clickOn");
            text += labels.getString("html.face0");
           text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
           text += labels.getString("email.notificationFooter2") + " "+ TMSConfigurator.getCreator(login);
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
            text += labels.getString("email.footer")+ " "+ TMSConfigurator.getCompany(login)+" ";
           text += labels.getString("email.footer1") ;
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
          //  text += "<a href=\""+TMSConfigurator.getMainURL()+"/login.do\">" ;
            text += "<a href=\""+url+"/login.do\">" ;
            text += labels.getString("html.span");
           // text += TMSConfigurator.getMainURL()+"/login.do";
            text += url+"/login.do";
            text += labels.getString("html.span0")+"</a>";
            text += labels.getString("html.br");

            text += labels.getString("html.body0");
            text += labels.getString("html.html0");

            msg.setSubject(subject);
            text= MimeUtility.encodeText(text, "iso-8859-1", "Q");
            msg.setContent(text, "text/html");

            try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
            } catch (Exception ex) {
            ex.printStackTrace();
            }
            //Transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }


    /**
     * Metodo que se ejecuta para enviar diversos emails del sistema.
     */
    public void send(String operation, membersData from_member, membersData to_member,
            int id, String extra, loginData login, String url) { 
        String from = null, to = null, cc = null, bcc = null;
                //, url = null;
        //String protocol = null, host = null, user = null, password = null,
        String subject = null;
        //String record = null;	// name of folder in which to record mail
        boolean debug = false;
        int toid = 0;
        setUpMail(587);
        // Get SUBJECT

                            System.out.println("Operation: "+ operation);
        System.out.println("Login: "+ to_member.getlogin());
        
        if(operation.equals("asignedOperationNumber")){
            subject = labels.getString("email." + operation + ".subject") + " " + extra + " a la tarea "+String.valueOf(id);
            
        }  else if (operation.equals("taskassignment") || operation.equals("prioritytaskchange") ||
                operation.equals("statustaskchange") || operation.equals("duedatetaskchange") ||
                operation.equals("statustaskclosed") || operation.equals("taskreasigned")
                || operation.equals("finishedtasksnotification")|| operation.equals("sendqatnotification")
                || operation.equals("sendqafnotification")) {

            subject = labels.getString("email." + operation + ".subject") + " " + String.valueOf(id) + "-" + extra;

        } else if (operation.equals("quotesended") || operation.equals("quoterejected") || operation.equals("quoteacepted")) {

            // Si se trata de la operacion de envio de cotizaciones, el titulo se varia
            subject = labels.getString("email." + operation + ".subject");

            try {

                tasksBroker taskBroker = new tasksBroker();
                tasksData task = (tasksData) taskBroker.getData(id);

                taskBroker.close();
                subject += ": " + task.getname();

            } catch (Exception eBroker) {
                System.out.println("sendMail Exception: " + eBroker.toString());
            }
        } else {
            subject = labels.getString("email." + operation + ".subject");
        }


//         En caso de qu    System.out.println(from);e algo venga en nulo
        /*if(from_member.getid() ==0){
            from = TMSConfigurator.getDefault_mail_address();
        } else {

            from = from_member.getemail_work();
        }*/

        from = TMSConfigurator.getDefault_mail_address();
           from= "tms@daytonasoft.com";
       
        System.out.println("FROM"+ from_member.getemail_work());
        System.out.println("FROM MAIL "+ from);
        // Si "to" es null, es que+  el correo se toma de la configuraci?n, esto
        // solo para el caso del correo al departamento de CxC.
        if (operation.equals("accounting")) {
            to = TMSConfigurator.getAccountingmail(login);
            toid = 0;
        } else {
            to = to_member.getemail_work();
            toid = to_member.getid();
        }


        // Si se trata de cerrado por el cliente, se envia una copia oculta al admin
        if (operation.equals("statustaskclosed")) {

            bcc = TMSConfigurator.getDefault_mail_address();
        }


      //  if ((from.equals(to) && from.equals(cc)) || (operation.equals("newTopic")) || (operation.equals("newTopicToCreator")) ||
          if ((from.equals(to) ) || (operation.equals("newTopic")) || (operation.equals("newTopicToCreator")) ||
              (operation.equals("newPost")) || (operation.equals("meetingrequest")) || (operation.equals("operation.equals")) || (operation.equals("alterUser")) || (operation.equals("alterUserPass"))) {

            cc = null;
        } else {
            cc = from;

        }

        if (from.equals(to) && from.equals(cc)){
            cc = null;
        }

        // El from es modificado a fin de que se emplee el usuario definido en
        // en el archivo de configuracion del tms y evitar hacer mail relay
        // con empresas externas. Pero se le agrega al email en su subject,
        // el nombre a partir del cual es enviado.
        if (from_member != null) {
          //  subject += " (" + from_member.getname() + ")";
            subject += " (" + login.gefullname() + ")";
        }

        System.out.println("Titulo del Correo: "+subject);
        if (extra != null) {
            try {

                subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
                // El text extra se debe encodear por aquello de los espacios extra
                // lo mismo deberia hacerse con el header.
                //extra = java.net.URLEncoder.encode(extra,"UTF-8");

                extra = MimeUtility.encodeText(extra, "iso-8859-1", "Q");
                extra = java.net.URLEncoder.encode(extra, "UTF-8");

            } catch (Exception e) {
                logger.logp(Level.SEVERE, "send.do", "send",
                        "Fatal Error: " + e.toString());
                e.printStackTrace();
            }
        }

        System.out.println("From: "+ from +" To: "+ to +" Cc: " + cc +" Bcc: " + bcc);
        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);
            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }

            if ((!operation.equals("alterUser")) && (!operation.equals("alterUserPass"))) {

                // Se le agrega a todo los correos, una copia al o los administradores
                if (bcc != null) {
                    bcc += "," + TMSConfigurator.getManagersEmails(login);
                } else {
                    bcc = TMSConfigurator.getManagersEmails(login);
                }
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }

            msg.setSubject(subject);
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            if (operation.equals("meetingrequest")) {
                //int type, fromid;
                MimeBodyPart mbp;
                String addr = "";
                try {
                //    addr = TMSConfigurator.getMainURL() + "/mail.do?operation=meetingrequest&id=" + id + "&fromid=" + from_member.getid() +
                      addr = url + "/mail.do?operation=meetingrequest&id=" + id + "&fromid=" + from_member.getid() +
                            "&toid=" + to_member.getid() + "&type=1&userAccount=" + login.getId_account();

                    URL news = new URL(addr);
                    BufferedReader in = new BufferedReader(new InputStreamReader(news.openStream()));

                    String line;
                    StringBuffer sb = new StringBuffer();

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    msg.setContent(sb.toString(), "text/html");
                } catch (Exception e) {
                    logger.logp(Level.SEVERE, "send.do", "send",
                            "Fatal Error: " + e.toString());
//                     e.printStackTrace();
                }

            } else {
                // create and fill the first message part (text part)
                MimeBodyPart mbp1 = new MimeBodyPart();
                collect(operation, 0, id, from_member.getid(), toid, extra, mbp1, login, url);
                // create and fill the second message part (html part)
                MimeBodyPart mbp2 = new MimeBodyPart();
                // Use setText(text, charset), to show it off !

                collect(operation, 1, id, from_member.getid(), toid, extra, mbp2, login, url);

                // create the Multipart and its parts to it
                Multipart mp = new MimeMultipart("alternative");
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);

                // add the Multipart to the message
                msg.setContent(mp);
            }
            // send the message
            transport.sendMessage(msg, msg.getAllRecipients());
           // Transport.send(msg);
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se ejecuta cuando se solicita una clave de acceso de un usuario q
     * la ha olvidado.
     */
    public void sendForgot(membersData member) {
        String from = null, to = null, cc = null, bcc = null, url = null;
        setUpMail(587);
        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail
        boolean debug = false; accountsData accData= new accountsData();
        // Get the email address of "from","to" and "cc"

        from = TMSConfigurator.getDefault_mail_address();
        to = member.getemail_work();
        subject = labels.getString("forgot.subject");

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Primeramente tomamos la clave que tiene el usuario y la encriptamps
            String pwd = member.getpassword();
            pwd = pwd.substring(0, 6);

            member.setpassword(membersData.encrypt(pwd));
            membersBroker memBroker = new membersBroker();
            accountsBroker accBroker = new accountsBroker();

            accData= (accountsData) accBroker.getData(member.getId_account());

            memBroker.update(member);
            memBroker.close();
            accBroker.close();


            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }
            if (bcc != null) {
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }

            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            // add the Multipart to the message
            String text = "";
            text += labels.getString("forgot.header");
            text += labels.getString("forgot.body1");
            text += labels.getString("loginForm.username.displayname") + ": " + member.getlogin() + "\n";
            text += labels.getString("loginForm.password.displayname") + ": " + pwd + "\n\n";

            text += labels.getString("forgot.body2");

           // text += TMSConfigurator.getMainURL() + "\n\n";
            text += accData.getMain_url() + "\n\n";
           // text += labels.getString("forgot.footer");

            msg.setText(text);


            // send the message
        transport.sendMessage(msg, msg.getAllRecipients());   
        // Transport.send(msg);
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }


    }

    // Metodo que se encarga de enviar un mensaje de correo a una cuenta de correo dada,
    // y con un texto en particular en formato TXT
    public void sendCheckSchedules(String to, String body) {
        String from = null, url = null;
        setUpMail(465);
        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail

        // Get the email address of "from","to" and "cc"
        from = TMSConfigurator.getDefault_mail_address();
        subject = "Verificacion de Agendas y Horarios";

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP

            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            // Recipiente del email
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            // add the Multipart to the message
            msg.setText(body);

            // send the message
            transport.sendMessage(msg, msg.getAllRecipients());
            //Transport.send(msg);
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }

    }

    // Metodo que se encarga de enviar un mensaje de correo a una cuenta de correo dada,
    // y con un texto en particular en formato TXT
public void sendCheckTasks(String to, String body) {
        String from = null, url = null;
        setUpMail(465);
        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail

        // Get the email address of "from","to" and "cc"
        from = TMSConfigurator.getDefault_mail_address();
        subject = "Verificacion de Tareas";

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP

            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            // Recipiente del email
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            // add the Multipart to the message
            msg.setContent(body, "text/html");
//            msg.setText(body);

            // send the message
            transport.sendMessage(msg, msg.getAllRecipients());
// Transport.send(msg);
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

    // Metodo que se encarga de enviar un mensaje de correo a una cuenta de correo dada,
    // y con un texto en particular en formato HTML
    public void sendCheckTickets(String to, String body) {
        String from = null, url = null;

        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail
        boolean flag= true;
        setUpMail(465);
        // Get the email address of "from","to" and "cc"
        from = TMSConfigurator.getDefault_mail_address();
        subject = "Verificacion de Tickets";

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP

            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            // Recipiente del email
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(from, false));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            // add the Multipart to the message
            msg.setContent(body, "text/html");

            // send the message
           // Transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "sendCheckTickets",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Metodo que se encarga de realizar el envio de un email genera a todos los usuarios
     * que vienen indicados como parametro
     */
    public void sendBroadcast(java.util.ArrayList emails, String subject, String body) {
        String from = null, url = null;
        setUpMail(587);
        String protocol = null, host = null, user = null, password = null;

        // Get the email address of "from","to" and "cc"
        from = TMSConfigurator.getDefault_mail_address();

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP

            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            // Se procesa cada uno de los emails
            for (int i = 0; i < emails.size(); i++) {
                // construct the message
                try {
                    Message msg = new MimeMessage(session);

                    if (from != null) {
                        msg.setFrom(new InternetAddress(from));
                    } else {
                        msg.setFrom();
                    }

                    // Recipiente del email
                    msg.setRecipient(Message.RecipientType.TO, new InternetAddress((String) emails.get(i)));

                    msg.setSubject(subject);

                    msg.setHeader("X-Mailer", mailer);
                    msg.setSentDate(new Date());

                    // add the Multipart to the message
                    msg.setContent(body, "text/html");

                    // send the message
                   // Transport.send(msg);
                    transport.sendMessage(msg, msg.getAllRecipients());
                } catch (Exception exc) {
                    System.out.println("Exception in broadcast " + exc.toString());
                }
            }
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "sendCheckTickets",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void collect(String operation, int type, int id, int fromid, int toid, String extra, MimeBodyPart mbp, loginData login, String url) throws MessagingException, IOException {

        String addr = "";
        try {
            String extra_param = "";
            if (operation.equals("quotesended") || operation.equals("quoterejected") || operation.equals("newUser") || operation.equals("alterUser") || operation.equals("alterUserPass") || operation.equals("quoteacepted") || operation.equals("newTopic") || operation.equals("newTopicToCreator") ||
                    operation.equals("taskrejected") || operation.equals("newPost") || operation.equals("meetingrequest")) {
                extra_param = "&extra=" + extra;
            }

            //addr = TMSConfigurator.getMainURL() + "/mail.do?operation=" + operation + "&id=" + id + "&fromid=" + fromid + "&toid=" + toid + "&type=" + type + extra_param + "&userAccount=" + login.getId_account();
addr = url + "/mail.do?operation=" + operation + "&id=" + id + "&fromid=" + fromid + "&toid=" + toid + "&type=" + type + extra_param + "&userAccount=" + login.getId_account() ;

//logger.logp(Level.INFO, "send.do", "send",
            //        addr);

            URL news = new URL(addr);
            BufferedReader in = new BufferedReader(new InputStreamReader(news.openStream()));
            String line;
            StringBuffer sb = new StringBuffer();
            // Se procesa cada linea de texto que venga como parte del cuerpo del aviso
            while ((line = in.readLine()) != null) {

                sb.append(line);
                if (type == 0) {
                    sb.append("\n");
                }
            }
            String contenttype;
            if (type == 0) {
                contenttype = "text/plain";
            } else {
                contenttype = "text/html";
            }
            mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), contenttype)));
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "collect",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }


    // Metodo que se encarga de enviar un mensaje de correo a una cuenta de correo dada,
    // y con un texto en particular en formato TXT
   public void sendReport(String subject, String to, FileOutputStream out, String name,String peri) {
        String from = null;
        // Get the email address of "from","to" and "cc"
        from = TMSConfigurator.getDefault_mail_address();
        setUpMail(587);
        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP

            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            // Recipiente del email
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            msg.setSubject(subject);

            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();

            // attach the file to the message
            FileDataSource fds = new FileDataSource("Informe_Laboral_"+peri+"_"+name+".xls");
            mbp1.setDataHandler(new DataHandler(fds));
            mbp1.setFileName(fds.getName());

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);

            // add the Multipart to the message
            msg.setContent(mp);

            // add the Multipart to the message
            msg.setContent(mp);
//            msg.setText(body);

            // send the message
            //Transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
    }

          public void sendNotification(accountsData account, int daysLeft, int countUsers, int select, String mainUrl) {
        String from = null, to = null, cc = null, bcc = TMSConfigurator.getSales_mail(), url = null;
        setUpMail(587);
        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail
        boolean debug = false;
        // Get the email address of "from","to" and "cc"

        from = TMSConfigurator.getSales_mail();
        to = account.getAccount_email();
        subject = labels.getString("email.endoftrial.subject")+"-"+ account.getCompany() +" ("+countUsers+" Usuarios)";

        try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }
            if (bcc != null) {
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            String text = "";
            java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

            text += labels.getString("html.html");
            text += labels.getString("html.head");
            text += labels.getString("html.metainf");
            text += labels.getString("html.head0");
            text += labels.getString("html.body");
            text += labels.getString("email.header");
            text += labels.getString("html.br");
            text += labels.getString("html.br");

            //text += labels.getString("html.arrow") + TMSConfigurator.getMainURL();
            text += labels.getString("html.arrow") + mainUrl;
            text += labels.getString("html.arrow0");
            text += labels.getString("html.face");
            switch(select)
            {
                case 0:
            // add the Multipart to the message
            text += labels.getString("email.endoftrial.subject");
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");

            text += labels.getString("email.endoftrial.body0") + df.format(account.getFinal_trial_date());
            text += labels.getString("email.endoftrial.body1") + daysLeft+ " ";
            text += labels.getString("email.endoftrial.body2") ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.body3") ;
         //   text += labels.getString("html.ref") ;
            text += " eduardo.gutierrez@daytonasoft.com ";
            text += labels.getString("email.endoftrial.body4") ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer0") ;
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer1") ;
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer2");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer3") ;
            text += labels.getString("html.br");

             subject = labels.getString("email.endoftrialusersrenew.subject")+" - "+account.getCompany() +" ("+countUsers+" Usuarios)";

                break;

                case 1:
              // add the Multipart to the message
            text += labels.getString("email.endoftrial.subject");
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.header")  ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.afterendoftrial.body0") ;
            //text += labels.getString("html.ref") ;

            text += " eduardo.gutierrez@daytonasoft.com ";
            text += labels.getString("email.afterendoftrial.body1") ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer0");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer1") ;
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer2");
            text += labels.getString("html.br");

            subject = labels.getString("email.endoftrialusersrenew.subject")+" - "+account.getCompany() +" ("+countUsers+" Usuarios)";

               break;

                case 2:
            text += labels.getString("email.endoftrial.subject");
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.header")  ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.lastUserExpired.body0");
            text += " eduardo.gutierrez@daytonasoft.com ";
            text += labels.getString("email.lastUserExpired.body1");
            text += labels.getString("html.br");
             text += labels.getString("html.br");
            text += labels.getString("email.lastUserExpired.body1") ;
             text += labels.getString("html.br");
             text += labels.getString("html.br");
            text += labels.getString("email.lastUserExpired.body2") ;
             text += labels.getString("html.br");
             text += labels.getString("html.br");
            text += labels.getString("email.lastUserExpired.body3") ;

            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer0");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer1") ;
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer2");
            text += labels.getString("html.br");

            }
            text += labels.getString("html.html0");
            text += labels.getString("html.body0");
            msg.setSubject(subject);
            text= MimeUtility.encodeText(text, "iso-8859-1", "Q");
            msg.setContent(text, "text/html");
            // send the message
            //Transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }


    }


       //Notificacion de Usuarios Vencidos
       public void sendNotificationOfUsers(accountsData account, Iterator it , int select, String mainUrl) {
        String from = null, to = null, cc = null, bcc = TMSConfigurator.getSales_mail(), url = null;

        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail
        boolean debug = false;
        // Get the email address of "from","to" and "cc"
        setUpMail(587);
        int countUsers=0;

        java.util.Calendar now= java.util.Calendar.getInstance();
        java.util.Calendar cal= java.util.Calendar.getInstance();

        from = TMSConfigurator.getSales_mail();
        to = account.getAccount_email();

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }


            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            if (cc != null) {
                msg.setRecipients(Message.RecipientType.CC,
                        InternetAddress.parse(cc, false));
            }
            if (bcc != null) {
                msg.setRecipients(Message.RecipientType.BCC,
                        InternetAddress.parse(bcc, false));
            }
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            int count=0;
            String text = "";

           // add the Multipart to the message

            text += labels.getString("html.html");
            text += labels.getString("html.head");
            text += labels.getString("html.metainf");
            text += labels.getString("html.head0");
            text += labels.getString("html.body");
            text += labels.getString("email.header");
            text += labels.getString("html.br");
           // text += labels.getString("html.arrow") + TMSConfigurator.getMainURL();
           text += labels.getString("html.arrow") +  mainUrl;
            text += labels.getString("html.arrow0");

            membersBroker memberBroker= new membersBroker();
            membersData mdata= new membersData();

            Iterator e= memberBroker.getList(account.getId());
            while (e.hasNext()){countUsers++; e.next();  }
            switch(select){
                case 0:

            text += labels.getString("html.face");
            text += labels.getString("email.endoftrialusersrenew.subject");
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.header")    ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrialusers.body0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.hr");
            text += labels.getString("html.table");
            text += labels.getString("html.tr");
            text += labels.getString("html.th")+ "Nombre" +labels.getString("html.th0");
            text += labels.getString("html.th")+ "Usuario" +labels.getString("html.th0");
            text += labels.getString("html.th")+ "Fecha vencimiento (DD/MM/AAAA)" +labels.getString("html.th0");
            text += labels.getString("html.th")+  "Dias Disponibles" +labels.getString("html.th0");
            text += labels.getString("html.tr0");

            count=0;
            while (it.hasNext())
            {
                 java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                 mdata= (membersData)it.next();

                 cal.setTime(mdata.getExpired_date());

                 int c1= cal.get(java.util.Calendar.DAY_OF_YEAR);
                 int c2= now.get(java.util.Calendar.DAY_OF_YEAR);
                 int daysLeft= c1-c2;

                 text += labels.getString("html.tr");
                 text += labels.getString("html.td")+ mdata.getname() +  labels.getString("html.td0");
                 text += labels.getString("html.td")+ mdata.getlogin() +  labels.getString("html.td0");
                 text += labels.getString("html.td")+ df.format(mdata.getExpired_date()) +  labels.getString("html.td0");
                 text += labels.getString("html.td")+ daysLeft +  labels.getString("html.td0");
                 text += labels.getString("html.tr0");
                 count++;
            }

            text += labels.getString("html.table0");
            text += labels.getString("html.hr");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrialusers.body1");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer0");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer1");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer2");
             subject = labels.getString("email.endoftrialusersrenew.subject")+" - <"+account.getCompany() +"> ("+count+" Usuarios)";

                break;

                case 1:
            text += labels.getString("html.face");
            text += labels.getString("email.endoftrialusers.subject");
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.header")    ;
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.html");
            text += labels.getString("email.afterendoftrialusers.body0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.hr");
            text += labels.getString("html.table");
            text += labels.getString("html.tr");
            text += labels.getString("html.th")+"Nombre"+labels.getString("html.th0");
            text += labels.getString("html.th")+ "Usuario" +labels.getString("html.th0");
            text += labels.getString("html.th")+ "Fecha vencimiento (DD/MM/AAAA)" +labels.getString("html.th0");
            text += labels.getString("html.th")+ "Dias Vencidos" +labels.getString("html.th0");

            text += labels.getString("html.tr0");

            count=0;
            while (it.hasNext())
            {
                 java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                 mdata= (membersData)it.next();

                 cal.setTime(mdata.getExpired_date());
                 int c1= cal.get(java.util.Calendar.DAY_OF_YEAR);
                 int c2= now.get(java.util.Calendar.DAY_OF_YEAR);
                 int daysLeft= c2-c1;

                 text += labels.getString("html.tr");
                 text += labels.getString("html.td")+ mdata.getname() +  labels.getString("html.td0");
                 text += labels.getString("html.td")+ mdata.getlogin() +  labels.getString("html.td0");
                 text += labels.getString("html.td")+ df.format(mdata.getExpired_date()) +  labels.getString("html.td0");
                 text += labels.getString("html.td")+ daysLeft +  labels.getString("html.td0");
                 text += labels.getString("html.tr0");
                 count++;
            }

            text += labels.getString("html.table0");
            text += labels.getString("html.hr");
             text += labels.getString("html.br");
             text += labels.getString("html.br");
            text += labels.getString("email.afterendoftrialusers.body1");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer0");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer1");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer2");


                subject = labels.getString("email.endoftrialusers.subject")+" - <"+account.getCompany() +"> ("+count+" Usuarios)";

                break;
            }

            text += labels.getString("html.body0");
            text += labels.getString("html.html0");
            msg.setSubject(subject);
            text= MimeUtility.encodeText(text, "iso-8859-1", "Q");
            msg.setContent(text, "text/html");

            try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
            } catch (Exception ex) {
            ex.printStackTrace();
        }
            // send the message
           // Transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }


    }

   /*    public void sendAM(){
                   ArrayList listat= new ArrayList();
            int x;
            for (x=0 ; x < 9; x++ ){
                boolean flag=true;
                while (flag){
                int m= (int)(Math.random() * 9);

                         if (!check(m, listat) && x!=m){
                             System.out.println(" "+x+":"+m);
                             listat.add((Integer.valueOf(m)));
                             flag=false;
                        }else{
                            flag=true;
                        }

                }
            }
                int j=0;
       for (j=0; j<=8 ; j++ ){
            String from = null, to = null, cc = null, bcc = TMSConfigurator.getSales_mail(), url = null;
//
        String protocol = null, host = null, user = null, password = null, subject = null;
        String record = null;	// name of folder in which to record mail
        boolean debug = false;
        // Get the email address of "from","to" and "cc"


        from = TMSConfigurator.getDefault_mail_address();
        to = getname(j)[0];

        try {
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null) {
                props.put("mail.smtp.host", mailhost);
            }

            if (debug) {
                session.setDebug(false);;
            }

            // construct the message
            Message msg = new MimeMessage(session);

            if (from != null) {
                msg.setFrom(new InternetAddress(from));
            } else {
                msg.setFrom();
            }
//
//
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
          
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());

            String text = "";

//           // add the Multipart to the message

            text += labels.getString("html.html");
            text += labels.getString("html.head");
            text += labels.getString("html.metainf");
            text += labels.getString("html.head0");
            text += labels.getString("html.body");
            text += labels.getString("email.header");
            text += labels.getString("html.br");
            text += labels.getString("html.arrow") + TMSConfigurator.getMainURL();
            text += labels.getString("html.arrow0");

            text += labels.getString("html.face");
//            text += labels.getString("email.endoftrialusersrenew.subject");
//            text += labels.getString("html.face0");

                subject = "Amigo Secreto 2012 DEFINITIVO!!";
//
//
            text +=  getname(j)[1]+ ", " ;

            Object obj= listat.get(j);  text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
            text += "Para el amigo secreto del 2012 estamos utilizando lo ultimo en tecnologia de DaytonaSoft y tenemos el agrado de informarle que usted es el amigo secreto de: <b>"+ getname(Integer.parseInt(obj.toString()))[1] + "</b>" ;
            text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
            text += "Cuota aproximada del Regalo: <b> 20000 colones </b> , fecha de entrega del regalo Viernes 21 de Diciembre hora y lugar por definir.";
                        text += labels.getString("html.face0");
            text += labels.getString("html.br");
            text += labels.getString("html.br");
            text += labels.getString("html.face1");
            text += labels.getString("email.endoftrial.footer0");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer1");
            text += labels.getString("html.br");
            text += labels.getString("email.endoftrial.footer2");
                        text += labels.getString("html.face0");
            text += labels.getString("html.html0");
            msg.setSubject(subject);
            text= MimeUtility.encodeText(text, "iso-8859-1", "Q");
            msg.setContent(text, "text/html");

            try {
            subject = MimeUtility.encodeText(subject, "iso-8859-1", "Q");
            } catch (Exception ex) {
            ex.printStackTrace();
        }
            // send the message
            Transport.send(msg);
        } catch (Exception e) {
            logger.logp(Level.SEVERE, "send.do", "send",
                    "Fatal Error: " + e.toString());
            e.printStackTrace();
        }
        }

       }

public boolean check(int id, ArrayList lista){
    int j=0;
    boolean flag= false;
    for (j=0 ; j < lista.size(); j++){
        String str= lista.get(j).toString();
        if(Integer.parseInt(str)==id){
            flag= true;
        }
    }
    return flag;
}
public String[] getname(int i) {
        String []nom= new String[2];
            switch(i){
                    case 0:
                  //  nom[0]="alberto.campos@daytonasoft.com";
                        nom[0]= "luisangel.cardenas@daytonasoft.com";
                        nom[1]="Luis";
                        break;
                    case 1:
                   // nom[0]="alberto.campos@daytonasoft.com";
                        nom[0]= "eduardo.gutierrez@daytonasoft.com";
                        nom[1]="Eduardo";
                        break;
                case 2:
                    //nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="victor.lara@daytonasoft.com";
                       nom[1]="Victor";
                    break;
                case 3:
                   // nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="gabriel.gonzalez@daytonasoft.com";
                       nom[1]="Gabriel";
                    break;
                case 4:
                //    nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="richard.morales@daytonasoft.com";
                        nom[1]="Richard";
                    break;
                case 5:
                  //  nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="rilie.esquivel@daytonasoft.com";
                        nom[1]="Rilie";
                    break;
                case 6:
                  //  nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="adams.mora@daytonasoft.com";
                       nom[1]="Adams";
                    break;
                case 7:
                //    nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="walter.ramirez@daytonasoft.com";
                        nom[1]="Walter";
                    break;
                case 8:
                   // nom[0]="alberto.campos@daytonasoft.com";
                    nom[0]="alberto.campos@daytonasoft.com";
                        nom[1]="Alberto";
                    break;
        }
            return nom;
}*/

    class MyAuthenticator extends Authenticator {

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(TMSConfigurator.getDefault_mail_username(), TMSConfigurator.getDefault_mail_password());
        }
    }
}
