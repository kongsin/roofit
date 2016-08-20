package com.example.roofit;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongsin on 8/20/16.
 */

public class AdapterHandler implements InvocationHandler {
    private static final String TAG = "AdapterHandler";
    private String mBaseUrl;

    public AdapterHandler(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
            throws Throwable {

        Type returnType = m.getGenericReturnType();
        String typeClass = returnType.toString().split("<")[1].split(">")[0];
        Object returnObject;
        Constructor constructor = m.getReturnType().getDeclaredConstructor(String.class, String.class, Class.class);
        constructor.setAccessible(true);
        String actualUrl = getAnnotationUrl(m);
        ArrayList<Param> params = getPathAnnotation(m, args);
        if (actualUrl != null) {
            for (Param param : params) {
                actualUrl = actualUrl.replace("{" + param.name + "}", param.value);
            }
        }
        returnObject = constructor.newInstance(actualUrl, mBaseUrl, Class.forName(typeClass));
        return returnObject;
    }

    private String getAnnotationUrl(Method m) {
        Annotation[] annotations = m.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET) {
                return ((GET) annotation).url();
            }
        }
        return null;
    }

    private ArrayList<Param> getPathAnnotation(Method m, Object[] args) {
        ArrayList<Param> params = new ArrayList<>();
        if (args == null) return params;
        Annotation[][] parameterTypes = m.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            Log.i(TAG, "getPathAnnotation: " + args[i]);
            for (Annotation path : parameterTypes[i]) {
                if (path instanceof PATH) {
                    Log.i(TAG, "getPathAnnotation: " + ((PATH) path).value());
                    params.add(new Param(((PATH) path).value(), args[i].toString()));
                }
            }
        }
        return params;
    }

    class Param {
        String name;
        String value;

        Param(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
