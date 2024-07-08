package com.nixiedroid.rpc.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ByteArrayUtils {

    private static final Pattern hexString = Pattern.compile("[0-9a-fA-F]+");
    
    public static byte[] toBytes(final byte b){
        byte[] out = new  byte[Byte.BYTES];
        toBytes(b,out);
        return out;
    }
    public static void toBytes(final byte b,final byte[] dest){
        toBytes(b,dest,0);
    }
    public static void toBytes(final byte b,final byte[] dest,final int start){
       dest[start] = b;
    }

    public static byte[] toBytes(final short s) {
        return toBytes(s, Endiannes.LITTLE);
    }
    public static byte[] toBytes(final short s, final Endiannes endiannes) {
        byte[] out = new byte[Short.BYTES];
        toBytes(s, out, 0, endiannes);
        return out;
    }
    public static void toBytes(final short s, final byte[] bytes, final Endiannes endiannes) {
        toBytes(s,  bytes, 0, endiannes);
    }
    public static void toBytes(final short s, final byte[] bytes, final int start, final Endiannes endiannes) {
        if (bytes == null || endiannes == null) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                 bytes[start] = (byte) (s & 0xFF);
                 bytes[start + 1] = (byte) ((s >> 8) & 0xFF);
                return;
            case BIG:
                 bytes[start] = (byte) ((s >> 8) & 0xFF);
                 bytes[start + 1] = (byte) ((s) & 0xFF);
        }
    }

    public static byte[] toBytes(final int i) {
        return toBytes(i, Endiannes.LITTLE);
    }
    public static byte[] toBytes(final int i, final Endiannes endiannes) {
        byte[] out = new byte[Integer.BYTES];
        toBytes(i, out, 0, endiannes);
        return out;
    }
    public static void toBytes(final int i, final byte[] bytes, final Endiannes endiannes) {
        toBytes(i,  bytes, 0, endiannes);
    }
    public static void toBytes(final int i, final byte[] bytes, final int start, final Endiannes endiannes) {
        if (bytes == null || endiannes == null) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                 bytes[start] = (byte) (i & 0xFF);
                 bytes[start + 1] = (byte) ((i >> 8) & 0xFF);
                 bytes[start + 2] = (byte) ((i >> 16) & 0xFF);
                 bytes[start + 3] = (byte) ((i >> 24) & 0xFF);
                return;
            case BIG:
                 bytes[start] = (byte) ((i >> 24) & 0xFF);
                 bytes[start + 1] = (byte) ((i >> 16) & 0xFF);
                 bytes[start + 2] = (byte) ((i >> 8) & 0xFF);
                 bytes[start + 3] = (byte) ((i) & 0xFF);
        }
    }

    public static byte[] toBytes(final long i) {
        return toBytes(i, Endiannes.LITTLE);
    }
    public static byte[] toBytes(final long i, final Endiannes endiannes) {
        byte[] out = new byte[Long.BYTES];
        toBytes(i, out, 0, endiannes);
        return out;
    }
    public static void toBytes(final long i, final byte[] bytes, final Endiannes endiannes) {
        toBytes(i,  bytes, 0, endiannes);
    }
    public static void toBytes(final long i, final byte[] bytes, final int start, final Endiannes endiannes) {
        if (bytes == null || endiannes == null) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                 bytes[start] = (byte) (i & 0xFF);
                 bytes[start + 1] = (byte) ((i >> 8) & 0xFF);
                 bytes[start + 2] = (byte) ((i >> 16) & 0xFF);
                 bytes[start + 3] = (byte) ((i >> 24) & 0xFF);
                 bytes[start + 4] = (byte) ((i >> 32) & 0xFF);
                 bytes[start + 5] = (byte) ((i >> 40) & 0xFF);
                 bytes[start + 6] = (byte) ((i >> 48) & 0xFF);
                 bytes[start + 7] = (byte) ((i >> 56) & 0xFF);
                return;
            case BIG:
                 bytes[start] = (byte) ((i >> 56) & 0xFF);
                 bytes[start + 1] = (byte) ((i >> 48) & 0xFF);
                 bytes[start + 2] = (byte) ((i >> 40) & 0xFF);
                 bytes[start + 3] = (byte) ((i >> 32) & 0xFF);
                 bytes[start + 4] = (byte) ((i >> 24) & 0xFF);
                 bytes[start + 5] = (byte) ((i >> 16) & 0xFF);
                 bytes[start + 6] = (byte) ((i >> 8) & 0xFF);
                 bytes[start + 7] = (byte) ((i) & 0xFF);
        }
    }
    
    public static byte toInt8(final byte[] bytes, final int start) {
        return (byte) ( bytes[start] & 0xff);
    }
    public static short toInt16(final byte[] bytes, final Endiannes endiannes) {
        return toInt16( bytes, 0, endiannes);
    }
    public static short toInt16(final byte[] bytes, final int start, final Endiannes endiannes) {
        if (bytes == null || endiannes == null) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                return (short) (( bytes[start] & 0xFF) | ( bytes[start + 1] & 0xFF) << 8);
            case BIG:
                return (short) (( bytes[start + 1] & 0xFF) | ( bytes[start] & 0xFF) << 8);
        }
        return 0;
    }

    public static int toInt32(final byte[] bytes, final Endiannes endiannes) {
        return toInt32( bytes, 0, endiannes);
    }
    public static int toInt32(final byte[] bytes, final int start, final Endiannes endiannes) {
        if (bytes == null || endiannes == null) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                return ( bytes[start] & 0xFF) | ( bytes[start + 1] & 0xFF) << 8 | ( bytes[start + 2] & 0xFF) << 16 | ( bytes[start + 3] & 0xFF) << 24;
            case BIG:
                return ( bytes[start + 3] & 0xFF) | ( bytes[start + 2] & 0xFF) << 8 | ( bytes[start + 1] & 0xFF) << 16 | ( bytes[start] & 0xFF) << 24;
        }
        return 0;
    }

    public static long toInt64(final byte[] bytes, final Endiannes endiannes) {
        return toInt64( bytes, 0, endiannes);
    }
    public static long toInt64(final byte[] bytes, final int start, final Endiannes endiannes) {
        if (bytes == null || endiannes == null) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                return ((long)  bytes[start + 7] & 0xff) << 56 | ((long)  bytes[start + 6] & 0xff) << 48 | ((long)  bytes[start + 5] & 0xff) << 40 | ((long)  bytes[start + 4] & 0xff) << 32 | ((long)  bytes[start + 3] & 0xff) << 24 | ((long)  bytes[start + 2] & 0xff) << 16 | ((long)  bytes[start + 1] & 0xff) << 8 | ((long)  bytes[start] & 0xff);
            case BIG:
                return ((long)  bytes[start] & 0xff) << 56 | ((long)  bytes[start + 1] & 0xff) << 48 | ((long)  bytes[start + 2] & 0xff) << 40 | ((long)  bytes[start + 3] & 0xff) << 32 | ((long)  bytes[start + 4] & 0xff) << 24 | ((long)  bytes[start + 5] & 0xff) << 16 | ((long)  bytes[start + 6] & 0xff) << 8 | ((long)  bytes[start + 7] & 0xff);
        }
        return 0;
    }
    
    public static byte[] xor(byte[] first, byte[] second) {
        if (first == null || second == null) throw new IllegalArgumentException("Null Array");
        int length = Math.min(first.length, second.length);
        byte[] out = new byte[length];
        for (int i = 0; i < length; i++)
            out[i] = (byte) (first[i] ^ second[i]);
        return out;
    }
    public static byte[] reverse(final byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException();
        byte[] out = new byte[bytes.length];
        System.arraycopy( bytes, 0, out, 0, out.length);
        int i = 0, j = out.length - 1;
        byte tmp;
        while (j > i) {
            tmp = out[j];
            out[j] = out[i];
            out[i] = tmp;
            j--;
            i++;
        }
        return out;
    }
    public static boolean isEquals(final byte[] first, final byte[] second) {
        return Arrays.equals(first, second);
    }
    
    public static String toString(final byte[] bytes) {
        return toString( bytes, StringType.HEX);
    }
    public static String toString(final byte[] bytes, StringType type) {
        try {
            switch (type) {
                case ASCII:
                case UTF8:
                    return Strings.utf8FromBytes(bytes);
                case HEX:
                    return Strings.hexFromBytes(bytes);
                case UTF16BE:
                    return Strings.utf16FromBytes( bytes, Endiannes.BIG);
                case UTF16LE:
                    return Strings.utf16FromBytes( bytes, Endiannes.LITTLE);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    
    public static byte[] fromString(final String str) {
        return fromString(str, StringType.HEX);
    }
    public static byte[] fromString(final String str, StringType type) {
        switch (type) {
            case UTF8:
            case ASCII:
                return Strings.utf8toBytes(str);
            case UTF16BE:
                return Strings.utf16toBytes(str, Endiannes.BIG);
            case UTF16LE:
                return Strings.utf16toBytes(str, Endiannes.LITTLE);
            case HEX:
                return Strings.hexToBytes(str);
        }
        return null;
    }
    
    private static class Strings {
        private static byte[] utf16toBytes(final String str, final Endiannes endiannes) {
            if (str == null) throw new IllegalArgumentException();
            final int length = str.length();
            final char[] buffer = new char[length];
            str.getChars(0, length, buffer, 0);
            final byte[] bytes = new byte[length * 2];
            for (int j = 0; j < length; j++) {
                switch (endiannes) {
                    case LITTLE:
                         bytes[j * 2] = (byte) (buffer[j] & 0xFF);
                         bytes[j * 2 + 1] = (byte) (buffer[j] >> 8);
                        break;
                    case BIG:
                         bytes[j * 2 + 1] = (byte) (buffer[j] & 0xFF);
                         bytes[j * 2] = (byte) (buffer[j] >> 8);
                        break;
                }
            }
            return bytes;
        }

        private static byte[] utf8toBytes(final String str) { //ASCII TABLE
            if (str == null) throw new IllegalArgumentException();
            final int length = str.length();
            final char[] buffer = new char[length];
            final byte[] bytes = new byte[length];
            str.getChars(0, length, buffer, 0);
            for (int j = 0; j < length; j++) {
                 bytes[j] = (byte) (buffer[j] & 0xFF);
            }
            return bytes;
        }

        private static byte[] hexToBytes(final String encoded) {
            if (encoded == null || encoded.length() == 0) return new byte[0];
            Matcher matcher = hexString.matcher(encoded);
            if (!matcher.matches()) return new byte[0];
            int length = encoded.length();
            if ((length % 2) != 0) length--;
            if (length == 0) {
                try {
                    return new byte[]{(byte) Integer.parseInt(encoded, 16)};
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            final byte[] bytes = new byte[length >> 1];
            final char[] chars = encoded.toCharArray();
            for (int i = 0; i < length; i += 2) {
                try {
                    bytes[i >> 1] = (byte) Integer.parseInt(String.valueOf(chars[i]) + chars[i + 1], 16);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            return bytes;
        }

        private static String utf8FromBytes(final byte[] bytes) throws UnsupportedEncodingException { //ASCII TABLE
            return new String( bytes, StandardCharsets.UTF_8);
        }

        private static String utf16FromBytes(final byte[] bytes, final Endiannes endiannes) throws UnsupportedEncodingException {
            switch (endiannes) {
                case LITTLE:
                    return new String( bytes, StandardCharsets.UTF_16BE);
                case BIG:
                    return new String( bytes, StandardCharsets.UTF_16LE);
            }
            return null;
        }

        private static String hexFromBytes(final byte[] bytes) {
            if (bytes == null) throw new IllegalArgumentException();
            StringBuilder sb = new StringBuilder();
            for (final byte b : bytes) {
                sb.append(String.format("%02X", b & 0xFF));
            }
            return sb.toString();
        }
    }
}
