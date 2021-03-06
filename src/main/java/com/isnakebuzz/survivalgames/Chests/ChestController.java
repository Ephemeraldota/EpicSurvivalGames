package com.isnakebuzz.survivalgames.Chests;

import com.google.common.collect.Lists;
import com.isnakebuzz.survivalgames.ArenaUtils.Arena;
import com.isnakebuzz.survivalgames.Main;
import com.isnakebuzz.survivalgames.Utils.Enums;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class ChestController {

    private static SettingsManager basicChest, normalChest, opChest;
    private List<ChestItem> basicItems = Lists.newArrayList();
    private List<ChestItem> normalItems = Lists.newArrayList();
    private List<ChestItem> opItems = Lists.newArrayList();
    private Random random = new Random();
    private Main plugin;
    private List<Integer> randomLoc = new ArrayList<Integer>();
    private List<Integer> randomDLoc = new ArrayList<Integer>();

    public ChestController(Main plugin) {
        this.plugin = plugin;
        basicChest = new SettingsManager(plugin, "Chests/Basic");
        normalChest = new SettingsManager(plugin, "Chests/Normal");
        opChest = new SettingsManager(plugin, "Chests/Overpowered");
        load();
        for (int i = 0; i < 27; i++) {
            randomLoc.add(i);
        }
        for (int i = 0; i < 54; i++) {
            randomDLoc.add(i);
        }
    }

    public static ItemStack parseItem(List<String> item) {
        if (item.size() < 2) {
            return null;
        }

        ItemStack itemStack = null;

        try {
            if (item.get(0).contains(":")) {
                Material material = Material.getMaterial(item.get(0).split(":")[0].toUpperCase());
                int amount = Integer.parseInt(item.get(1));
                if (amount < 1) {
                    return null;
                }
                short data = (short) Integer.parseInt(item.get(0).split(":")[1].toUpperCase());
                itemStack = new ItemStack(material, amount, data);
            } else {
                itemStack = new ItemStack(Material.getMaterial(item.get(0).toUpperCase()),
                        Integer.parseInt(item.get(1)));
            }

            if (item.size() > 2) {
                for (int x = 2; x < item.size(); x++) {
                    if (item.get(x).split(":")[0].equalsIgnoreCase("name")) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(item.get(x).split(":")[1]);
                        itemStack.setItemMeta(itemMeta);
                    } else {
                        itemStack.addUnsafeEnchantment(getEnchant(item.get(x).split(":")[0]),
                                Integer.parseInt(item.get(x).split(":")[1]));
                    }
                }

            }

        } catch (Exception ignored) {

        }
        return itemStack;
    }

    private static Enchantment getEnchant(String enchant) {
        switch (enchant.toLowerCase()) {
            case "protection":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "projectileprotection":
                return Enchantment.PROTECTION_PROJECTILE;
            case "fireprotection":
                return Enchantment.PROTECTION_FIRE;
            case "featherfall":
                return Enchantment.PROTECTION_FALL;
            case "blastprotection":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "respiration":
                return Enchantment.OXYGEN;
            case "aquaaffinity":
                return Enchantment.WATER_WORKER;
            case "sharpness":
                return Enchantment.DAMAGE_ALL;
            case "smite":
                return Enchantment.DAMAGE_UNDEAD;
            case "baneofarthropods":
                return Enchantment.DAMAGE_ARTHROPODS;
            case "knockback":
                return Enchantment.KNOCKBACK;
            case "fireaspect":
                return Enchantment.FIRE_ASPECT;
            case "depthstrider":
                return Enchantment.DEPTH_STRIDER;
            case "looting":
                return Enchantment.LOOT_BONUS_MOBS;
            case "power":
                return Enchantment.ARROW_DAMAGE;
            case "punch":
                return Enchantment.ARROW_KNOCKBACK;
            case "flame":
                return Enchantment.ARROW_FIRE;
            case "infinity":
                return Enchantment.ARROW_INFINITE;
            case "efficiency":
                return Enchantment.DIG_SPEED;
            case "silktouch":
                return Enchantment.SILK_TOUCH;
            case "unbreaking":
                return Enchantment.DURABILITY;
            case "fortune":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "luckofthesea":
                return Enchantment.LUCK;
            case "luck":
                return Enchantment.LUCK;
            case "lure":
                return Enchantment.LURE;
            case "thorns":
                return Enchantment.THORNS;
            default:
                return null;
        }
    }

    public void load() {
        basicItems.clear();
        if (basicChest.getConfig().contains("Loot")) {
            for (String item : basicChest.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<String>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (itemStack != null) {
                    basicItems.add(new ChestItem(itemStack, chance));
                }
            }
        }

        normalItems.clear();
        if (normalChest.getConfig().contains("Loot")) {
            for (String item : normalChest.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<String>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (itemStack != null) {
                    normalItems.add(new ChestItem(itemStack, chance));
                }
            }
        }

        opItems.clear();
        if (opChest.getConfig().contains("Loot")) {
            for (String item : opChest.getConfig().getStringList("Loot")) {
                List<String> itemData = new LinkedList<String>(Arrays.asList(item.split(" ")));

                int chance = Integer.parseInt(itemData.get(0));
                itemData.remove(itemData.get(0));

                ItemStack itemStack = parseItem(itemData);

                if (itemStack != null) {
                    opItems.add(new ChestItem(itemStack, chance));
                }
            }
        }

    }

    public void setChest(Arena arena, final Chest chest) {
        final Inventory inventory = chest.getBlockInventory();
        inventory.clear();
        int added = 0;
        Collections.shuffle(this.randomLoc);
        int max = 6;
        int min = 1;

        if (arena.getChestType().equals(Enums.ChestType.BASIC)) {
            for (final ChestItem chestItem : this.basicItems) {
                if (this.random.nextInt(99) + 1 <= chestItem.getChance()) {
                    inventory.setItem(this.randomLoc.get(added), chestItem.getItem());
                    // Random int entre 21 22 24 18.s
                    if (added++ >= inventory.getSize() - (new Random().nextInt((max - min) + 1) + min)) {
                        break;
                    }
                }
            }
        } else if (arena.getChestType().equals(Enums.ChestType.NORMAL)) {
            for (final ChestItem chestItem : this.normalItems) {
                if (this.random.nextInt(99) + 1 <= chestItem.getChance()) {
                    inventory.setItem(this.randomLoc.get(added), chestItem.getItem());
                    // Random int entre 21 22 24 18.s
                    if (added++ >= inventory.getSize() - (new Random().nextInt((max - min) + 1) + min)) {
                        break;
                    }
                }
            }
        } else if (arena.getChestType().equals(Enums.ChestType.OVERPOWERED)) {
            for (final ChestItem chestItem : this.opItems) {
                if (this.random.nextInt(99) + 1 <= chestItem.getChance()) {
                    inventory.setItem(this.randomLoc.get(added), chestItem.getItem());
                    // Random int entre 21 22 24 18.s
                    if (added++ >= inventory.getSize() - (new Random().nextInt((max - min) + 1) + min)) {
                        break;
                    }
                }
            }
        } else if (arena.getChestType().equals(Enums.ChestType.NOBODY)) {
            for (final ChestItem chestItem : this.normalItems) {
                if (this.random.nextInt(99) + 1 <= chestItem.getChance()) {
                    inventory.setItem(this.randomLoc.get(added), chestItem.getItem());
                    // Random int entre 21 22 24 18.s
                    if (added++ >= inventory.getSize() - (new Random().nextInt((max - min) + 1) + min)) {
                        break;
                    }
                }
            }
        }
    }

    public class ChestItem {

        private final ItemStack item;
        private final int chance;

        public ChestItem(ItemStack item, int chance) {
            this.item = item;
            this.chance = chance;
        }

        public ItemStack getItem() {
            return item;
        }

        public int getChance() {
            return chance;
        }
    }
}
