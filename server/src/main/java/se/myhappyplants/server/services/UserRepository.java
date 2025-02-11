package se.myhappyplants.server.services;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.User;

import java.sql.*;

/**
 * Class responsible for calling the database about users.
 * Created by: Frida Jacobsson 2021-03-30
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class UserRepository {

    private DatabaseConnection connection;

    public UserRepository(DatabaseConnection connection) {
        this.connection = connection;
    }
    /**
     * Method to save a new user using BCrypt.
     *
     * @param user An instance of a newly created User that should be stored in the database.
     * @return A boolean value, true if the user was stored successfully
     */
    public boolean saveUser(User user) {
        boolean success = false;
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String query = """
        INSERT INTO registered_users (email, password) VALUES (?, ?);
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.executeUpdate();
            success = true;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return success;
    }

    /**
     * Method to check if a user exists in database.
     * Purpose of method is to make it possible for user to log in
     *
     * @param email    typed email from client and the application
     * @param password typed password from client and the application
     * @return A boolean value, true if the user exist in database and the password is correct
     */
    public boolean checkLogin(String email, String password) {
        boolean isVerified = false;
        String query = """
        SELECT ? FROM registered_users WHERE email = ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString(1);
                isVerified = BCrypt.checkpw(password, hashedPassword);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return isVerified;
    }

    /**
     * Method to get information (id, username and notification status) about a specific user
     *
     * @param email ??
     * @return a new instance of USer
     */
    public User getUserDetails(String email) {
        User user = null;
        String query = """
        SELECT id, notification_activated, fun_facts_activated FROM registered_users WHERE email = ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            while (resultSet.next()) {
                int uniqueID = resultSet.getInt(1);
                String username = resultSet.getString(2);
                boolean notificationActivated = resultSet.getBoolean(3);
                boolean funFactsActivated = resultSet.getBoolean(4);
                user = new User(uniqueID, email, username, notificationActivated, funFactsActivated);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return user;
    }

    /**
     * Method to delete a user and all plants in user library at once
     * author: Frida Jacobsson
     *
     * @param email
     * @param password
     * @return boolean value, false if transaction is rolled back
     */
    public boolean deleteAccount(String email, String password) {
        boolean accountDeleted = false;
        if (checkLogin(email, password)) {
            String query = """
            SELECT id FROM registered_users WHERE email = ?;
            """;
            try {
                java.sql.Connection sqlConn = connection.getConnection();
                sqlConn.setAutoCommit(false);
                try (PreparedStatement preparedStatement = sqlConn.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new SQLException();
                    }
                    int id = resultSet.getInt(1);
                    String queryDeletePlants = """
                    DELETE FROM user_plants WHERE user_id = ?;
                    """;
                    try (PreparedStatement deletePlantsStatement = sqlConn.prepareStatement(queryDeletePlants)) {
                        deletePlantsStatement.setInt(1, id);
                        deletePlantsStatement.executeUpdate();
                    }
                    String queryDeleteUser = """
                    DELETE FROM registered_users WHERE id = ?;
                    """;
                    try (PreparedStatement deleteUserStatement = sqlConn.prepareStatement(queryDeleteUser)) {
                        deleteUserStatement.setInt(1, id);
                        deleteUserStatement.executeUpdate();
                    }
                    accountDeleted = true;
                } catch (SQLException sqlException) {
                    sqlConn.rollback();
                    System.out.println("Transaction rolled back");
                    System.out.println("Account was not deleted");
                    System.out.println(sqlException.getMessage());
                } finally {
                    sqlConn.setAutoCommit(true);
                    connection.closeConnection();
                }
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
        return accountDeleted;
    }

    public boolean changeNotifications(String email, boolean notifications) {
        boolean notificationsChanged = false;
        String query = """
        UPDATE registered_users SET notification_activated = ? WHERE email = ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setBoolean(1, notifications);
            preparedStatement.setString(2, email);
            notificationsChanged = true;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return notificationsChanged;
    }

    public boolean changeFunFacts(String email, boolean funFactsActivated) {
        boolean funFactsChanged = false;
        String query = """
        UPDATE registered_users SET fun_facts_activated = ? WHERE email = ?;
        """;
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setBoolean(1, funFactsActivated);
            preparedStatement.setString(2, email);
            funFactsChanged = true;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            connection.closeConnection();
        }
        return funFactsChanged;
    }
}

