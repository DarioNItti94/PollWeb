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
import iw.pollweb.model.dao.SupervisorDAO;
import iw.pollweb.model.dao.SurveyDAO;
import iw.pollweb.model.dto.Admin;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Question;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
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
public class Profile extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, IOException, DataException, TemplateManagerException {
        request.setAttribute("page_title", "profile");
        //System.out.println((int) s.getAttribute("userid"));
        if ((int) s.getAttribute("userid") == 1) {
            Admin admin = ((PollWebDataLayer) request.getAttribute("datalayer")).getAdminDAO().getAdminByID((int) s.getAttribute("userid"));
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveys();
            request.setAttribute("surveys", surveys);
        } else {
            Supervisor supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisorByID((int) s.getAttribute("userid"));
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveysBySupervisor(supervisor);
            request.setAttribute("surveys", surveys);
        }
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
        res.activate("/Dashboard.ftl.html", request, response);
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException {
        Survey survey;
        survey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().createSurvey();

        String title = request.getParameter("title");
        String open_txt = request.getParameter("opening_text");
        String close_txt = request.getParameter("closing_text");
        boolean isclosed = Boolean.parseBoolean(request.getParameter("isclosed"));
        boolean isreserved = Boolean.parseBoolean(request.getParameter("isreserved"));
        boolean isactive = Boolean.parseBoolean(request.getParameter("isactive"));
        int numberQuest = Integer.parseInt(request.getParameter("questions"));
        int numberPart = Integer.parseInt(request.getParameter("participants"));
        List<Question> questionsList = new ArrayList<Question>();
        int i = 0;
        while (i <= numberQuest) {
            Question question = new Question();
            questionsList.add(question);
            i++;
        }
        List<Participant> participantsList = new ArrayList<Participant>();
        int j = 0;
        while (j <= numberPart) {
            Participant participant = new Participant();
            participantsList.add(participant);
            j++;
        }
        int idsup = 2;
        if (title != null && open_txt != null && close_txt != null) {
            List<Supervisor> supervisors = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisors();
            request.setAttribute("supervisor", supervisors);
            //List< Supervisor> supervisors = ((PollWebDataLayer)request.getAttribute("datalayer")).getSupervisorDAO().getSupervisors().get(idsup);
            survey.setTitle(title);
            survey.setOpeningText(open_txt);
            survey.setClosingText(close_txt);
            survey.setActive(isactive);
            survey.setClosed(isclosed);
            survey.setReserved(isreserved);
            survey.setQuestions(questionsList);
            survey.setParticipants(participantsList);
            Supervisor supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisorByID(idsup);
            survey.setSupervisor(supervisor);
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().storeSurvey(survey);
            response.sendRedirect("/PollWeb/createsurvey?id=" + survey.getID());
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }

    private void action_create_sup(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException, IOException {
        Supervisor supervisor;
        supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().createSupervisor();
        String FName = request.getParameter("firstName");
        String LName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = PasswordUtility.generateRandomPassword();
        if (email != null && FName != null && LName != null) {
            supervisor.setFirstName(FName);
            supervisor.setLastName(LName);
            supervisor.setEmail(email);
            supervisor.setHashedPassword(PasswordUtility.getSHA256(password));
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().storeSupervisor(supervisor);
            response.sendRedirect("/PollWeb/profile");
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }

    private void action_addPart(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, DataException, IOException {
        //prendo il sondaggio appena creato attraverso il suo id
        Survey currentSurvey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID((int) s.getAttribute("userid"));

        String Fname = request.getParameter("Fname");
        String Lname = request.getParameter("Lname");
        String Email = request.getParameter("email");
        String password = PasswordUtility.generateRandomPassword();
        //creo un nuovo partecipante
        Participant participant = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().createParticipant();
        //setto i valori nel database
        participant.setFirstName(Fname);
        participant.setLastName(Lname);
        participant.setEmail(Email);
        participant.setHashedPassword(password);
        participant.setSurvey(currentSurvey);
        List<Participant> participants = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().getParticipants();
        ListIterator<Participant> pIterator = participants.listIterator();

        while (pIterator.hasNext()) {
            String email = pIterator.next().getEmail();
            //dopo aver settato tutte le variabili del DTO vado ad inviare la mail
            String mittente = "pollweb2020@gmail.com";
            String pass = "We_PollWeb_2020";
            String obj = "C'è un sonadggio per te";
            String url = "http://localhost:8080/PollWeb/login";
            String testo = "Ciao ,Sei stato invitato ad un nuovo sondaggio le tue credenziali sono: \n\n" + "Email:  " + email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;
            EmailSender.send(mittente, pass, email, obj, testo);
        }

        response.sendRedirect("/modifysurvey?survey");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            HttpSession s = checkSession(request);
            if (request.getParameter("create") != null) {
                action_create(request, response);
            } else if (request.getParameter("addPart") != null) {
                action_addPart(request, response, s);
            } else if (request.getParameter("super") != null) {
                action_create_sup(request, response);
            } else {
                action_default(request, response, s);
            }
        } catch (TemplateManagerException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
