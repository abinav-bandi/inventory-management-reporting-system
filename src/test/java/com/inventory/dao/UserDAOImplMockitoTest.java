package com.inventory.dao;

import com.inventory.model.User;
import com.inventory.util.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOImplMockitoTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private UserDAOImpl userDAO;
    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
        userDAO = new UserDAOImpl();
    }

    @AfterEach
    void tearDown() {
        if (mockedDBConnection != null) {
            mockedDBConnection.close();
        }
    }

    // ===== Test addUser() - success =====
    @Test
    void testAddUserSuccess() throws Exception {
        String uniqueUsername = "testuser_" + System.currentTimeMillis();
        User user = new User(uniqueUsername, "123", "ADMIN", "test@example.com", true);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        userDAO.addUser(user);

        verify(mockPreparedStatement).setString(1, uniqueUsername);
        verify(mockPreparedStatement).setString(2, "123");
        verify(mockPreparedStatement).setString(3, "ADMIN");
        verify(mockPreparedStatement).setString(4, "test@example.com");
        // Match DAO behavior: DAO sets verified = false by default
        verify(mockPreparedStatement).setBoolean(5, false);
        verify(mockPreparedStatement).executeUpdate();
    }
    // ===== Test addUser() - duplicate username =====
    @Test
    void testAddUserDuplicateUsername() throws Exception {
        User user = new User("duplicateUser", "123", "ADMIN", "dup@example.com", true);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        doThrow(new SQLIntegrityConstraintViolationException("Duplicate username"))
                .when(mockPreparedStatement).executeUpdate();

        assertDoesNotThrow(() -> userDAO.addUser(user));
        verify(mockPreparedStatement).executeUpdate();
    }

    // ===== Test getUserByUsername() - success =====
    @Test
    void testGetUserByUsernameSuccess() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("username")).thenReturn("uniqueUser");
        when(mockResultSet.getString("password")).thenReturn("123");
        when(mockResultSet.getString("role")).thenReturn("ADMIN");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        // DAO sets verified = false
        when(mockResultSet.getBoolean("isVerified")).thenReturn(false);

        User result = userDAO.getUserByUsername("uniqueUser");

        assertNotNull(result);
        assertEquals("uniqueUser", result.getUsername());
        assertEquals("ADMIN", result.getRole());
        assertEquals("test@example.com", result.getEmail());
        assertFalse(result.isVerified());
    }

    // ===== Test getUserByUsername() - user not found =====
    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        User result = userDAO.getUserByUsername("nonexistent");
        assertNull(result);
    }
}
