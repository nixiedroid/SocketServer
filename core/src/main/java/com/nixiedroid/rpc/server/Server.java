package com.nixiedroid.rpc.server;

import com.nixiedroid.rpc.Program;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        server = new Thread(new Runnable() {
            public void run() {
                startServer();
            }
        });
        server.start();
    }


    private void startServer() {
        try {
            serverSocket = new ServerSocket(Program.settings().getServerPort());
            Program.log().info("Server started on port: " + Program.settings().getServerPort());
            do {
                Socket clientSocket = serverSocket.accept();
                Program.log().info("Connection Accepted " + clientSocket.getInetAddress() + " : " + clientSocket.getPort());
                ClientThread client = new ClientThread(clientSocket);
                clientsList.add(client);
            } while (!Thread.currentThread().isInterrupted());
        } catch (IOException e) {
            if (serverSocket.isClosed()) {
                Program.log().info("Server closed");
            } else {
                Program.log().err("Server dead!");
                Program.log().err(e.getMessage());
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
        Program.log().info("Exit");

        for (ClientThread client : clientsList) {
            client.close();
        }
    }
}
