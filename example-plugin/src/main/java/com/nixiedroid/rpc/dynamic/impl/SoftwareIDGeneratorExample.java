package com.nixiedroid.rpc.dynamic.impl;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.dynamic.Key;
import com.nixiedroid.rpc.dynamic.stubs.SoftwareIDGenerator;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.StringType;
import com.nixiedroid.rpc.util.UUID;

public class SoftwareIDGeneratorExample extends SoftwareIDGenerator {

    @Override
    public byte[] getSoftwareID(UUID appId, int version) {
        if (Context.config().getKey(Key.SOFTWARE_ID) != null) {
            return ByteArrayUtils.fromString(Context.config().getKey(Key.SOFTWARE_ID), StringType.UTF16LE);
        }
        return ByteArrayUtils.fromString("55041-00206-200-000000-03-1049-17763.0000-1002020",StringType.UTF16LE);
    }
}
