package orbalance.player;

import orbalance.OrbalancePlugin;
import orbalance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private final OrbalancePlugin plugin;
    private HashMap<UUID,PlayerData> playerList = new HashMap<>();

    public PlayerDataManager(OrbalancePlugin plugin){
        Utils.log("PlayerDataManager loading...");
        this.plugin = plugin;
        for(Player player : Bukkit.getOnlinePlayers()){
            loadPlayerData(player.getUniqueId());
        }
    }
    public void loadPlayerData(UUID uuid){
        if(playerList.containsKey(uuid)) return;
        PlayerData playerData = new PlayerData(uuid,plugin.getDatabase().getBalance(uuid));
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
        plugin.getDatabase().saveBalance(playerData.getPlayerUUID(),playerData.getBalance());
    }

    public PlayerData getPlayerData(UUID uuid){
        if(!playerList.containsKey(uuid)) return null;
        return playerList.get(uuid);
    }


}
