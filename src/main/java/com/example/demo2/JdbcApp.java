package com.example.demo2;

import java.sql.*;
import java.util.Random;

public class JdbcApp {

    private Connection connection;


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:javadb.db"))
//             Statement statement = connection.createStatement();
        {
            connection.setAutoCommit(false);
            createTable(connection);
            insert(connection, "Bob", 95);
//            insert(statement, "Ivan", 80);
//            insert(statement, "Smith", 75);
//            select(connection);
//            selectByName(connection, "Ivan' union select 1, sql, 1 from sqlite_master --");
//            dropById(connection, 1);
            //SELECT * FROM students WHERE name = 'Ivan' union select 1, sql, 1 from sqlite_master --'"
            bulkInsert(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void dropById(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
           connection.rollback();
        }
    }

    private static void selectByName(Connection connection, String name) throws SQLException {
        //       String query = String.format("SELECT * FROM students WHERE name = '%s'", name);
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameDB = resultSet.getString("name");
                int score = resultSet.getInt("score");
                System.out.printf("%d - %s - %d\n", id, nameDB, score);
            }
        }
//       ResultSet resultSet = statement.executeQuery(query);
//       while (resultSet.next()) {
//           int id = resultSet.getInt("id");
//           String nameDB = resultSet.getString("name");
//           int score = resultSet.getInt("score");
//           System.out.printf("%d - %s - %d\n", id, nameDB, score);
    }

    private static void select(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM students")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int score = resultSet.getInt("score");
                System.out.printf("%d - %s - %d\n", id, name, score);
            }
        }
    }

    private static void bulkInsert(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO students (name, score) VALUES (?, ?)")) {
            for (int i = 0; i < 1000; i++) {
                statement.setString(1, "name" + i);
                statement.setInt(2, new Random().nextInt(100));
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private static void insert(Connection connection, String name, int score) throws SQLException {
        //     statement.executeUpdate("INSERT INTO students (name, score) VALUES ('" + name + "'," + score + ")");
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO students (name, score) VALUES (?, ?)")) {
            statement.setString(1, name);
            statement.setInt(2, score);
            statement.executeUpdate();
        }
        //       String query = String.format("INSERT INTO students (name, score) VALUES ('%s', %d)", name, score);
    }

    private static void createTable(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("" +
                " CREATE TABLE IF NOT EXISTS  students (" +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "   name TEXT," +
                "   score INTEGER" +
                ")")) {
            statement.executeUpdate();
        }
    }
}
