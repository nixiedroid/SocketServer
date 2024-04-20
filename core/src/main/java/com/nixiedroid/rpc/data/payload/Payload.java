package com.nixiedroid.rpc.data.payload;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;


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
        minor = ByteArrayUtils.toInt16(data, 0, Endiannes.LITTLE);
        major = ByteArrayUtils.toInt16(data, 2, Endiannes.LITTLE);
        isClientVM = ByteArrayUtils.toInt32(data,4, Endiannes.LITTLE);
        status = ByteArrayUtils.toInt32(data, 8, Endiannes.LITTLE);
        graceTime = ByteArrayUtils.toInt32(data,12, Endiannes.LITTLE);
        System.arraycopy(data, 16, appUUID, 0, 16);
        System.arraycopy(data, 32, skuUUID, 0, 16);
        System.arraycopy(data, 48, counterUUID, 0, 16);
        System.arraycopy(data, 64, cmUUID, 0, 16);
        requiredClientCount = ByteArrayUtils.toInt32(data,80, Endiannes.LITTLE);
        requestTime = ByteArrayUtils.toInt64(data,84,Endiannes.LITTLE);
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
