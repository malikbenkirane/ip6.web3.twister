package edu.twister.malik.services;

import java.sql.SQLException;

@SuppressWarnings("serial")
public class ServiceException extends Exception {

    public static final int _SERVICE_CLASS = 0;
    public static final int _USER_CLASS = 1;
    public static final int _JSON_CLASS = -1;
    public static final int _SQL_CLASS = -2;

    private int _class;
    private int code;

    public ServiceException
        (int _class) {
            super("Service Exception");
            this._class = _class;
            this.code = _SERVICE_CLASS;
        }

    public ServiceException
        (SQLException e) {
            super(classMsg(
                        _SQL_CLASS, 
                        e.getMessage() + 
                        " - state " + e.getSQLState() + 
                        " - sql code " + e.getErrorCode()
                        )
                 );
            this._class = _SQL_CLASS;
            this.code = _SERVICE_CLASS;
        }

    public ServiceException
        (String message, int _class) {
            super(classMsg(_class, message));
            this._class = _class;
            this.code = _SERVICE_CLASS;
        }

    public ServiceException
        (String message, int _class, int code) {
            super(classMsg(_class, message));
            this._class = _class;
            this.code = code;
        }

    public int 
        getErrorClass() {
            return _class;
        }

    public int
        getErrorCode() {
            return code;
        }

    public static String 
        classMsg (int _class, String message) {
        if ( ! message.equals("") )
            message = " (" + message + ")";
        switch (_class) {
            case _USER_CLASS:
                return "User Service Exception" + message;
            case _JSON_CLASS:
                return "JSON Exception" + message;
            case _SQL_CLASS:
                return "SQL Exception" + message;
            default:
                return "Unknown Exception" + message;
        }
    }

}
