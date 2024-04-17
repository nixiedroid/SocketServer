package com.nixiedroid.rpc.util;

import com.nixiedroid.rpc.data.BytePackable;

import java.util.Arrays;

public class UUID implements BytePackable {

    public byte[] uuid;

    public UUID(byte[] uuid) {
        this.uuid = uuid;
    }

    public static UUID cnv(String str){
        return new UUID(convert(ByteArrayUtils.fromHexString(str)));
    }

    private static byte[] convert(byte[] uuid) {
        byte[] arr2 = Arrays.copyOf(uuid, uuid.length);
        uuid[0] = arr2[3];
        uuid[1] = arr2[2];
        uuid[2] = arr2[1];
        uuid[3] = arr2[0];
        uuid[4] = arr2[5];
        uuid[5] = arr2[4];
        uuid[6] = arr2[7];
        uuid[7] = arr2[6];
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUID uuid1 = (UUID) o;
        return Arrays.equals(uuid, uuid1.uuid);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(uuid);
    }

    @Override
    public String toString() {
        return "UUID{" + "uuid=" + ByteArrayUtils.toString(uuid) + '}';
    }

    @Override
    public UUID deserialize(byte[] data, int start) {
        this.uuid = data;
        return this;
    }

    @Override
    public byte[] serialize() {
        return uuid;
    }

    @Override
    public int size() {
        return 16;
    }
}
