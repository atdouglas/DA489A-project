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
    public static void main(String[] args) throws UnknownHostException, SQLException {
        IDatabaseConnection connectionMyHappyPlants = new DatabaseConnection();
        IQueryExecutor databaseMyHappyPlants = new QueryExecutor(connectionMyHappyPlants);
        UserRepository userRepository = new UserRepository(databaseMyHappyPlants);
        PlantRepository plantRepository = new PlantRepository(databaseMyHappyPlants);
        UserPlantRepository userPlantRepository = new UserPlantRepository(plantRepository, databaseMyHappyPlants);
        ResponseController responseController = new ResponseController(userRepository,userPlantRepository,plantRepository);
        new Server(2555, responseController);
    }
}
