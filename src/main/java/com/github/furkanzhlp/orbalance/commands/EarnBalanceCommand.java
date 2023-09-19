package com.github.furkanzhlp.orbalance.commands;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;
import com.github.furkanzhlp.orbalance.player.PlayerData;
import com.github.furkanzhlp.orbalance.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EarnBalanceCommand implements CommandExecutor {
    private final OrbalancePlugin plugin;
    private final Map<UUID, Long> cooldown;
    private final Random random;
    public EarnBalanceCommand(OrbalancePlugin plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
        this.random = new Random();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Utils.getMessage("player-use-only"));
            return true;
        }
        Player player = (Player) sender;
        if (System.currentTimeMillis() >= cooldown.getOrDefault(player.getUniqueId(), Long.MIN_VALUE)) {
            int amount = random.nextInt(5);
            PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            if(playerData == null){
                //Load player's data if is not loaded.
                plugin.getPlayerDataManager().loadPlayerData(player.getUniqueId());
                sender.sendMessage(Utils.getMessage("error"));
                return true;
            }
            playerData.addBalance((double) amount);
            cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
            player.sendMessage(Utils.getMessage("earn-command")
                    .replace("%value%", String.valueOf(amount)));
        }
        long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(cooldown.get(player.getUniqueId()) - System.currentTimeMillis());
        player.sendMessage(Utils.getMessage("earn-command-cooldown").replace("%seconds%", String.valueOf(remainingSeconds)));
        return true;
    }
}
