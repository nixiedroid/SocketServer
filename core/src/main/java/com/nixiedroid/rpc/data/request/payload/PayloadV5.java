package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class PayloadV5 implements BytePackable {

    public static final int SIZE = 12;
    int dataLen1;
    int dataLen2;
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
    public PayloadV5 deserialize(byte[] data,int size) {
        dataLen1 = ByteArrayUtils.toInt32(data, 0, Endiannes.LITTLE);
        dataLen2 = ByteArrayUtils.toInt16(data, 4, Endiannes.LITTLE);
        minor = ByteArrayUtils.toInt16(data,8, Endiannes.LITTLE);
        major = ByteArrayUtils.toInt16(data,10, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.toBytes(dataLen1,Endiannes.LITTLE), 0, packed, 0, 4);
        System.arraycopy(ByteArrayUtils.toBytes(0x200,Endiannes.BIG),0,packed,4,4);
        System.arraycopy(ByteArrayUtils.toBytes(dataLen2,Endiannes.LITTLE), 0, packed, 8, 4);
        System.arraycopy(ByteArrayUtils.toBytes(minor,Endiannes.LITTLE), 0, packed, 12, 2);
        System.arraycopy(ByteArrayUtils.toBytes(major,Endiannes.LITTLE), 0, packed, 14, 2);
        System.arraycopy(salt, 0, packed, 16, 16);
        System.arraycopy(crypted, 0, packed, 32, crypted.length);
        return packed;
    }

    @Override
    public int size() {
        return 32+crypted.length+pad;
    }
}
