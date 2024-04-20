package com.nixiedroid.rpc.data;

import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.data.enums.PduTypeHolder.PduType;
import com.nixiedroid.rpc.data.enums.PduTypeHolder;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;


public class Header  implements   BytePackable<Header> {
    public static final int SIZE = 16;
    int major; //Byte
    int minor; //Byte
    PduType type; //Byte
    PacketFlagsHolder flags; //Byte
    int representation; //Little endian unsigned long (4bytes)
    short fragLen; //little endian unsigned short (2bytes) TOTAL MESSAGE SIZE in bytes, INCLUDING HEADER
    short authLen; //little endian unsigned short
    int callId; //Little endian unsigned long (4bytes)

    public Header() {}

    public Header(byte[] data)  {
        deserialize(data, 0);
    }

    public Header deserialize(byte[] data, int start)  {
        major = data[0];
        minor = data[1];
        this.type = PduTypeHolder.get(data[2]);
        flags = new PacketFlagsHolder().deserialize(data,3);
        representation = ByteArrayUtils.toInt32(data, 4, Endiannes.LITTLE);
        fragLen = ByteArrayUtils.toInt16(data, 8, Endiannes.LITTLE);
        authLen = ByteArrayUtils.toInt16(data, 10, Endiannes.LITTLE);
        callId = ByteArrayUtils.toInt32(data, 12, Endiannes.LITTLE);
        return this;
    }

    public byte[] serialize() {
        byte[] packed = new byte[16];
        packed[0] = (byte) major;
        packed[1] = (byte) minor;
        packed[2] = (byte) PduTypeHolder.get(type);
        packed[3] = flags.serialize()[0];
        System.arraycopy(ByteArrayUtils.toBytes(representation), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.toBytes(fragLen), 0, packed, 8, 2);
        System.arraycopy(ByteArrayUtils.toBytes(authLen), 0, packed, 10, 2);
        System.arraycopy(ByteArrayUtils.toBytes(callId), 0, packed, 12, 4);
        return packed;
    }

    public int size() {
        return SIZE;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public PduType getType() {
        return type;
    }

    public PacketFlagsHolder getFlags() {
        return flags;
    }

    public int getRepresentation() {
        return representation;
    }

    public short getFragLen() {
        return fragLen;
    }

    public short getAuthLen() {
        return authLen;
    }

    public int getCallId() {
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
        public Builder withType(PduTypeHolder.PduType type){
            header.type = type;
            return this;
        }
        public Builder withFlags(PacketFlagsHolder flags){
            header.flags = flags;
            return this;
        }
        public Builder withRepresentation(int representation){
            header.representation = representation;
            return this;
        }
        public Builder withFragLen(int len){
            header.fragLen = (short) len;
            return this;
        }
        public Builder withAuthLen(int authLen){
            header.authLen = (short) authLen;
            return this;
        }
        public Builder withCallId(int id){
            header.callId = id;
            return this;
        }
        public Header build(){
            return header;
        }
    }
}
