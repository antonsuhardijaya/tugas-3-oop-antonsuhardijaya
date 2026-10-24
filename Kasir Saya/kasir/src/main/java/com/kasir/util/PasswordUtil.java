package com.kasir.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Hash password dengan PBKDF2 (bawaan JDK). Format: iterasi:salt:hash (hex). */
public class PasswordUtil {
    private static final int ITER = 65000;

    public static String hash(String pw) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return ITER + ":" + toHex(salt) + ":" + toHex(pbkdf2(pw, salt, ITER, 256));
    }

    public static boolean verify(String pw, String stored) {
        try {
            String[] p = stored.split(":");
            if (p.length != 3) return false;
            byte[] expected = fromHex(p[2]);
            byte[] actual = pbkdf2(pw, fromHex(p[1]), Integer.parseInt(p[0]), expected.length * 8);
            return MessageDigest.isEqual(expected, actual);
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(String pw, byte[] salt, int iter, int bits) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return f.generateSecret(new PBEKeySpec(pw.toCharArray(), salt, iter, bits)).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }

    private static byte[] fromHex(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) b[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        return b;
    }
}
