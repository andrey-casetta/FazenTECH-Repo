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
@WebServlet(name = "ReceiverCadastroServlet", urlPatterns = {"/ReceiverCadastroServlet"})
public class ReceiverCadastroServlet extends HttpServlet {

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
            out.println("<title>Servlet ReceiverCadastroServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReceiverCadastroServlet at " + request.getContextPath() + "</h1>");
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
        String _cpf = request.getParameter("cpf");
        boolean _resultadoConsulta;
        DBConnection dbConnection = new DBConnection();

        _resultadoConsulta = dbConnection.ConsultarCPF(_cpf);
        PrintWriter writer = response.getWriter();
        String htmlRespone = "";

        if (_resultadoConsulta) {
            htmlRespone += "<script src='https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/6.11.4/sweetalert2.all.js'></script>";
            htmlRespone += "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>";
            htmlRespone += "<script>";
            htmlRespone += "$(document).ready(function(){";
            htmlRespone += "swal ('CPF Encontrado!!!', ' Prossiga para Cadastro ' , 'success' )";
            htmlRespone += "});";
            htmlRespone += "</script>";

            writer.println(htmlRespone);
            RequestDispatcher dp = request.getRequestDispatcher("NovoCadastro.html");
            dp.include(request, response);

        } else {

            htmlRespone += "<script src='https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/6.11.4/sweetalert2.all.js'></script>";
            htmlRespone += "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>";
            htmlRespone += "<script>";
            htmlRespone += "$(document).ready(function(){";
            htmlRespone += "swal ('CPF incorreto/inexistente!!!', ' Tente Novamente ' , 'error' )";
            htmlRespone += "});";
            htmlRespone += "</script>";

            writer.println(htmlRespone);
            RequestDispatcher dp = request.getRequestDispatcher("ConsultarCPF.html");
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
