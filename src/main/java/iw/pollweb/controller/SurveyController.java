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
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException, DataException {
        HttpSession session = checkSession(request);
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
            List<Response> responses = new ArrayList<Response>();
            ListIterator<Question> listIterator = questions.listIterator();
            boolean isValid = true;
            //while(){
        }
            

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //action_default(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
