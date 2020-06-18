/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.pollweb.controller;

import iw.framework.data.DataException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dario
 */
public class SupervisorControl extends BaseController {
     
    private void action_create(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
    }

    @Override
    public String getServletInfo() {
        return "controllo servlet del suervisore";
    }// </editor-fold>

}
