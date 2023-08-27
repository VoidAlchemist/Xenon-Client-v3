package com.xenon.client.patches;

import com.xenon.util.readability.Hook;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Ghost liquid blocks fixed.
 *
 * @author Zenon
 */
public class LiquidPlacingFix {

    /**
     * {@link net.minecraft.item.ItemBucket#tryPlaceContainedLiquid(World, BlockPos)} does <code>world.setBlockState()</code> without checking if
     * the world is remote, thus resulting in client side ghost blocks. Simply wrapped the setBlockState call with a <code>!isRemote</code> check.
     *
     */
    @Hook("net.minecraft.item.ItemBucket#onItemRightClick -> line 92")
    public static boolean tryPlaceLiquidFixed(ItemBucket bucket, World world, BlockPos pos) {
        if (bucket.isFull == Blocks.air) {
            return false;
        } else {
            Material material = world.getBlockState(pos).getBlock().getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(pos) && !flag) {
                return false;
            } else {
                if (world.provider.doesWaterVaporize() && bucket.isFull == Blocks.flowing_water) {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    world.playSoundEffect(
                            (float) x + 0.5F,
                            (float) y + 0.5F,
                            (float) z + 0.5F,
                            "random.fizz",
                            0.5F,
                            2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
                    );

                    for (int l = 0; l < 8; ++l)
                        world.spawnParticle(
                                EnumParticleTypes.SMOKE_LARGE,
                                (double) x + world.rand.nextDouble(),
                                (double) y + world.rand.nextDouble(),
                                (double) z + world.rand.nextDouble(),
                                0, 0, 0
                        );

                } else {
                    if (!world.isRemote) {
                        if (flag && !material.isLiquid())
                            world.destroyBlock(pos, true);
                        world.setBlockState(pos, bucket.isFull.getDefaultState(), 3);
                    }
                }

                return true;
            }
        }
    }

}
