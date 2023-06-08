package com.nixiedroid.sowftwareId;

import com.nixiedroid.util.UUID;

public abstract class SoftwareIDGeneratorStub {
    public abstract byte[] getSoftwareID(UUID appId, int version);
}
