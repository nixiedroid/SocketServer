package com.nixiedroid.rpc.dynamic.config;

import java.lang.reflect.InvocationTargetException;

public class ConfigLoader {
    public static ConfigStub load(String classname){
        ConfigStub generator;
        String pkg = ConfigLoader.class.getPackage().getName();
        try  {
            Class<?> loadedClass = Class.forName(pkg+".impl."+classname);
            generator = (ConfigStub) loadedClass.getConstructor().newInstance();
        } catch (Exception e) {
            generator = new ConfigExample();
        }
        return generator;
    }
}
