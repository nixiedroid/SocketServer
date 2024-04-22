package com.nixiedroid.rpc.data.request;

import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class Response extends Data {

    private static final int SIZE = 8;
    long alloc_hint; //UInt 32 L
    int uuidId; //UInt 16 L
    byte cancelCount; //UByte
    byte padLen;//UByte  Padding
    byte[] payload;
    int payloadSize = 0;

    @Override
    public Response deserialize(byte[] data, int start) {
       alloc_hint = ByteArrayUtils.toInt32(data,start, Endiannes.LITTLE);
       uuidId = ByteArrayUtils.toInt16(data, start+4, Endiannes.LITTLE);
       cancelCount = ByteArrayUtils.toInt8(data,start+6);
       padLen = ByteArrayUtils.toInt8(data,start+6);
       payloadSize = data.length - SIZE;
       payload = new byte[payloadSize];
       System.arraycopy(data,8,payload,0,payloadSize);
       return this;
    }

    @Override
    public byte[] serialize() {
        alloc_hint = payload.length;
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.toBytes(alloc_hint),0,packed,0,4);
        System.arraycopy(ByteArrayUtils.toBytes(uuidId), 0, packed, 4, 2);
        System.arraycopy(ByteArrayUtils.toBytes(cancelCount), 0, packed, 6, 1);
        System.arraycopy(ByteArrayUtils.toBytes(padLen), 0, packed, 6, 1);
        System.arraycopy(payload,0,packed,8,payloadSize);
        return packed;
    }

    @Override
    public int size() {
        payloadSize = payload.length;
        return SIZE+payloadSize+padLen;
    }
}
