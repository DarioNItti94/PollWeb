/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.security.SecurityLayer;
import iw.framework.utils.EmailSender;
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
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
    
    private void action_addPart(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, DataException, IOException {
        //prendo il sondaggio appena creato attraverso il suo id
        Survey currentSurvey = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveyByID(parseInt(request.getParameter("id")));
       
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
        participant.setHashedPassword(PasswordUtility.getSHA256(password));
        participant.setSurvey(currentSurvey);
        

        
            //dopo aver settato tutte le variabili del DTO vado ad inviare la mail
            String mittente = "pollweb2020@gmail.com";
            String pass = "We_PollWeb_2020";
            String obj = "C'è un sonadggio per te";
            String url = "http://localhost:8080/PollWeb/login";
            String testo = "Ciao ,Sei stato invitato ad un nuovo sondaggio le tue credenziali sono: \n\n" + "Email:  " + Email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;
            EmailSender.send(mittente, pass, Email, obj, testo);
                    response.sendRedirect("/modifysurvey?survey");

        }





    

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
