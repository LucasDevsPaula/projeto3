package com.EBAC.conections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionDB {
    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimeZone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "yourpassword";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
