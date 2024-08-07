package com.nixiedroid.rpc.dynamic.impl;

import com.nixiedroid.rpc.dynamic.stubs.Config;

import static com.nixiedroid.rpc.dynamic.Key.*;

public class ConfigExample extends Config {
    @Override
    public void fillKeyMap() {
        keys.put(KEYV6,"66666666666666666666666666666666");
        keys.put(KEYV5,"55555555555555555555555555555555");
        keys.put(KEYV4,"44444444444444444444444444444444");
        keys.put(UUID32,"00000000000000000000000000000000");
        keys.put(UUID64,"00000000000000000000000000000000");
        keys.put(UUIDTime,"00000000000000000000000000000000");
        keys.put(UUIDTimeA,"00000000000000000000000000000000");
        keys.put(UUIDEmpty,"00000000000000000000000000000000");
        keys.put(UUID14,"00000000000000000000000000000000");
        keys.put(UUID15,"00000000000000000000000000000000");
        keys.put(ABSTRACT_UUID,"00000000000000000000000000000000");
    }
}
