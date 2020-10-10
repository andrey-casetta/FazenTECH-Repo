/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SourcePackage.Servlets;

import Classes.DBConnection;
import Classes.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import microsoft.sql.Types;

/**
 *
 * @author andre
 */
@WebServlet(name = "CadastroVacasServlet", urlPatterns = {"/CadastroVacasServlet"})
public class CadastroVacasServlet extends HttpServlet {

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
            out.println("<title>Servlet CadastroVacasServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CadastroVacasServlet at " + request.getContextPath() + "</h1>");
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

        String sIdVaca = request.getParameter("idVaca");
        String sQtdLeiteVaca = request.getParameter("qtdLeiteVaca");
        String sIdVacaConsulta = request.getParameter("idVacaConsulta");
        String sLogin = request.getParameter("loginUsuario");

        String sData = request.getParameter("data");
        String sTipo = request.getParameter("tipo");

        if (sTipo.equals("cadastro")) {

            int idVaca;
            double iQtdLeite;

            idVaca = parseInt(sIdVaca);
            iQtdLeite = parseInt(sQtdLeiteVaca);

            SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm");

            Date date = null;
            try {
                date = dt.parse(sData);
            } catch (ParseException ex) {
                Logger.getLogger(CadastroVacasServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(date);
            boolean sucesso = false;

            if (Usuario.isLogado()) {
                DBConnection db = new DBConnection();
                sucesso = db.CadastrarVaca(idVaca, iQtdLeite, date);
            } else {
                System.out.println("Usuário não logado!!!");
            }

            if (sucesso) {
                PrintWriter writer = response.getWriter();
                String htmlRespone = "<html>";
                htmlRespone += "<h2>Cadastro realizado com sucesso!!!</h2>";
                htmlRespone += "</html>";

                writer.println(htmlRespone);

            } else {
                PrintWriter writer = response.getWriter();
                String htmlRespone = "<html>";
                htmlRespone += "<h2>Houve um erro no cadastro!!!</h2>";
                htmlRespone += "</html>";

                writer.println(htmlRespone);
            }
        } else {
            //codigo consulta vaca
            if (Usuario.isLogado()) {
                DBConnection db = new DBConnection();
                int idVaca = parseInt(sIdVacaConsulta);
                List<String> resultList = null;
                resultList = db.ConsultarVaca(idVaca, sLogin);
            } else {
                System.out.println("Busca não retornou resultados!!!");
            }
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
