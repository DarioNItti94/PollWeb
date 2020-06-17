/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.result.SplitSlashesFmkExt;
import iw.framework.result.StreamResult;
import iw.framework.result.TemplateManagerException;
import iw.framework.result.TemplateResult;
import iw.framework.security.SecurityLayer;
import iw.framework.utils.EmailSender;
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author dario
 */
public class ActionOnSurvey extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServerException, TemplateManagerException, DataException {
        HttpSession s = SecurityLayer.checkSession(request);
        int SurveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(SurveyID);
        request.setAttribute("survey", survey);
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
        request.setAttribute("page_title", "login");
        res.activate("/modifica-sondaggio.ftl.html", request, response);

    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        if (survey != null) {
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().deleteSurvey(surveyID);
            response.sendRedirect("/PollWeb/profile");
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

    private void action_deactivate(HttpServletRequest request, HttpServletResponse response) throws ServerException, DataException, IOException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        if (survey != null) {
            survey.setActive(false);
            response.sendRedirect("profile");
        }
    }

    private void action_dwn(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int suvreyID = SecurityLayer.checkNumeric(request.getParameter("survey"));
            Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(suvreyID);
            File temp = File.createTempFile(survey.getTitle() + " results", ".xml");
            JAXBContext ctx = JAXBContext.newInstance(Survey.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(survey, temp);
            StreamResult result = new StreamResult (getServletContext());
            result.activate(temp, request, response);
            response.sendRedirect("/PollWeb/profile");
        } catch (DataException | IOException | JAXBException exception) {
            throw new ServletException(exception);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            if (request.getAttribute("create") != null) {
                action_default(request, response);
            } else if (request.getAttribute("delete") != null) {
                action_delete(request, response);
            } else if (request.getAttribute("close") != null) {
                action_close(request, response);
            }

        } catch (Exception e) {
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
