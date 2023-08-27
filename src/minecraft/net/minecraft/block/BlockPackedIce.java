package net.minecraft.block;

import com.xenon.util.PRNG;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce extends Block {
    public BlockPackedIce() {
        super(Material.packedIce);
        this.slipperiness = 0.98F;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(PRNG random) {
        return 0;
    }
}
