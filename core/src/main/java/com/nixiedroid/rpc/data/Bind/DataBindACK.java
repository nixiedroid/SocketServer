package com.nixiedroid.rpc.data.Bind;


import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.util.ByteArrayUtils;

public class DataBindACK extends Data {
    public static final int SIZE = 14; //min size
    int maxTXLen; //little endian unsigned short (2bytes)
    int maxRXLen;//little endian unsigned short (2bytes)
    long messageId; //little endian long (4bytes)
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
        maxTXLen = ByteArrayUtils.ranged.toUInt16L(data, start);
        maxRXLen = ByteArrayUtils.ranged.toUInt16L(data, start +2);
        messageId = ByteArrayUtils.ranged.toUInt32L(data, start +4);
        portLength = ByteArrayUtils.ranged.toUInt16L(data, start +8);
        System.arraycopy(data, start +10, port, 0, portLength);
        uuidNum = (data[start +portLength +11] % 0xFF);
        reserved1 = (data[start +portLength +12] % 0xFF);
        reserved2 = ByteArrayUtils.ranged.toUInt16L(data, start +portLength +13);

        padLen = (4- (portLength +SIZE) % 4) % 4;
        secTrailerLen = (configDataLen >0)?8:0;
        return this;
    }

    @Override
    public byte[] serialize() {
        padLen = (4- (portLength +SIZE) % 4) % 4;
        byte[] packed = new byte[SIZE + uuidNum * UUIDItemResult.SIZE + portLength + padLen];
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.uInt32ToBytesL(messageId), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(portLength), 0, packed, 8, 2);
        System.arraycopy(port, 0, packed, 10, portLength);
        packed[10+ portLength +padLen] = ByteArrayUtils.uByteToByte(uuidNum);
        packed[12+ portLength +padLen] = ByteArrayUtils.uByteToByte(reserved1);
        System.arraycopy(ByteArrayUtils.uInt16ToBytesL(reserved2), 0, packed, 12+ portLength +padLen, 2);
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
