package orbalance.utils;

import orbalance.OrbalancePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    private static final NumberFormat nf = NumberFormat.getInstance();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static NumberFormat getNf() {
        return nf;
    }
    public static String formatNumber(Integer number){
        return nf.format(number);
    }
    public static String formatNumber(long number){
        return nf.format(number);
    }
    public static String formatNumber(Double number){
        nf.setMaximumFractionDigits(2);
        return nf.format(number);
    }

    public static void log(String log){
        Bukkit.getLogger().info("[Orbalance] "+log);
    }
    public static void warning(String log){
        Bukkit.getLogger().warning("[Orbalance] "+log);
    }

    public static String getMessage(String path){
        return ChatColor.translateAlternateColorCodes('&',OrbalancePlugin.getInstance().getConfig().getString("messages."+path));
    }


}
