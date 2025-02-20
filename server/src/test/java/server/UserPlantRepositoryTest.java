package server;

//TODO Write tests for the UserPlantsRepository class. Then after implement the methods in the class so all tests pass.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserPlantRepositoryTest {

    private UserPlantRepository userPlantRepository;
    private User testUser;
    private Plant testPlant;

    @BeforeEach
    void setUp() {
        userPlantRepository = new UserPlantRepository();
        testUser = new User("1", "test@testmail.com");
        //testPlant = new Plant("Testväxt", "999", Date.valueOf("2024-02-01"), 7, "image_url");
    }

    /*
    @Test
    void savePlantCorrectInput() {
        boolean result = userPlantRepository.savePlant(testUser, testPlant);
        assertTrue(result, "Plantan borde sparas korrekt i databasen.");
    }
    */
    @Test
    void deletePlantFromUser() {

    }

    @Test
    void changeLastWatered() {

    }
}

