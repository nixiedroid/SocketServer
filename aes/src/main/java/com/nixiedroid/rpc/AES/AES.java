package com.nixiedroid.rpc.AES;

public abstract  class AES {
   public static final int BLOCKSIZE = 16;
   public abstract byte[] encrypt(byte[] data);
   public abstract byte[] decrypt(byte[] data);
   protected final AESProcessor aes;

   public AES(byte[] key){
      this(key,Mode.DEFAULT);
   }
   public AES(byte[] key, int rounds, Mode mode) {
      aes = new AESProcessor(key, rounds, mode);
   }
   public AES(byte[] key, Mode mode){
      aes = new AESProcessor(key, mode);
   }
}
