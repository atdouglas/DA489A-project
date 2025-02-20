package server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.User;

/**
 * Test class for unit tests in the UserRepository class.
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
        void saveUserCorrectInputEmailLimit(){
            User newTestUser = new User(
                    "testteestesttestestestestestestestest@testmail.com",
                    "test123",
                    true,
                    true);

            boolean result = userRepository.saveUser(newTestUser);
            userRepository.deleteAccount(newTestUser.getEmail(), newTestUser.getPassword());
            assertTrue(result, "The method should return true because a valid user was submitted.");
        }

        @Test
        @Order(3)
        void saveUserCorrectInputPassLowLimit(){
            User newTestUser = new User(
                    "test@testmail1.com",
                    "test12",
                    true,
                    true);

            boolean result = userRepository.saveUser(newTestUser);
            userRepository.deleteAccount(newTestUser.getEmail(), newTestUser.getPassword());
            assertTrue(result, "The method should return true because a valid user was submitted.");
        }

        @Test
        @Order(4)
        void saveUserCorrectInputPassUpperLimit(){
            User newTestUser = new User(
                    "test@testmail2.com",
                    "test12312312312312312312312312312312312312312312312312312312312312312312",
                    true,
                    true);

            boolean result = userRepository.saveUser(newTestUser);
            userRepository.deleteAccount(newTestUser.getEmail(), newTestUser.getPassword());
            assertTrue(result, "The method should return true because a valid user was submitted.");
        }


        @Test
        @Order(5)
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
        @Order(6)
        void checkLoginCorrectInput(){
            boolean result = userRepository.checkLogin(testUser.getEmail(), testUser.getPassword());

            assertTrue(result, "Verification failed. The user is registered so the verification should return true.");
        }

        @Test
        @Order(7)
        void changeNotificationsCorrectInput(){
            boolean result = userRepository.changeNotifications(testUser.getEmail(), false);

            assertTrue(result, "Change notifications failed. The user is registered and " +
                    "the notification value is correct so the result should return true.");
        }

        @Test
        @Order(8)
        void changeFunFactsCorrectInput(){
            boolean result = userRepository.changeFunFacts(testUser.getEmail(), false);

            assertTrue(result, "Change fun facts failed. The user is registered and " +
                    "the change value is correct so the result should return true.");
        }

        @Test
        @Order(9)
        void deleteAccountCorrectInput(){
            boolean result1 = userRepository.deleteAccount(testUser.getEmail(), testUser.getPassword());

            assertTrue(result1, "The user was not deleted from the database.");


        }

    }

    @Nested
    @Order(2)
    class TestIncorrectInputSaveUser{
        @Test
        void saveUserIncorrectEmailInputTooLong(){
            User incorrectUser = new User(
                    "test@testmail.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",
                    "test123",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a too long email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputTooLong51Char(){
            User incorrectUser = new User(
                    "test@testmail.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",
                    "test123",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a too long email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputNull(){
            User incorrectUser = new User(
                    null,
                    "test123",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a null email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputEmpty(){
            User incorrectUser = new User(
                    "",
                    "test123",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an empty email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputNotAValidAddress(){
            User incorrectUser = new User(
                    "testmail",
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
            assertFalse(result, "The method should return false because a null password was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputEmpty(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a empty password was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputTooLong(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "abcdefghijklmnopqrstuvwxyz123456790abcdefghijklmnopqrstuvwxyz123456790abcdefghijklmnopq",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a password with too long of a length was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputTooShort(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "abcde",
                    true,
                    true);

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an password with too short of a length was submitted.");
        }
    }

    @Nested
    @Order(3)
    class TestIncorrectInputDetails{

        @Test
        void getUserDetailsWrongInput(){
            User result = userRepository.getUserDetails("asd");

            assertNull(result, "No such user exist so null should've been returned.");
        }

        @Test
        void getUserDetailsWrongInputNull(){
            User result = userRepository.getUserDetails(null);

            assertNull(result, "No such user exist so null should've been returned.");
        }

        @Test
        void getUserDetailsWrongInputEmpty(){
            User result = userRepository.getUserDetails("");

            assertNull(result, "No such user exist so null should've been returned.");
        }
    }
}




























