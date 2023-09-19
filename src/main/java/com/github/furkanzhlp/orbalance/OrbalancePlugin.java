package com.github.furkanzhlp.orbalance;

import com.github.furkanzhlp.orbalance.commands.BalanceCommand;
import com.github.furkanzhlp.orbalance.commands.EarnBalanceCommand;
import com.github.furkanzhlp.orbalance.commands.GiveBalanceCommand;
import com.github.furkanzhlp.orbalance.commands.SetBalanceCommand;
import com.github.furkanzhlp.orbalance.database.Database;
import com.github.furkanzhlp.orbalance.database.DatabaseHandler;
import com.github.furkanzhlp.orbalance.utils.Utils;
import com.github.furkanzhlp.orbalance.listeners.PlayerListeners;
import com.github.furkanzhlp.orbalance.player.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class OrbalancePlugin extends JavaPlugin {

    private static OrbalancePlugin instance;

    private PlayerDataManager playerDataManager;

    public Database database;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        Utils.log("Initializing plugin...");
        instance = this;

        //Database Connection
        database = new Database(getConfig().getConfigurationSection("database"));
        try {
            Utils.log("Trying to connect database...");
            database.openConnection();
            startRefresh();
        }
        catch (Exception e) {
            Utils.log("Database connection failed. Disabling plugin...");
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        database.checkTables();

        //Managers
        playerDataManager = new PlayerDataManager(this);

        //Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(this),this);

        //Commands
        getCommand("bal").setExecutor(new BalanceCommand(this));
        getCommand("setbal").setExecutor(new SetBalanceCommand(this));
        getCommand("givebal").setExecutor(new GiveBalanceCommand(this));
        getCommand("earn").setExecutor(new EarnBalanceCommand(this));


        if(database.isConnected()){
            playerDataManager.onEnable();
        }

        Utils.log("Initializing finished.");
    }

    @Override
    public void onDisable() {
        playerDataManager.onDisable();
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    public void startRefresh() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    database.refreshConnect();
                }
                catch (Exception e) {
                    Bukkit.getLogger().warning("Failed to reload MySQL: " + e.toString());
                }
            }
        }, 200L, 36000L);
    }
    public static OrbalancePlugin getInstance() {
        return instance;
    }

}
