package com.example.roofit;

import com.google.gson.Gson;
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

        Annotation[] annotations = m.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET){
                Constructor constructor = m.getReturnType().getDeclaredConstructor(String.class, String.class, Class.class);
                constructor.setAccessible(true);
                return constructor.newInstance(queryUrl(m, args), mBaseUrl, Class.forName(typeClass));
            } else if (annotation instanceof POST){
                Constructor constructor = m.getReturnType().getDeclaredConstructor(String.class, String.class, String.class, Class.class);
                constructor.setAccessible(true);
                return constructor.newInstance(queryUrl(m, args), mBaseUrl, queryJsonParam(m, args), Class.forName(typeClass));
            }
        }
        return null;
    }

    private String queryUrl(Method m, Object[] args) {
        Annotation[] annotations = m.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GET) {
                return queryPathParam(((GET) annotation).url(), m, args);
            } else if (annotation instanceof POST) {
                return ((POST) annotation).url();
            }
        }
        return null;
    }

    private String queryPathParam(String actualUrl, Method m, Object[] args) {
        actualUrl =  actualUrl.replace(" ", "");
        ArrayList<Param> params = getPathAnnotation(m, args);
        if (actualUrl != null) {
            for (Param param : params) {
                actualUrl = actualUrl.replace("{" + param.name + "}", param.value.toString());
            }
        }
        return actualUrl;
    }

    private String queryJsonParam(Method m, Object[] args){
        if (args != null) {
            Annotation[][] annotations = m.getParameterAnnotations();
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof ReqObject){
                        return new Gson().toJson(args[i]);
                    }
                }
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
                if (path instanceof com.example.roofit.Param) {
                    params.add(new Param(((com.example.roofit.Param) path).value(), args[i].toString()));
                }
            }
        }
        return params;
    }

    private class Param {
        String name;
        Object value;

        Param(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
