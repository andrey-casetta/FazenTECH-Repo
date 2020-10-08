/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SourcePackage.Servlets;

import Classes.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author andre
 */
@WebServlet(name = "ReceiverLoginServlet", urlPatterns = {"/ReceiverLoginServlet"})
public class ReceiverLoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReceiverServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReceiverServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        DBConnection dbConnection = new DBConnection();

        boolean _resultadoConsulta = dbConnection.ConnectAndLogin(username, password);

        PrintWriter writer = response.getWriter();
        String htmlRespone = "";

        if (_resultadoConsulta) {
            htmlRespone += "<script src='https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/6.11.4/sweetalert2.all.js'></script>";
            htmlRespone += "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>";
            htmlRespone += "<script>";
            htmlRespone += "$(document).ready(function(){";
            htmlRespone += "swal ('Login realizado!!!', ' Prossiga para Sistema ' , 'success' )";
            htmlRespone += "});";
            htmlRespone += "</script>";

            writer.println(htmlRespone);
            RequestDispatcher dp = request.getRequestDispatcher("LoginRealizado.html");
            dp.include(request, response);

        } else {

            htmlRespone += "<script src='https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/6.11.4/sweetalert2.all.js'></script>";
            htmlRespone += "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>";
            htmlRespone += "<script>";
            htmlRespone += "$(document).ready(function(){";
            htmlRespone += "swal ('Login/Senha incorretos!!!', ' Tente Novamente ' , 'error' )";
            htmlRespone += "});";
            htmlRespone += "</script>";

            writer.println(htmlRespone);
            RequestDispatcher dp = request.getRequestDispatcher("index.html");
            dp.include(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
