package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.AES.AES;
import com.nixiedroid.rpc.AES.AesBlockModeImplementation;
import com.nixiedroid.rpc.AES.AesCBCImplementation;
import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class V5 {

    static byte[] key;

    public static byte[] handle(byte[] data, GenericPayload header, boolean v6) {
        String vStr = (v6) ? "6" : "5";
        key = (v6) ? V6.key : ByteArrayUtils.fromString(Context.config().getKey("KEYV5"));
        Context.l().info("Received V" + vStr + " request");
        Context.l().verbose("Bytes: " + ByteArrayUtils.toString(data));
        Payload payload = decode(data, v6);
        PayloadAck ack = ResponseGenerator.generateResponse(payload);
        PayloadV5 v5 = (v6) ? V6.encode(ack) : encode(ack);
        v5.minor = header.minor;
        v5.major = header.major;
        return v5.serialize();
    }

    static PayloadV5 encode(PayloadAck ack) {
        //Objects
        AES aes;
        MessageDigest digest;

        //Init arrays
        final byte[] messageBytes = ack.serialize();
        int ackLen = messageBytes.length;
        int padding = 16 - (ackLen + 48) % 16;

        byte[] randomSalt = new byte[16];
        byte[] randomSaltSHA;
        try {
            SecureRandom.getInstanceStrong().nextBytes(randomSalt);
        } catch (NoSuchAlgorithmException e) {
            new Random().nextBytes(randomSalt);
        } catch (NoSuchMethodError e) {
            new Random().nextBytes(randomSalt);
        }
        randomSalt = ByteArrayUtils.fromString("00000000000000000000000000000000");
        try {
            digest = MessageDigest.getInstance("SHA-256");
            randomSaltSHA = digest.digest(randomSalt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //decrypt Request IV using key
        aes = new AesBlockModeImplementation(key, false);
        byte[] requestIV = aes.decrypt(ack.iv);
        byte[] xoredSalt = ByteArrayUtils.xor(requestIV, randomSalt);


        //Generate response salt
        byte[] response = new byte[48 + ackLen + padding];
        System.arraycopy(messageBytes, 0, response, 0, ackLen);
        System.arraycopy(xoredSalt, 0, response, ackLen, 16);
        System.arraycopy(randomSaltSHA, 0, response, ackLen + 16, 32);

        //PKCS7-padding
        for (int i = 1; i <= padding; i++) {
            response[response.length - i] = (byte) (padding & 0xff);
        }

        //Encrypt response
        Context.l().verbose("To be encrypyed response bytes: " + ByteArrayUtils.toString(response));
        aes = new AesCBCImplementation(key, ack.iv, false);
        byte[] out = aes.encrypt(response);
        Context.l().verbose("Encrypyed response bytes: " + ByteArrayUtils.toString(out));
        return new PayloadV5(out, ack.iv, 0, 0);
    }

    static Payload decode(byte[] data, boolean v6) {
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[data.length - 16];
        System.arraycopy(data, 0, iv, 0, iv.length);
        System.arraycopy(data, 16, encrypted, 0, encrypted.length);
        AES aes = new AesCBCImplementation(key, iv, v6);
        data = aes.decrypt(encrypted);
        Payload payload = new Payload().deserialize(data, 0);
        payload.iv = iv;
        return payload;
    }

}
