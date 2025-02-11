package se.myhappyplants.server;

import se.myhappyplants.server.controller.ResponseController;
import se.myhappyplants.server.services.*;

import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * Class that starts the server
 * Created by: Frida Jacobson, Eric Simonson, Anton Holm, Linn Borgström, Christopher O'Driscoll
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class StartServer {
    // TODO clean this up
    public static void main(String[] args) {
        DatabaseConnection connectionMyHappyPlants = new DatabaseConnection();
        UserRepository userRepository = new UserRepository(connectionMyHappyPlants);
        PlantRepository plantRepository = new PlantRepository(connectionMyHappyPlants);
        UserPlantRepository userPlantRepository = new UserPlantRepository(plantRepository, connectionMyHappyPlants);
        ResponseController responseController = new ResponseController(userRepository, userPlantRepository, plantRepository);
        new Server(2555, responseController);
    }
}
