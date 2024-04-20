package com.nixiedroid.rpc.data.Bind;


import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class DataBindACK extends Data {
    public static final int SIZE = 14; //min size
    int maxTXLen; //little endian unsigned short (2bytes)
    int maxRXLen;//little endian unsigned short (2bytes)
    int messageId; //little endian long (4bytes)
    int portLength; //uint16
    byte[] port = new byte[5]; // OPTIONAL. Only if addrlength!=0. NUL-terminated string
    int padLen;
    int uuidNum;
    int reserved1;
    int reserved2; //little endian unsigned short (2bytes)
    UUIDItemResult[] uuidItemResults;
    int secTrailerLen; //= (authLen>0)?8:0
    Trailer trailer;
    int configDataLen; //= authLen
    ConfigData configData;
    public DataBindACK() {

    }


    @Override
    public DataBindACK deserialize(byte[] data, int start) {
        maxTXLen = ByteArrayUtils.toInt16(data, start, Endiannes.LITTLE);
        maxRXLen = ByteArrayUtils.toInt16(data, start +2,Endiannes.LITTLE);
        messageId = ByteArrayUtils.toInt32(data, start +4,Endiannes.LITTLE);
        portLength = ByteArrayUtils.toInt16(data, start +8,Endiannes.LITTLE);
        System.arraycopy(data, start +10, port, 0, portLength);
        uuidNum = (data[start +portLength +11] % 0xFF);
        reserved1 = (data[start +portLength +12] % 0xFF);
        reserved2 = ByteArrayUtils.toInt16(data, start +portLength +13,Endiannes.LITTLE);;

        padLen = (4- (portLength +SIZE) % 4) % 4;
        secTrailerLen = (configDataLen >0)?8:0;
        return this;
    }

    @Override
    public byte[] serialize() {
        padLen = (4- (portLength +SIZE) % 4) % 4;
        byte[] packed = new byte[SIZE + uuidNum * UUIDItemResult.SIZE + portLength + padLen];
        System.arraycopy(ByteArrayUtils.toBytes(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.toBytes(messageId), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.toBytes(portLength), 0, packed, 8, 2);
        System.arraycopy(port, 0, packed, 10, portLength);
        packed[10+ portLength +padLen] = (byte) uuidNum;
        packed[12+ portLength +padLen] = (byte) reserved1;
        System.arraycopy(ByteArrayUtils.toBytes(reserved2), 0, packed, 12+ portLength +padLen, 2);
        for (int i = 0; i < uuidNum; i++) {
            System.arraycopy(uuidItemResults[i].serialize(), 0, packed, portLength + 14+i* UUIDItemResult.SIZE+padLen, UUIDItemResult.SIZE);
        }
        return packed;
    }

    @Override
    public int size() {
        if (maxTXLen !=0 ) {
            padLen = (4- (portLength +SIZE) % 4) % 4;
          return   SIZE+ portLength +padLen+ uuidNum * UUIDItemResult.SIZE+secTrailerLen+ configDataLen;
        }
        else return -1;
    }
}
