package edu.twister.malik.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.twister.malik.services.user.SignUpService;
import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class SignUpServlet extends HttpServlet {

    public void doGet( 
            HttpServletRequest request,
            HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject served;
        try {
            served = SignUpService.serve(
                    new Hashtable<String, String[]>
                    (request.getParameterMap()));
        } catch (ServiceException e) {
            served =
                ServiceFactory.error(e);
        }
        out.println(served.toString());
    }

}
