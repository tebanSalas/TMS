/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unify.webcenter.tools;

import com.unify.webcenter.broker.accountsBroker;
import com.unify.webcenter.data.accountsData;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author MARCELA QUINTERO
 */
public class downloadImage extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/gif");
                OutputStream out = response.getOutputStream();
      
        
        accountsBroker accountBroker = new accountsBroker();
        try {

            accountsData data = new accountsData();
            // Get parameters
            String id = request.getParameter("id");
            data = (accountsData) accountBroker.getData(Integer.parseInt(id));
       
            System.out.println(data.getAccount_logo());

            if (data.getAccount_logo() != null) {
                String filename = data.getAccount_logo();
                String path = data.getPath(id);
                returnFile(path, out);  // Shown earlier in the chapter
               
            }
          
        } finally {
            out.close();
            accountBroker.close();

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
    public static void returnFile(String filename, OutputStream out)
            throws FileNotFoundException, IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filename));
            byte[] buf = new byte[4 * 1024];  // 4K buffer
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("Missing File: " + filename);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
