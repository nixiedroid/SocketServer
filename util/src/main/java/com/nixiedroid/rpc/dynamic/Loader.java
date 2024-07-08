package com.nixiedroid.rpc.dynamic;

public class Loader {
    public static Object loadImplOrExample(Dynamic dynamic) {
        String name = dynamic.getName();
        Object o = load(name + "Impl");
        if (o==null) {
            o =  load(name+"Example");
            if (o==null) throw new RuntimeException("Could not load: " + dynamic.getName() );
        }
        return o;
    }

    private static Object load(String simpleClassName) {
        String pkg = Loader.class.getPackage().getName();
        try {
            String className = pkg + ".impl." + simpleClassName;
            return  Class.forName(className).getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
