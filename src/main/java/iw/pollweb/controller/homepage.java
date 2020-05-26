package iw.pollweb.controller;

import iw.framework.data.DataException;
import iw.framework.result.SplitSlashesFmkExt;
import iw.framework.result.TemplateManagerException;
import iw.framework.result.TemplateResult;
import iw.pollweb.controller.BaseController;
import iw.pollweb.model.PollWebDataLayer;
import iw.pollweb.model.dto.Survey;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dario
 */
public class homepage extends BaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Survey> surveys = ((PollWebDataLayer) request.getAttribute("datalayer")).getSurveyDAO().getSurveysByReservation(false);
            request.setAttribute("surveys", surveys);
            request.setAttribute("page_title", "home");
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            res.activate("home.ftl.html", request, response);
        } catch (TemplateManagerException | DataException ex) {
            ex.printStackTrace();
        }

    }

}
