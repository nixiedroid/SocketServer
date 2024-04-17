package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.data.BytePackable;

public class Trailer implements BytePackable {
    @Override
    public Trailer deserialize(byte[] data,int start) {
            return this;
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public int size() {
        return 0;
    }

}
