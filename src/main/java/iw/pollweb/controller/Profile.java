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
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Admin;
import iw.pollweb.model.dto.Participant;
import iw.pollweb.model.dto.Supervisor;
import iw.pollweb.model.dto.Survey;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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

    /*private void action_part(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            int partid = SecurityLayer.checkNumeric(request.getParameter("id"));
            Survey surveys = new Survey();
            if (s != null) {
                System.out.println("profilo non caricato");
            }
            Participant participant = ((PollWebDataLayer) request.getAttribute("datalayer")).getParticipantDAO().getParticipantByID(partid);
            surveys = participant.getSurvey();
            request.setAttribute("survey", surveys);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
            res.activate("profile.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex) {
            ex.printStackTrace();
            System.err.println(ex);

        }
    }
*/
    
    private void action_default(HttpServletRequest request, HttpServletResponse response, HttpSession s) throws ServletException, IOException, DataException, TemplateManagerException {
            Admin admin = ((PollWebDataLayer)request.getAttribute("datalayer")).getAdminDAO().getAdminByID((int)s.getAttribute("idUtenteLoggato"));
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveys();
            request.setAttribute("surveys", surveys);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
            res.activate("/Supervisore.ftl.html", request, response);
           
        }
    

    private void action_sup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            HttpSession s = SecurityLayer.checkSession(request);
            int supid = SecurityLayer.checkNumeric(request.getParameter("id"));
            Supervisor supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().getSupervisorByID(supid);
            if (s != null) {
                System.out.println("profilo non caricato");
            }
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveysBySupervisor(supervisor);
            request.setAttribute("surveys", surveys);
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
            res.activate("profile.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex2) {
            ex2.printStackTrace();
            System.err.println(ex2);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        
        try {
            HttpSession s = checkSession(request);
            if(request.getParameter("sup")!= null){
                action_sup(request, response);
            }else{
            action_default(request, response,s);
            
            }
        
        } catch (TemplateManagerException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}



   