package com.nixiedroid.rpc.AES;

public class AesCBCImplementation extends AES {
    private final AESProcessor aes;
    private final byte[] iv;
    @Override
    public byte[] encrypt(byte[] data) {
        byte[] out = new byte[data.length];
        byte[] chunk = copyOf(iv, iv.length);
        byte[] chunk2 = copyOf(iv,iv.length);
        for (int i = 0; i < data.length; i+=AES.BLOCKSIZE) {
            System.arraycopy(data,i,chunk2,0,chunk2.length);
            chunk = aes.encrypt(xor(chunk,chunk2));
            System.arraycopy(chunk,0,out,i,AES.BLOCKSIZE);
        }
        return out;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte[] out = new byte[data.length];
        byte[] chunk = copyOf(iv,iv.length);
        byte[] chunkIn = copyOf(iv,iv.length);
        for (int i = 0; i < data.length; i+=AES.BLOCKSIZE) {
            System.arraycopy(data,i,chunkIn,0,16);
            xor(chunk, aes.decrypt(chunkIn));
            System.arraycopy(chunk,0,out,i,AES.BLOCKSIZE);
            System.arraycopy(chunkIn,0,chunk,0,AES.BLOCKSIZE);
        }
        return out;
    }
    public AesCBCImplementation(byte[] key, byte[] iv){
        this.iv = iv;
        aes = new AESProcessor(key, false);
    }
    public AesCBCImplementation(byte[] key, int rounds,byte[] iv, boolean v6) {
        this.iv = iv;
        aes = new AESProcessor(key, rounds, v6);
    }
    public AesCBCImplementation(byte[] key,byte[] iv, boolean v6){
        this.iv = iv;
        aes = new AESProcessor(key, v6);
    }
    private static byte[] xor(byte[] first, byte[] second){
        if (first.length != second.length) throw new IllegalArgumentException("Arrays must have equal length");
        for (int i = 0; i< first.length;i++)
            first[i] = (byte) (first[i] ^ second[i]);
        return first;
    }
    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }
}
