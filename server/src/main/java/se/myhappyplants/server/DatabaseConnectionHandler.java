package se.myhappyplants.server;

import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.server.responses.*;
import se.myhappyplants.shared.Message;
import se.myhappyplants.shared.MessageType;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseConnectionHandler {
    // the database manager offered by the university likely only accepts at most 5 active connections (?)
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private HashMap<MessageType, IResponseHandler> responders = new HashMap<>();

    public DatabaseConnectionHandler() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        createResponders();
    }

    private void createResponders() {
        UserRepository userRepository = new UserRepository();
        PlantRepository plantRepository = new PlantRepository();
        UserPlantRepository userPlantRepository = new UserPlantRepository();
        //responders.put(MessageType.changeAllToWatered, new ChangeAllToWatered(userPlantRepository));
        responders.put(MessageType.changeFunFacts, new ChangeFunFacts(userRepository));
        //responders.put(MessageType.changeLastWatered, new ChangeLastWatered(userPlantRepository));
        //responders.put(MessageType.changeNickname, new ChangeNickname(userPlantRepository));
        responders.put(MessageType.changeNotifications, new ChangeNotifications(userRepository));
        //responders.put(MessageType.changePlantPicture, new ChangePlantPicture(userPlantRepository));
        responders.put(MessageType.deleteAccount, new DeleteAccount(userRepository));
        //responders.put(MessageType.deletePlant, new DeletePlant(userPlantRepository));
        //responders.put(MessageType.getLibrary, new GetLibrary(userPlantRepository));
        //responders.put(MessageType.getMorePlantInfo, new GetPlantDetails(plantRepository));
        responders.put(MessageType.login, new Login(userRepository));
        responders.put(MessageType.register, new Register(userRepository));
        //responders.put(MessageType.savePlant, new SavePlant(userPlantRepository));
        responders.put(MessageType.search, new Search(plantRepository));
        responders.put(MessageType.getPlant, new GetPlant(plantRepository));
    }

    private Message getResponse(Message request) {

        Message response;
        MessageType messageType = request.getMessageType();

        IResponseHandler responseHandler = responders.get(messageType);
        response = responseHandler.getResponse(request);
        return response;
    }

    public Message databaseRequest(Message request) {
        Future<Message> futureTask = executor.submit(() -> getResponse(request));
        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            // return Message even if exception
            return new Message(false);
        }
    }

}
