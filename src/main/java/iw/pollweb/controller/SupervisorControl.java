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
        request.setAttribute("page_title", "login");
        res.activate("/crea-supervisore.ftl.html", request, response);
    }

    private void action_create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DataException {
        String name = request.getParameter("name");
        String LName = request.getParameter("Lname");
        String email = request.getParameter("email");
        String password = PasswordUtility.generateRandomPassword();
        Supervisor supervisor = new Supervisor();
        supervisor.setFirstName(name);
        supervisor.setLastName(LName);
        supervisor.setEmail(email);
        supervisor.setHashedPassword(password);
        ((PollWebDataLayer) request.getAttribute("datalayer")).getSupervisorDAO().storeSupervisor(supervisor);
        response.sendRedirect("/PollWeb/profile");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            if (request.getParameter("create") != null) {
                action_create(request, response);
            } else {
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
