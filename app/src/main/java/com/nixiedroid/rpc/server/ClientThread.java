package com.nixiedroid.rpc.server;

import com.nixiedroid.rpc.data.Header;
import com.nixiedroid.rpc.data.ResponseGenerator;
import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.logger.Logger;

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
                byte[] response;
                try {
                    Header header = getHeader(in);
                    int dataSize = header.getFragLen() - Header.SIZE;
                    byte[] data = new byte[dataSize];
                    if (in.read(data) != dataSize) throw new EOFException();
                    response = process(data,header);
                } catch (EOFException e) {
                    response = new byte[0];
                }


                out.write(response, 0, response.length);
                out.flush();
                waitLoop();
            }
            Logger.info("Connection closed");
            socket.close();
        } catch (SocketException e) {
            Logger.err("Receiver unexpectedly closed connection");
            Logger.err(e.getMessage());
        } catch (IOException e) {
            Logger.err("Data stream failed");
            Logger.err(e.getMessage());
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

    private Header getHeader(BufferedInputStream inputStream) throws IOException {
        byte[] headerBytes = new byte[Header.SIZE];
        for (int i = 0; i < headerBytes.length; i++) {
            int avail = inputStream.available();
            if (avail < 1) throw new EOFException("Too few Data");
            int bite = inputStream.read();
            if (bite == -1) throw new EOFException("Too few Data");
            headerBytes[i] = (byte) (bite & 0xFF);
        }
        return new Header(headerBytes);
    }

    private byte[] process(byte[] data,Header header) throws IOException {
        byte[] response;
        Logger.debug("Received data package: " + ByteArrayUtils.toString(data));
        response = ResponseGenerator.generateResponse(data,header);
        Logger.debug("Response data package: " + ByteArrayUtils.toString(response));
        return response;
    }

    public void close() {
        interrupt();
    }
}
