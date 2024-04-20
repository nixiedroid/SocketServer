package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class PayloadBind implements BytePackable {
    short maxTXLen; //little endian unsigned short (2bytes)
    short maxRXLen;//little endian unsigned short (2bytes)
    int messageId; //little endian long (4bytes)
    int uuidNum;
    int reserved1;
    int reserved2; //little endian unsigned short (2bytes)
    UUIDItem[] uuidItems;

    @Override
    public PayloadBind deserialize(final byte[] data,int start) {
        maxTXLen =  ByteArrayUtils.toInt16(data,0, Endiannes.LITTLE);
        maxTXLen = (short) ByteArrayUtils.toInt16(data, 0, Endiannes.LITTLE);
        maxRXLen = ByteArrayUtils.toInt16(data,2, Endiannes.LITTLE);
        messageId =  ByteArrayUtils.toInt32(data,4, Endiannes.LITTLE);
        uuidNum = (data[8] % 0xFF);
        reserved1 = (data[9] % 0xFF);
        reserved2 = ByteArrayUtils.toInt16(data, 10, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[12 + uuidNum * UUIDItem.SIZE];
        System.arraycopy(ByteArrayUtils.toBytes(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.toBytes(messageId), 0, packed, 4, 4);
        packed[8] = (byte) uuidNum;
        packed[9] = (byte) reserved1;
        System.arraycopy(ByteArrayUtils.toBytes(reserved2), 0, packed, 10, 2);
        for (int i = 0; i < uuidNum; i++) {
            System.arraycopy(uuidItems[i].serialize(), 0, packed, 12 + i * UUIDItem.SIZE, UUIDItem.SIZE);
        }
        return packed;
    }

    @Override
    public int size() {
        return 12 + uuidNum * UUIDItem.SIZE;
    }

    public static class Builder {
        private PayloadBind payload;
        public Builder() {
            payload = new PayloadBind();
        }
        public Builder withTXLen(int len){
            payload.maxTXLen = (short) len;
            return this;
        }
        public Builder withRXLen(int len){
            payload.maxRXLen = (short) len;
            return this;
        }
        public Builder withMessageID(long id){
            payload.messageId = (int) id;
            return this;
        }
        public Builder withUuidNum(int num){
            payload.uuidNum = num;
            return this;
        }
        public Builder withUuidItems(UUIDItem[] items){
            payload.uuidItems = items;
            return this;
        }
        public PayloadBind build(){
            payload.reserved1 = 0;
            payload.reserved2 = 0;
            return payload;
        }
    }
}
