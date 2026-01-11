package com.themcraft.lobby.Listeners;

import com.themcraft.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AbilityListener implements Listener {

    private final Main plugin;

    public AbilityListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (event.getItem() == null) return;

        Material type = event.getItem().getType();

        if (type == Material.FEATHER) {
            if (player.hasCooldown(Material.FEATHER)) return;
            player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
            player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0f, 1.0f);
            player.setCooldown(Material.FEATHER, 60);
            return;
        }

        if (type == Material.SUGAR) {
            if (player.hasCooldown(Material.SUGAR)) return;
            Vector forward = player.getLocation().getDirection().normalize().multiply(3.0).setY(0.2);
            player.setVelocity(forward);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.setCooldown(Material.SUGAR, 120);
            return;
        }

        if (type == Material.FIREWORK_ROCKET) {
            if (player.hasCooldown(Material.FIREWORK_ROCKET)) return;
            Vector vel = player.getLocation().getDirection().multiply(0.5).setY(1.5);
            player.setVelocity(vel);
            player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 0.8f);
            player.setCooldown(Material.FIREWORK_ROCKET, 100);
            return;
        }

        if (type == Material.GUNPOWDER) {
            if (player.hasCooldown(Material.GUNPOWDER)) return;
            player.getWorld().spawnParticle(Particle.LARGE_SMOKE, player.getLocation().add(0,1,0), 50, 0.5, 0.5, 0.5, 0.02);
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
            player.setCooldown(Material.GUNPOWDER, 60);
            return;
        }

        if (type == Material.BLAZE_POWDER) {
            if (player.hasCooldown(Material.BLAZE_POWDER)) return;
            player.getWorld().strikeLightningEffect(player.getLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 0.8f);
            player.setCooldown(Material.BLAZE_POWDER, 100);
            return;
        }

        if (type == Material.SLIME_BALL) {
            if (player.hasCooldown(Material.SLIME_BALL)) return;
            player.setVelocity(player.getLocation().getDirection().multiply(0.8).setY(1.2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 60, 1)); // kurz extra Sprung
            player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.0f, 1.0f);
            player.setCooldown(Material.SLIME_BALL, 80);
            return;
        }

        if (type == Material.EGG) {
            event.setCancelled(true);
            if (player.hasCooldown(Material.EGG)) return;
            player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation().add(0,1,0), 100, 0.7, 0.7, 0.7, 0.05);
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
            player.setCooldown(Material.EGG, 40);
            return;
        }

        if (type == Material.BONE) {
            if (player.hasCooldown(Material.BONE)) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 1));
            new BukkitRunnable() {
                int ticks = 0;
                @Override
                public void run() {
                    if (!player.isOnline() || ticks > 20) {
                        cancel();
                        return;
                    }
                    for (int i = 0; i < 20; i++) {
                        double angle = 2 * Math.PI * i / 20.0;
                        double x = Math.cos(angle) * 1.2;
                        double z = Math.sin(angle) * 1.2;
                        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(x, 1.0, z), 1, 0, 0, 0, 0);
                    }
                    player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
                    ticks += 5;
                }
            }.runTaskTimer(plugin, 0L, 5L);

            player.setCooldown(Material.BONE, 140);
            return;
        }
    }
}
