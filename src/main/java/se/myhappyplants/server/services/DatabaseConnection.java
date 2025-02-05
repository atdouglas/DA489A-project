package se.myhappyplants.server.services;

import se.myhappyplants.server.PasswordsAndKeys;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for handling connection with a specific database
 * Created by: Frida Jacobsson 2021-05-21
 */
public class DatabaseConnection {
    private java.sql.Connection conn;

    public DatabaseConnection() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public java.sql.Connection getConnection() {
        if(conn==null) {
            try {
                conn = DriverManager.getConnection(PasswordsAndKeys.dbServerAddress, PasswordsAndKeys.dbUsername, PasswordsAndKeys.dbPassword);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        }
        catch (SQLException sqlException) {
           //do nothing when this occurs, we don't care about this exception
        }
        conn = null;
    }
}
