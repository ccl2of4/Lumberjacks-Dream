package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor Lirot on 3/5/2015.
 */
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class LumberjacksDreamPlugin extends JavaPlugin
{
    // Called when the server is being disabled.stop
    public void onDisable ()
    {
        System.out.println(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " disabled!");
    }

    // Called when the server is being enabled.
    public void onEnable ()
    {
         // Set up the logger
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();
        logger.setPlugin (this);
        logger.info (this.getDescription().getName() + " version " + this.getDescription().getVersion() + " enabled!");

        //check configuration
        checkConfig ();

        // set up the listener
        LumberjacksDreamListener listener = new LumberjacksDreamListener();
        listener.configure (loadConfig());
        getServer().getPluginManager().registerEvents(listener, this);

    }

    private HashMap<String,?> loadConfig () {
        HashMap<String, Object> result = new HashMap<String, Object> ();
        FileConfiguration config = getConfig ();

        for (String key : LumberjacksDreamListener.getConfigKeys ()) {
            result.put (key, config.get (key));
        }
        return result;
    }

    /**
     * makes sure that all required keys are in config.yml so the rest of the plugin will function without error
     * if config.yml is missing keys, then we load the default config.yml
     */
    private void checkConfig () {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        FileConfiguration config = getConfig ();

        for (String key : LumberjacksDreamListener.getConfigKeys ()) {
            if (config.get (key) == null) {
                logger.warning ("config.yml is invalid; using default config.yml.");
                saveDefaultConfig ();
            }
        }
    }

    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase ("author"))
        {
            sender.sendMessage ("Made by ccl2of4 - https://github.com/ccl2of4/LumberjacksDream");
            return true;
        }
        return false;
    }

    /*
        TODO: Make configuration files.
     */
}