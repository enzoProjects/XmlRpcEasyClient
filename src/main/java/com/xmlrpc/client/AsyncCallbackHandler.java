package com.xmlrpc.client;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by springfield-home on 5/26/17.
 */
public class AsyncCallbackHandler implements AsyncCallback{
    private AsyncCallback callback;
    private Class salida;

    public AsyncCallbackHandler(AsyncCallback callback) {
        this.callback = callback;
        this.salida = null;
    }

    public <T> AsyncCallbackHandler(AsyncCallback callback, Class<T> salida) {
        this.callback = callback;
        this.salida = salida;
    }

    @Override
    public void handleResult(XmlRpcRequest pRequest, Object pResult) {
        if (salida == null) callback.handleResult(pRequest, pResult);
        else {
            try {
                Object obj = salida.getConstructor().newInstance();
                for (Map.Entry<String, Object> entry : ((Map<String, Object>)pResult).entrySet()) {
                    Field field = salida.getDeclaredField(entry.getKey());
                    field.set(obj, entry.getValue());
                }
                callback.handleResult(pRequest, salida.cast(obj));
            } catch (Exception e) {
                System.err.println(e.getStackTrace());
            }
        }

    }

    @Override
    public void handleError(XmlRpcRequest pRequest, Throwable pError) {
        callback.handleError(pRequest, pError);
    }
}
