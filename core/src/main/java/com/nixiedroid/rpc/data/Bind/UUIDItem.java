package com.nixiedroid.rpc.data.Bind;

import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.data.BytePackable;
import com.nixiedroid.rpc.util.Endiannes;
import com.nixiedroid.rpc.util.UUID;


public class UUIDItem implements BytePackable {
    public static final int SIZE = 44;
    private short contextID; //2Bytes
    private short items; //2Bytes
    private UUID abstractUUID = new UUID(new byte[16]); //16bytes
    private int abstractVer;//4 bytes
    private UUID transferUUID = new UUID(new byte[16]);//16 bytes
    private int transferVer;//4 bytes

    public UUIDItem(byte[] data,int start) {
       deserialize(data,start);
    }

    private UUIDItem() {
    }

    public UUID getTransferUUID() {
        return transferUUID;
    }

    @Override
    public UUIDItem deserialize(byte[] data,int start) {
        contextID = ByteArrayUtils.toInt16(data, start, Endiannes.LITTLE);
        items = ByteArrayUtils.toInt16(data, start+2, Endiannes.LITTLE);
        abstractUUID.deserialize(data,start+4);
        abstractVer = ByteArrayUtils.toInt32(data, start+20, Endiannes.LITTLE);
        transferUUID.deserialize(data,start+24);
        transferVer = ByteArrayUtils.toInt32(data, start+40, Endiannes.LITTLE);
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.toBytes(contextID),0,packed,0,2);
        System.arraycopy(ByteArrayUtils.toBytes(items),0,packed,0,2);
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
            item.contextID = (short) id;
            return this;
        }
        public Builder withItems(int items){
            item.items = (short) items;
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
