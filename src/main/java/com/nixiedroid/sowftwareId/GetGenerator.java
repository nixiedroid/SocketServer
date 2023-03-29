package com.nixiedroid.sowftwareId;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class GetGenerator { //TODO Find all classes with name Generator in THIS package and return BEST
    public AbstractGenerator get(){
        AbstractGenerator generator;
        try {
           Class.forName("com.nixiedroid.sowftwareId.xml.Generator");
           generator = new com.nixiedroid.sowftwareId.xml.Generator();
        } catch (ClassNotFoundException e1){
            try {
                Class.forName("com.nixiedroid.sowftwareId.fallback.Generator");
                generator = new com.nixiedroid.sowftwareId.fallback.Generator();
            } catch (ClassNotFoundException e2){
                try {
                    Class.forName("com.nixiedroid.sowftwareId.simpleGenerator.Generator");
                    generator = new com.nixiedroid.sowftwareId.simpleGenerator.Generator();
                } catch (ClassNotFoundException e3){
                        throw new RuntimeException("No class found");
                }
            }
        }
        return generator;
    }
    private Set<Class> findAllClassesUsingClassLoader() {
        String packageName = this.getClass().getPackage().getName();
        InputStream stream = ClassLoader.getSystemClassLoader()
                                        .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                     .filter(line -> line.endsWith(".class"))
                     .map(line -> getClass(line, packageName))
                     .collect(Collectors.toSet());
     //   for()
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                                         + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
