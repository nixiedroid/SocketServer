package com.nixiedroid.server;

import com.nixiedroid.Program;
import com.nixiedroid.data.ClientDataProcess;
import com.nixiedroid.util.ByteArrayUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread extends Thread {
    boolean waiting = false;
    boolean timerExpired = false;
    private final BufferedOutputStream out;
    private final BufferedInputStream in;

    public ClientThread(Socket socket) throws IOException {
        in = new BufferedInputStream(socket.getInputStream());
        out = new BufferedOutputStream(socket.getOutputStream());
        start();
    }

    @Override
    public void run() {
        try {
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
            Program.log().info("Connection closed");
        } catch (SocketException e) {
            Program.log().err(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(byte[] data) throws IOException {
        byte[] response;
        Program.log().debug("Received data package: " + ByteArrayUtils.toString(data));
        response = ClientDataProcess.processRequest(data);
        Program.log().debug("Response data package: " + ByteArrayUtils.toString(response));
        out.write(response, 0, response.length);
        out.flush();
        Program.log().debug("Request - Response sequence end");
    }

    public void close() {
        timerExpired = true;
    }
}
