package com.nixiedroid.dynamic.sowftwareId;



public class SoftwareIDLoader {
    public static SoftwareIDGeneratorStub load(String classname){
        SoftwareIDGeneratorStub generator;
        try  {
            Class loadedClass = Class.forName(classname);
            generator = (SoftwareIDGeneratorStub) loadedClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            generator = new SoftwareIDGenerator();
        }
        return generator;
    }

}
