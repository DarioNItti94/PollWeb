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
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
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
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException{
        HttpSession s = SecurityLayer.checkSession(request);
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
        request.setAttribute("page_title", "login");
        res.activate("/crea-sondaggio.ftl.html", request, response);
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
            Participant participant = new Participant();
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
            response.sendRedirect("/actionquestion");
        } else {
            throw new ServerException("inserisci i parametri");
        }
    }

    private void action_addPart(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, DataException, IOException {
        //prendo il sondaggio appena creato attraverso il suo id
        Survey currentSurvey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID((int) s.getAttribute("id"));
        String Fname = request.getParameter("Fname");
        String Lname = request.getParameter("Lname");
        String email = request.getParameter("email");
        String password = PasswordUtility.generateRandomPassword();
        //creo un nuovo partecipante
        Participant participant = new Participant();
        //setto i valori nel database
        participant.setFirstName(Fname);
        participant.setLastName(Lname);
        participant.setEmail(email);
        participant.setHashedPassword(password);
        participant.setSurvey(currentSurvey);
        //dopo aver settato tutte le variabili del DTO vado ad inviare la mail
        String mittente = "pollweb2020@gmail.com";
        String pass = "We_PollWeb_2020";
        String obj = "C'è un sonadggio per te";
        String url = "http://localhost:8080/PollWeb/login";
        String testo = "Ciao " + Fname + " " + Lname + "\n"
                + "Sei stato invitato ad un nuovo sondaggio le tue credenziali sono: \n\n" + "Email:  " + email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;
        EmailSender.send(mittente, pass, email, obj, testo);
        response.sendRedirect("/modifysurvey?survey");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
