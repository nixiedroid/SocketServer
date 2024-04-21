package com.nixiedroid.rpc.dynamic.config;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ConfigStub {
    public ConfigStub() {
        fillKeyMap();
    }
    public abstract String getKeyV1();

    public abstract String getKeyV2();

    public abstract String getKeyV3();

    public abstract String getKeyV4();

    public abstract String getKeyV5();

    public abstract String getKeyV6();

    public abstract String getUuid32();

    public abstract String getUuid64();

    public abstract String getUuidTime();

    public abstract String getUuidTimeA();

    public abstract String getUuidEmpty();

    public abstract String getUuid14();

    public abstract String getUuid15();

    public abstract String getAbstractUuid();

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
