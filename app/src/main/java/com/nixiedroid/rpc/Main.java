package com.nixiedroid.rpc;

import com.nixiedroid.rpc.AES.AesBlockModeImplementation;
import com.nixiedroid.rpc.util.ByteArrayUtils;
import com.nixiedroid.rpc.util.StringType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private volatile boolean isRunning = true;
    Runnable helloRunnable = new Runnable() {
        public void run() {
            isRunning = false;
            System.out.println("Stopped");
        }
    };
    private ServerSocket serverSocket = null;

    Main() throws IOException {
        files();
    }


    public static void main(String[] args) throws Exception {
        new Main();
        new AesBlockModeImplementation(ByteArrayUtils.fromString(
                "11223344" +
                "55667788" +
                        "9900AABB" +
                        "CCDDEEFF"
        ),11,false).encrypt(ByteArrayUtils.fromString("HELOMYDEARFRIEND", StringType.UTF8));
    }
    private void server(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(helloRunnable, 60, TimeUnit.SECONDS);
        executor.shutdown();
        socket();
    }

    private void socket() {
        try {
            serverSocket = new ServerSocket(8008);
            do {
                Socket clientSocket = serverSocket.accept();
                DataInputStream is = new DataInputStream(clientSocket.getInputStream());
                while (is.available() >= 0) {
                    int i = is.read();
                    if (i != -1) {
                        System.out.println(i + " - " + is.available());
                    }
                }
                clientSocket.close();
            } while (isRunning);
        } catch (IOException e) {
            System.out.println("EXC");
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void print(int data) {
        System.out.printf("%02X\n", data);
    }

    private void files() {
        File f = new File("data.bin");
        System.out.println(f.getAbsoluteFile());
        try (InputStream is = new FileInputStream(f)) {
            System.out.println(is.available());


        } catch (IOException e) {
            throw new Error(e);
        }
    }
}