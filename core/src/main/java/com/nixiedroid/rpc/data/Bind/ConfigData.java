package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.data.BytePackable;

public class ConfigData implements BytePackable {
    public ConfigData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    private byte[] data;
    @Override
    public ConfigData deserialize(byte[] data,int start) {
        this.data = data;
        return this;
    }

    @Override
    public byte[] serialize() {
        return data;
    }

    @Override
    public int size() {
        return data.length;
    }
}
