package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor on 3/6/15.
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LumberjacksDreamLogger {
    public static final Logger logger = Logger.getLogger("Minecraft");
    private static LumberjacksDreamLogger singleton;
    private LumberjacksDreamPlugin plugin;

    public static LumberjacksDreamLogger sharedLogger () {
        return singleton == null ? (singleton = new LumberjacksDreamLogger ()) : singleton;
    }

    public LumberjacksDreamPlugin getPlugin () {
        return plugin;
    }

    public void setPlugin (LumberjacksDreamPlugin plugin) {
        this.plugin = plugin;
    }

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

    private LumberjacksDreamLogger () {}
}
