package net.minecraft.world.gen.feature;

import com.xenon.util.PRNG;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenFire extends WorldGenerator
{
    public boolean generate(World worldIn, PRNG rand, BlockPos position)
    {
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(blockpos) && worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.netherrack)
            {
                worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState(), 2);
            }
        }

        return true;
    }
}
