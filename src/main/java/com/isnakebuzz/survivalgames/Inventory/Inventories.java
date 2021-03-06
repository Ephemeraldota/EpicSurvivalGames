package com.isnakebuzz.survivalgames.Inventory;

import com.isnakebuzz.survivalgames.Inventory.Utils.ItemBuilder;
import com.isnakebuzz.survivalgames.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class Inventories {

    private Main plugin;

    public Inventories(Main plugin) {
        this.plugin = plugin;
    }

    public void setLobbyInventory(Player p) {
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(40);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Utils/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Lobby").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;

        for (String item : key) {
            String path = "Lobby." + item + ".";
            String _item = config.getString(path + "item");
            int _slot = config.getInt(path + "slot");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            p.getInventory().setItem(_slot, ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore));
        }
        p.updateInventory();
    }

    public void setSpectInventory(Player p) {
        Configuration config = plugin.getConfigUtils().getConfig(plugin, "Utils/Inventory");
        Set<String> key;
        try {
            key = config.getConfigurationSection("Spectator").getKeys(false);
        } catch (Exception ex) {
            key = null;
        }
        if (key == null | key.size() < 1) return;

        for (String item : key) {
            String path = "Spectator." + item + ".";
            String _item = config.getString(path + "item");
            int _slot = config.getInt(path + "slot");
            String _name = config.getString(path + "name");
            List<String> _lore = config.getStringList(path + "lore");
            String _action = config.getString(path + "action");
            p.getInventory().setItem(_slot, ItemBuilder.crearItem1(Integer.valueOf(_item.split(":")[0]), 1, Integer.valueOf(_item.split(":")[1]), _name, _lore));
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}