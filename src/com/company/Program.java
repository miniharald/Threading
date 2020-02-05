package com.company;

public class Program {

    public Program() {
        NetworkClient client = new NetworkClient();
        NetworkServer server = new NetworkServer();

        Thread thread1 = new Thread(client, "klient");
        Thread thread2 = new Thread(server, "server");


        thread1.start();
        thread2.start();
    }


}
