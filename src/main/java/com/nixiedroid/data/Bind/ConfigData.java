package com.nixiedroid.data.Bind;

import com.nixiedroid.data.Packable;

public class ConfigData implements Packable {
    public ConfigData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    private byte[] data;
    @Override
    public void unpack(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] pack() {
        return data;
    }

    @Override
    public int size() {
        return data.length;
    }
}
