package com.themcraft.lobby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseManager {
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private Connection connection;
    private final Logger logger;

    public DatabaseManager(String host, int port, String database, String user, String password, Logger logger) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.logger = logger;
    }

    public boolean connect() {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host, port, database);
        try {
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            logger.severe("[Lobby] Datenbankverbindung fehlgeschlagen: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                logger.warning("[Lobby] Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        if (!isConnected()) throw new SQLException("Not connected to database");
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        if (!isConnected()) throw new SQLException("Not connected to database");
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(sql);
    }
}

