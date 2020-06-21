/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.result.FailureResult;
import iw.pollweb.model.PollWebDataLayer;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author dario
 */
public abstract class BaseController extends HttpServlet {

    @Resource(name = "jdbc/pollwebdb")
    private DataSource dataSource;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try (PollWebDataLayer dataLayer = new PollWebDataLayer(dataSource)) {
            dataLayer.init();
            request.setAttribute("datalayer", dataLayer);
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message", "Errore Interna");
            request.setAttribute("submessage", "Riprova più tardi");
            action_error(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    protected void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
}
