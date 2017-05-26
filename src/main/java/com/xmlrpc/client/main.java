package com.xmlrpc.client;

import com.xmlrpc.client.exception.ConnectionException;
import com.xmlrpc.client.exception.ExecuteException;
import com.xmlrpc.client.model.Salida;
import com.xmlrpc.client.model.User;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by springfield-home on 5/25/17.
 */
public class main {
    public static void main(String[] args) throws ConnectionException, ExecuteException {
        String url = "https://bugzilla.eng.vmware.com/xmlrpc.cgi";
        XmlRpcEasyClient client = XmlRpcEasyClient.getInstance();
        client.buildClient(url);

        List<Object> params = new ArrayList();
        User user  = new User("ebonggio", "Abril2017!");
        params.add(user);
        client.executeAsync("User.login", params, new AsyncPrueba(), Salida.class);
        while(true){

        }

    }
}

class AsyncPrueba implements  AsyncCallback {

    @Override
    public void handleResult(XmlRpcRequest pRequest, Object pResult) {

        System.out.println();

    }

    @Override
    public void handleError(XmlRpcRequest pRequest, Throwable pError) {

    }
}
