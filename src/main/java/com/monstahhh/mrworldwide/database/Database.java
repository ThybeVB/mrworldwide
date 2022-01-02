package com.monstahhh.mrworldwide.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    protected Connection connection;

    public Database() {
        if (this.connect())
            this.initialize();
    }

    private boolean connect() {
        Connection conn;
        try {
            String url = "jdbc:sqlite:mrworldwide.db";
            conn = DriverManager.getConnection(url);

            this.connection = conn;
            System.out.println("Connected to SQLite database");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void initialize() {
        String initUsersTable =
                "CREATE TABLE IF NOT EXISTS users " +
                "(userId INTEGER PRIMARY KEY," +
                "city TEXT," +
                "country TEXT," +
                "clockType TEXT);";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(initUsersTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
