package com.nixiedroid.rpc.AES;

public abstract  class AES {
   public static final int BLOCKSIZE = 16;
   public abstract byte[] encrypt(byte[] data);
   public abstract byte[] decrypt(byte[] data);
}
