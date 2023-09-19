package com.github.furkanzhlp.orbalance.commands;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;
import com.github.furkanzhlp.orbalance.player.PlayerData;
import com.github.furkanzhlp.orbalance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private final OrbalancePlugin plugin;
    public BalanceCommand(OrbalancePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
                if(playerData != null){
                    player.sendMessage(Utils.getMessage("balance")
                            .replace("%balance%",Utils.formatNumber(playerData.getBalance())));
                }else{
                    //Load player's data if is not loaded.
                    plugin.getPlayerDataManager().loadPlayerData(player.getUniqueId());
                    player.sendMessage(Utils.getMessage("error"));
                }
            }else{
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
                if(targetPlayer != null){
                    PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(targetPlayer.getUniqueId());
                    if(playerData != null){
                        player.sendMessage(Utils.getMessage("target-balance")
                                .replace("%target%",targetPlayer.getName())
                                .replace("%balance%",Utils.formatNumber(playerData.getBalance())));
                    }else{
                        //Load player's data if is not loaded.
                        plugin.getPlayerDataManager().loadPlayerData(targetPlayer.getUniqueId());
                        player.sendMessage(Utils.getMessage("target-error"));
                    }
                }else{
                    player.sendMessage(Utils.getMessage("not-online").replace("%target%",targetPlayerName));
                }
            }
        }else{
            sender.sendMessage(Utils.getMessage("player-use-only"));
        }
        return true;
    }
}
