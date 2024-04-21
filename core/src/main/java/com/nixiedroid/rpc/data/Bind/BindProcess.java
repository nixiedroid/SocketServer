package com.nixiedroid.rpc.data.Bind;


import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.data.*;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.util.UUID;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder.PacketFlag;
import com.nixiedroid.rpc.data.enums.PduTypeHolder.PduType;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BindProcess {
    public static byte[] handle(byte[] data, Header header){
        BindRequest request = BindProcess.decodeBindRequest(data, header);
        BindRequestACK acknowledge = BindProcess.generateResponse(request);
        return BindProcess.encode(acknowledge);
    }
    private static BindRequest decodeBindRequest(byte[] data, Header header) {
        BindRequest request = new BindRequest();
        request.setHeader(header);
        request.deserialize(data,0);
        return request;
    }
    private static BindRequestACK generateResponse(BindRequest request) {
        BindRequestACK acknowledge = new BindRequestACK();
        acknowledge.setHeader(new Header());
        Header reqHeader = request.getHeader();
        PacketFlagsHolder flags = new PacketFlagsHolder(
                PacketFlag.FIRSTFRAG,
                PacketFlag.LASTFRAG
        );
        PduType type;
        int fragLen;
        if (reqHeader.getType() == PduType.BIND) {
            type = (PduType.BINDACK);
            flags.add(PacketFlag.MULTIPLEX);
            flags.addAll(reqHeader.getFlags().get());
            acknowledge.portLength = String.valueOf(Context.settings().getServerPort()).length() + 1;
            byte[] port = new byte[acknowledge.portLength];
            String portStr = Integer.toString(Context.settings().getServerPort());
            byte[] portShort;
            try {
                portShort = portStr.getBytes("UTF-8"); //StandartCharset-s are added to android 4.4
            } catch ( UnsupportedEncodingException e){
                throw new RuntimeException(e);
            }
            System.arraycopy(portShort, 0, port, 0, acknowledge.portLength-1);
            acknowledge.port = port;
            fragLen = (36 + request.uuidNum * UUIDItemResult.SIZE);
        } else if (reqHeader.getType() == PduType.ALTER_CONTEXT) {
            type = (PduType.ALTER_CONTEXT_RESP) ;
            acknowledge.portLength = 0;
            fragLen = (32 + request.uuidNum * UUIDItemResult.SIZE);
        } else throw new IllegalArgumentException("Unknown request type for bind like handler: " + reqHeader.getType());
        Header ackHeader = new Header.Builder()
                .withMajor(reqHeader.getMajor())
                .withMinor(reqHeader.getMinor())
                .withFlags(flags)
                .withType(type)
                .withRepresentation(reqHeader.getRepresentation())
                .withAuthLen(reqHeader.getAuthLen())
                .withCallId(reqHeader.getCallId())
                .withFragLen((short) fragLen).build();

        acknowledge.setHeader(ackHeader);
        acknowledge.maxTXLen = request.maxTXLen;
        acknowledge.maxRXLen = request.maxRXLen;
        acknowledge.messageId = 0x1063bf3f;
        acknowledge.uuidNum = request.uuidNum;
        Map<UUID, UUIDItemResult> prepared = new HashMap<UUID,UUIDItemResult>();
        if (reqHeader.getType() == PduType.BIND) {

            for (int i = 0; i < request.uuidNum; i++) {
                if (UUID.cnv(Context.config().getUuid64()).equals(request.uuidItems[i].getTransferUUID())) {
                    prepared.put(UUID.cnv(Context.config().getUuid32()), new UUIDItemResult(2, 2, UUID.cnv(Context.config().getUuidEmpty()), 0));
                    prepared.put(UUID.cnv(Context.config().getUuid64()), new UUIDItemResult(0, 0, UUID.cnv(Context.config().getUuid64()), 1));
                    prepared.put(UUID.cnv(Context.config().getUuidTimeA()), new UUIDItemResult(3, 3, UUID.cnv(Context.config().getUuidEmpty()), 0));
                    break;
                } else {
                    prepared.put(UUID.cnv(Context.config().getUuid32()), new UUIDItemResult(0, 0, UUID.cnv(Context.config().getUuid32()), 2));
                    prepared.put(UUID.cnv(Context.config().getUuid64()), new UUIDItemResult(2, 2, UUID.cnv(Context.config().getUuidEmpty()), 0));
                    prepared.put(UUID.cnv(Context.config().getUuidTimeA()), new UUIDItemResult(3, 3, UUID.cnv(Context.config().getUuidEmpty()), 0));
                }
            }

        } else if (reqHeader.getType() == PduType.ALTER_CONTEXT) {
            prepared.put(UUID.cnv(Context.config().getUuid32()), new UUIDItemResult(0, 0, UUID.cnv(Context.config().getUuid32()), 2));
        }
        acknowledge.uuidItemResults = new UUIDItemResult[acknowledge.uuidNum];
        for (int i = 0; i < request.uuidNum; i++) {
            UUID tuuid = request.uuidItems[i].getTransferUUID();
            UUIDItemResult response = prepared.get(tuuid);
            acknowledge.uuidItemResults[i] = response;
            if (reqHeader.getType() == PduType.ALTER_CONTEXT) {
                acknowledge.uuidItemResults[i] = new UUIDItemResult(0, 0, UUID.cnv(Context.config().getUuid32()), 2);
            }
        }
        return acknowledge;
    }
    private static byte[] encode(BindRequestACK acknowledge) {
        byte[] answer=new byte[acknowledge.size() + Header.SIZE];
        System.arraycopy(acknowledge.getHeader().serialize(), 0, answer, 0, Header.SIZE);
        System.arraycopy(acknowledge.serialize(), 0, answer, Header.SIZE, acknowledge.size());
        return answer;
    }
}
