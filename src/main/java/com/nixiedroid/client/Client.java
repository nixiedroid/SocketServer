package com.nixiedroid.client;

import com.nixiedroid.data.ClientDataProcess;

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
    public static void main(String[] args) { //for now.
        try (Socket client = new Socket(HOST, PORT)) {
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
        }
    }

    private static void process(byte[] data) throws IOException {
        byte[] response;
        //Program.log().info("Received payloadBind package: " + ByteArrayUtils.toString(payloadBind));
        response = ClientDataProcess.processRequest(data);
        // Program.log().info("Response payloadBind package: " + ByteArrayUtils.toString(response));
        out.write(response, 0, response.length);
        out.flush();
        // Program.log().info("Request - Response sequence end");
    }

    public static void generateRequest() throws IOException {
        byte[] request;
        System.out.println("Generating bind request");
        request = ClientDataProcess.generateRequest();
        out.write(request, 0, request.length);
        out.flush();
    }
}
