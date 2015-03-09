package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor on 3/6/15.
 */

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPluginLogger {
    public static final Logger logger = Logger.getLogger("Minecraft");
    private JavaPlugin plugin;

    public JavaPlugin getPlugin () {
        return plugin;
    }

    public void setPlugin (JavaPlugin plugin) { this.plugin = plugin; }

    public void info (String s)
    {
        logger.log( Level.INFO, "[" + plugin.getDescription().getName() + "] " + s);
    }

    public void severe (String s)
    {
        logger.log( Level.SEVERE, "[" + plugin.getDescription().getName() + "] " + s);
    }

    public void warning (String s)
    {
        logger.log( Level.WARNING, "[" + plugin.getDescription().getName() + "] " + s);
    }
}
