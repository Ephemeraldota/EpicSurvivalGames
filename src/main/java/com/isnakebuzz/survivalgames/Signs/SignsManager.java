package com.isnakebuzz.survivalgames.Signs;

import com.isnakebuzz.survivalgames.ArenaUtils.Arena;
import com.isnakebuzz.survivalgames.Main;
import com.isnakebuzz.survivalgames.Signs.utils.LocationUtil;
import com.isnakebuzz.survivalgames.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.*;

public class SignsManager {

    private Main plugin;
    private Map<String, SignBlock> stringSignMap;

    public SignsManager(Main plugin) {
        this.plugin = plugin;
        this.stringSignMap = new HashMap<>();
    }

    public Map<String, SignBlock> getSigns() {
        return this.stringSignMap;
    }

    public void initSigns() {
        FileConfiguration config = plugin.getConfigUtils().getConfig(plugin, "Signs");
        this.stringSignMap.clear();
        if (config.getStringList("Signs") == null) {
            return;
        }
        for (String key : config.getStringList("Signs")) {
            if (LocationUtil.getLocation(key).getBlock() != null) {
                SignBlock signBlock = new SignBlock(key);
                addSign(key, signBlock);
                this.searchingGames(signBlock);
            } else {
                List<String> list = config.getStringList("Signs");
                list.remove(key);
                config.set("Signs", list);

                plugin.log("Info", "Removing &b" + key + "&e from the Signs List due to NullPointerException");

                try {
                    config.save(plugin.getConfigUtils().getFile(plugin, "Signs"));
                } catch (IOException e) {
                    plugin.log("Error", e.getMessage());
                    plugin.log("Error", "Please report this :)");
                }
            }
        }
    }

    public boolean addSign(String loc, SignBlock sign) {
        if (!this.stringSignMap.containsKey(loc)) {
            this.stringSignMap.put(loc, sign);
        }
        return false;
    }

    public void removeSign(Arena game) {
        for (final SignBlock signBlock : stringSignMap.values()) {
            if (signBlock.getGame() == game) {
                game.setUsed(false);
                signBlock.setGame(null);
                this.searchingGames(signBlock);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, this::updateAllSigns);
                break;
            }
        }
    }

    public void updateAllSigns() {
        for (Arena game : getGames()) {
            if (!this.getSearchingSigns().isEmpty()) {
                final SignBlock randomSign = this.getSearchingSigns().get(new Random().nextInt(getSearchingSigns().size()));
                if (!game.isUsed() && randomSign.getGame() == null) {
                    randomSign.setGame(game);
                    signGame(randomSign);
                    game.setUsed(true);
                }
            }
        }

        for (final SignBlock signBlock : this.getUsedSigns()) {
            if (signBlock.getGame() != null) {
                signGame(signBlock);
            }
        }

    }

    public List<Arena> getGames() {
        List<Arena> newGamesList = new ArrayList<>();
        for (Arena game : plugin.getArenaManager().getArenas().values()) {
            if (game.getGameStatus().equals(Enums.GameStatus.WAITING) || game.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                newGamesList.add(game);
            }
        }
        return newGamesList;
    }

    public List<SignBlock> getSearchingSigns() {
        List<SignBlock> signBlocks = new ArrayList<>();
        for (SignBlock signBlock : this.stringSignMap.values()) {
            if (signBlock.getGame() == null) {
                signBlocks.add(signBlock);
            }
        }
        return signBlocks;
    }

    public List<SignBlock> getUsedSigns() {
        List<SignBlock> signBlocks = new ArrayList<>();
        for (SignBlock signBlock : this.stringSignMap.values()) {
            if (signBlock.getGame() != null) {
                signBlocks.add(signBlock);
            }
        }
        return signBlocks;
    }

    public void searchingGames(SignBlock signBlock) {
        Sign sign = signBlock.getSign();
        if (signBlock.getGame() == null) {
            Configuration config = plugin.getConfigUtils().getConfig(plugin, "Signs");
            List<String> list = chars(config.getStringList("Searching"), signBlock);
            for (int i = 0; i < list.size(); i++) {
                signBlock.setSignText(sign, i, list.get(i));
            }
        }
        signBlock.updateBlock();
    }

    public void signGame(SignBlock signBlock) {
        Sign sign = signBlock.getSign();
        if (signBlock.getGame() != null) {
            Configuration config = plugin.getConfigUtils().getConfig(plugin, "Signs");
            List<String> list = chars(config.getStringList("Format"), signBlock);
            for (int i = 0; i < list.size(); i++) {
                signBlock.setSignText(sign, i, list.get(i));
            }
            signBlock.updateBlock();
        }
    }

    private List<String> chars(List<String> lines, SignBlock signBlock) {
        List<String> newLines = new ArrayList<>();
        for (String s : lines) {
            if (signBlock.getGame() == null) {
                newLines.add(c(s));
            } else {
                newLines.add(c(s)
                        .replaceAll("%status%", signBlock.getGame().getStatusMsg())
                        .replaceAll("%map%", signBlock.getGame().getSignName())
                        .replaceAll("%online%", String.valueOf(signBlock.getGame().getGamePlayers().size()))
                        .replaceAll("%max%", String.valueOf(signBlock.getGame().getMaxPlayers()))
                );
            }
        }
        return newLines;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
