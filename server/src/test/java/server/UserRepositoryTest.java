package server;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.User;

public class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepository();

    private final User testUser = new User(
            "test@testmail.com",
            "test",
            "test123",
            true
    );



    @Test
    void saveUserCorrectInput(){
        boolean result = userRepository.saveUser(testUser);
        assertTrue(result, "The method should return true because a valid user was submitted.");
    }

    //DOES NOT PASS
    @Test
    void getUserDetailsCorrectInput(){
        User result = userRepository.getUserDetails(testUser.getEmail());

        assertAll(
                "Testing correct input on getUserDetails.",
                () -> assertEquals(testUser.getEmail(), result.getEmail(),
                        "Wrong email, the expected email is " + testUser.getEmail()),

                () -> assertEquals(testUser.getUniqueId(), result.getUniqueId(),
                        "Wrong ID, the expected ID is " + testUser.getUniqueId()),

                () -> assertEquals(testUser.areNotificationsActivated(), result.areNotificationsActivated(),
                        "The test user has notifications active, but the test returned false.")
        );
    }

    //TODO
    @Test
    void deleteAccountCorrectInput(){

    }
}
