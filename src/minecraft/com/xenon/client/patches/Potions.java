package com.xenon.client.patches;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class patch for potions, to prevent class loading deadlocks.
 * @author Zenon
 */
public class Potions {

    public static final Potion moveSpeed;
    public static final Potion moveSlowdown;
    public static final Potion digSpeed;
    public static final Potion digSlowdown;
    public static final Potion damageBoost;
    public static final Potion heal;
    public static final Potion harm;
    public static final Potion jump;
    public static final Potion confusion;
    public static final Potion regeneration;
    public static final Potion resistance;
    public static final Potion fireResistance;
    public static final Potion waterBreathing;
    public static final Potion invisibility;
    public static final Potion blindness;
    public static final Potion nightVision;
    public static final Potion hunger;
    public static final Potion weakness;
    public static final Potion poison;
    public static final Potion wither;
    public static final Potion healthBoost;
    public static final Potion absorption;
    public static final Potion saturation;


    // first slot is empty since genius mc code decided to start potion IDs at 1 instead of 0
    public static final Potion[] potionTypes;
    private static final Set<ResourceLocation> potionResourceLocations;
    private static final Map<String, Potion> potionsByLocation;

    static {
        potionTypes = new Potion[24];   // unused slot at the 0
        potionResourceLocations = new HashSet<>(23);
        potionsByLocation = new HashMap<>(23);


        String s = "speed";
        ResourceLocation rl = new ResourceLocation(s);
        potionTypes[1] = moveSpeed = new Potion(1,
                rl,
                false,
                8171462)
                .setPotionName("potion.moveSpeed")
                .setIconIndex(0, 0)
                .registerPotionAttributeModifier(
                        SharedMonsterAttributes.movementSpeed,
                        "91AEAA56-376B-4498-935B-2F7F68070635",
                        0.20000000298023224D, 2);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, moveSpeed);


        s = "slowness";
        rl = new ResourceLocation(s);
        potionTypes[2] = moveSlowdown = new Potion(2,
                rl,
                true,
                5926017)
                .setPotionName("potion.moveSlowdown")
                .setIconIndex(1, 0)
                .registerPotionAttributeModifier(
                        SharedMonsterAttributes.movementSpeed,
                        "7107DE5E-7CE8-4030-940E-514C1F160890",
                        -0.15000000596046448D, 2);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, moveSlowdown);


        s = "haste";
        rl = new ResourceLocation(s);
        potionTypes[3] = digSpeed = new Potion(3,
                rl,
                false,
                14270531)
                .setPotionName("potion.digSpeed")
                .setIconIndex(2, 0)
                .setEffectiveness(1.5D);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, digSpeed);

        s = "mining_fatigue";
        rl = new ResourceLocation(s);
        potionTypes[4] = digSlowdown = new Potion(4,
                rl,
                true,
                4866583)
                .setPotionName("potion.digSlowDown")
                .setIconIndex(3, 0);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, digSlowdown);

        s = "strength";
        rl = new ResourceLocation(s);
        potionTypes[5] = damageBoost = new PotionAttackDamage(5,
                rl,
                false,
                9643043)
                .setPotionName("potion.damageBoost")
                .setIconIndex(4, 0)
                .registerPotionAttributeModifier(
                        SharedMonsterAttributes.attackDamage,
                        "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9",
                        2.5D, 2);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, damageBoost);

        s = "instant_health";
        rl = new ResourceLocation(s);
        potionTypes[6] = heal = new PotionHealth(6,
                rl,
                false,
                16262179)
                .setPotionName("potion.heal");

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, heal);


        s = "instant_damage";
        rl = new ResourceLocation(s);
        potionTypes[7] = harm = new PotionHealth(7,
                rl,
                true,
                4393481)
                .setPotionName("potion.harm");

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, harm);


        s = "jump_boost";
        rl = new ResourceLocation(s);
        potionTypes[8] = jump = new Potion(8,
                rl,
                false,
                2293580)
                .setPotionName("potion.jump")
                .setIconIndex(2, 1);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, jump);

        s = "nausea";
        rl = new ResourceLocation(s);
        potionTypes[9] = confusion = new Potion(9,
                rl,
                true,
                5578058)
                .setPotionName("potion.confusion")
                .setIconIndex(3, 1)
                .setEffectiveness(0.25D);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, confusion);

        s = "regeneration";
        rl = new ResourceLocation(s);
        potionTypes[10] = regeneration = new Potion(10,
                rl,
                false,
                13458603)
                .setPotionName("potion.regeneration")
                .setIconIndex(7, 0)
                .setEffectiveness(0.25D);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, regeneration);

        s = "resistance";
        rl = new ResourceLocation(s);
        potionTypes[11] = resistance = new Potion(11,
                rl,
                false,
                10044730)
                .setPotionName("potion.resistance")
                .setIconIndex(6, 1);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, resistance);



        s = "fire_resistance";
        rl = new ResourceLocation(s);
        potionTypes[12] = fireResistance = new Potion(12,
                rl,
                false,
                14981690)
                .setPotionName("potion.fireResistance")
                .setIconIndex(7, 1);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, fireResistance);


        s = "water_breathing";
        rl = new ResourceLocation(s);
        potionTypes[13] = waterBreathing = new Potion(13,
                rl,
                false,
                3035801)
                .setPotionName("potion.waterBreathing")
                .setIconIndex(0, 2);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, waterBreathing);

        s = "invisibility";
        rl = new ResourceLocation(s);
        potionTypes[14] = invisibility = new Potion(14,
                rl,
                false,
                8356754)
                .setPotionName("potion.invisibility")
                .setIconIndex(0, 1);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, invisibility);


        s = "blindness";
        rl = new ResourceLocation(s);
        potionTypes[15] = blindness = new Potion(15,
                rl,
                true,
                2039587)
                .setPotionName("potion.blindness")
                .setIconIndex(5, 1)
                .setEffectiveness(0.25D);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, blindness);


        s = "night_vision";
        rl = new ResourceLocation(s);
        potionTypes[16] = nightVision = new Potion(16,
                rl,
                false,
                2039713)
                .setPotionName("potion.nightVision")
                .setIconIndex(4, 1);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, nightVision);

        s = "hunger";
        rl = new ResourceLocation(s);
        potionTypes[17] = hunger = new Potion(17,
                rl,
                true,
                5797459)
                .setPotionName("potion.hunger")
                .setIconIndex(1, 1);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, hunger);


        s = "weakness";
        rl = new ResourceLocation(s);
        potionTypes[18] = weakness = new PotionAttackDamage(18,
                rl,
                true,
                4738376)
                .setPotionName("potion.weakness")
                .setIconIndex(5, 0)
                .registerPotionAttributeModifier(
                        SharedMonsterAttributes.attackDamage,
                        "22653B89-116E-49DC-9B6B-9971489B5BE5",
                        2.0D, 0);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, weakness);


        s = "poison";
        rl = new ResourceLocation(s);
        potionTypes[19] = poison = new Potion(19,
                rl,
                true,
                5149489)
                .setPotionName("potion.poison")
                .setIconIndex(6, 0)
                .setEffectiveness(0.25D);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, poison);


        s = "wither";
        rl = new ResourceLocation(s);
        potionTypes[20] = wither = new Potion(20,
                rl,
                true,
                3484199)
                .setPotionName("potion.wither")
                .setIconIndex(1, 2)
                .setEffectiveness(0.25D);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, wither);


        s = "health_boost";
        rl = new ResourceLocation(s);
        potionTypes[21] = healthBoost = new PotionHealthBoost(21,
                rl,
                false,
                16284963)
                .setPotionName("potion.healthBoost")
                .setIconIndex(2, 2)
                .registerPotionAttributeModifier(
                        SharedMonsterAttributes.maxHealth,
                        "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC",
                        4.0D, 0);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, healthBoost);


        s = "absorption";
        rl = new ResourceLocation(s);
        potionTypes[22] = absorption = new PotionAbsorption(22,
                rl,
                false,
                2445989)
                .setPotionName("potion.absorption")
                .setIconIndex(2, 2);

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, absorption);


        s = "saturation";
        rl = new ResourceLocation(s);
        potionTypes[23] = saturation = new PotionHealth(23,
                rl,
                false,
                16262179)
                .setPotionName("potion.saturation");

        potionResourceLocations.add(rl);
        potionsByLocation.put(s, saturation);
    }


    public static Potion getPotionFromResourceLocation(String loc) {
        return potionsByLocation.get(loc);
    }

    public static Set<ResourceLocation> getPotionLocations() {
        return potionResourceLocations;
    }


}
