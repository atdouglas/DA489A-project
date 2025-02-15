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

    // TODO: change to completablefuture (?)
    public Message databaseRequest(Message request) {
        Message response;
        Future<Message> futureTask = executor.submit(() -> responseController.getResponse(request));

        try {
            response = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

}
