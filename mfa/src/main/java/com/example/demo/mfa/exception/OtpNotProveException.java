package com.example.demo.mfa.exception;

import javax.naming.AuthenticationException;

public class OtpNotProveException extends AuthenticationException {

    public OtpNotProveException(String explanation) {
        super(explanation);
    }

    public OtpNotProveException() {
    }
}
