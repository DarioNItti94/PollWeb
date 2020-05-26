/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import freemarker.template.TemplateException;
import iw.framework.data.DataException;
import iw.framework.result.SplitSlashesFmkExt;
import iw.framework.result.TemplateManagerException;
import iw.framework.result.TemplateResult;
import iw.framework.security.SecurityLayer;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dario
 */
@WebServlet(name = "profile", urlPatterns = {"/profile"})
public class Profile extends BaseController {

    private void action_part(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            int partid = SecurityLayer.checkNumeric(request.getParameter("id"));
            Survey surveys = new Survey();
            if (s != null) {
                System.out.println("profilo non caricato");
            }
            Participant participant = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().getParticipantByID(partid);
            surveys = participant.getSurvey();
            request.setAttribute("survey", surveys);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
            res.activate("profile.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex) {
            ex.printStackTrace();
            System.err.println(ex);

        }
    }

    private void action_admin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null) {
                System.out.println("profilo non caricato");
            }
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveys();
            request.setAttribute("surveys", surveys);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
            res.activate("profile.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex1) {
            ex1.printStackTrace();
            System.err.println(ex1);
        }
    }

    private void action_sup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            HttpSession s = SecurityLayer.checkSession(request);
            int supid = SecurityLayer.checkNumeric(request.getParameter("id"));
            Supervisor supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisorByID(supid);
            if (s != null) {
                System.out.println("profilo non caricato");
            }
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveysBySupervisor(supervisor);
            request.setAttribute("surveys", surveys);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
            res.activate("profile.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex2) {
            ex2.printStackTrace();
            System.err.println(ex2);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("profilo-part") != null) {
                action_part(request, response);
            } else if (request.getParameter("profilo-sup") != null) {
                action_sup(request, response);
            } else if (request.getParameter("profilo-admin") != null) {
                action_admin(request, response);
            } else {
                action_error(request, response);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
