package com.nixiedroid.rpc.data.payload;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;


public class Payload implements BytePackable {

    int minor; //Little endian unsigned short (2bytes)
    int major; //Little endian unsigned short (2bytes)
    long  isClientVM;
    long status;
    long graceTime;
    byte[] appUUID = new byte[16];
    byte[] skuUUID =new byte[16];
    byte[] counterUUID = new byte[16];
    byte[] cmUUID = new byte[16];
    long requiredClientCount;
    long requestTime;
    byte[] prevUUID = new byte[16];
    byte[] machinename; //2x NUl terminaled string TODO fillMe

    byte[] iv; //should NOT be packed

    @Override
    public Payload deserialize(byte[] data,int start) {
        minor = ByteArrayUtils.ranged.toUInt16L(data, 0);
        major = ByteArrayUtils.ranged.toUInt16L(data, 2);
        isClientVM = ByteArrayUtils.ranged.toUInt32L(data,4);
        status = ByteArrayUtils.ranged.toUInt32L(data, 8);
        graceTime = ByteArrayUtils.ranged.toUInt32L(data,12);
        System.arraycopy(data, 16, appUUID, 0, 16);
        System.arraycopy(data, 32, skuUUID, 0, 16);
        System.arraycopy(data, 48, counterUUID, 0, 16);
        System.arraycopy(data, 64, cmUUID, 0, 16);
        requiredClientCount = ByteArrayUtils.ranged.toUInt32L(data,80);
        requestTime = ByteArrayUtils.ranged.toInt64L(data,84);
        System.arraycopy(data, 92, prevUUID, 0, 16);
        return this;
    }

    @Override
    public byte[] serialize() {
       return new byte[2];
    }

    @Override
    public int size() {
        return 44;
    }
}
