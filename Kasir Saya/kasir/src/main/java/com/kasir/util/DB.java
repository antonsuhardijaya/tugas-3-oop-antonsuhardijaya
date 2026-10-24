package com.kasir.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Koneksi database MySQL. Sesuaikan URL, USER, dan PASS dengan MySQL Anda. */
public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/db_kasir"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
