package se.myhappyplants.server;

import se.myhappyplants.server.responses.ResponseController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private ResponseController responseController;
    private ExecutorService executor;

    public Server(int port, ResponseController responseController) {
        this.responseController = responseController;
        // the database manager offered by the university likely only accepts at most 5 active connections (?)
        executor = Executors.newFixedThreadPool(5);
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        System.out.println("Server running");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandlerTask clientHandlerTask = new ClientHandlerTask(socket, responseController);
                executor.submit(clientHandlerTask);
            }
            catch (IOException e) {
                e.printStackTrace();
                executor.shutdown();
                System.out.println("Server stopped");
                return;
            }
        }
    }
}
