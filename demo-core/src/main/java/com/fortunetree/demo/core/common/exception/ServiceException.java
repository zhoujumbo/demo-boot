package com.fortunetree.demo.core.common.exception;

import com.fortunetree.basic.support.commons.business.result.ResponseCode;

/**
 * @author
 */
public class ServiceException extends RuntimeException {

    private int code;
    private ResponseCode responseCode;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
        this.responseCode = ResponseCode.SERVER_ERROR;
        this.code = ResponseCode.SERVER_ERROR.value();
    }

    public ServiceException(Throwable cause) {
        super(ResponseCode.SERVER_ERROR.reasonPhrase(),cause);
        this.responseCode = ResponseCode.SERVER_ERROR;
        this.code = ResponseCode.SERVER_ERROR.value();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ResponseCode responseCode) {
        super(responseCode.reasonPhrase());
        this.responseCode = responseCode;
        this.code = responseCode.value();
    }

    public ServiceException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.reasonPhrase(), cause);
        this.responseCode = responseCode;
        this.code = responseCode.value();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.responseCode = ResponseCode.SERVER_ERROR;
        this.code = ResponseCode.SERVER_ERROR.value();
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code + "" +
                ", \"message\":\"" + getMessage() + "\"" +
                "}";
    }
}
