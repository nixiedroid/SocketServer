package com.nixiedroid.rpc.data.request;

import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class Request extends Data {
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
    public Request deserialize(byte[] data, int start) {
        allocHint = ByteArrayUtils.toInt32(data, 0, Endiannes.LITTLE);
        uuidId = ByteArrayUtils.toInt16(data, 4, Endiannes.LITTLE);
        opName = ByteArrayUtils.toInt16(data, 6, Endiannes.LITTLE);
        byte[] dataChunk;
        dataLen = getHeader().getFragLen() - getHeader().getAuthLen() - Request.SIZE;
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
