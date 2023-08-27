package net.minecraft.world.biome;

import com.xenon.util.PRNG;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenOcean extends BiomeGenBase
{
    public BiomeGenOcean(int id)
    {
        super(id);
        this.spawnableCreatureList.clear();
    }

    public BiomeGenBase.TempCategory getTempCategory()
    {
        return BiomeGenBase.TempCategory.OCEAN;
    }

    public void genTerrainBlocks(World worldIn, PRNG rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
