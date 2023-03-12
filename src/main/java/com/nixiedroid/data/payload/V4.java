package com.nixiedroid.data.payload;

import com.nixiedroid.AES.AES;
import com.nixiedroid.AES.AesBlockModeImplementation;
import com.nixiedroid.Program;
import com.nixiedroid.util.ByteArrayUtils;

public class V4 {

    static byte[] key =  ByteArrayUtils.fromHexString(Program.config().getKeyV4());
    public static byte[] handle(byte[] data, GenericPayload header){
        Program.log().info("Received V4 request");
        Payload payload = decode(data,header.minor);
        PayloadAck ack = ResponseGenerator.generateResponse(payload);
        byte[] response = ack.pack();
        PayloadV4 v4 = new PayloadV4(response,generateHash(response));
        return v4.pack();
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
        byte[] request = new byte[data.length - 12];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(minor), 0, request, 0, 2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(4), 0, request, 2, 2);
        System.arraycopy(data, 0, request, 4, request.length-4);
        System.arraycopy(data, data.length-16, hash, 0, hash.length);
        Payload payload = new Payload();
        payload.unpack(request);
        payload.iv = hash;
        return payload;
    }
}
