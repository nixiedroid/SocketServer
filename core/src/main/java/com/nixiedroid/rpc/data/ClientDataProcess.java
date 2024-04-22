package com.nixiedroid.rpc.data;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.data.Bind.Binder;
import com.nixiedroid.rpc.data.Bind.dto.BindRequest;
import com.nixiedroid.rpc.data.Bind.dto.UUIDItem;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder.PacketFlag;
import com.nixiedroid.rpc.data.enums.PduTypeHolder.PduType;
import com.nixiedroid.rpc.data.request.RequestProcess;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.UUID;

public class ClientDataProcess {
    public static byte[] generateRequest(){
        byte[] requestBytes = new byte[8];
        UUIDItem item1 = new UUIDItem.Builder()
                .withCtxID(0)
                .withItems(1)
                .withAbstract(UUID.cnv(Context.config().getKey("ABSTRACTUUID")))
                .withAbstractVer(1)
                .withTransfer(UUID.cnv(Context.config().getKey("UUID32")))
                .withTransferVer(2)
                .build();
        UUIDItem item2 = new UUIDItem.Builder()
                .withCtxID(1)
                .withItems(1)
                .withAbstract(UUID.cnv(Context.config().getKey("ABSTRACTUUID")))
                .withAbstractVer(1)
                .withTransfer(UUID.cnv(Context.config().getKey("UUIDTime")))
                .withTransferVer(1)
                .build();
        UUIDItem[] items = new UUIDItem[2];
        items[0] = item1;
        items[1] = item2;
        Header header = new Header.Builder()
                .withMajor(5)
                .withMinor(0)
                .withType(PduType.REQUEST)
                .withFlags(new PacketFlagsHolder(PacketFlag.FIRSTFRAG,PacketFlag.LASTFRAG,PacketFlag.MULTIPLEX))
                .withRepresentation(0x10)
                .withCallId(2) //TODO wrong number
                .withAuthLen((short) 0)
                .withFragLen((short) 250)
                .build();

        BindRequest request = new BindRequest.Builder()
                .withTXLen(5840)
                .withRXLen(5840)
                .withUuidNum(2)
                .withMessageID(0)
                .withUuidItems(items)
                .withHeader(header)
                .build();
      //  request.bindRequestHeader = new BindRequestHeader();
        requestBytes = request.serialize();

        return requestBytes;
    }
    public static byte[] processRequest(byte[] data) {
        if ((data.length - Header.SIZE)<0) return ByteArrayUtils.fromString("BAAD"); //TODO
        byte[] chunk = new byte[data.length - Header.SIZE];
        Header header = new Header(data);
        System.arraycopy(data, Header.SIZE, chunk, 0, chunk.length);
        if (header.getType() == PduType.ALTER_CONTEXT || header.getType()  == PduType.BIND) {
            return Binder.handle(chunk, header);
        } else
        if (header.getType()  == PduType.REQUEST) {
            return RequestProcess.handle(chunk, header);
        } else {
            return "Nothing Interesting".getBytes();
        }
    }
}
