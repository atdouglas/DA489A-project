package se.myhappyplants.server.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {
    private DatabaseConnection mockDBConnection;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws SQLException{
        mockDBConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);

        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        userRepository = new UserRepository(mockDBConnection);
    }

    @Test
    void saveUserSuccess() throws SQLException{
        User user = new User("test@domain.com", "password123");
        when(mockStatement.executeUpdate()).thenReturn(1);

        boolean result = userRepository.saveUser(user);

        assertTrue(result);
        verify(mockStatement, times(1)).executeUpdate();
        verify(mockDBConnection, times(1)).closeConnection();
    }

}