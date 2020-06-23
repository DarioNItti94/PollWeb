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
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Response;
import iw.pollweb.model.dto.Submission;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.rmi.ServerException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, TemplateManagerException, DataException, ParseException, IOException {
        s = SecurityLayer.checkSession(request);
        int SurveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(SurveyID);
        request.setAttribute("page_title", "survey");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        List<Question> questions = ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().getQuestionsBySurvey(survey);
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("questions", questions);
        request.setAttribute("survey", survey);
        res.activate("/sondaggio.ftl.html", request, response);
    }

    private void action_survey(HttpServletRequest request, HttpServletResponse response) throws ServerException, DataException, ParseException, IOException {

        //Participant participant = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().getParticipantByID((int) s.getAttribute("userid"));
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("pollID"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        List<Question> questions = ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().getQuestionsBySurvey(survey);

        if (request.getParameterMap() != null) {

            Submission submission = null;

            //submission = ((PollWebDataLayer) request.getAttribute("datalayer")).getSubmissionDAO().getSubmissionByParticipant(participant);
            List<Response> responses = new ArrayList<Response>();
            ListIterator<Question> listIter = questions.listIterator();
            boolean isvalid = true;
            while (listIter.hasNext() && isvalid) {
                Question q = listIter.next();
                int position = q.getNumber();
                Response r = ((PollWebDataLayer) request.getAttribute("datalayer")).getResponseDAO().createResponse();
                if (q.isMandatory()) {
                    String questanswer = request.getParameter("response" + position);
                    if (questanswer == null || questanswer.equals("")) {

                        isvalid = false;
                    }
                }
                switch (q.getType()) {
                    case "date":
                        String questRespo2 = request.getParameter("response" + position);
                        System.out.println("String" + questRespo2);
                        Date dateQuestanswer = new SimpleDateFormat("dd/MM/yyyy").parse(questRespo2);
                        SimpleDateFormat finalFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd");
                        if (!q.isMandatory() && questRespo2.equals("")) {
                            r.setValue("");
                            r.setQuestion(q);
                            responses.add(r);
                        } else {
                            r.setValue(questRespo2);
                            r.setQuestion(q);
                            responses.add(r);
                            break;
                        }
                    default:
                        String questanswer = request.getParameter("response" + position);
                        System.out.println("String " + questanswer);
                        if (!q.isMandatory() && questanswer.equals("")) {
                            r.setValue("");
                            r.setQuestion(q);
                            responses.add(r);
                        } else {
                            r.setValue(questanswer);
                            r.setQuestion(q);
                            responses.add(r);

                        }
                        break;
                }
            }
            if (isvalid) {
                for (Response resp : responses) {
                    response.sendRedirect("/PollWeb/");
                    ((PollWebDataLayer) request.getAttribute("datalayer")).getResponseDAO().storeResponse(resp);

                    if (submission != null) {
                        //submission.setParticipant(participant);
                        submission.setTimestamp((Timestamp) new Date());
                        ((PollWebDataLayer) request.getAttribute("datalayer")).getSubmissionDAO().storeSubmission(submission);
                    }
                }
                System.out.println("tuttok ok");

            }

        }
    }

    private void action_write(HttpServletRequest request, HttpServletResponse response, List<Response> responses, Submission submission) throws IOException {
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        HttpSession s = checkSession(request);
        try {
            if (request.getParameter("write") != null) {
                action_survey(request, response);
            } else {
                action_default(request, response, s);
            }
        } catch (TemplateManagerException ex1) {
            ex1.printStackTrace();
        } catch (ParseException ex2) {
            ex2.printStackTrace();
        } catch (IOException ex3) {
            ex3.printStackTrace();
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
