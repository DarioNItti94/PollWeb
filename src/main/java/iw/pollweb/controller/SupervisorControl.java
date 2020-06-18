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
import iw.framework.utils.PasswordUtility;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Supervisor;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dario
 */
public class SupervisorControl extends BaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException {
        HttpSession s = SecurityLayer.checkSession(request);
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("split_shalshes", new SplitSlashesFmkExt());
        request.setAttribute("page_title", "crea supervisore");
        res.activate("/crea-supervisore.ftl.html", request, response);
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        String email = request.getParameter("email");
        String FName = request.getParameter("firstName");
        String LName = request.getParameter("lastName");
        String password = PasswordUtility.generateRandomPassword();
        if (email != null && FName != null && LName != null) {
            Supervisor supervisor = ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().createSupervisor();
            supervisor.setEmail(email);
            supervisor.setFirstName(FName);
            supervisor.setLastName(LName);
            supervisor.setHashedPassword(password);
            ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().storeSupervisor(supervisor);
        } else {
            throw new ServletException("inserisci i parametri");
        }
    }


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
            HttpSession s = checkSession(request);
            try {
            if(request.getParameter("create") != null){
                action_create(request, response);
            }else{
                action_default(request, response);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public String getServletInfo() {
        return "controllo servlet del suervisore";
    }// </editor-fold>

}
