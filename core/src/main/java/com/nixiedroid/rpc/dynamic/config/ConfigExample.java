package com.nixiedroid.rpc.dynamic.config;

public class ConfigExample extends ConfigStub{
    private static final String KEY_V6 = "66666666666666666666666666666666";
    private static final String KEY_V5 = "55555555555555555555555555555555";
    private static final String KEY_V4 = "44444444444444444444444444444444";
    private static final String KEY_V3 = "33333333333333333333333333333333";
    private static final String KEY_V2 = "22222222222222222222222222222222";
    private static final String KEY_V1 = "11111111111111111111111111111111";


    @Override
    public String getKeyV1() {
        return KEY_V1;
    }

    @Override
    public String getKeyV2() {
        return KEY_V2;
    }

    @Override
    public String getKeyV3() {
        return KEY_V3;
    }

    @Override
    public String getKeyV4() {
        return KEY_V4;
    }

    @Override
    public String getKeyV5() {
        return KEY_V5;
    }

    @Override
    public String getKeyV6() {
        return KEY_V6;
    }

    @Override
    public String getUuid32() {
        return "00000000000000000000000000000000";
    }

    @Override
    public String getUuid64() {
        return "00000000000000000000000000000000";
    }

    @Override
    public String getUuidTime() {
        return "00000000000000000000000000000000";
    }

    @Override
    public String getUuidTimeA() {
        return "00000000000000000000000000000000";
    }

    @Override
    public String getUuidEmpty() {
        return "00000000000000000000000000000000";
    }

    @Override
    public String getUuid14() {
        return "00000000000000000000000000000000";
    }

    @Override
    public String getUuid15() {
        return "00000000000000000000000000000000";
    }
    public String getAbstractUuid(){
        return "00000000000000000000000000000000";
    }

    @Override
    public void fillKeyMap() {

    }
}
