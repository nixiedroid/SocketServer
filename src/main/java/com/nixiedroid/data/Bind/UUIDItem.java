package com.nixiedroid.data.Bind;

import com.nixiedroid.util.ByteArrayUtils;
import com.nixiedroid.data.Packable;
import com.nixiedroid.util.UUID;


public class UUIDItem implements Packable {
    public static final int SIZE = 44;
    private int items;
    private int contextID;
    private int padding;
    private UUID abstractUUID = new UUID(new byte[16]);
    private long abstractVer;
    private UUID transferUUID = new UUID(new byte[16]);
    private long transferVer;
    public UUIDItem(byte[] data) {
       unpack(data);
    }

    private UUIDItem() {
    }

    public UUID getTransferUUID() {
        return transferUUID;
    }

    @Override
    public void unpack(byte[] data) {
        contextID = ByteArrayUtils.ranged.toUInt16L(data, 0);
        items = data[2];
        padding = data[3];
        System.arraycopy(data, 4, abstractUUID.uuid, 0, 16);
        abstractVer = ByteArrayUtils.ranged.toUInt32L(data, 20);
        System.arraycopy(data, 24, transferUUID.uuid, 0, 16);
        transferVer = ByteArrayUtils.ranged.toUInt32L(data, 40);

    }

    @Override
    public byte[] pack() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(contextID),0,packed,0,2);
        packed[2] = ByteArrayUtils.uByteToByte(items);
        packed[3] = ByteArrayUtils.uByteToByte(padding);
        System.arraycopy(abstractUUID.uuid, 0, packed, 4, 16);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(abstractVer), 0, packed, 20, 4);
        System.arraycopy(transferUUID.uuid, 0, packed, 24, 16);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(transferVer), 0, packed, 40, 4);
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
        public Builder withAbstractVer(long ver){
            item.abstractVer = ver;
            return this;
        }
        public Builder withTransfer(UUID transferUUID){
            item.transferUUID = transferUUID;
            return this;
        }
        public Builder withTransferVer(long ver){
            item.transferVer = ver;
            return this;
        }

        public UUIDItem build() {
            return item;
        }
    }
}
