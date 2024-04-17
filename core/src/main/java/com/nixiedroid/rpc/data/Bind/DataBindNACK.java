package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.util.ByteArrayUtils;

public class DataBindNACK extends Data {
    int rejectReason; //UInt 16 L
    byte[] supportedVersions = new byte[0];

    @Override
    public DataBindNACK deserialize(byte[] data,int size) {
        rejectReason = ByteArrayUtils.ranged.toUInt16L(data,0);
        if (data.length>2) {
            supportedVersions = new byte[data.length-2];
            System.arraycopy(data,2,supportedVersions,0,data.length-2);
        }
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] out = new byte[2+supportedVersions.length];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(rejectReason),0,out,0,2);
        System.arraycopy(supportedVersions,0,out,2,supportedVersions.length);
        return out;
    }

    @Override
    public int size() {
        if(supportedVersions!=null) return 2+supportedVersions.length;
        else return 2;
    }
}
