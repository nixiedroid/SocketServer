package com.nixiedroid.rpc.data.payload;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class Unknown {
    public static byte[] handle(){
        Context.l().info("Received unknown request");
        byte[] out = new byte[12];
        System.arraycopy(ByteArrayUtils.toBytes(0xC004F042, Endiannes.LITTLE),0,out,8,4);
        Context.l().debug("Response payload: " + ByteArrayUtils.toString(out));
        return out;
    }

}