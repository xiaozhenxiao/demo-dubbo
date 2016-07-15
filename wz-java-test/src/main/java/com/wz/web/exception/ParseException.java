package com.wz.web.exception;

/**
 * Created by wangzhen on 2016-07-12.
 */
public class ParseException extends Exception {

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
