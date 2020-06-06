/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.result.FailureResult;
import iw.framework.result.SplitSlashesFmkExt;
import iw.framework.result.TemplateManagerException;
import iw.framework.result.TemplateResult;
import iw.framework.security.SecurityLayer;
import static iw.framework.security.SecurityLayer.checkSession;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.rmi.ServerException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dario
 */
public class SurveyController extends BaseController {

    protected void action_error(HttpServletRequest request, HttpServletResponse response) {

        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }

    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServerException, IOException, TemplateManagerException, DataException {
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(parseInt(request.getParameter("id")));
        if (survey.isClosed()) {
            request.setAttribute("message", "questo sondaggio è chiuso");
            action_error(request, response);
        } else {
            request.setAttribute("page_title", "survey");
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            List<Question> questions = ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().getQuestionsBySurvey(survey);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("/sondaggio.ftl.html", request, response);
            request.setAttribute("questions", questions);
            request.setAttribute("survey", survey);
            res.activate("/sondaggio.ftl.html", request, response);
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
            Survey survey = new iw.pollweb.model.dto.Survey();
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
             if (request.getParameter("action_create") != null) {
                action_create(request, response);
            } else if (request.getParameter("action_delete") != null) {
                action_delete(request, response);
            } else if (request.getParameter("action_close") != null) {
                action_close(request, response);
            } else if (request.getParameter("action_deactivate") != null) {
                action_deactivate(request, response);
            }else{
                action_default(request, response);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
