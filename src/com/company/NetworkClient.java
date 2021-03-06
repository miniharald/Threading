package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkClient implements Runnable, Observer {
    private final String SERVER_IP = "localhost";
    private final int MSG_SIZE = 512;
    private final int SLEEP_MS = 100;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private boolean isRunning = true;

    public NetworkClient(){
        try {
            serverAddress = InetAddress.getByName(SERVER_IP);
            socket = new DatagramSocket();
            socket.setSoTimeout(SLEEP_MS);
        } catch(Exception e){ System.out.println("I konstruktorn: " + e.getMessage()); }
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void sendMsgToServer(String msg) {
        byte[] buffer = msg.getBytes();
        DatagramPacket request = new DatagramPacket(buffer, buffer.length, this.serverAddress, NetworkServer.PORT);
        try {
            socket.send(request);
            //System.out.println("Meddelande skickat!");
        } catch (Exception e) {
            System.out.println("sendMsgToServer: " +  e.getMessage());
        }
    }

    private void receiveMessageFromServer() {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(response);
            String serverMsg = new String(buffer, 0, response.getLength());
            System.out.println(serverMsg); // debugging purpose only!
        } catch (Exception ex) {
            try { Thread.sleep(SLEEP_MS); }
            catch (Exception e) {}
        }
    }
    @Override
    public void run() {
        while (isRunning) {
            receiveMessageFromServer();
        }
        socket.close();
    }

    @Override
    public void update(String msg) {
        System.out.println("Message from server: " + msg);
    }
}

