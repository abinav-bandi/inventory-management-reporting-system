package com.inventory.dao;

import com.inventory.model.User;
import com.inventory.util.DBConnection;

import java.sql.*;

public class UserDAOImpl implements UserDao {
    private final Connection conn = DBConnection.getConnection();

    @Override
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setVerified(rs.getBoolean("verified")); // ✅ read verified status
                return user;
            } else {
                return null;
            }
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, email, verified) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isVerified()); // ✅ set verified flag
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ User already exists in the database: " + user.getUsername());
        }
    }

    @Override
    public boolean deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true if user deleted
        }
    }

    public void verifyUser(String username) throws SQLException {
        String sql = "UPDATE users SET verified = TRUE WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    @Override
    public User getUserByEmail(final String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(4, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setVerified(rs.getBoolean("verified"));
                return user;
            }
        }
        return null;
    }
}
