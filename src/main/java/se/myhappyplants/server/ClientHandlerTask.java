package se.myhappyplants.server;

import se.myhappyplants.server.responses.ResponseController;
import se.myhappyplants.shared.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandlerTask implements Runnable {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ResponseController responseController;

    public ClientHandlerTask(Socket socket, ResponseController responseController) throws IOException {
        this.responseController = responseController;
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.oos = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            Message request = (Message) ois.readObject();
            Message response = responseController.getResponse(request);
            oos.writeObject(response);
            oos.flush();
        }
        catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
