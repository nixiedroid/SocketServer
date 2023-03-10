package com.nixiedroid.data;

public abstract class Data implements Packable{
    private Header header = null;
   public abstract int size();

   public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }
}
