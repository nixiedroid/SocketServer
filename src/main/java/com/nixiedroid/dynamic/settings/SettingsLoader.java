package com.nixiedroid.dynamic.settings;


public class SettingsLoader {
    public static SettingsStub load(String classname){
        SettingsStub settings;
        try  {
            Class loadedClass = Class.forName(classname);
            settings = (SettingsStub) loadedClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            settings = new SettingsExample() {
            };
        }
        return settings;
    }
}
