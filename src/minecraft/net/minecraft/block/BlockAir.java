package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.IdentityHashMap;
import java.util.Map;

public class BlockAir extends Block {
    private static Map<Block, Integer> mapOriginalOpacity = new IdentityHashMap<>();

    protected BlockAir() {
        super(Material.air);
    }

    public static void setLightOpacity(Block block, int lightOpacity) {
        if (!mapOriginalOpacity.containsKey(block))
            mapOriginalOpacity.put(block, block.lightOpacity);

        block.lightOpacity = lightOpacity;
    }

    public static void restoreLightOpacity(Block block) {
        if (mapOriginalOpacity.containsKey(block))
            block.lightOpacity = mapOriginalOpacity.get(block);
    }

    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public int getRenderType() {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return false;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }
}
