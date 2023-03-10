package com.nixiedroid.data.payload;

import com.nixiedroid.util.ByteArrayUtils;
import com.nixiedroid.data.Packable;

public class GenericPayload implements Packable {
    public static final int SIZE = 12;
    long dataLen1;
    long dataLen2;
    int minor; //Little endian unsigned short (2bytes)
    int major; //Little endian unsigned short (2bytes)

    public GenericPayload(byte[] data) {
        unpack(data);
    }

    @Override
    public void unpack(byte[] data) {
       dataLen1 = ByteArrayUtils.ranged.toUInt32L(data, 0);
       dataLen2 = ByteArrayUtils.ranged.toUInt16L(data, 4);
        minor = ByteArrayUtils.ranged.toUInt16L(data,8);
        major = ByteArrayUtils.ranged.toUInt16L(data,10);
    }

    @Override
    public byte[] pack() {
        byte[] packed = new byte[12];
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(dataLen1), 0, packed, 0, 4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(dataLen2), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(minor), 0, packed, 8, 2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(major), 0, packed, 10, 2);
        return packed;
    }

    @Override
    public int size() {
        return SIZE;
    }
}
