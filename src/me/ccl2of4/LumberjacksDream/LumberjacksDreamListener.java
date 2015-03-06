package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor on 3/6/15.
 */

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LumberjacksDreamListener implements Listener {
    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();
        logger.info("Block broken!");
    }
}
