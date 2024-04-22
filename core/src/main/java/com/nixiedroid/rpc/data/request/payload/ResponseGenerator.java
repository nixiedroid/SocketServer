package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;

public class ResponseGenerator {
    static PayloadAck generateResponse(Payload payload){
        PayloadAck ack =  new PayloadAck();
        ack.minor = payload.minor;
        ack.major = payload.major;
        ack.softwareId = Context.generator().getSoftwareID(payload.appUUID, payload.major);
        ack.softwareIdLen = ack.softwareId.length+2;
        ack.cmUUID = payload.cmUUID;
        ack.ackTime = payload.requestTime;
        ack.pingTime = Context.settings().getPingTime();
        ack.delayTime = Context.settings().getDelayTime();
        ack.clientCount = Context.settings().getMinClientCount();
        ack.iv = payload.iv;
        Context.l().verbose("Response: "+ ByteArrayUtils.toString(ack.serialize()));
        return ack;
    }
}
