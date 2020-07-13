package me.bananentoast.minecrafttycoon.util;

import java.sql.*;

public class LiteStorage {

    private String filePath;
    private String fileName;

    private Connection connection;

    public LiteStorage(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + filePath + fileName);
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void update(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public ResultSet query(String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return resultSet;
    }

}
