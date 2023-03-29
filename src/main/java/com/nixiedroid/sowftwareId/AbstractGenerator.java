package com.nixiedroid.sowftwareId;

import com.nixiedroid.util.UUID;

public interface AbstractGenerator {
    public byte[] getSoftwareID(UUID appId, int version);
}
