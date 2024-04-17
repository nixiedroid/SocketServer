package com.nixiedroid.rpc.dynamic.sowftwareId;

import com.nixiedroid.rpc.util.UUID;

public abstract class SoftwareIDGeneratorStub {
    public abstract byte[] getSoftwareID(UUID appId, int version);
}
