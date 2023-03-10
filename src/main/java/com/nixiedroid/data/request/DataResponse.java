package com.nixiedroid.data.request;

import com.nixiedroid.data.Data;
import com.nixiedroid.util.ByteArrayUtils;

public class DataResponse extends Data {

    public static final int SIZE = 8;
    long alloc_hint; //UInt 32 L
    int uuidId; //UInt 16 L
    int cancelCount; //UByte
    int padLen;//UByte  Padding
    byte[] payload;
    int payloadSize = 0;

    @Override
    public void unpack(byte[] data) {
       alloc_hint = ByteArrayUtils.ranged.toUInt32L(data,0);
       uuidId = ByteArrayUtils.ranged.toUInt16L(data, 4);
       cancelCount = ByteArrayUtils.ranged.toUInt8(data,6);
       padLen = ByteArrayUtils.ranged.toUInt8(data,7);
       payloadSize = data.length - SIZE;
       payload = new byte[payloadSize];
       System.arraycopy(data,8,payload,0,payloadSize);
    }

    @Override
    public byte[] pack() {
        alloc_hint = payload.length;
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(alloc_hint),0,packed,0,4);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(uuidId), 0, packed, 4, 2);
        packed[6] = ByteArrayUtils.uByteToByte(cancelCount);
        packed[7] = ByteArrayUtils.uByteToByte(padLen);
        System.arraycopy(payload,0,packed,8,payloadSize);
        return packed;
    }

    @Override
    public int size() {
        payloadSize = payload.length;
        return SIZE+payloadSize+padLen;
    }
}
