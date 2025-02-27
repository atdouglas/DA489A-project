package se.myhappyplants.server;


import java.sql.DriverManager;
import java.sql.SQLException;


//Currently not in use, but will stay while we make sure everything else works.

public class DatabaseConnectionHandler {
    public DatabaseConnectionHandler() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
}
