package org.example.chat;

import java.io.*;
import java.net.Socket;

public class Client {

    private ClientAppController controller;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(Socket socket, ClientAppController controller) {
        try {
            this.controller = controller;
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("CLIENT >> opened input and output streams");
        }
        catch (IOException e) {
            System.out.println("CLIENT >> error occurred while opening streams");
            closeClient();
        }
    }

    public void sendMessage(UserData newMessage) {
        try {
            System.out.println("CLIENT >> sending message to SERVER: " + newMessage.getMessage());
            out.writeObject(newMessage);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("CLIENT >> error occurred while sending message to SERVER: " + newMessage.getMessage());
            closeClient();
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    receiveIncomingMessages();
                }
                catch(IOException | ClassNotFoundException e) {
                    System.out.println("CLIENT >> error occurred while listening for messages from SERVER " + e.getMessage());
                }
                finally {
                    closeClient();
                }
            }
        }).start();
    }

    private void receiveIncomingMessages() throws IOException, ClassNotFoundException {
        while(socket.isConnected()) {
            UserData incomingMessage = (UserData) in.readObject();
            if (incomingMessage != null) {
                displayMessage(incomingMessage);
                updateOnlineUsers(incomingMessage);
            }
        }
    }

    private void displayMessage(UserData incomingMessage) {
        System.out.println("CLIENT >> displaying a message from SERVER: " + incomingMessage.getMessage());
        String message = incomingMessage.getMessage();
        controller.setChatBoardText(message);
    }

    public void updateOnlineUsers(UserData incomingMessage) {
        controller.setOnlineUsers(incomingMessage.getOnlineUsers());
        controller.setUserBoardText();
    }

    public void closeClient() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        }
        catch (IOException e) {
            System.out.println("CLIENT >> error occurred while closing streams");
        }
        finally {
            System.exit(0);
        }
    }
}
