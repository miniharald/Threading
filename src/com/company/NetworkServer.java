package com.company;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkServer implements Runnable {
    static final int PORT = 80;
    private final int SLEEP_MS = 100;
    private final int MSG_SIZE = 512;
    List<String> queue = new ArrayList<>();
    List<Observer> observers = new ArrayList<>();
    List<DatagramPacket> clients = new ArrayList<>();
    private DatagramSocket socket;

    NetworkServer(){
        try {
            socket = new DatagramSocket(PORT);
            socket.setSoTimeout(SLEEP_MS);
        } catch(SocketException e){ System.out.println(e.getMessage()); }
    }

    private void sendMsgToClient(String msg, SocketAddress clientSocketAddress) {
        byte[] buffer = msg.getBytes();

        DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientSocketAddress);

        try { socket.send(response); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getQueue() {
        return queue;
    }

    public void addObserver(Observer object) {
        observers.add(object);
    }

    public void removeObserver(Observer object) {
        observers.remove(object);
    }

    private void notifyObservers(String msg) {
        observers.forEach(o -> o.update(msg));
        //System.out.println("notifyObservers: New message");
    }

    @Override
    public void run() {
        AtomicBoolean isRunning = new AtomicBoolean(true);
        while (isRunning.get()) {
            DatagramPacket clientRequest = new DatagramPacket(new byte[MSG_SIZE], MSG_SIZE);

            if (!receiveMsgFromAnyClient(clientRequest)) {
                continue;
            }
            //List<String> queuedMessages = new ArrayList<>();
            String clientMsg = new String(clientRequest.getData(), 0, clientRequest.getLength());
            if(!clients.contains(clientRequest)) {
                clients.add(clientRequest);
            }
            queue.add(clientMsg);
            notifyObservers(clientMsg);
            for (DatagramPacket client : clients) {
                sendMsgToClient("Server: Message received by " + clientRequest.getAddress(), new InetSocketAddress(client.getAddress(), client.getPort()));
            }
        }
    }

    private boolean receiveMsgFromAnyClient(DatagramPacket clientRequest){
        try { socket.receive(clientRequest);
            //System.out.println("receiveMsgFromAnyClient: Message recieved!");
            //System.out.println(clientRequest);
        }
        catch (Exception ex) { return false; }
        return true;
    }
}
