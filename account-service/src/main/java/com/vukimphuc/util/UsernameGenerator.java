package com.vukimphuc.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsernameGenerator {
    public static String generateUsernameFromEmail(String email) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(email.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            // Pad with leading zeros to ensure the length is consistent
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            // Remove non-alphanumeric characters and ensure the username doesn't start with a digit
            String cleanUsername = hashText.replaceAll("[^a-zA-Z0-9]", "");
            // Ensure the username doesn't start with a digit
            while (Character.isDigit(cleanUsername.charAt(0))) {
                cleanUsername = cleanUsername.substring(1) + (char) ('a' + Math.random() * ('z' - 'a')); // Replace the first digit with a letter
            }
            return "user_" + cleanUsername.substring(0, Math.min(8, cleanUsername.length())); // Limit the username length to 8 characters
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception
            return null;
        }
    }

}
