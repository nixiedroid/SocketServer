package com.nixiedroid.rpc.data;

public interface  BytePackable<T> {
    T deserialize(final byte[] data,int start);

    byte[] serialize();
    int size();
}
