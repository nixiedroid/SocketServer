package com.nixiedroid.rpc.data;

import com.nixiedroid.rpc.Program;
import com.nixiedroid.rpc.data.Bind.BindProcess;
import com.nixiedroid.rpc.data.Bind.PayloadBind;
import com.nixiedroid.rpc.data.Bind.DataBind;
import com.nixiedroid.rpc.data.Bind.UUIDItem;
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
                .withPadding(0)
                .withAbstract(UUID.cnv(Program.config().getAbstractUuid()))
                .withAbstractVer(1)
                .withTransfer(UUID.cnv(Program.config().getUuid32()))
                .withTransferVer(2)
                .build();
        UUIDItem item2 = new UUIDItem.Builder()
                .withCtxID(1)
                .withItems(1)
                .withPadding(0)
                .withAbstract(UUID.cnv(Program.config().getAbstractUuid()))
                .withAbstractVer(1)
                .withTransfer(UUID.cnv(Program.config().getUuidTime()))
                .withTransferVer(1)
                .build();
        UUIDItem[] items = new UUIDItem[2];
        items[0] = item1;
        items[1] = item2;
        PayloadBind payload = new PayloadBind.Builder()
                .withTXLen(5840)
                .withRXLen(5840)
                .withUuidNum(2)
                .withMessageID(0)
                .withUuidItems(items)
                .build();
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

        DataBind request = new DataBind.Builder()
                .withData(payload)
                .withHeader(header)
                .build();
      //  request.payloadBind = new PayloadBind();
        requestBytes = request.serialize();

        return requestBytes;
    }
    public static byte[] processRequest(byte[] data) {
        if ((data.length - Header.SIZE)<0) return ByteArrayUtils.fromString("BAAD"); //TODO
        byte[] chunk = new byte[data.length - Header.SIZE];
        Header header = new Header(data);
        System.arraycopy(data, Header.SIZE, chunk, 0, chunk.length);
        if (header.getType() == PduType.ALTER_CONTEXT || header.getType()  == PduType.BIND) {
            return BindProcess.handle(chunk, header);
        } else
        if (header.getType()  == PduType.REQUEST) {
            return RequestProcess.handle(chunk, header);
        } else {
            return "Nothing Interesting".getBytes();
        }
    }
}
