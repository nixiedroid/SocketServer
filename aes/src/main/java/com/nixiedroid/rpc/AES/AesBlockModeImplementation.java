package com.nixiedroid.rpc.AES;

public class AesBlockModeImplementation extends AES {
    private final AESProcessor aes;
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
    public AesBlockModeImplementation(byte[] key){
        this(key,false);
    }
    public AesBlockModeImplementation(byte[] key, int rounds, boolean v6) {
        aes = new AESProcessor(key, rounds, v6);
    }
    public AesBlockModeImplementation(byte[] key, boolean v6){
        aes = new AESProcessor(key, v6);
    }
}
