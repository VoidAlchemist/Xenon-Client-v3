package com.xenon.client.patches;

import net.minecraft.enchantment.*;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class patch for Enchantments, to prevent class loading deadlocks.
 * "Referencing subclass EnchantmentFishingSpeed from superclass Enchantment initializer might lead to class loading deadlock"
 * @author Zenon
 */
public class Enchants {

    public static final Enchantment protection,
            fireProtection,
            featherFalling,
            blastProtection,
            projectileProtection,
            respiration,
            aquaAffinity,
            thorns,
            depthStrider,
            sharpness,
            smite,
            baneOfArthropods,
            knockback,
            fireAspect,
            looting,
            efficiency,
            silkTouch,
            unbreaking,
            fortune,
            power,
            punch,
            flame,
            infinity,
            luckOfTheSea,
            lure;


    public static final Enchantment[] enchantmentsBookList;
    private static final Map<String, Enchantment> enchantmentsByResourceLocation;
    private static final Set<ResourceLocation> resourceLocations;

    static {
        enchantmentsBookList = new Enchantment[63];
        enchantmentsByResourceLocation = new HashMap<>(63);
        resourceLocations = new HashSet<>(63);


        String s = "protection";
        ResourceLocation r = new ResourceLocation(s);

        protection = new EnchantmentProtection(0, r, 10, 0);

        enchantmentsBookList[protection.effectId] = protection;
        enchantmentsByResourceLocation.put(s, protection);
        resourceLocations.add(r);


        s = "fire_protection";
        r = new ResourceLocation(s);

        fireProtection = new EnchantmentProtection(1, r, 5, 1);

        enchantmentsBookList[fireProtection.effectId] = fireProtection;
        enchantmentsByResourceLocation.put(s, fireProtection);
        resourceLocations.add(r);


        s = "feather_falling";
        r = new ResourceLocation(s);

        featherFalling = new EnchantmentProtection(2, r,5, 2);

        enchantmentsBookList[featherFalling.effectId] = featherFalling;
        enchantmentsByResourceLocation.put(s, featherFalling);
        resourceLocations.add(r);


        s = "blast_protection";
        r = new ResourceLocation(s);

        blastProtection = new EnchantmentProtection(3, r, 2, 3);

        enchantmentsBookList[blastProtection.effectId] = blastProtection;
        enchantmentsByResourceLocation.put(s, blastProtection);
        resourceLocations.add(r);


        s = "projectile_protection";
        r = new ResourceLocation(s);

        projectileProtection = new EnchantmentProtection(4, r, 5, 4);

        enchantmentsBookList[projectileProtection.effectId] = projectileProtection;
        enchantmentsByResourceLocation.put(s, projectileProtection);
        resourceLocations.add(r);


        s = "respiration";
        r = new ResourceLocation(s);

        respiration = new EnchantmentOxygen(5, r, 2);

        enchantmentsBookList[respiration.effectId] = respiration;
        enchantmentsByResourceLocation.put(s, respiration);
        resourceLocations.add(r);


        s = "aqua_affinity";
        r = new ResourceLocation(s);

        aquaAffinity = new EnchantmentWaterWorker(6, r, 2);

        enchantmentsBookList[aquaAffinity.effectId] = aquaAffinity;
        enchantmentsByResourceLocation.put(s, aquaAffinity);
        resourceLocations.add(r);


        s = "thorns";
        r = new ResourceLocation(s);

        thorns = new EnchantmentThorns(7, r, 1);

        enchantmentsBookList[thorns.effectId] = thorns;
        enchantmentsByResourceLocation.put(s, thorns);
        resourceLocations.add(r);


        s = "depth_strider";
        r = new ResourceLocation(s);

        depthStrider = new EnchantmentWaterWalker(8, r, 2);

        enchantmentsBookList[depthStrider.effectId] = depthStrider;
        enchantmentsByResourceLocation.put(s, depthStrider);
        resourceLocations.add(r);


        s = "sharpness";
        r = new ResourceLocation(s);

        sharpness = new EnchantmentDamage(16, r, 10, 0);

        enchantmentsBookList[sharpness.effectId] = sharpness;
        enchantmentsByResourceLocation.put(s, sharpness);
        resourceLocations.add(r);


        s = "smite";
        r = new ResourceLocation(s);

        smite = new EnchantmentDamage(17, r, 5, 1);

        enchantmentsBookList[smite.effectId] = smite;
        enchantmentsByResourceLocation.put(s, smite);
        resourceLocations.add(r);


        s = "bane_of_arthropods";
        r = new ResourceLocation(s);

        baneOfArthropods = new EnchantmentDamage(18, r, 5, 2);

        enchantmentsBookList[baneOfArthropods.effectId] = baneOfArthropods;
        enchantmentsByResourceLocation.put(s, baneOfArthropods);
        resourceLocations.add(r);


        s = "knockback";
        r = new ResourceLocation(s);

        knockback = new EnchantmentKnockback(19, r, 5);

        enchantmentsBookList[knockback.effectId] = knockback;
        enchantmentsByResourceLocation.put(s, knockback);
        resourceLocations.add(r);


        s = "fire_aspect";
        r = new ResourceLocation(s);

        fireAspect = new EnchantmentFireAspect(20, r, 2);

        enchantmentsBookList[fireAspect.effectId] = fireAspect;
        enchantmentsByResourceLocation.put(s, fireAspect);
        resourceLocations.add(r);


        s = "looting";
        r = new ResourceLocation(s);

        looting = new EnchantmentLootBonus(21, r, 2, EnumEnchantmentType.WEAPON);

        enchantmentsBookList[looting.effectId] = looting;
        enchantmentsByResourceLocation.put(s, looting);
        resourceLocations.add(r);


        s = "efficiency";
        r = new ResourceLocation(s);

        efficiency = new EnchantmentDigging(32, r, 10);

        enchantmentsBookList[efficiency.effectId] = efficiency;
        enchantmentsByResourceLocation.put(s, efficiency);
        resourceLocations.add(r);


        s = "silk_touch";
        r = new ResourceLocation(s);

        silkTouch = new EnchantmentUntouching(33, r, 1);

        enchantmentsBookList[silkTouch.effectId] = silkTouch;
        enchantmentsByResourceLocation.put(s, silkTouch);
        resourceLocations.add(r);


        s = "unbreaking";
        r = new ResourceLocation(s);

        unbreaking = new EnchantmentDurability(34, r, 5);

        enchantmentsBookList[unbreaking.effectId] = unbreaking;
        enchantmentsByResourceLocation.put(s, unbreaking);
        resourceLocations.add(r);


        s = "fortune";
        r = new ResourceLocation(s);

        fortune = new EnchantmentLootBonus(35, r, 2, EnumEnchantmentType.DIGGER);

        enchantmentsBookList[fortune.effectId] = fortune;
        enchantmentsByResourceLocation.put(s, fortune);
        resourceLocations.add(r);


        s = "power";
        r = new ResourceLocation(s);

        power = new EnchantmentArrowDamage(48, r, 10);

        enchantmentsBookList[power.effectId] = power;
        enchantmentsByResourceLocation.put(s, power);
        resourceLocations.add(r);


        s = "punch";
        r = new ResourceLocation(s);

        punch = new EnchantmentArrowKnockback(49, r, 2);

        enchantmentsBookList[punch.effectId] = punch;
        enchantmentsByResourceLocation.put(s, punch);
        resourceLocations.add(r);


        s = "flame";
        r = new ResourceLocation(s);

        flame = new EnchantmentArrowFire(50, r, 2);

        enchantmentsBookList[flame.effectId] = flame;
        enchantmentsByResourceLocation.put(s, flame);
        resourceLocations.add(r);


        s = "infinity";
        r = new ResourceLocation(s);

        infinity = new EnchantmentArrowInfinite(51, r, 1);

        enchantmentsBookList[infinity.effectId] = infinity;
        enchantmentsByResourceLocation.put(s, infinity);
        resourceLocations.add(r);


        s = "luck_of_the_sea";
        r = new ResourceLocation(s);

        luckOfTheSea = new EnchantmentLootBonus(61, r, 2, EnumEnchantmentType.FISHING_ROD);

        enchantmentsBookList[luckOfTheSea.effectId] = luckOfTheSea;
        enchantmentsByResourceLocation.put(s, luckOfTheSea);
        resourceLocations.add(r);


        s = "lure";
        r = new ResourceLocation(s);

        lure = new EnchantmentFishingSpeed(62, r, 2, EnumEnchantmentType.FISHING_ROD);

        enchantmentsBookList[lure.effectId] = lure;
        enchantmentsByResourceLocation.put(s, lure);
        resourceLocations.add(r);
    }


    public static Enchantment getEnchantmentByLocation(String location) {
        return enchantmentsByResourceLocation.get(location);
    }

    public static Enchantment getEnchantmentById(int enchID) {
        return enchID >= 0 && enchID < enchantmentsBookList.length ? enchantmentsBookList[enchID] : null;
    }

    public static Set<ResourceLocation> enchantmentResourceLocations() {
        return resourceLocations;
    }
}
