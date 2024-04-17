package com.nixiedroid.rpc.data;

public abstract class Data implements BytePackable<Data> {
    private Header header = null;
   public abstract int size();

   public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
}
