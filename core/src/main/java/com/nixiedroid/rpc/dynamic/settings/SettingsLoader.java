package com.nixiedroid.rpc.dynamic.settings;


public class SettingsLoader {
    public static SettingsStub load(String classname){
        SettingsStub settings;
        String pkg = SettingsLoader.class.getPackage().getName();
        try  {
            Class<?> loadedClass = Class.forName(pkg+".impl."+classname);
            settings = (SettingsStub) loadedClass.newInstance();
        } catch (Exception e) {
            settings = new SettingsExample() {
            };
        }
        return settings;
    }
}
