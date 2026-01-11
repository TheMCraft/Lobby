package com.themcraft.lobby;


import com.themcraft.lobby.Commands.BuildCommand;
import com.themcraft.lobby.Commands.LobbyCommand;
import com.themcraft.lobby.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;



import java.sql.SQLException;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private DatabaseManager dbManager;

    @Override
    public void onEnable() {
        Logger logger = getLogger();

        saveDefaultConfig();

        FileConfiguration cfg = getConfig();

        String dbType = cfg.getString("database.type", "mysql").toLowerCase();

        String host = cfg.getString("database.host", "localhost");
        int port = cfg.getInt("database.port", 3306);
        String name = cfg.getString("database.name", "minecraft");
        String user = cfg.getString("database.user", "root");
        String password = cfg.getString("database.password", "password");

        boolean isPlaceholder = "localhost".equals(host) && port == 3306 && "minecraft".equals(name)
                && "root".equals(user) && "password".equals(password);

        if (!"mysql".equals(dbType) || isPlaceholder) {
            logger.severe("[Lobby] Keine gültige MySQL-Konfiguration gefunden. Stelle sicher, dass 'database.type' = 'mysql' und die Zugangsdaten gesetzt sind.");
            return;
        }

        dbManager = new DatabaseManager(host, port, name, user, password, logger);
        if (!dbManager.connect()) {
            logger.severe("[Lobby] Datenbank verbindung konnte nicht hergestellt werden. Siehe vorherige Fehlermeldungen.");
            return;
        }

        try {
            dbManager.createTables();
        } catch (SQLException e) {
            logger.severe("[Lobby] Tabellen konnten nicht erstellt werden: " + e.getMessage());
            return;
        }

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new AbilityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(this), this);

        getCommand("lobby").setExecutor(new LobbyCommand(this));
        getCommand("setlobby").setExecutor(new LobbyCommand(this));
        getCommand("build").setExecutor(new BuildCommand(this));

        applyWorldSettings();

        new BukkitRunnable() {
            @Override
            public void run() {
                applyWorldSettings();
            }
        }.runTaskTimer(this, 0L, 200L);
    }

    private void applyWorldSettings() {
        for (World world : Bukkit.getWorlds()) {
            try {
                world.setTime(6000L);
                world.setStorm(false);
                world.setThundering(false);
                world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onDisable() {
        if (dbManager != null) {
            dbManager.disconnect();
        }
    }

    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }
}
