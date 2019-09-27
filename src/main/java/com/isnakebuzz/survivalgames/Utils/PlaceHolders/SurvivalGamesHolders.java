package com.isnakebuzz.survivalgames.Utils.PlaceHolders;

import com.isnakebuzz.survivalgames.Main;
import com.isnakebuzz.survivalgames.Player.GamePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class SurvivalGamesHolders extends PlaceholderExpansion {

    private Main plugin;

    public SurvivalGamesHolders(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player p, String holder) {

        if (plugin.getPlayerManager().containsPlayer(p)) {
            GamePlayer gamePlayer = plugin.getPlayerManager().getPlayer(p);

            if (holder.equalsIgnoreCase("wins")) {
                return String.valueOf(gamePlayer.getWins());
            } else if (holder.equalsIgnoreCase("kills")) {
                return String.valueOf(gamePlayer.getKills());
            } else if (holder.equalsIgnoreCase("deaths")) {
                return String.valueOf(gamePlayer.getDeaths());
            } else if (holder.equalsIgnoreCase("ashots")) {
                return String.valueOf(gamePlayer.getAShots());
            } else if (holder.equalsIgnoreCase("ahits")) {
                return String.valueOf(gamePlayer.getAHits());
            } else if (holder.equalsIgnoreCase("gplayeds")) {
                return String.valueOf(gamePlayer.getGPlayeds());
            } else {
                return "Error";
            }
        }

        return "Loading..";
    }

    @Override
    public String getIdentifier() {
        return "sg";
    }

    @Override
    public String getAuthor() {
        return "iSnakeBuzz_";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
