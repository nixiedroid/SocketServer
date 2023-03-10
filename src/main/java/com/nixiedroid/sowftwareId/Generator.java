package com.nixiedroid.sowftwareId;

import com.nixiedroid.util.UUID;

public interface Generator {
    public byte[] getSoftwareID();
    public byte[] getSoftwareID(UUID appId, int version);
}
