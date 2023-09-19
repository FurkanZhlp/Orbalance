package com.github.furkanzhlp.orbalance.commands;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;
import com.github.furkanzhlp.orbalance.player.PlayerData;
import com.github.furkanzhlp.orbalance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBalanceCommand implements CommandExecutor {
    private final OrbalancePlugin plugin;
    public SetBalanceCommand(OrbalancePlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.isOp() && !sender.hasPermission("orbalance.setbalance"))
        {
            sender.sendMessage(Utils.getMessage("no-permission"));
            return true;
        }
        if(args.length < 2){
            sender.sendMessage(Utils.getMessage("set-balance-usage"));
            return true;
        }
        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if(targetPlayer == null){
            sender.sendMessage(Utils.getMessage("not-online").replace("%target%",targetPlayerName));
            return true;
        }
        if(!Utils.isDouble(args[1]) || Double.parseDouble(args[1]) < 0){
            sender.sendMessage(Utils.getMessage("invalid-number"));
            return true;
        }
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(targetPlayer.getUniqueId());
        if(playerData == null){
            //Load player's data if is not loaded.
            plugin.getPlayerDataManager().loadPlayerData(targetPlayer.getUniqueId());
            sender.sendMessage(Utils.getMessage("error"));
            return true;
        }
        playerData.setBalance(Double.parseDouble(args[1]));
        sender.sendMessage(Utils.getMessage("set-balance")
                .replace("%target%",targetPlayer.getName())
                .replace("%value%",Utils.formatNumber(Double.parseDouble(args[1]))));
        targetPlayer.sendMessage(Utils.getMessage("set-balance-target").replace("%player%",sender.getName())
                .replace("%value%",Utils.formatNumber(Double.parseDouble(args[1]))));

        return true;
    }
}
