package com.nixiedroid.rpc.data.request;

import com.nixiedroid.rpc.data.Header;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder;
import com.nixiedroid.rpc.data.enums.PacketFlagsHolder.PacketFlag;
import com.nixiedroid.rpc.data.enums.PduTypeHolder.PduType;
import com.nixiedroid.rpc.data.payload.PayloadHandler;

public class RequestProcess {
    public static byte[] handle(byte[] data,Header header){
        DataRequest request = RequestProcess.decodeRequest(data, header);
        DataResponse acknowledge = RequestProcess.generateResponse(request);
        return RequestProcess.encode(acknowledge);
    }
    private static DataRequest decodeRequest(byte[] data, Header header) {
        DataRequest request = new DataRequest();
        request.setHeader(header);
        request.deserialize(data,0);
        return request;
    }
    private static DataResponse generateResponse(DataRequest request) {
        DataResponse response = new DataResponse();
        response.payload = PayloadHandler.process(request.payload);
        response.uuidId = request.uuidId;
        int fragLen = Header.SIZE + response.padLen + response.size();
        Header header = new Header.Builder()
                .withMajor(request.getHeader().getMajor())
                .withMinor(request.getHeader().getMinor())
                .withFlags(new PacketFlagsHolder(PacketFlag.FIRSTFRAG,PacketFlag.LASTFRAG))
                .withType(PduType.RESPONSE)
                .withRepresentation(request.getHeader().getRepresentation())
                .withAuthLen(0)
                .withCallId(request.getHeader().getCallId())
                .withFragLen(fragLen).build();
        response.setHeader(header);
        return response;
    }
    private static byte[] encode(DataResponse acknowledge) {
        byte[] answer=new byte[acknowledge.size() + Header.SIZE];
        System.arraycopy(acknowledge.serialize(), 0, answer, Header.SIZE, acknowledge.size());
        System.arraycopy(acknowledge.getHeader().serialize(), 0, answer, 0, acknowledge.getHeader().size());
        return answer;
    }
}
