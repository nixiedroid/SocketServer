package com.nixiedroid.rpc.data;


import com.nixiedroid.rpc.data.Bind.BindProcess;
import com.nixiedroid.rpc.data.request.RequestProcess;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.StringType;

import java.io.*;

public class ResponseGenerator {
    public static byte[] generateResponse(byte[] data,Header header) throws IOException {
        switch (header.type){
            case ALTER_CONTEXT:
            case BIND:
                return BindProcess.handle(data,header);
            case REQUEST:
                return RequestProcess.handle(data,header);
            default:
                return ByteArrayUtils.fromString("Nothing Interesting", StringType.UTF8);
        }
    }
}
