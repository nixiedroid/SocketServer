package com.nixiedroid;

import com.nixiedroid.confg.ConfigStub;
import com.nixiedroid.settings.ServerSettingsStub;


public class Starter {
    //Maybe reduce amount of byte[] allocations? There are over10000

    public static void main(String[] args) {
        ConfigStub config = new ConfigStub(new SecretConfig());
        ServerSettingsStub settings = new ServerSettingsStub(new PcSettings());
        Program.setConfig(config,settings);
        Program.start();
        InputReader reader = new InputReader();
    }



}
