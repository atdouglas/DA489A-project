package se.myhappyplants.server.services;

import se.myhappyplants.server.PasswordsAndKeys;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static java.sql.Connection conn;

    public DatabaseConnection() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public synchronized java.sql.Connection getConnection() {
        if(conn==null) {
            try {
                conn = DriverManager.getConnection(PasswordsAndKeys.dbServerAddress, PasswordsAndKeys.dbUsername, PasswordsAndKeys.dbPassword);
            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
        return conn;
    }

    public synchronized void closeConnection() {
        try {
            conn.close();
        }
        catch (SQLException sqlException) {
           //do nothing when this occurs, we don't care about this exception
        }
        conn = null;
    }
}
