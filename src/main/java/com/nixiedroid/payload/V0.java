package com.nixiedroid.payload;


public class V0 {
    public static byte[] handle(byte[] data){ //IS IT BROKEN ?
        byte[] reHandle = new byte[data.length-8];
        System.arraycopy(data, 0, reHandle, 0, 4);
        System.arraycopy(data, 8, reHandle, 4, 4);
        System.arraycopy(data, 16, reHandle, 8, data.length-16);

       return PayloadHandler.process(reHandle);
    }
}
