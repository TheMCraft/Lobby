package com.themcraft.lobby.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserData {
    private String username;
    private UUID uuid;
    private String abilityEquipped;
    private List<String> abilitiesUnlocked = new ArrayList<>();
    private int coins = 0;

    public UserData(String username, UUID uuid, String abilityEquipped, List<String> abilitiesUnlocked, int coins) {
        this.username = username;
        this.uuid = uuid;
        this.abilityEquipped = abilityEquipped;
        if (abilitiesUnlocked != null) this.abilitiesUnlocked = abilitiesUnlocked;
        this.coins = coins;
    }

    public String getUsername() { return username; }
    public UUID getUuid() { return uuid; }
    public String getAbilityEquipped() { return abilityEquipped; }
    public List<String> getAbilitiesUnlocked() { return abilitiesUnlocked; }
    public int getCoins() { return coins; }

    public void setUsername(String username) { this.username = username; }
    public void setAbilityEquipped(String abilityEquipped) { this.abilityEquipped = abilityEquipped; }
    public void setAbilitiesUnlocked(List<String> abilitiesUnlocked) { this.abilitiesUnlocked = abilitiesUnlocked; }
    public void setCoins(int coins) { this.coins = coins; }
}

