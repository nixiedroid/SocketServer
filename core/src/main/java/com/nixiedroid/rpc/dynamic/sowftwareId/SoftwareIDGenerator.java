package com.nixiedroid.rpc.dynamic.sowftwareId;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.StringType;
import com.nixiedroid.rpc.util.UUID;

public class SoftwareIDGenerator extends SoftwareIDGeneratorStub {

    @Override
    public byte[] getSoftwareID(UUID appId, int version) {
        if (Context.settings().getSoftwareID() != null) return ByteArrayUtils.fromString(Context.settings().getSoftwareID(), StringType.UTF16LE);
        return ByteArrayUtils.fromString("55041-00206-200-000000-03-1049-17763.0000-1002020",StringType.UTF16LE);
    }
}
