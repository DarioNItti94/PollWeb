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
import iw.framework.utils.EmailSender;
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dario
 */
public class ParticipantController extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "home");
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        res.activate("/addpart.ftl.html", request, response);

    }

    private void action_add(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException, IOException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        if (survey == null || !(survey.isReserved())) {
            action_error(request, response);
        }
        String email = (request.getParameter("email"));
        String password = PasswordUtility.generateRandomPassword();
        if (email == null) {
            throw new ServletException("Parametro mancante");
        }
        List<String> emails = Arrays.asList(email.split(", "));
        survey.getParticipants().add()
        for (String mail : emails) {
            
        }
        participant.setHashedPassword(password);
        participant.setSurvey(survey);
        if (survey.getParticipants().contains(participant)) {
            throw new ServletException("partecipante già registrato");
        }
        ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().storeParticipant(participants);
        response.sendRedirect("/profile");

        String mittente = "pollweb2020@gmail.com";
        String pass = "We_PollWeb_2020";
        String obj = "C'è un sonadggio per te";
        String url = "http://localhost:8080/PollWeb/login";
        String testo = "Sei stato invitato ad un nuovo sondaggio le tue credenziali sono: \n\n" + "Email:  " + email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;

        EmailSender.send(mittente, pass, email, obj, testo);
    }

    private void action_delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException {

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            if (request.getParameter("add") != null) {
                action_add(request, response);
            } else if (request.getParameter("delete") != null) {
                action_delete(request, response);
            } else {
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
