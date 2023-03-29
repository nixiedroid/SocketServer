package com.nixiedroid.data;

import com.nixiedroid.Program;
import com.nixiedroid.data.Bind.BindProcess;
import com.nixiedroid.data.Bind.PayloadBind;
import com.nixiedroid.data.Bind.DataBind;
import com.nixiedroid.data.Bind.UUIDItem;
import com.nixiedroid.data.Header;
import com.nixiedroid.data.enums.PacketFlags;
import com.nixiedroid.data.enums.RequestTypes;
import com.nixiedroid.data.request.RequestProcess;
import com.nixiedroid.util.ByteArrayUtils;
import com.nixiedroid.util.UUID;

import java.nio.charset.StandardCharsets;

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
                .withType(RequestTypes.TYPE_REQUEST.getType())
                .withFlags(PacketFlags.FIRSTFRAG.get() | PacketFlags.LASTFRAG.get() | PacketFlags.MULTIPLEX.get())
                .withRepresentation(0x10)
                .withCallId(2) //TODO wrong number
                .withAuthLen(0)
                .withFragLen(250)
                .build();

        DataBind request = new DataBind.Builder()
                .withData(payload)
                .withHeader(header)
                .build();
      //  request.payloadBind = new PayloadBind();
        requestBytes = request.pack();

        return requestBytes;
    }
    public static byte[] processRequest(byte[] data) {
        if ((data.length - Header.SIZE)<0) return ByteArrayUtils.fromHexString("BAAD"); //TODO
        byte[] chunk = new byte[data.length - Header.SIZE];
        Header header = new Header(data);
        System.arraycopy(data, Header.SIZE, chunk, 0, chunk.length);
        if (header.getType() == RequestTypes.TYPE_ALTERCTX.getType() || header.getType()  == RequestTypes.TYPE_BIND.getType()) {
            return BindProcess.handle(chunk, header);
        } else
        if (header.getType()  == RequestTypes.TYPE_REQUEST.getType()) {
            return RequestProcess.handle(chunk, header);
        } else {
            return "Nothing Interesting".getBytes();
        }
    }
}
