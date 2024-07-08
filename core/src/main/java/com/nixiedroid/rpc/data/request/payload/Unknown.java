package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.logger.Logger;

public class Unknown {
    public static byte[] handle(){
        Logger.info("Received unknown request");
        byte[] out = new byte[12];
        System.arraycopy(ByteArrayUtils.toBytes(0xC004F042, Endiannes.LITTLE),0,out,8,4);
        Logger.debug("Response payload: " + ByteArrayUtils.toString(out));
        return out;
    }

}