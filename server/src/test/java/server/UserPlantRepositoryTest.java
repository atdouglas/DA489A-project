package server;

import org.junit.jupiter.api.*;
import se.myhappyplants.server.repositories.UserPlantRepository;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.User;
import se.myhappyplants.shared.UserPlant;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserPlantRepositoryTest {

    private UserPlantRepository userPlantRepository = new UserPlantRepository();
    private static UserRepository userRepository = new UserRepository();
    private UserPlant testUserPlantWorking;
    //private UserPlant testUserPlantWorking2;
    private static User testUser;

    @BeforeAll
    static void createAndSaveUser() {
        testUser = new User(
                "test@testmail.com",
                "test123",
                "security question clue",
                "security question answer"
        );
        userRepository.saveUser(testUser);
        testUser.setUniqueId(userRepository.getUserDetails(testUser.getEmail()).getUniqueId());
    }

    @BeforeEach
    void createAndAddPlants() {
        testUserPlantWorking = new UserPlant(2862, "Euphorbia Amygdaloides Subsp. Robbiae", "Spurge", "Euphorbiaceae",
                "http://perenual.com/storage/species_image/2862_euphorbia_amygdaloides_subsp_robbiae/og/2048px-Bloeiende_Euphorbia_amygdaloides_var._Robbiae._31-03-2021._28d.j.b29.jpg", "Full sun", "Low",
                false, 734400000, "peeedeereeeeeeeeeeeeeeeeeeeeeeeeeee", 734400000);
        userPlantRepository.savePlant(testUser, testUserPlantWorking);

        /*
        testUserPlantWorking2 = new UserPlant(2895, "Eurybia Divaricata", "Asteraceae", "White Wood Aster",
                "https://perenual.com/storage/species_image/2895_eurybia_divaricata/og/7515068602_0626782d24_b.jpg", "Part shade", "Low",
                false, 0,"peeedeer", 0);
        userPlantRepository.savePlant(testUser, testUserPlantWorking2);
         */

        ArrayList<UserPlant> userPlants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        testUserPlantWorking.setUserPlantId(userPlants.getFirst().getUserPlantId());
    }

    @AfterEach
    void resetPlants() {
        int testUserID = testUser.getUniqueId();
        ArrayList<UserPlant> plantList = userPlantRepository.getUserLibrary(testUserID);
        for (UserPlant userPlant : plantList) {
            userPlantRepository.deletePlant(testUserID, userPlant.getUserPlantId());
        }
    }

    @AfterAll
    static void tearDown() {
        userRepository.deleteAccount("test@testmail.com", "test123");
    }

    @Test
    @Order(1)
    void testSavePlantSuccess() {
        boolean result = userPlantRepository.savePlant(testUser, testUserPlantWorking);
        assertTrue(result,
                "The savePlant method failed and didn't save the plant: testSavePlantSuccess failed");
    }

    @Test
    @Order(2)
    void testSavePlantTooShortNickname() {
        testUserPlantWorking.setNickname("Hi");
        boolean result = userPlantRepository.savePlant(testUser, testUserPlantWorking);
        assertFalse(result);
    }

    @Test
    @Order(3)
    void testSavePlantTooLongNickname() {
        testUserPlantWorking.setNickname("Hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        boolean result = userPlantRepository.savePlant(testUser, testUserPlantWorking);
        assertFalse(result);
    }

    @Test
    @Order(4)
    void testGetUserLibrarySuccess() {
        UserPlant newPlant = new UserPlant(2895, "Eurybia Divaricata", "Asteraceae", "White Wood Aster",
                "https://perenual.com/storage/species_image/2895_eurybia_divaricata/og/7515068602_0626782d24_b.jpg", "Part shade", "Low",
                false, 0, "peeedeer", 0);
        userPlantRepository.savePlant(testUser, newPlant);
        List<UserPlant> plants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        int idTest = plants.get(0).getUserPlantId();
        String commonNameTest = plants.get(1).getCommon_name();
        assertAll(
                () -> assertEquals(testUserPlantWorking.getUserPlantId(), idTest, "It should be " + testUserPlantWorking.getUserPlantId() + " :testGetUserLibrarySuccess failed"),
                () -> assertEquals(newPlant.getCommon_name(), commonNameTest, "It should be " + newPlant.getCommon_name() + " :testGetUserLibrarySuccess failed")
        );
    }

    @Test
    @Order(5)
    void testGetUserLibraryEmpty() {
        List<UserPlant> plants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        assertEquals(1, plants.size(),
                "list size should be 2 (this failed for an empty array) :testGetUserLibraryEmpty failed");
    }

    @Test
    @Order(6)
    void testGetUserLibraryNull() {
        userPlantRepository.savePlant(testUser, null);
        List<UserPlant> plants = userPlantRepository.getUserLibrary(testUser.getUniqueId());
        assertEquals(1, plants.size(),
                "list size should be 2 (this failed for the value null: testGetUserLibraryNull failed");
    }

    @Test
    @Order(7)
    void testDeletePlantSuccess() {
        userPlantRepository.savePlant(testUser, testUserPlantWorking);
        boolean result = userPlantRepository.deletePlant(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId());
        assertTrue(result,
                "The plant was not deleted :testDeletePlantSuccess failed");
    }

    @Test
    @Order(8)
    void testDeletePlantIllegalPlantID() {
        boolean result = userPlantRepository.deletePlant(testUser.getUniqueId(), -1);
        assertFalse(result,
                "A non existing plant was deleted :testDeletePlantIllegalPlantID failed");
    }

    @Test
    @Order(9)
    void testChangeLastWateredSuccess() {
        boolean result = userPlantRepository.changeLastWatered(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), 0);
        assertTrue(result,
                "The water was successfully changed :testChangeLastWateredSuccess failed");
    }

    @Test
    @Order(10)
    void testChangeLastWateredCorrectValue() {
        userPlantRepository.changeLastWatered(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), 10);
        assertEquals(10, userPlantRepository.getUserPlant(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId()).getLastWatered());
    }

    @Test
    @Order(11)
    void testChangeLastWateredIncorrectValue() {
        userPlantRepository.changeLastWatered(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), 100);
        assertNotEquals(10, userPlantRepository.getUserPlant(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId()).getLastWatered());
    }

    @Test
    @Order(12)
    void testChangeLastWateredFailureIllegalUser() {
        boolean result = userPlantRepository.changeLastWatered(-1, testUserPlantWorking.getUserPlantId(), 0);
        assertFalse(result,
                "The water was successfully changed which it should not :testChangeLastWateredFailureIllegalUser failed");
    }

    @Test
    @Order(13)
    void testChangeLastWateredFailureIllegalLastWatered() {
        assertFalse(userPlantRepository.changeLastWatered(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), -1));
    }

    @Test
    @Order(14)
    void testChangePlantNicknameSuccess() {
        boolean result = userPlantRepository.changePlantNickname(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), "Adrian is a g");
        assertTrue(result,
                "The nickname was not changed :testChangeNicknameSuccess failed");
    }

    @Test
    @Order(15)
    void testChangePlantNicknameFailureIllegalID() {
        boolean result = userPlantRepository.changePlantNickname(-1, testUserPlantWorking.getUserPlantId(), "Adrian is a g");
        assertFalse(result,
                "The nickname was changed :testChangeNicknameFailureIllegalID failed");
    }


    @Test
    @Order(16)
    void testChangePlantNicknameFailureIllegalPlantID() {
        boolean result = userPlantRepository.changePlantNickname(testUser.getUniqueId(), -1, "Adrian is a g");
        assertFalse(result,
                "The nickname was changed :testChangeNicknameFailureIllegalPlantID failed");
    }


    @Test
    @Order(17)
    void testChangeNicknameFailureNullPlantNickname() {
        boolean result = userPlantRepository.changePlantNickname(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), null);
        assertFalse(result,
                "The nickname was changed :testChangeNicknameFailureNullNickname failed");
    }

    @Test
    @Order(18)
    void testChangeNicknameFailureNoPlantNickname() {
        boolean result = userPlantRepository.changePlantNickname(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), "");
        assertFalse(result,
                "The nickname was changed :testChangeNicknameFailureNoNickname failed");
    }

    @Test
    @Order(19)
    void testChangeNicknameFailurePlantNicknameTooShort() {
        boolean result = userPlantRepository.changePlantNickname(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), "Hi");
        assertFalse(result);
    }

    @Test
    @Order(20)
    void testChangeNicknameFailurePlantNicknameTooLong() {
        boolean result = userPlantRepository.changePlantNickname(testUser.getUniqueId(), testUserPlantWorking.getUserPlantId(), "Hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        assertFalse(result);
    }

    @Test
    @Order(21)
    void testWaterAllPlantsSuccess() {
        boolean result = userPlantRepository.changeAllToWatered(testUser.getUniqueId());
        assertTrue(result,
                "All plants weren't watered :testWaterAllPlantsSuccess failed");
    }

    @Test
    @Order(22)
    void testWaterAllPlantFailureIllegalUserID() {
        boolean result = userPlantRepository.changeAllToWatered(-1);
        assertFalse(result,
                "All plants were watered which they should not be :testWaterAllPlantFailureIllegalUserID failed");
    }
    @Test
    void testChangeAllToWateredInvalidUserId() {
        boolean result = userPlantRepository.changeAllToWatered(-1);
        assertFalse(result, "The method should return false for an invalid user ID.");
    }



}
