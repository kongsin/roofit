package com.example.roofit;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

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

        Type resObjectClassName = m.getGenericReturnType();
        String typeClass = resObjectClassName.toString().split("<")[1].split(">")[0];

        Constructor constructor = m.getReturnType().getDeclaredConstructor(String.class, String.class, Class.class);
        constructor.setAccessible(true);

        return constructor.newInstance(queryPathParam(m, args), mBaseUrl, Class.forName(typeClass));
    }

    private String queryPathParam(Method m, Object[] args) {
        String actualUrl = getRawUrl(m);
        actualUrl =  actualUrl.replace(" ", "");
        Log.i(TAG, "queryPathParam: " +actualUrl);
        ArrayList<Param> params = getPathAnnotation(m, args);
        if (actualUrl != null) {
            for (Param param : params) {
                actualUrl = actualUrl.replace("{" + param.name + "}", param.value);
            }
        }
        return actualUrl;
    }

    private String getRawUrl(Method m) {
        Annotation[] annotations = m.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof URL) {
                return ((URL) annotation).url();
            }
        }
        return null;
    }

    private ArrayList<Param> getPathAnnotation(Method m, Object[] args) {
        ArrayList<Param> params = new ArrayList<>();
        if (args == null) return params;
        Annotation[][] parameterTypes = m.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            for (Annotation path : parameterTypes[i]) {
                if (path instanceof PATH) {
                    params.add(new Param(((PATH) path).value(), args[i].toString()));
                }
            }
        }
        return params;
    }

    private class Param {
        String name;
        String value;

        Param(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
