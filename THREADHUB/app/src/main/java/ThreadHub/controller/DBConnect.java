package ThreadHub.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DBConnect {
    private static final String DB_URL = "jdbc:sqlite:data/threadhub.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Koneksi SQLite gagal: " + e.getMessage());
        }
        return conn;
    }
}
