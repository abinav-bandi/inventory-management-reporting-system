package com.inventory.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OtpService {

    // Temporary in-memory OTP storage
    private static final Map<String, String> otpStorage = new HashMap<>();

    // ======= Generate OTP =======
    public static String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        // Send the OTP using EmailService
        EmailService.sendOtp(email, otp);
        return otp;
    }

    // ======= Validate OTP =======
    public static boolean validateOTP(String email, String enteredOtp) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp == null) {
            System.out.println("❌ No OTP found for: " + email);
            return false;
        }

        if (storedOtp.equals(enteredOtp)) {
            otpStorage.remove(email); // One-time use
            return true;
        } else {
            System.out.println("❌ Invalid OTP entered!");
            return false;
        }
    }
}
