package com.nixiedroid;

import java.io.*;
import java.util.Objects;

public class InputReader extends Thread {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public InputReader() {
        start();
    }

    public void run() {
        String input;
        try {
            while (true) {
                input = bufferedReader.readLine();
                System.out.println("Input: " + input);
                if (Objects.equals(input, "stop")) {
                    Program.stop();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
