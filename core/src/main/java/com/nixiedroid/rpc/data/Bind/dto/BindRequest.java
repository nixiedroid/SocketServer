package com.nixiedroid.rpc.data.Bind.dto;

import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.data.Header;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class BindRequest extends Data {
    short maxTXLen; //little endian unsigned short (2bytes)
    short maxRXLen;//little endian unsigned short (2bytes)
    int messageId; //little endian int (4bytes)
    int uuidNum; //LE 4bytes
    UUIDItem[] uuidItems;
   // public static final int SIZE = 16;

    @Override
    public BindRequest deserialize(byte[] data, int start) {
        maxTXLen = ByteArrayUtils.toInt16(data, start+0, Endiannes.LITTLE);
        maxRXLen = ByteArrayUtils.toInt16(data,start+2, Endiannes.LITTLE);
        messageId =  ByteArrayUtils.toInt32(data,start+4, Endiannes.LITTLE);
        uuidNum = ByteArrayUtils.toInt32(data, start+8, Endiannes.LITTLE);
        this.uuidItems = new UUIDItem[this.uuidNum];
        for (int i = 0; i < this.uuidNum; i++) {
            this.uuidItems[i] = new UUIDItem(data,start+12 +i * UUIDItem.SIZE);
        }
        return this;
    }

    public short getMaxTXLen() {
        return maxTXLen;
    }

    public short getMaxRXLen() {
        return maxRXLen;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getUuidNum() {
        return uuidNum;
    }

    public UUIDItem[] getUuidItems() {
        return uuidItems;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[12 + uuidNum * UUIDItem.SIZE];
        System.arraycopy(ByteArrayUtils.toBytes(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.toBytes(messageId), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.toBytes(uuidNum), 0, packed, 8, 4);
        for (int i = 0; i < uuidNum; i++) {
            System.arraycopy(uuidItems[i].serialize(), 0, packed, 12 + i * UUIDItem.SIZE, UUIDItem.SIZE);
        }
        return packed;
    }

    @Override
    public int size() {
        return 12 + uuidNum * UUIDItem.SIZE;
      //  return SIZE;
    }

    public static class Builder {
        private BindRequest request;
        public Builder(){
            request = new BindRequest();
        }
        public Builder withHeader(Header header ){
            request.setHeader(header);
            return this;
        }
            public Builder withTXLen(int len){
                request.maxTXLen = (short) len;
                return this;
            }
            public Builder withRXLen(int len){
                request.maxRXLen = (short) len;
                return this;
            }
            public Builder withMessageID(long id){
                request.messageId = (int) id;
                return this;
            }
            public Builder withUuidNum(int num){
                request.uuidNum = num;
                return this;
            }
            public Builder withUuidItems(UUIDItem[] items){
                request.uuidItems = items;
                return this;
            }
            public BindRequest build(){
                return request;
            }
    }
}
