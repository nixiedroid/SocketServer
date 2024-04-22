package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.AES.AES;
import com.nixiedroid.rpc.AES.AesBlockModeImplementation;
import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;

public class V4 {

    static byte[] key =  ByteArrayUtils.fromString(Context.config().getKey("KEYV4"));
    public static byte[] handle(byte[] data, GenericPayload header){
        Context.l().info("Received V4 request");
        Payload payload = decode(data,header.minor);
        PayloadAck ack = ResponseGenerator.generateResponse(payload);
        byte[] response = ack.serialize();
        PayloadV4 v4 = new PayloadV4(response,generateHash(response));
        return v4.serialize();
    }
    private static void xorBuffer(byte[] source, int offset, byte[] destination, int size){
        for (int i = 0; i < size; i++) {
            destination[i] ^= source[i+offset];
        }
    }
    private static byte[] generateHash(byte[] data){
        AES aes = new AesBlockModeImplementation(key,11,false);
        int dataLen = data.length;
        byte[] lastblock = new byte[16];
        byte[] hashbuffer = new byte[16];
        int j = dataLen>>4;
        int k = dataLen&0xf;
        for (int i = 0; i < j; i++) {
            xorBuffer(data,i<<4,hashbuffer,16);
            hashbuffer = aes.encrypt(hashbuffer);
        }
        int ii =0;
        for (int i = j<<4;   i < k + (j << 4); i++) {
            lastblock[ii] =data[i];
            ii++;
        }
        lastblock[k] = (byte) 0x80;
        xorBuffer(lastblock,0,hashbuffer,16);
        return aes.encrypt(hashbuffer);
    }
    private static Payload decode(byte[] data, int minor) {
        byte[] hash = new byte[16];
        byte[] request = new byte[data.length - GenericPayload.SIZE];
        System.arraycopy(ByteArrayUtils.toBytes(minor), 0, request, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(4), 0, request, 2, 2);
        System.arraycopy(data, 0, request, 4, request.length-4);
        System.arraycopy(data, data.length-16, hash, 0, hash.length);
        Payload payload = new Payload().deserialize(request,0);
        payload.iv = hash;
        return payload;
    }
}
