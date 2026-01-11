package com.themcraft.lobby.Commands;

import com.themcraft.lobby.Listeners.JoinListener;
import com.themcraft.lobby.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BuildCommand implements CommandExecutor {
    private final Main plugin;

    public static Map<Player, Boolean> buildMode = new HashMap<Player, Boolean>();

    public BuildCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("build")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("lobby.build")) {
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §cKeine Rechte §8«")
                );
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.6f);
                return true;
            }

            if (buildMode.containsKey(player)) {
                buildMode.remove(player);
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText("§8» §cBaumodus deaktiviert §8«")
                );
                player.setGameMode(GameMode.ADVENTURE);
                JoinListener.giveSpawnItems(player);
                return true;
            }
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§8» §aBaumodus aktiviert §8«")
            );
            buildMode.put(player, true);
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            return true;
        }
        return false;
    }
}
