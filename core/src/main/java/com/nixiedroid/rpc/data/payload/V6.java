package com.nixiedroid.rpc.data.payload;

import com.nixiedroid.rpc.AES.AES;
import com.nixiedroid.rpc.AES.AesBlockModeImplementation;
import com.nixiedroid.rpc.AES.AesCBCImplementation;
import com.nixiedroid.rpc.Program;
import com.nixiedroid.rpc.util.ByteArrayUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class V6 {
    static byte[] key =  ByteArrayUtils.fromString(Program.config().getKeyV6());
    static PayloadV5 encode(PayloadAck ack){
        //Objects
        AES aes;
        MessageDigest digest;

        //Init arrays
        final byte[] messageBytes = ack.serialize();
        int ackLen = messageBytes.length;
        int padding = 16 - (ackLen+88)%16;
        byte[] hardwareId = ByteArrayUtils.fromString(Program.settings().getHardwareID());
        byte[] randomSalt = new byte[16];
        byte[] randomSalt2 = new byte[16];
        byte[] macSalt = new byte[16];
        byte[] randomSaltSHA;
        //SHA-265 of randomSalt
        try {
            if (hardwareId == null){
                hardwareId = new byte[8];
                SecureRandom.getInstanceStrong().nextBytes(hardwareId);
            }
            SecureRandom.getInstanceStrong().nextBytes(randomSalt);
            SecureRandom.getInstanceStrong().nextBytes(randomSalt2);
        } catch (NoSuchMethodError  e) {
            new Random().nextBytes(hardwareId);
            new Random().nextBytes(randomSalt2);
            new Random().nextBytes(randomSalt);
        } catch (NoSuchAlgorithmException e) {
            new Random().nextBytes(hardwareId);
            new Random().nextBytes(randomSalt2);
            new Random().nextBytes(randomSalt);
        }
        try {
            digest = MessageDigest.getInstance("SHA-256");
            randomSaltSHA = digest.digest(randomSalt);
            digest.update(getMackKey(ack.ackTime));
            System.arraycopy(digest.digest(),16,macSalt,0,macSalt.length);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //decrypt Request IV using key
        aes = new AesBlockModeImplementation(key, true);
        byte[] requestIV = aes.decrypt(ack.iv);
        byte[] randomStuff = ByteArrayUtils.xor(requestIV, randomSalt);

        //decrypt RandomSalt2 using key
        aes = new AesCBCImplementation(key, randomSalt2, true);
        byte[] decryptedSalt2 = aes.decrypt(randomSalt2);
        byte[] hMac = ByteArrayUtils.xor(randomSalt2, decryptedSalt2);


        //Generate response salt
        byte[] packed = new byte[88+ackLen];
        System.arraycopy(hMac,0,packed,0,16);
        System.arraycopy(messageBytes,0,packed,16,ackLen);
        System.arraycopy(randomStuff,0,packed,ackLen+16,16);
        System.arraycopy(randomSaltSHA,0,packed,ackLen+32,32);
        System.arraycopy(hardwareId,0,packed,ackLen+64,8);
        System.arraycopy(requestIV,0,packed,ackLen+72,16);
        byte[] responseSalt = getHmak(packed, macSalt);

        byte[] response = new byte[ackLen+88 + padding];
        System.arraycopy(packed,16,response,0,ackLen+72);
        System.arraycopy(responseSalt, 16, response, ackLen+72, 16);

        //PKCS7-padding
        for (int i = 1; i <= padding; i++) {
            response[response.length-i] = (byte) (padding & 0xff);
        }

        //Encrypt response
        aes = new AesCBCImplementation (key, randomSalt2, true);
        requestIV = aes.encrypt(response);
        return new PayloadV5(requestIV, randomSalt2, 0, 0);
    }
    private static byte[] getHmak( byte[] data, byte[] key) {
        String alg = "HmacSHA256";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, alg);
            Mac mac = Mac.getInstance(alg);
            mac.init(secretKeySpec);
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static byte[] getMackKey(long t)  {
        long c1 = 0x00000022816889BDL;
        long c2 = 0x000000208CBAB5EDL;
        long c3 = 0x3156CD5AC628477AL;

        long i1 = (t / c1);
        long i2 = (i1 * c2);
        long seed = (i2 + c3);
        return ByteArrayUtils.toBytes(seed);
    }
}
