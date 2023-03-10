package com.nixiedroid.sowftwareId;

import com.nixiedroid.util.UUID;

public class GeneratorStub implements Generator{
    private final Generator generator;
    @Override
    public byte[] getSoftwareID() {
        return generator.getSoftwareID();
    }

    @Override
    public byte[] getSoftwareID(UUID appId, int version) {
        return generator.getSoftwareID(appId,version);
    }

    public GeneratorStub(Generator generator) {
        this.generator = generator;
    }
}
