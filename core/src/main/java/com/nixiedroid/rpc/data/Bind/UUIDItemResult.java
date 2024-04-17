package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.UUID;



public class UUIDItemResult implements BytePackable {
    public static final int SIZE = 24;
    int result;
    int reason;
    UUID transferUUID;
    long transferVer;
    public UUIDItemResult(int result, int reason, UUID uuid, int transferVer) {
        this.result = result;
        this.reason = reason;
        this.transferUUID = uuid;
        this.transferVer = transferVer;
    }
    @Override
    public UUIDItemResult deserialize(byte[] data,int start) {
        result = ByteArrayUtils.ranged.toUInt16L(data,0);
        reason = ByteArrayUtils.ranged.toUInt16L(data,2);
        System.arraycopy(transferUUID.uuid,0,data,4,16);
        transferVer = ByteArrayUtils.ranged.toUInt32L(data, 20);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] out = new byte[SIZE];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(result),0,out,0,2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(reason),0,out,2,2);
        System.arraycopy(transferUUID.uuid,0,out,4,16);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(transferVer), 0, out, 20, 4);
        return out;
    }

    @Override
    public int size() {
        return SIZE;
    }
}
