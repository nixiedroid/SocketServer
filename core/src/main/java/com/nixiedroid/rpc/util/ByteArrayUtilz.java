package com.nixiedroid.rpc.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")

public final class ByteArrayUtilz {
    private static final ByteArrayUtilz INSTANCE = new ByteArrayUtilz();
    private static final int HEX = 16;
    private static final Pattern hexString = Pattern.compile("[0-9a-fA-F]+");

    private ByteArrayUtilz() {
    }

    public static ByteArrayUtilz i() {
        return INSTANCE;
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
                    return Strings.utf16FromBytes(bytes, Endiannes.BIG);
                case UTF16LE:
                    return Strings.utf16FromBytes(bytes, Endiannes.LITTLE);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static byte[] xor(final byte[] first, final byte[] second) {
        if (first == null || second == null) throw new IllegalArgumentException("Null Array");
        int length = Math.min(first.length, second.length);
        byte[] out = new byte[length];
        for (int i = 0; i < length; i++)
            out[i] = (byte) (first[i] ^ second[i]);
        return out;
    }

    public static String toString(byte[] bytes) {
        return toString(bytes, StringType.HEX);
    }

    public static byte[] fromString(String str) {
        return fromString(str, StringType.HEX);
    }

    public static byte[] fromString(String str, StringType type) {
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

    public static byte[] reverse(final byte[] input) {
        if (input == null) throw new IllegalArgumentException();
        byte[] out = new byte[input.length];
        System.arraycopy(input, 0, out, 0, out.length);
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

    public <T extends Number> T fromBytes(final byte[] bytes) {
        return fromBytes(bytes, Endiannes.LITTLE);
    }

    public <T extends Number> byte[] toBytes(final T object) {
        return toBytes(object, Endiannes.LITTLE);
    }

    @SuppressWarnings("unchecked")
    public <T extends Number> T fromBytes(final byte[] bytes, final Endiannes endiannes) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        int count = bytes.length - 1;
        long out = 0;
        if (count >= Long.SIZE / Byte.SIZE) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                for (int i = 0; i <= count; i++) {
                    out |= ((long) bytes[i] & 0xFF) << (i << 3);
                }
                break;
            case BIG:
                for (int i = 0; i <= count; i++) {
                    out |= ((long) bytes[i] & 0xFF) << (count - i << 3);
                }
                break;
        }
        switch (bytes.length) {
            case 1:
                return (T) Byte.valueOf((byte) out);
            case 2:
                return (T) Short.valueOf((short) out);
            case 4:
                return (T) Integer.valueOf((int) out);
            default:
                return (T) Long.valueOf(out);
        }
    }

    public <T extends Number> byte[] toBytes(final T object, final Endiannes endiannes) {
        if (object == null) throw new IllegalArgumentException("Object is null");
        int count;
        if (object instanceof Integer) {
            count = Integer.SIZE / Byte.SIZE - 1;
        } else if (object instanceof Byte) {
            count = Byte.SIZE - 1;
        } else if (object instanceof Short) {
            count = Short.SIZE / Byte.SIZE - 1;
        } else if (object instanceof Long) {
            count = Long.SIZE / Byte.SIZE - 1;
        } else throw new IllegalArgumentException();
        byte[] out = new byte[count + 1];
        switch (endiannes) {
            case BIG:
                for (int i = 0; i <= count; i++) {
                    out[i] = (byte) ((object.longValue() >> ((count - i) << 3)) & 0xFF);
                }
                break;
            case LITTLE:
                for (int i = 0; i <= count; i++) {
                    out[i] = (byte) ((object.longValue() >> (i << 3)) & 0xFF);
                }
                break;
        }
        return out;
    }


    private static class Strings {
        private static byte[] utf16toBytes(final String str, Endiannes endiannes) {
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

        private static byte[] utf8toBytes(final String str) { //ASCII TABLE
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

        private static byte[] hexToBytes(final String encoded) {
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

        private static String utf8FromBytes(byte[] bytes) throws UnsupportedEncodingException { //ASCII TABLE
            return new String(bytes, "UTF-8");
        }

        private static String utf16FromBytes(byte[] bytes, Endiannes endiannes) throws UnsupportedEncodingException {
            switch (endiannes) {
                case LITTLE:
                    return new String(bytes, "UTF-16BE");
                case BIG:
                    return new String(bytes, "UTF-16LE");
            } return null;
        }

        private static String hexFromBytes(final byte[] data) {
            if (data == null) throw new IllegalArgumentException();
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {
                sb.append(String.format("%02X", b & 0xFF));
            }
            return sb.toString();
        }
    }

}
