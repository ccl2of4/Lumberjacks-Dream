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

public final class LumberjacksDreamListener implements Listener {

    public static final String TreeMaterialsKey = "tree_materials";
    public static final String PassthroughMaterialsKey = "passthrough_materials";
    public static final String TreeBaseMaterialsKey = "tree_base_materials";
    public static final String EligibleToolMaterialsKey = "eligible_tool_materials";
    private static final Set<String> configKeys = new HashSet<String> (Arrays.asList (new String[] {
            TreeMaterialsKey,
            PassthroughMaterialsKey,
            TreeBaseMaterialsKey,
            EligibleToolMaterialsKey
    }));

    private JavaPluginLogger logger;
    private Set<Material> treeMaterials = new HashSet<Material> ();
    private Set<Material> passthroughMaterials = new HashSet<Material> ();
    private Set<Material> treeBaseMaterials = new HashSet<Material> ();
    private Set<Material> eligibleToolMaterials = new HashSet<Material> ();

    /**
     *
     * @return a set of the configuration keys necessary for proper configuration of this plugin
     */
    public static Set<String> getConfigKeys () {
        return configKeys;
    }

    public JavaPluginLogger getLogger () { return logger; }
    public void setLogger (JavaPluginLogger logger) { this.logger = logger; }

    /**
     * Configure the behavior of this instance
     * @param configuration Key,value pairs. Should contain all keys returned by getConfigKeys()
     */
    public void configure (Map<String,?> configuration) {
        for (String key : configuration.keySet ()) {

            Object val = configuration.get (key);

            if (TreeMaterialsKey.equals (key)) {
                treeMaterials = createMaterialSet((List<String>)val);
            } else if (PassthroughMaterialsKey.equals (key)) {
                passthroughMaterials = createMaterialSet ((List<String>)val);
            } else if (TreeBaseMaterialsKey.equals (key)) {
                treeBaseMaterials = createMaterialSet ((List<String>)val);
            } else if (EligibleToolMaterialsKey.equals (key)) {
                eligibleToolMaterials = createMaterialSet ((List<String>)val);
            }
        }
    }

    /**
     * Checks to see if a the trunk of a tree is being cut down. If so, checks if the player
     * used a tool that will trigger this plugin to do its work
     * @param event
     */
    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
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
    private boolean checkIfTreeTrunk (Block block) {
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

    /**
     * Checks if tool should be destroyed, and destroys it if necessary
     * @param player holding the tool
     * @param tool in question
     */
    private void updateTool (Player player, ItemStack tool) {
        short durability = tool.getDurability ();
        short maxDurability = tool.getType().getMaxDurability ();

        if (durability == maxDurability) {
            player.getInventory().remove (tool);
        }
    }

    /**
     * Main logic of the plugin. Iterative breadth-first-search to destroy log blocks
     * @param block the block the player has broken
     * @param tool the tool the player used to break the block
     */
    private void applyEffect (Block block, ItemStack tool) {
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

    /**
     * Uses java reflection to find the corresponding Material enum for the given string
     * @param string the string representing the Material enum
     * @return
     */
    private static Material materialForString (String string) {
        Material[] stuff = Material.class.getEnumConstants ();
        for (Material material : stuff) {
            if (material.toString().equals (string))
                return material;
        }
        return null;
    }

    /**
     * Given a list of strings, finds the corresponding Material enums and returns them in a set
     * @param materialsStringList the list of strings named after Material enums
     */
    private Set<Material> createMaterialSet (List<String> materialsStringList) {
        Set<Material> result = new HashSet<Material> ();
        for (String string : materialsStringList) {
            Material material = materialForString (string);
            if (material != null) {
                result.add (material);
            } else {
                logger.warning ("Bad configuration -- could not find material \"" + string + "\".");
            }
        }
        return result;
    }
}

