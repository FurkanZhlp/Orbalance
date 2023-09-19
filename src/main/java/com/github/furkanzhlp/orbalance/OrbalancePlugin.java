package orbalance;

import orbalance.commands.BalanceCommand;
import orbalance.listeners.PlayerListeners;
import orbalance.player.PlayerDataManager;
import orbalance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class OrbalancePlugin extends JavaPlugin {

    private static OrbalancePlugin instance;

    private PlayerDataManager playerDataManager;

    private Database database;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        Utils.log("Initializing plugin...");
        instance = this;
        try {
            database = new Database(getConfig().getConfigurationSection("database"));
        } catch (SQLException e) {
            Utils.warning("--------------------------");
            Utils.warning("");
            Utils.warning("Database connection failed");
            Utils.warning("");
            e.printStackTrace();
            Utils.warning("");
            Utils.warning("--------------------------");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        playerDataManager = new PlayerDataManager(this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(this),this);
        getCommand("balance").setExecutor(new BalanceCommand(this));
        Utils.log("Initializing finished.");
    }
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public Database getDatabase() {
        return database;
    }

    public static OrbalancePlugin getInstance() {
        return instance;
    }

}
