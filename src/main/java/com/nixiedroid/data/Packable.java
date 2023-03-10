package com.nixiedroid.data;

public interface Packable {
    void unpack(byte[] data);

    byte[] pack();

    int size();
}
