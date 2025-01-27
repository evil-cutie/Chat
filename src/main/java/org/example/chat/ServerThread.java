package org.example.chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static org.example.chat.Server.users;

public class ServerThread extends Thread{

    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private String username = null;

    public ServerThread(Socket socket) {
        try {
            System.out.println("SERVER >> new server thread has started");
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("SERVER >> opened input and output streams");
        }
        catch (IOException e) {
            System.out.println("SERVER >> error occurred while opening streams");
            closeServerThread();
        }
    }
    @Override
    public void run() {
        super.run();
        try {
            listenForMessages();
        }
        catch (IOException e) {
            System.out.println("SERVER >> error occurred while listening for messages from CLIENT " + e.getMessage());
        }
        finally {
            closeServerThread();
            System.out.println("SERVER >> user " + username + " thread has stopped");
        }
    }

    private void listenForMessages() throws IOException {
        while (socket.isConnected()) {
            UserData newMessage = handleRead();
            if (newMessage != null) {
                if (username == null) username = newMessage.getUsername();
                if (!newMessage.isOnline()) users.remove(this); //remove user from broadcasting list after he disconnects
                updateOnlineUsers(newMessage);
                broadcastMessage(newMessage);
            }
        }
    }

    private UserData handleRead() {
        UserData newMessage = null;
        try {
            newMessage = (UserData) in.readObject();
            System.out.println("SERVER >> Received message from CLIENT: " + newMessage.getMessage());
        }
        catch (IOException | ClassNotFoundException e) {
            closeServerThread();
        }
        return newMessage;
    }

    private void broadcastMessage(UserData newMessage) throws IOException {
        System.out.println("SERVER >> Broadcasting message: " + newMessage.getMessage());
        for (ServerThread user : users) {
            user.getOutputStream().writeObject(newMessage);
            user.getOutputStream().flush();
        }
    }

    private void closeServerThread() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        }
        catch (IOException e) {
            System.out.println("SERVER >> error occurred while closing streams " + e.getMessage());
        }
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public String getUsername() {
        if (username != null) return username;
        else return "user_not_found";
    }

    private void updateOnlineUsers(UserData newMessage) {
        ArrayList<String> onlineUsers = new ArrayList<>();
        onlineUsers.add("\nonline users: ");
        for (ServerThread user : users) {
            onlineUsers.add(user.getUsername());
        }
        newMessage.setOnlineUsers(onlineUsers);
    }
}
