package com.xmlrpc.client.exception;

/**
 * Created by springfield-home on 5/25/17.
 */
public class ProcessParamException extends Exception {
    public ProcessParamException(String message, Exception e) {
        super(message, e);
    }
}
