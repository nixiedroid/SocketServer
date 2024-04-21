
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.StringType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ByteArrayUtilsTest {

    @Test
    void equalsTest() {
        Assertions.assertTrue(null == null);
//        Assertions.assertTrue(ByteArrayUtils.isEquals(null, null));
//        Assertions.assertFalse(ByteArrayUtils.isEquals(null, new byte[0]));
//        Assertions.assertFalse(ByteArrayUtils.isEquals(new byte[0], null));
//        Assertions.assertTrue(ByteArrayUtils.isEquals(new byte[0], new byte[0]));
//        Assertions.assertTrue(ByteArrayUtils.isEquals(new byte[]{0, 1, 2}, new byte[]{0, 1, 2}));
//        Assertions.assertFalse(ByteArrayUtils.isEquals(new byte[]{0, 1, 2}, new byte[]{0, 2, 2}));
    }

    @Test
    void fromStringTest() {
        String hexString = "ABCD15364216";
        String hexStringPartiallyValid = "ABCD153642160";
        byte[] expected = new byte[]{(byte) 0xAB, (byte) 0xCD, 0x15, 0x36, 0x42, 0x16};
        byte[] EMPTY = new byte[0];

        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromString(null));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromString(""));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromString("          "));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromString("HELLO THIS IS WRONG STRING"));
        Assertions.assertArrayEquals(expected, ByteArrayUtils.fromString(hexString));
        Assertions.assertArrayEquals(expected, ByteArrayUtils.fromString(hexStringPartiallyValid));
        Assertions.assertArrayEquals(new byte[]{10}, ByteArrayUtils.fromString("A"));
    }

    @Test
    void xorTest() {
        Assertions.assertArrayEquals(
                ByteArrayUtils.fromString("AAAA"),
                ByteArrayUtils.xor(
                        ByteArrayUtils.fromString("AA00"),
                        ByteArrayUtils.fromString("00AA")
                )
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteArrayUtils.xor(null, null));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.xor(new byte[0],new byte[0]));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.xor(new byte[0],new byte[1]));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.xor(new byte[1],new byte[0]));
        Assertions.assertArrayEquals(
                ByteArrayUtils.fromString("AAAA"),
                ByteArrayUtils.xor(
                        ByteArrayUtils.fromString("AA00CC"),
                        ByteArrayUtils.fromString("00AA")
                )
        );
        Assertions.assertArrayEquals(
                ByteArrayUtils.fromString("AAAA"),
                ByteArrayUtils.xor(
                        ByteArrayUtils.fromString("AA00"),
                        ByteArrayUtils.fromString("00AACC")
                )
        );
    }

    @Test
    void utf16toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtils.fromString(null,StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("31003100"), ByteArrayUtils.fromString("11",StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("00000000"), ByteArrayUtils.fromString("\0\0",StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("3A003A00"), ByteArrayUtils.fromString("::",StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString(
                "480065006C006C006F00200077006F0072006C0064002100"
        ), ByteArrayUtils.fromString("Hello world!", StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("00310031"), ByteArrayUtils.fromString("11",StringType.UTF16BE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("00000000"), ByteArrayUtils.fromString("\0\0",StringType.UTF16BE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("003A003A"), ByteArrayUtils.fromString("::",StringType.UTF16BE));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString(
                "00480065006C006C006F00200077006F0072006C00640021"
        ), ByteArrayUtils.fromString("Hello world!",StringType.UTF16BE));

    }

    @Test
    void utf8toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtils.fromString(null,StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("3131"), ByteArrayUtils.fromString("11",StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("0000"), ByteArrayUtils.fromString("\0\0",StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("3A3A"), ByteArrayUtils.fromString("::",StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("48656C6C6F20776F726C6421"), ByteArrayUtils.fromString("Hello world!",StringType.UTF8));
    }

    @Test
    void reverseTest() {
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("223344"), ByteArrayUtils.reverse(ByteArrayUtils.fromString("443322")));
        byte[] expected = ByteArrayUtils.fromString("EFCDAB9078563412");
        byte[] tested = ByteArrayUtils.fromString("1234567890ABCDEF");
        Assertions.assertArrayEquals(expected, ByteArrayUtils.reverse(tested));
        Assertions.assertArrayEquals(ByteArrayUtils.fromString("00AA"), ByteArrayUtils.reverse(ByteArrayUtils.fromString("AA00")));
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtils.reverse(null));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.reverse(new byte[0]));
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(new byte[]{i}, ByteArrayUtils.reverse(new byte[]{i}));
        }
    }

    @Test
    void print() {
    }

    @Test
    void testToString() {
        Assertions.assertEquals("AABBAA",ByteArrayUtils.toString(ByteArrayUtils.fromString("AABBAA")));
        Assertions.assertEquals("",ByteArrayUtils.toString(ByteArrayUtils.fromString("")));
        Assertions.assertEquals("",ByteArrayUtils.toString(new byte[0]));
        Assertions.assertEquals("01",ByteArrayUtils.toString(new byte[]{1}));
        Assertions.assertEquals("0B",ByteArrayUtils.toString(new byte[]{11}));
        Assertions.assertThrows(IllegalArgumentException.class,()->ByteArrayUtils.toString(null));
    }

    @Test
    void toBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(
                    ByteArrayUtils.fromString(Integer.toHexString(i & 0xFF)),
                    ByteArrayUtils.toBytes((byte)i)
            );
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            byte[] expected = fromStringShortPadded(i);
            byte[] test = ByteArrayUtils.toBytes(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected), test
            );
            test = ByteArrayUtils.toBytes(i, Endiannes.LITTLE);
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected), test
            );
            test = ByteArrayUtils.toBytes(i, Endiannes.BIG);
            Assertions.assertArrayEquals(
                    expected, test
            );
        }
        final  int STEP_I = 1 << (Integer.SIZE-6);
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE-STEP_I)+1; i += STEP_I ) {
            byte[] expected = fromStringIntPadded(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected),
                    ByteArrayUtils.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected),
                    ByteArrayUtils.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    expected,
                    ByteArrayUtils.toBytes(i, Endiannes.BIG)
            );
        }
        final long STEP_L = 1L << (Long.SIZE-6);
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE-STEP_L)+1; i += STEP_L) {
            byte[] expected = fromStringLongPadded(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected),
                    ByteArrayUtils.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected),
                    ByteArrayUtils.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    expected,
                    ByteArrayUtils.toBytes(i, Endiannes.BIG)
            );
        }
    }

    private byte[] fromStringShortPadded(short i) {
        return ByteArrayUtils.fromString(
                String.format("%1$" + Integer.SIZE/Byte.SIZE + "s",
                        Integer.toHexString(i & 0xFFFF)).replace(' ', '0')
        );
    }

    private byte[] fromStringIntPadded(int i) {
        return ByteArrayUtils.fromString(
                String.format("%1$" + Integer.BYTES * 2 + "s",
                        Integer.toHexString(i)).replace(' ', '0')
        );
    }

    private byte[] fromStringLongPadded(long i) {
        return ByteArrayUtils.fromString(
                String.format("%1$" + Long.BYTES * 2 + "s",
                        Long.toHexString(i)).replace(' ', '0')
        );
    }

    @Test
    void fromBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertEquals(i, ByteArrayUtils.toInt8(ByteArrayUtils.toBytes(i),0));
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            Assertions.assertEquals(i,ByteArrayUtils.toInt16(ByteArrayUtils.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i,ByteArrayUtils.toInt16(ByteArrayUtils.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
        final  int STEP_I = 1 << (Integer.SIZE-6);
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE-STEP_I)+1; i += STEP_I ) {
            Assertions.assertEquals(i,ByteArrayUtils.toInt32(ByteArrayUtils.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i,ByteArrayUtils.toInt32(ByteArrayUtils.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
        final long STEP_L = 1L << (Long.SIZE-6);
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE-STEP_L)+1; i += STEP_L) {
            Assertions.assertEquals(i,ByteArrayUtils.toInt64(ByteArrayUtils.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i,ByteArrayUtils.toInt64(ByteArrayUtils.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
    }
}