package com.nixiedroid.rpc.data.enums;


import java.util.EnumMap;
import java.util.Map;

public final class PduTypeHolder {

    private static final EnumMap<PduType, Integer> map = new EnumMap<PduType, Integer>(PduType.class);

    static {
        map.put(PduType.REQUEST, 0);
        map.put(PduType.PING, 1);
        map.put(PduType.RESPONSE, 2);
        map.put(PduType.FAULT, 3);
        map.put(PduType.WORKING, 4);
        map.put(PduType.NOCALL, 5);
        map.put(PduType.REJECT, 6);
        map.put(PduType.ACK, 7);
        map.put(PduType.CL_CANCEL, 8);
        map.put(PduType.FACK, 9);
        map.put(PduType.CANCELACK, 10);
        map.put(PduType.BIND, 11);
        map.put(PduType.BINDACK, 12);
        map.put(PduType.BINDNAK, 13);
        map.put(PduType.ALTER_CONTEXT, 14);
        map.put(PduType.ALTER_CONTEXT_RESP, 15);
        map.put(PduType.AUTH3, 16);
        map.put(PduType.SHUTDOWN, 17);
        map.put(PduType.CO_CANCEL, 18);
        map.put(PduType.ORPHANED, 19);
    }

    public static int get(PduType key) {
        return map.get(key);
    }

    public static PduType get(int i) {
        for (Map.Entry<PduType, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(i)) return entry.getKey();
        }
        return null;
    }
 
    public enum PduType {
        REQUEST, PING, RESPONSE, FAULT, WORKING, NOCALL, REJECT, ACK, CL_CANCEL, FACK, CANCELACK, BIND, BINDACK, BINDNAK, ALTER_CONTEXT, ALTER_CONTEXT_RESP, AUTH3, SHUTDOWN, CO_CANCEL, ORPHANED;
    }

}


