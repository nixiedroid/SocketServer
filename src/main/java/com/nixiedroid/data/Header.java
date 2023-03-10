package com.nixiedroid.data;

import com.nixiedroid.util.ByteArrayUtils;


public class Header implements Packable {
    public static final int SIZE = 16;
    int major; //Byte
    int minor; //Byte
     int type; //Byte
    int flags; //Byte
    long representation; //Little endian unsigned long (4bytes)
    int fragLen; //little endian unsigned short (2bytes) TOTAL MESSAGE SIZE in bytes, INCLUDING HEADER
    int authLen; //little endian unsigned short
    long callId; //Little endian unsigned long (4bytes)

    public Header() {
    }

    public Header(byte[] data) {
        unpack(data);
    }



    @Override
    public void unpack(byte[] data) {
        major = (data[0] & 0xFF);
        minor = (data[1] & 0xFF);
        type = (data[2] & 0xFF);
        flags = (data[3] & 0xFF);
        representation = ByteArrayUtils.ranged.toUInt32L(data, 4);
        fragLen = ByteArrayUtils.ranged.toUInt16L(data, 8);
        authLen = ByteArrayUtils.ranged.toUInt16L(data, 10);
        callId = ByteArrayUtils.ranged.toUInt32L(data, 12);
    }

    @Override
    public byte[] pack() {
        byte[] packed = new byte[16];
        packed[0] = ByteArrayUtils.uByteToByte(major);
        packed[1] = ByteArrayUtils.uByteToByte(minor);
        packed[2] = ByteArrayUtils.uByteToByte(type);
        packed[3] = ByteArrayUtils.uByteToByte(flags);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(representation), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(fragLen), 0, packed, 8, 2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(authLen), 0, packed, 10, 2);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(callId), 0, packed, 12, 4);
        return packed;
    }

    @Override
    public int size() {
        return SIZE;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getType() {
        return type;
    }

    public int getFlags() {
        return flags;
    }

    public long getRepresentation() {
        return representation;
    }

    public int getFragLen() {
        return fragLen;
    }

    public int getAuthLen() {
        return authLen;
    }

    public long getCallId() {
        return callId;
    }

    public static class Builder {
        private Header header;
        public Builder(){
            header = new Header();
        }
        public Builder withMajor(int major){
            header.major = major;
            return this;
        }
        public Builder withMinor(int minor){
            header.minor = minor;
            return this;
        }
        public Builder withType(int type){
            header.type = type;
            return this;
        }
        public Builder withFlags(int flags){
            header.flags = flags;
            return this;
        }
        public Builder withRepresentation(long representation){
            header.representation = representation;
            return this;
        }
        public Builder withFragLen(int len){
            header.fragLen = len;
            return this;
        }
        public Builder withAuthLen(int authLen){
            header.authLen = authLen;
            return this;
        }
        public Builder withCallId(long id){
            header.callId = id;
            return this;
        }
        public Header build(){
            return header;
        }
    }
}
