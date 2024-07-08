package com.nixiedroid.rpc.data.Bind;


import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.data.*;
import com.nixiedroid.rpc.data.Bind.dto.BindRequest;
import com.nixiedroid.rpc.data.Bind.dto.BindRequestACK;
import com.nixiedroid.rpc.data.Bind.dto.UUIDItemResult;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.dynamic.Key;
import com.nixiedroid.rpc.util.UUID;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder.PacketFlag;
import com.nixiedroid.rpc.data.enums.PduTypeHolder.PduType;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("CharsetObjectCanBeUsed")
public class Binder {
    public static byte[] handle(byte[] data, Header header){
        BindRequest request = Binder.decodeBindRequest(data, header);
        BindRequestACK acknowledge = Binder.generateResponse(request);
        return Binder.encode(acknowledge);
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
            acknowledge.setPortLength(String.valueOf(Context.settings().getServerPort()).length() + 1);
            byte[] port = new byte[acknowledge.getPortLength()];
            String portStr = Integer.toString(Context.settings().getServerPort());
            byte[] portShort;
            try {
                portShort = portStr.getBytes("UTF-8"); //StandartCharset-s are added to android 4.4
            } catch ( UnsupportedEncodingException e){
                throw new RuntimeException(e);
            }
            System.arraycopy(portShort, 0, port, 0, acknowledge.getPortLength()-1);
            acknowledge.setPort(port);
            fragLen = (36 + request.getUuidNum() * UUIDItemResult.SIZE);
        } else if (reqHeader.getType() == PduType.ALTER_CONTEXT) {
            type = (PduType.ALTER_CONTEXT_RESP) ;
            acknowledge.setPortLength(0);
            fragLen = (32 + request.getUuidNum() * UUIDItemResult.SIZE);
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
        acknowledge.setMaxRXLen(request.getMaxRXLen());
        acknowledge.setMaxTXLen(request.getMaxTXLen());
        acknowledge.setMessageId(0x1063bf3f);
        acknowledge.setUuidNum(request.getUuidNum());
        Map<UUID, UUIDItemResult> prepared = new HashMap<UUID,UUIDItemResult>();
        if (reqHeader.getType() == PduType.BIND) {

            for (int i = 0; i < request.getUuidNum(); i++) {
                if (UUID.cnv(Context.config().getKey(Key.UUID64)).equals(request.getUuidItems()[i].getTransferUUID())) {
                    prepared.put(UUID.cnv(Context.config().getKey(Key.UUID32)),
                            new UUIDItemResult(2, 2,
                                    UUID.cnv(Context.config().getKey(Key.UUIDEmpty)), 0));
                    prepared.put(UUID.cnv(Context.config().getKey(Key.UUID64)),
                            new UUIDItemResult(0, 0,
                                    UUID.cnv(Context.config().getKey(Key.UUID64)), 1));
                    prepared.put(UUID.cnv(Context.config().getKey(Key.UUIDTimeA)),
                            new UUIDItemResult(3, 3,
                                    UUID.cnv(Context.config().getKey(Key.UUIDEmpty)), 0));
                    break;
                } else {
                    prepared.put(UUID.cnv(Context.config().getKey(Key.UUID32)),
                            new UUIDItemResult(0, 0, UUID.cnv(Context.config().getKey(Key.UUID32)), 2));
                    prepared.put(UUID.cnv(Context.config().getKey(Key.UUID64)),
                            new UUIDItemResult(2, 2, UUID.cnv(Context.config().getKey(Key.UUIDEmpty)), 0));
                    prepared.put(UUID.cnv(Context.config().getKey(Key.UUIDTimeA)),
                            new UUIDItemResult(3, 3, UUID.cnv(Context.config().getKey(Key.UUIDEmpty)), 0));
                }
            }

        } else if (reqHeader.getType() == PduType.ALTER_CONTEXT) {
            prepared.put(UUID.cnv(Context.config().getKey(Key.UUID32)),
                    new UUIDItemResult(0, 0, UUID.cnv(Context.config().getKey(Key.UUID32)), 2));
        }
         UUIDItemResult[] uuidItemResults = new UUIDItemResult[acknowledge.getUuidNum()];
        for (int i = 0; i < request.getUuidNum(); i++) {
            UUID tuuid = request.getUuidItems()[i].getTransferUUID();
            UUIDItemResult response = prepared.get(tuuid);
            uuidItemResults[i] = response;
            if (reqHeader.getType() == PduType.ALTER_CONTEXT) {
                uuidItemResults[i] = new UUIDItemResult(0, 0,
                        UUID.cnv(Context.config().getKey(Key.UUID32)), 2);
            }
        }
        acknowledge.setUuidItemResults(uuidItemResults);
        return acknowledge;
    }
    private static byte[] encode(BindRequestACK acknowledge) {
        byte[] answer=new byte[acknowledge.size() + Header.SIZE];
        System.arraycopy(acknowledge.getHeader().serialize(), 0, answer, 0, Header.SIZE);
        System.arraycopy(acknowledge.serialize(), 0, answer, Header.SIZE, acknowledge.size());
        return answer;
    }
}
