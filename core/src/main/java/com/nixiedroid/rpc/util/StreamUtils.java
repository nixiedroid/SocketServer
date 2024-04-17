package com.nixiedroid.rpc.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;

public class StreamUtils {
    public static String toString(DataInputStream stream) throws IOException {
        return toString(stream, StringType.HEX);
    }
    public static String toString(final DataInputStream stream, StringType type) throws IOException {
        try {
            String out = "";
            stream.mark(-1);
            switch (type) {
                case ASCII:
                case UTF8:
                    out =  StreamUtils.Strings.utf8FromBytes(stream);
                case HEX:
                    out =   StreamUtils.Strings.hexFromBytes(stream);
                case UTF16BE:
                    out =   StreamUtils.Strings.utf16FromBytes(stream, Endiannes.BIG);
                case UTF16LE:
                    out =   StreamUtils.Strings.utf16FromBytes(stream, Endiannes.LITTLE);
            }
            stream.reset();
            return out;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static DataInputStream fromString(String str) {
        return fromString(str, StringType.HEX);
    }

    public static DataInputStream fromString(String str, StringType type) {
        switch (type) {
            case UTF8:
            case ASCII:
                return StreamUtils.Strings.utf8toBytes(str);
            case UTF16BE:
                return StreamUtils.Strings.utf16toBytes(str, Endiannes.BIG);
            case UTF16LE:
                return StreamUtils.Strings.utf16toBytes(str, Endiannes.LITTLE);
            case HEX:
                return StreamUtils.Strings.hexToBytes(str);
        }
        return null;
    }


    private static class Strings {
        private static DataInputStream utf16toBytes(final String str, Endiannes endiannes) {
            if (str == null) throw new IllegalArgumentException();
            final int length = str.length();
            final char[] buffer = new char[length];
            str.getChars(0, length, buffer, 0);
            final byte[] b = new byte[length * 2];
            for (int j = 0; j < length; j++) {
                switch (endiannes) {
                    case LITTLE:
                        b[j * 2] = (byte) (buffer[j] & 0xFF);
                        b[j * 2 + 1] = (byte) (buffer[j] >> 8);
                        break;
                    case BIG:
                        b[j * 2 + 1] = (byte) (buffer[j] & 0xFF);
                        b[j * 2] = (byte) (buffer[j] >> 8);
                        break;
                }
            }
            return b;
        }

        private static DataInputStream utf8toBytes(final String str) { //ASCII TABLE
            if (str == null) throw new IllegalArgumentException();
            final int length = str.length();
            final char[] buffer = new char[length];
            final byte[] b = new byte[length];
            str.getChars(0, length, buffer, 0);
            for (int j = 0; j < length; j++) {
                b[j] = (byte) (buffer[j] & 0xFF);
            }
            return b;
        }

        private static DataInputStream hexToBytes(final String encoded) {
            if (encoded == null || encoded.length() == 0) return new byte[0];
            Matcher matcher = hexString.matcher(encoded);
            if (!matcher.matches()) return new byte[0];
            int length = encoded.length();
            if ((length % 2) != 0) length--;
            if (length == 0) {
                try {
                    return new byte[]{(byte) Integer.parseInt(encoded, HEX)};
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            final byte[] result = new byte[length >> 1];
            final char[] chars = encoded.toCharArray();
            for (int i = 0; i < length; i += 2) {
                try {
                    result[i >> 1] = (byte) Integer.parseInt(String.valueOf(chars[i]) + chars[i + 1], HEX);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            return result;
        }

        private static String utf8FromBytes(DataInputStream bytes) throws IOException {
            return bytes.readUTF();
        }

        private static String utf16FromBytes(DataInputStream bytes, Endiannes endiannes) throws UnsupportedEncodingException {
//            switch (endiannes) {
//                case LITTLE:
//                    return new String(bytes, "UTF-16BE");
//                case BIG:
//                    return new String(bytes, "UTF-16LE");
            return null;
        }

        private static String hexFromBytes(final DataInputStream data) throws IOException {
            if (data == null) throw new IllegalArgumentException();
            StringBuilder sb = new StringBuilder();
            int b;
            while ((b = data.read()) !=-1) {
                sb.append(String.format("%02X", b & 0xFF));
            }
            return sb.toString();
        }
    }
}
