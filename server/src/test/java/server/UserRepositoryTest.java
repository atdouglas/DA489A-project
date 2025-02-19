package server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.User;

/**
 * Test class for unit tests in the UserRepository clas.
 *
 * @see UserRepository
 * @author Douglas Almö Thorsell
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepository();

    private final User testUser = new User(
            "test@testmail.com",
            "test123",
            true,
            true
    );

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Order(1)
    class TestCorrectInput{

        @Test
        @Order(1)
        void saveUserCorrectInput(){
            boolean result = userRepository.saveUser(testUser);
            assertTrue(result, "The method should return true because a valid user was submitted.");
        }


        @Test
        @Order(2)
        void getUserDetailsCorrectInput(){
            User result = userRepository.getUserDetails(testUser.getEmail());

            assertAll(
                    "Testing correct input on getUserDetails.",
                    () -> assertEquals(testUser.getEmail(), result.getEmail(),
                            "Wrong email, the expected email is " + testUser.getEmail()),
                    () -> assertEquals(testUser.areNotificationsActivated(), result.areNotificationsActivated(),
                            "The test user has notifications active, but the test returned false."),
                    () -> assertEquals(testUser.areFunFactsActivated(), result.areFunFactsActivated(),
                            "The test user has fun facts activated, but the test returned false.")
            );
        }

        @Test
        @Order(3)
        void checkLoginCorrectInput(){
            boolean result = userRepository.checkLogin(testUser.getEmail(), testUser.getPassword());

            assertTrue(result, "Verification failed. The user is registered so the verification should return true.");
        }

        @Test
        @Order(4)
        void changeNotificationsCorrectInput(){
            boolean result = userRepository.changeNotifications(testUser.getEmail(), false);

            assertTrue(result, "Change notifications failed. The user is registered and " +
                    "the notification value is correct so the result should return true.");
        }

        @Test
        @Order(5)
        void changeFunFactsCorrectInput(){
            boolean result = userRepository.changeFunFacts(testUser.getEmail(), false);

            assertTrue(result, "Change fun facts failed. The user is registered and " +
                    "the change value is correct so the result should return true.");
        }

        @Test
        @Order(6)
        void deleteAccountCorrectInput(){
            boolean result = userRepository.deleteAccount(testUser.getEmail(), testUser.getPassword());

            assertTrue(result, "The user was not deleted from the database.");
        }

    }

    @Nested
    @Order(2)
    class TestIncorrectInputSaveUser{
        @Test
        void saveUserIncorrectEmailInput(){
            User incorrectUser = new User(
                    "test@testmail.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",
                    "test123",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an invalid email was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputNull(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    null,
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an invalid password was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputEmpty(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an invalid password was submitted.");
        }
    }

    @Nested
    @Order(3)
    class TestIncorrectInputCheckLogin{

    }
}




























