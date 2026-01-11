package com.themcraft.lobby.Util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemBuilder {
    ItemStack item;
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }
    public ItemBuilder setName(String name) {
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return this;
    }
    public ItemBuilder setLore(List<String> lore) {
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return this;
    }
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    public ItemStack build() {
        return item;
    }
}
