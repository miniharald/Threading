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
        System.out.println("Enter message: ");
        Scanner scan = new Scanner(System.in);
        msg = scan.nextLine();
        client.sendMsgToServer(msg);
    }
}
