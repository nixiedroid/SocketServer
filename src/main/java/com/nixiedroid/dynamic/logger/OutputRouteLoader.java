package com.nixiedroid.dynamic.logger;

public class OutputRouteLoader {
    public static OutputRouteStub load(String classname){
        OutputRouteStub route;
        try  {
            Class loadedClass = Class.forName(classname);
            route = (OutputRouteStub) loadedClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            route = new OutputRoute();
        }
        return route;
    }
}
