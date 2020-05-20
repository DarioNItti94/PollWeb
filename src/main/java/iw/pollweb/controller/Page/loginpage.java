/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller.Page;

import iw.framework.data.DataException;
import iw.framework.result.SplitSlashesFmkExt;
import iw.framework.result.TemplateManagerException;
import iw.framework.result.TemplateResult;
import iw.framework.security.SecurityLayer;
import iw.pollweb.controller.BaseController;
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
public class loginpage extends BaseController{

   
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
         try {
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null){
                System.out.println("loggato");
            }else{
                System.out.println("non loggato");
            }
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("split_shalshes",new SplitSlashesFmkExt());
            request.setAttribute("page_title", "login");
            res.activate("/login.ftl.html", request, response);
            
        } catch (TemplateManagerException ex) {
        ex.printStackTrace();
        }
    } 
    
@Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

