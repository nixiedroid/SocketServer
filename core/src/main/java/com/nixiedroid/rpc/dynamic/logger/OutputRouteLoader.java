package com.nixiedroid.rpc.dynamic.logger;

public class OutputRouteLoader {
    public static OutputRouteStub load(String classname){
        OutputRouteStub route;
        try  {
            Class<?> loadedClass = Class.forName(classname);
            route = (OutputRouteStub) loadedClass.getConstructor().newInstance();
        } catch (Exception e) {
            route = new OutputRoute();
        }
        return route;
    }
}
