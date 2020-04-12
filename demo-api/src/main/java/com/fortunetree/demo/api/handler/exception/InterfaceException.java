package com.fortunetree.demo.api.handler.exception;

import com.fortunetree.basic.support.commons.business.result.ResponseCode;
import org.springframework.http.HttpStatus;

public class InterfaceException extends Exception{
    private static final long serialVersionUID = 1L;
    private ResponseCode responseCode;
    private final int code;

    public static InterfaceException unkown() {
        return new InterfaceException(ResponseCode.ERROR);
    }

    public static InterfaceException unauthenticated() {
        return new InterfaceException(ResponseCode.UNAUTHORIZED);
    }

    public static InterfaceException noPermission() {
        return new InterfaceException(ResponseCode.NO_PERMISSION);
    }

    public static InterfaceException failure() {
        return new InterfaceException(ResponseCode.SERVER_ERROR);
    }

    public static InterfaceException failure(String message) {
        return new InterfaceException(ResponseCode.SERVER_ERROR.value(), message);
    }

    public InterfaceException(ResponseCode responseCode) {
        super(responseCode.reasonPhrase());
        this.responseCode = responseCode;
        this.code = responseCode.value();
    }

    public InterfaceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public ResponseCode getInterfaceError() {
        return this.responseCode;
    }

    public boolean isUnauthorization() {
        return this.code == HttpStatus.UNAUTHORIZED.value() || this.code == ResponseCode.UNAUTHORIZED.value();
    }
}
