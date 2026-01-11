package com.themcraft.lobby.Listeners;

import com.themcraft.lobby.Commands.LobbyCommand;
import com.themcraft.lobby.Main;
import com.themcraft.lobby.Util.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;
import com.themcraft.lobby.Util.InvBuilder;

import java.sql.SQLException;
import java.util.List;

public class MenuListener implements Listener {
    private final Main plugin;

    public MenuListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView() == null || event.getCurrentItem() == null) return;
        if (event.getInventory().getSize() >= 27 && event.getSlot() == event.getInventory().getSize() - 5 && event.getCurrentItem().getType().equals(Material.BARRIER)) event.getWhoClicked().closeInventory();

        ItemStack clicked = event.getCurrentItem();


        String title = event.getView().getTitle();
        if ("§eServerauswahl".equals(title)) {
            event.setCancelled(true);
            if (clicked.getType() == Material.NETHER_STAR) {
                Player player = (Player) event.getWhoClicked();
                LobbyCommand.teleportToLobby(player);
                player.closeInventory();
            }
        }
        if ("§eCosmetics".equals(title)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            java.util.UUID uuid = player.getUniqueId();

            if (clicked.getType() == Material.STRUCTURE_VOID) {
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §cFähigkeiten abgerüstet §8«")
                );
                player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1.0f, 0.6f);
                player.getInventory().setItem(4, new ItemBuilder(Material.GRAY_DYE).setName("§8» §7Keine Fähigkeit §8«").setLore(List.of("§7Klicke alle Fähigkeiten abzurüsten")).build());
                try {
                    plugin.getDatabaseManager().saveUser(new com.themcraft.lobby.Model.UserData(player.getName(), uuid, "", plugin.getDatabaseManager().getUser(uuid).getAbilitiesUnlocked(), plugin.getDatabaseManager().getCoins(uuid)));
                    updatePlayerSidebar(player);
                } catch (SQLException e) {
                    plugin.getLogger().severe("Fehler beim Speichern beim Abrüsten: " + e.getMessage());
                }
                return;
            }

            try {
                boolean unlocked = false;
                String abilityName = null;
                int price = 0;

                if (clicked.getType() == Material.FEATHER) { abilityName = "Boost"; price = plugin.getConfig().getInt("abilities.prices.Boost", 100); }
                if (clicked.getType() == Material.GUNPOWDER) { abilityName = "Rauchbombe"; price = plugin.getConfig().getInt("abilities.prices.Rauchbombe", 150); }
                if (clicked.getType() == Material.FIREWORK_ROCKET) { abilityName = "Rakete"; price = plugin.getConfig().getInt("abilities.prices.Rakete", 200); }
                if (clicked.getType() == Material.SUGAR) { abilityName = "Turbo"; price = plugin.getConfig().getInt("abilities.prices.Turbo", 120); }
                if (clicked.getType() == Material.BLAZE_POWDER) { abilityName = "Blitz"; price = plugin.getConfig().getInt("abilities.prices.Blitz", 180); }
                if (clicked.getType() == Material.SLIME_BALL) { abilityName = "Sprungpad"; price = plugin.getConfig().getInt("abilities.prices.Sprungpad", 130); }
                if (clicked.getType() == Material.EGG) { abilityName = "Konfetti"; price = plugin.getConfig().getInt("abilities.prices.Konfetti", 80); }
                if (clicked.getType() == Material.BONE) { abilityName = "Schild"; price = plugin.getConfig().getInt("abilities.prices.Schild", 160); }

                if (abilityName == null) return;

                unlocked = plugin.getDatabaseManager().isAbilityUnlocked(uuid, abilityName);

                if (unlocked) {
                    player.closeInventory();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§8» §aAusgerüstet: §e" + abilityName + " §8«"));
                    player.playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 0.6f);
                    player.getInventory().setItem(4, new ItemBuilder(clicked.getType()).setName(clicked.getItemMeta().getDisplayName()).setLore(clicked.getItemMeta().getLore()).build());
                    updatePlayerSidebar(player);
                    return;
                }

                int coins = plugin.getDatabaseManager().getCoins(uuid);
                if (coins < price) {
                    player.closeInventory();
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§8» §cDu hast nicht genug Coins (" + coins + " / " + price + ") §8«"));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.6f);
                    return;
                }

                plugin.getDatabaseManager().unlockAbility(uuid, abilityName);
                plugin.getDatabaseManager().addCoins(uuid, -price);

                player.closeInventory();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§8» §aGekauft & Ausgerüstet: §e" + abilityName + " §8«"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.getInventory().setItem(4, new ItemBuilder(clicked.getType()).setName(clicked.getItemMeta().getDisplayName()).setLore(clicked.getItemMeta().getLore()).build());
                updatePlayerSidebar(player);

            } catch (SQLException ex) {
                plugin.getLogger().severe("DB Fehler beim Kauf: " + ex.getMessage());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() == Material.COMPASS) {
            Inventory inv = Bukkit.createInventory(null, 9, "§eServerauswahl");
            inv.setItem(4, new ItemBuilder(Material.NETHER_STAR).setName("§8» §aLobby §8«").setLore(List.of("§7Klicke zum Teleportieren")).build());
            player.openInventory(inv);
        }
        if (event.getItem().getType() == Material.CHEST) {
            Inventory inv = Bukkit.createInventory(null, 36, "§eCosmetics");
            inv.setItem(10, new ItemBuilder(Material.STRUCTURE_VOID).setName("§8» §7Nichts §8«").setLore(List.of("§7Klicke alle Fähigkeiten abzurüsten")).build());
            inv.setItem(11, new ItemBuilder(Material.FEATHER).setName("§8» §bBoost §8«").setLore(List.of("§7Schleudert dich nach vorne", "§7Klicke zum ausrüsten")).build());
            inv.setItem(12, new ItemBuilder(Material.GUNPOWDER).setName("§8» §7Rauchbombe §8«").setLore(List.of("§7Erzeugt eine Rauchwolke", "§7Klicke zum ausrüsten")).build());
            inv.setItem(13, new ItemBuilder(Material.FIREWORK_ROCKET).setName("§8» §cRakete §8«").setLore(List.of("§7Schießt dich nach oben", "§7Klicke zum ausrüsten")).build());
            inv.setItem(14, new ItemBuilder(Material.SUGAR).setName("§8» §6Turbo §8«").setLore(List.of("§7Gibt dir kurzzeitig Geschwindigkeit", "§7Klicke zum ausrüsten")).build());
            inv.setItem(15, new ItemBuilder(Material.BLAZE_POWDER).setName("§8» §eBlitz §8«").setLore(List.of("§7Zeigt einen Blitz-Effekt an", "§7Klicke zum ausrüsten")).build());
            inv.setItem(16, new ItemBuilder(Material.SLIME_BALL).setName("§8» §aSprungpad §8«").setLore(List.of("§7Stoß dich nach oben", "§7Klicke zum ausrüsten")).build());
            inv.setItem(19, new ItemBuilder(Material.EGG).setName("§8» §dKonfetti §8«").setLore(List.of("§7Feuert Konfetti ab", "§7Klicke zum ausrüsten")).build());
            inv.setItem(20, new ItemBuilder(Material.BONE).setName("§8» §7Schild §8«").setLore(List.of("§7Gibt kurzzeitig Resistenz", "§7Klicke zum ausrüsten")).build());
            InvBuilder.formatInventory(inv);
            player.openInventory(inv);
        }
    }

    private void updatePlayerSidebar(Player player) {
        try {
            int coins = plugin.getDatabaseManager().getCoins(player.getUniqueId());
            com.themcraft.lobby.Model.UserData ud = plugin.getDatabaseManager().getUser(player.getUniqueId());
            String ability = (ud != null && ud.getAbilityEquipped() != null && !ud.getAbilityEquipped().isBlank()) ? ud.getAbilityEquipped() : "";
            String prefix = plugin.getConfig().getString("general.prefix", "&a&lLobby").replaceAll("&", "§");
            String mainColor = plugin.getConfig().getString("general.main-color", "&a").replaceAll("&", "§");
            String secColor = plugin.getConfig().getString("general.secondary-color", "&7").replaceAll("&", "§");

            org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
            if (board == null) {
                org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
                if (manager != null) board = manager.getNewScoreboard();
            }
            org.bukkit.scoreboard.Objective obj = board.getObjective("lobby");
            if (obj == null) {
                obj = board.registerNewObjective("lobby", "dummy");
                obj.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
            }
            obj.setDisplayName(prefix);

            String empty0 = "\u200B";
            String line1 = mainColor + "§lSpieler";
            String line2 = secColor + player.getName();
            String empty3 = "\u200C";
            String line4 = mainColor + "§lCoins";
            String line5 = secColor + coins;
            String empty6 = "\u200D";

            obj.getScore(empty0).setScore(6);
            obj.getScore(line1).setScore(5);
            obj.getScore(line2).setScore(4);
            obj.getScore(empty3).setScore(3);
            obj.getScore(line4).setScore(2);
            obj.getScore(line5).setScore(1);
            obj.getScore(empty6).setScore(0);

            player.setScoreboard(board);
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Aktualisieren des Sidebars: " + e.getMessage());
        }
    }
}
