package com.nixiedroid.rpc.dynamic.impl;

import com.nixiedroid.rpc.dynamic.stubs.OutputRoute;
import com.nixiedroid.rpc.util.Date;

public class OutputRouteExample extends OutputRoute {
    @Override
    public void err(String str) {
         System.err.println(Date.getDate() + " - [ERR] - " + str);
    }

    @Override
    public void debug(String str) {
            System.out.println(Date.getDate() + " - [DEBUG] - " + str);
    }

    @Override
    public void info(String str) {
            System.out.println(Date.getDate() + " - [INFO] - " + str);
    }

    @Override
    public void trace(String str) {
            System.out.println(Date.getDate() + " - [TRACE] - " + str);
    }
}
