package com.themcraft.lobby;

import com.themcraft.lobby.Model.UserData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class DatabaseManager {
    private final JavaPlugin plugin;
    private Connection connection;
    private String host;
    private int port;
    private String databaseName;
    private String user;
    private String password;
    private Logger logger;
    private final boolean isMySQL;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.isMySQL = false;
    }

    public DatabaseManager(String host, int port, String databaseName, String user, String password, Logger logger) {
        this.plugin = null;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
        this.logger = logger;
        this.isMySQL = true;
    }

    public boolean connect() {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            if (logger != null) logger.severe("MySQL Verbindung fehlgeschlagen: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored) {}
    }

    public void init() throws SQLException {
        if (plugin == null) throw new SQLException("Plugin instance not provided for SQLite init.");
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        String path = new File(dataFolder, "database.db").getAbsolutePath();
        String url = "jdbc:sqlite:" + path;
        connection = DriverManager.getConnection(url);
    }

    public void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            if (isMySQL) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user (username VARCHAR(32), uuid VARCHAR(36) PRIMARY KEY, ability_equipped VARCHAR(128), abilities_unlocked TEXT, coins INT DEFAULT 0) ENGINE=InnoDB;");
            } else {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user (username TEXT, uuid TEXT PRIMARY KEY, ability_equipped TEXT, abilities_unlocked TEXT, coins INTEGER DEFAULT 0);");
            }
        }
    }

    public void createUserIfNotExists(UUID uuid, String username) throws SQLException {
        if (getUser(uuid) == null) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user (username, uuid, ability_equipped, abilities_unlocked, coins) VALUES (?, ?, ?, ?, ?)"
            )) {
                ps.setString(1, username);
                ps.setString(2, uuid.toString());
                ps.setString(3, "");
                ps.setString(4, "");
                ps.setInt(5, 0);
                ps.executeUpdate();
            }
        } else {
            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE user SET username = ? WHERE uuid = ?"
            )) {
                ps.setString(1, username);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        }
    }

    public UserData getUser(UUID uuid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT username, uuid, ability_equipped, abilities_unlocked, coins FROM user WHERE uuid = ?"
        )) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String abilityEquipped = rs.getString("ability_equipped");
                    String abilitiesUnlocked = rs.getString("abilities_unlocked");
                    int coins = rs.getInt("coins");
                    List<String> unlocked = new ArrayList<>();
                    if (abilitiesUnlocked != null && !abilitiesUnlocked.isBlank()) {
                        for (String s : abilitiesUnlocked.split(",")) {
                            if (!s.isBlank()) unlocked.add(s.trim());
                        }
                    }
                    return new UserData(username, UUID.fromString(rs.getString("uuid")), abilityEquipped, unlocked, coins);
                }
            }
        }
        return null;
    }

    public void saveUser(UserData user) throws SQLException {
        if (isMySQL) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user (username, uuid, ability_equipped, abilities_unlocked, coins) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = VALUES(username), ability_equipped = VALUES(ability_equipped), abilities_unlocked = VALUES(abilities_unlocked), coins = VALUES(coins)"
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getUuid().toString());
                ps.setString(3, user.getAbilityEquipped() == null ? "" : user.getAbilityEquipped());
                ps.setString(4, String.join(",", user.getAbilitiesUnlocked()));
                ps.setInt(5, user.getCoins());
                ps.executeUpdate();
            }
        } else {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user (username, uuid, ability_equipped, abilities_unlocked, coins) VALUES (?, ?, ?, ?, ?)" +
                            " ON CONFLICT(uuid) DO UPDATE SET username = excluded.username, ability_equipped = excluded.ability_equipped, abilities_unlocked = excluded.abilities_unlocked, coins = excluded.coins"
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getUuid().toString());
                ps.setString(3, user.getAbilityEquipped() == null ? "" : user.getAbilityEquipped());
                ps.setString(4, String.join(",", user.getAbilitiesUnlocked()));
                ps.setInt(5, user.getCoins());
                ps.executeUpdate();
            }
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored) {}
    }
}
