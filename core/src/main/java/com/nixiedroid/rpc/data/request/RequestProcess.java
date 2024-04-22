package com.nixiedroid.rpc.data.request;

import com.nixiedroid.rpc.data.Header;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder.PacketFlag;
import com.nixiedroid.rpc.data.enums.PduTypeHolder.PduType;
import com.nixiedroid.rpc.data.request.payload.PayloadHandler;

public class RequestProcess {
    public static byte[] handle(byte[] data,Header header){
        Request request = RequestProcess.decodeRequest(data, header);
        Response acknowledge = RequestProcess.generateResponse(request);
        return RequestProcess.encode(acknowledge);
    }
    private static Request decodeRequest(byte[] data, Header header) {
        Request request = new Request();
        request.setHeader(header);
        request.deserialize(data,0);
        return request;
    }
    private static Response generateResponse(Request request) {
        Response response = new Response();

        response.payload = PayloadHandler.process(request.payload);

        response.uuidId = request.uuidId;
        response.setHeader(new Header.Builder()
                .withMajor(request.getHeader().getMajor())
                .withMinor(request.getHeader().getMinor())
                .withFlags(new PacketFlagsHolder(PacketFlag.FIRSTFRAG,PacketFlag.LASTFRAG))
                .withType(PduType.RESPONSE)
                .withRepresentation(request.getHeader().getRepresentation())
                .withAuthLen(0)
                .withCallId(request.getHeader().getCallId())
                .withFragLen(Header.SIZE + response.size())// + response.padLen
                .build());
        return response;
    }
    private static byte[] encode(Response acknowledge) {
        byte[] answer=new byte[acknowledge.size() + Header.SIZE];
        System.arraycopy(acknowledge.serialize(), 0, answer, Header.SIZE, acknowledge.size());
        System.arraycopy(acknowledge.getHeader().serialize(), 0, answer, 0, acknowledge.getHeader().size());
        return answer;
    }
}
