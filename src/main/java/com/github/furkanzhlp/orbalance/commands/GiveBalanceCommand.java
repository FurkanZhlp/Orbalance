package com.github.furkanzhlp.orbalance.commands;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;
import com.github.furkanzhlp.orbalance.player.PlayerData;
import com.github.furkanzhlp.orbalance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveBalanceCommand implements CommandExecutor {
    private final OrbalancePlugin plugin;
    public GiveBalanceCommand(OrbalancePlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Utils.getMessage("player-use-only:"));
            return true;
        }
        Player player = (Player) sender;
        if(args.length < 2){
            sender.sendMessage(Utils.getMessage("give-balance-usage"));
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
        if(player.getName().equalsIgnoreCase(targetPlayerName)){
            sender.sendMessage(Utils.getMessage("give-balance-own"));
            return true;
        }
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        PlayerData targetPlayerData = plugin.getPlayerDataManager().getPlayerData(targetPlayer.getUniqueId());
        if(playerData == null || targetPlayerData == null){
            //Load player's data if is not loaded.
            plugin.getPlayerDataManager().loadPlayerData(player.getUniqueId());
            plugin.getPlayerDataManager().loadPlayerData(targetPlayer.getUniqueId());
            sender.sendMessage(Utils.getMessage("error"));
            return true;
        }
        Double amount = Double.parseDouble(args[1]);

        if(playerData.getBalance() < amount){
            player.sendMessage(Utils.getMessage("give-balance-not-enough"));
            return true;
        }
        playerData.removeBalance(amount);
        targetPlayerData.addBalance(amount);
        sender.sendMessage(Utils.getMessage("give-balance")
                .replace("%target%",targetPlayer.getName())
                .replace("%value%",Utils.formatNumber(Double.parseDouble(args[1]))));
        targetPlayer.sendMessage(Utils.getMessage("give-balance-target").replace("%player%",sender.getName())
                .replace("%value%",Utils.formatNumber(Double.parseDouble(args[1]))));

        return true;
    }
}
