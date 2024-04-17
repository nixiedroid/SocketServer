package com.nixiedroid.rpc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket s = null;
        try {
            s = new Socket("localhost",8008);
            //s.bind();
            OutputStream os =  s.getOutputStream();
            for (int i = 0; i < 8192; i++) {
                os.write(55);
            }
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
