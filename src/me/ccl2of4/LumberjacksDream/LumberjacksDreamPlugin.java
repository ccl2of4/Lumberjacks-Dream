package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor Lirot on 3/5/2015.
 */
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LumberjacksDreamPlugin extends JavaPlugin
{
    private JavaPluginLogger logger;

    // Called when the server is being disabled.stop
    @Override
    public void onDisable ()
    {
        System.out.println(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " disabled!");
    }

    // Called when the server is being enabled.
    @Override
    public void onEnable ()
    {
         // Set up the logger
        logger = new JavaPluginLogger ();
        logger.setPlugin (this);
        logger.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " enabled!");

        //check configuration
        if ( checkConfig ()) {

            // set up the listener
            LumberjacksDreamListener listener = new LumberjacksDreamListener ();
            listener.setLogger (logger);
            listener.configure(pullConfig ());
            getServer().getPluginManager().registerEvents (listener, this);
        }

    }

    // Command handler
    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase ("LumberjacksDream")) {
            sender.sendMessage ("Made by ccl2of4 - https://github.com/ccl2of4/LumberjacksDream");
            return true;
        }
        return false;
    }

    /**
     * Pull the configuration data out of config.yml and into a map
     * @return HashMap containing the key,value wanted by LumberjacksDreamListener
     */
    private Map<String,?> pullConfig () {
        HashMap<String, Object> result = new HashMap<String, Object> ();
        FileConfiguration config = getConfig ();

        for (String key : LumberjacksDreamListener.getConfigKeys ()) {
            result.put (key, config.get (key));
        }
        return result;
    }

    /**
     * Makes sure that all required keys are in config.yml so the rest of the plugin will function without error
     * Also saves the default config.yml to the data folder if none is present.
     * @return true of configuration is valid, false otherwise
     */
    private boolean checkConfig () {
        FileConfiguration config = getConfig ();

        // this will only be an issue if the default config.yml is broken
        for (String key : LumberjacksDreamListener.getConfigKeys ()) {
            if (config.get (key) == null) {
                logger.severe ("configuration key \"" + key + "\' found to be missing. This is a fatal error.");
                return false;
            }
        }

        // save a copy of the default config to the data folder, if necessary
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists ()) {
            logger.warning("config.yml not found in data folder. Creating default config.yml.");
            saveDefaultConfig();
        }

        return true;
    }
}