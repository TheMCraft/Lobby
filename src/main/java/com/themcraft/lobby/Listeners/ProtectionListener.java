package com.themcraft.lobby.Listeners;

import com.themcraft.lobby.Commands.BuildCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.inventory.ItemStack;

public class ProtectionListener implements Listener {

    private boolean isInBuildMode(Player player) {
        return BuildCommand.buildMode.containsKey(player)
                && BuildCommand.buildMode.get(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isInBuildMode(event.getPlayer())) return;
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.BOW) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysicsChange(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onXPChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && isInBuildMode(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isInBuildMode(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isInBuildMode(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawnGeneric(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawnReason(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn2(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn3(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn4(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn5(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn6(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn7(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn8(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn9(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn10(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn11(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn12(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn13(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn14(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn15(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn16(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn17(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn18(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn19(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn20(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn21(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn22(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn23(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn24(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn25(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn26(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn27(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn28(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn29(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn30(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        event.setCancelled(true);
    }

}
