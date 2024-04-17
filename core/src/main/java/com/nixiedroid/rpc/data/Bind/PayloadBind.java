package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.ByteArrayUtils;

public class PayloadBind implements BytePackable {
    int maxTXLen; //little endian unsigned short (2bytes)
    int maxRXLen;//little endian unsigned short (2bytes)
    long messageId; //little endian long (4bytes)
    int uuidNum;
    int reserved1;
    int reserved2; //little endian unsigned short (2bytes)
    UUIDItem[] uuidItems;

    @Override
    public PayloadBind deserialize(final byte[] data,int start) {
        maxTXLen = ByteArrayUtils.ranged.toUInt16L(data, 0);
        maxRXLen = ByteArrayUtils.ranged.toUInt16L(data, 2);
        messageId = ByteArrayUtils.ranged.toUInt32L(data, 4);
        uuidNum = (data[8] % 0xFF);
        reserved1 = (data[9] % 0xFF);
        reserved2 = ByteArrayUtils.ranged.toUInt16L(data, 10);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[12 + uuidNum * UUIDItem.SIZE];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(messageId), 0, packed, 4, 4);
        packed[8] = ByteArrayUtils.uByteToByte(uuidNum);
        packed[9] = ByteArrayUtils.uByteToByte(reserved1);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(reserved2), 0, packed, 10, 2);
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
            payload.maxTXLen = len;
            return this;
        }
        public Builder withRXLen(int len){
            payload.maxRXLen = len;
            return this;
        }
        public Builder withMessageID(long id){
            payload.messageId = id;
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
