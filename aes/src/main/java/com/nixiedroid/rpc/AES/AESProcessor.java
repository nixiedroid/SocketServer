package com.nixiedroid.rpc.AES;

import java.util.Arrays;

import static com.nixiedroid.rpc.AES.AESProcessor.C.*;

@SuppressWarnings({"SameParameterValue", "DuplicatedCode"})
final class AESProcessor {
    private boolean ROUNDS_12 = false;
    private boolean ROUNDS_14 = false;

    private int[] Ke = null;
    private int[] Kd = null;

    /** ROUNDS * 4 */
    private int limit = 0;
    private final int rounds;

    AESProcessor(byte[] key, int rounds, boolean flag) {
        this.rounds = rounds;
        makeSessionKey(key,flag);
    }

    AESProcessor(byte[] key, boolean flag) {
        this(key, 0, flag);
    }

    private void makeSessionKey(byte[] k,boolean flag)  {

        final int BC = 4;
        int ROUNDS = (rounds==0)?(k.length >> 2) + 6:rounds;
        int ROUND_KEY_COUNT = (ROUNDS + 1) * BC;
        int[] Ke = new int[ROUND_KEY_COUNT]; // encryption round keys
        int[] Kd = new int[ROUND_KEY_COUNT]; // decryption round keys

        int KC = k.length/4; // keylen in 32-bit elements

        int[] tk = new int[KC];
        int i, j;

        // copy user material bytes into temporary ints
        for (i = 0, j = 0; i < KC; i++, j+=4) {
            tk[i] = (k[j]       ) << 24 |
                    (k[j+1] & 0xFF) << 16 |
                    (k[j+2] & 0xFF) <<  8 |
                    (k[j+3] & 0xFF);
        }

        // copy values into round key arrays
        int t = 0;
        for (j = 0; (j < KC) && (t < ROUND_KEY_COUNT); j++, t++) {
            Ke[t] = tk[j];
            Kd[(ROUNDS - (t / BC))*BC + (t % BC)] = tk[j];
        }
        int tt, rconpointer = 0;
        while (t < ROUND_KEY_COUNT) {
            // extrapolate using phi (the round key evolution function)
            tt = tk[KC - 1];
            tk[0] ^= (S[(tt >>> 16) & 0xFF]       ) << 24 ^
                    (S[(tt >>>  8) & 0xFF] & 0xFF) << 16 ^
                    (S[(tt       ) & 0xFF] & 0xFF) <<  8 ^
                    (S[(tt >>> 24)       ] & 0xFF)       ^
                    (rcon[rconpointer++]         ) << 24;
            if (KC != 8)
                for (i = 1, j = 0; i < KC; i++, j++) tk[i] ^= tk[j];
            else {
                for (i = 1, j = 0; i < KC / 2; i++, j++) tk[i] ^= tk[j];
                tt = tk[KC / 2 - 1];
                tk[KC / 2] ^= (S[(tt       ) & 0xFF] & 0xFF)       ^
                        (S[(tt >>>  8) & 0xFF] & 0xFF) <<  8 ^
                        (S[(tt >>> 16) & 0xFF] & 0xFF) << 16 ^
                        (S[(tt >>> 24)       ]       ) << 24;
                for (j = KC / 2, i = j + 1; i < KC; i++, j++) tk[i] ^= tk[j];
            }
            // copy values into round key arrays
            for (j = 0; (j < KC) && (t < ROUND_KEY_COUNT); j++, t++) {
                Ke[t] = tk[j];
                Kd[(ROUNDS - (t / BC))*BC + (t % BC)] = tk[j];
            }
        }

        //
        if (flag) {
            Kd[(ROUNDS - 4)*BC] ^= (((byte) (0x73) << 24) & 0xff000000);
            Kd[(ROUNDS - 6)*BC] ^= (((byte) (0x09) << 24) & 0xff000000);
            Kd[(ROUNDS - 8)*BC] ^= (((byte) (0xe4) << 24) & 0xff000000);
            Ke[(4)*BC] ^= (((byte) (0x73) << 24) & 0xff000000);
            Ke[6*BC] ^= (((byte) (0x09) << 24) & 0xff000000);
            Ke[8*BC] ^= (((byte) (0xe4) << 24) & 0xff000000);
        }
        //


        for (int r = 1; r < ROUNDS; r++) {
            // inverse MixColumn where needed
            for (j = 0; j < BC; j++) {
                int idx = r*BC + j;
                tt = Kd[idx];
                Kd[idx] = U1[(tt >>> 24) & 0xFF] ^
                        U2[(tt >>> 16) & 0xFF] ^
                        U3[(tt >>>  8) & 0xFF] ^
                        U4[ tt         & 0xFF];
            }
        }

        // For decryption round keys, need to rotate right by 4 ints.
        // Do that without allocating and zeroing the small buffer.
        int KdTail_0 = Kd[Kd.length - 4];
        int KdTail_1 = Kd[Kd.length - 3];
        int KdTail_2 = Kd[Kd.length - 2];
        int KdTail_3 = Kd[Kd.length - 1];
        System.arraycopy(Kd, 0, Kd, 4, Kd.length - 4);
        Kd[0] = KdTail_0;
        Kd[1] = KdTail_1;
        Kd[2] = KdTail_2;
        Kd[3] = KdTail_3;

        Arrays.fill(tk, 0);
        ROUNDS_12 = (ROUNDS>=12);
        ROUNDS_14 = (ROUNDS==14);
        limit = ROUNDS*4;

        this.Ke = Ke;
        this.Kd = Kd;
    }

    byte[] decrypt(byte[] data) {
        byte[] out = new byte[data.length];
        decrypt(data, 0, out, 0);
        return out;
    }

    byte[] encrypt(byte[] data) {
        byte[] out = new byte[data.length];
        encrypt(data, 0, out, 0);
        return out;
    }

    void encrypt(byte[] in, int inOffset, byte[] out, int outOffset) {
        int keyOffset = 0;
        int t0   = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset++] & 0xFF)        ) ^ Ke[keyOffset++];
        int t1   = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset++] & 0xFF)        ) ^ Ke[keyOffset++];
        int t2   = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset++] & 0xFF)        ) ^ Ke[keyOffset++];
        int t3   = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset]   & 0xFF)        ) ^ Ke[keyOffset++];

        // apply round transforms
        while( keyOffset < limit )
        {
            int a0, a1, a2;
            a0 = T1[(t0 >>> 24)       ] ^
                    T2[(t1 >>> 16) & 0xFF] ^
                    T3[(t2 >>>  8) & 0xFF] ^
                    T4[(t3       ) & 0xFF] ^ Ke[keyOffset++];
            a1 = T1[(t1 >>> 24)       ] ^
                    T2[(t2 >>> 16) & 0xFF] ^
                    T3[(t3 >>>  8) & 0xFF] ^
                    T4[(t0       ) & 0xFF] ^ Ke[keyOffset++];
            a2 = T1[(t2 >>> 24)       ] ^
                    T2[(t3 >>> 16) & 0xFF] ^
                    T3[(t0 >>>  8) & 0xFF] ^
                    T4[(t1       ) & 0xFF] ^ Ke[keyOffset++];
            t3 = T1[(t3 >>> 24)       ] ^
                    T2[(t0 >>> 16) & 0xFF] ^
                    T3[(t1 >>>  8) & 0xFF] ^
                    T4[(t2       ) & 0xFF] ^ Ke[keyOffset++];
            t0 = a0; t1 = a1; t2 = a2;
        }

        // last round is special
        int tt = Ke[keyOffset++];
        out[outOffset++] = (byte)(S[(t0 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t1 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t2 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset++] = (byte)(S[(t3       ) & 0xFF] ^ (tt       ));
        tt = Ke[keyOffset++];
        out[outOffset++] = (byte)(S[(t1 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t2 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t3 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset++] = (byte)(S[(t0       ) & 0xFF] ^ (tt       ));
        tt = Ke[keyOffset++];
        out[outOffset++] = (byte)(S[(t2 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t3 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t0 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset++] = (byte)(S[(t1       ) & 0xFF] ^ (tt       ));
        tt = Ke[keyOffset];
        out[outOffset++] = (byte)(S[(t3 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t0 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t1 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset  ] = (byte)(S[(t2       ) & 0xFF] ^ (tt       ));
    }

    void decrypt(byte[] in, int inOffset, byte[] out, int outOffset) {
        int keyOffset = 4;
        int t0 = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset++] & 0xFF)        ) ^ Kd[keyOffset++];
        int t1 = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset++] & 0xFF)        ) ^ Kd[keyOffset++];
        int t2 = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset++] & 0xFF)        ) ^ Kd[keyOffset++];
        int t3 = ((in[inOffset++]       ) << 24 |
                (in[inOffset++] & 0xFF) << 16 |
                (in[inOffset++] & 0xFF) <<  8 |
                (in[inOffset  ] & 0xFF)        ) ^ Kd[keyOffset++];

        int a0, a1, a2;
        if(ROUNDS_12)
        {
            a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                    T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
            a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                    T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
            a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                    T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
            t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                    T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset++];
            t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                    T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ Kd[keyOffset++];
            t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                    T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ Kd[keyOffset++];
            t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                    T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
            t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                    T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ Kd[keyOffset++];

            if(ROUNDS_14)
            {
                a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                        T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
                a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                        T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
                a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                        T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
                t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                        T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset++];
                t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                        T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ Kd[keyOffset++];
                t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                        T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ Kd[keyOffset++];
                t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                        T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
                t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                        T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ Kd[keyOffset++];
            }
        }
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ Kd[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ Kd[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ Kd[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ Kd[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ Kd[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ Kd[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ Kd[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ Kd[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ Kd[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ Kd[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ Kd[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ Kd[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ Kd[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ Kd[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ Kd[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ Kd[keyOffset];

        t1 = Kd[0];
        out[outOffset++] = (byte)(Si[(a0 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(t3 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(a2 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset++] = (byte)(Si[(a1       ) & 0xFF] ^ (t1       ));
        t1 = Kd[1];
        out[outOffset++] = (byte)(Si[(a1 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(a0 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(t3 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset++] = (byte)(Si[(a2       ) & 0xFF] ^ (t1       ));
        t1 = Kd[2];
        out[outOffset++] = (byte)(Si[(a2 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(a1 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(a0 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset++] = (byte)(Si[(t3       ) & 0xFF] ^ (t1       ));
        t1 = Kd[3];
        out[outOffset++] = (byte)(Si[(t3 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(a2 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(a1 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset  ] = (byte)(Si[(a0       ) & 0xFF] ^ (t1       ));
    }

    @SuppressWarnings("FinalStaticMethod")
    static class C {
        public static final byte[] S = new byte[256];
        public static final byte[] Si = new byte[256];
        public static final int[] T1 = new int[256];
        public static final int[] T2 = new int[256];
        public static final int[] T3 = new int[256];
        public static final int[] T4 = new int[256];
        public static final int[] T5 = new int[256];
        public static final int[] T6 = new int[256];
        public static final int[] T7 = new int[256];
        public static final int[] T8 = new int[256];
        public static final int[] U1 = new int[256];
        public static final int[] U2 = new int[256];
        public static final int[] U3 = new int[256];
        public static final int[] U4 = new int[256];
        public static final byte[] rcon = new byte[30];
        private static int[] alog = new int[256];
        private static int[] log = new int[256];

        // Static code - to intialise S-boxes and T-boxes
        static {
            int ROOT = 0x11B;
            int i, j;

            //
            // produce log and alog tables, needed for multiplying in the
            // field GF(2^m) (generator = 3)
            //
            alog[0] = 1;
            for (i = 1; i < 256; i++) {
                j = (alog[i - 1] << 1) ^ alog[i - 1];
                if ((j & 0x100) != 0) {
                    j ^= ROOT;
                }
                alog[i] = j;
            }
            for (i = 1; i < 0xFF; i++) {
                log[alog[i]] = i;
            }
            byte[][] A = new byte[][]{
                    {1, 1, 1, 1, 1, 0, 0, 0},
                    {0, 1, 1, 1, 1, 1, 0, 0},
                    {0, 0, 1, 1, 1, 1, 1, 0},
                    {0, 0, 0, 1, 1, 1, 1, 1},
                    {1, 0, 0, 0, 1, 1, 1, 1},
                    {1, 1, 0, 0, 0, 1, 1, 1},
                    {1, 1, 1, 0, 0, 0, 1, 1},
                    {1, 1, 1, 1, 0, 0, 0, 1}
            };
            byte[] B = new byte[]{0, 1, 1, 0, 0, 0, 1, 1};

            //
            // substitution box based on F^{-1}(x)
            //
            int t;
            byte[][] box = new byte[256][8];
            box[1][7] = 1;
            for (i = 2; i < 256; i++) {
                j = alog[0xFF - log[i]];
                for (t = 0; t < 8; t++) {
                    box[i][t] = (byte) ((j >>> (7 - t)) & 0x01);
                }
            }
            //
            // affine transform:  box[i] <- B + A*box[i]
            //
            byte[][] cox = new byte[256][8];
            for (i = 0; i < 256; i++) {
                for (t = 0; t < 8; t++) {
                    cox[i][t] = B[t];
                    for (j = 0; j < 8; j++) {
                        cox[i][t] ^= (byte) (A[t][j] * box[i][j]);
                    }
                }
            }
            //
            // S-boxes and inverse S-boxes
            //
            for (i = 0; i < 256; i++) {
                S[i] = (byte) (cox[i][0] << 7);
                for (t = 1; t < 8; t++) {
                    S[i] ^= (byte) (cox[i][t] << (7 - t));
                }
                Si[S[i] & 0xFF] = (byte) i;
            }
            //
            // T-boxes
            //
            byte[][] G = new byte[][]{{2, 1, 1, 3}, {3, 2, 1, 1}, {1, 3, 2, 1}, {1, 1, 3, 2}};
            byte[][] AA = new byte[4][8];
            for (i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) AA[i][j] = G[i][j];
                AA[i][i + 4] = 1;
            }
            byte pivot, tmp;
            byte[][] iG = new byte[4][4];
            for (i = 0; i < 4; i++) {
                pivot = AA[i][i];
                if (pivot == 0) {
                    t = i + 1;
                    while (AA[t][i] == 0) {
                        t++;
                    }
                    for (j = 0; j < 8; j++) {
                        tmp = AA[i][j];
                        AA[i][j] = AA[t][j];
                        AA[t][j] = tmp;
                    }
                    pivot = AA[i][i];
                }
                for (j = 0; j < 8; j++) {
                    if (AA[i][j] != 0) {
                        AA[i][j] = (byte) alog[(0xFF + log[AA[i][j] & 0xFF] - log[pivot & 0xFF]) % 0xFF];
                    }
                }
                for (t = 0; t < 4; t++) {
                    if (i != t) {
                        for (j = i + 1; j < 8; j++) {
                            AA[t][j] ^= (byte) mul(AA[i][j], AA[t][i]);
                        }
                        AA[t][i] = 0;
                    }
                }
            }
            for (i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) {
                    iG[i][j] = AA[i][j + 4];
                }
            }

            int s;
            for (t = 0; t < 256; t++) {
                s = S[t];
                T1[t] = mul4(s, G[0]);
                T2[t] = mul4(s, G[1]);
                T3[t] = mul4(s, G[2]);
                T4[t] = mul4(s, G[3]);

                s = Si[t];
                T5[t] = mul4(s, iG[0]);
                T6[t] = mul4(s, iG[1]);
                T7[t] = mul4(s, iG[2]);
                T8[t] = mul4(s, iG[3]);

                U1[t] = mul4(t, iG[0]);
                U2[t] = mul4(t, iG[1]);
                U3[t] = mul4(t, iG[2]);
                U4[t] = mul4(t, iG[3]);
            }
            //
            // round constants
            //
            rcon[0] = 1;
            int r = 1;
            for (t = 1; t < 30; t++) {
                r = mul(2, r);
                rcon[t] = (byte) r;
            }
            log = null;
            alog = null;
        }

        // multiply two elements of GF(2^m)
        private static final int mul(int a, int b) {
            return (a != 0 && b != 0) ? alog[(log[a & 0xFF] + log[b & 0xFF]) % 0xFF] : 0;
        }

        // convenience method used in generating Transposition boxes
        private static final int mul4(int a, byte[] b) {
            if (a == 0) return 0;
            a = log[a & 0xFF];
            int a0 = (b[0] != 0) ? alog[(a + log[b[0] & 0xFF]) % 0xFF] & 0xFF : 0;
            int a1 = (b[1] != 0) ? alog[(a + log[b[1] & 0xFF]) % 0xFF] & 0xFF : 0;
            int a2 = (b[2] != 0) ? alog[(a + log[b[2] & 0xFF]) % 0xFF] & 0xFF : 0;
            int a3 = (b[3] != 0) ? alog[(a + log[b[3] & 0xFF]) % 0xFF] & 0xFF : 0;
            return a0 << 24 | a1 << 16 | a2 << 8 | a3;
        }
    }
}



