package edu.twister.malik.services;

import java.util.Hashtable;
import java.util.Enumeration;
import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class ServletException extends ServiceException {

    public ServletException
        (Hashtable<String, String> messages) {
            super(translate(messages), ServiceException._SERVEMAP_CLASS);
        }

    public ServletException
        (String message) {
            super("(Servlet Exception) " + message, ServiceException._SERVEMAP_CLASS);
        }

    public static String 
        translate
        (Hashtable<String, String> messages) {
            String message = " ";
            Enumeration<String> specification = messages.keys();
            for ( ; specification.hasMoreElements() ; ) {
                String spec = specification.nextElement();
                message += spec + " : " + messages.get(spec) + " (*) ";
            }
            return message;
        }

}
