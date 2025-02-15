package se.myhappyplants.server;

import se.myhappyplants.server.responses.ResponseController;
import se.myhappyplants.server.repositories.PlantRepository;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that starts the server
 * Created by: Frida Jacobson, Eric Simonson, Anton Holm, Linn Borgström, Christopher O'Driscoll
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class StartServer {
    // TODO clean this up
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        UserRepository userRepository = new UserRepository();
        PlantRepository plantRepository = new PlantRepository();
        UserPlantRepository userPlantRepository = new UserPlantRepository();
        ResponseController responseController = new ResponseController(userRepository, userPlantRepository, plantRepository);
        new Server(2555, responseController);
    }
}
