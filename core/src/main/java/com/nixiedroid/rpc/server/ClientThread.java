package com.nixiedroid.rpc.server;

import com.nixiedroid.rpc.Program;
import com.nixiedroid.rpc.data.ResponseGenerator;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.ByteArrayUtilz;
import com.nixiedroid.rpc.util.StreamUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread extends Thread {
    public static final int TIMEOUT_MILLIS = 500;
    private final BufferedOutputStream out;
    private final BufferedInputStream in;
    private final Socket socket;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedInputStream(socket.getInputStream());
        out = new BufferedOutputStream(socket.getOutputStream());
        start();
    }


    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                byte[] response = process(new DataInputStream(in));
                out.write(response, 0, response.length);
                out.flush();
                waitLoop();
            }
            Program.log().info("Connection closed");
            socket.close();
        } catch (SocketException e) {
            Program.log().err("Receiver unexpectedly closed connection");
            Program.log().err(e.getMessage());
        } catch (IOException e) {
            Program.log().err("Data stream failed");
            Program.log().err(e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void waitLoop() throws IOException {

        Thread wait = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(TIMEOUT_MILLIS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        wait.start();
        while (in.available() == 0 && !isInterrupted()) {
            if (!wait.isAlive()) interrupt();
        }
    }

    private byte[] process(DataInputStream stream) throws IOException {
        byte[] response;
        Program.log().debug("Received data package: " + StreamUtils.toString(stream));
        response = ResponseGenerator.generateResponse(new DataInputStream(stream));
        Program.log().debug("Response data package: " + ByteArrayUtils.toString(response));
        return response;
    }

    public void close() {
        interrupt();
    }
}
