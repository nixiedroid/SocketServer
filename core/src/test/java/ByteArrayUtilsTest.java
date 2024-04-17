
import com.nixiedroid.rpc.util.ByteArrayUtilz;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.StringType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;


class ByteArrayUtilzTest {
    static ByteArrayUtilz inst;

    @BeforeAll
    static void init() {
        inst = ByteArrayUtilz.i();
    }

    @Test
    void instanceTest() {
        Assertions.assertNotNull(ByteArrayUtilz.i());
        Assertions.assertSame(ByteArrayUtilz.i(), inst);
        Assertions.assertSame(ByteArrayUtilz.i(),ByteArrayUtilz.i());
    }

    @Test
    void equalsTest() {
        Assertions.assertTrue(null == null);
//        Assertions.assertTrue(ByteArrayUtilz.isEquals(null, null));
//        Assertions.assertFalse(ByteArrayUtilz.isEquals(null, new byte[0]));
//        Assertions.assertFalse(ByteArrayUtilz.isEquals(new byte[0], null));
//        Assertions.assertTrue(ByteArrayUtilz.isEquals(new byte[0], new byte[0]));
//        Assertions.assertTrue(ByteArrayUtilz.isEquals(new byte[]{0, 1, 2}, new byte[]{0, 1, 2}));
//        Assertions.assertFalse(ByteArrayUtilz.isEquals(new byte[]{0, 1, 2}, new byte[]{0, 2, 2}));
    }

    @Test
    void fromStringTest() {
        String hexString = "ABCD15364216";
        String hexStringPartiallyValid = "ABCD153642160";
        byte[] expected = new byte[]{(byte) 0xAB, (byte) 0xCD, 0x15, 0x36, 0x42, 0x16};
        byte[] EMPTY = new byte[0];

        Assertions.assertArrayEquals(EMPTY, ByteArrayUtilz.fromString(null));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtilz.fromString(""));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtilz.fromString("          "));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtilz.fromString("HELLO THIS IS WRONG STRING"));
        Assertions.assertArrayEquals(expected, ByteArrayUtilz.fromString(hexString));
        Assertions.assertArrayEquals(expected, ByteArrayUtilz.fromString(hexStringPartiallyValid));
        Assertions.assertArrayEquals(new byte[]{10}, ByteArrayUtilz.fromString("A"));
    }

    @Test
    void xorTest() {
        Assertions.assertArrayEquals(
                ByteArrayUtilz.fromString("AAAA"),
                ByteArrayUtilz.xor(
                        ByteArrayUtilz.fromString("AA00"),
                        ByteArrayUtilz.fromString("00AA")
                )
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteArrayUtilz.xor(null, null));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtilz.xor(new byte[0],new byte[0]));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtilz.xor(new byte[0],new byte[1]));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtilz.xor(new byte[1],new byte[0]));
        Assertions.assertArrayEquals(
                ByteArrayUtilz.fromString("AAAA"),
                ByteArrayUtilz.xor(
                        ByteArrayUtilz.fromString("AA00CC"),
                        ByteArrayUtilz.fromString("00AA")
                )
        );
        Assertions.assertArrayEquals(
                ByteArrayUtilz.fromString("AAAA"),
                ByteArrayUtilz.xor(
                        ByteArrayUtilz.fromString("AA00"),
                        ByteArrayUtilz.fromString("00AACC")
                )
        );
    }

    @Test
    void utf16toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtilz.fromString(null,StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("31003100"), ByteArrayUtilz.fromString("11",StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("00000000"), ByteArrayUtilz.fromString("\0\0",StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("3A003A00"), ByteArrayUtilz.fromString("::",StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString(
                "480065006C006C006F00200077006F0072006C0064002100"
        ), ByteArrayUtilz.fromString("Hello world!", StringType.UTF16LE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("00310031"), ByteArrayUtilz.fromString("11",StringType.UTF16BE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("00000000"), ByteArrayUtilz.fromString("\0\0",StringType.UTF16BE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("003A003A"), ByteArrayUtilz.fromString("::",StringType.UTF16BE));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString(
                "00480065006C006C006F00200077006F0072006C00640021"
        ), ByteArrayUtilz.fromString("Hello world!",StringType.UTF16BE));

    }

    @Test
    void utf8toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtilz.fromString(null,StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("3131"), ByteArrayUtilz.fromString("11",StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("0000"), ByteArrayUtilz.fromString("\0\0",StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("3A3A"), ByteArrayUtilz.fromString("::",StringType.UTF8));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("48656C6C6F20776F726C6421"), ByteArrayUtilz.fromString("Hello world!",StringType.UTF8));
    }

    @Test
    void reverseTest() {
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("223344"), ByteArrayUtilz.reverse(ByteArrayUtilz.fromString("443322")));
        byte[] expected = ByteArrayUtilz.fromString("EFCDAB9078563412");
        byte[] tested = ByteArrayUtilz.fromString("1234567890ABCDEF");
        Assertions.assertArrayEquals(expected, ByteArrayUtilz.reverse(tested));
        Assertions.assertArrayEquals(ByteArrayUtilz.fromString("00AA"), ByteArrayUtilz.reverse(ByteArrayUtilz.fromString("AA00")));
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtilz.reverse(null));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtilz.reverse(new byte[0]));
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(new byte[]{i}, ByteArrayUtilz.reverse(new byte[]{i}));
        }
    }

    @Test
    void print() {
    }

    @Test
    void testToString() {
        Assertions.assertEquals("AABBAA",ByteArrayUtilz.toString(ByteArrayUtilz.fromString("AABBAA")));
        Assertions.assertEquals("",ByteArrayUtilz.toString(ByteArrayUtilz.fromString("")));
        Assertions.assertEquals("",ByteArrayUtilz.toString(new byte[0]));
        Assertions.assertEquals("01",ByteArrayUtilz.toString(new byte[]{1}));
        Assertions.assertEquals("0B",ByteArrayUtilz.toString(new byte[]{11}));
        Assertions.assertThrows(IllegalArgumentException.class,()->ByteArrayUtilz.toString(null));
    }

    @Test
    void toBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.fromString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.fromString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.fromString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            byte[] expected = fromStringShortPadded(i);
            byte[] test = inst.toBytes(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.reverse(expected), test
            );
            test = inst.toBytes(i, Endiannes.LITTLE);
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.reverse(expected), test
            );
            test = inst.toBytes(i, Endiannes.BIG);
            Assertions.assertArrayEquals(
                    expected, test
            );
        }
        final  int STEP_I = 1 << (Integer.SIZE-6);
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE-STEP_I)+1; i += STEP_I ) {
            byte[] expected = fromStringIntPadded(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.reverse(expected),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.reverse(expected),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    expected,
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        final long STEP_L = 1L << (Long.SIZE-6);
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE-STEP_L)+1; i += STEP_L) {
            byte[] expected = fromStringLongPadded(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.reverse(expected),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtilz.reverse(expected),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    expected,
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> inst.toBytes(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> inst.toBytes(new BigInteger(String.valueOf(1))));
    }

    private byte[] fromStringShortPadded(short i) {
        return ByteArrayUtilz.fromString(
                String.format("%1$" + Integer.BYTES + "s",
                        Integer.toHexString(i & 0xFFFF)).replace(' ', '0')
        );
    }

    private byte[] fromStringIntPadded(int i) {
        return ByteArrayUtilz.fromString(
                String.format("%1$" + Integer.BYTES * 2 + "s",
                        Integer.toHexString(i)).replace(' ', '0')
        );
    }

    private byte[] fromStringLongPadded(long i) {
        return ByteArrayUtilz.fromString(
                String.format("%1$" + Long.BYTES * 2 + "s",
                        Long.toHexString(i)).replace(' ', '0')
        );
    }

    @Test
    void fromBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertEquals(i, (byte) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (byte) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (byte) inst.fromBytes(inst.toBytes(i), Endiannes.BIG));
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            Assertions.assertEquals(i, (short) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (short) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (short) inst.fromBytes(inst.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
        final  int STEP_I = 1 << (Integer.SIZE-6);
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE-STEP_I)+1; i += STEP_I ) {
            Assertions.assertEquals(i, (int) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (int) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (int) inst.fromBytes(inst.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
        final long STEP_L = 1L << (Long.SIZE-6);
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE-STEP_L)+1; i += STEP_L) {
            Assertions.assertEquals(i, (long) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (long) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (long) inst.fromBytes(inst.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
    }
}