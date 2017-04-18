package edu.twister.malik.services.relation;

import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class RelationException extends ServiceException {

    public static final int _NO_SUCH_USER = 1;
    public static final int _ALREADY_FOLLOWING = 2;
    public static final int _FOLLOW_TOO_SHORT = 3;

    private int code;

    public RelationException
        (int code) {
            super(getMessage(code), ServiceException._RELATION_CLASS, code);
            this.code = code;
        }

    public int 
        getCode() {
        return code;
    }

    private static String
        getMessage (int code) {
            switch (code) {
                case _NO_SUCH_USER:
                    return "There is no such user";
                case _ALREADY_FOLLOWING:
                    return "You already follow this user";
                case _FOLLOW_TOO_SHORT:
                    return "You cannot follow yourself";
                default:
                    return "Unknown Error";
            }
        }

}
