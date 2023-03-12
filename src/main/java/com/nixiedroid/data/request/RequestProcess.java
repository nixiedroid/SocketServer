package com.nixiedroid.data.request;

import com.nixiedroid.data.Header;
import com.nixiedroid.data.enums.PacketFlags;
import com.nixiedroid.data.enums.RequestTypes;
import com.nixiedroid.data.payload.PayloadHandler;

public class RequestProcess {
    public static byte[] handle(byte[] data,Header header){
        DataRequest request = RequestProcess.decodeRequest(data, header);
        DataResponse acknowledge = RequestProcess.generateResponse(request);
        return RequestProcess.encode(acknowledge);
    }
    private static DataRequest decodeRequest(byte[] data, Header header) {
        DataRequest request = new DataRequest();
        request.setHeader(header);
        request.unpack(data);
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
                .withFlags(PacketFlags.FIRSTFRAG.get() | PacketFlags.LASTFRAG.get())
                .withType(RequestTypes.TYPE_RESPONSE.getType())
                .withRepresentation(request.getHeader().getRepresentation())
                .withAuthLen(0)
                .withCallId(request.getHeader().getCallId())
                .withFragLen(fragLen).build();
        response.setHeader(header);
        return response;
    }
    private static byte[] encode(DataResponse acknowledge) {
        byte[] answer=new byte[acknowledge.size() + Header.SIZE];
        System.arraycopy(acknowledge.pack(), 0, answer, Header.SIZE, acknowledge.size());
        System.arraycopy(acknowledge.getHeader().pack(), 0, answer, 0, acknowledge.getHeader().size());
        return answer;
    }
}
