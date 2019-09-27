package com.isnakebuzz.survivalgames.Listeners.Events.ArenaEvents;

import com.isnakebuzz.survivalgames.ArenaUtils.Arena;
import com.isnakebuzz.survivalgames.Main;
import com.isnakebuzz.survivalgames.Utils.Enums;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.world.WorldInitEvent;

public class ExtraEvents implements Listener {

    private Main plugin;

    public ExtraEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void DropItems(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (plugin.getArenaManager().getArenaPlayer().containsKey(p.getUniqueId())) {
            Arena arena = plugin.getArenaManager().getArenaPlayer().get(p.getUniqueId());
            if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING) || arena.getGameStatus().equals(Enums.GameStatus.RESTARTING) || arena.isStarting()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void LossHungry(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (plugin.getArenaManager().getArenaPlayer().containsKey(p.getUniqueId())) {
            Arena arena = plugin.getArenaManager().getArenaPlayer().get(p.getUniqueId());
            if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING) || arena.getGameStatus().equals(Enums.GameStatus.RESTARTING) || arena.isGrace()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (plugin.getArenaManager().getArenaPlayer().containsKey(p.getUniqueId())) {
                Arena arena = plugin.getArenaManager().getArenaPlayer().get(p.getUniqueId());
                if (arena.getHealthType().equals(Enums.HealthType.HARD)) {
                    if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void worldInit(WorldInitEvent e) {
        World w = e.getWorld();
        w.setTime(6000);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setGameRuleValue("doMobSpawning", "false");
        w.setWeatherDuration(0);
        w.setAmbientSpawnLimit(0);
        w.setAutoSave(false);
        w.setDifficulty(Difficulty.NORMAL);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
