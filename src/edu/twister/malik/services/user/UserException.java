package edu.twister.malik.services.user;

import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class UserException extends ServiceException {

    public static final int _NAME_ALREADY_REGISTERED = 1;
    public static final int _USERNAME_ALREADY_USED = 2;
    public static final int _INVALID_EMAIL = 3;
    public static final int _INVALID_NAME = 4;
    public static final int _BAD_LOGIN = 5;

    private int code;

    public UserException
        (int code) {
            super(getMessage(code), ServiceException._USER_CLASS, code);
            this.code = code;
        }

    private static String 
        getMessage
        (int code) {
            switch (code) {
                case _NAME_ALREADY_REGISTERED:
                    return "First and Last Names already registred";
                case _USERNAME_ALREADY_USED:
                    return "This Login is already use";
                case _INVALID_EMAIL:
                    return "Not a valid email address";
                case _INVALID_NAME:
                    return "First name or last name must " +
                        "contain only letters and must " +
                        "be between 2 and 20 characters";
                case _BAD_LOGIN:
                    return "Wrong Username or Wrong Password";
                default:
                    return "Unknown Error";
            }
        }

    public int 
        getCode() {
        return code;
    }

}
