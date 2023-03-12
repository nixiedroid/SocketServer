package com.nixiedroid.data.payload;

import com.nixiedroid.util.ByteArrayUtils;
import com.nixiedroid.data.Packable;

public class PayloadAck implements Packable {
    int minor; //Little endian unsigned short (2bytes)
    int major; //Little endian unsigned short (2bytes)
    int softwareIdLen; //sowftwareId.length + 2 current 0x64=100
    byte[] softwareId;
    byte[] cmUUID = new byte[16];
    long ackTime;
    long clientCount; //Uint32
    long pingTime;
    long delayTime;

    byte[] iv; //should NOT be packed

    @Override
    public void unpack(byte[] data) {
        minor = ByteArrayUtils.ranged.toUInt16L(data, 0);
        major = ByteArrayUtils.ranged.toUInt16L(data, 2);
        softwareIdLen = ByteArrayUtils.ranged.toUInt16L(data, 4);
        softwareId = new byte[softwareIdLen];
        System.arraycopy(data, 8, softwareId, 0, softwareIdLen);
        System.arraycopy(data, 8 + softwareIdLen, cmUUID, 0, 16);
        ackTime = ByteArrayUtils.ranged.toInt64L(data,24 + softwareIdLen);
        clientCount = ByteArrayUtils.ranged.toUInt32L(data,32+ softwareIdLen);
        pingTime = ByteArrayUtils.ranged.toUInt32L(data, 36+ softwareIdLen);
        delayTime = ByteArrayUtils.ranged.toUInt32L(data, 40+ softwareIdLen);
    }

    @Override
    public byte[] pack() {
        byte[] packed = new byte[softwareIdLen +44];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(minor),0,packed,0,2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(major),0,packed,2,2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(softwareIdLen), 0, packed, 4, 2);
        System.arraycopy(softwareId, 0, packed, 8, softwareIdLen -2);
        System.arraycopy(cmUUID, 0, packed, 8+ softwareIdLen, 16);
        System.arraycopy(ByteArrayUtils.int64ToBytesL(ackTime), 0, packed, 24 + softwareIdLen, 8);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(clientCount), 0, packed, 32 + softwareIdLen, 4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(pingTime), 0, packed, 36 + softwareIdLen, 4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(delayTime), 0, packed, 40 + softwareIdLen, 4);
        return packed;
    }

    @Override
    public int size() {
        if (softwareIdLen != 0) return softwareIdLen +44;
        else return 44;
    }
}
