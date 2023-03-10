package com.nixiedroid.data.Bind;

import com.nixiedroid.data.Packable;

public class Trailer implements Packable {
    @Override
    public void unpack(byte[] data) {

    }

    @Override
    public byte[] pack() {
        return new byte[0];
    }

    @Override
    public int size() {
        return 0;
    }

}
