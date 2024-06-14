package com.nixiedroid.rpc;

import com.nixiedroid.rpc.server.Server;

public class Program {
    //Maybe reduce amount of byte[] allocations? There are over10000

    private static final Server server = Server.getInstance();
    private static boolean isInitialised = false;

    public static void main(String[] args) {
        Context.i();
        Program.start();
    }
    public static void init(){
        Context.i();
        Program.start();
    }

    public static void start(){
        server.startServerThread();
        isInitialised = true;
    }
    public static void stop() {
        if (isInitialised) {
            server.stop();
        }
    }

}