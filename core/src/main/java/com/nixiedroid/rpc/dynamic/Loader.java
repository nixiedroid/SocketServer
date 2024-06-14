package com.nixiedroid.rpc.dynamic;

import com.nixiedroid.rpc.dynamic.config.ConfigStub;

import java.util.function.Function;

public class Loader implements Function<String,Object> {
    @Override
    public Object apply(String classname) {
        ConfigStub generator;
        String pkg = Loader.class.getPackage().getName();
        try  {
            Class<?> loadedClass = Class.forName(pkg +"."+ classname.toLowerCase()+"."+ classname);
            generator = (ConfigStub) loadedClass.getConstructor().newInstance();
        } catch (Exception e) {
            generator = null;
        }
        return generator;
    }
}
