package net.minecraft.block;

import com.xenon.util.PRNG;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IGrowable {
    /**
     * Whether this IGrowable can grow
     */
    boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient);

    boolean canUseBonemeal(World worldIn, PRNG rand, BlockPos pos, IBlockState state);

    void grow(World worldIn, PRNG rand, BlockPos pos, IBlockState state);
}
