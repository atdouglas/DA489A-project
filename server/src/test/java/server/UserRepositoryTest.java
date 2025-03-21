package server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import se.myhappyplants.server.repositories.UserRepository;
import se.myhappyplants.shared.TokenStatus;
import se.myhappyplants.shared.User;

/**
 * Test class for unit tests in the UserRepository class.
 *
 * @see UserRepository
 * @author Douglas Almö Thorsell
 */

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class UserRepositoryTest {
    private static final UserRepository userRepository = new UserRepository();

    private static User testUser = new User(
            "test@testmail.com",
            "test123",
            "security question clue",
            "security question answer"
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
                    "clue",
                    "answer");

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
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(newTestUser);
            userRepository.deleteAccount(newTestUser.getEmail(), newTestUser.getPassword());
            assertTrue(result, "The method should return true because a valid user was submitted.");
        }

        @Test
        @Order(4)
        void saveUserCorrectInputPassUpperLimit(){
            User newTestUser = new User(
                    "test@testmail2.com",
                    "test1231231231231231231231231231231231231231231231",
                    "clue",
                    "answer");

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
                            "The test user has notifications active, but the test returned false.")
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
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a too long email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputTooLong51Char(){
            User incorrectUser = new User(
                    "test@testmail.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm",
                    "test123",
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a too long email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputNull(){
            User incorrectUser = new User(
                    null,
                    "test123",
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a null email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputEmpty(){
            User incorrectUser = new User(
                    "",
                    "test123",
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an empty email was submitted.");
        }

        @Test
        void saveUserIncorrectEmailInputNotAValidAddress(){
            User incorrectUser = new User(
                    "testmail",
                    "test123",
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because an invalid email was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputNull(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    null,
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a null password was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputEmpty(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "",
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a empty password was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputTooLong(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "abcdefghijklmnopqrstuvwxyz123456790abcdefghijklmnopqrstuvwxyz123456790abcdefghijklmnopq",
                    "clue",
                    "answer");

            boolean result = userRepository.saveUser(incorrectUser);
            assertFalse(result, "The method should return false because a password with too long of a length was submitted.");
        }

        @Test
        void saveUserIncorrectPasswordInputTooShort(){
            User incorrectUser = new User(
                    "test@testmail.com",
                    "abcde",
                    "clue",
                    "answer");

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

        @Test
        void getUserDetailsWrongInputTooLong(){
            User result = userRepository.getUserDetails("test@testmail.commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

            assertNull(result, "No such user exist so null should've been returned.");
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Order(4)
    class TestVerificationCorrectInput{
        private static String token;

        @BeforeAll
        static void setup(){
            userRepository.saveUser(testUser);
            testUser.setUniqueId(userRepository.getUserDetails(testUser.getEmail()).getUniqueId());

        }

        @Test
        @Order(1)
        void generateToken(){
            token = userRepository.getNewAccessToken(testUser.getEmail(), testUser.getPassword());

            assertEquals(32, token.length(), "If a token was generated, then the length should be 32 chars.");
        }

        @Test
        @Order(2)
        void verifyToken(){
            TokenStatus status = userRepository.verifyAccessToken(testUser.getUniqueId(), token);

            assertEquals(TokenStatus.VALID, status, "The token should return as valid.");
        }

        @Test
        @Order(3)
        void deleteAccessToken(){
            boolean result = userRepository.deleteAccessToken(testUser.getEmail(), testUser.getPassword());
            assertTrue(result, "The access token exists so the token should've been deleted. True should've been returned.");
        }

        @AfterAll
        static void close(){
            userRepository.deleteAccount(testUser.getEmail(), testUser.getPassword());
        }
    }

    @Nested
    @Order(5)
    class TestVerificationIncorrectInput{

        /*
            Never update this user, or it's access token in the database. If the user is deleted from the database
            or a new token is generated, then this needs to be updated with the correct token.
         */
        private final static User expiredUser = new User(
                328,
                "test@expired.token",
                "test123",
                "mouqspHpCosdRbQt-8zb-8m0k4kv6D1b",
                true
        );

        private static String token;

        @BeforeAll
        static void setup(){
            userRepository.saveUser(testUser);
            token = userRepository.getNewAccessToken(testUser.getEmail(), testUser.getPassword());
            testUser.setUniqueId(userRepository.getUserDetails(testUser.getEmail()).getUniqueId());
        }

        @AfterAll
        static void afterAll(){
            userRepository.deleteAccount(testUser.getEmail(), testUser.getPassword());
        }

        @Test
        void generateTokenWrongEmail(){
            String result = userRepository.getNewAccessToken("wrong@email.com", "test123");

            assertNull(result, "The token should be null because an account with this email does not exist.");
        }

        @Test
        void generateTokenWrongPassword(){
            String result = userRepository.getNewAccessToken("test@testmail.com", "wrongpassword");

            assertNull(result, "The token should be null because an incorrect password was input.");

        }

        @Test
        void generateTokenEmptyInputs(){
            String result = userRepository.getNewAccessToken("", "");

            assertNull(result, "The result should be null because the inputs where empty.");
        }

        @Test
        void generateTokenNullInputs(){
            String result = userRepository.getNewAccessToken(null, null);

            assertNull(result, "The result should be null because the inputs where empty.");
        }

        @Test
        void verifyAccessTokenWrongID(){
            TokenStatus result = userRepository.verifyAccessToken(-1, token);

            assertEquals(TokenStatus.NO_MATCH, result, "The token should return NO_MATCH because a user with this ID does not exist");
        }

        @Test
        void verifyAccessTokenWrongToken(){
            TokenStatus result = userRepository.verifyAccessToken(testUser.getUniqueId(), "123123asd");

            assertEquals(TokenStatus.NO_MATCH, result, "The token should return NO_MATCH because a user with this ID does not exist");
        }

        @Test
        void verifyAccessTokenExpired(){
            TokenStatus result = userRepository.verifyAccessToken(expiredUser.getUniqueId(), expiredUser.getAccessToken());

            assertEquals(TokenStatus.EXPIRED, result, "The token should return EXPIRED because a the access token is expired.");
        }

        @Test
        void deleteAccessTokenWrongEmail(){
            boolean result = userRepository.deleteAccessToken("wrongemail@email.com", "test123");

            assertFalse(result, "The test should've returned false because no user with this email exists.");
        }

        @Test
        void deleteAccessTokenWrongPassword(){
            boolean result = userRepository.deleteAccessToken("test@testmail.com", "wrongpassword");

            assertFalse(result, "The test should've returned false because the wrong password was input.");
        }

        @Test
        void deleteAccessTokenEmptyInput(){
            boolean result = userRepository.deleteAccessToken("", "");

            assertFalse(result, "The test should've returned false because the inputs are empty.");
        }

        @Test
        void deleteAccessTokenNullInput(){
            boolean result = userRepository.deleteAccessToken(null, null);

            assertFalse(result, "The test should've returned false because the inputs are null.");
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Order(6)
    class TestSecurityQuestion{
        public static String newPassword = "newPassword123";

        @BeforeAll
        static void setup(){
            userRepository.saveUser(testUser);
        }

        @Test
        @Order(1)
        void checkSecurityQuestion(){
            assertEquals(testUser.getSecurityQuestion(), userRepository.getSecurityQuestion(testUser.getEmail()));
        }

        @Test
        @Order(2)
        void checkWrongSecurityQuestion(){
            assertNotEquals("not security question clue", userRepository.getSecurityQuestion(testUser.getEmail()));
        }

        @Test
        @Order(3)
        void verifySecurityAnswer(){
            assertTrue(userRepository.verifySecurityQuestion(testUser.getEmail(), testUser.getSecurityAnswer()));
        }

        @Test
        @Order(4)
        void verifyWrongSecurityAnswer(){
            assertFalse(userRepository.verifySecurityQuestion(testUser.getEmail(), "not security question answer"));
        }

        @Test
        @Order(5)
        void changePassword(){
            assertTrue(userRepository.updatePasswordWithSecurityQuestion(testUser.getEmail(), testUser.getSecurityAnswer(), newPassword));
        }

        @Test
        @Order(6)
        void checkNewPassword(){
            assertTrue(userRepository.checkLogin(testUser.getEmail(), newPassword));
        }

        @Test
        @Order(7)
        void changePasswordWrongAnswer(){
            assertFalse(userRepository.updatePasswordWithSecurityQuestion(testUser.getEmail(), "not security question answer", newPassword));
        }

        @AfterAll
        static void close(){
            userRepository.deleteAccount(testUser.getEmail(), newPassword);
        }
    }

    @Test
    void testCheckSecurityQuestionLegalValidInput() {
        String validQuestion = "What is your favorite color?";
        String validAnswer = "Blue";
        assertTrue(userRepository.checkSecurityQuestionLegal(validQuestion, validAnswer),
                "The security question and answer should be valid.");
    }

    @Test
    void testCheckSecurityQuestionLegalEmptyQuestion() {
        String emptyQuestion = "";
        String validAnswer = "Blue";
        assertFalse(userRepository.checkSecurityQuestionLegal(emptyQuestion, validAnswer),
                "The security question should be invalid if it is empty.");
    }

    @Test
    void testCheckSecurityQuestionLegalEmptyAnswer() {
        String validQuestion = "What is your favorite color?";
        String emptyAnswer = "";
        assertFalse(userRepository.checkSecurityQuestionLegal(validQuestion, emptyAnswer),
                "The security answer should be invalid if it is empty.");
    }

    @Test
    void testCheckSecurityQuestionLegalQuestionTooLong() {
        String longQuestion = "What is your favorite color? What is your favorite food? What is your favorite movie? What is your favorite book?";
        String validAnswer = "Blue";
        assertFalse(userRepository.checkSecurityQuestionLegal(longQuestion, validAnswer),
                "The security question should be invalid if it exceeds the maximum length.");
    }

    @Test
    void testCheckSecurityQuestionLegalAnswerTooLong() {
        String validQuestion = "What is your favorite color?";
        String longAnswer = "BlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlueBlue";
        assertFalse(userRepository.checkSecurityQuestionLegal(validQuestion, longAnswer),
                "The security answer should be invalid if it exceeds the maximum length.");
    }
}
