package com.nixiedroid.rpc.data.Bind.dto;


import com.nixiedroid.rpc.data.Data;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.Endiannes;

public class BindRequestACK extends Data {
    public static final int SIZE = 14; //min size
    private int maxTXLen; //little endian unsigned short (2bytes)
    private int maxRXLen;//little endian unsigned short (2bytes)
    private int messageId; //little endian long (4bytes)
    private int portLength; //uint16
    private byte[] port = new byte[5]; // OPTIONAL. Only if addrlength!=0. NUL-terminated string
    private int uuidNum; // LE 4bytes
    private UUIDItemResult[] uuidItemResults;

    public BindRequestACK() {

    }


    public int getMaxTXLen() {
        return maxTXLen;
    }

    public void setMaxTXLen(int maxTXLen) {
        this.maxTXLen = maxTXLen;
    }

    public int getMaxRXLen() {
        return maxRXLen;
    }

    public void setMaxRXLen(int maxRXLen) {
        this.maxRXLen = maxRXLen;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getPortLength() {
        return portLength;
    }

    public void setPortLength(int portLength) {
        this.portLength = portLength;
    }

    public byte[] getPort() {
        return port;
    }

    public void setPort(byte[] port) {
        this.port = port;
    }

    public int getUuidNum() {
        return uuidNum;
    }

    public void setUuidNum(int uuidNum) {
        this.uuidNum = uuidNum;
    }

    public UUIDItemResult[] getUuidItemResults() {
        return uuidItemResults;
    }

    public void setUuidItemResults(UUIDItemResult[] uuidItemResults) {
        this.uuidItemResults = uuidItemResults;
    }

    private int pad(){
        return portLength + (4- (portLength +SIZE) % 4) % 4;
    }
    @Override
    public BindRequestACK deserialize(byte[] data, int start) {
        maxTXLen = ByteArrayUtils.toInt16(data, start, Endiannes.LITTLE);
        maxRXLen = ByteArrayUtils.toInt16(data, start +2,Endiannes.LITTLE);
        messageId = ByteArrayUtils.toInt32(data, start +4,Endiannes.LITTLE);
        portLength = ByteArrayUtils.toInt16(data, start +8,Endiannes.LITTLE);

        if(portLength!=0) System.arraycopy(data, start +10, port, 0, 6);
        uuidNum = ByteArrayUtils.toInt32(data, start +16+pad(),Endiannes.LITTLE);
        for (int i = 0; i < this.uuidNum; i++) {
            this.uuidItemResults[i] = new UUIDItemResult(data,start+ 20 +pad()+i * UUIDItem.SIZE);
        }
        return this;
    }

    @Override
    public byte[] serialize() {
        byte[] packed = new byte[size()];
        System.arraycopy(ByteArrayUtils.toBytes(maxTXLen), 0, packed, 0, 2);
        System.arraycopy(ByteArrayUtils.toBytes(maxRXLen), 0, packed, 2, 2);
        System.arraycopy(ByteArrayUtils.toBytes(messageId), 0, packed, 4, 4);
        System.arraycopy(ByteArrayUtils.toBytes(portLength), 0, packed, 8, 2);
        if(portLength!=0) System.arraycopy(port, 0, packed, 10, portLength);
        System.arraycopy(ByteArrayUtils.toBytes(uuidNum), 0, packed, 10+pad(), 4);
        for (int i = 0; i < uuidNum; i++) {
            System.arraycopy(uuidItemResults[i].serialize(), 0, packed, 14 +pad()+ i * UUIDItemResult.SIZE, UUIDItemResult.SIZE);
        }
        return packed;

    }

    @Override
    public int size() {
        return   SIZE + pad()+ uuidNum * UUIDItemResult.SIZE;
    }
}
