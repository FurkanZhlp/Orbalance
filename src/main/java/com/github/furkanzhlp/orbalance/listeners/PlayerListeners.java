package com.github.furkanzhlp.orbalance.listeners;

import com.github.furkanzhlp.orbalance.OrbalancePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final OrbalancePlugin plugin;

    public PlayerListeners(OrbalancePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        plugin.getPlayerDataManager().loadPlayerData(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        plugin.getPlayerDataManager().unloadPlayerData(e.getPlayer().getUniqueId());
    }




}
