package edu.twister.malik.services.content;

import edu.twister.malik.services.ServiceException;

@SuppressWarnings("serial")
public class ContentException extends ServiceException {

    public static final int _CONTENT_NOT_CASTING = 1;
    public static final int _NO_COUNTERS = 2;

    private int code;

    public ContentException
        (int code) {
            super(getMessage(code), ServiceException._CONTENT_CLASS, code);
            this.code = code;
        }

    public int 
        getCode() {
        return code;
    }

    private static String
        getMessage (int code) {
            switch (code) {
                case _CONTENT_NOT_CASTING:
                    return "incoherent data";
                case _NO_COUNTERS:
                    return "not allowed because no counters";
                default:
                    return "Unknown Error";
            }
        }

}
