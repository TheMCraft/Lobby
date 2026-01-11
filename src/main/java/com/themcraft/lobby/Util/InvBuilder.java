package com.themcraft.lobby.Util;

import org.bukkit.inventory.Inventory;

public class InvBuilder {
    public static void formatInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                if (i < 9 || i >= inventory.getSize() - 9 || i % 9 == 0 || i % 9 == 8) {
                    inventory.setItem(i, new ItemBuilder(org.bukkit.Material.BLACK_STAINED_GLASS_PANE)
                            .setName("§7-/-")
                            .build());
                } else {
                    inventory.setItem(i, new ItemBuilder(org.bukkit.Material.GRAY_STAINED_GLASS_PANE)
                            .setName("§7-/-")
                            .build());
                }
            }
        }
        if (inventory.getSize() >= 27) {
            inventory.setItem(inventory.getSize() - 5, new ItemBuilder(org.bukkit.Material.BARRIER)
                    .setName("§8» §cSchließen §8«")
                    .build());
        }
    }
}
