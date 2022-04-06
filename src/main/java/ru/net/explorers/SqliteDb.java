package ru.net.explorers;

// ! NOT WORKING AT ALL!
// !REWRITE IT AS SOON AS POSSIBLE!

import java.io.File;

import java.sql.*;

public class SqliteDb {
    private String url = null; // this.file.toString();
    public Connection connection = null;
    public Statement statement = null;

    public void open(File file) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.url = "jdbc:sqlite:." + file.toString();
            this.connection = DriverManager.getConnection(url);
            this.statement = connection.createStatement();

        } catch (Exception e) {

        }
    }
    // var statement: Statement = this.connection.createStatement();
    // var result: ResultSet = statement.executeQuery(sql);

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
    }

}