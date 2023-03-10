package com.nixiedroid.payload;

import com.nixiedroid.Program;
import com.nixiedroid.util.ByteArrayUtils;
import com.nixiedroid.util.UUID;

public class ResponseGenerator {
    static PayloadAck generateResponse(Payload payload){
        PayloadAck ack =  new PayloadAck();
        ack.minor = payload.minor;
        ack.major = payload.major;
        ack.softwareId = Program.generator().getSoftwareID(new UUID(payload.appUUID), payload.major);
        ack.softwareIdLen = ack.softwareId.length+2;
        ack.cmUUID = payload.cmUUID;
        ack.ackTime = payload.requestTime;
        ack.pingTime = Program.settings().getPingTime();
        ack.delayTime = Program.settings().getDelayTime();
        ack.clientCount = Program.settings().getMinClientCount();
        ack.iv = payload.iv;
        Program.log().verbose("Response: "+ ByteArrayUtils.toString(ack.pack()));
        return ack;
    }
}
