package se.myhappyplants.server.services;

import org.mindrot.jbcrypt.BCrypt;
import se.myhappyplants.shared.User;

import java.sql.*;

public class UserRepository extends Repository {

    public boolean saveUser(User user) {
        boolean success = false;
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String query = """
                INSERT INTO registered_users (email, password) VALUES (?, ?);
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return success;
    }

    public boolean checkLogin(String email, String password) {
        boolean isVerified = false;
        String query = """
                SELECT ? FROM registered_users WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, password);
                preparedStatement.setString(2, email);
                ResultSet resultSet = preparedStatement.executeQuery(query);
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString(1);
                    isVerified = BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return isVerified;
    }

    public User getUserDetails(String email) {
        User user = null;
        String query = """
                SELECT id, notification_activated, fun_facts_activated FROM registered_users WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery(query);
                while (resultSet.next()) {
                    int uniqueID = resultSet.getInt(1);
                    String username = resultSet.getString(2);
                    boolean notificationActivated = resultSet.getBoolean(3);
                    boolean funFactsActivated = resultSet.getBoolean(4);
                    user = new User(uniqueID, email, username, notificationActivated, funFactsActivated);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return user;
    }

    public boolean deleteAccount(String email, String password) {
        boolean accountDeleted = false;
        if (checkLogin(email, password)) {
            String query = """
                    SELECT id FROM registered_users WHERE email = ?;
                    """;
            try (Connection connection = startConnection()) {
                connection.setAutoCommit(false);
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new SQLException();
                    }
                    int id = resultSet.getInt(1);
                    String queryDeletePlants = """
                            DELETE FROM user_plants WHERE user_id = ?;
                            """;
                    try (PreparedStatement deletePlantsStatement = connection.prepareStatement(queryDeletePlants)) {
                        deletePlantsStatement.setInt(1, id);
                        deletePlantsStatement.executeUpdate();
                    }
                    String queryDeleteUser = """
                            DELETE FROM registered_users WHERE id = ?;
                            """;
                    try (PreparedStatement deleteUserStatement = connection.prepareStatement(queryDeleteUser)) {
                        deleteUserStatement.setInt(1, id);
                        deleteUserStatement.executeUpdate();
                    }
                    accountDeleted = true;
                } catch (SQLException sqlException) {
                    connection.rollback();
                    System.out.println("Transaction rolled back");
                    System.out.println("Account was not deleted");
                    System.out.println(sqlException.getMessage());
                } finally {
                    connection.setAutoCommit(true);
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
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, notifications);
                preparedStatement.setString(2, email);
                notificationsChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return notificationsChanged;
    }

    public boolean changeFunFacts(String email, boolean funFactsActivated) {
        boolean funFactsChanged = false;
        String query = """
                UPDATE registered_users SET fun_facts_activated = ? WHERE email = ?;
                """;
        try (java.sql.Connection connection = startConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBoolean(1, funFactsActivated);
                preparedStatement.setString(2, email);
                funFactsChanged = true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return funFactsChanged;
    }
}

