package com.nixiedroid.server;

import com.nixiedroid.Program;
import com.nixiedroid.data.DataProcess;
import com.nixiedroid.util.ByteArrayUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
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
                byte[] data = new byte[in.available()];
                in.read(data); //Never gets EOF. So ignoring
                byte[] response = process(data);
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
        Thread wait = new Thread(new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
        wait.start();
        while (in.available() == 0 && !isInterrupted()) {
            if (!wait.isAlive()) interrupt();
        }
    }

    private byte[] process(byte[] data) throws IOException {
        byte[] response;
        Program.log().debug("Received data package: " + ByteArrayUtils.toString(data));
        response = DataProcess.processRequest(data);
        Program.log().debug("Response data package: " + ByteArrayUtils.toString(response));
        return response;
    }

    public void close() {
        interrupt();
    }
}
