package com.nixiedroid.rpc.data.request.payload;

import com.nixiedroid.rpc.Context;

public class PayloadHandler {
   public static byte[] process(byte [] data){
       GenericPayload header = new GenericPayload(data);
       byte[] chunk = new byte[data.length - header.size()];
       System.arraycopy(data, header.size(), chunk, 0,chunk.length );
       if (header.major == 6){
         return V5.handle(chunk,header,true);
       } else
       if (header.major == 5) {
           return V5.handle(chunk,header,false);
       } else
       if (header.major == 4) {
           return V4.handle(chunk, header);
       }
       return Unknown.handle();
   }

   }
