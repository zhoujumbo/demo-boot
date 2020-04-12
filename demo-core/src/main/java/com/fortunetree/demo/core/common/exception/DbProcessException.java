package com.fortunetree.demo.core.common.exception;

public class DbProcessException extends RuntimeException {
    private static final long serialVersionUID = 5427957736208284422L;

    private int code;

    public DbProcessException() {
    }

    public DbProcessException(String message) {
        super(message);
    }

    public DbProcessException(Throwable cause) {
        super(cause);
    }

    public DbProcessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public DbProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbProcessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public DbProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public int getCode() {
        return code;
    }


    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code + "" +
                ", \"message\":\"" + getMessage() + "\"" +
                "}";
    }
}
