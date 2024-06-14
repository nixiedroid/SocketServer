package com.nixiedroid.rpc.dynamic.config;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigStub {
    public ConfigStub() {
        fillKeyMap();
    }
    public abstract void fillKeyMap();

    public Map<String,String> keys = new HashMap<>();
    public String getKey(String keyName){
        for (Map.Entry<String,String> e: keys.entrySet()) {
            if (e.getKey().equals(keyName)) return e.getValue();
        }
        return "";
       // return keys.get(keyName);
    }
}
