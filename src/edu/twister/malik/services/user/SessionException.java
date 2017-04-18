package edu.twister.malik.services.user;

import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class SessionException extends ServiceException {

    public static final int _FORBIDDEN_USERNAME = 1;
    public static final int _NO_SESSION_TO_CLOSE = 2;
    public static final int _SESSION_EXPIRED = 3;
    public static final int _INVALID_SESSION = 4;

    public static final int _NO_NEW_SESSION_ENTRY = -1;

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
        getMessage (int code) {
            String suffix = " (Session Exception)";
            String msg;
            switch (code) {
                case _FORBIDDEN_USERNAME:
                    msg = "Not registred username. Access Forbidden";
                    break;
                case _NO_NEW_SESSION_ENTRY:
                    msg = "No session entry made";
                    break;
                case _NO_SESSION_TO_CLOSE:
                    msg = "Not logged in (no session to close)";
                    break;
                case _SESSION_EXPIRED:
                    msg = "The session has expired";
                    break;
                default:
                    msg = "";
                    break;
            }
            return msg + suffix;
        }

}
