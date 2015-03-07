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

    public static final String TreeMaterialsKey = "tree_materials";
    public static final String PassthroughMaterialsKey = "passthrough_materials";
    public static final String TreeBaseMaterialsKey = "tree_base_materials";
    public static final String EligibleToolMaterialsKey = "eligible_tool_materials";

    private static Set<Material> treeMaterials = new HashSet<Material> ();
    private static Set<Material> passthroughMaterials = new HashSet<Material> ();
    private static Set<Material> treeBaseMaterials = new HashSet<Material> ();
    private static Set<Material> eligibleToolMaterials = new HashSet<Material> ();

    public static Set<String> getConfigKeys () {
        return new HashSet<String> (Arrays.asList (new String[] {
            TreeMaterialsKey,
            PassthroughMaterialsKey,
            TreeBaseMaterialsKey,
            EligibleToolMaterialsKey
        }));
    }

    public Material materialForString (String string) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        Material[] stuff = Material.class.getEnumConstants ();
        for (Material material : stuff) {
            if (material.toString().equals (string))
                return material;
        }

        logger.severe ("Bad configuration -- could not find material \"" + string + "\". This is a fatal error.");
        return null;
    }

    public void populateMaterialSet (Set<Material> materialsSet, List<String> materialsStringList) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        for (String str : materialsStringList) {
            Material material = materialForString (str);
            materialsSet.add (material);
        }
    }

    public void configure (Map<String,?> configuration) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        for (String key : configuration.keySet ()) {

            Object obj = configuration.get (key);
            if (obj == null) logger.severe (key);

            if (TreeMaterialsKey.equals (key)) {
                populateMaterialSet (treeMaterials, (List<String>)configuration.get (key));
            } else if (PassthroughMaterialsKey.equals (key)) {
                populateMaterialSet (passthroughMaterials, (List<String>)configuration.get (key));
            } else if (TreeBaseMaterialsKey.equals (key)) {
                populateMaterialSet (treeBaseMaterials, (List<String>)configuration.get (key));
            } else if (EligibleToolMaterialsKey.equals (key)) {
                populateMaterialSet (eligibleToolMaterials, (List<String>)configuration.get (key));
            }
        }
    }

    @EventHandler
    public final void onBlockBreak (BlockBreakEvent event) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        Block block = event.getBlock ();
        Material blockMaterial = block.getType ();

        if (treeMaterials.contains (blockMaterial)) {

            Player player = event.getPlayer ();
            ItemStack tool = player.getItemInHand();
            Material toolMaterial = tool.getType ();

            if (eligibleToolMaterials.contains (toolMaterial) && checkIfTreeTrunk (block)) {
                applyEffect(block, tool);
                updateTool (player, tool);
            }

        }
    }

    /**
     *
     * @param block the block in question
     * @return true if the block is considered to be the trunk of a tree, false otherwise
     */
    private static final boolean checkIfTreeTrunk (Block block) {

        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();
        Material material = block.getType();

        // go straight down until we find a dirt block
        while (treeMaterials.contains (material)) {
            block = block.getRelative(BlockFace.DOWN);
            material = block.getType();

            if (treeBaseMaterials.contains (material)) {
                return true;
            }
        }

        return false;
    }

    private static final void updateTool (Player player, ItemStack tool) {
        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();

        short durability = tool.getDurability ();
        short maxDurability = tool.getType().getMaxDurability ();

        if (durability == maxDurability) {
            player.getInventory().remove (tool);
        }
    }

    /**
     *
      * @param block the block the player has broken
     * @param tool the tool the player used to break the block
     */
    private static final void applyEffect (Block block, ItemStack tool) {

        LumberjacksDreamLogger logger = LumberjacksDreamLogger.sharedLogger ();
        Queue<Block> queue = new LinkedList<Block> ();
        Set<Block> exploredBlocks = new HashSet<Block> ();

        do {

            Material material = block.getType ();

            if (!treeMaterials.contains (material) && !passthroughMaterials.contains (material)) {
                // algorithm stops at non-log, non-leaves blocks
            }

            else {

                if (treeMaterials.contains (material)) {
                    // don't break blocks that the tool can't afford to break
                    if (tool.getDurability() < tool.getType().getMaxDurability ()) {
                        block.breakNaturally(tool);
                        tool.setDurability((short)(tool.getDurability() + 1));
                    }
                } else if (passthroughMaterials.contains (material)) {
                    //leave this block alone, but keep going
                }

                Block[] adjacentBlocks = {
                        block.getRelative(BlockFace.UP),
                        block.getRelative(BlockFace.NORTH),
                        block.getRelative(BlockFace.EAST),
                        block.getRelative(BlockFace.SOUTH),
                        block.getRelative(BlockFace.WEST)
                };

                // add in all blocks we haven't already explored
                for (Block adjacentBlock : adjacentBlocks) {
                    if (!exploredBlocks.contains (adjacentBlock)) {
                        exploredBlocks.add (adjacentBlock);
                        queue.add (adjacentBlock);
                    }
                }

            }

            block = queue.poll ();

        } while (block != null);
    }
}

