package com.nixiedroid.sowftwareId;

import com.nixiedroid.util.UUID;

public class GeneratorStub implements AbstractGenerator{
    private final AbstractGenerator generator;

    @Override
    public byte[] getSoftwareID(UUID appId, int version) {
        return generator.getSoftwareID(appId,version);
    }

    public GeneratorStub(AbstractGenerator generator) {
        this.generator = generator;
    }
}
