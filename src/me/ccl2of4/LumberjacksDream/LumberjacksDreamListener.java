package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor on 3/6/15.
 */

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;

public class LumberjacksDreamListener implements Listener {
    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        Block block = event.getBlock ();
        Material material = block.getType ();

        if (material == Material.LOG) {
            logger.info ("Broke wood!");
        }

        logger.info (material.toString ());
    }
}
