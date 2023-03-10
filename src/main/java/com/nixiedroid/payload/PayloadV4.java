package com.nixiedroid.payload;

import com.nixiedroid.data.Packable;
import com.nixiedroid.util.ByteArrayUtils;

public class PayloadV4 implements Packable {
public static final int SIZE = 12;
    long dataLen1;
    long dataLen2;
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
    public void unpack(byte[] data) {
        dataLen1 = ByteArrayUtils.ranged.toUInt32L(data, 0);
        dataLen2 = ByteArrayUtils.ranged.toUInt16L(data, 4);
        response = new byte[(int) (dataLen1 - 16)];
        hash = new byte[16];
        System.arraycopy(data,8,response,0,response.length);
        System.arraycopy(data,data.length-17,hash,0,16);
    }

    @Override
    public byte[] pack() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(dataLen1), 0, packed, 0, 4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesB(0x00000200L),0,packed,4,4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(dataLen2), 0, packed, 8, 4);
        System.arraycopy(response, 0, packed, 12, response.length);
        System.arraycopy(hash, 0, packed, response.length+12, 16);
        return packed;
    }

    @Override
    public int size() {
        return 28+response.length+pad;
    }
}
