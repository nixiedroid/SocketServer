package com.nixiedroid.data.Bind;

import com.nixiedroid.data.Data;
import com.nixiedroid.data.Header;

public class DataBind extends Data {

    public static final int SIZE = 16;
    int dataLen; //=fragLen-authLen-SIZE-(authLen > 0)?8:0
    //dataLen = PayloadBind.size() * UUidNum*UUidItem.size()
    public PayloadBind payloadBind;

    int padLen; //=(4 - ((SIZE + ((flags & 0x80) > 0)?16:0 + pduData.length()) & 3) & 3)

    int secTrailerLen; //= (authLen>0)?8:0

    Trailer trailer;

    int configDataLen; //= authLen

    ConfigData configData;

    @Override
    public void unpack(byte[] data) {
        byte[] dataChunk, dataChunk2;
        dataLen = getHeader().getFragLen() - getHeader().getAuthLen() - DataBind.SIZE;
        dataLen -= (getHeader().getAuthLen() > 0) ? 8 : 0;
        dataChunk = new byte[dataLen];
        System.arraycopy(data, 0, dataChunk, 0, dataLen);
        this.payloadBind = new PayloadBind();
        this.payloadBind.unpack(dataChunk);
        this.payloadBind.uuidItems = new UUIDItem[this.payloadBind.uuidNum];
        for (int i = 0; i < this.payloadBind.uuidNum; i++) {
            dataChunk2 = new byte[UUIDItem.SIZE];
            System.arraycopy(dataChunk, 12 + i * UUIDItem.SIZE, dataChunk2, 0, UUIDItem.SIZE);
            this.payloadBind.uuidItems[i] = new UUIDItem(dataChunk2);
        }
        int tmp1 = ((getHeader().getFlags() & 0x80) > 0) ? 16 : 0;
        padLen = (4 - ((DataBind.SIZE + (tmp1 + dataChunk.length)) & 3) & 3);
        secTrailerLen = (getHeader().getAuthLen() > 0) ? 8 : 0;
        if (secTrailerLen == 0) trailer = null;
        configDataLen = getHeader().getAuthLen();
        if (configDataLen == 0) configData = null;
    }

    @Override
    public byte[] pack() {
        dataLen = payloadBind.size()+SIZE;
        byte[] packed = new byte[dataLen];

        return new byte[0];
    }

    @Override
    public int size() {
        return SIZE;
    }

    public static class Builder {
        private DataBind request;
        public Builder(){
            request = new DataBind();
        }
        public Builder withHeader(Header header ){
            request.setHeader(header);
            return this;
        }
        public Builder withData(PayloadBind payloadBind){
            request.payloadBind = payloadBind;
            return this;
        }
        public Builder withSecTrailer(Trailer trailer){
            request.trailer = trailer;
            return this;
        }
        public Builder withAuthData(ConfigData data){
            request.configData = data;
            return this;
        }
        public DataBind build(){
            return request;
        }
    }
}
