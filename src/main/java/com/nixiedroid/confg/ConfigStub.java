package com.nixiedroid.confg;

import com.nixiedroid.logger.LoggerStub;
import com.nixiedroid.sowftwareId.GeneratorStub;

public class ConfigStub implements Config {
    private final Config config;

    public ConfigStub(Config config) {
        this.config = config;
    }

    @Override
    public GeneratorStub getGenerator() {
        return config.getGenerator();
    }

    @Override
    public LoggerStub logger() {
        return config.logger();
    }

    @Override
    public String getKeyV1() {
       return config.getKeyV1();
    }

    @Override
    public String getKeyV2() {
        return config.getKeyV2();
    }

    @Override
    public String getKeyV3() {
        return config.getKeyV3();
    }

    @Override
    public String getKeyV4() {
        return config.getKeyV4();
    }

    @Override
    public String getKeyV5() {
        return config.getKeyV5();
    }

    @Override
    public String getKeyV6() {
        return config.getKeyV6();
    }

    @Override
    public String getUuid32() {
        return config.getUuid32();
    }

    @Override
    public String getUuid64() {
        return config.getUuid64();
    }

    @Override
    public String getUuidTime() {
        return config.getUuidTime();
    }

    @Override
    public String getUuidTimeA() {
        return config.getUuidTimeA();
    }

    @Override
    public String getUuidEmpty() {
        return config.getUuidEmpty();
    }

    @Override
    public String getUuid14() {
        return config.getUuid14();
    }

    @Override
    public String getUuid15() {
        return config.getUuid15();
    }
    public String getAbstractUuid(){ return config.getAbstractUuid();}
}
