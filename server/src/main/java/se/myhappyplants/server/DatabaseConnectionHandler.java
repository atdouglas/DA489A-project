package se.myhappyplants.server;

import se.myhappyplants.server.responses.ResponseController;
import se.myhappyplants.shared.Message;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseConnectionHandler {

    private ResponseController responseController;
    private ExecutorService executor;

    public DatabaseConnectionHandler(ResponseController responseController) {
        this.responseController = responseController;
        // the database manager offered by the university likely only accepts at most 5 active connections (?)
        executor = Executors.newFixedThreadPool(5);
    }

    public Message databaseRequest(Message request) {
        Future<Message> futureTask = executor.submit(() -> responseController.getResponse(request));
        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            return new Message(false);
        }
    }

}
