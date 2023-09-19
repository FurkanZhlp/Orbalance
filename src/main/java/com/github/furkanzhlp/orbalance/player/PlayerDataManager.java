package com.github.furkanzhlp.orbalance.player;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;
import com.github.furkanzhlp.orbalance.database.DatabaseHandler;
import com.github.furkanzhlp.orbalance.utils.Utils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private final OrbalancePlugin plugin;
    private HashMap<UUID,PlayerData> playerList = new HashMap<>();

    public PlayerDataManager(OrbalancePlugin plugin){
        Utils.log("PlayerDataManager loading...");
        this.plugin = plugin;
    }
    public void loadPlayerData(UUID uuid){
        if(playerList.containsKey(uuid)) return;
        PlayerData playerData = new PlayerData(uuid,  DatabaseHandler.getBalance(uuid));
        playerList.put(uuid,playerData);
    }
    public void unloadPlayerData(UUID uuid){
        if(!playerList.containsKey(uuid)) return;
        savePlayerData(uuid);
        playerList.remove(uuid);
    }
    public void savePlayerData(UUID uuid){
        if(!playerList.containsKey(uuid)) return;
        PlayerData playerData = playerList.get(uuid);
        DatabaseHandler.saveBalance(playerData.getPlayerUUID(),playerData.getBalance());
    }

    public PlayerData getPlayerData(UUID uuid){
        if(!playerList.containsKey(uuid)) return null;
        return playerList.get(uuid);
    }


    public void onEnable(){
        Bukkit.getOnlinePlayers().forEach(player -> loadPlayerData(player.getUniqueId()));
    }
    public void onDisable(){
        playerList.keySet().forEach(this::savePlayerData);
    }


}
