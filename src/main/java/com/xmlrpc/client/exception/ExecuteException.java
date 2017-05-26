package com.xmlrpc.client.exception;

/**
 * Created by springfield-home on 5/25/17.
 */
public class ExecuteException extends Exception {
    public ExecuteException(String message, Exception e) {
        super(message, e);
    }
}
