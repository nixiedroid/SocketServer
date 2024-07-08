package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.AES.AES;
import com.nixiedroid.rpc.AES.AesBlockModeImplementation;
import com.nixiedroid.rpc.AES.AesCBCImplementation;
import com.nixiedroid.rpc.AES.Mode;
import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.dynamic.Key;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class V5 {

    static byte[] key;

    public static byte[] handle(byte[] data, GenericPayload header, Mode mode) {
        String vStr;
        if (Objects.requireNonNull(mode) == Mode.EXTRA) {
            vStr = "6";
            key = V6.key;
        } else {
            key = ByteArrayUtils.fromString(Context.config().getKey(Key.KEYV5));
            vStr = "5";
        }
        Logger.info("Received V" + vStr + " request");
        Logger.trace("Bytes: " + ByteArrayUtils.toString(data));
        Payload payload = decode(data, mode);
        PayloadAck ack = ResponseGenerator.generateResponse(payload);
        PayloadV5 v5;
        if (mode == Mode.EXTRA) {
            v5 = V6.encode(ack);
        } else {
            v5 = encode(ack);
        }
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
        } catch (NoSuchAlgorithmException | NoSuchMethodError e) {
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
        aes = new AesBlockModeImplementation(key, Mode.DEFAULT);
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
        Logger.trace("To be encrypyed response bytes: " + ByteArrayUtils.toString(response));
        aes = new AesCBCImplementation(key, ack.iv, Mode.DEFAULT);
        byte[] out = aes.encrypt(response);
        Logger.trace("Encrypyed response bytes: " + ByteArrayUtils.toString(out));
        return new PayloadV5(out, ack.iv, 0, 0);
    }

    static Payload decode(byte[] data, Mode mode) {
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[data.length - 16];
        System.arraycopy(data, 0, iv, 0, iv.length);
        System.arraycopy(data, 16, encrypted, 0, encrypted.length);
        AES aes = new AesCBCImplementation(key, iv, mode);
        data = aes.decrypt(encrypted);
        Payload payload = new Payload().deserialize(data, 0);
        payload.iv = iv;
        return payload;
    }

}
