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
import java.rmi.ServerException;
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
        Supervisor supervisor;
        supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().createSupervisor();
        String title = request.getParameter("title");
        String open_txt = request.getParameter("opening_text");
        String close_txt = request.getParameter("closing_text");
        boolean isPrivate = request.getParameter("isPrivate") != null;

        String email = request.getParameter("email");
        supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisorByEmail(email);
        if (title != null) {
            survey.setTitle(title);
            survey.setOpeningText(open_txt);
            survey.setClosingText(close_txt);
            survey.setSupervisor(supervisor);
            
            response.sendRedirect("/PollWeb/profile");
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().storeSurvey(survey);
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
            String mittente = "pollweb2020@gmail.com";
            String pass = "We_PollWeb_2020";
            String obj = "Sei diventato supervisore";
            String url = "http://localhost:8080/PollWeb/login";
            String testo = "Ciao " + FName + " " + LName + "\n"
                    + "Sei stato invitato ad essere un supervisore della nostra piattaforma le tue credenziali sono: \n\n" + "Email:  " + email + "\n" + "password:  " + password + "\n" + "clicca qui per accedere al sondaggio: " + url;
            EmailSender.send(mittente, pass, email, obj, testo);
            response.sendRedirect("/PollWeb/profile");

            ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().storeSupervisor(supervisor);
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            HttpSession s = checkSession(request);
            if (request.getParameter("create") != null) {
                action_create(request, response);
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
