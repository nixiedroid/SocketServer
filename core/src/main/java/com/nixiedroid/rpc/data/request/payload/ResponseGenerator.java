package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.dynamic.Key;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.logger.Logger;

public class ResponseGenerator {
    static PayloadAck generateResponse(Payload payload){
        PayloadAck ack =  new PayloadAck();
        ack.minor = payload.minor;
        ack.major = payload.major;
        ack.softwareId = Context.generator().getSoftwareID(payload.appUUID, payload.major);
        ack.softwareIdLen = ack.softwareId.length+2;
        ack.cmUUID = payload.cmUUID;
        ack.ackTime = payload.requestTime;
        ack.pingTime = Integer.parseInt(Context.config().getKey(Key.PING_TIME));
        ack.delayTime = Integer.parseInt(Context.config().getKey(Key.DELAY_TIME));
        ack.clientCount = Integer.parseInt(Context.config().getKey(Key.CLIENT_COUNT));
        ack.iv = payload.iv;
        Logger.trace("Response: " + ByteArrayUtils.toString(ack.serialize()));
        return ack;
    }
}
