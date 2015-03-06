package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor Lirot on 3/5/2015.
 */
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class LumberjacksDreamPlugin extends JavaPlugin
{
    // Called when the server is being disabled.
    public void onDisable ()
    {
        System.out.println(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " disabled!");
    }

    // Called when the server is being enabled.
    public void onEnable ()
    {
        // Register the events.
        getServer().getPluginManager().registerEvents (new LumberjacksDreamListener (), this);

        // Set up the logger
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger();
        logger.setPlugin(this);
        logger.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " enabled!");
    }

    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("author"))
        {
            sender.sendMessage("Made by ccl2of4 - https://github.com/ccl2of4/LumberjacksDream");
            return true;
        }
        return false;
    }

    /*
        TODO: Make configuration files.
     */
}