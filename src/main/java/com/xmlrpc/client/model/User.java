package com.xmlrpc.client.model;

/**
 * Created by springfield-home on 5/26/17.
 */
public class User {
    public String login;
    public String password;

    public User(String ebonggio, String s) {
        password = s;
        login = ebonggio;
    }
}
