package com.nixiedroid.rpc.dynamic.stubs;
import com.nixiedroid.rpc.dynamic.Key;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Config {
    public Config() {
        fillKeyMap();
    }

    public ConcurrentMap<Key,String> keys = new ConcurrentHashMap<>();

    public String getKey(Key key){
       return keys.get(key);
    }

    protected abstract void fillKeyMap();
}
