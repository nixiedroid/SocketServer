package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.UUID;

public class PayloadAck implements BytePackable {
    short minor; //Little endian unsigned short (2bytes)
    short major; //Little endian unsigned short (2bytes)
    int softwareIdLen; //sowftwareId.length + 2 current 0x64=100
    byte[] softwareId;
    UUID cmUUID;
    long ackTime;
    int clientCount; //Uint32
    int pingTime;
    int delayTime;

    byte[] iv; //should NOT be packed

    @Override
    public PayloadAck deserialize(byte[] data, int start) {
        minor = ByteArrayUtils.toInt16(data, start + 0, Endiannes.LITTLE);
        major = ByteArrayUtils.toInt16(data, start + 2, Endiannes.LITTLE);
        softwareIdLen = ByteArrayUtils.toInt16(data, start + 4, Endiannes.LITTLE);
        softwareId = new byte[softwareIdLen];
        System.arraycopy(data, start + 8, softwareId, 0, softwareIdLen);
        cmUUID = new UUID(data, start + 8 + softwareIdLen);
        ackTime = ByteArrayUtils.toInt64(data, start + 24 + softwareIdLen, Endiannes.LITTLE);
        clientCount = ByteArrayUtils.toInt32(data, start + 32 + softwareIdLen, Endiannes.LITTLE);
        pingTime = ByteArrayUtils.toInt32(data, start + 36 + softwareIdLen, Endiannes.LITTLE);
        delayTime = ByteArrayUtils.toInt32(data, start + 40 + softwareIdLen, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[softwareIdLen + 44];
        System.arraycopy(ByteArrayUtils.toBytes(minor), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(major), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.toBytes(softwareIdLen), 0, packed, 4, 2);
        System.arraycopy(softwareId, 0, packed, 8, softwareIdLen - 2);
        System.arraycopy(cmUUID.serialize(), 0, packed, 8 + softwareIdLen, 16);
        System.arraycopy(ByteArrayUtils.toBytes(ackTime), 0, packed, 24 + softwareIdLen, 8);
        System.arraycopy(ByteArrayUtils.toBytes(clientCount), 0, packed, 32 + softwareIdLen, 4);
        System.arraycopy(ByteArrayUtils.toBytes(pingTime), 0, packed, 36 + softwareIdLen, 4);
        System.arraycopy(ByteArrayUtils.toBytes(delayTime), 0, packed, 40 + softwareIdLen, 4);
        return packed;
    }

    @Override
    public int size() {
        if (softwareIdLen != 0) return softwareIdLen + 44;
        else return 44;
    }
}
