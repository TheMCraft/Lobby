package com.themcraft.lobby;


import com.themcraft.lobby.Commands.BuildCommand;
import com.themcraft.lobby.Commands.LobbyCommand;
import com.themcraft.lobby.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;



import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private DatabaseManager dbManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Logger logger = getLogger();

        // Erstelle die config.yml im Plugin-Ordner, falls noch nicht vorhanden
        saveDefaultConfig();
        // Listener werden nachher registriert

        FileConfiguration cfg = getConfig();

        String host = cfg.getString("database.host", "localhost");
        int port = cfg.getInt("database.port", 3306);
        String name = cfg.getString("database.name", "minecraft");
        String user = cfg.getString("database.user", "root");
        String password = cfg.getString("database.password", "password");

        // Prüfen, ob der Benutzer die Platzhalterwerte noch nicht geändert hat
        boolean isPlaceholder = "localhost".equals(host) && port == 3306 && "minecraft".equals(name)
                && "root".equals(user) && "password".equals(password);

        if (isPlaceholder) {
            logger.info("[Lobby] Es wurden noch keine Datenbank verbindung eingerichtet.");
            return;
        }

        // Versuche die Verbindung aufzubauen
        dbManager = new DatabaseManager(host, port, name, user, password, logger);
        if (dbManager.connect()) {
            logger.info("[Lobby] Datenbank verbindung hergestellt.");
        } else {
            logger.warning("[Lobby] Datenbank verbindung konnte nicht hergestellt werden. Siehe vorherige Fehlermeldungen.");
        }
        // Registriere Listener
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new AbilityListener(this), this);

        // Commands registrieren
        getCommand("lobby").setExecutor(new LobbyCommand(this));
        getCommand("setlobby").setExecutor(new LobbyCommand(this));
        getCommand("build").setExecutor(new BuildCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (dbManager != null) {
            dbManager.disconnect();
        }
    }
}

