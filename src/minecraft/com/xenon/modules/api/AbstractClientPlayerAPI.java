package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.SharedMonsterAttributes;

/**
 * @author Zenon
 * @see net.minecraft.client.entity.AbstractClientPlayer
 */
public class AbstractClientPlayerAPI {

    /**
     * Allows one to bypass speed modifying FOV.
     *
     * @param player the target
     * @return the new FOV modifier
     */
    @Hook("net.minecraft.client.entity.AbstractClientPlayer#getFOVModifier() -> line 156")
    public static float getFOVModifier(AbstractClientPlayer player) {
        if (!XenonClient.instance.settings.speedFOV) {
            if (player.isSprinting())
                return player.capabilities.isFlying ? 1.26500000655651F : 1.1500000059604645F;

            return player.capabilities.isFlying ? 1.1f : 1f;
        }

        float f = player.capabilities.isFlying ? 1.1f : 1f;
        return (float) ((double) f * ((player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() /
                (double) player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
    }

}
