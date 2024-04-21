package com.nixiedroid.rpc.data.Bind;


import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class BindRequestACK extends Data {
    public static final int SIZE = 14; //min size
    int maxTXLen; //little endian unsigned short (2bytes)
    int maxRXLen;//little endian unsigned short (2bytes)
    int messageId; //little endian long (4bytes)
    int portLength; //uint16
    byte[] port = new byte[5]; // OPTIONAL. Only if addrlength!=0. NUL-terminated string
    int uuidNum; // LE 4bytes
    UUIDItemResult[] uuidItemResults;

    public BindRequestACK() {

    }


    @Override
    public BindRequestACK deserialize(byte[] data, int start) {
        maxTXLen = ByteArrayUtils.toInt16(data, start, Endiannes.LITTLE);
        maxRXLen = ByteArrayUtils.toInt16(data, start +2,Endiannes.LITTLE);
        messageId = ByteArrayUtils.toInt32(data, start +4,Endiannes.LITTLE);
        portLength = ByteArrayUtils.toInt16(data, start +8,Endiannes.LITTLE);

        if(portLength!=0) System.arraycopy(data, start +10, port, 0, 6);
        int pad = portLength + (4- (portLength +14) % 4) % 4;
        uuidNum = ByteArrayUtils.toInt32(data, start +16+pad,Endiannes.LITTLE);
        for (int i = 0; i < this.uuidNum; i++) {
            this.uuidItemResults[i] = new UUIDItemResult(data,start+20 +pad+i * UUIDItem.SIZE);
        }
        return this;
    }

    @Override
    public byte[] serialize() {
        int pad = portLength + (4- (portLength +14) % 4) % 4;
        byte[] packed = new byte[SIZE +pad+uuidNum * UUIDItemResult.SIZE];
        System.arraycopy(ByteArrayUtils.toBytes(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.toBytes(messageId), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.toBytes(portLength), 0, packed, 8, 2);
        if(portLength!=0) System.arraycopy(port, 0, packed, 10, portLength);
        System.arraycopy(ByteArrayUtils.toBytes(uuidNum), 0, packed, 10+pad, 4);
        for (int i = 0; i < uuidNum; i++) {
            System.arraycopy(uuidItemResults[i].serialize(), 0, packed, 14 +pad+ i * UUIDItemResult.SIZE, UUIDItemResult.SIZE);
        }
        return packed;

    }

    @Override
    public int size() {
        if (maxTXLen !=0 ) {
          return   14 + uuidNum * UUIDItemResult.SIZE;
        }
        else return -1;
    }
}
