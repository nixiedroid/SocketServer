import java.math.BigInteger;

import static com.nixiedroid.util.ByteArrayUtils.chunk.*;
import static com.nixiedroid.util.ByteArrayUtils.*;

public class ByteArrayUtilsTest {
    public static void main(String[] args) {
        byte[] a;
        a =  uInt16ToBytesL(0x1234);
        print(a);
        System.out.println("val: " +  toUInt16L(a));
        a =  uInt16ToBytesB(0x1234);
        print(a);
        System.out.println("val: " + toUInt16B(a));
        a = uInt32ToBytesL(0x12345678L);
        print(a);
        System.out.println("val: " + toUInt32L(a));
        a = uInt32ToBytesB(0x12345678L);
        print(a);
        System.out.println("val: " + toUInt32B(a));
        a = uInt64ToBytesL(BigInteger.valueOf(0x1234567812345678L));
        print(a);
        System.out.println("val: " + toUInt64L(a));
        a = uInt64ToBytesB(BigInteger.valueOf(0x1234567812345678L));
        print(a);
        System.out.println("val: " + toUInt64B(a));

        a = int16ToBytesL((short) 0x1234);
        print(a);
        System.out.println("val: " + toInt16L(a));
        a = int16ToBytesB((short) 0x1234);
        print(a);
        System.out.println("val: " + toInt16B(a));
        a = int32ToBytesL(0x12345678);
        print(a);
        System.out.println("val: " + toInt32L(a));
        a = int32ToBytesB(0x12345678);
        print(a);
        System.out.println("val: " + toInt32B(a));
        a = int64ToBytesL(0x1234567812345678L);
        print(a);
        System.out.println("val: " + toInt64L(a));
        a = int64ToBytesB(0x1234567812345678L);
        print(a);
        System.out.println("val: " + toInt64B(a));
    }
}
