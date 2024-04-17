package com.nixiedroid.rpc.data;


import com.nixiedroid.rpc.data.Bind.BindProcess;
import com.nixiedroid.rpc.data.request.RequestProcess;
import com.nixiedroid.rpc.util.ByteArrayUtilz;
import com.nixiedroid.rpc.util.StringType;

import java.io.*;

public class ResponseGenerator {
    public static byte[] generateResponse(DataInputStream is) throws IOException {
        if ((is.available() - Header.SIZE)<0) return new byte[0];

        Header header = new Header(is);
        switch (header.type){
            case ALTER_CONTEXT:
            case BIND:
                return BindProcess.handle(pdu,header);
            case REQUEST:
                return RequestProcess.handle(pdu,header);
            default:
                return ByteArrayUtilz.fromString("Nothing Interesting", StringType.UTF8);
        }
    }
}
