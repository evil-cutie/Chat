package org.example.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static ArrayList<ServerThread> users = new ArrayList<>();
    private ServerSocket serverSocket = null;

    public Server() {
        try {
            serverSocket = new ServerSocket(7777);
            waitForNewClient();
        }
        catch (IOException e) {
            closeServerSocket();
        }
    }

    private void waitForNewClient() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ServerThread newUser = new ServerThread(clientSocket);
            users.add(newUser);
            newUser.start();
        }
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        }
        catch (IOException e1) {
            System.out.println("SERVER >> error closing server socket: " + e1.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
