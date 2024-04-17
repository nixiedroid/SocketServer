package com.nixiedroid.rpc.dynamic.sowftwareId;

import com.nixiedroid.rpc.Program;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.UUID;

public class SoftwareIDGenerator extends SoftwareIDGeneratorStub {

    @Override
    public byte[] getSoftwareID(UUID appId, int version) {
        if (Program.settings().getSoftwareID() != null) return ByteArrayUtils.UTF16LEtoBytes(Program.settings().getSoftwareID());
        return ByteArrayUtils.UTF16LEtoBytes("55041-00206-200-000000-03-1049-17763.0000-1002020");
    }
}