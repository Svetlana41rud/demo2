package com.example.demo2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcApp {

    private Connection connection;


    public static void main(String[] args) {
        final JdbcApp jdbcApp = new JdbcApp();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:javadb.db")) {
               } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
