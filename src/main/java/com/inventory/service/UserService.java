package com.inventory.service;

import com.inventory.dao.UserDAOImpl;
import com.inventory.dao.UserDao;
import com.inventory.model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    private final UserDao userDAO;

    public UserService() throws SQLException {
        this.userDAO = new UserDAOImpl();
    }

    public boolean register(String username, String password, String role,String email) throws SQLException {
        try {
            // Check if username already exists
            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null) {
                System.out.println("❌ Username already exists. Please try a different one.");
                return false;
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole(role.toUpperCase());
            newUser.setEmail(email);
            newUser.setVerified(false); // initially unverified

            // Save to database
            userDAO.addUser(newUser);
            String otp = OtpService.generateOTP(email); // generate OTP
            EmailService.sendOtp(email, otp);          // send OTP via email
            Scanner sc = new Scanner(System.in);
            System.out.print("📧 Enter OTP: ");
            String enteredOtp = sc.nextLine().trim();

            // Validate OTP
            if (OtpService.validateOTP(email, enteredOtp)) {
                // Mark user as verified
                userDAO.verifyUser(username);
                System.out.println("✅ Email verified successfully! Registration complete.");
                return true;
            } else {
                System.out.println("❌ Invalid OTP. Registration failed.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Database error during registration: " + e.getMessage());
            return false;
        }
    }
    public User login(String username, String password) throws SQLException {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user == null) return null;

            if (!user.getPassword().equals(password)) return null;

            if (!user.isVerified()) {
                System.out.println("❌ User not verified. Please complete email verification first.");
                return null;
            }

            return user;

        } catch (SQLException e) {
            System.out.println("⚠️ Database error during login: " + e.getMessage());
            return null;
        }
    }
    public boolean verifyEmail(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("❌ User not found.");
            return false;
        }

        if (user.isVerified()) {
            System.out.println("✅ User already verified.");
            return true;
        }

        // Generate OTP and send email
        String otp = OtpService.generateOTP(user.getEmail());
        EmailService.sendOtp(user.getEmail(), otp);
        System.out.println("📧 OTP sent to " + user.getEmail()+ ". Please enter the OTP to verify.");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter OTP: ");
        String enteredOtp = sc.nextLine().trim();

        if (OtpService.validateOTP(user.getEmail(), enteredOtp)) {
            userDAO.verifyUser(username);
            System.out.println("✅ Email verified successfully!");
            return true;
        } else {
            System.out.println("❌ Invalid OTP. Verification failed.");
            return false;
        }
    }

}
