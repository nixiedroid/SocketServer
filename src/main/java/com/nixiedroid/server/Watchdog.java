package com.nixiedroid.server;

public class Watchdog extends Thread {
    ClientThread client;
    private boolean isWatching = true;

    public Watchdog(ClientThread client) {
        this.client = client;
        start();
    }

    @Override
    public void run() {
        try {
            while (isWatching) {
                Thread.sleep(2000);
                if (client == null) {
                    isWatching = false;
                    continue;
                }
                if (client.waiting) {
                    client.timerExpired = true;
                    isWatching = false;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
