package com.themcraft.lobby.Listeners;

import com.themcraft.lobby.Commands.BuildCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        BuildCommand.buildMode.remove(event.getPlayer());
    }
}
