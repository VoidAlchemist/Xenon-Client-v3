package net.optifine.config;

import com.xenon.client.patches.Enchants;
import net.minecraft.enchantment.Enchantment;

public class ParserEnchantmentId implements IParserInt {
    public int parse(String str, int defVal) {
        Enchantment enchantment = Enchants.getEnchantmentByLocation(str);
        return enchantment == null ? defVal : enchantment.effectId;
    }
}
