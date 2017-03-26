package edu.twister.malik.services;

@SuppressWarnings("serial")
public class ServiceException extends Exception {

    public static final int _USER_CLASS = 1;
    public static final int _JSON_CLASS = -1;
    public static final int _SQL_CLASS = -2;

    private int _class;
    private int code;

    public ServiceException
        (String message, int _class) {
            super(classMsg(_class, message));
            this._class = _class;
            this.code = 0;
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
