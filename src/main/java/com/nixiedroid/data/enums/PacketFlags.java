package com.nixiedroid.data.enums;

public enum PacketFlags {
   
         FIRSTFRAG((byte) 0x01),
         LASTFRAG((byte) 0x02),
         CANCELPENDING((byte) 0x04),
         RESERVED((byte) 0x08),
         MULTIPLEX((byte) 0x10),
         DIDNOTEXECUTE((byte) 0x20),
         MAYBE((byte) 0x40),
         OBJECTUUID((byte) 0x80);
    

    private final byte flag;

    PacketFlags(byte flag) {
        this.flag = flag;
    }

    public byte get() {
        return flag;
    }
}
