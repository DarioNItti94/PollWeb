/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.result.SplitSlashesFmkExt;
import iw.framework.result.TemplateManagerException;
import iw.framework.result.TemplateResult;
import iw.framework.security.SecurityLayer;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
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
@WebServlet(name = "SurveyController", urlPatterns = {"/survey"})
public class SurveyController extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException, TemplateManagerException, DataException {
        int surveyid = SecurityLayer.checkNumeric(request.getParameter("survey"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyid);
        request.setAttribute("survey", survey);
        HttpSession s = SecurityLayer.checkSession(request);
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
        request.setAttribute("page_title", "login");
        res.activate("/survey.ftl.html", request, response);
        if (survey.isClosed() && survey.isActive()) {
            res.activate("/error.ftl.html", request, response);
            request.setAttribute("error", "il sondaggio è chiuso");
        } else {
            response.sendRedirect("survey?survey=" + surveyid);
        }

    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        if (survey != null) {
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().deleteSurvey(surveyID);
            response.sendRedirect("profile");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void action_close(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException, DataException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        if (survey != null) {
            survey.setActive(false);
            survey.setClosed(true);
            response.sendRedirect("profile");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException {
        String title = request.getParameter("title");
        String open_txt = request.getParameter("opening_text");
        String close_txt = request.getParameter("closing_text");
        boolean isclosed = Boolean.parseBoolean(request.getParameter("isclosed"));
        boolean isreserved = Boolean.parseBoolean(request.getParameter("isreserved"));
        boolean isactive = Boolean.parseBoolean(request.getParameter("isactive"));
        int idsup = Integer.parseInt(request.getParameter("supervisor"));
        if (title != null && open_txt != null && close_txt != null) {
            Supervisor supervisor = new Supervisor();
            Survey survey = new Survey();
            survey.setTitle(title);
            survey.setOpeningText(open_txt);
            survey.setClosingText(close_txt);
            survey.setActive(isactive);
            survey.setClosed(isclosed);
            survey.setReserved(isreserved);
            survey.setSupervisor(supervisor);
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().storeSurvey(survey);
        } else {
            throw new ServerException("inserisci i parametri");
        }
    }

    private void action_deactivate(HttpServletRequest request, HttpServletResponse response) throws ServerException, DataException, IOException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        if (survey != null) {
            survey.setActive(false);
            response.sendRedirect("profile");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            if(request.getParameter("action_dafault")!= null){
                action_default(request, response);
            }else if(request.getParameter("action_create")!= null){
                action_create(request, response);
            }else if(request.getParameter("action_delete") != null){
                action_delete(request, response);
            }else if(request.getParameter("action_close") != null){
                action_close(request, response);
            }else if (request.getParameter("action_deactivate")!= null) {
                action_deactivate(request, response);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
