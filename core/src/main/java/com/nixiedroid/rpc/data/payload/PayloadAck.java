package com.nixiedroid.rpc.data.payload;

import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.Endiannes;

public class PayloadAck implements BytePackable {
    int minor; //Little endian unsigned short (2bytes)
    int major; //Little endian unsigned short (2bytes)
    int softwareIdLen; //sowftwareId.length + 2 current 0x64=100
    byte[] softwareId;
    byte[] cmUUID = new byte[16];
    long ackTime;
    int clientCount; //Uint32
    int pingTime;
    int delayTime;

    byte[] iv; //should NOT be packed

    @Override
    public PayloadAck deserialize(byte[] data,int size) {
        minor = ByteArrayUtils.toInt16(data, 0, Endiannes.LITTLE);
        major = ByteArrayUtils.toInt16(data, 2, Endiannes.LITTLE);
        softwareIdLen = ByteArrayUtils.toInt16(data, 4, Endiannes.LITTLE);
        softwareId = new byte[softwareIdLen];
        System.arraycopy(data, 8, softwareId, 0, softwareIdLen);
        System.arraycopy(data, 8 + softwareIdLen, cmUUID, 0, 16);
        ackTime = ByteArrayUtils.toInt64(data,24 + softwareIdLen,Endiannes.LITTLE);
        clientCount = ByteArrayUtils.toInt32(data,32+ softwareIdLen, Endiannes.LITTLE);
        pingTime = ByteArrayUtils.toInt32(data, 36+ softwareIdLen, Endiannes.LITTLE);
        delayTime = ByteArrayUtils.toInt32(data, 40+ softwareIdLen, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[softwareIdLen +44];
        System.arraycopy(ByteArrayUtils.toBytes(minor),0,packed,0,2);
        System.arraycopy(ByteArrayUtils.toBytes(major),0,packed,2,2);
        System.arraycopy(ByteArrayUtils.toBytes(softwareIdLen), 0, packed, 4, 2);
        System.arraycopy(softwareId, 0, packed, 8, softwareIdLen -2);
        System.arraycopy(cmUUID, 0, packed, 8+ softwareIdLen, 16);
        System.arraycopy(ByteArrayUtils.toBytes(ackTime), 0, packed, 24 + softwareIdLen, 8);
        System.arraycopy(ByteArrayUtils.toBytes(clientCount), 0, packed, 32 + softwareIdLen, 4);
        System.arraycopy(ByteArrayUtils.toBytes(pingTime), 0, packed, 36 + softwareIdLen, 4);
        System.arraycopy(ByteArrayUtils.toBytes(delayTime), 0, packed, 40 + softwareIdLen, 4);
        return packed;
    }

    @Override
    public int size() {
        if (softwareIdLen != 0) return softwareIdLen +44;
        else return 44;
    }
}
