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
            if (clicked.getType() == Material.STRUCTURE_VOID) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §cFähigkeiten abgerüstet §8«")
                );
                player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1.0f, 0.6f);
                player.getInventory().setItem(4, new ItemBuilder(Material.GRAY_DYE).setName("§8» §7Keine Fähigkeit §8«").setLore(List.of("§7Du kannst eine Fähigkeit mit der Kiste auswählen")).build());
            }
            if (clicked.getType() == Material.FEATHER) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §bBoost §8«")
                );
                player.playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 0.6f);
                player.getInventory().setItem(4, new ItemBuilder(Material.FEATHER).setName("§8» §bBoost §8«").setLore(List.of("§aSchleudert dich nach vorne")).build());
            }

            if (clicked.getType() == Material.GUNPOWDER) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §7Rauchbombe §8«")
                );
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
                player.getInventory().setItem(4, new ItemBuilder(Material.GUNPOWDER).setName("§8» §7Rauchbombe §8«").setLore(List.of("§7Erzeugt eine Rauchwolke", "§7Klicke zum auslösen")).build());
            }

            if (clicked.getType() == Material.FIREWORK_ROCKET) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §cRakete §8«")
                );
                player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 0.8f);
                player.getInventory().setItem(4, new ItemBuilder(Material.FIREWORK_ROCKET).setName("§8» §cRakete §8«").setLore(List.of("§7Schießt dich nach oben", "§7Klicke zum auslösen")).build());
            }

            if (clicked.getType() == Material.SUGAR) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §6Turbo §8«")
                );
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.getInventory().setItem(4, new ItemBuilder(Material.SUGAR).setName("§8» §6Turbo §8«").setLore(List.of("§7Gibt dir kurzzeitig Geschwindigkeit", "§7Klicke zum auslösen")).build());
            }

            if (clicked.getType() == Material.BLAZE_POWDER) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §eBlitz §8«")
                );
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
                player.getInventory().setItem(4, new ItemBuilder(Material.BLAZE_POWDER).setName("§8» §eBlitz §8«").setLore(List.of("§7Zeigt einen Blitz-Effekt an", "§7Klicke zum auslösen")).build());
            }

            if (clicked.getType() == Material.SLIME_BALL) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §aSprungpad §8«")
                );
                player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1.0f, 1.0f);
                player.getInventory().setItem(4, new ItemBuilder(Material.SLIME_BALL).setName("§8» §aSprungpad §8«").setLore(List.of("§7Stoß dich nach oben", "§7Klicke zum auslösen")).build());
            }

            if (clicked.getType() == Material.EGG) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §dKonfetti §8«")
                );
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
                player.getInventory().setItem(4, new ItemBuilder(Material.EGG).setName("§8» §dKonfetti §8«").setLore(List.of("§7Feuert Konfetti ab", "§7Klicke zum ausrüsten")).build());
            }

            if (clicked.getType() == Material.BONE) {
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §aAusgerüstet: §7Schild §8«")
                );
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
                player.getInventory().setItem(4, new ItemBuilder(Material.BONE).setName("§8» §7Schild §8«").setLore(List.of("§7Gibt kurzzeitig Resistenz", "§7Klicke zum ausrüsten")).build());
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
}