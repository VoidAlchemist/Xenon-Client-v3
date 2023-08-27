package com.xenon.modules.api;

import com.xenon.XenonClient;
import com.xenon.util.readability.Hook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class BetterFlyAPI {


    @Hook("net.minecraft.entity.EntityLivingBase#moveEntityWithHeading --> line 1366" +
            "net.minecraft.client.entity.EntityPlayerSP#onLivingUpdate --> line 719")
    public static boolean shouldUseBetterFly(EntityLivingBase entity) {
        Minecraft mc = Minecraft.getMinecraft();
        return XenonClient.instance.settings.betterFly &&
                mc.isSingleplayer() &&
                entity == mc.thePlayer &&
                entity == mc.getRenderViewEntity() && mc.thePlayer.capabilities.isFlying;
    }

    @Hook("net.minecraft.entity.EntityLivingBase#moveEntityWithHeading --> line 1367")
    public static void moveEntity(EntityLivingBase entity, float strafe, float forward) {
        float dist = strafe * strafe + forward * forward;

        double x = 0, y = 0, z = 0;
        if (dist >= 1.0E-4F) {
            dist = Math.max(MathHelper.sqrt_float(dist), 1);

            dist = 10 * entity.jumpMovementFactor / dist;
            strafe = strafe * dist;
            forward = forward * dist;
            float f1 = MathHelper.sin(entity.rotationYaw * (float) Math.PI / 180.0F);
            float f2 = MathHelper.cos(entity.rotationYaw * (float) Math.PI / 180.0F);
            x = strafe * f2 - forward * f1;
            z = forward * f2 + strafe * f1;
        }

        MovementInput mi = Minecraft.getMinecraft().thePlayer.movementInput;
        int flyboost = Math.max(XenonClient.instance.settings.flyBoost, 1);
        if (mi.sneak)
            y = -flyboost;
        if (mi.jump)
            y += flyboost;

        entity.moveEntity(x, y, z);
    }
}
