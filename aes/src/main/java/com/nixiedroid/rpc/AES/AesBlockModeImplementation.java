package com.nixiedroid.rpc.AES;

public class AesBlockModeImplementation extends AES {

    public AesBlockModeImplementation(byte[] key, int rounds, Mode mode) {
        super(key, rounds, mode);
    }

    public AesBlockModeImplementation(byte[] key, Mode mode) {
        super(key, mode);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        if (data.length != BLOCKSIZE) throw new IllegalArgumentException("Data MUST be "+ BLOCKSIZE + " bytes long");
        return aes.encrypt(data);
    }
    @Override
    public byte[] decrypt(byte[] data) {
        if (data.length != BLOCKSIZE) throw new IllegalArgumentException("Data MUST be " + BLOCKSIZE + " bytes long");
        return aes.decrypt(data);
    }

}
