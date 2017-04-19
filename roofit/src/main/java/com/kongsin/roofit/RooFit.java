package com.kongsin.roofit;

import java.lang.reflect.Proxy;

/**
 * Created by kongsin on 8/20/16.
 */

public class RooFit {
    public static  <T> T create(Class<T> interfaceClass, String baseUrl){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new MethodManager(baseUrl));
    }
}

