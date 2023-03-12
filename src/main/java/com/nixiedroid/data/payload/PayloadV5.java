package com.nixiedroid.data.payload;

import com.nixiedroid.data.Packable;
import com.nixiedroid.util.ByteArrayUtils;

public class PayloadV5 implements Packable {

    public static final int SIZE = 12;
    long dataLen1;
    long dataLen2;
    int minor; //Little endian unsigned short (2bytes)
    int major; //Little endian unsigned short (2bytes)
    byte[] salt;
    byte[] crypted;
    int pad;

    public PayloadV5(byte[] crypted, byte[] salt, int major,int minor) {
        this.crypted = crypted;
        this.salt = salt;
        this.major = major;
        this.minor = minor;
        dataLen1 = 4+salt.length+crypted.length;
        dataLen2 = 4+salt.length+crypted.length;
        pad = (int) (4 + (((~dataLen1 & 3) + 1) & 3));
    }

    @Override
    public void unpack(byte[] data) {
        dataLen1 = ByteArrayUtils.ranged.toUInt32L(data, 0);
        dataLen2 = ByteArrayUtils.ranged.toUInt16L(data, 4);
        minor = ByteArrayUtils.ranged.toUInt16L(data,8);
        major = ByteArrayUtils.ranged.toUInt16L(data,10);
    }

    @Override
    public byte[] pack() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(dataLen1), 0, packed, 0, 4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesB(0x00000200L),0,packed,4,4);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(dataLen2), 0, packed, 8, 4);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(minor), 0, packed, 12, 2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(major), 0, packed, 14, 2);
        System.arraycopy(salt, 0, packed, 16, 16);
        System.arraycopy(crypted, 0, packed, 32, crypted.length);
        return packed;
    }

    @Override
    public int size() {
        return 32+crypted.length+pad;
    }
}
