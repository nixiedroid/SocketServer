package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.UUID;

import java.nio.charset.StandardCharsets;


public class Payload implements BytePackable {

    short minor; //Little endian unsigned short (2bytes)
    short major; //Little endian unsigned short (2bytes)
    int  isClientVM;
    int status;
    int graceTime;
    UUID appUUID;
    UUID skuUUID;
    UUID counterUUID;
    UUID cmUUID;
    long requiredClientCount;
    long requestTime;
    UUID prevUUID;
    String machinename; //2x NUl terminaled string TODO fillMe

    byte[] iv; //should NOT be packed

    @Override
    public Payload deserialize(byte[] data,int start) {
        minor = ByteArrayUtils.toInt16(data, start+0, Endiannes.LITTLE);
        major = ByteArrayUtils.toInt16(data, start+2, Endiannes.LITTLE);
        isClientVM = ByteArrayUtils.toInt32(data,start+4, Endiannes.LITTLE);
        status = ByteArrayUtils.toInt32(data, start+8, Endiannes.LITTLE);
        graceTime = ByteArrayUtils.toInt32(data,start+12, Endiannes.LITTLE);
        appUUID = new UUID(data,start+16);
        skuUUID = new UUID(data,start+32);
        counterUUID = new UUID(data,start+48);
        cmUUID = new UUID(data,start+64);
        requiredClientCount = ByteArrayUtils.toInt32(data,start+80, Endiannes.LITTLE);
        requestTime = ByteArrayUtils.toInt64(data,start+84,Endiannes.LITTLE);
        prevUUID = new UUID(data,start+92);
        machinename = getClientName(data,start+108);
        return this;
    }
    public String getClientName(byte[] data,final int offset){
        int stringSize =0;
        if (offset>=data.length) return "";
        boolean isNullFound = false;
        for (int i = offset; i < data.length; i++) {
            if (data[i]==0) {
                if (isNullFound) {
                    stringSize = i-offset;
                    break;
                }
                isNullFound = true;
            } else isNullFound = false;
        }
        if(stringSize==0) return "";
        byte[] chunk = new byte[stringSize];
        System.arraycopy(data,offset,chunk,0,stringSize);
        return new String(chunk, StandardCharsets.UTF_16LE);
    }

    @Override
    public byte[] serialize() {
       return new byte[2];
    }

    @Override
    public int size() {
        return 44;
    }
}
