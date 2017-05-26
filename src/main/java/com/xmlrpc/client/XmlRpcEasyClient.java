package com.xmlrpc.client;

import com.xmlrpc.client.exception.ConnectionException;
import com.xmlrpc.client.exception.ExecuteException;
import com.xmlrpc.client.exception.ProcessParamException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by springfield-home on 5/25/17.
 */
public class XmlRpcEasyClient {
    private static XmlRpcEasyClient instance = null;
    private final XmlRpcClient xmlRpcClient = new XmlRpcClient();
    private URL url;

    private XmlRpcEasyClient() {
    }

    public static XmlRpcEasyClient getInstance() {
        if (instance == null) {
            instance = new XmlRpcEasyClient();
        }
        return instance;
    }

    public XmlRpcClient getXmlRpcClient() {
        return xmlRpcClient;
    }

    public XmlRpcEasyClient buildClient(String sUrl) throws ConnectionException {
        try {
            this.url = new URL(sUrl);
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(this.url);
            this.getXmlRpcClient().setConfig(config);
            return this;
        } catch (MalformedURLException e) {
            throw new ConnectionException(e.getMessage(), e);
        }

    }

    public Object execute(String method, List<Object> params) throws ExecuteException {
        List<Object> processedParams = processParams(params);
        try {
            return this.xmlRpcClient.execute(method, processedParams);
        } catch (XmlRpcException e) {
            throw new ExecuteException(e.getMessage(), e);
        }
    }

    public <T> T execute(String method, List<Object> params, Class<T> salida) throws ExecuteException {
        List<Object> processedParams = processParams(params);
        try {
            Map<String, Object> res = (Map<String, Object>) this.xmlRpcClient.execute(method, processedParams);
            T obj = salida.getConstructor().newInstance();
            for (Map.Entry<String, Object> entry : res.entrySet()) {
                Field field = salida.getDeclaredField(entry.getKey());
                field.set(obj, entry.getValue());
            }
            return salida.cast(obj);
        } catch (XmlRpcException e) {
            throw new ExecuteException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExecuteException(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            throw new ExecuteException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new ExecuteException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ExecuteException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExecuteException(e.getMessage(), e);
        }
    }


    public void executeAsync(String method, List<Object> params, AsyncCallback handler) throws ExecuteException {
        try {
            this.xmlRpcClient.executeAsync(method, processParams(params), new AsyncCallbackHandler(handler));
        } catch (XmlRpcException e) {
            throw new ExecuteException(e.getMessage(), e);
        }

    }

    public <T> void executeAsync(String method, List<Object> params, AsyncCallback handler, Class<T> salida) throws ExecuteException {
        try {
            this.xmlRpcClient.executeAsync(method, processParams(params), new AsyncCallbackHandler(handler, salida));
        } catch (XmlRpcException e) {
            throw new ExecuteException(e.getMessage(), e);
        }
    }

    private List<Object> processParams(List<Object> params) {
        List<Object> processedParams = new Vector();
        for (Object param : params) {
            try {
                processedParams.add(processParam(param));
            } catch (ProcessParamException e) {
                System.err.println("Error while process param: " + param);
                continue;
            }

        }
        return processedParams;
    }

    private Object processParam(Object param) throws ProcessParamException {
        if (param instanceof String || param instanceof Integer || param instanceof Map) {
            return param;
        } else if (param instanceof List) {
            return processParams(new Vector<Object>((List) param));
        } else {
            Map<String, Object> map = new HashMap();
            Class<?> clazz = param.getClass();
            for (Field f : clazz.getDeclaredFields()) {
                try {
                    Object fieldValue = f.get(param);
                    map.put(f.getName(), processParam(fieldValue));
                } catch (IllegalAccessException e) {
                    throw new ProcessParamException(e.getMessage(), e);
                } catch (Exception ex) {
                    throw new ProcessParamException(ex.getMessage(), ex);
                }
            }
            return map;
        }
    }

}

