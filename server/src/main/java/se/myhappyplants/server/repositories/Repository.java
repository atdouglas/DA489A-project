package se.myhappyplants.server.repositories;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository {
    Dotenv dotenv = Dotenv.load();

    // use try-with-resources when starting connection
    public java.sql.Connection startConnection() {
        java.sql.Connection connection = null;
        try {
            connection = DriverManager.getConnection(dotenv.get("DB_SERVER_ADDRESS"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"));
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return connection;
    }
}
