package com.nixiedroid.data.payload;

import com.nixiedroid.Program;
import com.nixiedroid.util.ByteArrayUtils;

public class Unknown {
    public static byte[] handle(){
        Program.log().info("Received unknown request");
        byte[] out = new byte[12];
        System.arraycopy(ByteArrayUtils.int32ToBytesL(0xC004F042),0,out,8,4);
        Program.log().debug("Response payload: " + ByteArrayUtils.toString(out));
        return out;
    }

}