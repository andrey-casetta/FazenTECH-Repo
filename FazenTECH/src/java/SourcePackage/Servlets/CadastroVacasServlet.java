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
import java.sql.Timestamp;
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
import javax.servlet.RequestDispatcher;
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

        String htmlResponse = "";
        htmlResponse += "<script src='https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/6.11.4/sweetalert2.all.js'></script>";;;;;
        htmlResponse += "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>";
        htmlResponse += "<script>";

        PrintWriter writer = response.getWriter();

        if (sTipo.equals("cadastro")) {

            int idVaca;
            double iQtdLeite;

            idVaca = parseInt(sIdVaca);
            iQtdLeite = parseInt(sQtdLeiteVaca);
            Date date = null;
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            try {
                date = sdf.parse(sData);
            } catch (ParseException ex) {
                Logger.getLogger(CadastroVacasServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            Timestamp ts = new Timestamp(date.getTime());

            boolean sucesso = false;

            if (Usuario.isLogado()) {
                DBConnection db = new DBConnection();
                sucesso = db.CadastrarVaca(idVaca, iQtdLeite, ts);
            } else {
                System.out.println("Usuário não logado!!!");
            }

            if (sucesso) {
                htmlResponse += "$(document).ready(function(){";
                htmlResponse += "swal ('Cadastro Realizado com Sucesso!', '' , 'success' )";
                htmlResponse += "});";
                htmlResponse += "</script>";

                writer.println(htmlResponse);
                RequestDispatcher dp = request.getRequestDispatcher("MonitoramentoVacas.html");
                dp.include(request, response);

            } else {
                htmlResponse += "$(document).ready(function(){";
                htmlResponse += "swal ('Cadastro nao pode ser realizado!!!', ' Tente Novamente ' , 'error' )";
                htmlResponse += "});";
                htmlResponse += "</script>";

                writer.println(htmlResponse);
                RequestDispatcher dp = request.getRequestDispatcher("MonitoramentoVacas.html");
                dp.include(request, response);
            }
        } else {
            //codigo consulta vaca
            if (Usuario.isLogado()) {
                DBConnection db = new DBConnection();
                int idVaca;

                if (sIdVacaConsulta.isEmpty()) {
                    idVaca = 0;
                } else {
                    idVaca = parseInt(sIdVacaConsulta);
                }

                List<List<String>> resultList = null;
                resultList = db.ConsultarVaca(idVaca, sLogin);

                if (!resultList.isEmpty()) {

                    htmlResponse = "<br>";
                    htmlResponse += "<div id='divTable'>";
                    htmlResponse += "<table>";
                    htmlResponse += "<tr class='headerTR'>";
                    htmlResponse += "<td>Identificacao Vaca</td>";
                    htmlResponse += "<td>Quantidade Leite (L)</td>";
                    htmlResponse += "<td>Data</td>";
                    htmlResponse += "<td>Usuario</td>";
                    htmlResponse += "</tr>";

                    for (int i = 0; i < resultList.size(); i++) {
                        htmlResponse += "<tr>";
                        for (int j = 0; j < resultList.get(i).size(); j++) {
                            htmlResponse += "<td>" + resultList.get(i).get(j) + "</td>";
                        }
                        htmlResponse += "</tr>";
                    }

                    htmlResponse += "</table>";
                    htmlResponse += "</div><br>";

                    writer.println(htmlResponse);
                    RequestDispatcher dp = request.getRequestDispatcher("MostrarDadosBusca.html");
                    dp.include(request, response);

                } else {

                    htmlResponse += "$(document).ready(function(){";
                    htmlResponse += "swal ('Busca nao encontrou resultados!!!', ' Tente Novamente ' , 'error' )";
                    htmlResponse += "});";
                    htmlResponse += "</script>";

                    writer.println(htmlResponse);
                    RequestDispatcher dp = request.getRequestDispatcher("MonitoramentoVacas.html");
                    dp.include(request, response);
                }

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
