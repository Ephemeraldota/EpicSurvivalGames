package com.isnakebuzz.survivalgames.Listeners.Events.DeathMsg;

import com.isnakebuzz.survivalgames.ArenaUtils.Arena;
import com.isnakebuzz.survivalgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EventDie implements Listener {

    private Main plugin;

    public EventDie(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDie(EntityDeathEvent e) {
        if (e.getEntity() != null) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (plugin.getArenaManager().getArenaPlayer().containsKey(p.getUniqueId())) {
                    Arena arena = plugin.getArenaManager().getArenaPlayer().get(p.getUniqueId());
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> createMessage(arena, e.getEntity(), e.getEntity().getLastDamageCause().getCause()));
                    EventTag.lastDamager.remove(e.getEntity());
                    EventTag.lastDmgTime.remove(e.getEntity());
                }
            }
        }
    }

    private void print(Arena arena, String message) {
        //System.out.println(message);
        arena.broadcast(message);
    }

    private void createMessage(Arena arena, LivingEntity dead, EntityDamageEvent.DamageCause cause) {
        if (!(dead instanceof Player)) return;

        if (EventTag.getInflicted(dead)) {
            String message;
            if (EventTag.getWho(dead) instanceof Player) {
                message = messageReplaceAndColor(dead, true, EventTag.getWho(dead), cause, true);
            } else {
                message = messageReplaceAndColor(dead, true, EventTag.getWho(dead), cause, false);
            }
            print(arena, message);
        } else {
            String message = messageReplaceAndColor(dead, false, null, cause, false);
            print(arena, message);
        }
    }

    private String messageReplaceAndColor(LivingEntity dead, Boolean inflicted, LivingEntity attacker, EntityDamageEvent.DamageCause cause, Boolean playerAttack) {
        String raw2 = "";
        String returnRaw = "";
        if (dead instanceof Player) {
            String raw = getMessageFromConfig(cause, inflicted, playerAttack);
            raw2 = raw.replaceAll("%dead%", dead.getName());
        }
        if (attacker != null) {
            if (attacker instanceof Player) {
                returnRaw = raw2.replaceAll("%killer%", attacker.getName());
            }
        } else {
            returnRaw = raw2;
        }
        return c(returnRaw);
    }

    private String getMessageFromConfig(EntityDamageEvent.DamageCause cause, Boolean inflicted, Boolean playerAttack) {
        String path = "other";
        String messageType = null;
        if (inflicted) {
            messageType = "inflicted";
        }
        if (!inflicted) {
            messageType = "other";
        }
        /*String ver = Bukkit.getBukkitVersion();
        ver.substring(0, ver.indexOf("-"));*/
        if (cause == EntityDamageEvent.DamageCause.FALL) {
            path = "fall";
        }
        if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            path = "explosion";
        }
        if (cause == EntityDamageEvent.DamageCause.CONTACT) {
            path = "cactus";
        }
        if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            path = "drown";
        }
        if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
            path = "fire";
        }
        if (cause == EntityDamageEvent.DamageCause.LAVA) {
            path = "lava";
        }
        if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            path = "lightning";
        }
        if (cause == EntityDamageEvent.DamageCause.MELTING) {
            path = "melting";
        }
        if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
            path = "projectile";
        }
        if (cause == EntityDamageEvent.DamageCause.STARVATION) {
            path = "starvation";
        }
        if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            path = "suffocation";
        }
        if (cause == EntityDamageEvent.DamageCause.SUICIDE) {
            path = "suicide";
        }
        if (cause == EntityDamageEvent.DamageCause.THORNS) {
            path = "thorns";
        }
        if (cause == EntityDamageEvent.DamageCause.VOID) {
            path = "void";
        }
        if (cause == EntityDamageEvent.DamageCause.WITHER) {
            path = "wither";
        }
        if (cause == EntityDamageEvent.DamageCause.MAGIC) {
            path = "potion";
        }
        if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (playerAttack) {
                path = "player-attack";
            } else {
                path = "entity-attack";
            }
        }
        return plugin.getConfigUtils().getConfig(plugin, "Lang").getString("DeathMessages." + path + "." + messageType);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
