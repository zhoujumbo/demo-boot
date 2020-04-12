package com.fortunetree.demo.core.common.exception;

/**
 * Argument Requirement Exception
 */
public class ArgumentRequirementException extends RuntimeException {

    private int code;

    public ArgumentRequirementException() {
    }

    public ArgumentRequirementException(String message) {
        super(message);
    }

    public ArgumentRequirementException(Throwable cause) {
        super(cause);
    }

    public ArgumentRequirementException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ArgumentRequirementException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentRequirementException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ArgumentRequirementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
