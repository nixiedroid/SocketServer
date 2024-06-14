package com.nixiedroid.rpc.client;

import com.nixiedroid.rpc.data.ClientDataProcess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static final int PORT = 8066;
    public static final String HOST = "192.168.0.67";
    static boolean waiting = false;
    static boolean timerExpired = false;
    private static BufferedOutputStream out;
    private static BufferedInputStream in;


    //Bind, after that generate request
    public static void main(String[] args) {
        Socket client = null; //for now.
        try {
            client = new Socket(HOST, PORT);
            in = new BufferedInputStream(client.getInputStream());
            out = new BufferedOutputStream(client.getOutputStream());
            generateRequest();
            while (!timerExpired) {
                byte[] data = new byte[0];
                if (in.available() == 0) {
                    waiting = true;
                    continue;
                }
                waiting = false;
                while (in.available() != 0) {
                    data = new byte[in.available()];
                    in.read(data); //Never gets EOF. So ignoring
                }
                process(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void process(byte[] data) throws IOException {
        byte[] response;
        //Context.log().info("Received bindRequestHeader package: " + ByteArrayUtils.toString(bindRequestHeader));
        response = ClientDataProcess.processRequest(data);
        // Context.log().info("Response bindRequestHeader package: " + ByteArrayUtils.toString(response));
        out.write(response, 0, response.length);
        out.flush();
        // Context.log().info("Request - Response sequence end");
    }

    public static void generateRequest() throws IOException {
        byte[] request;
        System.out.println("Generating bind request");
        request = ClientDataProcess.generateRequest();
        out.write(request, 0, request.length);
        out.flush();
    }
}
