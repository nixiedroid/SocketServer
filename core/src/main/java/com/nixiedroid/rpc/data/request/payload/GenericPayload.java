package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.Endiannes;

public class GenericPayload implements BytePackable {
    public static final int SIZE = 12;
    int dataLen1;
    int dataLen2;
    int minor; //Little endian unsigned short (2bytes)
    int major; //Little endian unsigned short (2bytes)
    int padding;

    public GenericPayload(byte[] data) {
        deserialize(data,0);
    }

    @Override
    public GenericPayload deserialize(byte[] data,int start) {
       dataLen1 = ByteArrayUtils.toInt16(data, start+0, Endiannes.LITTLE);
       dataLen2 = ByteArrayUtils.toInt16(data, start+4, Endiannes.LITTLE);
       if (dataLen2!=dataLen1){ //datalen is 32 bit
           dataLen1 = ByteArrayUtils.toInt32(data, start+0, Endiannes.LITTLE);
           dataLen2 = ByteArrayUtils.toInt32(data, start+8, Endiannes.LITTLE);
           padding = 8 ;
       }

        minor = ByteArrayUtils.toInt16(data,start+8+padding, Endiannes.LITTLE);
        major = ByteArrayUtils.toInt16(data,start+10+padding, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[12];
        System.arraycopy(ByteArrayUtils.toBytes(dataLen1), 0, packed, 0, 4);
        System.arraycopy(ByteArrayUtils.toBytes(dataLen2), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.toBytes(minor), 0, packed, 8, 2);
        System.arraycopy(ByteArrayUtils.toBytes(major), 0, packed, 10, 2);
        return packed;
    }

    @Override
    public int size() {
        return SIZE+padding;
    }
}
