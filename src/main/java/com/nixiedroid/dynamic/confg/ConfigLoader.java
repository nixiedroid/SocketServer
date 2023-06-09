package com.nixiedroid.dynamic.confg;

public class ConfigLoader {
    public static ConfigStub load(String classname){
        ConfigStub generator;
        try  {
            Class loadedClass = Class.forName(classname);
            generator = (ConfigStub) loadedClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            generator = new ConfigExample();
        }
        return generator;
    }
}
