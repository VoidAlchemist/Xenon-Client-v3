package net.minecraft.enchantment;

import com.xenon.util.PRNG;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDurability extends Enchantment {
    public EnchantmentDurability(int enchID, ResourceLocation enchName, int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.BREAKABLE);
        this.setName("durability");
    }

    /**
     * Used by ItemStack.attemptDamageItem. Randomly determines if a point of damage should be negated using the
     * enchantment level (par1).
     */
    public static boolean negateDamage(ItemStack stack, int level, PRNG rand) {
        return stack.getItem() instanceof ItemArmor && rand.nextFloat() < 0.6F ? false : rand.nextInt(level + 1) > 0;
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 8;
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
     * Determines if this enchantment can be applied to a specific ItemStack.
     */
    public boolean canApply(ItemStack stack) {
        return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }
}
