package com.httpserver.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.httpserver.server.client.ClientHandler;
import com.httpserver.server.utils.GZIPCompressor;

public class App {
    public static void main(String[] args) throws Exception, InterruptedException {
        // Some VooDoo magic is making this not work.
        // try (var connection = DB.connect()){
        // System.out.print("Connected to the PostgreSQL database.");
        // } catch (SQLException e) {
        // e.printStackTrace();
        // System.err.println(e.getMessage());
        // }
        try (ServerSocket serverSocket = new ServerSocket(8081)) {
            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        }
    }
}
