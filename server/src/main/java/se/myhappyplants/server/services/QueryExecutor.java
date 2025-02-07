package se.myhappyplants.server.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for handling querys to database
 * Created by: Frida Jacobsson 2021-05-21
 */
public class QueryExecutor {

    private DatabaseConnection connection;

    public QueryExecutor(DatabaseConnection connection) {
        this.connection = connection;
    }

    public void executeUpdate(String query) throws SQLException {
        int retries = 0;
        do {
            try {
                connection.getConnection().createStatement().executeUpdate(query);
                return;
            }
            catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                connection.closeConnection();
                retries++;
            }
        } while (retries < 3);
        throw new SQLException("No connection to database");
    }

    public ResultSet executeQuery(String query) throws SQLException {
        int retries = 0;
        do {
            try {
                return connection.getConnection().createStatement().executeQuery(query);
            }
            catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                connection.closeConnection();
                retries++;
            }
        } while (retries < 3);
        throw new SQLException("No connection to database");
    }

    public Statement beginTransaction() throws SQLException {
        int retries = 0;
        do {
            try {
                connection.getConnection().setAutoCommit(false);
                return connection.getConnection().createStatement();
            }
            catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
                connection.closeConnection();
                retries++;
            }
        }
        while (retries < 3);
        throw new SQLException("No connection to database");
    }

    public void endTransaction() throws SQLException {
        connection.getConnection().commit();
        connection.getConnection().setAutoCommit(true);
    }

    public void rollbackTransaction() throws SQLException {
        connection.getConnection().rollback();
    }
}
