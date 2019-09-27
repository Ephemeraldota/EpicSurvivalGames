package com.isnakebuzz.survivalgames.Player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerUtils {

    public static void clean(Player p, GameMode gameMode, boolean allowFlight, boolean resetHealth, boolean flying, boolean vanished) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setGameMode(gameMode);
        p.setFoodLevel(20);
        p.setExhaustion(20);
        p.setHealth(20);
        p.setAllowFlight(allowFlight);
        p.setFlying(flying);
        p.setLevel(0);
        p.setExp(0);
        //p.setVelocity(p.getVelocity().normalize());
        p.getInventory().setHeldItemSlot(4);
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));

        if (resetHealth)
            p.setHealthScale(20);

        if (vanished)
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
    }

}
