package com.nixiedroid.data;


import com.nixiedroid.data.Bind.BindProcess;
import com.nixiedroid.data.request.RequestProcess;
import com.nixiedroid.data.enums.RequestTypes;

public class DataProcess {
    public static byte[] processRequest(byte[] data) {
        if ((data.length - Header.SIZE)<0) return new byte[0];
        byte[] chunk = new byte[data.length - Header.SIZE];
        Header header = new Header(data);
        System.arraycopy(data, Header.SIZE, chunk, 0, chunk.length);
        if (header.type == RequestTypes.TYPE_ALTERCTX.getType() || header.type == RequestTypes.TYPE_BIND.getType()) {
            return BindProcess.handle(chunk,header);
        } else
        if (header.type == RequestTypes.TYPE_REQUEST.getType()) {
            return RequestProcess.handle(chunk,header);
        } else return "Nothing Interesting".getBytes();
    }

}
