/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.security.SecurityLayer;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dario
 */
public class ActionOnSurvey extends BaseController {
    
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
        List<Participant> participants = new ArrayList<Participant>();
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
            
            survey.setParticipants(participants);
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().storeSurvey(survey);
            response.sendRedirect("/addpart");
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

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
