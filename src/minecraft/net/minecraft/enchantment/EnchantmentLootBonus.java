package net.minecraft.enchantment;

import com.xenon.client.patches.Enchants;
import net.minecraft.util.ResourceLocation;

public class EnchantmentLootBonus extends Enchantment {
    public EnchantmentLootBonus(int id, ResourceLocation loc, int weight, EnumEnchantmentType type) {
        super(id, loc, weight, type);

        if (type == EnumEnchantmentType.DIGGER)
            this.setName("lootBonusDigger");

        else if (type == EnumEnchantmentType.FISHING_ROD)
            this.setName("lootBonusFishing");

        else
            this.setName("lootBonus");
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 3;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && ench.effectId != Enchants.silkTouch.effectId;
    }
}
