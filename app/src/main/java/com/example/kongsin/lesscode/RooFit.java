package com.example.kongsin.lesscode;

import java.lang.reflect.Proxy;

/**
 * Created by kongsin on 8/20/16.
 */

public class RooFit {
    public <T> T create(Class<T> interfaceClass, String baseUrl){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new MyHandler(baseUrl));
    }
}

