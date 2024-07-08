package com.nixiedroid.rpc.AES;

public class AesECBImplementation extends AES {

    public AesECBImplementation(byte[] key) {
        super(key);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        if (data.length%16 != 0) throw new IllegalArgumentException("Data len must be divisible by 16");
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i+=AES.BLOCKSIZE) {
            out = aes.encrypt(data);
        }
        return out;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        if (data.length%16 != 0) throw new IllegalArgumentException("PayloadBind len must be divisible by 16");
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i+=AES.BLOCKSIZE) {
            out = aes.decrypt(data);
        }
        return out;
    }


}
