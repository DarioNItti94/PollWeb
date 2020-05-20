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
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Admin;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Supervisor;
import java.io.IOException;
import java.io.PrintWriter;
import javax.security.auth.spi.LoginModule;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dario
 */
public class Autentication extends BaseController {
    
    /*
    Questo metodo servirà per eseguire l'accesso al partecipante dei sondaggi
    */
 
      private void action_login_part(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email != null && password != null) {
            // PollWebDataLayer polldata = (PollWebDataLayer) request.getAttribute("datalayer");
            Participant participant = new Participant();
            participant.setEmail(email);
            participant.setHashedPassword(PasswordUtility.getSHA256(password));
            try {
                int participantID = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().authenticateParticipant(participant);
                if (participantID > 0) {
                    participant = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().getParticipantByID(participantID);
                    SecurityLayer.createSession(request, participant.getEmail(), participant.getID());
                    response.sendRedirect("/Sondaggio");
                } else {
                    throw new ServletException("Email e Password errati");
                }
            } catch (DataException e) {
                e.printStackTrace();
            }
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }
      
      /*
      Questo metodo servirà per l'accesso del supervisore ai sondaggi
      all'interno di questo metodo c'è il richiamo al metodo di accesso dell'admin
      */
      
      private void action_login_sup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("emailAS");
        String password = request.getParameter("passwordAS");
        if (email != null && password != null) {
            PollWebDataLayer polldata = (PollWebDataLayer) request.getAttribute("datalayer");
            Supervisor supervisor = new Supervisor();
            supervisor.setEmail(email);
            supervisor.setHashedPassword(PasswordUtility.getSHA256(password));
            try {
                int supervisorID = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().authenticateSupervisor(supervisor);
                if (supervisorID > 0) {
                    supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisorByID(supervisorID);
                    SecurityLayer.createSession(request, supervisor.getEmail(), supervisor.getID());
                    response.sendRedirect("/Dashboard");
                } else {
                    /*
                    se la ricerca del supervisore non va a buon fine chiamiamo il metodo
                    che cercherà lo username dell'admin con la sua password
                    se non  troverà neanche questo darà un avviso che non è presente alcun 
                    utente supervisore o amministratore nel sistema
                    */
                    action_login_admin(request, response);
                }
            } catch (DataException e) {
                e.printStackTrace();
            }
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }
      /*
      questo metodo verrà usato per eseguire l'accesso all'admin (pre-registrato)
      (metodo richiamato nel action_login_sup)
      */
       private void action_login_admin(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String email = request.getParameter("emailAS");
        String password = request.getParameter("passwordAS");
        if (email != null && password != null) {
            PollWebDataLayer polldata = (PollWebDataLayer) request.getAttribute("datalayer");
            Admin admin = new Admin();
            admin.setEmail(email);
            admin.setHashedPassword(PasswordUtility.getSHA256(password));
            try {
                int adminID = ((PollWebDataLayer) request.getAttribute("datalayer")).getAdminDAO().authenticateAdmin(admin);
                if (adminID > 0) {
                    admin = ((PollWebDataLayer) request.getAttribute("datalayer")).getAdminDAO().getAdminByID(adminID);
                    SecurityLayer.createSession(request, admin.getEmail(), admin.getID());
                    response.sendRedirect("/");
                } else {
                    throw new ServletException("Password e Email errati!");
                }
            } catch (DataException e) {
                e.printStackTrace();
            }
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }

       private void action_logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityLayer.disposeSession(request);
        response.sendRedirect("/homepage");
    }
       
  


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            if(request.getParameter("login_part")!= null){
                action_login_part(request, response);
            }else if(request.getParameter("login_sup") != null){
                action_login_sup(request, response);
            }else if(request.getParameter("logout") != null){
                action_logout(request, response);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
       
    }
    
    

}
