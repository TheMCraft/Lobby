package com.themcraft.lobby.Listeners;

import com.themcraft.lobby.Main;
import com.themcraft.lobby.Commands.LobbyCommand;
import com.themcraft.lobby.Util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.DisplaySlot;

import java.time.LocalDate;
import java.util.List;

public class JoinListener implements Listener {
    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
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

        giveSpawnItems(player);

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
    public static void giveSpawnItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("§8» §eServerauswahl §8«").setLore(List.of("§7Rechtsklick öffnet das Menü")).build());
        player.getInventory().setItem(4, new ItemBuilder(Material.GRAY_DYE).setName("§8» §7Keine Fähigkeit §8«").setLore(List.of("§7Du kannst eine Fähigkeit mit der Kiste auswählen")).build());
        player.getInventory().setItem(8, new ItemBuilder(Material.CHEST).setName("§8» §eFähigkeiten §8«").setLore(List.of("§7Klicke um eine Fähigkeit auszuwählen")).build());

    }
}
