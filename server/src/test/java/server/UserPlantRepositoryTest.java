package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.Plant;
import se.myhappyplants.shared.User;
import se.myhappyplants.shared.UserPlant;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserPlantRepositoryTest {

    private UserPlantRepository userPlantRepository;
    private Plant testPlant1;
    private Plant testPlant2;
    private UserRepository userRepository = new UserRepository();
    private UserPlant testUserPlantWorking;
    private UserPlant testUserPlantNotWorking;
    private final User testUser = new User(
            "test@testmail.com",
            "test123",
            true,
            true
    );
    @BeforeEach
    void setUp() {

        userPlantRepository = new UserPlantRepository();
        testPlant1 = new Plant(999, "Ficus lyrata", "Moraceae", "Fiolfikus",
                null, "Indirect sunlight", "Medium",
                false, 7);

        testPlant2 = new Plant(1000, "Monstera deliciosa", "Araceae", "Monstera",
                null, "Bright, indirect light", "Low",
                false, 5);
        testUserPlantWorking = new UserPlant(2862, "Euphorbia amygdaloides subsp. robbiae", "spurge", "Euphorbiaceae",
                "http://perenual.com/storage/species_image/2862_euphorbia_amygdaloides_subsp_robbiae/og/2048px-Bloeiende_Euphorbia_amygdaloides_var._Robbiae._31-03-2021._28d.j.b29.jpg", "Full sun", "Low",
                false, 734400000,"peeedeer",734400000);

        testUserPlantNotWorking = new UserPlant(2862, "Euphorbia amygdaloides subsp. robbiae", "spurge", "Euphorbiaceae",
                "http://perenual.com/storage/species_image/2862_euphorbia_amygdaloides_subsp_robbiae/og/2048px-Bloeiende_Euphorbia_amygdaloides_var._Robbiae._31-03-2021._28d.j.b29.jpg", "Full sun", "Low",
                false, 734400000,"peeedeer",734400000);

    }

    @Test
    void testSavePlantSuccess() {
        userRepository.saveUser(testUser);
        User testUserCopy = userRepository.getUserDetails("test@testmail.com");
        boolean result = userPlantRepository.savePlant(testUserCopy, testUserPlantWorking);
        userRepository.deleteAccount("test@testmail.com","test123");
        assertEquals(true,result,
                "The savePlant method failed and didn't save the plant: testSavePlantSuccess failed");
    }

/*
    @Test
    void testGetUserLibrarySuccess() {
        userPlantRepository.savePlant(testUser, testPlant1);
        userPlantRepository.savePlant(testUser, testPlant2);

        List<Plant> plants = userPlantRepository.getUserLibrary(testUser.getId());
        assertEquals("Ficus lyrata",plants.get(0),
                "It should be Ficus lyrata :testGetUserLibrarySuccess failed");
        assertEquals("Monstera deliciosa",plants.get(1),
                "It should be Monstera Deliciosa :testGetUserLibrarySuccess failed");
    }

    @Test
    void testGetUserLibraryEmpty() {
        List<Plant> plants = userPlantRepository.getUserLibrary(testUser.getId());
        assertEquals("",plants.get(0),
                "it should be empty (this failed for an empty array) :testGetUserLibraryEmpty failed");
    }

    @Test
    void testGetUserLibraryNull() {
        userPlantRepository.savePlant(testUser, null);
        List<Plant> plants = userPlantRepository.getUserLibrary(testUser.getId());
        assertEquals("",plants.get(0),
                "it should be empty (this failed for the value null: testGetUserLibraryNull failed");
    }



    @Test
    void testDeletePlantSuccess() {
        userPlantRepository.savePlant(testUser, testPlant1);

        //plats i library kanske istället för namn eller nickname då detta bör kunna hantera att en användare har två av samma
        // planta med exv olika smeknamn//ett unikt id så att man mer kontrollerat kan ta bort den ena över den andra
        boolean result = userPlantRepository.deletePlant(testUser, testPlant1.getScientific_name());
        assertEquals(true,result,
                "The plant was not deleted :testDeletePlantSuccess failed");
    }

    @Test
    void testDeletePlantFailure() {
        userPlantRepository.savePlant(testUser, testPlant1);
        boolean result = userPlantRepository.deletePlant(testUser, "");
        assertEquals(false,result,
                "The a non existing plant was deleted :testDeletePlantFailure failed");
    }

    @Test
    void testDeletePlantNull() {
        userPlantRepository.savePlant(testUser, testPlant1);
        boolean result = userPlantRepository.deletePlant(testUser, null);
        assertEquals(false,result,
                "A null plant was deleted :testDeletePlantNull failed");
    }

    @Test
    void testChangeLastWateredSuccess() {
        userPlantRepository.savePlant(testUser, testPlant1);
        LocalDate newDate = LocalDate.of(2024, 2, 10);
        //plats i library kanske istället för namn eller nickname då detta bör kunna hantera att en användare har två av samma
        // planta med exv olika smeknamn//ett unikt id så att man mer kontrollerat kan ta bort den ena över den andra
        boolean result = userPlantRepository.changeLastWatered(testUser, testPlant1.getCommon_name(), newDate);
        assertEquals(true, result,
                "The water was successfully changed :testChangeLastWateredSuccess failed");
    }

    @Test
    void testChangeLastWateredFailureNoUser() {
        userPlantRepository.savePlant(testUser, testPlant1);
        LocalDate newDate = LocalDate.of(2024, 2, 10);
        //plats i library kanske istället för namn eller nickname då detta bör kunna hantera att en användare har två av samma
        // planta med exv olika smeknamn//ett unikt id så att man mer kontrollerat kan ta bort den ena över den andra
        boolean result = userPlantRepository.changeLastWatered("", testPlant1.getCommon_name(), newDate);
        assertEquals(false, result,
                "The water was successfully changed which it should not :testChangeLastWateredFailureNoUser failed");
    }

    @Test
    void testChangeLastWateredFailureNullUser() {
        userPlantRepository.savePlant(testUser, testPlant1);
        LocalDate newDate = LocalDate.of(2024, 2, 10);
        //plats i library kanske istället för namn eller nickname då detta bör kunna hantera att en användare har två av samma
        // planta med exv olika smeknamn//ett unikt id så att man mer kontrollerat kan ta bort den ena över den andra
        boolean result = userPlantRepository.changeLastWatered(null, testPlant1.getCommon_name(), newDate);
        assertEquals(false, result,
                "The water was successfully changed which it should not :testChangeLastWateredFailureNullUser failed");
    }

    @Test
    void testChangeLastWateredFailureNullDate() {
        userPlantRepository.savePlant(testUser, testPlant1);
        //plats i library kanske istället för namn eller nickname då detta bör kunna hantera att en användare har två av samma
        // planta med exv olika smeknamn//ett unikt id så att man mer kontrollerat kan ta bort den ena över den andra
        boolean result = userPlantRepository.changeLastWatered(testUser, testPlant1.getCommon_name(), null);
        assertEquals(true, result,
                "The water was successfully changed which it should not :testChangeLastWateredFailureNullDate failed");
    }

    @Test
    void testChangeLastWateredFailureNoDate() {
        userPlantRepository.savePlant(testUser, testPlant1);
        //plats i library kanske istället för namn eller nickname då detta bör kunna hantera att en användare har två av samma
        // planta med exv olika smeknamn så att man mer kontrollerat kan ta bort den ena över den andra
        boolean result = userPlantRepository.changeLastWatered(testUser, testPlant1.getCommon_name(), "");
        assertEquals(true, result,
                "The water was successfully changed which it should not :testChangeLastWateredFailureNoDate failed");
    }

    @Test
    void testChangeNicknameSuccess() {
        boolean result = userPlantRepository.changeNickname(testUser, plantId, "Adrian is a g" ); //Should be changed to user id, plant id and a new nickname
        assertEquals(true,result,
                "The nickname was not changed :testChangeNicknameSuccess failed");
    }

    @Test
    void testChangeNicknameFailureNoUserID() {
        boolean result = userPlantRepository.changeNickname("", plantId, "Adrian is a g" ); //Should be changed to user id, plant id and a new nickname
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNoUserID failed");
    }


    @Test
    void testChangeNicknameFailureNullUserID() {
        boolean result = userPlantRepository.changeNickname(null, plantId, "Adrian is a g" ); //Should be changed to user id, plant id and a new nickname
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNullUserID failed");
    }

    @Test
    void testChangeNicknameFailureNoPlantID() {
        boolean result = userPlantRepository.changeNickname(testUser, "", "Adrian is a g" ); //Should be changed to user id, plant id and a new nickname
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNoPlantID failed");
    }

    @Test
    void testChangeNicknameFailureNullPlantID() {
        boolean result = userPlantRepository.changeNickname(testUser, null, "Adrian is a g" ); //Should be changed to user id, plant id and a new nickname
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNullPlantID failed");
    }

    @Test
    void testChangeNicknameFailureNullNickname() {
        boolean result = userPlantRepository.changeNickname(testUser, plantId, null ); //Should be changed to user id, plant id and a new nickname
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameSuccess failed");
    }

    @Test
    void testChangeNicknameFailureNoNickname() {
        boolean result = userPlantRepository.changeNickname(testUser, plantId, "" ); //Should be changed to user id, plant id and a new nickname
        assertEquals(false,result,
                "The nickname was changed :testChangeNicknameFailureNoNickname failed");
    }

    @Test
    void testWaterAllPlantsSuccess() {
        boolean result = changeAllToWatered(testUser);
        assertEquals(true,result,
                "All plants weren't watered :testWaterAllPlantsSuccess failed");
    }

    @Test
    void testWaterAllPlantFailureNoUserID() {
        boolean result = changeAllToWatered("");
        assertEquals(false,result,
                "All plants were watered which they should not be :testWaterAllPlantFailureNoUserID failed");
    }

    @Test
    void testWaterAllPlantFailureNullUserID() {
        boolean result = changeAllToWatered(null);
        assertEquals(false,result,
                "All plants were watered which they should not be :testWaterAllPlantFailureNullUserID failed");
    }

    */
}
