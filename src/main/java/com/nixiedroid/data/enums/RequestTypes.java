package com.nixiedroid.data.enums;


public enum RequestTypes {
    TYPE_REQUEST((byte) 0x00),

    TYPE_PING((byte) 0x01),

    TYPE_RESPONSE((byte) 0x02),

    TYPE_FAULT((byte) 0x03),
    TYPE_WORKING((byte) 0x04),

    TYPE_NOCALL((byte) 0x05),

    TYPE_REJECT((byte) 0x06),

    TYPE_ACK((byte) 0x07),

    TYPE_CL_CANCEL((byte) 0x08),

    TYPE_FACK((byte) 0x09),

    TYPE_CANCELACK((byte) 0x0A),

    TYPE_BIND((byte) 0x0B),

    TYPE_BINDACK((byte) 0x0C),

    TYPE_BINDNAK((byte) 0x0D),

    TYPE_ALTERCTX((byte) 0x0E),

    TYPE_ALTERCTX_ACK((byte) 0x0F),

    TYPE_AUTH3((byte) 0x10),

    TYPE_SHUTDOWN((byte) 0x11),

    TYPE_CO_CANCEL((byte) 0x12),

    TYPE_ORPHANED((byte) 0x13);

    private final byte type;


    RequestTypes(byte type) {
        this.type = type;

    }

    public byte getType() {
        return type;
    }
}
