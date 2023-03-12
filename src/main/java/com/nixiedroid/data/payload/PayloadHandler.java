package com.nixiedroid.data.payload;

import com.nixiedroid.Program;

public class PayloadHandler {
   public static byte[] process(byte [] data){
       byte[] chunk = new byte[data.length - GenericPayload.SIZE];
       GenericPayload header = new GenericPayload(data);
       System.arraycopy(data, GenericPayload.SIZE, chunk, 0,chunk.length );
       if (header.major == 6){
         return V5.handle(chunk,header,true);
       } else
       if (header.major == 5) {
           return V5.handle(chunk,header,false);
       } else
       if (header.major == 4) {
           return V4.handle(chunk, header);
       }
       if (header.major == 0){
           Program.log().verbose("Version is zero. This is, probably bug. Rehandling");
          return V0.handle(data);
       }
       else return Unknown.handle();
     //  return Unknown.handle();
   }

   }
