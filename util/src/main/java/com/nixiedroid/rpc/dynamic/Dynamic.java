package com.nixiedroid.rpc.dynamic;

import com.nixiedroid.rpc.dynamic.stubs.Config;
import com.nixiedroid.rpc.dynamic.stubs.OutputRoute;
import com.nixiedroid.rpc.dynamic.stubs.Settings;
import com.nixiedroid.rpc.dynamic.stubs.SoftwareIDGenerator;

public enum Dynamic {
    SETTINGS(Settings.class.getSimpleName()),
    CONFIG(Config.class.getSimpleName()),
    LOGGER_ROUTE(OutputRoute.class.getSimpleName()),
    SID_GENERATOR(SoftwareIDGenerator.class.getSimpleName());

    private final String name;

    Dynamic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
