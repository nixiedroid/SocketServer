package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.UUID;


public class UUIDItem implements BytePackable {
    public static final int SIZE = 44;
    private int items;
    private int contextID;
    private int padding;
    private UUID abstractUUID = new UUID(new byte[16]);
    private int abstractVer;
    private UUID transferUUID = new UUID(new byte[16]);
    private int transferVer;
    public UUIDItem(byte[] data) {
       deserialize(data,0);
    }

    private UUIDItem() {
    }

    public UUID getTransferUUID() {
        return transferUUID;
    }

    @Override
    public UUIDItem deserialize(byte[] data,int size) {
        contextID = ByteArrayUtils.toInt16(data, 0, Endiannes.LITTLE);
        items = data[2];
        padding = data[3];
        System.arraycopy(data, 4, abstractUUID.uuid, 0, 16);
        abstractVer = ByteArrayUtils.toInt32(data, 20, Endiannes.LITTLE);
        System.arraycopy(data, 24, transferUUID.uuid, 0, 16);
        transferVer = ByteArrayUtils.toInt32(data, 40, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.toBytes(contextID),0,packed,0,2);
        packed[2] = (byte) items;
        packed[3] = (byte) padding;
        System.arraycopy(abstractUUID.uuid, 0, packed, 4, 16);
        System.arraycopy(ByteArrayUtils.toBytes(abstractVer), 0, packed, 20, 4);
        System.arraycopy(transferUUID.uuid, 0, packed, 24, 16);
        System.arraycopy(ByteArrayUtils.toBytes(transferVer), 0, packed, 40, 4);
        return packed;
    }


    @Override
    public int size() {
        return SIZE;
    }

    public static class Builder {
        private UUIDItem item;
        public Builder() { item = new UUIDItem();}
        public Builder withCtxID(int id){
            item.contextID = id;
            return this;
        }
        public Builder withItems(int items){
            item.items = items;
            return this;
        }
        public Builder withPadding(int padding){
            item.padding = padding;
            return this;
        }
        public Builder withAbstract(UUID abstractUUID){
            item.abstractUUID = abstractUUID;
            return this;
        }
        public Builder withAbstractVer(int ver){
            item.abstractVer = ver;
            return this;
        }
        public Builder withTransfer(UUID transferUUID){
            item.transferUUID = transferUUID;
            return this;
        }
        public Builder withTransferVer(int ver){
            item.transferVer = ver;
            return this;
        }

        public UUIDItem build() {
            return item;
        }
    }
}
