/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
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
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
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
                ListIterator<Question> questionIterator = questions.listIterator();
                boolean isvalid = true;
                while (questionIterator.hasNext() && isvalid) {
                    Question q = questionIterator.next();
                    int position = q.getNumber();
                    Response r = ((PollWebDataLayer) request.getAttribute("datalayer")).getResponseDAO().createResponse();
                    if (!q.isMandatory()) {
                        String answer = request.getParameter("answer" + position);
                        if(answer == null || answer.equals("")){
                            request.setAttribute("message", "necessario rispondere alle domande");
                            action_error(request, response);
                            isvalid = false;
                        }
                    }
                    String questans = request.getParameter("answer" + position);
                    if(q.isMandatory() && questans.equals("")){
                        r.setValue("");
                        r.setQuestion(q);
                        responses.add(r);
                    }if(isvalid){
                        action_write(request,response);
                    }
                   
                }

            }
        } catch (DataException | TemplateManagerException ex) {
            ex.printStackTrace();
            System.err.println(ex);
        }
    }
     /*private boolean checkInputValues(String[] values, String[] quest) {
        boolean isvalid = true;
        for (String qst: values) {
            if (!Arrays.stream(quest).anyMatch(qst::equals)) {
                isvalid = false;
            }
        }
        return isvalid;
    }*/
    
        private void action_write(HttpServletRequest request, HttpServletResponse response) {
            
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>



   

}
