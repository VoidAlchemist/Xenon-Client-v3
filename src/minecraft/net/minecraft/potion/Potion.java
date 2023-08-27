package net.minecraft.potion;

import com.google.common.collect.Maps;
import com.xenon.client.patches.Potions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class Potion {

    /**
     * The Id of a Potion object.
     */
    public final int id;
    private final Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();

    /**
     * This field indicated if the effect is 'bad' - negative - for the entity.
     */
    private final boolean isBadEffect;

    /**
     * Is the color of the liquid for this potion.
     */
    private final int liquidColor;

    /**
     * The name of the Potion.
     */
    private String name = "";

    /**
     * The index for the icon displayed when the potion effect is active.
     */
    private int statusIconIndex = -1;
    private double effectiveness;
    private boolean usable;

    public Potion(int potionID, ResourceLocation location, boolean badEffect, int potionColor) {
        this.id = potionID;
        this.isBadEffect = badEffect;

        effectiveness = badEffect ? 0.5 : 1.0;

        this.liquidColor = potionColor;
    }


    public static String getDurationString(PotionEffect effect) {
        if (effect.getIsPotionDurationMax()) {
            return "**:**";
        } else {
            int i = effect.getDuration();
            return StringUtils.ticksToElapsedTime(i);
        }
    }

    /**
     * Sets the index for the icon displayed in the player's inventory when the status is active.
     */
    public Potion setIconIndex(int p_76399_1_, int p_76399_2_) {
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }

    /**
     * returns the ID of the potion
     */
    public int getId() {
        return this.id;
    }

    public void performEffect(EntityLivingBase entity, int p_76394_2_) {
        if (this.id == Potions.regeneration.id) {
            if (entity.getHealth() < entity.getMaxHealth())
                entity.heal(1.0F);

        } else if (this.id == Potions.poison.id) {
            if (entity.getHealth() > 1.0F)
                entity.attackEntityFrom(DamageSource.magic, 1.0F);

        } else if (this.id == Potions.wither.id) {
            entity.attackEntityFrom(DamageSource.wither, 1.0F);

        } else if (this.id == Potions.hunger.id && entity instanceof EntityPlayer) {
            ((EntityPlayer) entity).addExhaustion(0.025F * (float) (p_76394_2_ + 1));

        } else if (this.id == Potions.saturation.id && entity instanceof EntityPlayer) {
            if (!entity.worldObj.isRemote)
                ((EntityPlayer) entity).getFoodStats().addStats(p_76394_2_ + 1, 1.0F);

        } else if ((this.id != Potions.heal.id || entity.isEntityUndead()) &&
                (this.id != Potions.harm.id || !entity.isEntityUndead())) {
            if (this.id == Potions.harm.id && !entity.isEntityUndead() ||
                    this.id == Potions.heal.id && entity.isEntityUndead())
                entity.attackEntityFrom(DamageSource.magic, (float) (6 << p_76394_2_));

        } else entity.heal((float) Math.max(4 << p_76394_2_, 0));
    }

    public void affectEntity(Entity attacker,
                             Entity indirectEntity,
                             EntityLivingBase entityLivingBaseIn,
                             int p_180793_4_, double p_180793_5_) {
        if (id != Potions.harm.id && id != Potions.heal.id) return;

        if ( (id == Potions.heal.id && !entityLivingBaseIn.isEntityUndead()) ||
                (id == Potions.harm.id && entityLivingBaseIn.isEntityUndead()))
            entityLivingBaseIn.heal((int) (p_180793_5_ * (double)(4 << p_180793_4_) + 0.5));

        else entityLivingBaseIn.attackEntityFrom(
                attacker == null ? DamageSource.magic : DamageSource.causeIndirectMagicDamage(attacker, indirectEntity),
                (int) (p_180793_5_ * (double) (6 << p_180793_4_) + 0.5));

    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int p_76397_1_, int p_76397_2_) {

        if (this.id == Potions.regeneration.id) {
            int k = 50 >> p_76397_2_;
            return k == 0 || p_76397_1_ % k == 0;
        } else if (this.id == Potions.poison.id) {
            int j = 25 >> p_76397_2_;
            return j == 0 || p_76397_1_ % j == 0;
        } else if (this.id == Potions.wither.id) {
            int i = 40 >> p_76397_2_;
            return i == 0 || p_76397_1_ % i == 0;
        } else {
            return this.id == Potions.hunger.id;
        }
    }

    /**
     * Set the potion name.
     */
    public Potion setPotionName(String nameIn) {
        this.name = nameIn;
        return this;
    }

    /**
     * returns the name of the potion
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns true if the potion has a associated status icon to display in then inventory when active.
     */
    public boolean hasStatusIcon() {
        return this.statusIconIndex >= 0;
    }

    /**
     * Returns the index for the icon to display when the potion is active.
     */
    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }

    /**
     * This method returns true if the potion effect is bad - negative - for the entity.
     */
    public boolean isBadEffect() {
        return this.isBadEffect;
    }

    public double getEffectiveness() {
        return this.effectiveness;
    }

    public Potion setEffectiveness(double effectivenessIn) {
        this.effectiveness = effectivenessIn;
        return this;
    }

    public boolean isUsable() {
        return this.usable;
    }

    /**
     * Returns the color of the potion liquid.
     */
    public int getLiquidColor() {
        return this.liquidColor;
    }

    /**
     * Used by potions to register the attribute they modify.
     */
    public Potion registerPotionAttributeModifier(IAttribute p_111184_1_, String p_111184_2_, double p_111184_3_, int p_111184_5_) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_111184_2_), this.getName(), p_111184_3_, p_111184_5_);
        this.attributeModifierMap.put(p_111184_1_, attributemodifier);
        return this;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111187_2_, int amplifier) {
        for (Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            IAttributeInstance iattributeinstance = p_111187_2_.getAttributeInstance((IAttribute) entry.getKey());

            if (iattributeinstance != null) {
                iattributeinstance.removeModifier((AttributeModifier) entry.getValue());
            }
        }
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111185_2_, int amplifier) {
        for (Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet()) {
            IAttributeInstance iattributeinstance = p_111185_2_.getAttributeInstance((IAttribute) entry.getKey());

            if (iattributeinstance != null) {
                AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
                iattributeinstance.removeModifier(attributemodifier);
                iattributeinstance.applyModifier(new AttributeModifier(attributemodifier.getID(), this.getName() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.getOperation()));
            }
        }
    }

    public double getAttributeModifierAmount(int p_111183_1_, AttributeModifier modifier) {
        return modifier.getAmount() * (double) (p_111183_1_ + 1);
    }
}
