package com.nixiedroid.rpc.data.request;

import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.util.ByteArrayUtils;

public class DataRequest extends Data {
    public static final int SIZE = 24;
    long allocHint;
    int uuidId;
    int opName;
    int uuidLength; //16 if self["flags"] & 0x80 > 0 else 0
    byte[] uuid = new byte[16];
    int dataLen; //=fragLen-authLen-SIZE-(authLen > 0)?8:0

    byte[] payload;

   // int padLen; //=(4 - ((SIZE + ((flags & 0x80) > 0)?16:0 + pduData.length()) & 3) & 3)

    @Override
    public DataRequest deserialize(byte[] data,int start) {
        allocHint = ByteArrayUtils.ranged.toUInt32L(data, 0);
        uuidId = ByteArrayUtils.ranged.toUInt16L(data, 4);
        opName = ByteArrayUtils.ranged.toUInt16L(data, 6);
        byte[] dataChunk;
        dataLen = getHeader().getFragLen() - getHeader().getAuthLen() - DataRequest.SIZE;
        dataLen -= (getHeader().getAuthLen() > 0) ? 8 : 0;
        if (getHeader().getFlags().checkFlag(PacketFlagsHolder.PacketFlag.OBJECTUUID)){
            uuidLength = 16;
        } else uuidLength = 0;
        if (uuidLength > 0) System.arraycopy(data, 8, uuid, 0, 16);
        dataChunk = new byte[dataLen];
        System.arraycopy(data, 8+uuidLength, dataChunk, 0, dataLen);
        payload = dataChunk;
        return this;
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public int size() {
        return SIZE;
    }
}
