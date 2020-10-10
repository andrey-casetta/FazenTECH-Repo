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
import java.util.ArrayList;
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
@WebServlet(name = "CadastroAnimaisServlet", urlPatterns = {"/CadastroAnimaisServlet"})
public class CadastroAnimaisServlet extends HttpServlet {

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

        String sIdAnimal = request.getParameter("idAnimal");
        String sTipoTratamento = request.getParameter("tipoTratamento");
        String sIdAnimalConsulta = request.getParameter("idAnimalConsulta");
        String sDias = request.getParameter("qtdDias");

        String sTipo = request.getParameter("tipo");

        String htmlResponse = "";
        htmlResponse += "<script src='https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/6.11.4/sweetalert2.all.js'></script>";;;;;
        htmlResponse += "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>";
        htmlResponse += "<script>";

        PrintWriter writer = response.getWriter();

        if (sTipo.equals("cadastro")) {

            int idAnimal;
            int iDias = parseInt(sDias);
            idAnimal = parseInt(sIdAnimal);

            SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm");

            boolean sucesso = false;

            if (Usuario.isLogado()) {
                DBConnection db = new DBConnection();
                sucesso = db.CadastrarAnimal(idAnimal, sTipoTratamento, iDias);

            } else {
                System.out.println("Usuário não logado!!!");
            }

            if (sucesso) {
                htmlResponse += "$(document).ready(function(){";
                htmlResponse += "swal ('Cadastro Realizado com Sucesso!', '' , 'success' )";
                htmlResponse += "});";
                htmlResponse += "</script>";

                writer.println(htmlResponse);
                RequestDispatcher dp = request.getRequestDispatcher("MonitoramentoAnimais.html");
                dp.include(request, response);

            } else {
                htmlResponse += "$(document).ready(function(){";
                htmlResponse += "swal ('Cadastro nao pode ser realizado!!!', ' Tente Novamente ' , 'error' )";
                htmlResponse += "});";
                htmlResponse += "</script>";

                writer.println(htmlResponse);
                RequestDispatcher dp = request.getRequestDispatcher("MonitoramentoAnimais.html");
                dp.include(request, response);
            }
        } else {

            //codigo consulta animal
            if (Usuario.isLogado()) {
                DBConnection db = new DBConnection();
                int idAnimal;

                if (sIdAnimalConsulta.isEmpty()) {
                    idAnimal = 0;
                } else {
                    idAnimal = parseInt(sIdAnimalConsulta);
                }
                List<List<String>> resultList = new ArrayList();
                resultList = db.ConsultarAnimal(idAnimal);

                if (!resultList.isEmpty()) {

                    int size = resultList.size();

                    htmlResponse = "<br>";
                    htmlResponse += "<div id='divTable'>";
                    htmlResponse += "<table class='w3 table w3-card-4'>";
                    htmlResponse += "<tr>";
                    htmlResponse += "<td>Identificacao Animal</td>";
                    htmlResponse += "<td>Tratamento</td>";
                    htmlResponse += "<td>Quantidade de Dias</td>";
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
                    RequestDispatcher dp = request.getRequestDispatcher("MonitoramentoAnimais.html");
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
