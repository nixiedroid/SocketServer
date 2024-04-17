package com.nixiedroid.rpc.dynamic.sowftwareId;



public class SoftwareIDLoader {
    public static SoftwareIDGeneratorStub load(String classname){
        SoftwareIDGeneratorStub generator;
        try  {
            Class<?> loadedClass = Class.forName(classname);
            generator = (SoftwareIDGeneratorStub) loadedClass.newInstance();
        } catch (Exception e) {
            generator = new SoftwareIDGenerator();
        }
        return generator;
    }

}
