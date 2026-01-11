package com.themcraft.lobby.Listeners;

import com.themcraft.lobby.Main;
import com.themcraft.lobby.Commands.LobbyCommand;
import com.themcraft.lobby.Util.ItemBuilder;
import com.themcraft.lobby.DatabaseManager;
import com.themcraft.lobby.Model.UserData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.DisplaySlot;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JoinListener implements Listener {
    private final Main plugin;
    private final DatabaseManager db;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
        this.db = plugin.getDatabaseManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        player.sendTitle("§e§lWillkommen", "§f§k- " + plugin.getConfig().getString("general.main-color", "&a").replaceAll("&", "§") + "auf " + plugin.getConfig().getString("general.prefix", "&a&lLobby").replaceAll("&", "§") + plugin.getConfig().getString("general.main-color", "&a").replaceAll("&", "§") +"! §f§k-", 10, 70, 20);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.9f);

        player.setGameMode(GameMode.ADVENTURE);

        LobbyCommand.teleportToLobby(player);

        player.addPotionEffect(
                new PotionEffect(
                        PotionEffectType.REGENERATION,
                        PotionEffect.INFINITE_DURATION,
                        0,
                        false,
                        false,
                        false
                )
        );

        player.setHealth(20);
        player.setSaturation(20);

        player.setLevel(LocalDate.now().getYear());
        player.setExp(1.0f);

        try {
            db.createUserIfNotExists(player.getUniqueId(), player.getName());
            UserData data = db.getUser(player.getUniqueId());
            giveSpawnItems(player);
            if (data != null && data.getAbilityEquipped() != null && !data.getAbilityEquipped().isBlank()) {
                String ability = data.getAbilityEquipped();
                Material mat = materialForAbility(ability);
                ItemStack abilityItem = new ItemBuilder(mat).setName(displayNameForAbility(ability)).setLore(loreForAbility(mat)).build();
                player.getInventory().setItem(4, abilityItem);
            } else {
                player.getInventory().setItem(4, new ItemBuilder(Material.GRAY_DYE).setName("§8» §7Keine Fähigkeit §8«").setLore(List.of("§7Du kannst eine Fähigkeit mit der Kiste auswählen")).build());
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("DB Fehler beim Join von " + player.getName() + ": " + ex.getMessage());
            giveSpawnItems(player);
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard board = manager.getNewScoreboard();
            Objective obj = board.registerNewObjective("lobby", "dummy");
            obj.setDisplayName("§6§lLobby");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.getScore("Spieler: §a" + player.getName()).setScore(1);
            player.setScoreboard(board);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String equipped = "";
        ItemStack item = player.getInventory().getItem(4);
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String name = item.getItemMeta().getDisplayName();
            equipped = ChatColor.stripColor(name).replace("»", "").replace("«", "").trim();
        }
        try {
            UserData existing = db.getUser(player.getUniqueId());
            List<String> unlocked = existing != null ? existing.getAbilitiesUnlocked() : List.of();
            int coins = existing != null ? existing.getCoins() : 0;
            UserData data = new UserData(player.getName(), player.getUniqueId(), equipped, unlocked, coins);
            db.saveUser(data);
        } catch (SQLException e) {
            plugin.getLogger().severe("DB Fehler beim Speichern von " + player.getName() + ": " + e.getMessage());
        }
    }

    private Material materialForAbility(String raw) {
        String s = raw == null ? "" : raw.toLowerCase().trim();
        if (s.contains("boost")) return Material.FEATHER;
        if (s.contains("rauch")) return Material.GUNPOWDER;
        if (s.contains("rakete") || s.contains("rocket")) return Material.FIREWORK_ROCKET;
        if (s.contains("turbo") || s.contains("speed") || s.contains("turbo")) return Material.SUGAR;
        if (s.contains("blitz")) return Material.BLAZE_POWDER;
        if (s.contains("sprung") || s.contains("jump")) return Material.SLIME_BALL;
        if (s.contains("konfetti")) return Material.EGG;
        if (s.contains("schild") || s.contains("shield")) return Material.BONE;
        return Material.GRAY_DYE;
    }

    private String displayNameForAbility(String raw) {
        String s = raw == null ? "" : raw.trim();
        if (s.equalsIgnoreCase("Boost")) return "§8» §bBoost §8«";
        if (s.equalsIgnoreCase("Rauchbombe")) return "§8» §7Rauchbombe §8«";
        if (s.equalsIgnoreCase("Rakete")) return "§8» §cRakete §8«";
        if (s.equalsIgnoreCase("Turbo")) return "§8» §6Turbo §8«";
        if (s.equalsIgnoreCase("Blitz")) return "§8» §eBlitz §8«";
        if (s.equalsIgnoreCase("Sprungpad")) return "§8» §aSprungpad §8«";
        if (s.equalsIgnoreCase("Konfetti")) return "§8» §dKonfetti §8«";
        if (s.equalsIgnoreCase("Schild")) return "§8» §7Schild §8«";
        return "§8» §7Keine Fähigkeit §8«";
    }

    private List<String> loreForAbility(Material mat) {
        if (mat == Material.FEATHER) return List.of("§aSchleudert dich nach vorne");
        if (mat == Material.GUNPOWDER) return List.of("§7Erzeugt eine Rauchwolke", "§7Klicke zum auslösen");
        if (mat == Material.FIREWORK_ROCKET) return List.of("§7Schießt dich nach oben", "§7Klicke zum auslösen");
        if (mat == Material.SUGAR) return List.of("§7Gibt dir kurzzeitig Geschwindigkeit", "§7Klicke zum auslösen");
        if (mat == Material.BLAZE_POWDER) return List.of("§7Zeigt einen Blitz-Effekt an", "§7Klicke zum auslösen");
        if (mat == Material.SLIME_BALL) return List.of("§7Stoß dich nach oben", "§7Klicke zum auslösen");
        if (mat == Material.EGG) return List.of("§7Feuert Konfetti ab", "§7Klicke zum ausrüsten");
        if (mat == Material.BONE) return List.of("§7Gibt kurzzeitig Resistenz", "§7Klicke zum ausrüsten");
        return List.of("§7Du kannst eine Fähigkeit mit der Kiste auswählen");
    }

    public static void giveSpawnItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("§8» §eServerauswahl §8«").setLore(List.of("§7Rechtsklick öffnet das Menü")).build());
        player.getInventory().setItem(4, new ItemBuilder(Material.GRAY_DYE).setName("§8» §7Keine Fähigkeit §8«").setLore(List.of("§7Du kannst eine Fähigkeit mit der Kiste auswählen")).build());
        player.getInventory().setItem(8, new ItemBuilder(Material.CHEST).setName("§8» §eFähigkeiten §8«").setLore(List.of("§7Klicke um eine Fähigkeit auszuwählen")).build());

    }
}
