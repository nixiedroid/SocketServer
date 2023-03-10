package com.nixiedroid.server;

import com.nixiedroid.Program;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public final static LinkedList<ClientThread> clientsList = new LinkedList<>();
    static ServerSocket serverSocket = null;
    static Socket clientSocket = null;

    public static void start() {
        try {
            serverSocket = new ServerSocket(Program.settings().getServerPort());
            Program.log().info("Server started on port: " + Program.settings().getServerPort());
            while (true) {
                clientSocket = serverSocket.accept();
                Program.log().info("Connection Accepted " + clientSocket.getInetAddress() + " : " + clientSocket.getPort());
                ClientThread client = new ClientThread(clientSocket);
                Watchdog wd = new Watchdog(client);
                clientsList.add(client);
            }
        } catch (IOException e) {
            Program.log().err("Server dead!");
            Program.log().err(e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void stop() {
        Program.log().info("Exit");
        if (serverSocket != null) {
            try {
            for (ClientThread client : clientsList) {
                client.close();
            }
            serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
                }

        }
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
}
