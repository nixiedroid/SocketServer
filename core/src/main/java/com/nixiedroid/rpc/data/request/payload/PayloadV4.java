package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class PayloadV4 implements BytePackable {
public static final int SIZE = 12;
    int dataLen1;
    int dataLen2;
    byte[] response;
    byte[] hash;
    int pad;

    public PayloadV4(byte[] response, byte[] hash) {
        this.response = response;
        this.hash = hash;
        dataLen1 = response.length+hash.length;
        dataLen2 = response.length+hash.length;
        pad = (int) (4 + (((~dataLen1 & 3) + 1) & 3));
    }

    @Override
    public PayloadV4 deserialize(byte[] data,int size) {
        dataLen1 = ByteArrayUtils.toInt32(data, 0, Endiannes.LITTLE);
        dataLen2 = ByteArrayUtils.toInt16(data, 4, Endiannes.LITTLE);
        response = new byte[(int) (dataLen1 - 16)];
        hash = new byte[16];
        System.arraycopy(data,8,response,0,response.length);
        System.arraycopy(data,data.length-17,hash,0,16);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.toBytes(dataLen1,Endiannes.LITTLE), 0, packed, 0, 4);
        System.arraycopy(ByteArrayUtils.toBytes(0x200,Endiannes.BIG),0,packed,4,4);
        System.arraycopy(ByteArrayUtils.toBytes(dataLen2,Endiannes.LITTLE), 0, packed, 8, 4);
        System.arraycopy(response, 0, packed, 12, response.length);
        System.arraycopy(hash, 0, packed, response.length+12, 16);
        return packed;
    }

    @Override
    public int size() {
        return 28+response.length+pad;
    }
}
