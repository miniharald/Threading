package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Program implements Observer{

    private String msg;
    private NetworkClient client;
    List<String> queue = new ArrayList<>();
    NetworkServer server = new NetworkServer();

    Program() {
        server.addObserver(this);
        Thread thread1 = new Thread(server, "server");
        thread1.setPriority(10);
        thread1.start();
        client = new NetworkClient();
        Thread thread2 = new Thread(client, "klient");
        thread2.start();
        inputMsg();
    }

    private void inputMsg() {
        boolean isLooping = true;
        while (isLooping) {
            System.out.println("Enter message (Write STOP to exit): ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextLine()) {
                msg = scan.nextLine();
            }
            else {
                isLooping = false;
            }
            if (msg.equals("STOP")) {
                client.setIsRunning(false);
                isLooping = false;
                System.exit(0);
            } else {
                client.sendMsgToServer(msg);
            }
            //System.out.println("Meddelandet var: " + msg +"!");
            //System.out.println(msg.length());
        }
    }

    @Override
    public void update(String msg) {
        System.out.println("(P) Message from server: " + msg);
    }
}
