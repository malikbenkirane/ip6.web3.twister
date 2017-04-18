package edu.twister.malik.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.twister.malik.services.ServiceFactory;
import edu.twister.malik.services.ServiceException;

import edu.twister.malik.services.content.PostCommentService;

@SuppressWarnings("serial")
public class PostCommentServlet extends HttpServlet {

    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException
        {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JSONObject served;
            try {
                served = PostCommentService.serve(
                        new Hashtable<String, String[]>
                        (request.getParameterMap()));
            } catch (ServiceException e) {
                served =
                    ServiceFactory.error(e);
            }
            out.println(served.toString());
        }

}
