package com.themcraft.lobby.Commands;

import com.themcraft.lobby.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LobbyCommand implements CommandExecutor {
    private static Main plugin = null;

    public LobbyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setlobby")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("lobby.set")) {
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §cKeine Rechte §8«")
                );
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.6f);
                return true;
            }

            plugin.getConfig().set("lobby.spawn.world", player.getWorld().getName());
            plugin.getConfig().set("lobby.spawn.x", player.getLocation().getX());
            plugin.getConfig().set("lobby.spawn.y", player.getLocation().getY());
            plugin.getConfig().set("lobby.spawn.z", player.getLocation().getZ());
            plugin.getConfig().set("lobby.spawn.yaw", player.getLocation().getYaw());
            plugin.getConfig().set("lobby.spawn.pitch", player.getLocation().getPitch());
            plugin.saveConfig();

            player.sendMessage("§aLobby-Spawn gespeichert.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("lobby")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
                return true;
            }
            Player player = (Player) sender;
            teleportToLobby(player);
            return true;
        }

        return false;
    }

    public static void teleportToLobby(Player player) {
        if (!plugin.getConfig().contains("lobby.spawn.world")) {
            return;
        }
        String worldName = plugin.getConfig().getString("lobby.spawn.world");
        double x = plugin.getConfig().getDouble("lobby.spawn.x");
        double y = plugin.getConfig().getDouble("lobby.spawn.y");
        double z = plugin.getConfig().getDouble("lobby.spawn.z");
        float yaw = (float) plugin.getConfig().getDouble("lobby.spawn.yaw");
        float pitch = (float) plugin.getConfig().getDouble("lobby.spawn.pitch");

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                20,
                0,
                false,
                false
        ));
        player.teleport(new Location(world, x, y, z, yaw, pitch));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }
}

