package com.nixiedroid.rpc.dynamic.stubs;

import com.nixiedroid.rpc.util.UUID;

public abstract class SoftwareIDGenerator {
    public abstract byte[] getSoftwareID(UUID appId, int version);
}
