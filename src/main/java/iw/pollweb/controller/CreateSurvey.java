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
import static iw.framework.security.SecurityLayer.checkSession;
import iw.framework.utils.EmailSender;
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Choice;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
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
public class CreateSurvey extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException, DataException {
        HttpSession s = SecurityLayer.checkSession(request);
        int SurveyID = SecurityLayer.checkNumeric(request.getParameter("id"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(SurveyID);
        request.setAttribute("survey", survey);
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
        request.setAttribute("page_title", "create");
        res.activate("/Creazione-domande.ftl.html", request, response);
    }

    private void action_addPart(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, DataException, IOException {
        //prendo il sondaggio appena creato attraverso il suo id

        int surveyID = SecurityLayer.checkNumeric(request.getParameter("pollID"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        Participant participant;
        participant = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().createParticipant();

        String FName = request.getParameter("firstName");
        String LName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = PasswordUtility.generateRandomPassword();
        //creo un nuovo partecipante
        //setto i valori nel database
        if (email != null && FName != null && LName != null) {
            participant.setFirstName(FName);
            participant.setLastName(LName);
            participant.setEmail(email);
            participant.setHashedPassword(PasswordUtility.getSHA256(password));
            participant.setSurvey(survey);
            if (survey.getParticipants().contains(participant)) {
                throw new ServletException("Il participante è già registrato.");
            }
            String mittente = "pollweb2020@gmail.com";
            String pass = "We_PollWeb_2020";
            String obj = "C'è un sondaggio per te";
            String url = "http://localhost:8080/PollWeb/login";
            String testo = "Ciao " + FName + " " + LName + "\n"
                    + "Sei stato invitato ad un nuovo sondaggio le tue credenziali sono: \n\n" + "Email:  " + email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;
            EmailSender.send(mittente, pass, email, obj, testo);
            response.sendRedirect("/PollWeb/CreateSurvey?id=" + survey.getID());

            ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().storeParticipant(participant);
        }
    }

    private void action_question(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException, IOException {
        int surveyID = SecurityLayer.checkNumeric(request.getParameter("pollID"));
        Survey survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(surveyID);
        Question question;
        question = ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().createQuestion();
        String type = "";
        String typeFront = request.getParameter("type");
        switch(typeFront){
            case "Lunga":
                type = "long";
                break;
            case "Corta":
                type = "short";
                break;
            case "Data":
                type = "date";
                break;
            case "Numero":
                type = "number";
                break;
            case "Aperta con scelta singola":
                type = "single";
                break;
            case "Aperta con scelta multipla":
                type = "multiple";
                break;
        }
        String text = request.getParameter("text");
        String note = request.getParameter("note");

        boolean mandatory = request.getParameter("isMandatory")!= null;
//        boolean private = request.getParameter("isPrivate")!= null;
        int number = Integer.parseInt(request.getParameter("number"));
        if (type != null && text != null) {
            question.setType(type);
            question.setText(text);
            question.setNote(note);

            question.setMandatory(mandatory);
            question.setNumber(number);
            question.setSurvey(survey);
            if (type == "multiple" || type == "single") {
                Choice choice1;
                Choice choice2;
                Choice choice3;
                choice1 = ((PollWebDataLayer) request.getAttribute("datalayer")).getChoiceDAO().createChoice();
                choice2 = ((PollWebDataLayer) request.getAttribute("datalayer")).getChoiceDAO().createChoice();
                choice3 = ((PollWebDataLayer) request.getAttribute("datalayer")).getChoiceDAO().createChoice();
                
                String choiceValue1 = request.getParameter("value1");
                String choiceValue2 = request.getParameter("value2");
                String choiceValue3 = request.getParameter("value3");
                
                int ChoiceNumber1 = 1;
                int ChoiceNumber2 = 2;
                int ChoiceNumber3 = 3;
                if( choice1 != null && choice2 != null && choice3 != null){
                choice1.setValue(choiceValue1);
                choice1.setNumber(ChoiceNumber1);
                choice2.setValue(choiceValue2);
                choice2.setNumber(ChoiceNumber2);

                choice3.setValue(choiceValue3);
                choice3.setNumber(ChoiceNumber3);

                List<Choice> choicesList = new ArrayList<Choice>();
                choicesList.add(choice1);
                choicesList.add(choice2);
                choicesList.add(choice3);
                
                choice1.setQuestion(question);
                choice2.setQuestion(question);
                choice3.setQuestion(question);
             
                ((PollWebDataLayer) request.getAttribute("datalayer")).getChoiceDAO().storeChoice(choice1);
                ((PollWebDataLayer) request.getAttribute("datalayer")).getChoiceDAO().storeChoice(choice2);
                ((PollWebDataLayer) request.getAttribute("datalayer")).getChoiceDAO().storeChoice(choice3);
                question.setChoices(choicesList);
                }
            }
            response.sendRedirect("/PollWeb/CreateSurvey?id=" + survey.getID());

            ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().storeQuestion(question);

        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            HttpSession s = checkSession(request);
            if (request.getParameter("addpart") != null) {
                action_addPart(request, response, s);
            } else if (request.getParameter("question") != null) {
                action_question(request, response);
            } else {
                action_default(request, response);
            }

        } catch (Exception e) {
        }
    }

}
