package com.example.kongsin.lesscode;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by kongsin on 8/20/16.
 */

public class MyHandler implements InvocationHandler {
    private static final String TAG = "MyHandler";
    private String mBaseUrl;

    public MyHandler(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
            throws Throwable {
        m.setAccessible(true);
        Type rett = m.getGenericReturnType();
        String typeClass = rett.toString().split("<")[1].split(">")[0];
        Object returnObject = null;
        Constructor constructor = m.getReturnType().getDeclaredConstructor(String.class, String.class, Class.class);
        constructor.setAccessible(true);
        Annotation[] annotations = m.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof URL) {
                returnObject = constructor.newInstance(((URL) annotation).url(), mBaseUrl, Class.forName(typeClass));
            }
        }
        return returnObject;
    }
}
