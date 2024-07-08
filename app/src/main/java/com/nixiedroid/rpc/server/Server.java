package com.nixiedroid.rpc.server;

import com.nixiedroid.rpc.Context;
import com.nixiedroid.rpc.util.logger.Logger;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;

public class Server {
    private static final Server instance = new Server();
    private final LinkedList<ClientThread> clientsList = new LinkedList<ClientThread>();
    private ServerSocket serverSocket = null;
    private Thread server;

    private Server() {
    }

    public static Server getInstance() { //Singleton pattern
        return instance;
    }

    public void startServerThread() {//Starting new thread, which will actually do the job
        server = new Thread(this::startServer);
        server.start();
    }


    private void startServer() {
        try {
            int port = Context.settings().getServerPort();
            InetAddress.getByAddress(new byte[4]);
            serverSocket = new ServerSocket(port);
            Logger.info("Server started on port: " + Context.settings().getServerPort());
            do {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSendBufferSize(5840);
                clientSocket.setReceiveBufferSize(5840);
                clientSocket.setSoTimeout(500);
                Logger.info("Connection Accepted " + clientSocket.getInetAddress() + " : " + clientSocket.getPort());
                ClientThread client = new ClientThread(clientSocket);
                clientsList.add(client);
            } while (!Thread.currentThread().isInterrupted());
        } catch (IOException e) {
            if (serverSocket.isClosed()) {
                Logger.info("Server closed");
            } else {
                Logger.err("Server dead!");
                Logger.err(e.getMessage());
            }

        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stop() {
        server.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Logger.info("Exit");

        for (ClientThread client : clientsList) {
            client.close();
        }
    }
}
