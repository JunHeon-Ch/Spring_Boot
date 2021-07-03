package com.example.demo.mfa.exception;


import org.springframework.security.core.AuthenticationException;

public class OtpNotProveException extends AuthenticationException {

    public OtpNotProveException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OtpNotProveException(String msg) {
        super(msg);
    }
}
