package com.nixiedroid.data.Bind;


import com.nixiedroid.Program;
import com.nixiedroid.data.*;
import com.nixiedroid.util.UUID;
import com.nixiedroid.data.enums.PacketFlags;
import com.nixiedroid.data.enums.RequestTypes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BindProcess {
    public static byte[] handle(byte[] data, Header header){
        DataBind request = BindProcess.decodeBindRequest(data, header);
        DataBindACK acknowledge = BindProcess.generateResponse(request);
        return BindProcess.encode(acknowledge);
    }
    private static DataBind decodeBindRequest(byte[] data, Header header) {
        DataBind request = new DataBind();
        request.payloadBind = new PayloadBind();
        request.setHeader(header);
        request.unpack(data);
        return request;
    }
    private static DataBindACK generateResponse(DataBind request) {
        DataBindACK acknowledge = new DataBindACK();
        acknowledge.setHeader(new Header());
        Header reqHeader = request.getHeader();
        byte flags = ((byte) (PacketFlags.FIRSTFRAG.get() | PacketFlags.LASTFRAG.get()));
        int type;
        int fragLen;
        if (reqHeader.getType() == RequestTypes.TYPE_BIND.getType()) {
            type = (RequestTypes.TYPE_BINDACK.getType());
            flags |= reqHeader.getFlags() & PacketFlags.MULTIPLEX.get();
            acknowledge.portLength = String.valueOf(Program.settings().getServerPort()).length() + 1;
            byte[] port = new byte[acknowledge.portLength];
            String portStr = Integer.toString(Program.settings().getServerPort());
            byte[] portShort;
            try {
                portShort = portStr.getBytes("UTF-8"); //StandartCharset-s are added to android 4.4
            } catch ( UnsupportedEncodingException e){
                throw new RuntimeException(e);
            }
            System.arraycopy(portShort, 0, port, 0, acknowledge.portLength -1);
            acknowledge.port = port;
            fragLen = (36 + request.payloadBind.uuidNum * UUIDItemResult.SIZE);
        } else if (reqHeader.getType() == RequestTypes.TYPE_ALTERCTX.getType()) {
            type = (RequestTypes.TYPE_ALTERCTX_ACK.getType());
            acknowledge.portLength = 0;
            fragLen = (32 + request.payloadBind.uuidNum * UUIDItemResult.SIZE);
        } else throw new IllegalArgumentException("Unknown request type for bind like handler: " + reqHeader.getType());
        Header ackHeader = new Header.Builder()
                .withMajor(reqHeader.getMajor())
                .withMinor(reqHeader.getMinor())
                .withFlags(flags)
                .withType(type)
                .withRepresentation(reqHeader.getRepresentation())
                .withAuthLen(reqHeader.getAuthLen())
                .withCallId(reqHeader.getCallId())
                .withFragLen(fragLen).build();

        acknowledge.setHeader(ackHeader);
        acknowledge.maxTXLen = request.payloadBind.maxTXLen;
        acknowledge.maxRXLen = request.payloadBind.maxRXLen;
        acknowledge.messageId = 0x1063bf3f;
        acknowledge.uuidNum = request.payloadBind.uuidNum;
        Map<UUID, UUIDItemResult> prepared = new HashMap<>();
        if (reqHeader.getType() == RequestTypes.TYPE_BIND.getType()) {

            for (int i = 0; i < request.payloadBind.uuidNum; i++) {
                if (UUID.cnv(Program.config().getUuid64()).equals(request.payloadBind.uuidItems[i].getTransferUUID())) {
                    prepared.put(UUID.cnv(Program.config().getUuid32()), new UUIDItemResult(2, 2, UUID.cnv(Program.config().getUuidEmpty()), 0));
                    prepared.put(UUID.cnv(Program.config().getUuid64()), new UUIDItemResult(0, 0, UUID.cnv(Program.config().getUuid64()), 1));
                    prepared.put(UUID.cnv(Program.config().getUuidTimeA()), new UUIDItemResult(3, 3, UUID.cnv(Program.config().getUuidEmpty()), 0));
                    break;
                } else {
                    prepared.put(UUID.cnv(Program.config().getUuid32()), new UUIDItemResult(0, 0, UUID.cnv(Program.config().getUuid32()), 2));
                    prepared.put(UUID.cnv(Program.config().getUuid64()), new UUIDItemResult(2, 2, UUID.cnv(Program.config().getUuidEmpty()), 0));
                    prepared.put(UUID.cnv(Program.config().getUuidTimeA()), new UUIDItemResult(3, 3, UUID.cnv(Program.config().getUuidEmpty()), 0));
                }
            }

        } else if (reqHeader.getType() == RequestTypes.TYPE_ALTERCTX.getType()) {
            prepared.put(UUID.cnv(Program.config().getUuid32()), new UUIDItemResult(0, 0, UUID.cnv(Program.config().getUuid32()), 2));
        }
        acknowledge.uuidItemResults = new UUIDItemResult[acknowledge.uuidNum];
        for (int i = 0; i < request.payloadBind.uuidNum; i++) {
            UUID tuuid = request.payloadBind.uuidItems[i].getTransferUUID();
            UUIDItemResult response = prepared.get(tuuid);
            acknowledge.uuidItemResults[i] = response;
            if (reqHeader.getType() == RequestTypes.TYPE_ALTERCTX.getType()) {
                acknowledge.uuidItemResults[i] = new UUIDItemResult(0, 0, UUID.cnv(Program.config().getUuid32()), 2);
            }
        }
        return acknowledge;
    }
    private static byte[] encode(DataBindACK acknowledge) {
        byte[] answer=new byte[acknowledge.size() + Header.SIZE];
        System.arraycopy(acknowledge.pack(), 0, answer, Header.SIZE, acknowledge.size());
        System.arraycopy(acknowledge.getHeader().pack(), 0, answer, 0, acknowledge.getHeader().size());

        return answer;
    }
}
