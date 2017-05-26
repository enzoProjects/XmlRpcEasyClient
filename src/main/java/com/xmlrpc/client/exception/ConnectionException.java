package com.xmlrpc.client.exception;

/**
 * Created by springfield-home on 5/25/17.
 */
public class ConnectionException extends Exception {
    public ConnectionException(String message, Exception e) {
        super(message,e);
    }
}
