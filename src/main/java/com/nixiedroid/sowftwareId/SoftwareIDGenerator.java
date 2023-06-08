package com.nixiedroid.sowftwareId;

import com.nixiedroid.Program;
import com.nixiedroid.util.ByteArrayUtils;
import com.nixiedroid.util.UUID;

public class SoftwareIDGenerator extends SoftwareIDGeneratorStub {

    @Override
    public byte[] getSoftwareID(UUID appId, int version) {
        if (Program.settings().getSoftwareID() != null) return ByteArrayUtils.UTF16LEtoBytes(Program.settings().getSoftwareID());
        return ByteArrayUtils.UTF16LEtoBytes("55041-00206-200-000000-03-1049-17763.0000-1002020");
    }
}
