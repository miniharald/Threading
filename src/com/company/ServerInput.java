package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ServerInput {
    NetworkClient client;
    private String msg;

    public ServerInput(NetworkClient client){
        this.client = client;
        inputMsg();
    }

    public void inputMsg() {
        boolean isLooping = true;
        while (isLooping) {
            System.out.println("Enter message: ");
            Scanner scan = new Scanner(System.in);
            if (scan.hasNextLine()) {
                msg = scan.nextLine();
            }
            else {
                isLooping = false;
            }
            if (msg.equals("STOPP")) {
                client.setIsRunning(false);
                isLooping = false;
            } else {
                client.sendMsgToServer(msg);
            }
            System.out.println("Meddelandet var: " + msg +"!");
            System.out.println(msg.length());
        }
    }
}
