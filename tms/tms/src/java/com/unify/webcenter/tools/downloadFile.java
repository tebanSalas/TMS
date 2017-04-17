/*
 * downloadFile.java
 *
 * Created on April 29, 2003, 10:39 AM
 */
package com.unify.webcenter.tools;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.unify.webcenter.data.*;
import com.unify.webcenter.broker.*;
import com.unify.webcenter.conf.TMSConfigurator;
import com.unify.webcenter.desencrypter.DesEncrypter;
import com.unify.webcenter.desencrypter.StringEncrypter;

/**
 *
 * @author  Administrator
 * @version
 */
public class downloadFile extends HttpServlet {

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);



    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        filesBroker broker = new filesBroker();
        accountsBroker brokerAccount = new accountsBroker();
        HttpSession session;
        // Get parameters
        String id = request.getParameter("id");
        filesData data = (filesData) broker.getData(Integer.parseInt(id));

        String filename = data.getname();
        String path = data.getPath();

        if (request.getParameter("downloadPath")!= null)
        path =request.getParameter("downloadPath");
       

        //   TMSConfigurator tms= new TMSConfigurator(user);
        // Get the URL to the upload dir
        //         String path = tms.getDownloadPath();
                
        System.out.println("File to download");
        System.out.println(data.getname());
        System.out.println(data.getPath());
        System.out.println(path);
        //System.out.println("Path: "+path);
        OutputStream out = response.getOutputStream();

        // Set the headers.
        response.setContentType("application/x-download");
        
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"")  ;

        // Send the file.

        returnFile(path, out, brokerAccount, data.getId_account());  // Shown earlier in the chapter

        broker.close();
        out.close();
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    /** Returns the file to the output
     */
    public static void returnFile(String filename, OutputStream out, accountsBroker broker, int account)
            throws FileNotFoundException, IOException {
        InputStream in = null;
        try {
             System.out.println(TMSConfigurator.getEnc());
             System.out.println("FILE");
             System.out.println(filename);
            in = new BufferedInputStream(new FileInputStream(filename));
            if (TMSConfigurator.getEnc()!=null && TMSConfigurator.getEnc().equalsIgnoreCase("Y")) {
                String passPhrase = TMSConfigurator.getPassPhrase();
                StringEncrypter desEncrypter = new StringEncrypter(passPhrase);
                accountsData data = (accountsData) broker.getData(account);
                String desDecrypted = desEncrypter.decrypt(data.getKey_encriptation());
                // Create encrypter/decrypter class
                DesEncrypter encrypter = new DesEncrypter(desDecrypted);
                // DesEncrypt
                encrypter.decrypt(in,
                        out);
            } else {
                byte[] buf = new byte[4 * 1024];  // 4K buffer
                int bytesRead;
                while ((bytesRead = in.read(buf)) != -1) {
                    out.write(buf, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            System.out.println("Missing File: " + filename);
             System.out.println("Error: " + e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

   
}
