package edu.twister.malik.services.user;

import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class SessionException extends ServiceException {

    public static final int _FORBIDDEN_USERNAME = 0;

    private int code;

    public SessionException
        (int code) {
            super(getMessage(code), ServiceException._USER_CLASS, code);
            this.code = code;
        }

    public int getCode() {
        return code;
    }

    private static String
        getMessage
        (int code) {
            String suffix = " (Session Exception)";
            String msg;
            switch (code) {
                case _FORBIDDEN_USERNAME:
                    msg = "Not registred username. Access Forbidden";
                    break;
                default:
                    msg = "";
                    break;
            }
            return msg + suffix;
        }

}
