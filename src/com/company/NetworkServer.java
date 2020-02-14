package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkServer implements Runnable {
    public static final int PORT = 80;
    private final int SLEEP_MS = 100;
    private final int MSG_SIZE = 512;
    private DatagramSocket socket;

    public NetworkServer(){
        try {
            socket = new DatagramSocket(PORT);
            socket.setSoTimeout(SLEEP_MS);
        } catch(SocketException e){ System.out.println(e.getMessage()); }
    }



    public void sendMsgToClient(String msg, SocketAddress clientSocketAddress) {
        byte[] buffer = msg.getBytes();

        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientSocketAddress);

        try { socket.send(response); }
        catch (Exception e) { e.printStackTrace(); }
    }
    @Override
    public void run() {
        AtomicBoolean isRunning = new AtomicBoolean(true);
        while (isRunning.get()) {
            DatagramPacket clientRequest = new DatagramPacket(new byte[MSG_SIZE], MSG_SIZE);

            if (!receiveMsgFromAnyClient(clientRequest)) {
                continue;
            }
            else{
                System.out.println("New message recieved! " + clientRequest);
            }
            //List<String> queuedMessages = new ArrayList<>();
            String clientMsg = new String(clientRequest.getData(), 0, clientRequest.getLength());

            // TODO: Save the msg to a queue instead
        }
    }

    private boolean receiveMsgFromAnyClient(DatagramPacket clientRequest){
        try { socket.receive(clientRequest);
            System.out.println("Alex connected!");
            System.out.println(clientRequest);
        }
        catch (Exception ex) { return false; }
        return true;
    }
}
