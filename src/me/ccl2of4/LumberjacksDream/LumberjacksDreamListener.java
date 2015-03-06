package me.ccl2of4.LumberjacksDream;

/**
 * Created by Connor on 3/6/15.
 */

import java.util.*;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class LumberjacksDreamListener implements Listener {

    static final int leafDistance = 4;

    @EventHandler
    public final void onBlockBreak (BlockBreakEvent event) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        Block block = event.getBlock ();
        Material blockMaterial = block.getType ();

        if (blockMaterial == Material.LOG) {
            logger.info("Broke log!");

            Player player = event.getPlayer ();
            ItemStack tool = player.getItemInHand();
            Material toolMaterial = tool.getType ();

            if (toolMaterial == Material.DIAMOND_AXE && checkIfTreeTrunk (block)) {
                logger.info ("Applying effect!");
                applyEffect(block, tool);
            }

        }
    }

    /**
     *
     * @param block the block in question
     * @return true if the block is considered to be the trunk of a tree, false otherwise
     */
    private static final boolean checkIfTreeTrunk (Block block) {

        final Material[] materialsArray = {
                Material.DIRT,
                Material.GRASS
        };
        final Set<Material> materials = new HashSet<Material>(Arrays.asList(materialsArray));

        Material material = block.getType ();

        while (material == Material.LOG) {
            block = block.getRelative(BlockFace.DOWN);
            material = block.getType ();

            if (materials.contains (material)) {
                return true;
        }

        return false;
    }

    private final void applyEffect (Block block, ItemStack tool) {

        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();
        Queue<Block> queue = new LinkedList<Block> ();
        Set<Block> exploredBlocks = new HashSet<Block> ();

        do {

            Material material = block.getType ();

            if (material != Material.LOG && material != Material.LEAVES) {
                // algorithm stops at non-log, non-leaves blocks
            }

            else {

                if (material == Material.LOG) {
                    block.breakNaturally(tool);
                } else if (material == Material.LEAVES) {
                    //do not break leaves
                }

                Block[] adjacentBlocks = {
                        block.getRelative(BlockFace.UP),
                        block.getRelative(BlockFace.NORTH),
                        block.getRelative(BlockFace.EAST),
                        block.getRelative(BlockFace.SOUTH),
                        block.getRelative(BlockFace.WEST)
                };

                for (Block adjacentBlock : adjacentBlocks) {
                    if (!exploredBlocks.contains (adjacentBlock)) {
                        exploredBlocks.add (adjacentBlock);
                        queue.add (adjacentBlock);
                    } else {
                        logger.info ("Already found that block");
                    }
                }

            }

            block = queue.poll ();

        } while (block != null);

    }
}
